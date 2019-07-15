package org.deeplearning4j.examples.feedforward.regression;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.format.annotation.DateTimeFormat;
import play.api.mvc.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order {

    private String orderId;
    private String datePurchasedString;
    private LocalDateTime datePurchasedDateFormat;
    private String customerId;
    private double orderTotal;
    private List<String> productIds = new ArrayList<String>();

    private LocalDateTime timeStartView;
    private LocalDateTime timeEndView;

    private double timeSpent = 0;

    private int pageViews = 0;

    //Constuctors
    Order()
    {

    }

    Order(String orderId, String datePurchasedString, String customerId, double orderTotal, String productId) {
        this.orderId = orderId;
        this.datePurchasedString = datePurchasedString;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
        productIds.add(productId);
        this.pageViews = 0;

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
    public double getOrderTotal () {
        return orderTotal;
    }
    public List<String> getProductIds () {
        return productIds;
    }
    public int getPageViews () {
        return pageViews;
    }
    public double getTimeSpent () {
        return timeSpent;
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
    public void setOrderTotal (double newOrderTotal){ this.orderTotal = newOrderTotal; }
    public void setProductIds(List<String> getProductIds) {
        this.productIds = productIds;
    }
    public void setPageViews (int newPageViews){ this.pageViews = newPageViews; }
    public void setTimeSpent (double newTimeSpent){ this.timeSpent = timeSpent; }


    public void addProduct(String productId) {
        productIds.add(productId);
    }
    public void addPageView() { pageViews++; }

    public void handleTimeSpent(LocalDateTime timestamp) {

        //case: first timestamp for this order
        if(timeStartView == null && timeEndView == null){   //AN EINAI TO ENA NULL?->den yparxei tetoia periptosh
            timeStartView = timestamp;
            timeEndView = timestamp;
        }
        //case: timestamp belonging to earlier or later session
        else if(timestamp.plusMinutes(30).isBefore(timeStartView) || timestamp.minusMinutes(30).isAfter(timeEndView)){  //if inactive for 30 minutes then session is lost, create new session
            //start new session
            timeSpent = timeSpent + (Duration.between(timeStartView, timeEndView).toMillis()) / 1000;  //calculate in seconds
            timeStartView = timestamp;
            timeEndView = timestamp;
        }
        //case: timestamp before startView, but inside session
        else if(timestamp.isBefore(timeStartView)){
            timeStartView = timestamp;
        }
        //case: timestamp after endView, but inside session
        else if(timestamp.isAfter(timeEndView)){
            timeEndView = timestamp;
        }
    }

    public void calculateTimeSpent() {
        if(timeStartView != null && timeEndView != null){   //AN EINAI TO ENA NULL?->den yparxei tetoia periptosh
            timeSpent = timeSpent + (Duration.between(timeStartView, timeEndView).toMillis()) / 1000; //calculate in seconds
        }
        this.timeSpent = timeSpent;
    }

}
