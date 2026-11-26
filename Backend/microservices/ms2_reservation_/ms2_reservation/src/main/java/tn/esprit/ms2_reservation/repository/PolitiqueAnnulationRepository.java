package tn.esprit.ms2_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2_reservation.entity.PolitiqueAnnulation;

import java.util.Optional;

public interface PolitiqueAnnulationRepository extends JpaRepository<PolitiqueAnnulation, Long> {

    boolean existsByNom(String nom);

    Optional<PolitiqueAnnulation> findFirstByActiveTrueOrderByIdentifiantAsc();
}
