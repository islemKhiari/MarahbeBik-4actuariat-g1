package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.config.UtilisateurCourant;
import tn.esprit.ms1_hebergement.dto.ChambreRequest;
import tn.esprit.ms1_hebergement.dto.ChambreResponse;
import tn.esprit.ms1_hebergement.service.ChambreService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChambreController {

    private final ChambreService chambreService;
    private final UtilisateurCourant utilisateurCourant;

    @GetMapping("/api/maisons/{maisonId}/chambres")
    public List<ChambreResponse> listerParMaison(@PathVariable Long maisonId) {
        return chambreService.listerParMaison(maisonId);
    }

    @PostMapping("/api/maisons/{maisonId}/chambres")
    public ResponseEntity<ChambreResponse> creer(@PathVariable Long maisonId,
                                                 @Valid @RequestBody ChambreRequest req) {
        ChambreResponse cree = chambreService.creer(maisonId, req, utilisateurCourant.get());
        return ResponseEntity.created(URI.create("/api/chambres/" + cree.id())).body(cree);
    }

    // Cet endpoint sera aussi appelé par MS2 (OpenFeign) : capacité, prix, suppléments
    @GetMapping("/api/chambres/{id}")
    public ChambreResponse trouver(@PathVariable Long id) {
        return chambreService.trouver(id);
    }

    @PutMapping("/api/chambres/{id}")
    public ChambreResponse modifier(@PathVariable Long id, @Valid @RequestBody ChambreRequest req) {
        return chambreService.modifier(id, req, utilisateurCourant.get());
    }

    @DeleteMapping("/api/chambres/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        chambreService.supprimer(id, utilisateurCourant.get());
        return ResponseEntity.noContent().build();
    }
}