package Projects.Proj4.Banking;
/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * CreditCard
 */
public class CreditCard extends BankCard{

    private double limit; //card limit
    public int creditScore;
    private int lifetimePayments;//lifetime payments

    public CreditCard(Bank account){
        this.account = account;
        this.limit = account.getBal();
        this.creditScore = 350;
        lifetimePayments = 0;
    }
    public CreditCard(Bank account, int creditScore){
        this(account);
        this.creditScore = creditScore;
        if(creditScore > 499)
            changeLimit();
    }
    
    public int add(double amount){ //pays balance owed
        if (account.getBal()+amount > limit) {
            return -1; //System.out.println("Amount would exceed money owed.");
        } else {
            account.addMoney(amount);
            lifetimePayments++;
            if (lifetimePayments%5 == 0 && creditScore+20 <= 850)//every 5 payments score goes up by 20
                creditScore += 20;

            return 0;
        }
    }

    public int changeLimit(){ //petition to change limit if credit score is > 500 (returns 0 upon sucess and less than upon error)
        if (creditScore > 500){
            double returnVal = account.raiseLimit(creditScore, limit);
            if(returnVal > 0){
                double oldlimit = limit;
                limit = returnVal;
                account.addMoney(limit-oldlimit); 
                return 0;
            }else{
                return (int) returnVal; //an error either (-1,-2)
            }
        }else
            return -3; //System.out.println("Not possible. Raise your credit score.");
    }

    public int getScore(){ //returns credit score
        return creditScore;
    }

    public double getLimit(){ //returns credit limit
        return limit;
    }

}