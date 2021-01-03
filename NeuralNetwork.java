package org.deeplearning4j.examples.feedforward.regression;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.eval.RegressionEvaluation;

import java.io.*;
import java.util.Random;
import java.util.Arrays;
import java.util.*;

public class NeuralNetwork {

    private int numberOfInputNodes;
    private int numberOfHiddenNodes;
    private int numberOfOutputNodes;

    //Constructor
    NeuralNetwork(int numberOfInputNodes, int numberOfHiddenNodes, int numberOfOutputNodes) {
        this.numberOfInputNodes = numberOfInputNodes;
        this.numberOfHiddenNodes = numberOfHiddenNodes;
        this.numberOfOutputNodes = numberOfOutputNodes;
    }

    // Getters
    public int getNumberOfInputNodes () {
        return numberOfInputNodes;
    }
    public int getNumberOfHiddenNodes () {
        return numberOfHiddenNodes;
    }
    public int getNumberOfOutputNodes () {
        return numberOfOutputNodes;
    }

    // Setters
    public void setNumberOfInputNodes (int newNumberOfInputNodes){ 
        this.numberOfInputNodes = newNumberOfInputNodes; 
    }
    public void setNumberOfHiddenNodes (int newNumberOfHiddenNodes){ 
        this.numberOfHiddenNodes = newNumberOfHiddenNodes;
    }
    public void setNumberOfOutputNodes (int newNumberOfOutputNodes){ 
        this.numberOfOutputNodes = newNumberOfOutputNodes; 
    }


    //Random number generator seed, for reproducability
    public static final int seed = 12345;
    //Number of epochs (full passes of the data)
    public static final int nEpochs = 2000; 
    //Number of data points
    public static final int nSamples = 1000; 
    //Batch size: i.e., each epoch has nSamples/batchSize parameter updates
    public static final int batchSize = 1000; 
    //Network learning rate
    public static final double learningRate = 0.00000001; 
    // The range of the sample data, data in range (0-1 is sensitive for NN, you can try other ranges and see how it effects the results
    // also try changing the range along with changing the activation function


    public static final Random rng = new Random(seed);

    public void trainAndEvaluateNN1()throws IOException, InterruptedException {

        //Load the training data:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File("inputNormalised_NN1.csv")));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, 0, 1);

        //Load the test/evaluation data:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File("inputNormalised_NN1.csv"))); //experimentData1
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 1);

        //build the neural network
        //MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .updater(new Nesterovs(learningRate, 0.9))
            .list()
            .layer(0, new DenseLayer.Builder().nIn(numberOfInputNodes).nOut(numberOfHiddenNodes)
                .weightInit(WeightInit.XAVIER) 
                .activation(Activation.RELU)  //RELU  /TANH //RELU
                .build())
            //add hiddelayer
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)    //MSE //NEGATIVELOGLIKELIHOOD
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.LEAKYRELU)   //LEAKYRELU     //IDENTITY //SOFTMAX
                .nIn(numberOfHiddenNodes).nOut(numberOfOutputNodes).build())
            .pretrain(false).backprop(true).build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates

        //set weights of input layer
        double weightsLayer0[] = setWeights(numberOfInputNodes, numberOfHiddenNodes);
        INDArray paramsLayer0 = Nd4j.create(weightsLayer0);
        model.getLayer(0).setParams(paramsLayer0);

        //set weights of hidden layer
        double weightsLayer1[] = setWeights(numberOfHiddenNodes, numberOfOutputNodes);
        INDArray paramsLayer1 = Nd4j.create(weightsLayer1);
        model.getLayer(1).setParams(paramsLayer1);

        //Train the network on the full data set, and evaluate it periodically
        for ( int n = 0; n < nEpochs; n++) {
            model.fit( iterator );
        }

        //Statistics of regression evaluation
        RegressionEvaluation eval = model.evaluateRegression(testIter);
        //System.out.println(eval.stats());
        //RegressionEvaluation eval =  new RegressionEvaluation(1);
        
        //System.out.println(model.getLayer(0).getParam("W"));
        //System.out.println(model.getLayer(1).getParam("W"));
        

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            PrintWriter pw1 = new PrintWriter(new File("outputDataNN1.csv"));

            try {
                br = new BufferedReader(new FileReader("inputNormalised_NN1.csv"));
                while ((line = br.readLine()) != null) {

                    //use comma as separator
                    String[] productdetailsstring = line.split(cvsSplitBy);
                    //convert string to double
                    double[] productdetails = Arrays.stream(productdetailsstring).mapToDouble(Double::parseDouble).toArray();

                    INDArray input = Nd4j.create(new double[]{productdetails[0], productdetails[1], productdetails[2], productdetails[3], productdetails[4], productdetails[5], productdetails[6], productdetails[7], productdetails[8]}, new int[]{1, 9});
                    INDArray out = model.output(input, false);

                    double outputPriceNN = out.getDouble(0, 0);
                    double outputPriceNNAfterCorrection = outputPriceNN;
            
                    double productCost = productdetails[0];
                    //correct extreme price values
                    if (outputPriceNN <= productCost) {
                        outputPriceNNAfterCorrection = 1.2 * productCost;
                    }
                    if (outputPriceNN > 2 * productCost) {
                        outputPriceNNAfterCorrection = 2 * productCost;
                    }

                    double errorBeforeCorrection = Math.abs(outputPriceNN - productdetails[9]);
                    double squareErrorBeforeCorrection = Math.abs(outputPriceNN - productdetails[9])*Math.abs(outputPriceNN - productdetails[9]);
                    double errorAfterCorrection = Math.abs(outputPriceNNAfterCorrection - productdetails[9]);
                    double squareErrorAfterCorrection = Math.abs(outputPriceNNAfterCorrection - productdetails[9])*Math.abs(outputPriceNNAfterCorrection - productdetails[9]);

                    StringBuilder sb = new StringBuilder();
                    pw1.write(sb.toString());
                    sb.append(outputPriceNN);
                    sb.append(',');
                    sb.append(errorBeforeCorrection);
                    sb.append(',');
                    sb.append(squareErrorBeforeCorrection);
                    sb.append(',');
                    sb.append(outputPriceNNAfterCorrection);
                    sb.append(',');
                    sb.append(errorAfterCorrection);
                    sb.append(',');
                    sb.append(squareErrorAfterCorrection);
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
    }


    public void trainAndEvaluateNN2()throws IOException, InterruptedException {

        //Load the training data:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File("inputNormalised_NN2.csv")));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, 0, 1);

        //Load the test/evaluation data:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File("inputNormalised_NN2.csv"))); //experimentData1
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 1);

        numberOfInputNodes =6;
        numberOfHiddenNodes = 6;
        
        //build the neural network
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .updater(new Nesterovs(learningRate, 0.9))
            .list()
            .layer(0, new DenseLayer.Builder().nIn(numberOfInputNodes).nOut(numberOfHiddenNodes)
                .weightInit(WeightInit.XAVIER) 
                .activation(Activation.RELU)  //RELU  /TANH //RELU
                .build())
            //add hiddelayer
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)    //MSE //NEGATIVELOGLIKELIHOOD
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.LEAKYRELU)   //LEAKYRELU     //IDENTITY //SOFTMAX
                .nIn(numberOfHiddenNodes).nOut(numberOfOutputNodes).build())
            .pretrain(false).backprop(true).build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates

        //Train the network on the full data set, and evaluate in periodically
        for ( int n = 0; n < nEpochs; n++) {
            model.fit( iterator );
        }

        //Statistics of regression evaluation
        RegressionEvaluation eval = model.evaluateRegression(testIter);
        //System.out.println(eval.stats());

        
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int counter = 0;

        try {
            PrintWriter pw1 = new PrintWriter(new File("outputDataNN2.csv"));

            try {
                br = new BufferedReader(new FileReader("inputNormalised_NN2.csv"));
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] customerdetailsstring = line.split(cvsSplitBy);
                    //convert string to double
                    double[] productdetails = Arrays.stream(customerdetailsstring).mapToDouble(Double::parseDouble).toArray();

                    INDArray input = Nd4j.create(new double[]{productdetails[0], productdetails[1], productdetails[2], productdetails[3], productdetails[4], productdetails[5] }, new int[]{1, 6}); 
                    INDArray out = model.output(input, false);
           
                    double outputPriceNN = out.getDouble(0, 0);
                    double outputPriceNNAfterCorrection = outputPriceNN;
                
                    double nn1Price = productdetails[0];
                    //correct extreme price values
                    if (outputPriceNN <= 0.4 * nn1Price) {
                        outputPriceNNAfterCorrection = nn1Price;
                    }
                    if (outputPriceNN > 2 * nn1Price) {
                        outputPriceNNAfterCorrection = 2 *nn1Price;
                    }

                    double errorBeforeCorrection = Math.abs(outputPriceNN - productdetails[6]);
                    double squareErrorBeforeCorrection = Math.abs(outputPriceNN - productdetails[6])*Math.abs(outputPriceNN - productdetails[6]);
                    double errorAfterCorrection = Math.abs(outputPriceNNAfterCorrection - productdetails[6]);
                    double squareErrorAfterCorrection = Math.abs(outputPriceNNAfterCorrection - productdetails[6])*Math.abs(outputPriceNNAfterCorrection - productdetails[6]);

                    StringBuilder sb = new StringBuilder();
                    pw1.write(sb.toString());
                    sb.append(outputPriceNN);
                    sb.append(',');
                    sb.append(errorBeforeCorrection);
                    sb.append(',');
                    sb.append(squareErrorBeforeCorrection);
                    sb.append(',');
                    sb.append(outputPriceNNAfterCorrection);
                    sb.append(',');
                    sb.append(errorAfterCorrection);
                    sb.append(',');
                    sb.append(squareErrorAfterCorrection);
                    sb.append('\n');
                    pw1.write(sb.toString());

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
            pw1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public double[] setWeights(int numberLayerInputs, int numberLayerOutputs) {

         double[] weightsArray = new double[(numberLayerInputs+1) * numberLayerOutputs];
         int counter = 0;

         while(counter< ((numberLayerInputs+1) * numberLayerOutputs)){
             if( (counter % numberLayerInputs) == 0 && counter != numberLayerInputs * numberLayerOutputs){
                weightsArray[counter] = 1;
             }else{
                weightsArray[counter] = 1.0 / (numberLayerInputs-1);
             }
             counter++;
         }
        return weightsArray;
    }
}