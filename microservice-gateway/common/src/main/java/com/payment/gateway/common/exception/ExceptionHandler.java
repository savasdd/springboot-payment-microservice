package com.payment.gateway.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExceptionHandler {

    private final MessageSource messageSource;

    @org.springframework.web.bind.annotation.ExceptionHandler(GeneralException.class)
    public ResponseEntity<ExceptionResponse> generalException(Exception ex) {
        log.error("GeneralException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, getLangMessage(ex.getMessage(), null), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionResponse> runtimeException(Exception ex ) {
        log.error("RuntimeException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, getLangMessage(ex.getMessage(), null), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> constraintViolationException(ConstraintViolationException ex ) {
        log.error("ConstraintViolationException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, StringUtils.join(getViolationsMsg(ex), ","), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex ) {
        log.error("MethodArgumentNotValidException", ex);
        final List<String> allError = ex.getBindingResult().getAllErrors().stream().map(m -> getLangMessage(m.getDefaultMessage(),
                m.getArguments() != null ? Arrays.stream(m.getArguments()).filter(f -> !(f instanceof DefaultMessageSourceResolvable)).toArray() : null)).toList();
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, StringUtils.join(allError, ","),null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> MethodArgumentTypeMismatchException(Exception ex ) {
        log.error("MethodArgumentTypeMismatchException", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, getLangMessage(ex.getMessage(), null), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionResponse> unknownException(Exception ex ) {
        log.error("Exception", ex);
        ExceptionResponse error = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, getLangMessage(ex.getMessage(), null), null);
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