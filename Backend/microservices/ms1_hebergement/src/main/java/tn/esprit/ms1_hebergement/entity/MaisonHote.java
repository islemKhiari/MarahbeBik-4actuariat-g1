package tn.esprit.ms1_hebergement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maison_hote")
@Getter
@Setter
@NoArgsConstructor
public class MaisonHote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String adresse;

    // Liste d'URLs : la première est la photo principale (@OrderColumn garde l'ordre)
    @ElementCollection
    @CollectionTable(name = "maison_photo", joinColumns = @JoinColumn(name = "maison_id"))
    @OrderColumn(name = "position")
    @Column(name = "url", nullable = false)
    private List<String> photos = new ArrayList<>();

    // id de l'utilisateur Keycloak : simple texte, pas de clé étrangère
    @Column(name = "proprietaire_id", nullable = false)
    private String proprietaireId;

    @OneToMany(mappedBy = "maison", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chambre> chambres = new ArrayList<>();

    public void ajouterChambre(Chambre chambre) {
        chambres.add(chambre);
        chambre.setMaison(this);
    }

    public void retirerChambre(Chambre chambre) {
        chambres.remove(chambre);
        chambre.setMaison(null);
    }
}