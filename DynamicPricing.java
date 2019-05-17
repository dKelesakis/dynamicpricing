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
        final String filenameTrain  = new ClassPathResource("/classification/finaldata.csv").getFile().getPath();
        //Generate the training data
        //Load the training data:
        RecordReader rr = new CSVRecordReader();
//        rr.initialize(new FileSplit(new File("src/main/resources/classification/linear_data_train.csv")));
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr,batchSize,0,1);
      //  DataSetIterator iterator = ; //getTrainingData(batchSize,rng);

        //Create the network
        int numInput = 9;
        int numOutputs = 1;  //to kanw 1 kai peirazw th seira 90
        int nHidden = 21;
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
        final INDArray input = Nd4j.create(new double[] { 7.088, 5, 1, 0, 2.5, 5, 4.83757875, 4, 1 }, new int[] { 1, 9 });
        INDArray out = net.output(input, false);
        System.out.println(out);


    }
}


