package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.config.UtilisateurCourant;
import tn.esprit.ms1_hebergement.dto.MaisonRequest;
import tn.esprit.ms1_hebergement.dto.MaisonResponse;
import tn.esprit.ms1_hebergement.dto.MaisonResumeResponse;
import tn.esprit.ms1_hebergement.entity.TypeEquipement;
import tn.esprit.ms1_hebergement.service.MaisonService;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maisons")
@RequiredArgsConstructor
public class MaisonController {

    private final MaisonService maisonService;
    private final UtilisateurCourant utilisateurCourant;

    @GetMapping
    public Page<MaisonResumeResponse> rechercher(
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) Integer capacite,
            @RequestParam(required = false) BigDecimal budgetMax,
            @RequestParam(required = false) List<TypeEquipement> equipements,
            @PageableDefault(size = 10) Pageable pageable) {
        return maisonService.rechercher(ville, capacite, budgetMax, equipements, pageable);
    }

    @GetMapping("/{id}")
    public MaisonResponse trouver(@PathVariable Long id) {
        return maisonService.trouver(id);
    }

    @GetMapping("/mes-maisons")
    public List<MaisonResponse> mesMaisons() {
        return maisonService.mesMaisons(utilisateurCourant.get());
    }

    @PostMapping
    public ResponseEntity<MaisonResponse> creer(@Valid @RequestBody MaisonRequest req) {
        MaisonResponse cree = maisonService.creer(req, utilisateurCourant.get());
        return ResponseEntity.created(URI.create("/api/maisons/" + cree.id())).body(cree); // 201
    }

    @PutMapping("/{id}")
    public MaisonResponse modifier(@PathVariable Long id, @Valid @RequestBody MaisonRequest req) {
        return maisonService.modifier(id, req, utilisateurCourant.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        maisonService.supprimer(id, utilisateurCourant.get());
        return ResponseEntity.noContent().build(); // 204
    }
}