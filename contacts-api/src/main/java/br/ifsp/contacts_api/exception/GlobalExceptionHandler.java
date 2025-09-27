package br.ifsp.contacts_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para tratamento de exceções da aplicação.
 * Centraliza o tratamento de erros e formata respostas consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Trata erros de validação de campos.
     * Retorna HTTP 400 com mensagens de erro detalhadas.
     * 
     * @param ex Exceção de validação
     * @return Resposta com erro formatado
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.put("erro", errorMessage);
        });
        
        return ResponseEntity.badRequest().body(errors);
    }
    
    /**
     * Trata exceções gerais não capturadas.
     * 
     * @param ex Exceção genérica
     * @return Resposta com erro interno do servidor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno do servidor: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
