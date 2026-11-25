package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.dto.ChambreRequest;
import tn.esprit.ms1_hebergement.dto.ChambreResponse;
import tn.esprit.ms1_hebergement.service.ChambreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChambreController {

    private final ChambreService chambreService;

    @GetMapping("/api/maisons/{maisonId}/chambres")
    public List<ChambreResponse> listerParMaison(@PathVariable Long maisonId) {
        return chambreService.listerParMaison(maisonId);
    }

    @PostMapping("/api/maisons/{maisonId}/chambres")
    public ResponseEntity<ChambreResponse> creer(@PathVariable Long maisonId,
                                                 @Valid @RequestBody ChambreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chambreService.creer(maisonId, request));
    }

    // Sera aussi appelé par MS2 (OpenFeign) : capacité, prix par nuit, suppléments
    @GetMapping("/api/chambres/{id}")
    public ChambreResponse trouver(@PathVariable Long id) {
        return chambreService.trouver(id);
    }

    @PutMapping("/api/chambres/{id}")
    public ChambreResponse modifier(@PathVariable Long id,
                                    @Valid @RequestBody ChambreRequest request) {
        return chambreService.modifier(id, request);
    }

    @DeleteMapping("/api/chambres/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        chambreService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}