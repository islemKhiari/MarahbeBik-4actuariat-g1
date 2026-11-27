package tn.esprit.avis.avis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations_eligibles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationEligible {

    @Id
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "voyageur_id", nullable = false)
    private String voyageurId;

    @Column(name = "maison_id", nullable = false)
    private Long maisonId;

    @Column(name = "date_fin_sejour", nullable = false)
    private LocalDate dateFinSejour;
}