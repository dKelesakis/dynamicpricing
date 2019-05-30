package org.deeplearning4j.examples.feedforward.regression;

import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Shop extends Marketplace {

    Product[] productList = new Product[numberOfProducts];

    //Constructor
    Shop(double transportationCost, boolean paymentMethod, boolean deliveryMethod, double deliveryTime,  double sellerReviews, double sellerReputation, double averageProfitDifference){

        super(numberOfCustomers, numberOfProducts, numberOfShops);
        this.transportationCost = transportationCost;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.deliveryTime = deliveryTime;
        this.sellerReviews = sellerReviews;
        this.sellerReputation = sellerReputation;
        this.averageProfitDifference = averageProfitDifference;


        Product[] productList = new Product[numberOfProducts];

        for(int i=0;i<numberOfProducts;i++){

            String csvFile = "C:/Users/Dimitris/dl4j-examples/dl4j-examples/src/main/resources/classification/product_input.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            try {

                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] productdetailsstring = line.split(cvsSplitBy);

                    //convert string to double
                    double[] productdetails = Arrays.stream(productdetailsstring).mapToDouble(Double::parseDouble).toArray();

                    //ypologismos timhs vash perithwroy kerdoys

                    productList[i].setBaseCost(productdetails[0]);
                    productList[i].setBrandPower(productdetails[1]);


                    double basePrice = productdetails[2];
                    productList[i].setBasePrice(basePrice);

                    Random r = new Random();

                    double finalPrice = basePrice + basePrice * (2 * averageProfitDifference * r.nextDouble());
                    productList[i].setPrice(finalPrice);  //sos vash perithwrioy kerdoys

                    System.out.println( "timi vashs" + basePrice + "teliki timi" + finalPrice );


                    //set stock, 90% probability available
                    float stockProbability = r.nextFloat();

                    if (stockProbability <= 0.10f) {
                        productList[i].setStockBool(false);
                    }   else{
                        productList[i].setStockBool(true);
                    }


                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }



    }


    static double transportationCost;
    static  boolean paymentMethod;
    static  boolean deliveryMethod;
    static  double deliveryTime;
    static  double sellerReviews;
    static  double sellerReputation;
    static  double averageProfitDifference; //if=0 then dynamic pricing


    // Getters
    public double getTrasportationCost() {
        return transportationCost;
    }
    public boolean getPaymentMethod() {
        return paymentMethod;
    }
    public boolean getDeliveryMethod() {
        return deliveryMethod;
    }
    public double getDeliveryTime() {
        return deliveryTime;
    }
    public double getSellerReviews() { return sellerReviews; }
    public double getSellerReputation() {
        return sellerReputation;
    }
    public double getAverageProfitDifference() {
        return averageProfitDifference;
    }
    public Product[] getProductList() { return productList; }


    // Setters
    public void setTransportationCost(double newTransportationCost) {
        this.transportationCost = newTransportationCost;
    }
    public void setPaymentMethod(boolean newPaymentMethod) {
        this.paymentMethod = newPaymentMethod;
    }
    public void setDeliveryMethod(boolean newDeliveryMethod) {
        this.deliveryMethod = newDeliveryMethod;
    }
    public void setDeliveryTime(double newDeliveryTime) {
        this.deliveryTime = newDeliveryTime;
    }
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
