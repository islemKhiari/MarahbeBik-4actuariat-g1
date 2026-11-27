package tn.esprit.ms2_reservation.dto;

public record PolitiqueAnnulationResponse(
        Long identifiant,
        String nom,
        Integer delaiGratuitHeures,
        Integer pourcentagePenalite,
        Boolean active
) {}
