package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.model.ExchangeRates;
import com.gif.currency.app.model.RateStatus;
import com.gif.currency.app.service.RatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.gif.currency.app.model.RateStatus.DECREASE;
import static com.gif.currency.app.model.RateStatus.INCREASE;
import static com.gif.currency.app.model.RateStatus.WITHOUT_CHANGES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatesServiceImplTest {
    private static final String BASE_CURRENCY = "USD";
    @Mock
    FeignRatesClient feignClient;

    RatesService service;

    @BeforeEach
    void init() {
        service = new RatesServiceImpl(feignClient, "111", BASE_CURRENCY);
    }

    @Test
    void shouldReturnCorrectSupportedCurrenciesList() {
        Set<String> expected = buildCurrencies();
        ExchangeRates rates = new ExchangeRates(BASE_CURRENCY,
                Map.of("JPY", 1.0, "GBP", 1.0, "EUR", 1.0));
        when(feignClient.getLatestRates(any(), eq(BASE_CURRENCY))).thenReturn(rates);

        assertEquals(expected, service.getSupportedCurrencyCodes());
    }

    @ParameterizedTest
    @MethodSource("rateStatusCalculationProvider")
    void shouldCorrectlyResolveRateStatus(String currency,
                                          ExchangeRates prevExchangeRates,
                                          ExchangeRates currentExchangeRates,
                                          RateStatus expectedRateStatus) {
        when(feignClient.getHistoricalRates(any(), any(), eq(BASE_CURRENCY))).thenReturn(prevExchangeRates);
        when(feignClient.getLatestRates(any(), eq(BASE_CURRENCY))).thenReturn(currentExchangeRates);

        RateStatus actual = service.calculateRateStatus(currency);

        assertEquals(expectedRateStatus, actual);
        verify(feignClient).getHistoricalRates(eq(LocalDate.now().minusDays(1)), eq("111"), eq(BASE_CURRENCY));
        verify(feignClient).getLatestRates(any(), eq(BASE_CURRENCY));
    }

    private static Stream<Arguments> rateStatusCalculationProvider() {
        return buildCurrencies().stream()
                .flatMap(RatesServiceImplTest::buildExchangeRateArgument);
    }

    private static Stream<Arguments> buildExchangeRateArgument(String currency) {
        return Stream.of(
                Arguments.of(currency, buildExchangeRates(currency, 1.200), buildExchangeRates(currency, 1.52), INCREASE),
                Arguments.of(currency, buildExchangeRates(currency, 1.54), buildExchangeRates(currency, 1.41), DECREASE),
                Arguments.of(currency, buildExchangeRates(currency, 1.500), buildExchangeRates(currency, 1.500), WITHOUT_CHANGES)
        );
    }

    private static ExchangeRates buildExchangeRates(String currency, Double value) {
        return new ExchangeRates("USD", Map.of(currency, value));
    }

    private static Set<String> buildCurrencies() {
        return Set.of("JPY", "EUR", "GBP");
    }
}
