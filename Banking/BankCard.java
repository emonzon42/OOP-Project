package Projects.Proj4.Banking;
/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * Card
 */
public abstract class BankCard implements PaymentType{// main way to interact with bank account

    protected Bank account;
    
    public int charge(double amount){ //charges card
        if (account.getBal() - amount <= 0) {
            return -1; //declined
        } else {
            account.chargeAcc(amount);
            return 0; //success
        }
    }

    public double getBal(){ //returns balance of account
        return account.getBal();
    }

    public int add(double amount){ //adds money to account (returns 0 upon success)
        account.addMoney(amount);
        return 0;
    }
    
}