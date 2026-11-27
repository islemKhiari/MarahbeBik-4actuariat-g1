from sqlalchemy import select
from sqlalchemy.orm import Session
from app.entity import Recommandation

class RecommandationRepository:
    def __init__(self, db: Session): self.db = db
    def find_all(self):
        return self.db.scalars(select(Recommandation).order_by(Recommandation.idRecommendation)).all()
    def find_by_id(self, identifiant):
        return self.db.get(Recommandation, identifiant)
    def exists_by_preference_id(self, identifiant):
        return self.db.scalar(select(Recommandation.idRecommendation).where(Recommandation.idPreference == identifiant).limit(1)) is not None
    def save(self, entity):
        self.db.add(entity)
        self.db.flush()
        return entity
    def delete(self, entity):
        self.db.delete(entity)
        self.db.flush()
