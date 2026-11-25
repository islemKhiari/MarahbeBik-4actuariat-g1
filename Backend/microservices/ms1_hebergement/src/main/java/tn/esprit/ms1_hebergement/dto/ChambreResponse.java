package tn.esprit.ms1_hebergement.dto;

import tn.esprit.ms1_hebergement.entity.TypeChambre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ChambreResponse(
        Long id,
        Long maisonId,
        String nom,
        TypeChambre type,
        int capacite,
        LocalDate dateDebut,
        LocalDate dateFin,
        BigDecimal prixParNuit,
        List<EquipementResponse> equipements
) {}