from datetime import datetime
from pydantic import BaseModel, ConfigDict, Field

class RecommandationRequest(BaseModel):
    model_config = ConfigDict(str_strip_whitespace=True, extra="forbid", allow_inf_nan=False)
    idPreference: int = Field(gt=0)
    voyageurId: str = Field(min_length=1, max_length=255)
    hebergementId: int = Field(gt=0)
    scoreGlobal: float

class RecommandationResponse(RecommandationRequest):
    idRecommendation: int
    dateGeneration: datetime
