package tn.esprit.ms2_reservation.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2_reservation.client.HebergementClient;
import tn.esprit.ms2_reservation.client.HebergementClient.ChambreDistante;
import tn.esprit.ms2_reservation.client.HebergementClient.EquipementDistant;
import tn.esprit.ms2_reservation.client.HebergementClient.MaisonDistante;
import tn.esprit.ms2_reservation.dto.ChangementStatutRequest;
import tn.esprit.ms2_reservation.dto.ReservationRequest;
import tn.esprit.ms2_reservation.dto.ReservationResponse;
import tn.esprit.ms2_reservation.entity.HistoriqueStatut;
import tn.esprit.ms2_reservation.entity.Reservation;
import tn.esprit.ms2_reservation.entity.StatutReservation;
import tn.esprit.ms2_reservation.exception.RequeteInvalideException;
import tn.esprit.ms2_reservation.exception.RessourceIntrouvableException;
import tn.esprit.ms2_reservation.exception.ServiceIndisponibleException;
import tn.esprit.ms2_reservation.mapper.Mapper;
import tn.esprit.ms2_reservation.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PolitiqueAnnulationService politiqueService;
    private final BlocDisponibiliteService blocService;
    private final HebergementClient hebergementClient;

    // READ : toutes les réservations, ou filtrées (voyageur, hôte, statut)
    @Transactional(readOnly = true)
    public List<ReservationResponse> lister(String voyageurId, String hoteId, StatutReservation statut) {
        List<Reservation> reservations;
        if (voyageurId != null) {
            reservations = reservationRepository.findByIdentifiantVoyageur(voyageurId);
        } else if (hoteId != null) {
            reservations = reservationRepository.findByIdentifiantHote(hoteId);
        } else {
            reservations = reservationRepository.findAll();
        }
        return reservations.stream()
                .filter(r -> hoteId == null || r.getIdentifiantHote().equals(hoteId))
                .filter(r -> statut == null || r.getStatut() == statut)
                .map(Mapper::toReservationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse trouver(Long id) {
        return Mapper.toReservationResponse(chercher(id));
    }

    // CREATE : vérifie MS1 (chambre, capacité, prix), la disponibilité, puis réserve la chambre
    public ReservationResponse creer(ReservationRequest request) {
        if (request.dateArrivee().isBefore(LocalDate.now(ZoneOffset.UTC))) {
            throw new RequeteInvalideException("dateArrivee ne peut pas être dans le passé");
        }
        if (!request.dateDepart().isAfter(request.dateArrivee())) {
            throw new RequeteInvalideException("dateDepart doit être après dateArrivee");
        }

        MaisonDistante maison = chargerMaison(request.identifiantHebergement());
        ChambreDistante chambre = maison.chambres().stream()
                .filter(c -> Objects.equals(c.id(), request.identifiantChambre()))
                .findFirst()
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Chambre " + request.identifiantChambre() + " introuvable dans l'hébergement "
                                + request.identifiantHebergement()));

        if (request.voyageurs().size() > chambre.capacite()) {
            throw new RequeteInvalideException("Capacité de la chambre dépassée : "
                    + request.voyageurs().size() + " voyageurs pour " + chambre.capacite() + " places");
        }
        if (!blocService.estDisponible(chambre.id(), request.dateArrivee(), request.dateDepart())) {
            throw new RequeteInvalideException("La chambre n'est pas disponible sur cette période");
        }

        Reservation reservation = new Reservation();
        reservation.setIdentifiantHebergement(maison.id());
        reservation.setIdentifiantChambre(chambre.id());
        reservation.setIdentifiantVoyageur(request.identifiantVoyageur());
        reservation.setIdentifiantHote(maison.proprietaireId());
        reservation.setDateArrivee(request.dateArrivee());
        reservation.setDateDepart(request.dateDepart());
        reservation.setPrixTotal(calculerPrixTotal(chambre, request.dateArrivee(), request.dateDepart()));
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setPolitiqueAnnulation(politiqueService.choisirPourReservation(request.politiqueAnnulationId()));
        request.voyageurs().forEach(v -> reservation.ajouterVoyageur(Mapper.toEntity(v)));
        reservation.ajouterHistorique(ligneHistorique(null, StatutReservation.CONFIRMEE,
                request.identifiantVoyageur(), "Création de la réservation"));

        Reservation enregistree = reservationRepository.save(reservation);
        blocService.bloquerPourReservation(chambre.id(), enregistree.getIdentifiant(),
                request.dateArrivee(), request.dateDepart());
        return Mapper.toReservationResponse(enregistree);
    }

    // CONFIRMEE -> ANNULEE : calcule la pénalité et libère la chambre
    public ReservationResponse annuler(Long id, ChangementStatutRequest request) {
        Reservation reservation = chercher(id);
        StatutReservation ancien = reservation.getStatut();
        reservation.annuler();
        reservation.ajouterHistorique(ligneHistorique(ancien, reservation.getStatut(),
                request.modifiePar(), request.motif()));
        blocService.libererPourReservation(id);
        return Mapper.toReservationResponse(reservation);
    }

    // CONFIRMEE -> TERMINEE
    public ReservationResponse terminer(Long id, ChangementStatutRequest request) {
        Reservation reservation = chercher(id);
        StatutReservation ancien = reservation.getStatut();
        reservation.terminer();
        reservation.ajouterHistorique(ligneHistorique(ancien, reservation.getStatut(),
                request.modifiePar(), request.motif()));
        return Mapper.toReservationResponse(reservation);
    }

    private Reservation chercher(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Réservation introuvable : " + id));
    }

    // Appel OpenFeign vers MS1
    private MaisonDistante chargerMaison(Long maisonId) {
        try {
            return hebergementClient.trouverMaison(maisonId);
        } catch (FeignException.NotFound e) {
            throw new RessourceIntrouvableException("Hébergement introuvable : " + maisonId);
        } catch (FeignException e) {
            throw new ServiceIndisponibleException("Le service hébergement est indisponible");
        }
    }

    // nombre de nuits x (prix par nuit + suppléments des équipements de la chambre)
    private BigDecimal calculerPrixTotal(ChambreDistante chambre, LocalDate arrivee, LocalDate depart) {
        long nuits = ChronoUnit.DAYS.between(arrivee, depart);
        BigDecimal supplements = chambre.equipements() == null ? BigDecimal.ZERO
                : chambre.equipements().stream()
                        .map(EquipementDistant::supplement)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        return chambre.prixParNuit().add(supplements)
                .multiply(BigDecimal.valueOf(nuits))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private HistoriqueStatut ligneHistorique(StatutReservation ancien, StatutReservation nouveau,
                                             String modifiePar, String motif) {
        HistoriqueStatut ligne = new HistoriqueStatut();
        ligne.setAncienStatut(ancien);
        ligne.setNouveauStatut(nouveau);
        ligne.setModifieLe(Instant.now());
        ligne.setModifiePar(modifiePar);
        ligne.setMotif(motif);
        return ligne;
    }
}
