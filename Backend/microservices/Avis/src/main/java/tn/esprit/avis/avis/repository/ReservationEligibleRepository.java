package tn.esprit.avis.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.avis.avis.entity.ReservationEligible;

public interface ReservationEligibleRepository extends JpaRepository<ReservationEligible, Long> {
}