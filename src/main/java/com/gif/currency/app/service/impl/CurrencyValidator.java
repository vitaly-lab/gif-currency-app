package com.gif.currency.app.service.impl;

import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.service.RatesService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CurrencyValidator {

    private final Set<String> supportedCurrency;

    public CurrencyValidator(RatesService ratesService) {
        this.supportedCurrency = ratesService.getSupportedCurrencyCodes();

    }

    public void validate(String currencyCode) {
        if (currencyCode == null) throw new NotFoundException("Currency code is not supported by application");
        boolean isSupported = supportedCurrency.contains(currencyCode);

        if (!isSupported) {
            throw new NotFoundException("Currency code is not supported by application");
        }
    }
}
