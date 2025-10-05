package com.example.myapplication.lab3;

public class FuelSale {
    private FuelType fuelType;
    private double quantity; // количество в литрах

    public FuelSale(FuelType fuelType, double quantity) {
        this.fuelType = fuelType;
        this.quantity = quantity;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return fuelType.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f л × %.2f руб/л = %.2f руб.",
                fuelType.getName(), quantity, fuelType.getPrice(), getTotalPrice());
    }
}
