package org.deeplearning4j.examples.feedforward.regression;

public class Customer extends Marketplace {

    //Constructor
    Customer() {

        super(numberOfCustomers, numberOfProducts, numberOfShops);
        double[] wtpArray = new double[numberOfProducts];

    }

    Customer(double[] wtpArray) {

        super(numberOfCustomers, numberOfProducts, numberOfShops);
        this.wtpArray = wtpArray;

    }

    //variables for 1st NN

    private double wtpArray[] = new double[numberOfProducts]; // private = restricted access

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
