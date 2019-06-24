package org.deeplearning4j.examples.feedforward.regression;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import java.util.Arrays;


public class DynamicPricingMain {


    public static void main(String[] args) throws IOException, InterruptedException {


        //marketplace parameters
        int numberOfShops = 10;
        int numberOfProducts = 500; //1000//12883
        int numberOfCustomers = 1000;


        //create a marketplace
        Marketplace simulationMarketplace = new Marketplace(numberOfShops, numberOfProducts, numberOfCustomers);

        //create shops
        simulationMarketplace.createShops(numberOfShops);

        //create products for every shop
        for (int k = 0; k < numberOfShops; k++) {
            Shop currentShop = simulationMarketplace.getShop(k);
            currentShop.createProducts(numberOfProducts);
            simulationMarketplace.setShop(k, currentShop);
        }

        //create Customers
        simulationMarketplace.createCustomers(numberOfCustomers);   //always create customers after creating products

        //Shop[] shopList = simulationMarketplace.getShopList();
        //Customer[] customerList = simulationMarketplace.getCustomerList();



        //create csv nevronikoy diktyoy
        try {
            PrintWriter pw = new PrintWriter(new File("experimentData.csv"));


            for (int k = 0; k < numberOfShops; k++) {

                Product[] productListTemp = simulationMarketplace.getShop(k).getProductList();

                for (int j = 0; j < numberOfProducts; j++) {

                    StringBuilder sb = new StringBuilder();
                    pw.write(sb.toString());

                    sb.append(productListTemp[j].getBaseCost());
                    sb.append(',');
                    sb.append(productListTemp[j].getBrandPower());
                    sb.append(',');

                    if (productListTemp[j].getStockBool() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');


                    sb.append(simulationMarketplace.getShop(k).getDeliveryCost());
                    sb.append(',');
                    sb.append(simulationMarketplace.getShop(k).getDeliveryTime());
                    sb.append(',');


                    if (simulationMarketplace.getShop(k).getDeliveryMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    if (simulationMarketplace.getShop(k).getPaymentMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    sb.append(simulationMarketplace.getShop(k).getSellerReviews());
                    sb.append(',');
                    sb.append(simulationMarketplace.getShop(k).getSellerReputation());
                    sb.append(',');

                    sb.append(productListTemp[j].getPrice());
                    sb.append('\n');


                    pw.write(sb.toString());
                    //   System.out.println(sb);
                }


            }
            pw.close();
            System.out.println("done!");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //Create the neural network
        int numberOfInputNodes = 9; //9
        int numberOfHiddenNodes = 6;//128//32 //16 //οχι 100
        int numberOfOutputNodes = 1;

        NeuralNetwork neuralNetwork1 = new NeuralNetwork(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
        neuralNetwork1.trainAndEvaluateNetwork();


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


