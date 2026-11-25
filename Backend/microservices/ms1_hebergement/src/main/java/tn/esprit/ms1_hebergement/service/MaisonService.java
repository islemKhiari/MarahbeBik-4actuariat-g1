package tn.esprit.ms1_hebergement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms1_hebergement.dto.MaisonRequest;
import tn.esprit.ms1_hebergement.dto.MaisonResponse;
import tn.esprit.ms1_hebergement.entity.MaisonHote;
import tn.esprit.ms1_hebergement.exception.RessourceIntrouvableException;
import tn.esprit.ms1_hebergement.mapper.Mapper;
import tn.esprit.ms1_hebergement.repository.MaisonRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaisonService {

    private final MaisonRepository maisonRepository;

    // READ : toutes les maisons, ou recherche avec filtres optionnels
    @Transactional(readOnly = true)
    public List<MaisonResponse> lister(String ville, Integer capacite, BigDecimal budgetMax) {
        List<MaisonHote> maisons;
        if (ville == null && capacite == null && budgetMax == null) {
            maisons = maisonRepository.findAll();   // sans filtre : même les maisons sans chambre
        } else {
            maisons = maisonRepository.rechercher(ville, capacite, budgetMax);
        }
        return maisons.stream().map(Mapper::toMaisonResponse).toList();
    }

    // READ : détail d'une maison
    @Transactional(readOnly = true)
    public MaisonResponse trouver(Long id) {
        return Mapper.toMaisonResponse(chercher(id));
    }

    // READ : les maisons d'un propriétaire
    @Transactional(readOnly = true)
    public List<MaisonResponse> listerParProprietaire(String proprietaireId) {
        return maisonRepository.findByProprietaireId(proprietaireId).stream()
                .map(Mapper::toMaisonResponse)
                .toList();
    }

    // CREATE
    public MaisonResponse creer(MaisonRequest request) {
        MaisonHote maison = Mapper.toEntity(request);
        return Mapper.toMaisonResponse(maisonRepository.save(maison));
    }

    // UPDATE (pas de save() : JPA détecte les changements dans la transaction)
    public MaisonResponse modifier(Long id, MaisonRequest request) {
        MaisonHote maison = chercher(id);
        Mapper.updateEntity(maison, request);
        return Mapper.toMaisonResponse(maison);
    }

    // DELETE : supprime aussi ses chambres (cascade)
    public void supprimer(Long id) {
        maisonRepository.delete(chercher(id));
    }

    // Utilisé aussi par ChambreService
    public MaisonHote chercher(Long id) {
        return maisonRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Maison introuvable : " + id));
    }
}