package tn.esprit.ms2_reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2_reservation.dto.BlocRequest;
import tn.esprit.ms2_reservation.dto.BlocResponse;
import tn.esprit.ms2_reservation.dto.DisponibiliteResponse;
import tn.esprit.ms2_reservation.entity.BlocDisponibilite;
import tn.esprit.ms2_reservation.exception.RequeteInvalideException;
import tn.esprit.ms2_reservation.exception.RessourceIntrouvableException;
import tn.esprit.ms2_reservation.mapper.Mapper;
import tn.esprit.ms2_reservation.repository.BlocDisponibiliteRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlocDisponibiliteService {

    private final BlocDisponibiliteRepository blocRepository;

    @Transactional(readOnly = true)
    public List<BlocResponse> listerParChambre(Long chambreId) {
        return blocRepository.findByIdentifiantChambreOrderByDateDebut(chambreId).stream()
                .map(Mapper::toBlocResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisponibiliteResponse verifierDisponibilite(Long chambreId, LocalDate arrivee, LocalDate depart) {
        verifierPeriode(arrivee, depart);
        boolean disponible = estDisponible(chambreId, arrivee, depart);
        return new DisponibiliteResponse(chambreId, arrivee, depart, disponible);
    }

    // Blocage manuel par l'hôte (maintenance, indisponibilité...)
    public BlocResponse creerManuel(BlocRequest request) {
        verifierPeriode(request.dateDebut(), request.dateFin());
        if (!estDisponible(request.identifiantChambre(), request.dateDebut(), request.dateFin())) {
            throw new RequeteInvalideException("La chambre est déjà occupée sur cette période");
        }
        BlocDisponibilite bloc = Mapper.toEntity(request);
        return Mapper.toBlocResponse(blocRepository.save(bloc));
    }

    // Seuls les blocs manuels se suppriment ici ; ceux d'une réservation disparaissent avec son annulation
    public void supprimerManuel(Long id) {
        BlocDisponibilite bloc = blocRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Bloc introuvable : " + id));
        if (bloc.getIdentifiantReservation() != null) {
            throw new RequeteInvalideException("Ce bloc appartient à la réservation "
                    + bloc.getIdentifiantReservation() + " : annulez la réservation");
        }
        blocRepository.delete(bloc);
    }

    public boolean estDisponible(Long chambreId, LocalDate arrivee, LocalDate depart) {
        return !blocRepository.existeChevauchement(chambreId, arrivee, depart);
    }

    // Bloc créé en même temps qu'une réservation
    public void bloquerPourReservation(Long chambreId, Long reservationId, LocalDate arrivee, LocalDate depart) {
        BlocDisponibilite bloc = new BlocDisponibilite();
        bloc.setIdentifiantChambre(chambreId);
        bloc.setIdentifiantReservation(reservationId);
        bloc.setDateDebut(arrivee);
        bloc.setDateFin(depart);
        bloc.setMotif(BlocDisponibilite.MOTIF_RESERVATION);
        blocRepository.save(bloc);
    }

    public void libererPourReservation(Long reservationId) {
        blocRepository.findByIdentifiantReservation(reservationId).ifPresent(blocRepository::delete);
    }

    private void verifierPeriode(LocalDate debut, LocalDate fin) {
        if (!fin.isAfter(debut)) {
            throw new RequeteInvalideException("La date de fin doit être après la date de début");
        }
    }
}
