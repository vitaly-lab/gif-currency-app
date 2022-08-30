package com.gifcurrencyapp.service.serviceimp;

import com.gifcurrencyapp.client.FeignGiphyClient;
import com.gifcurrencyapp.model.GifDTO;
import com.gifcurrencyapp.service.serviceinterface.GifService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class GifServiceImp implements GifService {
    private FeignGiphyClient feignGiphyClient;
    private String apiKey;

    public GifServiceImp(FeignGiphyClient feignGiphyClient, @Value("${giphy.api.key}") String apiKey) {
        this.feignGiphyClient = feignGiphyClient;
        this.apiKey = apiKey;
    }

    @Override
    public ResponseEntity<GifDTO> getGif(String tag) {
        return feignGiphyClient.getRandomGif(apiKey, tag);
    }

    @Override
    public ResponseEntity<byte[]> getGifByUrl(URI uri) {
        return feignGiphyClient.getGifByUrl(uri);
    }

    @Override
    public String getUrl(String url) {
        int indexStart = url.indexOf("webp");
        String substr = url.substring(indexStart);
        String strStart = substr.substring(5);
        int indexEnd = strStart.indexOf(",");
        String strEnd = strStart.substring(0, indexEnd);
        return strEnd;
    }
}
