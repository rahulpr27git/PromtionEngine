package com.example.demo.model;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class Unit {

    private final String name;
    private final int value;

    public static Set<Unit> getUnits() {
        return Set.of(
                new Unit("A", 50),
                new Unit("B", 30),
                new Unit("C", 20),
                new Unit("D", 15)
        );
    }
}
