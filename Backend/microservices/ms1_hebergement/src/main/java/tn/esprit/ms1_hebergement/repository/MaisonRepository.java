package tn.esprit.ms1_hebergement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms1_hebergement.entity.MaisonHote;

import java.math.BigDecimal;
import java.util.List;

public interface MaisonRepository extends JpaRepository<MaisonHote, Long> {

    List<MaisonHote> findByProprietaireId(String proprietaireId);

    // Filtres optionnels : si un paramètre est null, la condition est ignorée
    @Query("""
            SELECT DISTINCT m FROM MaisonHote m JOIN m.chambres c
            WHERE (:ville IS NULL OR LOWER(m.ville) LIKE LOWER(CONCAT('%', :ville, '%')))
              AND (:capacite IS NULL OR c.capacite >= :capacite)
              AND (:budgetMax IS NULL OR c.prixParNuit <= :budgetMax)
            """)
    List<MaisonHote> rechercher(@Param("ville") String ville,
                                @Param("capacite") Integer capacite,
                                @Param("budgetMax") BigDecimal budgetMax);
}