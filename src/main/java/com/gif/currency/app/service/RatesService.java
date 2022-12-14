package com.gif.currency.app.service;

import com.gif.currency.app.model.RateStatus;

import java.util.Set;

public interface RatesService {

    Set<String> getSupportedCurrencyCodes();

    RateStatus calculateRateStatus(String currencyCode);
}
