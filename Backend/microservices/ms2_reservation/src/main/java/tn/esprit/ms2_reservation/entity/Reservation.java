package tn.esprit.ms2_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.ms2_reservation.exception.RequeteInvalideException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    // Références vers MS1 : simples valeurs, pas de clé étrangère entre services
    @Column(name = "identifiant_hebergement", nullable = false)
    private Long identifiantHebergement;

    @Column(name = "identifiant_chambre", nullable = false)
    private Long identifiantChambre;

    // ids des utilisateurs Keycloak : simple texte
    @Column(name = "identifiant_voyageur", nullable = false)
    private String identifiantVoyageur;

    @Column(name = "identifiant_hote", nullable = false)
    private String identifiantHote;

    @Column(name = "date_arrivee", nullable = false)
    private LocalDate dateArrivee;

    @Column(name = "date_depart", nullable = false)
    private LocalDate dateDepart;

    @Column(name = "prix_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.CONFIRMEE;

    @Column(name = "date_annulation")
    private Instant dateAnnulation;

    @Column(name = "montant_penalite", precision = 10, scale = 2)
    private BigDecimal montantPenalite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "politique_annulation_id", nullable = false)
    private PolitiqueAnnulation politiqueAnnulation;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InviteReservation> voyageurs = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("modifieLe ASC, identifiant ASC")
    private List<HistoriqueStatut> historique = new ArrayList<>();

    public void ajouterVoyageur(InviteReservation invite) {
        voyageurs.add(invite);
        invite.setReservation(this);
    }

    public void ajouterHistorique(HistoriqueStatut ligne) {
        historique.add(ligne);
        ligne.setReservation(this);
    }

    // CONFIRMEE -> ANNULEE ; fige la date d'annulation et la pénalité au moment de l'appel
    public void annuler() {
        exigerConfirmee("annulée");
        Instant maintenant = Instant.now();
        this.montantPenalite = calculerPenalite(maintenant);
        this.dateAnnulation = maintenant;
        this.statut = StatutReservation.ANNULEE;
    }

    // CONFIRMEE -> TERMINEE ; possible une fois le séjour commencé
    public void terminer() {
        exigerConfirmee("terminée");
        if (LocalDate.now(ZoneOffset.UTC).isBefore(dateArrivee)) {
            throw new RequeteInvalideException("Le séjour n'a pas encore commencé");
        }
        this.statut = StatutReservation.TERMINEE;
    }

    // Pénalité si on annulait maintenant
    public BigDecimal calculerPenalite() {
        return calculerPenalite(Instant.now());
    }

    // Gratuit jusqu'à (début du jour d'arrivée - délai gratuit), ensuite pourcentage du prix total
    public BigDecimal calculerPenalite(Instant maintenant) {
        int pourcentage = politiqueAnnulation.getPourcentagePenalite();
        Instant limiteGratuite = dateArrivee.atStartOfDay(ZoneOffset.UTC).toInstant()
                .minus(Duration.ofHours(politiqueAnnulation.getDelaiGratuitHeures()));
        if (pourcentage == 0 || !maintenant.isAfter(limiteGratuite)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return prixTotal.multiply(BigDecimal.valueOf(pourcentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private void exigerConfirmee(String action) {
        if (statut != StatutReservation.CONFIRMEE) {
            throw new RequeteInvalideException(
                    "La réservation ne peut pas être " + action + " : statut actuel " + statut);
        }
    }
}
