package org.deeplearning4j.examples.feedforward.regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Shop {

    //Shop object variables
    Product[] productList;
    private double deliveryCost;
    private double deliveryTime;
    private boolean deliveryMethod;
    private boolean paymentMethod;
    private double sellerReviews;
    private double sellerReputation;
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
    public Product[] getProductList() {
        return productList;
    }

    public Product getProduct(int i) {
        return productList[i];
    }

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

    public double getSellerReviews() {
        return sellerReviews;
    }

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

    public void setProduct(int i, Product product) {
        this.productList[i] = product;
    }

    public void setDeliveryCost(double newDeliveryCost) {
        this.deliveryCost = newDeliveryCost;
    }

    public void setDeliveryTime(double newDeliveryTime) {
        this.deliveryTime = newDeliveryTime;
    }

    public void setDeliveryMethod(boolean newDeliveryMethod) {
        this.deliveryMethod = newDeliveryMethod;
    }

    public void setPaymentMethod(boolean newPaymentMethod) {
        this.paymentMethod = newPaymentMethod;
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


    public void createProducts(int numberOfProducts) {

        String csvFile = "C:/Users/Dimitris/dl4j-examples/dl4j-examples/src/main/resources/classification/product_input.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        int counter = 0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null & counter < numberOfProducts) {

                Product product = new Product();

                // use comma as separator
                String[] productdetailsstring = line.split(cvsSplitBy);

                //convert string to double
                double[] productdetails = Arrays.stream(productdetailsstring).mapToDouble(Double::parseDouble).toArray();

                //ypologismos timhs vash perithwroy kerdoys

                product.setBaseCost(productdetails[0]);
                product.setBrandPower(productdetails[1]);


                double basePrice = productdetails[2];
                product.setBasePrice(basePrice);

                Random r = new Random();

                double finalPrice = basePrice + basePrice * (2 * this.getAverageProfitDifference() * r.nextDouble());
                product.setPrice(finalPrice);  //sos vash perithwrioy kerdoys

                // System.out.println( "timi vashs" + basePrice + "teliki timi" + finalPrice ); //ws edw trexei ok


                //set stock, 90% probability available
                float stockProbability = r.nextFloat();

                if (stockProbability <= 0.10f) {
                    product.setStockBool(false);
                } else {
                    product.setStockBool(true);
                }

                productList[counter] = product;
                counter++;
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
