package com.example.smartchef.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException e) {
        return construir(HttpStatus.NOT_FOUND, "Recurso no encontrado", e.getMessage());
    }

    @ExceptionHandler(ConflictoException.class)
    public ProblemDetail conflicto(ConflictoException e) {
        return construir(HttpStatus.CONFLICT, "Conflicto con los datos existentes", e.getMessage());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ProblemDetail reglaNegocio(ReglaNegocioException e) {
        return construir(HttpStatus.UNPROCESSABLE_ENTITY, "Regla de negocio incumplida", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        ProblemDetail problema = construir(HttpStatus.BAD_REQUEST, "Datos no válidos", "Hay campos con errores");
        problema.setProperty("errores", errores);
        return problema;
    }

    // Crea la respuesta de error con el mismo formato para todos los casos
    private ProblemDetail construir(HttpStatus estado, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(estado, detalle);
        problema.setTitle(titulo);
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }
}
