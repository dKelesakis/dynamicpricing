package org.deeplearning4j.examples.feedforward.regression;

public class Shop {

    //Shop object variables
    Product[] productList;
    private double deliveryCost;
    private  double deliveryTime;
    private  boolean deliveryMethod;
    private  boolean paymentMethod;
    private  double sellerReviews;
    private  double sellerReputation;
    private double averageProfitDifference;


    //Constructor
    Shop(int numberOfProducts, double deliveryCost, double deliveryTime, boolean deliveryMethod, boolean paymentMethod, double sellerReviews, double sellerReputation, double averageProfitDifference) {
        this.productList = new Product[numberOfProducts];
        this.deliveryCost = deliveryCost;
        this.deliveryTime = deliveryTime;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
        this.sellerReviews = sellerReviews;
        this.sellerReputation = sellerReputation;
        this.averageProfitDifference = averageProfitDifference;
    }


    // Getters
    public Product[] getProductList() { return productList; }
    public double getDeliveryCost() {
        return deliveryCost;
    }
    public double getDeliveryTime() {
        return deliveryTime;
    }
    public boolean getDeliveryMethod() {
        return deliveryMethod;
    }
    public boolean getPaymentMethod() {
        return paymentMethod;
    }
    public double getSellerReviews() { return sellerReviews; }
    public double getSellerReputation() {
        return sellerReputation;
    }
    public double getAverageProfitDifference() {
        return averageProfitDifference;
    }


    // Setters
    public void setProductList(Product[] productList) {
        this.productList = productList;
    }
    public void setDeliveryCost(double newDeliveryCost) {
        this.deliveryCost = newDeliveryCost;
    }
    public void setDeliveryTime(double newDeliveryTime) { this.deliveryTime = newDeliveryTime; }
    public void setDeliveryMethod(boolean newDeliveryMethod) { this.deliveryMethod = newDeliveryMethod; }
    public void setPaymentMethod(boolean newPaymentMethod) { this.paymentMethod = newPaymentMethod; }
    public void setSellerReviews(double newSellerReviews) {
        this.sellerReviews = newSellerReviews;
    }
    public void setSellerReputation(double newSellerReputation) {
        this.sellerReputation = newSellerReputation;
    }
    public void setAverageProfitDifference(double newAverageProfitDifference) {
        this.averageProfitDifference = newAverageProfitDifference;
    }

}
