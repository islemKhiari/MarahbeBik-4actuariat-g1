package tn.esprit.ms2_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms2_reservation.entity.BlocDisponibilite;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BlocDisponibiliteRepository extends JpaRepository<BlocDisponibilite, Long> {

    List<BlocDisponibilite> findByIdentifiantChambreOrderByDateDebut(Long identifiantChambre);

    Optional<BlocDisponibilite> findByIdentifiantReservation(Long identifiantReservation);

    // Périodes [debut, fin[ : deux périodes se chevauchent si chacune commence avant la fin de l'autre
    @Query("""
            SELECT COUNT(b) > 0 FROM BlocDisponibilite b
            WHERE b.identifiantChambre = :chambreId
              AND b.dateDebut < :fin
              AND b.dateFin > :debut
            """)
    boolean existeChevauchement(@Param("chambreId") Long chambreId,
                                @Param("debut") LocalDate debut,
                                @Param("fin") LocalDate fin);
}
