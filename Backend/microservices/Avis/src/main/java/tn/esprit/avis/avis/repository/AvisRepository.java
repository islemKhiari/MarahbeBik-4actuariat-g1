package tn.esprit.avis.avis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.avis.avis.entity.Avis;
import tn.esprit.avis.avis.entity.StatutAvis;

import java.util.List;
import java.util.Optional;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    boolean existsByReservationId(Long reservationId);
    Optional<Avis> findByReservationId(Long reservationId);
    List<Avis> findByMaisonIdAndStatut(Long maisonId, StatutAvis statut);
    List<Avis> findByVoyageurId(String voyageurId);
    List<Avis> findByProprietaireId(String proprietaireId);

    @Query("SELECT AVG(a.note) FROM Avis a WHERE a.maisonId = :maisonId AND a.statut = 'PUBLIE'")
    Double calculerMoyenneNoteMaison(Long maisonId);
}