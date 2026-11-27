from datetime import datetime, timezone
from app.dto.recommandation_dto import RecommandationRequest, RecommandationResponse
from app.entity import Recommandation

class RecommandationMapper:
    @staticmethod
    def to_entity(dto: RecommandationRequest):
        return Recommandation(**dto.model_dump(), dateGeneration=datetime.now(timezone.utc).replace(tzinfo=None))

    @staticmethod
    def to_response(entity: Recommandation):
        return RecommandationResponse(idRecommendation=entity.idRecommendation, idPreference=entity.idPreference,
            voyageurId=entity.voyageurId, hebergementId=entity.hebergementId, scoreGlobal=entity.scoreGlobal,
            dateGeneration=entity.dateGeneration)
