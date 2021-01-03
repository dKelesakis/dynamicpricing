package org.deeplearning4j.examples.feedforward.regression;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        //marketplace parameters
        int numberOfShops = 10;
        int numberOfProducts = 12883; 
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


        //Note: choose one of the following choices

        /*
        //Case 1:
        //create random customers with random wtp (products already created)
        simulationMarketplace.createCustomers(numberOfCustomers);   
        */

        /*
        //Case 2:
        //create customers with personal profiles, taking into account orders from orders file
        simulationMarketplace.readOrders();
        //read views and combine them with order information
        simulationMarketplace.readViews();
        */

        ///*
        //Case 3:
        //create customers with personal profiles, reading orders and views all-together
        simulationMarketplace.readOrderViews();
        //*/

        
        //Create the NN1
        int NN1numberOfInputNodes = 9; 
        int NN1numberOfHiddenNodes = 6;
        int NN1numberOfOutputNodes = 1;

        NeuralNetwork neuralNetwork1 = new NeuralNetwork(NN1numberOfInputNodes, NN1numberOfHiddenNodes, NN1numberOfOutputNodes);
        neuralNetwork1.setWeights(9,6);
        neuralNetwork1.setWeights(6,1);
        neuralNetwork1.trainAndEvaluateNN1();

        simulationMarketplace.setNeuralPrices();  

        //export data for NN2 in csv format, now that neural prices are set
        simulationMarketplace.exportNN2DataCSV();
        simulationMarketplace.dataNormaliserCSV(7, 2);

        //Create the NN2
        int NN2numberOfInputNodes = 6;
        int NN2numberOfHiddenNodes = 6;
        int NN2numberOfOutputNodes = 1;

        NeuralNetwork neuralNetwork2 = new NeuralNetwork(NN2numberOfInputNodes, NN2numberOfHiddenNodes, NN2numberOfOutputNodes);
        neuralNetwork1.setWeights(6,6);
        neuralNetwork1.setWeights(6,1);
        neuralNetwork1.trainAndEvaluateNN2();

    }
}