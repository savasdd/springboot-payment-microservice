package com.payment.user.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.security.SignatureException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExceptionHandler {

    private final MessageSource messageSource;

    @org.springframework.web.bind.annotation.ExceptionHandler(GeneralException.class)
    public ResponseEntity<ExceptionResponse> generalException(Exception ex, HttpServletRequest request) {
        log.error("GeneralException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionResponse> runtimeException(Exception ex, HttpServletRequest request) {
        log.error("RuntimeException", ex);

        if (ex instanceof BadCredentialsException) {
            ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, getLangMessage("The username or password is incorrect.", null), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        if (ex instanceof AccountStatusException) {
            ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, getLangMessage("The account is locked.", null), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
        if (ex instanceof AccessDeniedException) {
            ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, getLangMessage("You are not authorized to access this resource.", null), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
        if (ex instanceof SignatureException) {
            ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, getLangMessage("The JWT signature is invalid.", null), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
        if (ex instanceof ExpiredJwtException) {
            ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, getLangMessage("The JWT token has expired.", null), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ExceptionResponse> jpaSystemException(Exception ex, HttpServletRequest request) {
        log.error("JpaSystemException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ExceptionResponse> persistenceException(PersistenceException ex, HttpServletRequest request) {
        log.error("PersistenceException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("DataIntegrityViolationException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> constraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("ConstraintViolationException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, StringUtils.join(getViolationsMsg(ex), ","), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException", ex);
        final List<String> allError = ex.getBindingResult().getAllErrors().stream().map(m -> getLangMessage(m.getDefaultMessage(),
                m.getArguments() != null ? Arrays.stream(m.getArguments()).filter(f -> !(f instanceof DefaultMessageSourceResolvable)).toArray() : null)).toList();
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, StringUtils.join(allError, ","), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {TransactionSystemException.class})
    public ResponseEntity<ExceptionResponse> transactionSystemException(TransactionSystemException ex, HttpServletRequest request) {
        log.error("TransactionSystemException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, null, request.getRequestURI());
        Throwable t = ex.getCause();
        if (t instanceof ConstraintViolationException) {
            error.setMessage(StringUtils.join(getViolationsMsg((ConstraintViolationException) t), ", "));
        } else if (t.getCause() != null) {
            Throwable tt = t.getCause();
            if (tt instanceof ConstraintViolationException) {
                error.setMessage(StringUtils.join(getViolationsMsg((ConstraintViolationException) tt), ", "));
            } else {
                error.setMessage(ex.getMessage());
            }
        } else {
            error.setMessage(ex.getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> MethodArgumentTypeMismatchException(Exception ex, HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionResponse> unknownException(Exception ex, HttpServletRequest request) {
        log.error("Exception", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, getLangMessage(ex.getMessage(), null), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private List<String> getViolationsMsg(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes();

            List<String> keysForFiltering = Arrays.asList("groups", "inclusive", "message", "payload");
            List<Object> objects = attributes.entrySet().stream().filter(e -> !keysForFiltering.contains(e.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
            errorMessages.add(getLangMessage(violation.getMessage(), objects.toArray()));
        }
        return errorMessages;
    }

    private String getLangMessage(String msg, Object[] arguments) {
        String message = StringUtils.substringBetween(msg, "{", "}");
        if (message == null) {
            return msg;
        }
        String anotherMessage = msg.replace("{" + message + "}", "");
        return messageSource.getMessage(message, arguments, LocaleContextHolder.getLocale()) + "\n" + ((!anotherMessage.isEmpty()) ? anotherMessage : "");
    }
}