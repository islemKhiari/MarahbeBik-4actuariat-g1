package tn.esprit.ms2_reservation.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangementStatutRequest(
        @NotBlank String modifiePar,
        String motif
) {}
