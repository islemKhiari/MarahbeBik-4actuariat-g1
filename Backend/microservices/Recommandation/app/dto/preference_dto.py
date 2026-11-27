from decimal import Decimal
from pydantic import BaseModel, ConfigDict, Field, model_validator

class PreferenceRequest(BaseModel):
    model_config = ConfigDict(str_strip_whitespace=True, extra="forbid")
    voyageurId: str = Field(min_length=1, max_length=255)
    destination: str = Field(min_length=1, max_length=255)
    budgetMin: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    budgetMax: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    typeHebergement: str = Field(min_length=1, max_length=100)

    @model_validator(mode="after")
    def verifier_budget(self):
        if self.budgetMin > self.budgetMax:
            raise ValueError("budgetMin doit etre inferieur ou egal a budgetMax")
        return self

class PreferenceResponse(PreferenceRequest):
    idPreference: int
    recommandationIds: list[int]
