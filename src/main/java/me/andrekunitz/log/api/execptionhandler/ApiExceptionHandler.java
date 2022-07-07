package me.andrekunitz.log.api.execptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;
import me.andrekunitz.log.domain.exception.DomainException;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	private final MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Problem.Field> fields = new ArrayList<>();
		List<ObjectError> errors = ex.getBindingResult().getAllErrors();
		errors.forEach(error -> {
			String name = ((FieldError) error).getField();
			String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			fields.add(new Problem.Field(name, message));
		});
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle("One or more fields are invalid. Inform them correctly and try again.");
		problem.setFields(fields);
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle(ex.getMessage());
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
}
