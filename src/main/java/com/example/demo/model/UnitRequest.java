package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnitRequest {

    private final String unitName;
    private final int quantity;
}
