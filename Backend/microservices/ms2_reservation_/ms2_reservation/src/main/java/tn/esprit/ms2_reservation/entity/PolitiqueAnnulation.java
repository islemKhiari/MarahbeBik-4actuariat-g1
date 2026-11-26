package tn.esprit.ms2_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "politique_annulation")
@Getter
@Setter
@NoArgsConstructor
public class PolitiqueAnnulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    @Column(nullable = false, unique = true)
    private String nom;

    // Annulation gratuite jusqu'à X heures avant l'arrivée
    @Column(name = "delai_gratuit_heures", nullable = false)
    private Integer delaiGratuitHeures;

    // Pourcentage du prix total retenu si l'annulation est tardive (0 à 100)
    @Column(name = "pourcentage_penalite", nullable = false)
    private Integer pourcentagePenalite;

    @Column(nullable = false)
    private Boolean active = true;
}
