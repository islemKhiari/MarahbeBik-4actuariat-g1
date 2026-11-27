from sqlalchemy.orm import Session
from app.repository.preference_repository import PreferenceRepository
from app.repository.recommandation_repository import RecommandationRepository
from app.mapper.preference_mapper import PreferenceMapper
from app.exception.resource_not_found import ResourceNotFoundException
from app.exception.conflict import ConflictException

class PreferenceService:
    def __init__(self, db: Session):
        self.db = db
        self.repository = PreferenceRepository(db)
        self.recommandations = RecommandationRepository(db)

    def find_all(self):
        return [PreferenceMapper.to_response(p) for p in self.repository.find_all()]
    def find_by_id(self, identifiant):
        return PreferenceMapper.to_response(self._get(identifiant))
    def create(self, dto):
        entity = self.repository.save(PreferenceMapper.to_entity(dto))
        result = PreferenceMapper.to_response(entity)
        self.db.commit()
        return result
    def update(self, identifiant, dto):
        entity = self._get(identifiant)
        if entity.voyageurId != dto.voyageurId and self.recommandations.exists_by_preference_id(identifiant):
            raise ConflictException("Impossible de changer le voyageur d'une preference avec des recommandations")
        for key, value in dto.model_dump().items():
            setattr(entity, key, value)
        result = PreferenceMapper.to_response(self.repository.save(entity))
        self.db.commit()
        return result
    def delete(self, identifiant):
        entity = self._get(identifiant)
        if self.recommandations.exists_by_preference_id(identifiant):
            raise ConflictException("Impossible de supprimer une preference ayant des recommandations")
        self.repository.delete(entity)
        self.db.commit()
    def _get(self, identifiant):
        entity = self.repository.find_by_id(identifiant)
        if entity is None:
            raise ResourceNotFoundException("PreferenceVoyageur", identifiant)
        return entity
