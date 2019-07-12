package org.deeplearning4j.examples.feedforward.regression;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.concurrent.TimeUnit ;
import java.util.*;

public class Customer {

    //variables for 1st NN
    private double wtpArray[]; // private = restricted access




    //Constructors ton 1o constructor xrhsimopoiei to programma
    Customer(int numberOfProducts) {

        double[] wtpArray = new double[numberOfProducts];
    }

    Customer(double[] wtpArray) {

        this.wtpArray = wtpArray;
    }

    Customer(){

    }

    Customer(String customerId){
         this.customerId = customerId;
    }

    // Getter
    public double[] getWtp() { return wtpArray; }
    // Setter
    public void setWtp(double[] newWtp) {
        this.wtpArray = newWtp;
    }


    //variables for 2nd NN
    private String customerId;
    private double recency;
    private double frequency;
    private double monetary;
    private double productViewsEngagement;
    private double timeSpentEngagement;
    private List<Order> orderList = new ArrayList<Order>();
    ListIterator<Order> iter = orderList.listIterator();

    // Getters
    public String getCustomerId() {
        return customerId;
    }
    public List<Order> getOrderList () {
        return orderList;
    }

    // Setters
    public void setCustomerId(String newCustomerId) {
        this.customerId = newCustomerId;
    }


    public void addOrder(Order order) {
        orderList.add(order);
    }


    public void sortOrders(){

        Collections.sort(orderList, new Comparator<Order>() {
            public int compare(Order o1, Order o2) {
                return o1.getDatePurchasedDateFormat().compareTo(o2.getDatePurchasedDateFormat()))
            }
        });

        //other way
        //orderList.sort((o1,o2) -> o1.getDatePurchasedDateFormat().compareTo(o2.getDatePurchasedDateFormat()));
    }


    public void calculateRecency(String customerId, int orderIndex){    //in days  //NA DOKIMASW KAI ME LOCALDATETIME

        int numberOfOrders = getOrderList().size();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lastDate = orderList.get(numberOfOrders-1).getDatePurchasedString();
        String beforeLastDate = orderList.get(numberOfOrders-2).getDatePurchasedString();


        try {
            Date lastOrderDate = myFormat.parse(lastDate);
            Date beforeLastOrderDate = myFormat.parse(beforeLastDate);
            long diff = lastOrderDate.getTime() - beforeLastOrderDate.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            double recencey = (double)diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void calculateFrequency(String customerId, int orderIndex) {   //by using days

        int numberOfOrders = getOrderList().size();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lastDate = orderList.get(numberOfOrders-1).getDatePurchasedString();
        String firstDate = orderList.get(0).getDatePurchasedString();


        try {
            Date lastOrderDate = myFormat.parse(lastDate);
            Date firstOrderDate = myFormat.parse(firstDate);
            long diff = lastOrderDate.getTime() - firstOrderDate.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            double frequency = (double)numberOfOrders / (double)diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void calculateMonetary(String customerId, int orderIndex) {

        monetary = 0;
        while (iter.hasNext()) {
            monetary = monetary + iter.next().getOrderTotal();
        }

    }

}




