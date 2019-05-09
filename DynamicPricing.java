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
import org.datavec.api.records.writer.impl.csv.CSVRecordWriter;
import java.io.File;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.List;
import java.util.Random;


public class DynamicPricing {
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
    public static int MIN_RANGE = 0;
    public static int MAX_RANGE = 6;
    static final String COMMA_DELIMITER = ",";
    static final String NEW_LINE_SEPARATOR = "\n";
    static final String FILE_HEADER = "number1,number2,result";

    public static final Random rng = new Random(seed);

    public static void main(String[] args) throws IOException {


        DataSetIterator iterator = getTrainingData(batchSize, rng);

        //Create the network
        int numInput = 2;
        int numOutputs = 1;
        int nHidden = 10;
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
        // Test the addition of 2 numbers (Try different numbers here)
        final INDArray input = Nd4j.create(new double[]{3.4, 4.5}, new int[]{1, 2});
        INDArray out = net.output(input, false);
        System.out.println(out);

    }

    private static DataSetIterator getTrainingData(int batchSize, Random rand) throws IOException {
        double[] sum = new double[nSamples];
        double[] input1 = new double[nSamples];
        double[] input2 = new double[nSamples];

        PrintWriter pw = new PrintWriter(new File("test.csv"));

        for (int i = 0; i < nSamples; i++) {
            input1[i] = MIN_RANGE + (MAX_RANGE - MIN_RANGE) * rand.nextDouble();
            input2[i] = MIN_RANGE + (MAX_RANGE - MIN_RANGE) * rand.nextDouble();
            sum[i] = input1[i] * input2[i];

            StringBuilder sb = new StringBuilder();
            sb.append(input1[i]);
            sb.append(',');
            sb.append(input2[i]);
            sb.append(',');
            sb.append(sum[i]);
            sb.append('\n');

            pw.write(sb.toString());

        }
        pw.close();
        System.out.println("done!");

            INDArray inputNDArray1 = Nd4j.create(input1, new int[]{nSamples, 1});
            INDArray inputNDArray2 = Nd4j.create(input2, new int[]{nSamples, 1});
            INDArray inputNDArray = Nd4j.hstack(inputNDArray1, inputNDArray2);
            INDArray outPut = Nd4j.create(sum, new int[]{nSamples, 1});
            DataSet dataSet = new DataSet(inputNDArray, outPut);
            List<DataSet> listDs = dataSet.asList();
            Collections.shuffle(listDs, rng);
            return new ListDataSetIterator(listDs, batchSize);

        }
    }

