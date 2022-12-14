package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignGiphyClient;
import com.gif.currency.app.model.GifDTO;
import com.gif.currency.app.model.RateStatus;
import com.gif.currency.app.service.GifRetrieveService;
import com.gif.currency.app.service.RatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class GifRetrieveServiceImpl implements GifRetrieveService {
    private final FeignGiphyClient feignGiphyClient;
    private final RatesService ratesService;
    private final CurrencyValidator validator;
    private final GifTagResolver gifTagResolver;
    private final String apiKey;

    public GifRetrieveServiceImpl(FeignGiphyClient feignGiphyClient,
                                  @Value("${giphy.api.key}") String apiKey,
                                  RatesService ratesService,
                                  CurrencyValidator validator,
                                  GifTagResolver gifTagResolver) {
        this.feignGiphyClient = feignGiphyClient;
        this.apiKey = apiKey;
        this.ratesService = ratesService;
        this.validator = validator;
        this.gifTagResolver = gifTagResolver;
    }

    public byte[] resolveGif(String currencyCode) {
        validator.validate(currencyCode);

        GifDTO result = getRandomGifReal(currencyCode);
        String url = String.valueOf(Objects.requireNonNull(result.getData().get("images")));

        return feignGiphyClient.getGifByUrl(URI.create(getUrl(url))).getBody();
    }

    private GifDTO getRandomGifReal(String currencyCode) {
        RateStatus rateStatus = ratesService.calculateRateStatus(currencyCode);
        String tag = gifTagResolver.getTag(rateStatus);

        return feignGiphyClient.getRandomGif(apiKey, tag).getBody();
    }

    private String getUrl(String url) {
        int indexStart = url.indexOf("webp");
        String substr = url.substring(indexStart);
        String strStart = substr.substring(5);
        int indexEnd = strStart.indexOf(",");

        return strStart.substring(0, indexEnd);
    }
}
