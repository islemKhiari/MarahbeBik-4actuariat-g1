package tn.esprit.ms2_reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PolitiqueAnnulationRequest(
        @NotBlank String nom,
        @NotNull @Min(0) Integer delaiGratuitHeures,
        @NotNull @Min(0) @Max(100) Integer pourcentagePenalite,
        @NotNull Boolean active
) {}
