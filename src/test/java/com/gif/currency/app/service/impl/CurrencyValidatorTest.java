package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.model.ExchangeRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyValidatorTest {

    @Mock
    FeignRatesClient client;

    CurrencyValidator validator;

    @BeforeEach
    void init() {
        when(client.getLatestRates(any())).thenReturn(buildExchangeRates());
        validator = new CurrencyValidator(client, "app_id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"EUR", "GBP", "JPY"})
    void shouldSuccessfullyValidateSupportedCurrency(String currencyCode) {
        assertDoesNotThrow(() -> validator.validate(currencyCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"AAA", "BBB", "GGG"})
    void shouldFailure(String currencyCode) {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> validator.validate(currencyCode));
        assertEquals("Currency code is not supported by application", exception.getMessage());
    }

    private Map<String, Double> loadMap() {
        Map<String, Double> mapTest = new HashMap<>();
        mapTest.put("EUR", 1.022);
        mapTest.put("GBP", 0.903);
        mapTest.put("JPY", 144.408);

        return mapTest;
    }

    private ExchangeRates buildExchangeRates() {

        return new ExchangeRates("USD", loadMap());
    }
}
