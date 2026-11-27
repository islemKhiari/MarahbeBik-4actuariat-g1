from __future__ import annotations
from decimal import Decimal
from sqlalchemy import BigInteger, Integer, Numeric, String
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.config.database import Base

class PreferenceVoyageur(Base):
    __tablename__ = "preferences_voyageurs"
    idPreference: Mapped[int] = mapped_column(BigInteger().with_variant(Integer, "sqlite"), primary_key=True, autoincrement=True)
    voyageurId: Mapped[str] = mapped_column(String(255), index=True)
    destination: Mapped[str] = mapped_column(String(255))
    budgetMin: Mapped[Decimal] = mapped_column(Numeric(12, 2))
    budgetMax: Mapped[Decimal] = mapped_column(Numeric(12, 2))
    typeHebergement: Mapped[str] = mapped_column(String(100))
    recommandations: Mapped[list[Recommandation]] = relationship(back_populates="preference", passive_deletes="all", order_by="Recommandation.idRecommendation")
