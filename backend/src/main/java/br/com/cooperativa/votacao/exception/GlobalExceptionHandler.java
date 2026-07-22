package br.com.cooperativa.votacao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PautaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePautaNotFound(PautaNotFoundException ex) {
        log.error("Pauta não encontrada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    @ExceptionHandler(SessaoVotacaoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSessaoNotFound(SessaoVotacaoNotFoundException ex) {
        log.error("Sessão não encontrada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    @ExceptionHandler(SessaoVotacaoJaAbertaException.class)
    public ResponseEntity<Map<String, Object>> handleSessaoJaAberta(SessaoVotacaoJaAbertaException ex) {
        log.warn("Tentativa de abrir sessão já existente: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
    
    @ExceptionHandler(SessaoVotacaoFechadaException.class)
    public ResponseEntity<Map<String, Object>> handleSessaoFechada(SessaoVotacaoFechadaException ex) {
        log.warn("Tentativa de votar em sessão fechada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    @ExceptionHandler(VotoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleVotoDuplicado(VotoDuplicadoException ex) {
        log.warn("Tentativa de voto duplicado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
    
    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleCpfInvalido(CpfInvalidoException ex) {
        log.warn("CPF inválido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    @ExceptionHandler(CpfNaoAutorizadoException.class)
    public ResponseEntity<Map<String, Object>> handleCpfNaoAutorizado(CpfNaoAutorizadoException ex) {
        log.warn("CPF não autorizado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Erro de validação: {}", errors);
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de validação");
        response.put("details", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Erro inesperado: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
