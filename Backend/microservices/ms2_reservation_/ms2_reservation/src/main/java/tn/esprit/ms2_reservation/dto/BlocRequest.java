package tn.esprit.ms2_reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Blocage manuel d'une chambre par l'hôte (dateFin = jour où la chambre redevient libre)
public record BlocRequest(
        @NotNull Long identifiantChambre,
        @NotNull LocalDate dateDebut,
        @NotNull LocalDate dateFin,
        @NotBlank String motif
) {}
