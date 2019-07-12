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


        //Note: choose one of the two following choices
        //create random customers with random wtp
        //simulationMarketplace.createCustomers(numberOfCustomers);   //always create customers after creating products
        //create customers with personal profile including orders from orders file
        simulationMarketplace.readOrders();

        //export data for nn in csv format
        simulationMarketplace.exportDataCSV();
        simulationMarketplace.dataNormaliserCSV(10);


        //Create the neural network
        int numberOfInputNodes = 9; //9
        int numberOfHiddenNodes = 32;//6//128//32 //16 //οχι 100
        int numberOfOutputNodes = 1;

        //NeuralNetwork neuralNetwork1 = new NeuralNetwork(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
        //neuralNetwork1.trainAndEvaluateNetwork();


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


