package com.bruno.mypub.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionModel {

    private HttpStatus status;
    private String errorName;
    private List<String> errorMessages;
}
