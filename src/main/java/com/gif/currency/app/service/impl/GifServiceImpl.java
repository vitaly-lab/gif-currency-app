package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignGiphyClient;
import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.model.GifDTO;
import com.gif.currency.app.service.GifService;
import com.gif.currency.app.service.RatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class GifServiceImpl implements GifService {
    private final FeignGiphyClient feignGiphyClient;
    private final String apiKey;
    private final RatesService ratesService;

    public GifServiceImpl(FeignGiphyClient feignGiphyClient, @Value("${giphy.api.key}") String apiKey,
                          RatesService ratesService) {
        this.feignGiphyClient = feignGiphyClient;
        this.apiKey = apiKey;
        this.ratesService = ratesService;
    }

    private String getUrl(String url) {
        int indexStart = url.indexOf("webp");
        String substr = url.substring(indexStart);
        String strStart = substr.substring(5);
        int indexEnd = strStart.indexOf(",");
        return strStart.substring(0, indexEnd);
    }

    public byte[] resolveGif(String charCode) {
        GifDTO result;
        try {
            result = feignGiphyClient.getRandomGif(apiKey, ratesService.changeGifTag(charCode)).getBody();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        assert result != null;
        String url = String.valueOf(Objects.requireNonNull(result.getData().get("images")));

        return feignGiphyClient.getGifByUrl(URI.create(getUrl(url))).getBody();
    }
}
