
package tn.esprit.prestations.repository;

import tn.esprit.prestations.entity.ReservationPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationPrestationRepository extends JpaRepository<ReservationPrestation, Long> {
}
