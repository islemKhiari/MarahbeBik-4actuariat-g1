package tn.esprit.avis.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.avis.avis.entity.Signalement;
import tn.esprit.avis.avis.entity.StatutSignalement;

import java.util.List;

public interface SignalementRepository extends JpaRepository<Signalement, Long> {
    List<Signalement> findByAvisId(Long avisId);
    List<Signalement> findByStatut(StatutSignalement statut);
    boolean existsByAvisIdAndSignaleurId(Long avisId, String signaleurId);
}