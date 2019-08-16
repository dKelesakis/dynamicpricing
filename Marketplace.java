package org.deeplearning4j.examples.feedforward.regression;

import java.io.*;
import java.util.*;

public class Marketplace {

    //Marketplace object variables
    private int numberOfShops;
    private int numberOfProducts;
    private int numberOfCustomers;
    private List<Shop> shopList = new ArrayList<Shop>();
    private List<Customer> customerList = new ArrayList<Customer>();

    //Constructor
    Marketplace(int numberOfShops, int numberOfProducts, int numberOfCustomers) {
        this.numberOfShops = numberOfShops;
        this.numberOfProducts = numberOfProducts;
        this.numberOfCustomers = numberOfCustomers;
    }

    // Getters
    public int getNumberOfShops () {
        return numberOfShops;
    }
    public int getNumberOfProducts () {
        return numberOfProducts;
    }
    public int getNumberOfCustomers () {
        return numberOfCustomers;
    }
    public List<Shop> getShopList () {
        return shopList;
    }
    public List<Customer> getCustomerList () {
        return customerList;
    }

    // Setters
    public void setNumberOfShops ( int newNumberOfShops){ this.numberOfShops = newNumberOfShops; }
    public void setNumberOfProducts ( int newNumberOfProducts){ this.numberOfProducts = newNumberOfProducts; }
    public void setNumberOfCustomers ( int newNumberOfCustomers){ this.numberOfCustomers = newNumberOfCustomers; }


    public void createShops(int numberOfShops) {

        if(numberOfShops==10){
            shopList.add(new Shop(numberOfProducts, 5, 2.5, true, false, 5, 4, 0));
            shopList.add(new Shop(numberOfProducts, 5.5, 3, true, false, 4, 4, 0.03));
            shopList.add(new Shop(numberOfProducts, 6, 2, true, true, 4, 5, 0.03));
            shopList.add(new Shop(numberOfProducts, 4.5, 3.5, true, false, 2, 3, 0.005));
            shopList.add(new Shop(numberOfProducts, 5, 4, true, false, 5, 4, 0.005));
            shopList.add(new Shop(numberOfProducts, 5, 3, true, true, 5, 4, -0.05));
            shopList.add(new Shop(numberOfProducts, 5.5, 3, true, false, 4, 5, 0.05));
            shopList.add(new Shop(numberOfProducts, 4, 5, true, true, 3, 3, -0.05));
            shopList.add(new Shop(numberOfProducts, 5, 3, true, false, 5, 5, -0.05));
            shopList.add(new Shop(numberOfProducts, 5, 4, false, false, 5, 5, -0.03));
        }else{
            //create only the first shop
            shopList.add(new Shop(numberOfProducts, 5, 2.5, true, false, 5, 4, 0));
        }

    }


    public void createCustomers(int numberOfCustomers) {

        for (int i = 0; i < numberOfCustomers; i++) {

            Customer customer = new Customer(numberOfProducts);

            double wtpArray[] = new double[numberOfProducts];
            //kanw omoiomorfh katanomh wtp

            //create distribution for customer wtps
            Random rand = new Random();

            //ypologizw wtp toy customer gia to kathena
            for (int j = 0; j < numberOfProducts; j++) {

                //scannarw ta products
                double basePriceTemp = this.shopList.get(0).getProductList().get(j).getReferencePrice(); //pairnw ta base price apo ena tyxaio, einai idia gia ola ta eshop

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
            customerList.add(customer);

        }
    }

    public void readOrders() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader("data_orders.csv"));
            while ((line = br.readLine()) != null) {

                //read data
                String[] orderDetailsString = line.split(cvsSplitBy);
                String orderId = orderDetailsString[0];
                String datePurchased = orderDetailsString[1];
                String customerId = orderDetailsString[2];
                double orderTotal = Double.parseDouble(orderDetailsString[3]);
                String productId = orderDetailsString[4];


                //SOS check product existence in database SOS
//               if (checkProductIndex(productId) != -1) {


                    //check customer existence
                    int customerIndex = checkCustomerIndex(customerId);

                    if (customerIndex == -1) {      //case: customer doesn't exist, no orders
                        Customer newCustomer = new Customer(customerId);
                        Order newOrder = new Order(orderId, datePurchased, customerId, orderTotal, productId);
                        newCustomer.addOrder(newOrder);
                        customerList.add(newCustomer);

                    } else {
                        //check order existence
                        int orderIndex = checkOrderIndex(orderId, customerIndex);

                        if (orderIndex == -1) {     //case: customer exists, order doesn't exist
                            Order newOrder = new Order(orderId, datePurchased, customerId, orderTotal, productId);
                            customerList.get(customerIndex).addOrder(newOrder);
                        } else {                                                //case: customer exists, order exists, add product

                            customerList.get(customerIndex).getOrderList().get(orderIndex).addProduct(productId);
                        }

                    }

             //   }
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


        //sort orders chronologically
        for(int i=0; i<customerList.size();i++){
         customerList.get(i).sortOrders();
        }


        /*for(int i=0; i<customerList.size();i++){
            for(int j=0; j<customerList.get(i).getOrderList().size();j++){
                System.out.println("meta"+customerList.get(i).getOrderList().get(j).getDatePurchasedString());
            }
        }*/

    }

    public void readOrderViews() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader("orderViews.csv"));
            while ((line = br.readLine()) != null) {

                //read data
                String[] orderDetailsString = line.split(cvsSplitBy);
                String orderId = orderDetailsString[0];
                String datePurchased = orderDetailsString[1];
                String customerId = orderDetailsString[2];
                double orderTotal = Double.parseDouble(orderDetailsString[3]);
                String productId = orderDetailsString[4];
                double timeSpent = Double.parseDouble(orderDetailsString[5]);
                int pageViews = (int) Double.parseDouble(orderDetailsString[6]);


                if (checkProductIndex(productId) != -1 && (timeSpent + pageViews) > 0) {


                    //check customer existence
                    int customerIndex = checkCustomerIndex(customerId);

                    if (customerIndex == -1) {      //case: customer doesn't exist, no orders
                        Customer newCustomer = new Customer(customerId);
                        Order newOrder = new Order(orderId, datePurchased, customerId, orderTotal, productId, timeSpent, pageViews);
                        newCustomer.addOrder(newOrder);
                        customerList.add(newCustomer);

                    } else {
                        //check order existence
                        int orderIndex = checkOrderIndex(orderId, customerIndex);

                        if (orderIndex == -1) {     //case: customer exists, order doesn't exist
                            Order newOrder = new Order(orderId, datePurchased, customerId, orderTotal, productId, timeSpent, pageViews);
                            customerList.get(customerIndex).addOrder(newOrder);
                        } else {                                                //case: customer exists, order exists, add product

                            customerList.get(customerIndex).getOrderList().get(orderIndex).addProduct(productId);
                        }

                    }

                       }
                }
            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            } finally{
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        //sort orders chronologically
        for(int i=0; i<customerList.size();i++){
            customerList.get(i).sortOrders();
        }


        /*for(int i=0; i<customerList.size();i++){
            for(int j=0; j<customerList.get(i).getOrderList().size();j++){
                System.out.println("meta"+customerList.get(i).getOrderList().get(j).getDatePurchasedString());
            }
        }*/

    }


    public int checkCustomerIndex(String customerId) {

        if (customerList.size()==0){
            return -1;
        }

        for(int i=0;i<customerList.size();i++){
            if (customerId.equals(customerList.get(i).getCustomerId())){
                return i;
            }
        }
      //  System.out.println("de to vrhka ton pelath kai oyte htan h 1h seira dedomenon");
        return -1;
    }


    public int checkOrderIndex(String orderId, int customerIndex){

        if (customerList.get(customerIndex).getOrderList().size()==0){
            return -1;
        }

        for(int i=0;i<customerList.get(customerIndex).getOrderList().size();i++){
            if (orderId.equals(customerList.get(customerIndex).getOrderList().get(i).getOrderId())){
                return i;
            }
        }

        System.out.println("de to vrhka to order kai oyte htan h 1h seira dedomenwn");
        return -1;
    }


    public int checkProductIndex(String productId){    //to kanw opws ta epanw me equals gia ta string   //REPAIR OPWS TA EPANW

        for(int i=0;i<shopList.get(0).getProductList().size();i++){
            if (productId.equals(shopList.get(0).getProductList().get(i).getProductId())){
                return i;
            }
        }

      //  System.out.println("de to vrhka to product");
        return -1;
    }

    public void exportNN1DataCSV(){
        //create csv nevronikoy diktyoy
        try {
            PrintWriter pw = new PrintWriter(new File("input_NN1.csv"));


            for (int k = 0; k < numberOfShops; k++) {

                List<Product> productList = this.shopList.get(k).getProductList();

                for (int j = 0; j < numberOfProducts; j++) {

                    StringBuilder sb = new StringBuilder();
                    pw.write(sb.toString());

                    sb.append(productList.get(j).getBaseCost());
                    sb.append(',');
                    sb.append(productList.get(j).getBrandPower());
                    sb.append(',');

                    if (productList.get(j).getStockBool() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');


                    sb.append(this.shopList.get(k).getDeliveryCost());
                    sb.append(',');
                    sb.append(this.shopList.get(k).getDeliveryTime());
                    sb.append(',');


                    if (shopList.get(k).getDeliveryMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    if (this.shopList.get(k).getPaymentMethod() == true) {
                        sb.append(1);
                    } else {
                        sb.append(0);
                    }
                    sb.append(',');

                    sb.append(this.shopList.get(k).getSellerReviews());
                    sb.append(',');
                    sb.append(this.shopList.get(k).getSellerReputation());
                    sb.append(',');

                    sb.append(productList.get(j).getPrice());
                    sb.append('\n');


                    pw.write(sb.toString());
                }


            }
            pw.close();
            System.out.println("done!");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void exportNN2DataCSV(){
        //create csv nevronikoy diktyoy
        try {
            PrintWriter pw = new PrintWriter(new File("input_NN2.csv"));

            int counter = 0;

            for (int i = 0; i < customerList.size(); i++) {

                for (int j = 0; j < customerList.get(i).getOrderList().size(); j++) {

                    List<String> productIdsList = customerList.get(i).getOrderList().get(j).getProductIds();

                    for (int k = 0; k < productIdsList.size(); k++) {

                        double neuralPrice = shopList.get(0).getNeuralPriceByProductId(productIdsList.get(k));  //thewrw oti to shop 0 mono xrhsimopoiei dynamic pricing sto 2o peirama
                        double recency = customerList.get(i).calculateRecency(j);
                        double frequency = customerList.get(i).calculateFrequency(j);
                        double monetary = customerList.get(i).calculateMonetary(j);
                        double pageViews = customerList.get(i).getOrderList().get(j).getPageViews();
                        double timeSpent = customerList.get(i).getOrderList().get(j).getTimeSpent();
                        double personalPrice = shopList.get(0).getPriceByProductId(productIdsList.get(k));

                        if( pageViews + timeSpent > 0 ){
                            StringBuilder sb = new StringBuilder();
                            pw.write(sb.toString());

                            sb.append(neuralPrice);
                            sb.append(',');
                            sb.append(recency);
                            sb.append(',');
                            sb.append(frequency);
                            sb.append(',');
                            sb.append(monetary);
                            sb.append(',');
                            sb.append(pageViews);
                            sb.append(',');
                            sb.append(timeSpent);
                            sb.append(',');
                            sb.append(personalPrice);
                            sb.append('\n');

                            pw.write(sb.toString());
                        }

                    }

                }

            }
            pw.close();
            System.out.println("done!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void dataNormaliserCSV(int numberOfColumns, int NNid){
        double[] minValues = new double[numberOfColumns];
        double[] maxValues = new double[numberOfColumns];
        double[] sumOfValues = new double[numberOfColumns];
        double[] meanValues = new double[numberOfColumns];
        double[] range = new double[numberOfColumns];

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";    // use comma as separator

        try {

            if(NNid == 1){
                br = new BufferedReader(new FileReader("input_NN1.csv"));
            }else if(NNid ==2){
                br = new BufferedReader(new FileReader("input_NN2.csv"));
            }


            int counter=0;

            while ((line = br.readLine()) != null) {

                //read data
                String[] dataString = line.split(cvsSplitBy);
                double[] dataDouble = Arrays.stream(dataString).mapToDouble(Double::parseDouble).toArray();


                counter++;
                if(counter==1){              //if it is the first line of the file
                    for(int i=0;i<numberOfColumns;i++) {
                        minValues[i] = dataDouble[i];
                        maxValues[i] = dataDouble[i];
                        sumOfValues[i] = dataDouble[i];
                    }

                }else{                        //for the rest of the lines
                    for(int i=0;i<numberOfColumns;i++) {
                        if( dataDouble[i] < minValues[i] ){
                            minValues[i] = dataDouble[i];
                        }

                        if( dataDouble[i] > maxValues[i] ){
                            maxValues[i] = dataDouble[i];
                        }

                        sumOfValues[i] += dataDouble[i];

                    }
                }
            }


            //calculate mean and range of each variable
            for(int i=0;i<numberOfColumns;i++) {  //currently not normalise base cost,price
                meanValues[i] = sumOfValues[i] / counter;  //prosoxh mhn exw terastio arithmo ektos orion
                range[i] = maxValues[i] - minValues[i];
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


        BufferedReader br1 = null;
        String line1 = "";
        String cvsSplitBy1 = ",";
        PrintWriter pw1 = null;

            try {

                if(NNid == 1){
                    pw1 = new PrintWriter(new File("inputNormalised_NN1.csv"));
                }else if(NNid ==2){
                    pw1 = new PrintWriter(new File("inputNormalised_NN2.csv"));
                }

                try {

                    if(NNid == 1){
                        br1 = new BufferedReader(new FileReader("input_NN1.csv"));
                    }else if(NNid ==2){
                        br1 = new BufferedReader(new FileReader("input_NN2.csv"));
                    }

                    while ((line = br1.readLine()) != null) {

                        //read data
                        String[] dataString = line.split(cvsSplitBy);
                        double[] dataDouble = Arrays.stream(dataString).mapToDouble(Double::parseDouble).toArray();

                        //normalize data
                        for(int i=1;i<numberOfColumns-1;i++) {  //currently not normalise base cost,price   //int i=0;i<numberOfColumns;i++
                            dataDouble[i] = (dataDouble[i] - meanValues[i]) / range[i];
                        }


                        //write data
                        StringBuilder sb = new StringBuilder();
                        pw1.write(sb.toString());

                        for(int i=0;i<numberOfColumns-1;i++) {
                            sb.append(dataDouble[i]);
                            sb.append(',');
                        }
                        sb.append(dataDouble[numberOfColumns-1]);
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

    public void readViews() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader("data_views.csv"));
            while ((line = br.readLine()) != null) {

                //read data
                String[] orderDetailsString = line.split(cvsSplitBy);
                String customerId = orderDetailsString[0];
                String dateViewed = orderDetailsString[1];


                //check customer existence
                int customerIndex = checkCustomerIndex(customerId);

                if(customerIndex == -1){      //case: customer doesn't exist, no orders
                    //do nothing
                }else{                         //case: customer exists
                    boolean sorted = customerList.get(customerIndex).sortView(dateViewed);
                    if(sorted == false){
                        System.out.println("taksinomhsh view ektos orion - kapoio sfalma yparxei");
                    }


                }

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

        //after reading all timestamps from data_views file estimate total time of sessions before every order
        for(int i=0; i<customerList.size();i++){
            for(int j=0; j<customerList.get(i).getOrderList().size();j++){
                customerList.get(i).getOrderList().get(j).calculateTimeSpent();
               // System.out.println(customerList.get(i).getOrderList().get(j).getPageViews());
               // System.out.println(customerList.get(i).getOrderList().get(j).getTimeSpent());
            }
        }

        try {
            PrintWriter pw = new PrintWriter(new File("orderViews.csv"));


            for(int i=0; i<customerList.size();i++) {
                for (int j = 0; j < customerList.get(i).getOrderList().size(); j++) {
                    for (int k = 0; k < customerList.get(i).getOrderList().get(j).getProductIds().size(); k++) {

                        StringBuilder sb = new StringBuilder();
                        pw.write(sb.toString());

                        sb.append(customerList.get(i).getOrderList().get(j).getOrderId());
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getDatePurchasedString());
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getCustomerId());
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getOrderTotal());
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getProductIds().get(k));
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getPageViews());
                        sb.append(',');
                        sb.append(customerList.get(i).getOrderList().get(j).getTimeSpent());
                        sb.append('\n');

                        pw.write(sb.toString());

                    }
                }
            }

            pw.close();
            System.out.println("done!");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void setNeuralPrices(){

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";    // use comma as separator

        int counter=0;

        try {
            br = new BufferedReader(new FileReader("outputDataNN1.csv"));
            while ((line = br.readLine()) != null) {

            //read data
            String[] dataString = line.split(cvsSplitBy);
            double[] dataDouble = Arrays.stream(dataString).mapToDouble(Double::parseDouble).toArray();
            double neuralPrice = dataDouble[3];    //the output of the NN1
            this.getShopList().get(counter/numberOfProducts).getProductList().get(counter%numberOfProducts).setNN1Price(neuralPrice);

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


    }

}


//vazw toys pelates na pane sta magazia na paroyn tyxaia proionta kai ypologizw ta kerdh
