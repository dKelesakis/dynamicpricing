package org.deeplearning4j.examples.feedforward.regression;

import java.util.Random;

public class Marketplace {

    //Constructor
    Marketplace(int numberOfCustomers, int numberOfProducts, int numberOfShops){
        this.numberOfCustomers = numberOfCustomers;
        this.numberOfProducts = numberOfProducts;
        this.numberOfShops = numberOfShops;

        Shop[] shopList = new Shop[numberOfShops];


        if (this.numberOfShops == 10) {

            Shop shop1 = new Shop(5, true, false, 2.5, 5, 4, 0);
            Shop shop2 = new Shop(5.5, true, false, 3, 4, 4, 0.03);
            Shop shop3 = new Shop(6, true, true, 2, 4, 5, 0.03);
            Shop shop4 = new Shop(4.5, true, false, 3.5, 2, 3, 0.005);
            Shop shop5 = new Shop(5, true, false, 4, 5, 4, 0.005);
            Shop shop6 = new Shop(5, true, true, 3, 5, 4, -0.05);
            Shop shop7 = new Shop(5.5, true, false, 3, 4, 5, 0.05);
            Shop shop8 = new Shop(4, true, true, 5, 3, 3, -0.05);
            Shop shop9 = new Shop(5, true, false, 3, 5, 5, -0.05);
            Shop shop10 = new Shop(5, false, false, 4, 5, 5, -0.03);

            shopList[0] = shop1;
            shopList[1] = shop2;
            shopList[2] = shop3;
            shopList[3] = shop4;
            shopList[4] = shop5;
            shopList[5] = shop6;
            shopList[6] = shop7;
            shopList[7] = shop8;
            shopList[8] = shop9;
            shopList[9] = shop10;

        }

        Customer[] customerList = new Customer[numberOfCustomers];

        //ftiaxnw toys customers
        for(int i=0;i<numberOfCustomers;i++) {


            double wtpArray[] = new double[numberOfProducts];
            //kanw omoiomorfh katanomh wtp

            //create distribution for customer wtps
            Random rand = new Random();

            //ypologizw wtp toy customer gia to kathena
            Product[] productListTemp = shopList[0].getProductList();

            for (int j= 0; j < numberOfProducts; j++) {

                //scannarw ta products
                double basePriceTemp = productListTemp[j].getBasePrice();
                double wtpAverage = 0.95 * basePriceTemp;


                // normal distribution
                double wtp = (rand.nextGaussian() * 0.3 * wtpAverage);
                while (Math.abs(wtp) > 0.3 * wtpAverage) //gialogoys asfaleias an ksefygei kati ap ta oria ths katanomhs
                    wtp = (rand.nextGaussian() * 0.3 * wtpAverage);
                wtp = wtp + wtpAverage;
                wtpArray[j]=wtp;
                System.out.println(wtp);

                if (basePriceTemp <= wtp) {
                    System.out.println("petyxhmenh polhsh");
                }
            }

            customerList[i].setWtp(wtpArray);
        }


        //create csv nevronikoy diktyoy

        //for eshops 1-10
          //for products 1-1289090
                //write csv


    }

     static int numberOfCustomers;
     static int numberOfProducts;
     static int numberOfShops;


    // Getters
    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }
    public int getNumberOfProducts() {
        return numberOfProducts;
    }
    public int numberOfShops() {
        return numberOfShops;
    }


    // Setters
    public void setNumberOfCustomers(int newNumberOfCustomers) {
        this.numberOfCustomers = newNumberOfCustomers;
    }
    public void setNumberOfProducts(int newNumberOfProducts) {
        this.numberOfProducts = newNumberOfProducts;
    }
    public void setNumberOfShops(int newNumberOfShops) {
        this.numberOfShops = newNumberOfShops;
    }



}
