package tn.esprit.ms2_reservation.exception;

// Un autre microservice (MS1) ne répond pas
public class ServiceIndisponibleException extends RuntimeException {
    public ServiceIndisponibleException(String message) {
        super(message);
    }
}
