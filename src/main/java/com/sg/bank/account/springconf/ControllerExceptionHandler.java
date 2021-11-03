package com.sg.bank.account.springconf;

import com.sg.bank.account.domain.DepositAmountLessThanTheMinimumException;
import com.sg.bank.account.domain.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {InsufficientBalanceException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleInsufficientBalanceException(InsufficientBalanceException e) {
        Map<String, String> errMessages = new HashMap<>();
        errMessages.put("error", "InsufficientBalanceException");
        errMessages.put("message", e.getMessage());
        return errMessages;
    }

    @ExceptionHandler(value = {DepositAmountLessThanTheMinimumException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleDepositAmountLessThanTheMinimumException(DepositAmountLessThanTheMinimumException e) {
        Map<String, String> errMessages = new HashMap<>();
        errMessages.put("error", "DepositAmountLessThanTheMinimumException");
        errMessages.put("message", e.getMessage());
        return errMessages;
    }
}