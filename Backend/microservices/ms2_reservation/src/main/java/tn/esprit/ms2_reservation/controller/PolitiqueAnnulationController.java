package tn.esprit.ms2_reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2_reservation.dto.PolitiqueAnnulationRequest;
import tn.esprit.ms2_reservation.dto.PolitiqueAnnulationResponse;
import tn.esprit.ms2_reservation.service.PolitiqueAnnulationService;

import java.util.List;

@RestController
@RequestMapping("/api/politiques-annulation")
@RequiredArgsConstructor
public class PolitiqueAnnulationController {

    private final PolitiqueAnnulationService politiqueService;

    @GetMapping
    public List<PolitiqueAnnulationResponse> lister() {
        return politiqueService.lister();
    }

    @GetMapping("/{id}")
    public PolitiqueAnnulationResponse trouver(@PathVariable Long id) {
        return politiqueService.trouver(id);
    }

    @PostMapping
    public ResponseEntity<PolitiqueAnnulationResponse> creer(@Valid @RequestBody PolitiqueAnnulationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(politiqueService.creer(request));
    }

    @PutMapping("/{id}")
    public PolitiqueAnnulationResponse modifier(@PathVariable Long id,
                                                @Valid @RequestBody PolitiqueAnnulationRequest request) {
        return politiqueService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        politiqueService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
