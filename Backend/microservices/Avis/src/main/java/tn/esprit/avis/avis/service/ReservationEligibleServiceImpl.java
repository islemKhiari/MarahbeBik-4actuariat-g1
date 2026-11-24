package tn.esprit.avis.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.avis.avis.entity.ReservationEligible;
import tn.esprit.avis.avis.repository.ReservationEligibleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationEligibleServiceImpl implements IReservationEligibleService {

    private final ReservationEligibleRepository reservationEligibleRepository;

    @Override
    public ReservationEligible enregistrerReservationEligible(ReservationEligible reservation) {
        // Idempotence lors de la réception de ReservationCompleted depuis MS2
        return reservationEligibleRepository.findById(reservation.getReservationId())
                .orElseGet(() -> reservationEligibleRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifierEligibilite(Long reservationId) {
        return reservationEligibleRepository.existsById(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationEligible getById(Long reservationId) {
        return reservationEligibleRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation éligible introuvable"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationEligible> getAll() {
        return reservationEligibleRepository.findAll();
    }
}