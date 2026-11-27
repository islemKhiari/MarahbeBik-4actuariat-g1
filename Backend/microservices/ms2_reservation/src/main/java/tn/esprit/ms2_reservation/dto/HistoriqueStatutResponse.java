package tn.esprit.ms2_reservation.dto;

import tn.esprit.ms2_reservation.entity.StatutReservation;

import java.time.Instant;

public record HistoriqueStatutResponse(
        Long identifiant,
        StatutReservation ancienStatut,
        StatutReservation nouveauStatut,
        Instant modifieLe,
        String modifiePar,
        String motif
) {}
