package tn.esprit.prestations.mapper;

import tn.esprit.prestations.dto.PrestationDTO;
import tn.esprit.prestations.entity.Prestation;
import tn.esprit.prestations.entity.CategoriePrestation;
import tn.esprit.prestations.entity.ReservationPrestation;

import org.springframework.stereotype.Component;

@Component
public class PrestationMapper {

    // Entity vers DTO
    public PrestationDTO toDTO(Prestation prestation) {

        return new PrestationDTO(
                prestation.getId(),
                prestation.getTitre(),
                prestation.getDescription(),
                prestation.getPrix(),
                prestation.getDate(),
                prestation.getCapaciteMax(),
                prestation.getHebergementId(),

                prestation.getCategorie() != null
                        ? prestation.getCategorie().name()
                        : null,

                prestation.getReservations()
                        .stream()
                        .map(ReservationPrestation::getId)
                        .toList()
        );
    }

    // DTO vers Entity
    public Prestation toEntity(PrestationDTO dto) {

        Prestation prestation = new Prestation();

        prestation.setId(dto.getId());
        prestation.setTitre(dto.getTitre());
        prestation.setDescription(dto.getDescription());
        prestation.setPrix(dto.getPrix());
        prestation.setDate(dto.getDate());
        prestation.setCapaciteMax(dto.getCapaciteMax());
        prestation.setHebergementId(dto.getHebergementId());

        if (dto.getCategorie() != null) {
            prestation.setCategorie(
                    CategoriePrestation.valueOf(dto.getCategorie())
            );
        }

        return prestation;
    }
}