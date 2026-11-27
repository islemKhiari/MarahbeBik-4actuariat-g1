package tn.esprit.ms1_hebergement.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
//commentaire
@Entity
@Table(name = "equipement")
@Getter
@Setter
@NoArgsConstructor
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TypeEquipement type;

    // 0 = inclus dans le prix ; > 0 = supplément facturé
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal supplement = BigDecimal.ZERO;

    @ManyToMany(mappedBy = "equipements")
    private Set<Chambre> chambres = new HashSet<>();
}