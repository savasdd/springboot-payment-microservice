package com.payment.stok.common.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

@Slf4j
public class GeneralException extends Exception {

    @Serial
    private static final long serialVersionUID = -2803799245666892878L;

    public GeneralException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }

    public GeneralException() {
        super();
    }
}
