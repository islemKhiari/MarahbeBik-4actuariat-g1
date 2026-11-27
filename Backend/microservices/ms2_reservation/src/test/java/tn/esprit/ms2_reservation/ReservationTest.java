package tn.esprit.ms2_reservation;

import org.junit.jupiter.api.Test;
import tn.esprit.ms2_reservation.entity.PolitiqueAnnulation;
import tn.esprit.ms2_reservation.entity.Reservation;
import tn.esprit.ms2_reservation.entity.StatutReservation;
import tn.esprit.ms2_reservation.exception.RequeteInvalideException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

// Tests unitaires de la logique métier (sans Spring ni base de données)
class ReservationTest {

    private Reservation reservation(LocalDate arrivee, int delaiHeures, int pourcentage) {
        PolitiqueAnnulation politique = new PolitiqueAnnulation();
        politique.setNom("Test");
        politique.setDelaiGratuitHeures(delaiHeures);
        politique.setPourcentagePenalite(pourcentage);
        Reservation r = new Reservation();
        r.setDateArrivee(arrivee);
        r.setDateDepart(arrivee.plusDays(3));
        r.setPrixTotal(new BigDecimal("300.00"));
        r.setPolitiqueAnnulation(politique);
        return r;
    }

    private Instant debutJour(LocalDate d) {
        return d.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    @Test
    void penaliteNulleAvantLeDelaiGratuit() {
        Reservation r = reservation(LocalDate.of(2027, 1, 10), 48, 50);
        // 3 jours avant l'arrivée : dans le délai gratuit de 48 h
        assertEquals(0, r.calculerPenalite(debutJour(LocalDate.of(2027, 1, 7))).signum());
    }

    @Test
    void penaliteAppliqueeApresLeDelaiGratuit() {
        Reservation r = reservation(LocalDate.of(2027, 1, 10), 48, 50);
        // 1 jour avant l'arrivée : hors délai gratuit -> 50 % de 300
        assertEquals(new BigDecimal("150.00"), r.calculerPenalite(debutJour(LocalDate.of(2027, 1, 9))));
    }

    @Test
    void penaliteNulleSiPourcentageZero() {
        Reservation r = reservation(LocalDate.of(2027, 1, 10), 48, 0);
        assertEquals(0, r.calculerPenalite(debutJour(LocalDate.of(2027, 1, 10))).signum());
    }

    @Test
    void annulerPasseLeStatutEtFigeLaPenalite() {
        Reservation r = reservation(LocalDate.now(ZoneOffset.UTC).plusDays(30), 48, 50);
        r.annuler();
        assertEquals(StatutReservation.ANNULEE, r.getStatut());
        assertNotNull(r.getDateAnnulation());
        assertEquals(0, r.getMontantPenalite().signum());
    }

    @Test
    void annulerDeuxFoisEstRefuse() {
        Reservation r = reservation(LocalDate.now(ZoneOffset.UTC).plusDays(30), 48, 50);
        r.annuler();
        assertThrows(RequeteInvalideException.class, r::annuler);
    }

    @Test
    void terminerAvantLeDebutDuSejourEstRefuse() {
        Reservation r = reservation(LocalDate.now(ZoneOffset.UTC).plusDays(5), 48, 50);
        assertThrows(RequeteInvalideException.class, r::terminer);
    }

    @Test
    void terminerUnSejourCommenceFonctionne() {
        Reservation r = reservation(LocalDate.now(ZoneOffset.UTC).minusDays(1), 48, 50);
        r.terminer();
        assertEquals(StatutReservation.TERMINEE, r.getStatut());
    }

    @Test
    void annulerUneReservationTermineeEstRefuse() {
        Reservation r = reservation(LocalDate.now(ZoneOffset.UTC).minusDays(1), 48, 50);
        r.terminer();
        assertThrows(RequeteInvalideException.class, r::annuler);
    }
}
