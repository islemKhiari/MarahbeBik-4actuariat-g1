from fastapi import APIRouter, Depends, Response
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.dto.preference_dto import PreferenceRequest, PreferenceResponse
from app.service.preference_service import PreferenceService

router = APIRouter(prefix="/api/preferences", tags=["Preference"])

@router.post("", response_model=PreferenceResponse, status_code=201)
def create(dto: PreferenceRequest, db: Session = Depends(get_db)):
    return PreferenceService(db).create(dto)

@router.get("", response_model=list[PreferenceResponse])
def find_all(db: Session = Depends(get_db)):
    return PreferenceService(db).find_all()

@router.get("/{identifiant}", response_model=PreferenceResponse)
def find_by_id(identifiant: int, db: Session = Depends(get_db)):
    return PreferenceService(db).find_by_id(identifiant)

@router.put("/{identifiant}", response_model=PreferenceResponse)
def update(identifiant: int, dto: PreferenceRequest, db: Session = Depends(get_db)):
    return PreferenceService(db).update(identifiant, dto)

@router.delete("/{identifiant}", status_code=204, response_class=Response)
def delete(identifiant: int, db: Session = Depends(get_db)):
    PreferenceService(db).delete(identifiant)
    return Response(status_code=204)
