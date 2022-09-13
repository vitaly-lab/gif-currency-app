package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignRatesClient;
import com.gif.currency.app.exception.NotFoundException;
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
public class RatesServiceImp implements RatesService {
    private final FeignRatesClient feignRatesClient;
    @Value("${giphy.rich}")
    private String increase;
    @Value("${giphy.broke}")
    private String decrease;
    @Value("${giphy.zero}")
    private String withoutChanges;
    @Value("${giphy.error}")
    private String error;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    private RatesServiceImp(FeignRatesClient feignRatesClient) {
        this.feignRatesClient = feignRatesClient;
    }

    @Override
    public List<String> getCharCodes() {
        Set<String> currencies = feignRatesClient.getLatestRates(appId).getRates().keySet();
        return new ArrayList<>(currencies);
    }

    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map;

        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(charCode);
            appBaseRate = map.get(base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal((defaultBaseRate / appBaseRate) * targetRate)
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }

    public LocalDate makeDateMinusDay() {
        LocalDate date = LocalDate.now();
        date = date.minusDays(1);
        return date;
    }

    public int getKeyForTag(String charCode) {

        ExchangeRates prevs = feignRatesClient.getHistoricalRates(makeDateMinusDay(), appId);
        ExchangeRates current = feignRatesClient.getLatestRates(appId);
        int error = 11;

        Double prevCoefficient = getCoefficient(prevs, charCode);
        Double currentCoefficient = getCoefficient(current, charCode);

        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : error;
    }

    public String changeGifTag(String charCode) throws NotFoundException {
        int gifKey = getKeyForTag(charCode);

        String gifTag = switch (gifKey) {
            case 1 -> increase;
            case -1 -> decrease;
            case 0 -> withoutChanges;
            case 11 -> error;
            default -> throw new NotFoundException("Unexpected value: " + gifKey);
        };

        return gifTag;
    }
}
