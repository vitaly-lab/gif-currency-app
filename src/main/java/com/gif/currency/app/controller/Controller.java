package com.gif.currency.app.controller;

import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.exception.ValidationException;
import com.gif.currency.app.service.GifService;
import com.gif.currency.app.service.RatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/api")
@RestController
@AllArgsConstructor
public class Controller {
    private final RatesService ratesService;
    private final GifService gifService;

    @GetMapping("/currency/codes")
    public List<String> getCurrencyCodes() throws ValidationException {
        return ratesService.getCharCodes();
    }

    @ExceptionHandler(NotFoundException.class)
    @GetMapping(value = "/gif/{charCode}", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getGif(@PathVariable String charCode) throws ValidationException {
        return gifService.resolveGif(charCode);
    }
}
