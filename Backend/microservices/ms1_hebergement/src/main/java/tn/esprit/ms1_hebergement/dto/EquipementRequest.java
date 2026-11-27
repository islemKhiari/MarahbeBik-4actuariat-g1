package tn.esprit.ms1_hebergement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms1_hebergement.entity.TypeEquipement;

import java.math.BigDecimal;
//commentaire
public record EquipementRequest(
        @NotNull TypeEquipement type,
        @NotNull @DecimalMin("0.0") BigDecimal supplement
) {}