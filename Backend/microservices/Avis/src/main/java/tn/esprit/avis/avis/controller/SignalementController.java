package tn.esprit.avis.avis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.avis.avis.dto.SignalementRequestDTO;
import tn.esprit.avis.avis.dto.SignalementResponseDTO;
import tn.esprit.avis.avis.entity.StatutSignalement;
import tn.esprit.avis.avis.service.ISignalementService;

import java.util.List;

@RestController
@RequestMapping("/api/avis/signalements")
@RequiredArgsConstructor
public class SignalementController {

    private final ISignalementService signalementService;

    @PostMapping("/avis/{avisId}")
    public ResponseEntity<SignalementResponseDTO> signalerAvis(
            @PathVariable Long avisId,
            @Valid @RequestBody SignalementRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signalementService.signalerAvis(avisId, dto));
    }

    @GetMapping
    public ResponseEntity<List<SignalementResponseDTO>> getAllSignalements() {
        return ResponseEntity.ok(signalementService.getAllSignalements());
    }

    @GetMapping("/avis/{avisId}")
    public ResponseEntity<List<SignalementResponseDTO>> getSignalementsParAvis(@PathVariable Long avisId) {
        return ResponseEntity.ok(signalementService.getSignalementsParAvis(avisId));
    }

    @GetMapping("/statut")
    public ResponseEntity<List<SignalementResponseDTO>> getSignalementsParStatut(
            @RequestParam(defaultValue = "EN_ATTENTE") StatutSignalement statut) {
        return ResponseEntity.ok(signalementService.getSignalementsParStatut(statut));
    }

    @PatchMapping("/{signalementId}/traiter")
    public ResponseEntity<SignalementResponseDTO> traiterSignalement(
            @PathVariable Long signalementId,
            @RequestParam StatutSignalement statut) {
        return ResponseEntity.ok(signalementService.traiterSignalement(signalementId, statut));
    }

    @DeleteMapping("/{signalementId}")
    public ResponseEntity<Void> supprimerSignalement(@PathVariable Long signalementId) {
        signalementService.supprimerSignalement(signalementId);
        return ResponseEntity.noContent().build();
    }
}