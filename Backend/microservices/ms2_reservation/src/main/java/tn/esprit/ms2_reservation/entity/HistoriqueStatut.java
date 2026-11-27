package tn.esprit.ms2_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "historique_statut")
@Getter
@Setter
@NoArgsConstructor
public class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    // null à la création de la réservation
    @Enumerated(EnumType.STRING)
    @Column(name = "ancien_statut")
    private StatutReservation ancienStatut;

    @Enumerated(EnumType.STRING)
    @Column(name = "nouveau_statut", nullable = false)
    private StatutReservation nouveauStatut;

    @Column(name = "modifie_le", nullable = false)
    private Instant modifieLe;

    // id de l'utilisateur Keycloak : simple texte, pas de clé étrangère
    @Column(name = "modifie_par", nullable = false)
    private String modifiePar;

    @Column(length = 500)
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
}
