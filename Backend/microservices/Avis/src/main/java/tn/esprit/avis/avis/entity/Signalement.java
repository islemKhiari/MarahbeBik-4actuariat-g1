package tn.esprit.avis.avis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "signalements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signaleur_id", nullable = false)
    private String signaleurId;

    @Column(nullable = false, length = 500)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSignalement statut;

    @CreationTimestamp
    @Column(name = "date_signalement", updatable = false)
    private LocalDateTime dateSignalement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avis_id", nullable = false)
    private Avis avis;
}