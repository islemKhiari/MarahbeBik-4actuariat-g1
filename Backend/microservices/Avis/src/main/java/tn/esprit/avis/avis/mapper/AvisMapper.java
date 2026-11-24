package tn.esprit.avis.avis.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.avis.avis.dto.*;
import tn.esprit.avis.avis.entity.Avis;
import tn.esprit.avis.avis.entity.Signalement;

@Component
public class AvisMapper {

    public Avis toEntity(AvisRequestDTO dto) {
        return Avis.builder()
                .reservationId(dto.getReservationId())
                .voyageurId(dto.getVoyageurId())
                .proprietaireId(dto.getProprietaireId())
                .note(dto.getNote())
                .commentaire(dto.getCommentaire())
                .build();
    }

    public AvisResponseDTO toDTO(Avis avis) {
        return AvisResponseDTO.builder()
                .id(avis.getId())
                .reservationId(avis.getReservationId())
                .voyageurId(avis.getVoyageurId())
                .maisonId(avis.getMaisonId())
                .proprietaireId(avis.getProprietaireId())
                .note(avis.getNote())
                .commentaire(avis.getCommentaire())
                .statut(avis.getStatut())
                .dateCreation(avis.getDateCreation())
                .dateModification(avis.getDateModification())
                .reponseContenu(avis.getReponseContenu())
                .dateReponse(avis.getDateReponse())
                .build();
    }

    public SignalementResponseDTO toSignalementDTO(Signalement s) {
        return SignalementResponseDTO.builder()
                .id(s.getId())
                .avisId(s.getAvis().getId())
                .signaleurId(s.getSignaleurId())
                .motif(s.getMotif())
                .statut(s.getStatut())
                .dateSignalement(s.getDateSignalement())
                .build();
    }
}