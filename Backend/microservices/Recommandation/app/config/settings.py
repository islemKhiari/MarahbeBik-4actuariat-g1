import os
from dataclasses import dataclass

@dataclass(frozen=True)
class Settings:
    app_name: str = "Recommandation"
    port: int = 8086
    host: str = "127.0.0.1"
    database_url: str = "postgresql+psycopg://postgres:VOTRE_MOT_DE_PASSE@localhost:5432/recommandation_db"
    eureka_enabled: bool = True
    eureka_url: str = "http://localhost:8761/eureka/"

    @classmethod
    def from_env(cls):
        return cls(
            app_name=os.getenv("APP_NAME", "Recommandation"),
            port=int(os.getenv("SERVER_PORT", "8086")),
            host=os.getenv("INSTANCE_HOST", "127.0.0.1"),
            database_url=os.getenv("DATABASE_URL", cls.database_url),
            eureka_enabled=os.getenv("EUREKA_ENABLED", "true").lower() == "true",
            eureka_url=os.getenv("EUREKA_URL", cls.eureka_url),
        )
