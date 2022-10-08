package com.bruno.mypub.common.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = NOT_FOUND)
    public Mono<ExceptionModel> handleEntityNotFoundException(NotFoundException e) {
        return Mono.just(ExceptionModel.builder()
                .errorName("Not Found Exception")
                .errorMessages(List.of(e.getMessage()))
                .status(NOT_FOUND)
                .build());
    }

    @SuppressWarnings("ConstantConditions")
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public Mono<ExceptionModel> handleWebExchangeBindException(WebExchangeBindException e) {
        return Mono.just(ExceptionModel.builder()
                .errorName("Validation Exception")
                .errorMessages(e.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .status(BAD_REQUEST)
                .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    public Mono<ExceptionModel> handleUntreatedException(Exception e) {
        return Mono.just(ExceptionModel.builder()
                .errorName(e.getClass().toString())
                .errorMessages(List.of(e.getMessage()))
                .status(INTERNAL_SERVER_ERROR)
                .build());
    }


}
