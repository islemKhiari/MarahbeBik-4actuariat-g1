package tn.esprit.ms2_reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteRequest(
        @NotBlank String nomComplet,
        @NotNull @Min(0) @Max(120) Integer age
) {}
