
package tn.esprit.prestations.service;

import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.entity.ReservationPrestation;
import tn.esprit.prestations.repository.PrestationRepository;
import tn.esprit.prestations.repository.ReservationPrestationRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationPrestationService {

    private final ReservationPrestationRepository reservationRepository;
    private final PrestationRepository prestationRepository;

    public ReservationPrestationService(
            ReservationPrestationRepository reservationRepository,
            PrestationRepository prestationRepository) {
        this.reservationRepository = reservationRepository;
        this.prestationRepository = prestationRepository;
    }

    // Ajouter une réservation
    public ReservationPrestation ajouterReservation(ReservationPrestation reservation) {

        Long prestationId = reservation.getPrestation().getId();

        Prestation prestation = prestationRepository.findById(prestationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Prestation introuvable"));

        reservation.setPrestation(prestation);

        return reservationRepository.save(reservation);
    }

    // Afficher toutes les réservations
    @Transactional(readOnly = true)
    public List<ReservationPrestation> afficherReservations() {
        return reservationRepository.findAll();
    }

    // Afficher une réservation par ID
    @Transactional(readOnly = true)
    public Optional<ReservationPrestation> afficherReservationParId(Long id) {
        return reservationRepository.findById(id);
    }

    // Modifier une réservation
    public ReservationPrestation modifierReservation(
            Long id, ReservationPrestation nouvelleReservation) {

        ReservationPrestation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Réservation introuvable"));

        Long prestationId = nouvelleReservation.getPrestation().getId();

        Prestation prestation = prestationRepository.findById(prestationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Prestation introuvable"));

        reservation.setVoyageurId(nouvelleReservation.getVoyageurId());
        reservation.setNombrePlaces(nouvelleReservation.getNombrePlaces());
        reservation.setDateReservation(nouvelleReservation.getDateReservation());
        reservation.setStatut(nouvelleReservation.getStatut());
        reservation.setPrestation(prestation);

        return reservationRepository.save(reservation);
    }

    // Supprimer une réservation
    public void supprimerReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("Réservation introuvable");
        }

        reservationRepository.deleteById(id);
    }
}
