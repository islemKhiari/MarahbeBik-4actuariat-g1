package tn.esprit.ms2_reservation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

// L'hôte et le prix total ne sont pas envoyés : MS2 les calcule à partir de MS1
public record ReservationRequest(
        @NotNull Long identifiantHebergement,
        @NotNull Long identifiantChambre,
        @NotBlank String identifiantVoyageur,
        @NotNull LocalDate dateArrivee,
        @NotNull LocalDate dateDepart,
        @NotEmpty List<@Valid InviteRequest> voyageurs,
        Long politiqueAnnulationId   // facultatif : sinon la première politique active
) {}
