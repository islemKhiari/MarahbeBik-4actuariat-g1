package tn.esprit.avis.avis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.avis.avis.dto.*;
import tn.esprit.avis.avis.entity.StatutAvis;
import tn.esprit.avis.avis.service.IAvisService;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
@RequiredArgsConstructor
public class AvisController {

    private final IAvisService avisService;

    @PostMapping
    public ResponseEntity<AvisResponseDTO> creerAvis(@Valid @RequestBody AvisRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avisService.creerAvis(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvisResponseDTO> modifierAvis(@PathVariable Long id, @Valid @RequestBody AvisUpdateDTO dto) {
        return ResponseEntity.ok(avisService.modifierAvis(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Long id) {
        avisService.supprimerAvis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvisResponseDTO> getAvisById(@PathVariable Long id) {
        return ResponseEntity.ok(avisService.getAvisById(id));
    }

    @GetMapping
    public ResponseEntity<List<AvisResponseDTO>> getAllAvis() {
        return ResponseEntity.ok(avisService.getAllAvis());
    }

    @GetMapping("/maison/{maisonId}")
    public ResponseEntity<List<AvisResponseDTO>> getAvisPubliesParMaison(@PathVariable Long maisonId) {
        return ResponseEntity.ok(avisService.getAvisPubliesParMaison(maisonId));
    }

    @GetMapping("/maison/{maisonId}/moyenne")
    public ResponseEntity<Double> getMoyenneNoteMaison(@PathVariable Long maisonId) {
        return ResponseEntity.ok(avisService.getMoyenneNoteMaison(maisonId));
    }

    @GetMapping("/voyageur/{voyageurId}")
    public ResponseEntity<List<AvisResponseDTO>> getAvisParVoyageur(@PathVariable String voyageurId) {
        return ResponseEntity.ok(avisService.getAvisParVoyageur(voyageurId));
    }

    @GetMapping("/proprietaire/{proprietaireId}")
    public ResponseEntity<List<AvisResponseDTO>> getAvisParProprietaire(@PathVariable String proprietaireId) {
        return ResponseEntity.ok(avisService.getAvisParProprietaire(proprietaireId));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<AvisResponseDTO> changerStatutAvis(@PathVariable Long id, @RequestParam StatutAvis statut) {
        return ResponseEntity.ok(avisService.changerStatutAvis(id, statut));
    }

    @PutMapping("/{id}/reponse")
    public ResponseEntity<AvisResponseDTO> repondreAvis(@PathVariable Long id, @Valid @RequestBody ReponseRequestDTO dto) {
        return ResponseEntity.ok(avisService.repondreAvis(id, dto));
    }
}