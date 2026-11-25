package tn.esprit.ms1_hebergement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms1_hebergement.dto.EquipementRequest;
import tn.esprit.ms1_hebergement.dto.EquipementResponse;
import tn.esprit.ms1_hebergement.entity.Chambre;
import tn.esprit.ms1_hebergement.entity.Equipement;
import tn.esprit.ms1_hebergement.exception.RequeteInvalideException;
import tn.esprit.ms1_hebergement.exception.RessourceIntrouvableException;
import tn.esprit.ms1_hebergement.mapper.Mapper;
import tn.esprit.ms1_hebergement.repository.EquipementRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementService {

    private final EquipementRepository equipementRepository;

    @Transactional(readOnly = true)
    public List<EquipementResponse> lister() {
        return equipementRepository.findAll().stream()
                .map(Mapper::toEquipementResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipementResponse trouver(Long id) {
        return Mapper.toEquipementResponse(chercher(id));
    }

    public EquipementResponse creer(EquipementRequest request) {
        if (equipementRepository.existsByType(request.type())) {
            throw new RequeteInvalideException("Cet équipement existe déjà : " + request.type());
        }
        Equipement equipement = Mapper.toEntity(request);
        return Mapper.toEquipementResponse(equipementRepository.save(equipement));
    }

    public EquipementResponse modifier(Long id, EquipementRequest request) {
        Equipement equipement = chercher(id);
        if (!equipement.getType().equals(request.type())
                && equipementRepository.existsByType(request.type())) {
            throw new RequeteInvalideException("Cet équipement existe déjà : " + request.type());
        }
        Mapper.updateEntity(equipement, request);
        return Mapper.toEquipementResponse(equipement);
    }

    public void supprimer(Long id) {
        Equipement equipement = chercher(id);
        // Chambre est le côté propriétaire de la relation : on détache l'équipement
        // de chaque chambre avant de le supprimer, sinon la clé étrangère bloque.
        for (Chambre chambre : new ArrayList<>(equipement.getChambres())) {
            chambre.getEquipements().remove(equipement);
        }
        equipementRepository.delete(equipement);
    }

    private Equipement chercher(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Équipement introuvable : " + id));
    }
}