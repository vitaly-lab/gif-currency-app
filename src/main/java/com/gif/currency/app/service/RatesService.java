package com.gif.currency.app.service;

import com.gif.currency.app.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface RatesService {

    List<String> getCharCodes();

    LocalDate makeDateMinusDay();

    String changeGifTag(String code) throws NotFoundException;
}
