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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

/**
 * Created by Anwar on 3/15/2016.
 * An example of regression neural network for performing addition
 */
public class RegressionMultiply{
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

        Marketplace simulationMarketplace = new Marketplace(1000, 12883, 10);
/*
        final String filenameTrain  = new ClassPathResource("/classification/eshop2to10compressed4inputs.csv").getFile().getPath();
        //Generate the training data
        //Load the training data:
        RecordReader rr = new CSVRecordReader();
//        rr.initialize(new FileSplit(new File("src/main/resources/classification/linear_data_train.csv")));
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr,batchSize,0,1);
      //  DataSetIterator iterator = ; //getTrainingData(batchSize,rng);

        //Create the network
        int numInput = 4;
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
        for( int i=0; i<nEpochs; i++ ){
            iterator.reset();
            net.fit(iterator);
        }


        // Test the addition of 2 numbers (Try different numbers here)
        INDArray input = Nd4j.create(new double[] { 1.536, 5.5, 1, 0.2756844}, new int[] { 1, 4 });//1, 0, 3, 4, 4
        INDArray out = net.output(input, false);
        System.out.println(out);




        String csvFile = "C:/Users/Dimitris/dl4j-examples/dl4j-examples/src/main/resources/classification/first100pricescompressed4inputs.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] productdetailsstring = line.split(cvsSplitBy);


                //System.out.println("Country [code= " + productdetailsstring[0] + " , name=" + productdetailsstring[1] + "]");

                //convert string to double
                double[] productdetails = Arrays.stream(productdetailsstring).mapToDouble(Double::parseDouble).toArray();

             input = Nd4j.create(new double[] { productdetails[0], productdetails[1], productdetails[2], productdetails[3]}, new int[] { 1, 4 }); //, productdetails[4], productdetails[5], productdetails[6], productdetails[7], productdetails[8]
             System.out.println("Country [code= " + productdetails[0] + " , name=" + productdetails[1] + "]" + productdetails[2] + productdetails[3]);
             out = net.output(input, false);
             System.out.println(out);

             /*if (out<=productcost){
                 out = 1.2 * productcost
             }*/

             /*if (out>= 2 * productcost){
                 out = 2 * productcost
             }*/
        /*    }

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






        }*/


    }
}


