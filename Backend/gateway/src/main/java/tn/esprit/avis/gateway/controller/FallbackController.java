package tn.esprit.avis.gateway.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private final CircuitBreakerRegistry registry;

    public FallbackController(CircuitBreakerRegistry registry) {
        this.registry = registry;
    }

    // MS1 - HEBERGEMENT
    @RequestMapping("/hebergement")
    public ResponseEntity<Map<String, Object>> hebergementFallback() {
        return fallback("MS1-HEBERGEMENT", "hebergementCB");
    }

    // MS2 - RESERVATION
    @RequestMapping("/reservation")
    public ResponseEntity<Map<String, Object>> reservationFallback() {
        return fallback("MS2-RESERVATION", "reservationCB");
    }

    // MS3 - PRESTATIONS
    @RequestMapping("/prestations")
    public ResponseEntity<Map<String, Object>> prestationsFallback() {
        return fallback("PRESTATIONS", "prestationsCB");
    }

    // MS4 - AVIS
    @RequestMapping("/avis")
    public ResponseEntity<Map<String, Object>> avisFallback() {
        return fallback("AVIS", "avisCB");
    }

    // MS5 - RECOMMANDATION
    @RequestMapping("/recommandation")
    public ResponseEntity<Map<String, Object>> recommandationFallback() {
        return fallback("RECOMMANDATION", "recommandationCB");
    }

    // Methode commune pour tous les microservices
    private ResponseEntity<Map<String, Object>> fallback(
            String service, String circuitBreakerName) {

        String state = registry.find(circuitBreakerName)
                .map(cb -> cb.getState().name())
                .orElse("UNKNOWN");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "service", service,
                        "status", 503,
                        "fallback", true,
                        "circuitBreaker", state,
                        "message", "Le microservice " + service
                                + " est temporairement indisponible. "
                                + "Veuillez reessayer plus tard."
                ));
    }
}