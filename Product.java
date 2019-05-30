package org.deeplearning4j.examples.feedforward.regression;

public class Product extends Shop {

    //Constuctor
    Product(double baseCost, boolean stockBool, double brandPower, double basePrice, double price)
    {
        super(transportationCost, paymentMethod, deliveryMethod, deliveryTime, sellerReviews, sellerReputation, averageProfitDifference);
        this.baseCost = baseCost;
        this.stockBool = stockBool;
        this.brandPower = brandPower;
        this.basePrice = basePrice;
        this.price = price;
    }

    private double baseCost;
    private double stock; //number of pieces
    private boolean stockBool; //stock available or no stock
    private double productReviews;
    private double brandPower;
    private double basePrice;
    private double price;

    // Getters
    public double getBaseCost() {
        return baseCost;
    }
    public double getStock() {
        return stock;
    }
    public boolean getStockBool() {
        return stockBool;
    }
    public double getProductReviews() {
        return productReviews;
    }
    public double getBrandPower() {
        return brandPower;
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
    public void setStock(double newStock) {
        this.stock = newStock;
    }
    public void setStockBool(boolean newStockBool) {
        this.stockBool = newStockBool;
    }
    public void setProductReviews(double newProductReviews) { this.productReviews = newProductReviews; }
    public void setBrandPower(double newBrandPower) { this.brandPower = newBrandPower; }
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
