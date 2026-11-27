from __future__ import annotations
from datetime import datetime
from sqlalchemy import BigInteger, Integer, Float, DateTime, String, ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.config.database import Base

class Recommandation(Base):
    __tablename__ = "recommandations"
    idRecommendation: Mapped[int] = mapped_column(BigInteger().with_variant(Integer, "sqlite"), primary_key=True, autoincrement=True)
    voyageurId: Mapped[str] = mapped_column(String(255), index=True)
    hebergementId: Mapped[int] = mapped_column(BigInteger, index=True)
    scoreGlobal: Mapped[float] = mapped_column(Float(53))
    dateGeneration: Mapped[datetime] = mapped_column(DateTime)
    idPreference: Mapped[int] = mapped_column(ForeignKey("preferences_voyageurs.idPreference", ondelete="RESTRICT"), index=True)
    preference: Mapped[PreferenceVoyageur] = relationship(back_populates="recommandations")
