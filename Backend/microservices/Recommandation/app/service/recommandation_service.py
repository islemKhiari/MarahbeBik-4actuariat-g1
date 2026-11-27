from sqlalchemy.orm import Session
from app.repository.preference_repository import PreferenceRepository
from app.repository.recommandation_repository import RecommandationRepository
from app.mapper.recommandation_mapper import RecommandationMapper
from app.exception.resource_not_found import ResourceNotFoundException
from app.exception.conflict import ConflictException

class RecommandationService:
    def __init__(self, db: Session):
        self.db = db
        self.repository = RecommandationRepository(db)
        self.preferences = PreferenceRepository(db)

    def find_all(self):
        return [RecommandationMapper.to_response(r) for r in self.repository.find_all()]
    def find_by_id(self, identifiant):
        return RecommandationMapper.to_response(self._get(identifiant))
    def create(self, dto):
        preference = self._preference(dto)
        entity = RecommandationMapper.to_entity(dto)
        entity.preference = preference  # back_populates synchronise les deux cotes
        result = RecommandationMapper.to_response(self.repository.save(entity))
        self.db.commit()
        return result
    def update(self, identifiant, dto):
        entity = self._get(identifiant)
        entity.preference = self._preference(dto)
        entity.voyageurId = dto.voyageurId
        entity.hebergementId = dto.hebergementId
        entity.scoreGlobal = dto.scoreGlobal
        result = RecommandationMapper.to_response(self.repository.save(entity))
        self.db.commit()
        return result
    def delete(self, identifiant):
        self.repository.delete(self._get(identifiant))
        self.db.commit()
    def _get(self, identifiant):
        entity = self.repository.find_by_id(identifiant)
        if entity is None:
            raise ResourceNotFoundException("Recommandation", identifiant)
        return entity
    def _preference(self, dto):
        preference = self.preferences.find_by_id(dto.idPreference)
        if preference is None:
            raise ResourceNotFoundException("PreferenceVoyageur", dto.idPreference)
        if preference.voyageurId != dto.voyageurId:
            raise ConflictException("La preference appartient a un autre voyageur")
        return preference
