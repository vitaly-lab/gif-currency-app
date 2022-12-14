package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignGiphyClient;
import com.gif.currency.app.model.GifDTO;
import com.gif.currency.app.service.GifRetrieveService;
import com.gif.currency.app.service.RatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GifRetrieveServiceImplTest {

    private final byte[] expectedArray = new byte[]{1, 10, -20, 5};

    @Mock
    FeignGiphyClient feignGiphyClient;
    @Mock
    RatesService ratesService;
    @Mock
    CurrencyValidator validator;
    @Mock
    GifTagResolver gifTagResolver;
    @Mock
    GifRetrieveService service;

    @BeforeEach
    public void init() {
        service = new GifRetrieveServiceImpl(feignGiphyClient, "apiKey", ratesService, validator, gifTagResolver);
        Object ob = "testwebptesttesttestte,st";
        Map<String, Object> map = Map.of("images", ob);
        GifDTO dto = new GifDTO();
        dto.setData(map);
        ResponseEntity<GifDTO> responseEntityGetRandomGif = ResponseEntity.of(Optional.of(dto));
        ResponseEntity<byte[]> responseEntityGetGifByUrl = ResponseEntity.of(Optional.of(expectedArray));

        when(feignGiphyClient.getRandomGif(any(), any())).thenReturn(responseEntityGetRandomGif);
        when(feignGiphyClient.getGifByUrl(any())).thenReturn(responseEntityGetGifByUrl);
    }

    @Test
    void resolveGifTest() {
        byte[] actual = service.resolveGif("EUR");

        assertEquals(actual, expectedArray);
        verify(validator).validate("EUR");
        verify(feignGiphyClient).getRandomGif(any(), any());
        verify(feignGiphyClient).getGifByUrl(any());
    }
}
