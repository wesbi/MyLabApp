package com.example.myapplication.lab3;

public enum FuelType {
    AI92("АИ-92", 59.41),
    AI95("АИ-95", 62.20),
    AI98("АИ-98", 84.80),
    DIESEL("Дизель", 71.30),
    GAS("Газ", 27.40);

    private final String name;
    private final double price;

    FuelType(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " - " + price + " руб/л";
    }
}
