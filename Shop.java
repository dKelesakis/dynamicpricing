package org.deeplearning4j.examples.feedforward.regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Shop {

    //Shop object variables
    private double deliveryCost;
    private double deliveryTime;
    private boolean deliveryMethod;
    private boolean paymentMethod;
    private double sellerReviews;
    private double sellerReputation;
    private double averageProfitDifference;
    private List<Product> productList = new ArrayList<Product>();


    //Constructor
    Shop(int numberOfProducts, double deliveryCost, double deliveryTime, boolean deliveryMethod, boolean paymentMethod, double sellerReviews, double sellerReputation, double averageProfitDifference) {
        this.deliveryCost = deliveryCost;
        this.deliveryTime = deliveryTime;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
        this.sellerReviews = sellerReviews;
        this.sellerReputation = sellerReputation;
        this.averageProfitDifference = averageProfitDifference;
    }


    // Getters
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
    public List<Product> getProductList () {
        return productList;
    }

    // Setters
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
    public void setAverageProfitDifference(double newAverageProfitDifference) { this.averageProfitDifference = newAverageProfitDifference; }


    public void createProducts(int numberOfProducts) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";    // use comma as separator

        int counter = 0;

        try {

            br = new BufferedReader(new FileReader("data_products.csv"));
            while ((line = br.readLine()) != null & counter < numberOfProducts) {

                //read data
                String[] productDetailsString = line.split(cvsSplitBy);

                //create product & set attibutes
                Product product = new Product();
                product.setProductId(productDetailsString[0]);
                product.setBaseCost(Double.parseDouble(productDetailsString[1]));
                product.setBrandPower(Double.parseDouble(productDetailsString[2]));
                double referencePrice = Double.parseDouble(productDetailsString[3]);
                product.setReferencePrice(referencePrice);

                //ypologismos timhs vash perithwroy kerdoys
                Random r = new Random();
                double price = referencePrice + referencePrice * (2 * this.getAverageProfitDifference() * r.nextDouble());
                product.setPrice(price);  //sos vash perithwrioy kerdoys

                //estimate relative base cost, relative price -> essential for 2nd nn, experiment with it at first nn
                double relativePrice = (price / referencePrice) *100;   //or  ( (price - referencePrice)  / referencePrice) *100;
                product.setRelativePrice(relativePrice);


                //set stock, 90% probability available
                float stockProbability = r.nextFloat();

                if (stockProbability <= 0.10f) {
                    product.setStockBool(false);
                } else {
                    product.setStockBool(true);
                }

                productList.add(product);
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

    public double getNeuralPriceByProductId(String productId){
        for(int i=0; i<productList.size();i++){
            if(productId.equals(productList.get(i).getProductId())){
                return productList.get(i).getNN1Price();
            }
        }
        System.out.println("de vrhka to"+productId);
        return -1;
    }

    public double getPriceByProductId(String productId) {
        for(int i=0; i<productList.size();i++){
            if(productId.equals(productList.get(i).getProductId())){
                return productList.get(i).getPrice();
            }
        }
        System.out.println("de vrhka to"+productId);
        return -1;
    }

}
