package com.example.clienteStorage.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.clienteStorage.exception.CannotDeleteException;
import com.example.clienteStorage.exception.CannotDisableException;
import com.example.clienteStorage.exception.EntityNotFoundException;
import com.example.clienteStorage.exception.NegativeSaldoException;
import com.example.clienteStorage.exception.RequiredCorrentistaException;
import com.example.clienteStorage.exception.RequiredNameException;
import com.example.clienteStorage.exception.RequiredPhoneException;
import com.example.clienteStorage.exception.RequiredSaldoException;
import com.example.clienteStorage.exception.TelefoneUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
	
	 private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ErrorMessage> MethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result){
//		log.error("Api Error - ", ex);
//	
//        
//		return ResponseEntity
//				.status(HttpStatus.UNPROCESSABLE_ENTITY)
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", result));
//	}
	 
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
	            MethodArgumentNotValidException ex, 
	            HttpServletRequest request) {

	        log.error("Erro de validação", ex);

	        // Pega apenas a primeira mensagem de erro
	        String mensagemErro = "Campo(s) inválido(s)";
	        if (ex.getBindingResult().hasErrors()) {
	            mensagemErro = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
	        }

	        return ResponseEntity
	                .status(HttpStatus.UNPROCESSABLE_ENTITY)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, mensagemErro));
	    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, HttpServletRequest request) {

        logger.error("Erro genérico capturado", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + ex.getMessage()));
    }
	
	@ExceptionHandler(RequiredSaldoException.class)
	public ResponseEntity<ErrorMessage> RequiredSaldoException(RuntimeException ex, HttpServletRequest request){
		log.error("Erro na API - ", ex);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
	}

	
	@ExceptionHandler(TelefoneUniqueViolationException.class)
	public ResponseEntity<ErrorMessage> TelefoneUniqueViolationException(RuntimeException ex, HttpServletRequest request){
		log.error("Erro na API - ", ex);
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
	}
	
	
	//Id buscado não existe
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> EntityNotFoundException(RuntimeException ex, HttpServletRequest request){
		log.error("Erro na API - ", ex);
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
	}
	
	//A conta corrente está ativa e não pode ser excluída.
	@ExceptionHandler(CannotDeleteException.class)
    public ResponseEntity<ErrorMessage> handleClienteNaoRemovivelException(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro na API - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
	
	//É necessário estar com saldo zerado para desativar a conta corrente.
	@ExceptionHandler(CannotDisableException.class)
    public ResponseEntity<ErrorMessage> handleCannotDisableException(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro na API - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
	
	//É necessário estar com saldo zerado para desativar a conta corrente.
		@ExceptionHandler(RequiredNameException.class)
	    public ResponseEntity<ErrorMessage> handleRequiredNameException(RuntimeException ex, HttpServletRequest request) {
	        log.error("Erro na API - ", ex);
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
	    }
		
		@ExceptionHandler(RequiredPhoneException.class)
	    public ResponseEntity<ErrorMessage> RequiredPhoneException(RuntimeException ex, HttpServletRequest request) {
	        log.error("Erro na API - ", ex);
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
	    }
		
		@ExceptionHandler(RequiredCorrentistaException.class)
	    public ResponseEntity<ErrorMessage> RequiredCorrentistaException(RuntimeException ex, HttpServletRequest request) {
	        log.error("Erro na API - ", ex);
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
	    }
		
	//É necessário estar com saldo zerado para desativar a conta corrente.
		@ExceptionHandler(NegativeSaldoException.class)
	    public ResponseEntity<ErrorMessage> handleNegativeSaldoException(RuntimeException ex, HttpServletRequest request) {
	        log.error("Erro na API - ", ex);
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
		    }
}
