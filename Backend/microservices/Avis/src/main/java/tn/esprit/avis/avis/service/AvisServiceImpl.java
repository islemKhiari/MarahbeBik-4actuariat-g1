package tn.esprit.avis.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.avis.avis.dto.*;
import tn.esprit.avis.avis.entity.*;
import tn.esprit.avis.avis.mapper.AvisMapper;
import tn.esprit.avis.avis.repository.AvisRepository;
import tn.esprit.avis.avis.repository.ReservationEligibleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AvisServiceImpl implements IAvisService {

    private final AvisRepository avisRepository;
    private final ReservationEligibleRepository reservationEligibleRepository;
    private final AvisMapper avisMapper;

    @Override
    public AvisResponseDTO creerAvis(AvisRequestDTO dto) {
        ReservationEligible eligible = reservationEligibleRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Réservation non éligible ou séjour non terminé"));

        if (!eligible.getVoyageurId().equals(dto.getVoyageurId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Ce voyageur n'est pas autorisé à noter cette réservation");
        }

        if (avisRepository.existsByReservationId(dto.getReservationId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Un avis existe déjà pour cette réservation");
        }

        Avis avis = avisMapper.toEntity(dto);
        avis.setMaisonId(eligible.getMaisonId());
        avis.setStatut(StatutAvis.PUBLIE);

        return avisMapper.toDTO(avisRepository.save(avis));
    }

    @Override
    public AvisResponseDTO modifierAvis(Long avisId, AvisUpdateDTO dto) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));

        if (!avis.getVoyageurId().equals(dto.getVoyageurId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul l'auteur peut modifier cet avis");
        }

        avis.setNote(dto.getNote());
        avis.setCommentaire(dto.getCommentaire());
        return avisMapper.toDTO(avisRepository.save(avis));
    }

    @Override
    public void supprimerAvis(Long avisId) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));
        // La composition supprime automatiquement les signalements liés
        avisRepository.delete(avis);
    }

    @Override
    @Transactional(readOnly = true)
    public AvisResponseDTO getAvisById(Long id) {
        return avisRepository.findById(id)
                .map(avisMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAllAvis() {
        return avisRepository.findAll().stream().map(avisMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAvisPubliesParMaison(Long maisonId) {
        return avisRepository.findByMaisonIdAndStatut(maisonId, StatutAvis.PUBLIE)
                .stream().map(avisMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getMoyenneNoteMaison(Long maisonId) {
        Double moyenne = avisRepository.calculerMoyenneNoteMaison(maisonId);
        return moyenne != null ? Math.round(moyenne * 10.0) / 10.0 : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAvisParVoyageur(String voyageurId) {
        return avisRepository.findByVoyageurId(voyageurId).stream().map(avisMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvisResponseDTO> getAvisParProprietaire(String proprietaireId) {
        return avisRepository.findByProprietaireId(proprietaireId).stream().map(avisMapper::toDTO).toList();
    }

    @Override
    public AvisResponseDTO changerStatutAvis(Long avisId, StatutAvis nouveauStatut) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));
        avis.setStatut(nouveauStatut);
        return avisMapper.toDTO(avisRepository.save(avis));
    }

    @Override
    public AvisResponseDTO repondreAvis(Long avisId, ReponseRequestDTO dto) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));

        if (!avis.getProprietaireId().equals(dto.getProprietaireId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul le propriétaire concerné peut répondre");
        }
        if (avis.getReponseContenu() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Une réponse a déjà été publiée");
        }

        avis.setReponseContenu(dto.getReponseContenu());
        avis.setDateReponse(LocalDateTime.now());
        return avisMapper.toDTO(avisRepository.save(avis));
    }
}