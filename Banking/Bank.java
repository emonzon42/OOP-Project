package Projects.Proj4.Banking;
/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * Bank
 */
public class Bank {//Bank class represents a bank account

    public enum AccountType{Checking, Credit;}

    private AccountType type;
    private String BankName;
    private double balance; //total $$ on account (owed balance if credit account)
    

    public Bank(String name, AccountType type){
        this.BankName = name;
        this.type = type;

        if (type == AccountType.Credit) {
            this.balance = 10000; //default limit for new credit accs
        } else {
            this.balance = 0; //default starting balance if no initial deposit is made
        }
    }

    public Bank(String name, AccountType type, int initalDep){
        this(name, type);
        if (type == AccountType.Checking) {
            this.balance = initalDep;
        }
    }

    public double raiseLimit(int creditScore, double limit){ //petition to raise credit limit (returns 0 or less if failed)
        if(type != AccountType.Credit){
            return -1; //Cannot be done. Must be a credit account.
        }else if (creditScore > 850 || creditScore < 350) {
            return -2; //Invalid credit score.
        } else {
            if (creditScore < 650) {
                limit = creditScore + (int)(creditScore*creditScore)/20;
            } else if (creditScore < 750){
                limit = creditScore + (int)(creditScore*creditScore)/10;
            }else if (creditScore < 820){
                limit = creditScore + (int)(creditScore*creditScore)/5;
            }else {
                limit = creditScore + (int)(creditScore*creditScore)/3;
            }
            return limit; //System.out.println("Done. Your new limit is " + limit + ".");
        }
    }

    public BankCard bankCard(Bank account){//returns either a debit card or a credit card depending on account type
        switch (type) {
            case Checking:
                return new DebitCard(account);
            case Credit:
                return new CreditCard(account);
            default:
                return new DebitCard(account);
        }
    }

    public String getBankName(){
        return this.BankName;
    }

    public double getBal(){
        return this.balance;
    }

    public void addMoney(double amount){
        this.balance += amount;
    }

    public void chargeAcc(double amount){
        this.balance -= amount;
    }

}