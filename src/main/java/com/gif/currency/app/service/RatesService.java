package com.gif.currency.app.service;

import java.util.List;

public interface RatesService {

    List<String> getCharCodes();

    int getKey(String currencyCode);
}
