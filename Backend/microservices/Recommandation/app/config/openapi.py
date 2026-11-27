from fastapi import FastAPI
from fastapi.openapi.utils import get_openapi

def configure_openapi(app: FastAPI):
    def custom_openapi():
        if app.openapi_schema:
            return app.openapi_schema
        schema = get_openapi(title=app.title, version=app.version, description=app.description, routes=app.routes)
        # La validation est convertie en HTTP 400 par le GlobalExceptionHandler.
        for path in schema.get("paths", {}).values():
            for operation in path.values():
                if isinstance(operation, dict):
                    responses = operation.get("responses", {})
                    if "422" in responses:
                        responses.pop("422")
                        responses["400"] = {"description": "Validation failed", "content": {"application/problem+json": {"schema": {"type": "object"}}}}
        app.openapi_schema = schema
        return schema
    app.openapi = custom_openapi
