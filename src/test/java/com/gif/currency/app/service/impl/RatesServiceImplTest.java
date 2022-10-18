package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.model.ExchangeRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RatesServiceImplTest {

    @Mock
    RatesServiceImpl service;

    @BeforeEach
    void init() {
        FeignRatesClient client = mock(FeignRatesClient.class);
        when(client.getLatestRates(any())).thenReturn(buildLatestExchangeRates());
        service = new RatesServiceImpl(client);
    }

    @Test
    void getCharCodes() {
        assertEquals(loadCollection(), service.getCharCodes());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EUR"})
    void getKey(String currencyCode) {

        ExchangeRates prev = buildHistoricalExchangeRates();
        ExchangeRates current = buildLatestExchangeRates();
        Double prevCoefficient = getCoefficient(prev, currencyCode);
        Double currentCoefficient = getCoefficient(current, currencyCode);

        int x = Double.compare(currentCoefficient, prevCoefficient);

        assertEquals(x, 1);
    }

    private ExchangeRates buildLatestExchangeRates() {
        return new ExchangeRates("USD", loadLatestMap());
    }

    private ExchangeRates buildHistoricalExchangeRates() {
        return new ExchangeRates("USD", loadHistoricalMap());
    }

    private Map<String, Double> loadLatestMap() {
        Map<String, Double> mapTest = new TreeMap<>();
        mapTest.put("EUR", 1.154);
        mapTest.put("USD", 1.000);
        return mapTest;
    }

    private Map<String, Double> loadHistoricalMap() {
        Map<String, Double> mapTest = new TreeMap<>();
        mapTest.put("EUR", 1.022);
        mapTest.put("USD", 1.000);
        return mapTest;
    }

    private List<String> loadCollection() {
        List<String> listTest = new ArrayList<>();
        listTest.add("EUR");
        listTest.add("USD");
        return listTest;
    }

    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Map<String, Double> map = rates.getRates();
        Double targetRate = map.get(charCode);
        Double appBaseRate = 1.00;
        Double defaultBaseRate = map.get(rates.getBase());

        return new BigDecimal((defaultBaseRate / appBaseRate) * targetRate)
                .setScale(4, RoundingMode.UP)
                .doubleValue();
    }
}
