package com.payment.stock.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Date;

@Getter
@Setter
public class ExceptionResponse {
    private static MessageSource messageSource;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private int status;
    private Object error;
    private Object message;
    private String path;

    public ExceptionResponse(String infoMessage) {
        this.message = getLangMessage(infoMessage);
    }

    public static void SetMessageSource(MessageSource ms) {
        messageSource = ms;
    }

    public ExceptionResponse() {
    }

    public ExceptionResponse(Date time, int status, Object error, Object message, String path) {
        this.time = time;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    private String getLangMessage(String msg) {
        String message = StringUtils.substringBetween(msg, "{", "}");
        if (message == null) {
            return msg;
        }
        String anotherMessage = msg.replace("{" + message + "}", "");
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale()) + "\n" + ((!anotherMessage.isEmpty()) ? anotherMessage : "");
    }
}