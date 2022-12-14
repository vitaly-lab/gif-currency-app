package com.gif.currency.app.controller;

import com.gif.currency.app.service.GifRetrieveService;
import com.gif.currency.app.service.RatesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = Controller.class)
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    RatesService ratesService;
    @MockBean
    GifRetrieveService gifRetrieveService;

    @Test
    void getCurrencyCodes() throws Exception {
        Set<String> codes = Set.of("USD", "EUR", "JPY", "KRW");
        when(ratesService.getSupportedCurrencyCodes()).thenReturn(codes);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/api/currency/codes")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"JPY\",\"USD\",\"KRW\",\"EUR\"]"));
        verify(ratesService).getSupportedCurrencyCodes();
    }

    @Test
    public void etGif() throws Exception {
        byte[] gif = "Test".getBytes();
        when(gifRetrieveService.resolveGif("GBP")).thenReturn(gif);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/api/gif/GBP", "GBP"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(gif));
        verify(gifRetrieveService).resolveGif("GBP");
    }
}
