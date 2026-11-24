// SignalementResponseDTO.java
package tn.esprit.avis.avis.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.avis.avis.entity.StatutSignalement;
import java.time.LocalDateTime;

@Data @Builder
public class SignalementResponseDTO {
    private Long id;
    private Long avisId;
    private String signaleurId;
    private String motif;
    private StatutSignalement statut;
    private LocalDateTime dateSignalement;
}