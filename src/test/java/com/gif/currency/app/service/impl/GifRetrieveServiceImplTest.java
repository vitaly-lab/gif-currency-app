package com.gif.currency.app.service.impl;

import com.gif.currency.app.client.FeignGiphyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GifRetrieveServiceImplTest {

    private final byte[] expectedArray = new byte[]{1, 10, -20, 5};

    @Mock
    FeignGiphyClient feignGiphyClient;

    @Mock
    CurrencyValidator validator;

    @BeforeEach
    public void init() {
        when(feignGiphyClient.getGifByUrl(any())).thenReturn(new ResponseEntity<>(expectedArray, HttpStatus.OK));
    }

    @Test
    void resolveGifTest() {
        byte[] actual = feignGiphyClient.getGifByUrl(URI.create("http://www.test.lv")).getBody();

        assertEquals(actual, expectedArray);
    }
}
