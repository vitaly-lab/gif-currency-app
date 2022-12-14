package com.gif.currency.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRates {

    private String base;
    private Map<String, Double> rates;
}
