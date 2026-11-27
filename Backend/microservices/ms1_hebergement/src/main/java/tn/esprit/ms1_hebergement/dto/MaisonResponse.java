package tn.esprit.ms1_hebergement.dto;

import java.util.List;
//commentaire
public record MaisonResponse(
        Long id,
        String nom,
        String description,
        String ville,
        String adresse,
        List<String> photos,
        String proprietaireId,
        List<ChambreResponse> chambres
) {}