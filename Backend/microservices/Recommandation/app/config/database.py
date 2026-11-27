from fastapi import Request
from sqlalchemy import create_engine, event
from sqlalchemy.orm import DeclarativeBase, sessionmaker
from sqlalchemy.pool import StaticPool

class Base(DeclarativeBase):
    pass

def create_database(url):
    options = {"pool_pre_ping": True}
    if url.startswith("sqlite"):
        options["connect_args"] = {"check_same_thread": False}
        if url in ("sqlite://", "sqlite:///:memory:"):
            options["poolclass"] = StaticPool
    engine = create_engine(url, **options)
    if url.startswith("sqlite"):
        @event.listens_for(engine, "connect")
        def enable_fk(connection, _):
            connection.execute("PRAGMA foreign_keys=ON")
    return engine, sessionmaker(bind=engine, expire_on_commit=False)

def get_db(request: Request):
    with request.app.state.session_factory() as db:
        try:
            yield db
        except Exception:
            db.rollback()
            raise
