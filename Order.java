package org.deeplearning4j.examples.feedforward.regression;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order {

    private String orderId;
    private String datePurchasedString;
    private LocalDateTime datePurchasedDateFormat;
    private String customerId;
    private Double orderTotal;
    private List<String> productIds = new ArrayList<String>();

    //Constuctors
    Order()
    {

    }

    Order(String orderId, String datePurchasedString, String customerId, Double orderTotal, String productId) {
        this.orderId = orderId;
        this.datePurchasedString = datePurchasedString;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
        productIds.add(productId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.datePurchasedDateFormat = LocalDateTime.parse(datePurchasedString, formatter);

    }


    // Getters
    public String getOrderId () {
        return orderId;
    }
    public String getDatePurchasedString () {
        return datePurchasedString;
    }
    public LocalDateTime getDatePurchasedDateFormat () {
        return datePurchasedDateFormat;
    }
    public String getCustomerId () {
        return customerId;
    }
    public Double getOrderTotal () {
        return orderTotal;
    }
    public List<String> getProductIds () {
        return productIds;
    }


    // Setters
    public void setOrderId (String newOrderId){ this.orderId = newOrderId; }
    public void setDatePurchasedString(String newDatePurchasedString){
        this.datePurchasedString = newDatePurchasedString;
    }
    public void setDatePurchasedDateFormat(String newDatePurchasedString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.datePurchasedDateFormat = LocalDateTime.parse(datePurchasedString, formatter);
    }
    public void setCustomerId (String newCustomerId){ this.customerId = newCustomerId; }
    public void setOrderTotal (Double newOrderTotal){ this.orderTotal = newOrderTotal; }
    public void setProductIds(List<String> getProductIds) {
        this.productIds = productIds;
    }

    public void addProduct(String productId) {
        productIds.add(productId);
    }

}
