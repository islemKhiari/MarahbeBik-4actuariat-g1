// AvisResponseDTO.java
package tn.esprit.avis.avis.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.avis.avis.entity.StatutAvis;
import java.time.LocalDateTime;

@Data @Builder
public class AvisResponseDTO {
    private Long id;
    private Long reservationId;
    private String voyageurId;
    private Long maisonId;
    private String proprietaireId;
    private Integer note;
    private String commentaire;
    private StatutAvis statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String reponseContenu;
    private LocalDateTime dateReponse;
}