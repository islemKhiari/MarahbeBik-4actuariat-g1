
package tn.esprit.prestations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal prix;

    private LocalDateTime date;

    private Integer capaciteMax;

    private Long hebergementId;

    @Enumerated(EnumType.STRING)
    private CategoriePrestation categorie;
}
