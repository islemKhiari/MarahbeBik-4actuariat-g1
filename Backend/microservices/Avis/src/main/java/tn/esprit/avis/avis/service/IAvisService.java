package tn.esprit.avis.avis.service;

import tn.esprit.avis.avis.dto.*;
import tn.esprit.avis.avis.entity.StatutAvis;
import java.util.List;

public interface IAvisService {
    AvisResponseDTO creerAvis(AvisRequestDTO dto);
    AvisResponseDTO modifierAvis(Long avisId, AvisUpdateDTO dto);
    void supprimerAvis(Long avisId);
    AvisResponseDTO getAvisById(Long id);
    List<AvisResponseDTO> getAllAvis();
    List<AvisResponseDTO> getAvisPubliesParMaison(Long maisonId);
    Double getMoyenneNoteMaison(Long maisonId);
    List<AvisResponseDTO> getAvisParVoyageur(String voyageurId);
    List<AvisResponseDTO> getAvisParProprietaire(String proprietaireId);
    AvisResponseDTO changerStatutAvis(Long avisId, StatutAvis nouveauStatut);
    AvisResponseDTO repondreAvis(Long avisId, ReponseRequestDTO dto);
}