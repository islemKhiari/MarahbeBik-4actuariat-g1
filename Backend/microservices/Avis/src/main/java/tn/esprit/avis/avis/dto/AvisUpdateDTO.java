// AvisUpdateDTO.java
package tn.esprit.avis.avis.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AvisUpdateDTO {
    @NotBlank
    private String voyageurId;
    @NotNull @Min(1) @Max(5)
    private Integer note;
    private String commentaire;
}