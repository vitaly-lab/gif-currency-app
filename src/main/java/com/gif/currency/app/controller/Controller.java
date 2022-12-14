package com.gif.currency.app.controller;

import com.gif.currency.app.service.GifRetrieveService;
import com.gif.currency.app.service.RatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequestMapping("/v1/api")
@RestController
@AllArgsConstructor
public class Controller {
    private final RatesService ratesService;
    private final GifRetrieveService gifSRetrieveService;

    @GetMapping("/currency/codes")
    public Set<String> getCurrencyCodes() {
        return ratesService.getSupportedCurrencyCodes();
    }

    @GetMapping(value = "/gif/{charCode}", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getGif(@PathVariable String charCode) {
        return gifSRetrieveService.resolveGif(charCode);
    }
}
