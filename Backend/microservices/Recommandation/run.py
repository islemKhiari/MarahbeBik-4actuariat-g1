from dotenv import load_dotenv
import uvicorn
from app.config.settings import Settings

if __name__ == "__main__":
    load_dotenv()
    settings = Settings.from_env()
    uvicorn.run("app.main:app", host="0.0.0.0", port=settings.port)
