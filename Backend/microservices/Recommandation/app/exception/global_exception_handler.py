from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from sqlalchemy.exc import IntegrityError
from app.exception.resource_not_found import ResourceNotFoundException
from app.exception.conflict import ConflictException

def problem(request, status, title, detail, **extra):
    return JSONResponse(status_code=status, media_type="application/problem+json", content={
        "type": "about:blank", "title": title, "status": status, "detail": detail,
        "instance": request.url.path, **extra})

def register_handlers(app: FastAPI):
    @app.exception_handler(ResourceNotFoundException)
    async def not_found(request: Request, exc):
        return problem(request, 404, "Resource not found", str(exc))
    @app.exception_handler(ConflictException)
    async def conflict(request: Request, exc):
        return problem(request, 409, "Conflict", str(exc))
    @app.exception_handler(RequestValidationError)
    async def validation(request: Request, exc):
        errors = {".".join(map(str, e["loc"])): e["msg"] for e in exc.errors()}
        return problem(request, 400, "Validation failed", "One or more fields are invalid", errors=errors)
    @app.exception_handler(IntegrityError)
    async def integrity(request: Request, exc):
        return problem(request, 409, "Conflict", "Contrainte de base de donnees non respectee")
