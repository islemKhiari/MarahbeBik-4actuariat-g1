package tn.esprit.avis.avis.service;

import tn.esprit.avis.avis.entity.ReservationEligible;
import java.util.List;

public interface IReservationEligibleService {
    ReservationEligible enregistrerReservationEligible(ReservationEligible reservation);
    boolean verifierEligibilite(Long reservationId);
    ReservationEligible getById(Long reservationId);
    List<ReservationEligible> getAll();
}