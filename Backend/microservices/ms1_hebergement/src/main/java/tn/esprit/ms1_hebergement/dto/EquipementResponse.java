package tn.esprit.ms1_hebergement.dto;

import tn.esprit.ms1_hebergement.entity.TypeEquipement;

import java.math.BigDecimal;

public record EquipementResponse(
        Long id,
        TypeEquipement type,
        BigDecimal supplement
) {}