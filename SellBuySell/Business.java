package Projects.Proj4.SellBuySell;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * Business
 */
import java.util.*;
import Projects.Proj4.Banking.*;
import Projects.Proj4.Banking.Bank.AccountType;
public class Business {

    
    public double bestOffer[]; //array to hold best offer for product [0] holds original offer [1] holds changed
    public String name;
    public Product<ProductType> wanted;
    public ProductType type;
    private DebitCard companyFunds;

    public Business(){//constructor
        try {
            generateName();
        } catch (FileNotFoundException e) {
            System.out.println("Please make sure 'business.txt' is in local directory and try again. Game ending.");
            System.exit(0);
        }
        companyFunds = new DebitCard(new Bank("GoldmanSachs", AccountType.Checking, 30000000));
        type = wantedType();
        bestOffer = new double[2];
    }

    public int makeOffer(LinkedList<Product<ProductType>> products){//makes inital offer
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getType() == type) {
                bestOffer[0] = products.get(i).getPrice() + (1/5)*(products.get(i).getPrice());
                bestOffer[1] = bestOffer[0];
                wanted = products.get(i);
                return 0;
            }
        }

        return -1; //doesn't sell specified product
    }
    //todo: makes offer 0.
    public int renegotiate(double counterOffer, int numOfDeclines){//reconsiders bestOffer by testing against counterOffer
        Random rand = new Random();
        double difference;
        
        if (counterOffer < bestOffer[1]){//user asks for less money
            bestOffer[1] = counterOffer;
        }else if (numOfDeclines > 30 || bestOffer[1] > 3*bestOffer[0]){ //to avoid going too high / prevent upsale from going on too lono
            return -1; //can't reconsider
        }   else if (counterOffer > bestOffer[1]-bestOffer[1]*.1 
                    && counterOffer < bestOffer[1]+bestOffer[1]*.1){ //counter within 10% range of best then accept it
            bestOffer[1] = counterOffer;
        } else if (numOfDeclines < 6 && counterOffer > (1.5*bestOffer[0])) {//counter offer is way too high so
            difference = (1.5*bestOffer[0]) - bestOffer[1];
            bestOffer[1] = bestOffer[1]+difference/2f;
        } else if (numOfDeclines < 4) { //guarentees user can raise if they put a reasonable enough number
            difference = Math.abs(counterOffer-bestOffer[1]);
            bestOffer[1] = bestOffer[1]+difference/2f;
        } else { //a random number within a range close too bestOffer bc user too greedy
            bestOffer[1] = rand.nextInt((int)((((1.25*bestOffer[0]) - (.75*bestOffer[0])) + 1))) + .75*bestOffer[0];
        }
        return 0;
    }

    public int buyProduct(double price){//mimics how the business would buy the product
        return companyFunds.charge(price);
    }

    private void generateName() throws FileNotFoundException{//generates a random name for business using txt file
        File names = new File("SellBuySell/business.txt");
        Scanner reader = new Scanner(names);
        Random rand = new Random();
        int lineNum = rand.nextInt(40) + 1;
        int currentLine = 1;
        while (reader.hasNextLine()) {
            if (currentLine == lineNum){
                name = reader.nextLine();
                break;
            }
            reader.nextLine();
            currentLine++;
        }
        reader.close();
    }

    private ProductType wantedType(){// returns the wanted product type
        Random rand = new Random();
        int randnum = rand.nextInt(101);
        randnum %= 5;

        switch (randnum) {
            case 1:
                return ProductType.Art;
            case 2:       
                return ProductType.HeavyUtility;
            case 3:               
                return ProductType.Medicine;
            default:
                return ProductType.Weaponry;
        }
    }
    
}