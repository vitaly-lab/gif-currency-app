package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignGiphyClient;
import com.gif.currency.app.controller.CurrencyValidator;
import com.gif.currency.app.exception.NotFoundException;
import com.gif.currency.app.model.GifDTO;
import com.gif.currency.app.service.GifSRetrieveService;
import com.gif.currency.app.service.RatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class GifSRetrieveSRetrieveServiceImpl implements GifSRetrieveService {
    private final FeignGiphyClient feignGiphyClient;
    private final String apiKey;
    private final RatesService ratesService;
    private final CurrencyValidator validator;
    @Value("${giphy.rich}")
    private String increase;
    @Value("${giphy.broke}")
    private String decrease;
    @Value("${giphy.zero}")
    private String withoutChanges;

    public GifSRetrieveSRetrieveServiceImpl(FeignGiphyClient feignGiphyClient,
                                            @Value("${giphy.api.key}") String apiKey,
                                            RatesService ratesService, CurrencyValidator validator) {
        this.feignGiphyClient = feignGiphyClient;
        this.apiKey = apiKey;
        this.ratesService = ratesService;
        this.validator = validator;
    }

    public String changeGifTag(String currencyCode) {
        int gifKey = ratesService.getKey(currencyCode);

        return switch (gifKey) {
            case 1 -> increase;
            case -1 -> decrease;
            case 0 -> withoutChanges;
            default -> throw new NotFoundException("Unexpected value: " + gifKey);
        };
    }

    public byte[] resolveGif(String currencyCode) {
        String code = validator.validate(currencyCode);
        GifDTO result = getRandomGif(code);
        String url = String.valueOf(Objects.requireNonNull(result.getData().get("images")));

        return feignGiphyClient.getGifByUrl(URI.create(getUrl(url))).getBody();
    }

    private GifDTO getRandomGif(String currencyCode) {

        return feignGiphyClient.getRandomGif(apiKey, changeGifTag(currencyCode)).getBody();
    }

    private String getUrl(String url) {
        int indexStart = url.indexOf("webp");
        String substr = url.substring(indexStart);
        String strStart = substr.substring(5);
        int indexEnd = strStart.indexOf(",");

        return strStart.substring(0, indexEnd);
    }
}
