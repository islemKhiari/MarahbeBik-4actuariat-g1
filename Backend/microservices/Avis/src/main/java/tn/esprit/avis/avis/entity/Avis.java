package tn.esprit.avis.avis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avis")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false, unique = true)
    private Long reservationId;

    @Column(name = "voyageur_id", nullable = false)
    private String voyageurId;

    @Column(name = "maison_id", nullable = false)
    private Long maisonId;

    @Column(name = "proprietaire_id", nullable = false)
    private String proprietaireId;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer note;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAvis statut;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // --- Réponse du propriétaire (0..1) ---
    @Column(name = "reponse_contenu", columnDefinition = "TEXT")
    private String reponseContenu;

    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;

    // --- Composition avec Signalement (0..*) ---
    @OneToMany(mappedBy = "avis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Signalement> signalements = new ArrayList<>();
}