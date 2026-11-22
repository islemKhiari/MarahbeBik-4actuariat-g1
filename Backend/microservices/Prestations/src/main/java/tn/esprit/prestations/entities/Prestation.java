package tn.esprit.prestations.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Prestation {
    private Long id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private LocalDateTime date;
    private Integer capaciteMax;
    private Long hebergementId;
    private CategoriePrestation categorie;
}