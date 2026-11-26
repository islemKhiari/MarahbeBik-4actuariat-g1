package tn.esprit.ms2_reservation.dto;

import tn.esprit.ms2_reservation.entity.StatutReservation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        Long identifiant,
        Long identifiantHebergement,
        Long identifiantChambre,
        String identifiantVoyageur,
        String identifiantHote,
        LocalDate dateArrivee,
        LocalDate dateDepart,
        BigDecimal prixTotal,
        StatutReservation statut,
        Instant dateAnnulation,
        BigDecimal montantPenalite,
        Long politiqueAnnulationId,
        List<InviteResponse> voyageurs,
        List<HistoriqueStatutResponse> historique
) {}
