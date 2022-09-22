package com.gif.currency.app.service;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CurrencyValidator {
    private final FeignRatesClient feignRatesClient;
    @Value("${openexchangerates.app.id}")
    private String appId;

    public CurrencyValidator(FeignRatesClient feignRatesClient) {
        this.feignRatesClient = feignRatesClient;
    }

    public String validate(String currencyCode) {

        Set<String> currencies = feignRatesClient.getLatestRates(appId).getRates().keySet();
        List<String> arr = new ArrayList<>(currencies);
        boolean contains = arr.contains(currencyCode);

        if (!contains) {
            throw new NotFoundException("input error");
        } else return currencyCode;
    }
}
