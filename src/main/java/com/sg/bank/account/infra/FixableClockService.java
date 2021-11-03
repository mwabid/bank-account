package com.sg.bank.account.infra;

import com.sg.bank.account.application.ClockService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FixableClockService implements ClockService {

    private LocalDate fixedLocalDate;

    @Override
    public LocalDate dateNow() {
        if(this.fixedLocalDate != null){
            return this.fixedLocalDate;
        }
        return LocalDate.now();
    }

    public void setFixedLocalDate(LocalDate fixedLocalDate) {
        this.fixedLocalDate = fixedLocalDate;
    }

    public void resetFixedLocalDate() {
        this.fixedLocalDate = null;
    }

}
