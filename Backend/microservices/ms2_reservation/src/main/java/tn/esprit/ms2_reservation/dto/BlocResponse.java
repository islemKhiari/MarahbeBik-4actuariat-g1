package tn.esprit.ms2_reservation.dto;

import java.time.LocalDate;

public record BlocResponse(
        Long identifiant,
        Long identifiantChambre,
        Long identifiantReservation,
        LocalDate dateDebut,
        LocalDate dateFin,
        String motif
) {}
