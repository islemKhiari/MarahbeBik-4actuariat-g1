import asyncio
from contextlib import asynccontextmanager
import httpx
from fastapi import FastAPI
from app.config.settings import Settings
from app.config.database import Base, create_database
from app.config.eureka_client import EurekaClient
from app.config.openapi import configure_openapi
from app.controller import preference_controller, recommandation_controller
from app.exception.global_exception_handler import register_handlers
from app.health.health_controller import router as health_router

def create_app(settings=None):
    settings = settings or Settings.from_env()
    eureka = EurekaClient(settings)
    @asynccontextmanager
    async def lifespan(app):
        engine, factory = create_database(settings.database_url)
        app.state.session_factory = factory
        try:
            Base.metadata.create_all(engine)
            async with httpx.AsyncClient(timeout=5) as client:
                stop = asyncio.Event()
                task = asyncio.create_task(eureka.run(client, stop)) if settings.eureka_enabled else None
                try:
                    yield
                finally:
                    if task is not None:
                        stop.set()
                        await task
                        await eureka.deregister(client)
        finally:
            engine.dispose()
    app = FastAPI(title="Recommandation Microservice API", version="1.0.0",
                  description="CRUD des preferences et recommandations (MS5).", lifespan=lifespan)
    app.state.eureka = eureka
    register_handlers(app)
    configure_openapi(app)
    app.include_router(preference_controller.router)
    app.include_router(recommandation_controller.router)
    app.include_router(health_router)
    return app

app = create_app()
