package tn.esprit.ms2_reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2_reservation.dto.PolitiqueAnnulationRequest;
import tn.esprit.ms2_reservation.dto.PolitiqueAnnulationResponse;
import tn.esprit.ms2_reservation.entity.PolitiqueAnnulation;
import tn.esprit.ms2_reservation.exception.RequeteInvalideException;
import tn.esprit.ms2_reservation.exception.RessourceIntrouvableException;
import tn.esprit.ms2_reservation.mapper.Mapper;
import tn.esprit.ms2_reservation.repository.PolitiqueAnnulationRepository;
import tn.esprit.ms2_reservation.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolitiqueAnnulationService {

    private final PolitiqueAnnulationRepository politiqueRepository;
    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<PolitiqueAnnulationResponse> lister() {
        return politiqueRepository.findAll().stream()
                .map(Mapper::toPolitiqueResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PolitiqueAnnulationResponse trouver(Long id) {
        return Mapper.toPolitiqueResponse(chercher(id));
    }

    public PolitiqueAnnulationResponse creer(PolitiqueAnnulationRequest request) {
        if (politiqueRepository.existsByNom(request.nom())) {
            throw new RequeteInvalideException("Cette politique existe déjà : " + request.nom());
        }
        PolitiqueAnnulation politique = Mapper.toEntity(request);
        return Mapper.toPolitiqueResponse(politiqueRepository.save(politique));
    }

    public PolitiqueAnnulationResponse modifier(Long id, PolitiqueAnnulationRequest request) {
        PolitiqueAnnulation politique = chercher(id);
        if (!politique.getNom().equals(request.nom()) && politiqueRepository.existsByNom(request.nom())) {
            throw new RequeteInvalideException("Cette politique existe déjà : " + request.nom());
        }
        Mapper.updateEntity(politique, request);
        return Mapper.toPolitiqueResponse(politique);
    }

    public void supprimer(Long id) {
        PolitiqueAnnulation politique = chercher(id);
        if (reservationRepository.existsByPolitiqueAnnulationIdentifiant(id)) {
            throw new RequeteInvalideException(
                    "Politique utilisée par des réservations : désactivez-la au lieu de la supprimer");
        }
        politiqueRepository.delete(politique);
    }

    // Utilisé aussi par ReservationService
    public PolitiqueAnnulation chercher(Long id) {
        return politiqueRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Politique d'annulation introuvable : " + id));
    }

    // Politique demandée (doit être active), sinon la première politique active
    public PolitiqueAnnulation choisirPourReservation(Long id) {
        if (id != null) {
            PolitiqueAnnulation politique = chercher(id);
            if (!Boolean.TRUE.equals(politique.getActive())) {
                throw new RequeteInvalideException("Cette politique d'annulation n'est pas active : " + id);
            }
            return politique;
        }
        return politiqueRepository.findFirstByActiveTrueOrderByIdentifiantAsc()
                .orElseThrow(() -> new RequeteInvalideException("Aucune politique d'annulation active"));
    }
}
