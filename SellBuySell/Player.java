package Projects.Proj4.SellBuySell;

/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * Client
 */

import java.util.*;
import Projects.Proj4.Banking.*;
import Projects.Proj4.Banking.Bank.AccountType;


public class Player {
    
    public DebitCard debitCard;
    public CreditCard creditCard;
    public Cash cash;
    private LinkedList<Product<ProductType>> inventory;

    public Player(){
        Bank checkAcc = new Bank("Chase", AccountType.Checking);
        Bank credAcc = new Bank("Discover", AccountType.Credit);

        cash = new Cash(generateCash());
        debitCard = (DebitCard) checkAcc.bankCard(checkAcc);
        creditCard = (CreditCard) credAcc.bankCard(credAcc);
        inventory = new LinkedList<>();

        //sets player up to start with one item of each type in inventory
        addToInven(new Product<ProductType>(ProductType.Art, 28000));
        addToInven(new Product<ProductType>(ProductType.HeavyUtility, 90000));
        addToInven(new Product<ProductType>(ProductType.Medicine, 30000));
        addToInven(new Product<ProductType>(ProductType.Weaponry, 37000));
    }

    public void removeFrmInven(Product<ProductType> product){ //removes from inventory
        inventory.remove(product);
    }

    public void addToInven(Product<ProductType> product){ //adds to inventory
        inventory.add(product);
    }

    public LinkedList<Product<ProductType>> getInventory(){ //player inventory
        return this.inventory;
    }

    public double totalBal(){//total balance
        return cash.getBal() + debitCard.getBal() + creditCard.getBal();
    }

    private double generateCash(){ //generates a random num of cash
        Random rand = new Random();
        int randNum = 0;
        while (randNum < 600000 || randNum > 1300000) {
            randNum = rand.nextInt(1500000);
        }
        return randNum;
    }

}