package org.deeplearning4j.examples.feedforward.regression;

public class Product {

    //Product object variables
    private String productId;
    private double baseCost;
    private double brandPower;
    private double stock; //number of pieces
    private boolean stockBool; //stock available or no stock
    private double referencePrice;
    private double price;

    private double relativeBaseCost;
    private double relativePrice;


    //Constuctors
    Product()
    {

    }

    Product(String productId, double baseCost, double brandPower, boolean stockBool, double referencePrice, double price)
    {
        this.productId = productId;
        this.baseCost = baseCost;
        this.brandPower = brandPower;
        this.stockBool = stockBool;
        this.referencePrice = referencePrice;
        this.price = price;
    }


    // Getters
    public String getProductId() {
        return productId;
    }
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
    public double getReferencePrice() {
        return referencePrice;
    }
    public double getPrice() {
        return price;
    }
    public double getRelativeBaseCost() { return relativeBaseCost; }
    public double getRelativePrice() {
        return relativePrice;
    }


    // Setters
    public void setProductId(String productId) {
        this.productId = productId;
    }
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
    public void setReferencePrice(double newBasePrice) { this.referencePrice = newBasePrice; }
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }
    public void setRelativeBaseCost(double newRelativeBaseCost) { this.relativeBaseCost = newRelativeBaseCost; }
    public void setRelativePrice(double newRelativePrice) {
        this.relativePrice = newRelativePrice;
    }


    public void refreshstock() {   //when a product s sold
        stock = this.getStock()-1;
        if(this.stock==0){
            this.setStockBool(false);
        }
    }
}
