package Projects.Proj4.Banking;
/**
 * Eli Monzon
 * 4.26.20
 * ICSI 311
 * Cash
 */
public class Cash implements PaymentType{

    private double balance;

    public Cash(){
        this.balance = 0;
    }

    public Cash(double amount){
        this.balance = amount;
    }

    public int charge(double amount){ //charges card
        if (balance - amount < 0) {
            return -1; //System.out.println("Not enough money.");
        } else {
            balance -= amount;
            return 0; //success
        }
    }

    public double getBal(){ //returns balance of account
        return this.balance;
    }

    public int add(double amount){ //adds money to account (returns 0 upon success)
        balance += amount;
        return 0;
    }

}