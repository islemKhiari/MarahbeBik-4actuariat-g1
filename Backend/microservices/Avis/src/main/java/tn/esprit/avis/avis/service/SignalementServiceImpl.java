package tn.esprit.avis.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.avis.avis.dto.SignalementRequestDTO;
import tn.esprit.avis.avis.dto.SignalementResponseDTO;
import tn.esprit.avis.avis.entity.*;
import tn.esprit.avis.avis.mapper.AvisMapper;
import tn.esprit.avis.avis.repository.AvisRepository;
import tn.esprit.avis.avis.repository.SignalementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SignalementServiceImpl implements ISignalementService {

    private final SignalementRepository signalementRepository;
    private final AvisRepository avisRepository;
    private final AvisMapper avisMapper;

    @Override
    public SignalementResponseDTO signalerAvis(Long avisId, SignalementRequestDTO dto) {
        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable"));

        if (signalementRepository.existsByAvisIdAndSignaleurId(avisId, dto.getSignaleurId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vous avez déjà signalé cet avis");
        }

        Signalement signalement = Signalement.builder()
                .signaleurId(dto.getSignaleurId())
                .motif(dto.getMotif())
                .statut(StatutSignalement.EN_ATTENTE)
                .avis(avis)
                .build();

        return avisMapper.toSignalementDTO(signalementRepository.save(signalement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDTO> getAllSignalements() {
        return signalementRepository.findAll().stream()
                .map(avisMapper::toSignalementDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDTO> getSignalementsParAvis(Long avisId) {
        if (!avisRepository.existsById(avisId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avis introuvable");
        }
        return signalementRepository.findByAvisId(avisId).stream()
                .map(avisMapper::toSignalementDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDTO> getSignalementsParStatut(StatutSignalement statut) {
        return signalementRepository.findByStatut(statut).stream()
                .map(avisMapper::toSignalementDTO)
                .toList();
    }

    @Override
    public SignalementResponseDTO traiterSignalement(Long signalementId, StatutSignalement nouveauStatut) {
        Signalement signalement = signalementRepository.findById(signalementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Signalement introuvable"));

        signalement.setStatut(nouveauStatut);

        // Si le signalement est validé (TRAITE), on masque l'avis correspondant
        if (nouveauStatut == StatutSignalement.TRAITE) {
            Avis avis = signalement.getAvis();
            avis.setStatut(StatutAvis.MASQUE);
            avisRepository.save(avis);
        }

        return avisMapper.toSignalementDTO(signalementRepository.save(signalement));
    }

    @Override
    public void supprimerSignalement(Long signalementId) {
        if (!signalementRepository.existsById(signalementId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Signalement introuvable");
        }
        signalementRepository.deleteById(signalementId);
    }
}