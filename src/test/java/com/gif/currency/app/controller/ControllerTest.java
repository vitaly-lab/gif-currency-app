package com.gif.currency.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Controller controller;

    @Test
    @DisplayName("currency codes")
    void getCharCodes() throws Exception {
        mockMvc.perform(get("/api/getcodes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getting a gif")
    void getGif() throws Exception {
        mockMvc.perform(get("/api/getgif/code")
                        .param("code", "EUR"))
                .andExpect(status().isOk());
        //  .andExpect(content().contentType(MediaType.IMAGE_GIF)); // todo fix test
    }

    @Test
    @DisplayName("wrong input")
    void getGifError() throws Exception {
        mockMvc.perform(get("/api/getgif/code")
                        .param("code", "BADCODE"))
                .andExpect(status().isOk());
        //  .andExpect(content().contentType(MediaType.IMAGE_GIF)); // todo fix test

    }
}
