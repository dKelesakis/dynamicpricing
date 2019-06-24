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
    public void setNumberOfInputNodes ( int newNumberOfInputNodes){ this.numberOfInputNodes = newNumberOfInputNodes; }
    public void setNumberOfHiddenNodes ( int newNumberOfHiddenNodes){ this.numberOfHiddenNodes = newNumberOfHiddenNodes; }
    public void setNumberOfOutputNodes ( int newNumberOfOutputNodes){ this.numberOfOutputNodes = newNumberOfOutputNodes; }

    //Random number generator seed, for reproducability
    public static final int seed = 12345;
    //Number of epochs (full passes of the data)
    public static final int nEpochs = 10000; //1000//10000//50000//1000  //200
    //Number of data points
    public static final int nSamples = 1000; //1000
    //Batch size: i.e., each epoch has nSamples/batchSize parameter updates
    public static final int batchSize = 1000; //1000
    //Network learning rate
    public static final double learningRate = 0.0001; //0.001//0.01
    // The range of the sample data, data in range (0-1 is sensitive for NN, you can try other ranges and see how it effects the results
    // also try changing the range along with changing the activation function


    public static final Random rng = new Random(seed);

    public void trainAndEvaluateNetwork()throws IOException, InterruptedException {

        //Load the training data:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File("experimentData.csv")));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, 0, 1);

        //Load the test/evaluation data:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File("experimentData.csv"))); //experimentData1
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 1);


        //MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .updater(new Nesterovs(learningRate, 0.9))
            .list()
            .layer(0, new DenseLayer.Builder().nIn(numberOfInputNodes).nOut(numberOfHiddenNodes)
                .weightInit(WeightInit.XAVIER) //XAVIER
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


        INDArray paramsLayer0 = Nd4j.create(new double[]{2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11,2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11,2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11,2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11,2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11,2,0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11, 0.11, 0.11, 0.11,0.11, 0.11, 0.11  }, new int[]{1, 60});
        INDArray paramsLayer1 = Nd4j.create(new double[]{0.17, 0.17, 0.17, 0.17, 0.17, 0.17, 0.17}, new int[]{1, 7});
        System.out.println("hi"+paramsLayer0);
        System.out.println("hi"+paramsLayer1);

        model.getLayer(0).setParams(paramsLayer0);
        model.getLayer(1).setParams(paramsLayer1);
        //INDArray parameters =  Nd4j.create(float[][]);
        //model.getLayer(0).setParams();
        //model.getLayer(1).getParam("W");

        System.out.println(model.getLayer(0).getParam("W"));
        System.out.println(model.getLayer(1).getParam("W"));
            //(0).getParam("W")


       /* Map<String, INDArray> paramTable = model.paramTable();
        Set<String> keys = paramTable.keySet();
        Iterator<String> it = keys.iterator();
*/
        /*while (it.hasNext()) {
            String key = it.next();
            INDArray values = paramTable.get(key);
            System.out.print(key+" ");//print keys
            System.out.println(Arrays.toString(values.shape()));//print shape of INDArray
            System.out.println(values);
            model.setParam(key, Nd4j.//set some random values
        }*/


        //Train the network on the full data set, and evaluate in periodically
        for ( int n = 0; n < nEpochs; n++) {
            model.fit( iterator );
        }


        RegressionEvaluation eval = model.evaluateRegression(testIter);
        //RegressionEvaluation eval =  new RegressionEvaluation(1);

        //Print the evaluation statistics
        System.out.println(eval.stats());


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
                    INDArray out = model.output(input, false);
                    //System.out.println(out);

                    double outputPriceNN = out.getDouble(0, 0);
                    double outputPriceNNAfterCorrection = outputPriceNN;
                     //System.out.println(outputPriceNN);

                    double productCost = productdetails[0];
                    // System.out.println(error/numberOfProducts);    //σοσ το εδιτ
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
}
