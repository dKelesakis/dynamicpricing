package org.deeplearning4j.examples.feedforward.regression;

import java.io.*;


public class DynamicPricingMain {


    public static void main(String[] args) throws IOException, InterruptedException {


        //marketplace parameters
        int numberOfShops = 10;
        int numberOfProducts = 12883; //10//100//1000//12883  //gia na leitoyrghsei to 2o nn prepei na valw ola ta proionta
        int numberOfCustomers = 1000;

        //create marketplace
        Marketplace simulationMarketplace = new Marketplace(numberOfShops, numberOfProducts, numberOfCustomers);
        //create shops
        simulationMarketplace.createShops(numberOfShops);
        //create products for every shop
        for (int k = 0; k < numberOfShops; k++) {
            simulationMarketplace.getShopList().get(k).createProducts(numberOfProducts);
        }

        //export data for NN1 in csv format
       simulationMarketplace.exportNN1DataCSV();
       simulationMarketplace.dataNormaliserCSV(10, 1);

        //Note: choose one of the two following choices

        //Case 0
        //create random customers with random wtp
      //  simulationMarketplace.createCustomers(numberOfCustomers);   //always create customers after creating products

        //Case 1
        //create customers with personal profile including orders from orders file
        //simulationMarketplace.readOrders();
        //read views and combine them with order information
        //simulationMarketplace.readViews();


         //Case 2
        simulationMarketplace.readOrderViews();




        //Create the NN1
        int NN1numberOfInputNodes = 9; //9
        int NN1numberOfHiddenNodes = 6;//6//128//32 //16 //οχι 100
        int NN1numberOfOutputNodes = 1;

        NeuralNetwork neuralNetwork1 = new NeuralNetwork(NN1numberOfInputNodes, NN1numberOfHiddenNodes, NN1numberOfOutputNodes);
        neuralNetwork1.setWeights(9,6);
        neuralNetwork1.setWeights(6,1);
        neuralNetwork1.trainAndEvaluateNN1();

        simulationMarketplace.setNeuralPrices();  //at product objects //necessary step

        //export data for NN2 in csv format, now that neural prices are set
        simulationMarketplace.exportNN2DataCSV();
        simulationMarketplace.dataNormaliserCSV(7, 2);

        //Create the NN2
        int NN2numberOfInputNodes = 6;
        int NN2numberOfHiddenNodes = 6;//6//128//32 //16 //οχι 100
        int NN2numberOfOutputNodes = 1;

       NeuralNetwork neuralNetwork2 = new NeuralNetwork(NN2numberOfInputNodes, NN2numberOfHiddenNodes, NN2numberOfOutputNodes);
       neuralNetwork1.setWeights(6,6);
       neuralNetwork1.setWeights(6,1);
       neuralNetwork1.trainAndEvaluateNN2();


        //peirama me wtp
        /*
        int numberOfBuyers[] = {0,0,0,0,0,0,0,0,0,0};
        double prices[] = {18.69,15.04,15.75,14.89,14.89,14.23,15.36,14.03,14.29,14.69};  //2nd experiment

        for (int k = 0; k < numberOfShops; k++) {
            Product[] productListTemp = shopList[k].getProductList();
            double priceShopTemp = prices[k];            //productListTemp[0].getPrice();

            for (int i = 0; i < numberOfCustomers; i++) {

                double wtp[] = customerList[i].getWtp();
                if (wtp[0] >= priceShopTemp){
                    numberOfBuyers[k]= numberOfBuyers[k] + 1;
                }
            }
        }

        for (int k = 0; k < numberOfShops; k++) {
            System.out.println(numberOfBuyers[k]+"\n");
        }
        */


    }


}


