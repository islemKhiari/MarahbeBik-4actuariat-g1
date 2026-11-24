
package tn.esprit.prestations.service;

import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.repository.PrestationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestationService {

    private final PrestationRepository prestationRepository;

    public PrestationService(PrestationRepository prestationRepository) {
        this.prestationRepository = prestationRepository;
    }

    public Prestation ajouterPrestation(Prestation prestation) {
        return prestationRepository.save(prestation);
    }

    public List<Prestation> afficherPrestations() {
        return prestationRepository.findAll();
    }

    public Optional<Prestation> afficherPrestationParId(Long id) {
        return prestationRepository.findById(id);
    }

    public Prestation modifierPrestation(Long id, Prestation nouvellePrestation) {
        return prestationRepository.findById(id)
                .map(prestation -> {
                    prestation.setTitre(nouvellePrestation.getTitre());
                    prestation.setDescription(nouvellePrestation.getDescription());
                    prestation.setPrix(nouvellePrestation.getPrix());
                    prestation.setDate(nouvellePrestation.getDate());
                    prestation.setCapaciteMax(nouvellePrestation.getCapaciteMax());
                    prestation.setHebergementId(nouvellePrestation.getHebergementId());
                    prestation.setCategorie(nouvellePrestation.getCategorie());

                    return prestationRepository.save(prestation);
                })
                .orElseThrow(() -> new RuntimeException("Prestation introuvable"));
    }

    public void supprimerPrestation(Long id) {
        prestationRepository.deleteById(id);
    }
}
