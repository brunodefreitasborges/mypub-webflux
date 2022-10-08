package com.bruno.mypub.common.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends RuntimeException{
    private final String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

}

