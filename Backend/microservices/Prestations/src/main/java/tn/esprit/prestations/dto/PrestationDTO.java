package tn.esprit.prestations.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestationDTO {

    private Long id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private LocalDateTime date;
    private Integer capaciteMax;
    private Long hebergementId;
    private String categorie;

    private List<Long> reservationIds;
}