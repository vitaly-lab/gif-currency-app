package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.model.ExchangeRates;
import com.gif.currency.app.model.RateStatus;
import com.gif.currency.app.service.RatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.gif.currency.app.model.RateStatus.DECREASE;
import static com.gif.currency.app.model.RateStatus.INCREASE;
import static com.gif.currency.app.model.RateStatus.WITHOUT_CHANGES;

@Service
public class RatesServiceImpl implements RatesService {

    private final FeignRatesClient feignRatesClient;

    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String baseCurrency;

    public RatesServiceImpl(FeignRatesClient feignRatesClient,
                            @Value("${openexchangerates.app.id}") String appId,
                            @Value("${openexchangerates.base}") String baseCurrency) {
        this.feignRatesClient = feignRatesClient;
        this.appId = appId;
        this.baseCurrency = baseCurrency;
    }

    @Override
    public Set<String> getSupportedCurrencyCodes() {
        return new HashSet<>(feignRatesClient.getLatestRates(appId, baseCurrency).getRates().keySet());
    }

    @Override
    public RateStatus calculateRateStatus(String currencyCode) {
        ExchangeRates prev = feignRatesClient.getHistoricalRates(LocalDate.now().minusDays(1), appId, baseCurrency);
        ExchangeRates current = feignRatesClient.getLatestRates(appId, baseCurrency);

        Double prevCoefficient = getCoefficient(prev, currencyCode);
        Double currentCoefficient = getCoefficient(current, currencyCode);

        return resolveRateStatus(prevCoefficient, currentCoefficient);
    }

    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Map<String, Double> map = rates.getRates();
        Double targetRate = map.get(charCode);

        return new BigDecimal(targetRate)
                .setScale(4, RoundingMode.UP)
                .doubleValue();
    }

    private RateStatus resolveRateStatus(Double prevCoefficient, Double currentCoefficient) {
        return switch (Double.compare(prevCoefficient, currentCoefficient)) {
            case 1 -> DECREASE;
            case -1 -> INCREASE;
            default -> WITHOUT_CHANGES;
        };
    }
}
