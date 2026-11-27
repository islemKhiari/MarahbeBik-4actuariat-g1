from fastapi import APIRouter, Request
from fastapi.responses import JSONResponse
from sqlalchemy import text, select, func
from sqlalchemy.exc import SQLAlchemyError
from app.entity import PreferenceVoyageur, Recommandation

router = APIRouter(tags=["Health"])

@router.get("/actuator/health")
def health(request: Request):
    eureka = request.app.state.eureka
    discovery = "UP" if eureka.registered else ("DOWN" if eureka.settings.eureka_enabled else "DISABLED")
    components = {"eureka": {"status": discovery}}
    try:
        with request.app.state.session_factory() as db:
            db.execute(text("SELECT 1"))
            preferences = db.scalar(select(func.count()).select_from(PreferenceVoyageur))
            recommandations = db.scalar(select(func.count()).select_from(Recommandation))
        components["db"] = {"status": "UP"}
        components["recommandation"] = {"status": "UP", "details": {"nombrePreferences": preferences, "nombreRecommandations": recommandations}}
    except SQLAlchemyError:
        components["db"] = {"status": "DOWN"}
    up = components["db"]["status"] == "UP" and discovery != "DOWN"
    return JSONResponse(status_code=200 if up else 503, content={"status": "UP" if up else "DOWN", "components": components})

@router.get("/actuator/info")
def info(request: Request):
    settings = request.app.state.eureka.settings
    return {"app": {"name": settings.app_name, "description": "MS5 - Recommandation intelligente", "version": "1.0.0", "port": settings.port}}
