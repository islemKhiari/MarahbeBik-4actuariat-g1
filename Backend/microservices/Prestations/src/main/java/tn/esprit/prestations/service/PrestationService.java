package tn.esprit.prestations.service;

import tn.esprit.prestations.dto.PrestationDTO;
import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.exception.ConflictException;
import tn.esprit.prestations.exception.ResourceNotFoundException;
import tn.esprit.prestations.mapper.PrestationMapper;
import tn.esprit.prestations.repository.PrestationRepository;
import tn.esprit.prestations.repository.ReservationPrestationRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PrestationService {

    private final PrestationRepository prestationRepository;
    private final ReservationPrestationRepository reservationRepository;
    private final PrestationMapper prestationMapper;

    // Ajouter une prestation
    public PrestationDTO ajouterPrestation(PrestationDTO dto) {

        Prestation prestation = prestationMapper.toEntity(dto);
        prestation.setId(null);

        return prestationMapper.toDTO(
                prestationRepository.save(prestation));
    }

    // Afficher toutes les prestations
    @Transactional(readOnly = true)
    public List<PrestationDTO> afficherPrestations() {

        return prestationRepository.findAll()
                .stream()
                .map(prestationMapper::toDTO)
                .toList();
    }

    // Afficher une prestation par ID
    @Transactional(readOnly = true)
    public PrestationDTO afficherPrestationParId(Long id) {

        return prestationMapper.toDTO(getPrestation(id));
    }

    // Modifier une prestation
    public PrestationDTO modifierPrestation(
            Long id, PrestationDTO dto) {

        Prestation prestation = getPrestation(id);
        Prestation nouvelle = prestationMapper.toEntity(dto);

        prestation.setTitre(nouvelle.getTitre());
        prestation.setDescription(nouvelle.getDescription());
        prestation.setPrix(nouvelle.getPrix());
        prestation.setDate(nouvelle.getDate());
        prestation.setCapaciteMax(nouvelle.getCapaciteMax());
        prestation.setHebergementId(nouvelle.getHebergementId());
        prestation.setCategorie(nouvelle.getCategorie());

        return prestationMapper.toDTO(
                prestationRepository.save(prestation));
    }

    // Supprimer une prestation
    public void supprimerPrestation(Long id) {

        Prestation prestation = getPrestation(id);

        if (reservationRepository.existsByPrestation_Id(id)) {
            throw new ConflictException(
                    "Impossible de supprimer une prestation "
                            + "ayant des reservations");
        }

        prestationRepository.delete(prestation);
    }

    // Recuperer une prestation ou declencher une exception
    private Prestation getPrestation(Long id) {

        return prestationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Prestation", id));
    }
}