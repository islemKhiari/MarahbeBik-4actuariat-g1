package tn.esprit.ms1_hebergement.exception;
//commentaire
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GestionnaireExceptions {

    public record Erreur(int statut, String message, LocalDateTime date) {}

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<Erreur> introuvable(RessourceIntrouvableException e) {
        return reponse(HttpStatus.NOT_FOUND, e.getMessage());       // 404
    }

    @ExceptionHandler(RequeteInvalideException.class)
    public ResponseEntity<Erreur> invalide(RequeteInvalideException e) {
        return reponse(HttpStatus.BAD_REQUEST, e.getMessage());     // 400
    }

    // Erreurs des annotations @NotBlank, @Min... des DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erreur> validation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return reponse(HttpStatus.BAD_REQUEST, message);            // 400
    }

    // JSON mal formé ou valeur d'énumération inconnue (ex : "type": "TRIPLE")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Erreur> jsonInvalide(HttpMessageNotReadableException e) {
        return reponse(HttpStatus.BAD_REQUEST, "Corps de la requête invalide");
    }

    // Paramètre d'URL invalide (ex : budgetMax=abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Erreur> typeInvalide(MethodArgumentTypeMismatchException e) {
        return reponse(HttpStatus.BAD_REQUEST, "Valeur invalide pour : " + e.getName());
    }

    private ResponseEntity<Erreur> reponse(HttpStatus statut, String message) {
        return ResponseEntity.status(statut)
                .body(new Erreur(statut.value(), message, LocalDateTime.now()));
    }
}