package tn.esprit.prestations.controller;

import tn.esprit.prestations.dto.ReservationPrestationDTO;
import tn.esprit.prestations.service.ReservationPrestationService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations-prestations")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ReservationPrestationController {

    private final ReservationPrestationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationPrestationDTO> ajouter(
            @RequestBody ReservationPrestationDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.ajouterReservation(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservationPrestationDTO>> afficherToutes() {

        return ResponseEntity.ok(
                reservationService.afficherReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationPrestationDTO> afficherParId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService.afficherReservationParId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationPrestationDTO> modifier(
            @PathVariable Long id,
            @RequestBody ReservationPrestationDTO dto) {

        return ResponseEntity.ok(
                reservationService.modifierReservation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {

        reservationService.supprimerReservation(id);

        return ResponseEntity.noContent().build();
    }
}