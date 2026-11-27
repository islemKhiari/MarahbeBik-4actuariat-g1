package tn.esprit.ms1_hebergement.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
//commentaire
public record MaisonRequest(
        @NotBlank String nom,
        String description,
        @NotBlank String ville,
        @NotBlank String adresse,
        List<String> photos,
        @NotBlank String proprietaireId
) {}