class ResourceNotFoundException(Exception):
    def __init__(self, resource, identifiant):
        super().__init__(f"{resource} avec id {identifiant} introuvable")
