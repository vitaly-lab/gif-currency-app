package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyValidator {
    private final FeignRatesClient feignRatesClient;
    private final List<String> supportedCurrency;

    @Value("${openexchangerates.app.id}")
    private String appId;

    public CurrencyValidator(FeignRatesClient feignRatesClient, @Value("${openexchangerates.app.id}") String appId) {
        this.feignRatesClient = feignRatesClient;
        this.appId = appId;
        supportedCurrency = List.copyOf(feignRatesClient.getLatestRates(appId).getRates().keySet());
    }

    public void validate(String currencyCode) {

        boolean isSupported = supportedCurrency.contains(currencyCode);

        if (!isSupported) {
            throw new NotFoundException("Currency code is not supported by application");
        }
    }
}
