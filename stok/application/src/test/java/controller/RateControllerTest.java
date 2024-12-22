package controller;

import com.payment.stock.application.StockApplication;
import com.payment.stock.common.utils.JsonUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.dto.StockRateDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(classes = StockApplication.class)
@AutoConfigureMockMvc
public class RateControllerTest {
    private static final String URI = "/api/payment/stocks/rate";

    @Autowired
    private MockMvc mvc;

    @Test
    public void findAll() throws Exception {
        ResultActions response = mvc.perform(get(URI + "/all"));
        response.andExpect(status().isOk());
        log.info("Test api findAll response: {}", getBody(response));
    }

    @Test
    public void findById() throws Exception {
        ResultActions response = mvc.perform(get(URI + "/findOne/{id}", 1L));
        response.andExpect(status().isOk());
        log.info("Test api findById response: {}", getBody(response));
    }

    @Test
    public void save() throws Exception {
        StockRateDto dto = getDto();

        ResultActions response = mvc.perform(post(URI + "/save").contentType(MediaType.APPLICATION_JSON).content(getAsJson(dto)));
        response.andExpect(status().isCreated());
        log.info("Test api save response: {}", getBody(response));
    }

    @Test
    public void update() throws Exception {
        StockRateDto dto = getDto();
        dto.setId(10L);
        dto.setRate(new BigDecimal("0.56"));

        ResultActions response = mvc.perform(put(URI + "/update").contentType(MediaType.APPLICATION_JSON).content(getAsJson(dto)));
        response.andExpect(status().isOk());
        log.info("Test api update response: {}", getBody(response));
    }

    @Test
    public void deletes() throws Exception {
        ResultActions response = mvc.perform(delete(URI + "/delete/{id}",10L));
        response.andExpect(status().isOk());
        log.info("Test api delete response: {}", getBody(response));
    }

    private static StockRateDto getDto() {
        StockRateDto dto = new StockRateDto();
        dto.setRateName("Mock Rate");
        dto.setRate(new BigDecimal("0.55"));
        return dto;
    }

    private static String getAsJson(Object dto) {
        return JsonUtil.toJson(dto);
    }

    private static String getBody(ResultActions response) {
        return new String(response.andReturn().getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

}
