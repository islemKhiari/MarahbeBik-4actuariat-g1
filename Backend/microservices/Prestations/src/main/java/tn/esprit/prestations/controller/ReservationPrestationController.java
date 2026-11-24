
package tn.esprit.prestations.controller;

import tn.esprit.prestations.dto.ReservationPrestationDTO;
import tn.esprit.prestations.entity.ReservationPrestation;
import tn.esprit.prestations.mapper.ReservationPrestationMapper;
import tn.esprit.prestations.service.ReservationPrestationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations-prestations")
@CrossOrigin(origins = "*")
public class ReservationPrestationController {

    private final ReservationPrestationService reservationService;
    private final ReservationPrestationMapper reservationMapper;

    public ReservationPrestationController(
            ReservationPrestationService reservationService,
            ReservationPrestationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    // Ajouter une réservation
    @PostMapping
    public ResponseEntity<ReservationPrestationDTO> ajouter(
            @RequestBody ReservationPrestationDTO dto) {

        ReservationPrestation reservation =
                reservationMapper.toEntity(dto);

        ReservationPrestation saved =
                reservationService.ajouterReservation(reservation);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationMapper.toDTO(saved));
    }

    // Afficher toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationPrestationDTO>> afficherToutes() {

        List<ReservationPrestationDTO> reservations =
                reservationService.afficherReservations()
                        .stream()
                        .map(reservationMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(reservations);
    }

    // Afficher une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationPrestationDTO> afficherParId(
            @PathVariable Long id) {

        return reservationService.afficherReservationParId(id)
                .map(reservationMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Modifier une réservation
    @PutMapping("/{id}")
    public ResponseEntity<ReservationPrestationDTO> modifier(
            @PathVariable Long id,
            @RequestBody ReservationPrestationDTO dto) {

        ReservationPrestation reservation =
                reservationMapper.toEntity(dto);

        ReservationPrestation updated =
                reservationService.modifierReservation(id, reservation);

        return ResponseEntity.ok(reservationMapper.toDTO(updated));
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {

        reservationService.supprimerReservation(id);

        return ResponseEntity.noContent().build();
    }
}
