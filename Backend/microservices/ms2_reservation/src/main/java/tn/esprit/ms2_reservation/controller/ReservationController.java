package tn.esprit.ms2_reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2_reservation.dto.ChangementStatutRequest;
import tn.esprit.ms2_reservation.dto.ReservationRequest;
import tn.esprit.ms2_reservation.dto.ReservationResponse;
import tn.esprit.ms2_reservation.entity.StatutReservation;
import tn.esprit.ms2_reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationResponse> lister(@RequestParam(required = false) String voyageurId,
                                            @RequestParam(required = false) String hoteId,
                                            @RequestParam(required = false) StatutReservation statut) {
        return reservationService.lister(voyageurId, hoteId, statut);
    }

    // Sera aussi appelé par MS3 (OpenFeign) pour vérifier un séjour
    @GetMapping("/{id}")
    public ReservationResponse trouver(@PathVariable Long id) {
        return reservationService.trouver(id);
    }

    @GetMapping("/port")
    public String getPort(HttpServletRequest request) {
        return "Réponse depuis Reservation - Port : "
                + request.getLocalPort();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> creer(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.creer(request));
    }

    @PostMapping("/{id}/annuler")
    public ReservationResponse annuler(@PathVariable Long id,
                                       @Valid @RequestBody ChangementStatutRequest request) {
        return reservationService.annuler(id, request);
    }

    @PostMapping("/{id}/terminer")
    public ReservationResponse terminer(@PathVariable Long id,
                                        @Valid @RequestBody ChangementStatutRequest request) {
        return reservationService.terminer(id, request);
    }
}
