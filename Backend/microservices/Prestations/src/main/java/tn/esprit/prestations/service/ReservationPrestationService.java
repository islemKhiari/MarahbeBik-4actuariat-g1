package tn.esprit.prestations.service;

import tn.esprit.prestations.dto.ReservationPrestationDTO;
import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.entity.ReservationPrestation;
import tn.esprit.prestations.exception.ResourceNotFoundException;
import tn.esprit.prestations.mapper.ReservationPrestationMapper;
import tn.esprit.prestations.repository.PrestationRepository;
import tn.esprit.prestations.repository.ReservationPrestationRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class ReservationPrestationService {

    private final ReservationPrestationRepository reservationRepository;
    private final PrestationRepository prestationRepository;
    private final ReservationPrestationMapper reservationMapper;

    // Ajouter une reservation
    public ReservationPrestationDTO ajouterReservation(
            ReservationPrestationDTO dto) {

        Prestation prestation = getPrestation(dto.getPrestationId());

        ReservationPrestation reservation =
                reservationMapper.toEntity(dto);

        reservation.setId(null);

        reservation.setPrestation(prestation);
        prestation.getReservations().add(reservation);

        return reservationMapper.toDTO(
                reservationRepository.save(reservation));
    }

    // Afficher toutes les reservations
    @Transactional(readOnly = true)
    public List<ReservationPrestationDTO> afficherReservations() {

        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    // Afficher une reservation par ID
    @Transactional(readOnly = true)
    public ReservationPrestationDTO afficherReservationParId(Long id) {

        return reservationMapper.toDTO(getReservation(id));
    }

    // Modifier une reservation
    public ReservationPrestationDTO modifierReservation(
            Long id, ReservationPrestationDTO dto) {

        ReservationPrestation reservation = getReservation(id);

        Prestation anciennePrestation = reservation.getPrestation();
        Prestation nouvellePrestation =
                getPrestation(dto.getPrestationId());

        ReservationPrestation nouvelle =
                reservationMapper.toEntity(dto);

        reservation.setVoyageurId(nouvelle.getVoyageurId());
        reservation.setNombrePlaces(nouvelle.getNombrePlaces());
        reservation.setDateReservation(nouvelle.getDateReservation());
        reservation.setStatut(nouvelle.getStatut());

        // Gestion de la relation bidirectionnelle
        if (!Objects.equals(anciennePrestation.getId(),
                nouvellePrestation.getId())) {

            anciennePrestation.getReservations().remove(reservation);
            nouvellePrestation.getReservations().add(reservation);
            reservation.setPrestation(nouvellePrestation);
        }

        return reservationMapper.toDTO(
                reservationRepository.save(reservation));
    }

    // Supprimer une reservation
    public void supprimerReservation(Long id) {

        ReservationPrestation reservation = getReservation(id);

        reservation.getPrestation()
                .getReservations()
                .remove(reservation);

        reservationRepository.delete(reservation);
    }

    // Recuperer une reservation
    private ReservationPrestation getReservation(Long id) {

        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));
    }

    // Recuperer une prestation
    private Prestation getPrestation(Long id) {

        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'identifiant de la prestation est obligatoire");
        }

        return prestationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prestation", id));
    }
}