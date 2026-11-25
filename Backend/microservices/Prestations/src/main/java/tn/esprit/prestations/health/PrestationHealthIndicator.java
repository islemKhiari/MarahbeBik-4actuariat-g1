
package tn.esprit.prestations.health;

import tn.esprit.prestations.repository.PrestationRepository;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PrestationHealthIndicator implements HealthIndicator {

    private final PrestationRepository prestationRepository;

    public PrestationHealthIndicator(
            PrestationRepository prestationRepository) {
        this.prestationRepository = prestationRepository;
    }

    @Override
    public Health health() {
        try {
            long nombrePrestations = prestationRepository.count();

            return Health.up()
                    .withDetail("nombrePrestations", nombrePrestations)
                    .withDetail("service", "MS3 - Prestations")
                    .withDetail("message", "Le service fonctionne correctement")
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("service", "MS3 - Prestations")
                    .withDetail("message", "Impossible d'acceder aux prestations")
                    .build();
        }
    }
}
