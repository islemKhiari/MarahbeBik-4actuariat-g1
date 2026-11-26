package tn.esprit.ms2_reservation.mapper;

import tn.esprit.ms2_reservation.dto.*;
import tn.esprit.ms2_reservation.entity.*;

/** Converts between entities and DTOs. */
public final class Mapper {

    private Mapper() {
    }

    // ---------- PolitiqueAnnulation ----------

    public static PolitiqueAnnulation toEntity(PolitiqueAnnulationRequest request) {
        PolitiqueAnnulation politique = new PolitiqueAnnulation();
        updateEntity(politique, request);
        return politique;
    }

    public static void updateEntity(PolitiqueAnnulation politique, PolitiqueAnnulationRequest request) {
        politique.setNom(request.nom());
        politique.setDelaiGratuitHeures(request.delaiGratuitHeures());
        politique.setPourcentagePenalite(request.pourcentagePenalite());
        politique.setActive(request.active());
    }

    public static PolitiqueAnnulationResponse toPolitiqueResponse(PolitiqueAnnulation p) {
        return new PolitiqueAnnulationResponse(
                p.getIdentifiant(), p.getNom(), p.getDelaiGratuitHeures(),
                p.getPourcentagePenalite(), p.getActive());
    }

    // ---------- InviteReservation ----------

    public static InviteReservation toEntity(InviteRequest request) {
        InviteReservation invite = new InviteReservation();
        invite.setNomComplet(request.nomComplet());
        invite.setAge(request.age());
        return invite;
    }

    public static InviteResponse toInviteResponse(InviteReservation i) {
        return new InviteResponse(i.getIdentifiant(), i.getNomComplet(), i.getAge());
    }

    // ---------- HistoriqueStatut ----------

    public static HistoriqueStatutResponse toHistoriqueResponse(HistoriqueStatut h) {
        return new HistoriqueStatutResponse(
                h.getIdentifiant(), h.getAncienStatut(), h.getNouveauStatut(),
                h.getModifieLe(), h.getModifiePar(), h.getMotif());
    }

    // ---------- BlocDisponibilite ----------

    public static BlocDisponibilite toEntity(BlocRequest request) {
        BlocDisponibilite bloc = new BlocDisponibilite();
        bloc.setIdentifiantChambre(request.identifiantChambre());
        bloc.setDateDebut(request.dateDebut());
        bloc.setDateFin(request.dateFin());
        bloc.setMotif(request.motif());
        return bloc;
    }

    public static BlocResponse toBlocResponse(BlocDisponibilite b) {
        return new BlocResponse(
                b.getIdentifiant(), b.getIdentifiantChambre(), b.getIdentifiantReservation(),
                b.getDateDebut(), b.getDateFin(), b.getMotif());
    }

    // ---------- Reservation ----------
    // L'hôte, le prix, la politique, les invités et l'historique sont renseignés dans le service

    public static ReservationResponse toReservationResponse(Reservation r) {
        return new ReservationResponse(
                r.getIdentifiant(),
                r.getIdentifiantHebergement(),
                r.getIdentifiantChambre(),
                r.getIdentifiantVoyageur(),
                r.getIdentifiantHote(),
                r.getDateArrivee(),
                r.getDateDepart(),
                r.getPrixTotal(),
                r.getStatut(),
                r.getDateAnnulation(),
                r.getMontantPenalite(),
                r.getPolitiqueAnnulation().getIdentifiant(),
                r.getVoyageurs().stream().map(Mapper::toInviteResponse).toList(),
                r.getHistorique().stream().map(Mapper::toHistoriqueResponse).toList()
        );
    }
}
