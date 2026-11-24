
package tn.esprit.prestations.controller;

import tn.esprit.prestations.dto.PrestationDTO;
import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.mapper.PrestationMapper;
import tn.esprit.prestations.service.PrestationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
@CrossOrigin(origins = "*")
public class PrestationController {

    private final PrestationService prestationService;
    private final PrestationMapper prestationMapper;

    public PrestationController(
            PrestationService prestationService,
            PrestationMapper prestationMapper) {
        this.prestationService = prestationService;
        this.prestationMapper = prestationMapper;
    }

    // Ajouter une prestation
    @PostMapping
    public ResponseEntity<PrestationDTO> ajouter(
            @RequestBody PrestationDTO dto) {

        Prestation prestation = prestationMapper.toEntity(dto);
        Prestation saved = prestationService.ajouterPrestation(prestation);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestationMapper.toDTO(saved));
    }

    // Afficher toutes les prestations
    @GetMapping
    public ResponseEntity<List<PrestationDTO>> afficherToutes() {

        List<PrestationDTO> prestations =
                prestationService.afficherPrestations()
                        .stream()
                        .map(prestationMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(prestations);
    }

    // Afficher une prestation par ID
    @GetMapping("/{id}")
    public ResponseEntity<PrestationDTO> afficherParId(
            @PathVariable Long id) {

        return prestationService.afficherPrestationParId(id)
                .map(prestationMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Modifier une prestation
    @PutMapping("/{id}")
    public ResponseEntity<PrestationDTO> modifier(
            @PathVariable Long id,
            @RequestBody PrestationDTO dto) {

        Prestation prestation = prestationMapper.toEntity(dto);
        Prestation updated =
                prestationService.modifierPrestation(id, prestation);

        return ResponseEntity.ok(prestationMapper.toDTO(updated));
    }

    // Supprimer une prestation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {

        prestationService.supprimerPrestation(id);

        return ResponseEntity.noContent().build();
    }
}
