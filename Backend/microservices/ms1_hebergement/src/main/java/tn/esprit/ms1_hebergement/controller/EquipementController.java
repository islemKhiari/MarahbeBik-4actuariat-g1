package tn.esprit.ms1_hebergement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms1_hebergement.config.UtilisateurCourant;
import tn.esprit.ms1_hebergement.dto.EquipementRequest;
import tn.esprit.ms1_hebergement.dto.EquipementResponse;
import tn.esprit.ms1_hebergement.service.EquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final EquipementService equipementService;
    private final UtilisateurCourant utilisateurCourant;

    @GetMapping
    public List<EquipementResponse> lister() {
        return equipementService.lister();
    }

    @PutMapping("/{id}")
    public EquipementResponse modifierSupplement(@PathVariable Long id,
                                                 @Valid @RequestBody EquipementRequest req) {
        return equipementService.modifierSupplement(id, req, utilisateurCourant.get());
    }
}