// SignalementRequestDTO.java
package tn.esprit.avis.avis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignalementRequestDTO {
    @NotBlank
    private String signaleurId;
    @NotBlank
    private String motif;
}