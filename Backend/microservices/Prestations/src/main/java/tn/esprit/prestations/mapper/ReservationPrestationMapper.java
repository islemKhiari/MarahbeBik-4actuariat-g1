
package tn.esprit.prestations.mapper;

import tn.esprit.prestations.dto.ReservationPrestationDTO;
import tn.esprit.prestations.entity.ReservationPrestation;
import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.entity.StatutReservation;

import org.springframework.stereotype.Component;

@Component
public class ReservationPrestationMapper {

    // Entity vers DTO
    public ReservationPrestationDTO toDTO(
            ReservationPrestation reservation) {

        return new ReservationPrestationDTO(
                reservation.getId(),
                reservation.getVoyageurId(),
                reservation.getNombrePlaces(),
                reservation.getDateReservation(),
                reservation.getStatut() != null
                        ? reservation.getStatut().name()
                        : null,
                reservation.getPrestation() != null
                        ? reservation.getPrestation().getId()
                        : null
        );
    }

    // DTO vers Entity
    public ReservationPrestation toEntity(
            ReservationPrestationDTO dto) {

        ReservationPrestation reservation =
                new ReservationPrestation();

        reservation.setId(dto.getId());
        reservation.setVoyageurId(dto.getVoyageurId());
        reservation.setNombrePlaces(dto.getNombrePlaces());
        reservation.setDateReservation(dto.getDateReservation());

        if (dto.getStatut() != null) {
            reservation.setStatut(
                    StatutReservation.valueOf(dto.getStatut())
            );
        }

        if (dto.getPrestationId() != null) {
            Prestation prestation = new Prestation();
            prestation.setId(dto.getPrestationId());
            reservation.setPrestation(prestation);
        }

        return reservation;
    }
}
