// ReponseRequestDTO.java
package tn.esprit.avis.avis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReponseRequestDTO {
    @NotBlank
    private String proprietaireId;
    @NotBlank
    private String reponseContenu;
}