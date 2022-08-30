package com.gifcurrencyapp.service.serviceinterface;

import com.gifcurrencyapp.model.GifDTO;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface GifService {

    ResponseEntity<GifDTO> getGif(String tag);

    ResponseEntity<byte[]> getGifByUrl(URI url);

    String getUrl(String url);
}
