package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.dto.MaisonRequest;
import tn.esprit.ms1_hebergement.dto.MaisonResponse;
import tn.esprit.ms1_hebergement.service.MaisonService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/maisons")
@RequiredArgsConstructor
public class MaisonController {

    private final MaisonService maisonService;

    @GetMapping
    public List<MaisonResponse> lister(@RequestParam(required = false) String ville,
                                       @RequestParam(required = false) Integer capacite,
                                       @RequestParam(required = false) BigDecimal budgetMax) {
        return maisonService.lister(ville, capacite, budgetMax);
    }

    @GetMapping("/{id}")
    public MaisonResponse trouver(@PathVariable Long id) {
        return maisonService.trouver(id);
    }

    @GetMapping("/proprietaire/{proprietaireId}")
    public List<MaisonResponse> listerParProprietaire(@PathVariable String proprietaireId) {
        return maisonService.listerParProprietaire(proprietaireId);
    }

    @PostMapping
    public ResponseEntity<MaisonResponse> creer(@Valid @RequestBody MaisonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maisonService.creer(request));
    }

    @PutMapping("/{id}")
    public MaisonResponse modifier(@PathVariable Long id, @Valid @RequestBody MaisonRequest request) {
        return maisonService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        maisonService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}