package org.deeplearning4j.examples.feedforward.regression;

import java.util.Random;

public class Marketplace {

    //Marketplace object variables
    private int numberOfShops;
    private int numberOfProducts;
    private int numberOfCustomers;
    private Shop[] shopList;
    private Customer[] customerList;

    //Constructor
    Marketplace(int numberOfShops, int numberOfProducts, int numberOfCustomers) {
        this.numberOfShops = numberOfShops;
        this.numberOfProducts = numberOfProducts;
        this.numberOfCustomers = numberOfCustomers;
        shopList = new Shop[numberOfShops];
        customerList = new Customer[numberOfCustomers];
    }


    // Getters
    public int getNumberOfShops () {
        return numberOfShops;
    }
    public int getNumberOfProducts () {
        return numberOfProducts;
    }
    public int getNumberOfCustomers () {
        return numberOfCustomers;
    }
    public Shop[] getShopList () {
        return shopList;
    }
    public Shop getShop(int i) {
        return shopList[i];
    }
    public Customer[] getCustomerList () {
        return customerList;
    }
    public Customer getCustomer(int i) {
        return customerList[i];
    }

    // Setters
    public void setNumberOfShops ( int newNumberOfShops){ this.numberOfShops = newNumberOfShops; }
    public void setNumberOfProducts ( int newNumberOfProducts){
        this.numberOfProducts = newNumberOfProducts;
    }
    public void setNumberOfCustomers ( int newNumberOfCustomers){ this.numberOfCustomers = newNumberOfCustomers; }
    public void setShopList ( Shop[] shopList){ this.shopList = shopList; }
    public void setShop(int i, Shop shop) {
        this.shopList[i] = shop;
    }
    public void setCustomerList ( Customer[] customerList){ this.customerList = customerList; }
    public void setCustomer(int i, Customer customer) {
        this.customerList[i] = customer;
    }

    public void createShops(int numberOfShops) {

        //if number of shops==10
        shopList[0] = new Shop(numberOfProducts, 5, 2.5, true, false, 5, 4, 0);
        shopList[1] = new Shop(numberOfProducts, 5.5, 3, true, false, 4, 4, 0.03);
        shopList[2] = new Shop(numberOfProducts, 6, 2, true, true, 4, 5, 0.03);
        shopList[3] = new Shop(numberOfProducts, 4.5, 3.5, true, false, 2, 3, 0.005);
        shopList[4] = new Shop(numberOfProducts, 5, 4, true, false, 5, 4, 0.005);
        shopList[5] = new Shop(numberOfProducts, 5, 3, true, true, 5, 4, -0.05);
        shopList[6] = new Shop(numberOfProducts, 5.5, 3, true, false, 4, 5, 0.05);
        shopList[7] = new Shop(numberOfProducts, 4, 5, true, true, 3, 3, -0.05);
        shopList[8] = new Shop(numberOfProducts, 5, 3, true, false, 5, 5, -0.05);
        shopList[9] = new Shop(numberOfProducts, 5, 4, false, false, 5, 5, -0.03);

    }


    public void createCustomers(int numberOfCustomers) {

        for (int i = 0; i < numberOfCustomers; i++) {

            Customer customer = new Customer(numberOfProducts);

            double wtpArray[] = new double[numberOfProducts];
            //kanw omoiomorfh katanomh wtp

            //create distribution for customer wtps
            Random rand = new Random();

            //ypologizw wtp toy customer gia to kathena
            Product[] productListTemp = this.shopList[0].getProductList(); //pairnw ta base price apo ena tyxaio, einai idia gia ola ta eshop

            for (int j = 0; j < numberOfProducts; j++) {

                //scannarw ta products
                double basePriceTemp = productListTemp[j].getBasePrice();
                double wtpAverage = 0.95 * basePriceTemp;


                // normal distribution
                double wtp = (rand.nextGaussian() * 0.3 * wtpAverage);
                while (Math.abs(wtp) > 0.3 * wtpAverage) //gialogoys asfaleias an ksefygei kati ap ta oria ths katanomhs
                    wtp = (rand.nextGaussian() * 0.3 * wtpAverage);
                wtp = wtp + wtpAverage;
                wtpArray[j] = wtp;
                // System.out.println(wtp);

                if (basePriceTemp <= wtp) {
                    //  System.out.println("petyxhmenh polhsh");
                }
            }

            customer.setWtp(wtpArray);
            customerList[i] = customer;

        }
    }


}

//trexw to nevroniko, ftiaxnw tis times toy eshop1
       /* Product[] productListTemp = shopList[0].getProductList();

        for (int j = 0; j < numberOfProducts; j++) {
          //  productListTemp[j].setPrice(eksodos nevronikoy);
        }
        shopList[0].setProductList(productListTemp);
*/


//vazw toys pelates na pane sta magazia na paroyn tyxaia proionta kai ypologizw ta kerdh
