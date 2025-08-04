package com.imdbclone.imdbclone.exceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* Helper that logs once & builds the body */
    private ResponseEntity<ErrorObject> buildResponse(
            HttpStatus status,
            List<String> message,
            HttpServletRequest request,
            Exception ex) {

        log.error("{} {} -> {}", request.getMethod(), request.getRequestURI(), status, ex); // full stack trace
        ErrorObject body = new ErrorObject(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                ex.toString()
        );
        return ResponseEntity.status(status).body(body);
    }

    /* -------- custom domain exceptions ---------------- */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorObject> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                    HttpServletRequest request){
        List<String> msg =List.of("Mandatory field is missing.");
        return buildResponse(HttpStatus.CONFLICT,msg,request,ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                          HttpServletRequest request){

        List<String> msg =List.of("Mandatory \"+ex.getValue().toString().replace(\":\",\"\")+\" field is missing.");
        return buildResponse(HttpStatus.BAD_REQUEST, msg, request, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> resourceNotFound(ResourceNotFoundException ex,
                                                        HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), request, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> badRequest(BadRequestException ex,
                                                  HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), request, ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorObject> unauthorized(UnauthorizedException ex,
                                                    HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, List.of(ex.getMessage()), request, ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorObject> forbidden(ForbiddenException ex,
                                                 HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, List.of(ex.getMessage()), request, ex);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorObject> conflict(ConflictException ex,
                                                HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, List.of(ex.getMessage()), request, ex);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorObject> serviceUnavailable(ServiceUnavailableException ex,
                                                          HttpServletRequest request) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, List.of(ex.getMessage()), request, ex);
    }

    /* -------- validation & builtin exceptions --------- */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> validationErrors(MethodArgumentNotValidException ex,
                                                        HttpServletRequest request) {

        List<String> msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());

        return buildResponse(HttpStatus.BAD_REQUEST, msg, request, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorObject> illegalArgument(IllegalArgumentException ex,
                                                       HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()),request, ex);
    }

    /* -------------- catch‑all fallback ----------------- */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> unhandled(Exception ex,
                                                 HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()), request, ex);
    }
}