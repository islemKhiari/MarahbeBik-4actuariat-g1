package tn.esprit.avis.avis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.avis.avis.entity.ReservationEligible;
import tn.esprit.avis.avis.service.IReservationEligibleService;

import java.util.List;

@RestController
@RequestMapping("/api/avis/reservations-eligibles")
@RequiredArgsConstructor
public class ReservationEligibleController {

    private final IReservationEligibleService reservationEligibleService;

    @PostMapping
    public ResponseEntity<ReservationEligible> enregistrer(@RequestBody ReservationEligible reservation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationEligibleService.enregistrerReservationEligible(reservation));
    }

    @GetMapping("/{reservationId}/check")
    public ResponseEntity<Boolean> verifierEligibilite(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationEligibleService.verifierEligibilite(reservationId));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationEligible> getById(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationEligibleService.getById(reservationId));
    }

    @GetMapping
    public ResponseEntity<List<ReservationEligible>> getAll() {
        return ResponseEntity.ok(reservationEligibleService.getAll());
    }
}