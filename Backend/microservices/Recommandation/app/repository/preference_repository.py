from sqlalchemy import select
from sqlalchemy.orm import Session, selectinload
from app.entity import PreferenceVoyageur

class PreferenceRepository:
    def __init__(self, db: Session): self.db = db
    def find_all(self):
        return self.db.scalars(select(PreferenceVoyageur).options(selectinload(PreferenceVoyageur.recommandations)).order_by(PreferenceVoyageur.idPreference)).all()
    def find_by_id(self, identifiant):
        return self.db.get(PreferenceVoyageur, identifiant)
    def save(self, entity):
        self.db.add(entity)
        self.db.flush()
        return entity
    def delete(self, entity):
        self.db.delete(entity)
        self.db.flush()
