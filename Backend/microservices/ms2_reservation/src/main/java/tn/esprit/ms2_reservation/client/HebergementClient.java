package tn.esprit.ms2_reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

/** Appel synchrone vers MS1 (nom Eureka : ms1-hebergement). */
@FeignClient(name = "ms1-hebergement")
public interface HebergementClient {

    @GetMapping("/api/maisons/{id}")
    MaisonDistante trouverMaison(@PathVariable("id") Long id);

    // Seuls les champs utiles à MS2 sont déclarés ; les autres sont ignorés
    record MaisonDistante(Long id, String proprietaireId, List<ChambreDistante> chambres) {}

    record ChambreDistante(Long id, int capacite, BigDecimal prixParNuit,
                           List<EquipementDistant> equipements) {}

    record EquipementDistant(Long id, BigDecimal supplement) {}
}
