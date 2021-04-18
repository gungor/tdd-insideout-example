package tddexample.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tddexample.exception.EmployeeNotFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Component
public class ControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = EmployeeNotFoundException.class)
    public void employeeNotFoundHandler(EmployeeNotFoundException ex, HttpServletRequest request) {

    }

}
