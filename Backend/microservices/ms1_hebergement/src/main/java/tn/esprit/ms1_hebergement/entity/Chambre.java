package tn.esprit.ms1_hebergement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
//commentaire
@Entity
@Table(name = "chambre")
@Getter
@Setter
@NoArgsConstructor
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeChambre type;

    @Column(nullable = false)
    private int capacite;

    // Période et prix par nuit (ancien Tarif)
    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "prix_par_nuit", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixParNuit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maison_id", nullable = false)
    private MaisonHote maison;

    @ManyToMany
    @JoinTable(
            name = "chambre_equipement",
            joinColumns = @JoinColumn(name = "chambre_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();
}
