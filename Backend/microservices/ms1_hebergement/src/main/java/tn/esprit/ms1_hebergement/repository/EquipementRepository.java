package tn.esprit.ms1_hebergement.repository;
//commentaire
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms1_hebergement.entity.Equipement;
import tn.esprit.ms1_hebergement.entity.TypeEquipement;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {

    boolean existsByType(TypeEquipement type);
}