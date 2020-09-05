package com.example.demo.dao;

import lombok.Getter;

import java.util.Set;

@Getter
public final class Unit {

    private final String name;
    private final int value;

    private final static Set<Unit> UNITS = Set.of(
            new Unit("A", 50),
            new Unit("B", 30),
            new Unit("C", 20),
            new Unit("D", 15)
    );

    public Unit(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static Set<Unit> getUNITS() {
        return UNITS;
    }
}
