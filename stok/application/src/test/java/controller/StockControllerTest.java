package controller;

import com.payment.stock.application.StockApplication;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.common.utils.DateUtil;
import com.payment.stock.common.utils.JsonUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.vo.CategoryV0;
import com.payment.stock.entity.vo.StockRateV0;
import com.payment.stock.entity.vo.StockV0;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import util.TestUtil;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(classes = StockApplication.class)
@AutoConfigureMockMvc
public class StockControllerTest {
    private static final String URI = "/api/payment/stocks";

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
        ResultActions response = mvc.perform(get(URI + "/findOne/{id}", 114L));
        response.andExpect(status().isOk());
        log.info("Test api findById response: {}", getBody(response));
    }

    @Test
    public void findPageable() throws Exception {
        ResultActions response = mvc.perform(get(URI + "/pageable?page=0?size=10&sort=creDate&sort=desc"));
        response.andExpect(status().isOk());
        log.info("Test api pageable response: {}", getBody(response));
    }

    @Test
    public void findAllLoad() throws Exception {
        ResultActions response = mvc.perform(post(URI + "/pageable-load").contentType(MediaType.APPLICATION_JSON).content(getAsJson(TestUtil.getDataLoad())));
        response.andExpect(status().isOk());
        log.info("Test api load response: {}", getBody(response));
    }

    @Test
    public void save() throws Exception {
        StockV0 dto = getDto();

        ResultActions response = mvc.perform(post(URI + "/save").contentType(MediaType.APPLICATION_JSON).content(getAsJson(dto)));
        response.andExpect(status().isCreated());
        log.info("Test api save response: {}", getBody(response));
    }

    @Test
    public void update() throws Exception {
        StockV0 dto = getDto();
        dto.setId(115L);
        dto.setStockName("Mock Stock 2");
        dto.setPrice(new BigDecimal("120.556"));

        ResultActions response = mvc.perform(put(URI + "/update").contentType(MediaType.APPLICATION_JSON).content(getAsJson(dto)));
        response.andExpect(status().isOk());
        log.info("Test api update response: {}", getBody(response));
    }

    @Test
    public void deletes() throws Exception {
        ResultActions response = mvc.perform(delete(URI + "/delete/{id}", 115L));
        response.andExpect(status().isOk());
        log.info("Test api delete response: {}", getBody(response));
    }

    private static StockV0 getDto() {
        StockV0 dto = new StockV0();
        dto.setStockName("Mock Stock");
        dto.setYear(DateUtil.getYear(new Date()));
        dto.setUnitType(UnitType.Litre);
        dto.setPrice(new BigDecimal("120.549"));
        dto.setAvailableQuantity(90);
        dto.setDetails(List.of());
        dto.setUserId(77L);
        dto.setCategory(new CategoryV0(2L));
        dto.setRate(new StockRateV0(2L));
        return dto;
    }

    private static String getAsJson(Object dto) {
        return JsonUtil.toJson(dto);
    }

    private static String getBody(ResultActions response) {
        return new String(response.andReturn().getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

}
