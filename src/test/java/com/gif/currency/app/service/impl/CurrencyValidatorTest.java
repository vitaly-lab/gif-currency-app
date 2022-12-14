package com.gif.currency.app.service.impl;

import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.service.RatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyValidatorTest {

    @Mock
    RatesService ratesService;

    CurrencyValidator validator;

    @BeforeEach
    void init() {
        when(ratesService.getSupportedCurrencyCodes()).thenReturn(buildExchangeRates());
        validator = new CurrencyValidator(ratesService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"EUR", "GBP", "JPY"})
    void shouldSuccessfullyValidateSupportedCurrency(String currencyCode) {
        assertDoesNotThrow(() -> validator.validate(currencyCode));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"AAA", "BBB", "GGG", "aadds 2313", "#22 46443FG"})
    void shouldFailure(String currencyCode) {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> validator.validate(currencyCode));
        assertEquals("Currency code is not supported by application", exception.getMessage());
    }

    private Set<String> buildExchangeRates() {
        return Set.of("EUR", "GBP", "JPY");
    }
}
