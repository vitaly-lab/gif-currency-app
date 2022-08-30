package com.gifcurrencyapp.service.serviceinterface;

import java.util.List;

public interface RatesService {

    List<String> getCharCodes();

    int getKeyForTag(String charCode);

    void refreshRates();

    String ChangeGifTag(String code);
}
