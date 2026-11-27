package tn.esprit.ms1_hebergement.repository;
//commentaire
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms1_hebergement.entity.Chambre;

import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    List<Chambre> findByMaisonId(Long maisonId);
}