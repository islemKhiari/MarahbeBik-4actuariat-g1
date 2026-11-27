package tn.esprit.ms2_reservation.dto;

import java.time.LocalDate;

public record DisponibiliteResponse(
        Long identifiantChambre,
        LocalDate dateArrivee,
        LocalDate dateDepart,
        boolean disponible
) {}
