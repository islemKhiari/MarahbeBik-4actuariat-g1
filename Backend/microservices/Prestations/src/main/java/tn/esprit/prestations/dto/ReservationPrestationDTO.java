
package tn.esprit.prestations.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPrestationDTO {

    private Long id;
    private Long voyageurId;
    private Integer nombrePlaces;
    private LocalDateTime dateReservation;
    private String statut;
    private Long prestationId;
}
