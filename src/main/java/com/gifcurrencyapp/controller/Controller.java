package com.gifcurrencyapp.controller;

import com.gifcurrencyapp.model.GifDTO;
import com.gifcurrencyapp.service.serviceinterface.GifService;
import com.gifcurrencyapp.service.serviceinterface.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RequestMapping("/api")
@RestController
public class Controller {

    private RatesService ratesService;
    private GifService gifService;

    @Autowired
    public Controller(
            RatesService RatesService,
            GifService gifService
    ) {
        this.ratesService = RatesService;
        this.gifService = gifService;
    }

    @GetMapping("/getcodes")
    public List<String> getCharCodes() {
        return ratesService.getCharCodes();
    }

    @GetMapping(value = "/getgif/{code}", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getGif(@PathVariable String code) {
        ResponseEntity<GifDTO> result = null;
        result = gifService.getGif(ratesService.ChangeGifTag(code));
        String url = String.valueOf(Objects.requireNonNull(result.getBody().getData().get("images")));
        byte[] array = gifService.getGifByUrl(URI.create(gifService.getUrl(url))).getBody();

        return array;
    }
}
