package org.deeplearning4j.examples.feedforward.regression;

import java.time.Duration;
import java.time.LocalDateTime;
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
   // private double recency;
    //private double frequency;
    //private double monetary;
    //private double totalProductViewsEngagement;
    //private double totalTimeSpentEngagement;
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
                return o1.getDatePurchasedDateFormat().compareTo(o2.getDatePurchasedDateFormat());
            }
        });

        //other way
        //orderList.sort((o1,o2) -> o1.getDatePurchasedDateFormat().compareTo(o2.getDatePurchasedDateFormat()));
    }


    public double calculateRecency(int orderIndex){    //in days  //NA DOKIMASW KAI ME LOCALDATETIME

        if (orderIndex>0) {
            LocalDateTime lastOrder = orderList.get(orderIndex).getDatePurchasedDateFormat();
            LocalDateTime previousOrder = orderList.get(orderIndex-1).getDatePurchasedDateFormat();
            double recency = (Duration.between(previousOrder, lastOrder).toDays());
            return recency;
        }else if(orderIndex == 0){
            LocalDateTime lastOrder = orderList.get(orderIndex).getDatePurchasedDateFormat();
            LocalDateTime previousOrder = orderList.get(orderIndex).getDatePurchasedDateFormat().minusYears(1); //an einai h 1h paraggelia thewrw oti eixe ena xrono na paraggeilei
            double recency = (Duration.between(previousOrder, lastOrder).toDays());
            return recency;
        }

        return -1;
    }


    public double calculateFrequency(int orderIndex) {   //by using days

        double orderIndexDouble = (double) orderIndex;
        if (orderIndex>0) {

            LocalDateTime lastOrder = orderList.get(orderIndex).getDatePurchasedDateFormat();
            LocalDateTime firstOrder = orderList.get(0).getDatePurchasedDateFormat();
            double frequency = ( ((orderIndexDouble + 1.0) * 100 ) / (Duration.between(firstOrder, lastOrder).toDays()));
            return frequency;

        }else if(orderIndex == 0){

            LocalDateTime lastOrder = orderList.get(orderIndex).getDatePurchasedDateFormat();
            LocalDateTime previousOrder = orderList.get(orderIndex).getDatePurchasedDateFormat().minusYears(1); //an einai h 1h paraggelia thewrw oti eixe ena xrono na paraggeilei
            double frequency = ( (2.0 * 100) / (Duration.between(previousOrder, lastOrder).toDays()));  //gia na apofygw poly mikra noymera//ypothetkh paraggelia prin 1 xrono
            return frequency;
        }

        return -1;
    }

    public double calculateMonetary(int orderIndex) {

        double monetary = 0;

        for(int i=0;i<=orderIndex;i++){
            monetary = monetary + orderList.get(i).getOrderTotal();
        }
        return monetary;
    }


    public boolean sortView(String dateViewed) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateViewedDateFormat = LocalDateTime.parse(dateViewed, formatter);


        for(int i=0;i<this.getOrderList().size();i++){
            if(dateViewedDateFormat.isBefore(this.getOrderList().get(i).getDatePurchasedDateFormat()) || dateViewedDateFormat.isEqual(this.getOrderList().get(i).getDatePurchasedDateFormat()) ) {

                //page view
                this.getOrderList().get(i).addPageView();

                //timespent
                this.getOrderList().get(i).handleTimeSpent(dateViewedDateFormat);

                return true;
            }
        }

        return false;
    }

}




