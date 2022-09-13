package com.gif.currency.app.client;

import com.gif.currency.app.model.GifDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@org.springframework.cloud.openfeign.FeignClient(name = "giphyClient", url = "${giphy.url.general}")
public interface FeignGiphyClient {

    @GetMapping("/random")
    ResponseEntity<GifDTO> getRandomGif(@RequestParam("api_key") String apiKey,
                                        @RequestParam("tag") String tag);

    @GetMapping
    ResponseEntity<byte[]> getGifByUrl(URI uri);
}
