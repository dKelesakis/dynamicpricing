package org.deeplearning4j.examples.feedforward.regression;

public class Marketplace {

    //Marketplace object variables
    private int numberOfShops;
    private int numberOfProducts;
    private int numberOfCustomers;


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


    // Setters
    public void setNumberOfShops ( int newNumberOfShops){ this.numberOfShops = newNumberOfShops; }
    public void setNumberOfProducts ( int newNumberOfProducts){
        this.numberOfProducts = newNumberOfProducts;
    }
    public void setNumberOfCustomers ( int newNumberOfCustomers){ this.numberOfCustomers = newNumberOfCustomers; }

}

//trexw to nevroniko, ftiaxnw tis times toy eshop1
       /* Product[] productListTemp = shopList[0].getProductList();

        for (int j = 0; j < numberOfProducts; j++) {
          //  productListTemp[j].setPrice(eksodos nevronikoy);
        }
        shopList[0].setProductList(productListTemp);
*/


//vazw toys pelates na pane sta magazia na paroyn tyxaia proionta kai ypologizw ta kerdh
