
from fastapi import APIRouter, Depends, Response, Request
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.dto.recommandation_dto import RecommandationRequest, RecommandationResponse
from app.service.recommandation_service import RecommandationService

router = APIRouter(prefix="/api/recommandations", tags=["Recommandation"])


@router.get("/port")
def get_port(request: Request):
    return f"Reponse depuis Recommandation - Port : {request.scope['server'][1]}"


@router.post("", response_model=RecommandationResponse, status_code=201)
def create(dto: RecommandationRequest, db: Session = Depends(get_db)):
    return RecommandationService(db).create(dto)


@router.get("", response_model=list[RecommandationResponse])
def find_all(db: Session = Depends(get_db)):
    return RecommandationService(db).find_all()


@router.get("/{identifiant}", response_model=RecommandationResponse)
def find_by_id(identifiant: int, db: Session = Depends(get_db)):
    return RecommandationService(db).find_by_id(identifiant)


@router.put("/{identifiant}", response_model=RecommandationResponse)
def update(identifiant: int, dto: RecommandationRequest, db: Session = Depends(get_db)):
    return RecommandationService(db).update(identifiant, dto)


@router.delete("/{identifiant}", status_code=204, response_class=Response)
def delete(identifiant: int, db: Session = Depends(get_db)):
    RecommandationService(db).delete(identifiant)
    return Response(status_code=204)
