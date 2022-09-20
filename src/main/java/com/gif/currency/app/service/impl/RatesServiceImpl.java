package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.model.ExchangeRates;
import com.gif.currency.app.service.RatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RatesServiceImpl implements RatesService {
    private final FeignRatesClient feignRatesClient;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    private RatesServiceImpl(FeignRatesClient feignRatesClient) {
        this.feignRatesClient = feignRatesClient;
    }

    @Override
    public List<String> getCharCodes() {
        Set<String> currencies = feignRatesClient.getLatestRates(appId).getRates().keySet();

        return new ArrayList<>(currencies);
    }

    public int getKey(String currencyCode) {

        ExchangeRates prev = feignRatesClient.getHistoricalRates(LocalDate.now().minusDays(1), appId);
        ExchangeRates current = feignRatesClient.getLatestRates(appId);

        Double prevCoefficient = getCoefficient(prev, currencyCode);
        Double currentCoefficient = getCoefficient(current, currencyCode);

        return Double.compare(currentCoefficient, prevCoefficient);
    }

    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Map<String, Double> map = rates.getRates();
        Double targetRate = map.get(charCode);
        Double appBaseRate = map.get(base);
        Double defaultBaseRate = map.get(rates.getBase());

        return new BigDecimal((defaultBaseRate / appBaseRate) * targetRate)
                .setScale(4, RoundingMode.UP)
                .doubleValue();
    }
}
