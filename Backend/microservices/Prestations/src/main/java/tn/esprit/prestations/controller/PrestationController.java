package tn.esprit.prestations.controller;

import jakarta.servlet.http.HttpServletRequest;
import tn.esprit.prestations.dto.PrestationDTO;
import tn.esprit.prestations.service.PrestationService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PrestationController {


    private final PrestationService prestationService;

    // Ajouter une prestation
    @PostMapping
    public ResponseEntity<PrestationDTO> ajouter(
            @RequestBody PrestationDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestationService.ajouterPrestation(dto));
    }

    // Afficher toutes les prestations
    @GetMapping
    public ResponseEntity<List<PrestationDTO>> afficherToutes() {

        return ResponseEntity.ok(
                prestationService.afficherPrestations());
    }

    // Afficher une prestation par ID
    @GetMapping("/{id}")
    public ResponseEntity<PrestationDTO> afficherParId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestationService.afficherPrestationParId(id));
    }

    @GetMapping("/port")
    public String getPort(HttpServletRequest request) {
        return "Réponse depuis Prestations - Port : "
                + request.getLocalPort();
    }
    // Modifier une prestation
    @PutMapping("/{id}")
    public ResponseEntity<PrestationDTO> modifier(
            @PathVariable Long id,
            @RequestBody PrestationDTO dto) {

        return ResponseEntity.ok(
                prestationService.modifierPrestation(id, dto));
    }

    // Supprimer une prestation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {

        prestationService.supprimerPrestation(id);

        return ResponseEntity.noContent().build();
    }
}