package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.dto.EquipementRequest;
import tn.esprit.ms1_hebergement.dto.EquipementResponse;
import tn.esprit.ms1_hebergement.service.EquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final EquipementService equipementService;

    @GetMapping
    public List<EquipementResponse> lister() {
        return equipementService.lister();
    }

    @GetMapping("/{id}")
    public EquipementResponse trouver(@PathVariable Long id) {
        return equipementService.trouver(id);
    }

    @PostMapping
    public ResponseEntity<EquipementResponse> creer(@Valid @RequestBody EquipementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementService.creer(request));
    }

    @PutMapping("/{id}")
    public EquipementResponse modifier(@PathVariable Long id,
                                       @Valid @RequestBody EquipementRequest request) {
        return equipementService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        equipementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}