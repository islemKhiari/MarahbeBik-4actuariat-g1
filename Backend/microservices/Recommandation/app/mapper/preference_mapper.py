from app.dto.preference_dto import PreferenceRequest, PreferenceResponse
from app.entity import PreferenceVoyageur

class PreferenceMapper:
    @staticmethod
    def to_entity(dto: PreferenceRequest):
        return PreferenceVoyageur(**dto.model_dump())

    @staticmethod
    def to_response(entity: PreferenceVoyageur):
        return PreferenceResponse(idPreference=entity.idPreference, voyageurId=entity.voyageurId,
            destination=entity.destination, budgetMin=entity.budgetMin, budgetMax=entity.budgetMax,
            typeHebergement=entity.typeHebergement,
            recommandationIds=[r.idRecommendation for r in entity.recommandations])
