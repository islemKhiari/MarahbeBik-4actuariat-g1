package tn.esprit.ms2_reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2_reservation.dto.BlocRequest;
import tn.esprit.ms2_reservation.dto.BlocResponse;
import tn.esprit.ms2_reservation.dto.DisponibiliteResponse;
import tn.esprit.ms2_reservation.service.BlocDisponibiliteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/blocs")
@RequiredArgsConstructor
public class BlocDisponibiliteController {

    private final BlocDisponibiliteService blocService;

    // Périodes occupées d'une chambre (réservations et blocages manuels)
    @GetMapping
    public List<BlocResponse> listerParChambre(@RequestParam Long chambreId) {
        return blocService.listerParChambre(chambreId);
    }

    @GetMapping("/disponibilite")
    public DisponibiliteResponse disponibilite(
            @RequestParam Long chambreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateArrivee,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDepart) {
        return blocService.verifierDisponibilite(chambreId, dateArrivee, dateDepart);
    }

    @PostMapping
    public ResponseEntity<BlocResponse> creer(@Valid @RequestBody BlocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blocService.creerManuel(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        blocService.supprimerManuel(id);
        return ResponseEntity.noContent().build();
    }
}
