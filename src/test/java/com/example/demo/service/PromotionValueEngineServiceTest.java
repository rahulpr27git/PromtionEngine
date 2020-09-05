package com.example.demo.service;

import com.example.demo.model.UnitRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromotionValueEngineServiceTest {

    @Autowired
    private PromotionValueEngineService service;

    @Test
    public void test1() throws JsonProcessingException {
        service.getTotalValue(Arrays.asList(
                new UnitRequest("A", 8),
                new UnitRequest("B", 3),
                new UnitRequest("C", 1)
        ));
    }
}