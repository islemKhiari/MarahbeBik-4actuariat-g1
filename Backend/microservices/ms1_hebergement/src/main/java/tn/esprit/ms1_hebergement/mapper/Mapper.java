package tn.esprit.ms1_hebergement.mapper;
//commentaire
import tn.esprit.ms1_hebergement.dto.*;
import tn.esprit.ms1_hebergement.entity.Chambre;
import tn.esprit.ms1_hebergement.entity.Equipement;
import tn.esprit.ms1_hebergement.entity.MaisonHote;

import java.util.ArrayList;

/** Converts between entities and DTOs. */
public final class Mapper {

    private Mapper() {
    }

    // ---------- Equipement ----------

    public static Equipement toEntity(EquipementRequest request) {
        Equipement equipement = new Equipement();
        updateEntity(equipement, request);
        return equipement;
    }

    public static void updateEntity(Equipement equipement, EquipementRequest request) {
        equipement.setType(request.type());
        equipement.setSupplement(request.supplement());
    }

    public static EquipementResponse toEquipementResponse(Equipement e) {
        return new EquipementResponse(e.getId(), e.getType(), e.getSupplement());
    }

    // ---------- Chambre ----------
    // La maison et les équipements sont rattachés dans le service

    public static Chambre toEntity(ChambreRequest request) {
        Chambre chambre = new Chambre();
        updateEntity(chambre, request);
        return chambre;
    }

    public static void updateEntity(Chambre chambre, ChambreRequest request) {
        chambre.setNom(request.nom());
        chambre.setType(request.type());
        chambre.setCapacite(request.capacite());
        chambre.setDateDebut(request.dateDebut());
        chambre.setDateFin(request.dateFin());
        chambre.setPrixParNuit(request.prixParNuit());
    }

    public static ChambreResponse toChambreResponse(Chambre c) {
        return new ChambreResponse(
                c.getId(),
                c.getMaison().getId(),
                c.getNom(),
                c.getType(),
                c.getCapacite(),
                c.getDateDebut(),
                c.getDateFin(),
                c.getPrixParNuit(),
                c.getEquipements().stream().map(Mapper::toEquipementResponse).toList()
        );
    }

    // ---------- MaisonHote ----------

    public static MaisonHote toEntity(MaisonRequest request) {
        MaisonHote maison = new MaisonHote();
        maison.setProprietaireId(request.proprietaireId());
        updateEntity(maison, request);
        return maison;
    }

    // Le propriétaire ne change pas après la création
    public static void updateEntity(MaisonHote maison, MaisonRequest request) {
        maison.setNom(request.nom());
        maison.setDescription(request.description());
        maison.setVille(request.ville());
        maison.setAdresse(request.adresse());
        maison.getPhotos().clear();
        if (request.photos() != null) {
            maison.getPhotos().addAll(request.photos());
        }
    }

    public static MaisonResponse toMaisonResponse(MaisonHote m) {
        return new MaisonResponse(
                m.getId(),
                m.getNom(),
                m.getDescription(),
                m.getVille(),
                m.getAdresse(),
                new ArrayList<>(m.getPhotos()),
                m.getProprietaireId(),
                m.getChambres().stream().map(Mapper::toChambreResponse).toList()
        );
    }
}