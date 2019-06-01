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
    //Random number generator seed, for reproducability
    public static final int seed = 12345;
    //Number of epochs (full passes of the data)
    public static final int nEpochs = 200;
    //Number of data points
    public static final int nSamples = 1000;
    //Batch size: i.e., each epoch has nSamples/batchSize parameter updates
    public static final int batchSize = 100;
    //Network learning rate
    public static final double learningRate = 0.01;
    // The range of the sample data, data in range (0-1 is sensitive for NN, you can try other ranges and see how it effects the results
    // also try changing the range along with changing the activation function


    public static final Random rng = new Random(seed);

    public static void main(String[] args) throws IOException, InterruptedException {


        //marketplace parameters
        int numberOfShops = 10;
        int numberOfProducts = 10; //12883
        int numberOfCustomers = 1000;


        //marketplace's list of objects
        Shop[] shopList = new Shop[numberOfShops];
        Customer[] customerList = new Customer[numberOfCustomers];


        //create a marketplace object for simulation of the market
        Marketplace simulationMarketplace = new Marketplace(numberOfShops, numberOfProducts, numberOfCustomers);


        //create the shops of the market
        // if (numberOfShops == 10) {
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
        // }


        //create products for every shop

        for (int k = 0; k < numberOfShops; k++) {

            Product[] productList = new Product[numberOfProducts];

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

                    double finalPrice = basePrice + basePrice * (2 * shopList[k].getAverageProfitDifference() * r.nextDouble());
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


            shopList[k].setProductList(productList);
        }


        //create customers
        for (int i = 0; i < numberOfCustomers; i++) {

            Customer customer = new Customer(numberOfProducts);

            double wtpArray[] = new double[numberOfProducts];
            //kanw omoiomorfh katanomh wtp

            //create distribution for customer wtps
            Random rand = new Random();

            //ypologizw wtp toy customer gia to kathena
            Product[] productListTemp = shopList[0].getProductList(); //pairnw ta base price apo ena tyxaio, einai idia gia ola ta eshop

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


        //create csv nevronikoy diktyoy
        try {
            PrintWriter pw = new PrintWriter(new File("experimentData.csv"));


            for (int k = 0; k < numberOfShops; k++) {

                Product[] productListTemp = shopList[k].getProductList();

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


                    sb.append(shopList[k].getDeliveryCost());
                    sb.append(',');
                    sb.append(shopList[k].getDeliveryTime());
                    sb.append(',');


                    if (shopList[k].getDeliveryMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    if (shopList[k].getPaymentMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    sb.append(shopList[k].getSellerReviews());
                    sb.append(',');
                    sb.append(shopList[k].getSellerReputation());
                    sb.append(',');

                    sb.append(productListTemp[j].getPrice());
                    sb.append('\n');


                    pw.write(sb.toString());
                    //   System.out.println(sb);
                }


            }
            pw.close();
            System.out.println("done!");
          /*  System.out.println(shopList[0].getDeliveryCost());
            System.out.println(shopList[1].getDeliveryCost());
            System.out.println(shopList[2].getDeliveryCost());*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //Load the training data:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File("experimentData.csv")));

        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, 0, 1);


        //Create the network
        int numInput = 9;
        int numOutputs = 1;  //to kanw 1 kai peirazw th seira 90
        int nHidden = 32;  //16 //οχι 100
        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(learningRate, 0.9))
            .list()
            .layer(0, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                .activation(Activation.TANH)
                .build())
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.IDENTITY)
                .nIn(nHidden).nOut(numOutputs).build())
            .pretrain(false).backprop(true).build()
        );
        net.init();
        net.setListeners(new ScoreIterationListener(1));


        //Train the network on the full data set, and evaluate in periodically
        for (int i = 0; i < nEpochs; i++) {
            iterator.reset();
            net.fit(iterator);
        }


        String csvFile = "C:/Users/Dimitris/deeplearning4j_dynamicpricing_thesis/experimentData.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";


        try {
            PrintWriter pw1 = new PrintWriter(new File("outputData.csv"));

            try {

                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] productdetailsstring = line.split(cvsSplitBy);


                    //System.out.println("Country [code= " + productdetailsstring[0] + " , name=" + productdetailsstring[1] + "]");

                    //convert string to double
                    double[] productdetails = Arrays.stream(productdetailsstring).mapToDouble(Double::parseDouble).toArray();

                    INDArray input = Nd4j.create(new double[]{productdetails[0], productdetails[1], productdetails[2], productdetails[3], productdetails[4], productdetails[5], productdetails[6], productdetails[7], productdetails[8]}, new int[]{1, 9}); //
                    //System.out.println(input);
                    INDArray out = net.output(input, false);
                    //System.out.println(out);

                    double outputPriceNN = out.getDouble(0, 0);
                    double outputPriceNNAfterCorrection = outputPriceNN;
                    // System.out.println(outputPriceNN);

                    double productCost = productdetails[0];
                    // System.out.println(error/numberOfProducts);    //σοσ το εδιτ
                    if (outputPriceNN <= productCost) {
                        outputPriceNNAfterCorrection = 1.2 * productCost;
                    }

                    if (outputPriceNN > 2 * productCost) {
                        outputPriceNNAfterCorrection = 2 * productCost;
                    }

                    double errorBeforeCorrection = Math.abs(outputPriceNN - productdetails[9]);
                    double errorAfterCorrection = Math.abs(outputPriceNNAfterCorrection - productdetails[9]);


                    StringBuilder sb = new StringBuilder();
                    pw1.write(sb.toString());

                    sb.append(outputPriceNN);
                    sb.append(',');
                    sb.append(errorBeforeCorrection);
                    sb.append(',');
                    sb.append(outputPriceNNAfterCorrection);
                    sb.append(',');
                    sb.append(errorAfterCorrection);
                    sb.append('\n');

                    pw1.write(sb.toString());


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

            pw1.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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


    }
}


