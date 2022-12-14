package com.gif.currency.app.client;

import com.gif.currency.app.model.ExchangeRates;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@org.springframework.cloud.openfeign.FeignClient(name = "OERClient", url = "${openexchangerates.url.general}")
public interface FeignRatesClient {

    @GetMapping("/latest.json")
    ExchangeRates getLatestRates(@RequestParam("app_id") String appId, @RequestParam("base") String base);

    @GetMapping("/historical/{date}.json")
    ExchangeRates getHistoricalRates(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     @RequestParam("app_id") String appId,
                                     @RequestParam("base") String base);
}
