// AvisRequestDTO.java
package tn.esprit.avis.avis.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AvisRequestDTO {
    @NotNull
    private Long reservationId;
    @NotBlank
    private String voyageurId;
    @NotBlank
    private String proprietaireId;
    @NotNull @Min(1) @Max(5)
    private Integer note;
    private String commentaire;
}