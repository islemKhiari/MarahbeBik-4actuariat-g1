package tn.esprit.prestations.entities;

import java.time.LocalDateTime;

public class ReservationPrestation {
    private Long id;
    private Long voyageurId;
    private Integer nombrePlaces;
    private LocalDateTime dateReservation;
    private StatutReservation statut;
    private Prestation prestation;
}