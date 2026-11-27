package tn.esprit.ms1_hebergement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms1_hebergement.dto.ChambreRequest;
import tn.esprit.ms1_hebergement.dto.ChambreResponse;
import tn.esprit.ms1_hebergement.entity.Chambre;
import tn.esprit.ms1_hebergement.entity.Equipement;
import tn.esprit.ms1_hebergement.entity.MaisonHote;
import tn.esprit.ms1_hebergement.exception.RequeteInvalideException;
import tn.esprit.ms1_hebergement.exception.RessourceIntrouvableException;
import tn.esprit.ms1_hebergement.mapper.Mapper;
import tn.esprit.ms1_hebergement.repository.ChambreRepository;
import tn.esprit.ms1_hebergement.repository.EquipementRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ChambreService {

    private final ChambreRepository chambreRepository;
    private final EquipementRepository equipementRepository;
    private final MaisonService maisonService;

    @Transactional(readOnly = true)
    public List<ChambreResponse> listerParMaison(Long maisonId) {
        maisonService.chercher(maisonId); // 404 si la maison n'existe pas
        return chambreRepository.findByMaisonId(maisonId).stream()
                .map(Mapper::toChambreResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChambreResponse trouver(Long id) {
        return Mapper.toChambreResponse(chercher(id));
    }

    public ChambreResponse creer(Long maisonId, ChambreRequest request) {
        MaisonHote maison = maisonService.chercher(maisonId);
        verifierDates(request);

        Chambre chambre = Mapper.toEntity(request);
        chambre.setMaison(maison);
        chambre.setEquipements(chargerEquipements(request.equipementIds()));
        return Mapper.toChambreResponse(chambreRepository.save(chambre));
    }

    public ChambreResponse modifier(Long id, ChambreRequest request) {
        Chambre chambre = chercher(id);
        verifierDates(request);

        Mapper.updateEntity(chambre, request);
        chambre.setEquipements(chargerEquipements(request.equipementIds()));
        return Mapper.toChambreResponse(chambre);
    }

    public void supprimer(Long id) {
        Chambre chambre = chercher(id);
        // orphanRemoval=true sur MaisonHote.chambres : la retirer de la maison la supprime en base
        chambre.getMaison().retirerChambre(chambre);
    }

    private Chambre chercher(Long id) {
        return chambreRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Chambre introuvable : " + id));
    }

    private void verifierDates(ChambreRequest request) {
        if (request.dateDebut() != null && request.dateFin() != null
                && request.dateFin().isBefore(request.dateDebut())) {
            throw new RequeteInvalideException("dateFin doit être après dateDebut");
        }
    }

    // Transforme les ids reçus en équipements existants ; 404 si l'un d'eux n'existe pas
    private Set<Equipement> chargerEquipements(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<Equipement> trouves = equipementRepository.findAllById(ids);
        if (trouves.size() != ids.size()) {
            throw new RessourceIntrouvableException("Un ou plusieurs équipements sont introuvables");
        }
        return new HashSet<>(trouves);
    }
}