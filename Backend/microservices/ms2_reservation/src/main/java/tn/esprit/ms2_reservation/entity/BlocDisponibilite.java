package tn.esprit.ms2_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Période pendant laquelle une chambre n'est pas disponible.
 * dateFin est exclusive (jour du départ) : un départ le 10 et une arrivée le 10 sont compatibles.
 */
@Entity
@Table(name = "bloc_disponibilite")
@Getter
@Setter
@NoArgsConstructor
public class BlocDisponibilite {

    public static final String MOTIF_RESERVATION = "RESERVATION";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    // id d'une chambre de MS1 : simple valeur, pas de clé étrangère entre services
    @Column(name = "identifiant_chambre", nullable = false)
    private Long identifiantChambre;

    // null pour un blocage manuel de l'hôte (maintenance, indisponibilité...)
    @Column(name = "identifiant_reservation", unique = true)
    private Long identifiantReservation;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private String motif;
}
