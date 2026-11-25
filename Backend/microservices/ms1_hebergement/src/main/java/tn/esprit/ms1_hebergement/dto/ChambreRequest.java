package tn.esprit.ms1_hebergement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms1_hebergement.entity.TypeChambre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ChambreRequest(
        @NotBlank String nom,
        @NotNull TypeChambre type,
        @Min(1) int capacite,
        LocalDate dateDebut,
        LocalDate dateFin,
        @NotNull @DecimalMin("0.0") BigDecimal prixParNuit,
        Set<Long> equipementIds
) {}