package org.deeplearning4j.examples.feedforward.regression;

public class Customer {

    //variables for 1st NN
    private double wtpArray[]; // private = restricted access


    //Constructors
    Customer(int numberOfProducts) {
        double[] wtpArray = new double[numberOfProducts];
    }

    Customer(double[] wtpArray) {
        this.wtpArray = wtpArray;
    }


    // Getter
    public double[] getWtp() { return wtpArray; }


    // Setter
    public void setWtp(double[] newWtp) {
        this.wtpArray = newWtp;
    }


    //variables for 2nd NN
    private double recency;
    private double frequency;
    private double monetary;
    private double productViewsEngagement;
    private double timeSpentEngagement;
}
