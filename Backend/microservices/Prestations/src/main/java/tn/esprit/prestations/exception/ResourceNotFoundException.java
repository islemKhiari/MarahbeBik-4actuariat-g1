package tn.esprit.prestations.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " avec id " + id + " introuvable");
    }
}