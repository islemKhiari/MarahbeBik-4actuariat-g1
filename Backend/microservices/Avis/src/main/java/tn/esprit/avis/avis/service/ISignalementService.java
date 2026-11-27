package tn.esprit.avis.avis.service;

import tn.esprit.avis.avis.dto.SignalementRequestDTO;
import tn.esprit.avis.avis.dto.SignalementResponseDTO;
import tn.esprit.avis.avis.entity.StatutSignalement;

import java.util.List;

public interface ISignalementService {
    SignalementResponseDTO signalerAvis(Long avisId, SignalementRequestDTO dto);
    List<SignalementResponseDTO> getAllSignalements();
    List<SignalementResponseDTO> getSignalementsParAvis(Long avisId);
    List<SignalementResponseDTO> getSignalementsParStatut(StatutSignalement statut);
    SignalementResponseDTO traiterSignalement(Long signalementId, StatutSignalement nouveauStatut);
    void supprimerSignalement(Long signalementId);
}