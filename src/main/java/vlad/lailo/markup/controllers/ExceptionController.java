package vlad.lailo.markup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vlad.lailo.markup.models.dto.ErrorDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> globalExceptionHandler(RuntimeException ex, WebRequest request) {
        ErrorDto error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(ZoneOffset.UTC),
                ex.getMessage(),
                request.getDescription(false));
        ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
