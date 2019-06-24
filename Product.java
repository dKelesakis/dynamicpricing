package org.deeplearning4j.examples.feedforward.regression;

public class Product {

    //Product object variables
    private double baseCost;
    private double brandPower;
    private double stock; //number of pieces
    private boolean stockBool; //stock available or no stock
    private double basePrice;
    private double price;


    //Constuctors
    Product()
    {

    }

    Product(double baseCost, double brandPower, boolean stockBool, double basePrice, double price)
    {
        this.baseCost = baseCost;
        this.brandPower = brandPower;
        this.stockBool = stockBool;
        this.basePrice = basePrice;
        this.price = price;
    }


    // Getters
    public double getBaseCost() {
        return baseCost;
    }
    public double getBrandPower() {
        return brandPower;
    }
    public double getStock() {
        return stock;
    }
    public boolean getStockBool() {
        return stockBool;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public double getPrice() {
        return price;
    }

    // Setters
    public void setBaseCost(double newBaseCost) {
        this.baseCost = newBaseCost;
    }
    public void setBrandPower(double newBrandPower) { this.brandPower = newBrandPower; }
    public void setStock(double newStock) {
        this.stock = newStock;
    }
    public void setStockBool(boolean newStockBool) {
        this.stockBool = newStockBool;
    }
    public void setBasePrice(double newBasePrice) { this.basePrice = newBasePrice; }
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }


    public void refreshstock() {   //when a product s sold
        stock = this.getStock()-1;
        if(this.stock==0){
            this.setStockBool(false);
        }
    }
}
