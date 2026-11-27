package tn.esprit.ms2_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms2_reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByIdentifiantVoyageur(String identifiantVoyageur);

    List<Reservation> findByIdentifiantHote(String identifiantHote);

    List<Reservation> findByIdentifiantChambre(Long identifiantChambre);

    boolean existsByPolitiqueAnnulationIdentifiant(Long politiqueId);
}
