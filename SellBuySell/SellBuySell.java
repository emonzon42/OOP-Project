package Projects.Proj4.SellBuySell;

/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * SellBuySell! (a game by Eli)
 */
import java.util.*;

import Projects.Proj4.Banking.*;

public class SellBuySell{

    private Player user;//player object
    private Scanner input;
    private LinkedList<Product<ProductType>> shop;

    public SellBuySell(){//constructor
        System.out.println();
        new Business(); //to make sure business.txt is in directory
        user = new Player();
        input = new Scanner(System.in);
        buildShop();
    }

    public boolean run(){ //reports back to game loop
        
        boolean stillPlaying = mainMenu();
        //input.close();
        return stillPlaying;
    }

    private boolean mainMenu(){//main user input menu
        final String COMMANDS = "\nAccepted commands: " 
                                +"\n S - Sets up a meeting with you and a business to negotiate a sale."
                                +"\n B - Shows a list of products that can be purchased for inventory"
                                +"\n D - Prompts you to pay a card / deposit money into a card"
                                +"\n C - Credit Card Options"
                                +"\n M - Shows all your money "
                                +"\n I - Shows current inventory"
                                +"\n H - How To Play"
                                +"\n Q - Quits the game "
                                +"\n L - List of commands\n";

        final String HOW_TO_PLAY = "\nThe point of the game is in the name, you sell products and you buy products, it's that"
                                  +"\nsimple! Buy products and resell them at higher values to earn profit! Become the multi-"
                                  +"\nbillionare you've always wanted to be! Sell! Buy! Sell! Meet with a business, negotiate"
                                  +"\na price, then SELL. Use that money, goto product store, then BUY to earn more profit!"
                                  +"\nDon't have the funds to buy right now? You also have the ability to pay with credit!"
                                  +"\nBuy with the credit card, profit, then pay the credit card back to get a raise on your"
                                  +"\ncredit score! Higher credit scores get higher credit limits! Now what are you waiting"
                                  +"\nfor, GO PROFIT!\n";

        System.out.println("(hint type \"L\" for a list of commands.)");
        System.out.print("What would you like to do?: ");
        String userIn = input.nextLine();

        if (userIn.equalsIgnoreCase("L")) {
            System.out.println(COMMANDS);
            mainMenu();
        }else if (userIn.equalsIgnoreCase("H")) {
            System.out.println(HOW_TO_PLAY);
            mainMenu();
        }else if (userIn.equalsIgnoreCase("S")) {
            sell();
        }else if (userIn.equalsIgnoreCase("B")) {
            buy();
        }else if (userIn.equalsIgnoreCase("D")) {
            deposit();
        }else if (userIn.equalsIgnoreCase("M")) {
            balance();
        }else if (userIn.equalsIgnoreCase("C")) {
            credit();
        }else if (userIn.equalsIgnoreCase("I")) {
            showInventory();
        }else if (userIn.equalsIgnoreCase("Q")) {
            return false;
        }else{
            System.out.println("Invalid input. Try again.");
            mainMenu();
        }
        return true;
    }

    private void sell(){ //prompts user to sell
        //todo: make buyer be able to randomly select a product that client owns and start their asking price at 20% more than user buy price
        //todo: select business name randomly from a text file
        Business buyer = new Business(); //generates new buyer
        //todo: consider adding all in-game dialog to a text file and have it "dynamically" print to the screen letter by letter

        System.out.println("\n???: Hello! I am " + buyer.name + " and I am looking for some " + buyer.type + ".");
        System.out.println(buyer.name + ": Would you like to do business with me?         (hint: 'no' will cancel)");
        System.out.print("You: ");
        if (input.nextLine().equalsIgnoreCase("No")) { 
            System.out.println(buyer.name + ": Sorry to hear that. Have a good day.\n");
            return;
        } else if (buyer.makeOffer(user.getInventory()) == -1) {
            System.out.println(buyer.name + ": Seems you do not have what I am searching for. Good day.");
            sell(); // starts method over with new buyer
            return;
        } else {//buyer made a offer

            //government offers 3 times what you paid for your weaponry (1/160 chance of this happening)
            if (buyer.name.equalsIgnoreCase("the government") && buyer.type == ProductType.Weaponry) {
                buyer.bestOffer[0] *= 3;
                buyer.bestOffer[1] *= 3;
            }
            int numOfDeclines = 0;
            boolean notAccepted = true;
            while (notAccepted) {//input loop
                String userInput;
                System.out.printf("%s: I am offering $%,.2f for some of your %s\n", buyer.name, buyer.bestOffer[1], buyer.type);
                System.out.println("Accept offer? (yes/no)");
                System.out.print("You: ");
                userInput = input.nextLine();
                if (userInput.equalsIgnoreCase("yes")) {
                    notAccepted = false;
                } else if (userInput.equalsIgnoreCase("no")) {
                    System.out.println("Would you like to renegotiate the offer? (hint: anything other than 'no' will continue)");
                    System.out.print("You: ");
                    userInput = input.nextLine();
                    if (!userInput.equalsIgnoreCase("no")) {
                        boolean incorrect = true; //see loop below
                        double counter = 0f;// users counter offer

                        while (incorrect) { //to ensure user doesnt input anything non numerical
                            System.out.print("You: I propose a counter-offer of $");
                            userInput = input.nextLine();
                            try {
                                counter = Double.parseDouble(userInput);
                                incorrect = false;
                            } catch (NumberFormatException  e) {
                                System.out.println("Enter a number. (ex: 12345.67)");
                            } catch (NullPointerException e) {
                                System.out.println("Enter a number. (ex: 12345.67)");
                            }
                        }
                        numOfDeclines++;
                        if (buyer.renegotiate(counter, numOfDeclines) == -1)
                            System.out.println("Sorry I won't go any higher than that.");
                        
                    } else {
                        System.out.println(); //blank line to make reading easier
                        return;
                    }
                }
            }
            buyer.buyProduct(buyer.bestOffer[1]); // charges buyer
            user.cash.add(buyer.bestOffer[1]); // sends money to user
            user.removeFrmInven(buyer.wanted); // removes product from inventory

            System.out.println(buyer.name + ": Pleasure doing buisness with you.");
            System.out.println(); //blank line to make reading easier
        }

    }

    private void buy(){ //prompts user to buy 
        //todo: make a hashtable of purchaseable inventory and have the user select and buy
        System.out.println();
        System.out.printf("| %2s   %-19s        %13s |\n","#","Product Type","Purchase Price");
        System.out.printf("| %2s   %-19s        %14s |\n"," "," "," ");
        for (int i = 0; i < shop.size(); i++) {
            Product<?> current = shop.get(i);
            if (i < 21) {
                System.out.printf("| %2d   %-19s       $ %,13.2f |\n",i+1,current.getType(),current.getPrice());
            }else{
                System.out.printf("| %2d   %-12s (High Class) $ %,13.2f |\n",i+1, current.getType(),current.getPrice());
            }
        }
        System.out.println();
        System.out.println("Owner: Welcome to the Product Shop! Please take a look at what I have in stock.");
        System.out.println("       Once you find what you are looking for enter the item number. I accept debit,");
        System.out.println("       credit, cash, and foodstamps. :)");

        boolean incorrect = true;
        int itemNum = 0;
        while (incorrect) {//input loop
            System.out.println("\n(hint: type 'cancel' to cancel)");
            System.out.print("You: ");
            String userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("cancel")) {
                System.out.println("See you soon!");
                System.out.println();
                return;
            }

            try {
                itemNum = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid. Enter a number. (1-25)");
                continue;
            }

            itemNum--; //turns (1-25) number back into index (0-24)
            if (itemNum < 0 || itemNum > 24) {
                System.out.println("Please enter a number between 1-25.");
                continue;
            }
            System.out.printf("Owner: Great! So you would like %s for $%,.2f? (YES/NO)\n",shop.get(itemNum).getType(), shop.get(itemNum).getPrice());

            System.out.print("You: ");
            userInput = input.nextLine();

            if (userInput.equalsIgnoreCase("no")) {
                System.out.println("Owner: What would you like?");
                continue;
            } else {
                System.out.println("Owner: How will you pay?  (1: debit, 2: credit, 3: cash, or 'cancel')");
                boolean formatErr = true;
                int method = 0;
                while (formatErr) {
                    System.out.print("You: ");
                    userInput = input.nextLine();
//todo: add ability to use both cards at once 
                    if (userInput.equalsIgnoreCase("cancel")) {
                        formatErr = false;
                        System.out.println("Owner: See you soon!");
                        System.out.println();
                        return;
                    }

                    try {
                        method = Integer.parseInt(userInput);
                        if (method == 1 || method == 2 || method == 3) {
                            formatErr = false;
                        } else {
                            System.out.println("Invalid. Enter 1 for debit, 2 for credit, 3 for cash.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid. Enter 1 for debit, 2 for credit, 3 for cash.");
                    }
                    int returnVal = payType(method).charge(shop.get(itemNum).getPrice());
                    if (method == 3 && returnVal != 0) {
                        System.out.println("Owner: Seems you don't have sufficient cash for that.");
                        continue;
                    }else if (returnVal != 0) {
                        System.out.println("Owner: Seems that card declined. Try a different one.");
                        continue;
                    }
                }
                
                incorrect = false;
            }
        }
        System.out.println("Owner: All Done!");
        user.addToInven(shop.get(itemNum)); //adds item to user inventory

        ProductType type = shop.get(itemNum).getType();//captures type to restock with similar item
        int price = (int) shop.get(itemNum).getPrice(); //captures price
        Random rand = new Random();

        shop.remove(itemNum); //removes item that user bought
        shop.add(itemNum, new Product<>(type, rand.nextInt((int)((price+(price*.1)) - (price-(price*.1))) + 1 ) + price-(price*.1))); //restocks with similar item
        
        System.out.println("Owner: Thank you, I hope you come again soon.");
        System.out.println();
    }

    private void deposit(){ //prompts user to deposit
        boolean invalidInput = true;
        String input1 = "", input2 = ""; //user inputs
        double depositAmt = 0;
        balance();

        while (invalidInput) {
            System.out.print("(hint type 'cancel' to cancel)\n");
            System.out.print("Where would you like to deposit? (credit/checking): ");
            input1 = input.nextLine();
            if(input1.compareToIgnoreCase("cancel") == 0){
                System.out.println("Okay cancelling.");
                System.out.println();
                return;
            }else if (input1.compareToIgnoreCase("credit") != 0 && input1.compareToIgnoreCase("checking") != 0) {
                System.out.println("Invalid input. Only enter credit/checking");
                continue;
            }
            System.out.print("How much will you deposit?: ");
            input2 = input.nextLine();
            try {
                depositAmt = Double.parseDouble(input2);
                if (depositAmt > user.cash.getBal() ) {
                    System.out.println("Not enough money try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid. Please enter a valid number.");
                continue;
            }

            if (input1.compareToIgnoreCase("checking") == 0) {
                user.cash.charge(depositAmt);
                user.debitCard.add(depositAmt);
            } else {
                user.cash.charge(depositAmt);
                if (user.creditCard.add(depositAmt) == -1) {
                    System.out.println("Amount would exceed the limit.");
                    continue;
                }
            }
            invalidInput = false;
        }

        System.out.println("Ok! Finished.");
        balance();
    }

    private double balance(){ //reports current balance to user
        final double totalBal = user.totalBal();

        System.out.printf("\nTotal checking balance: %,.2f\n", user.debitCard.getBal());
        System.out.printf("Remaining credit balance: %,.2f (limit: %,.2f)\n", user.creditCard.getBal(), user.creditCard.getLimit());
        System.out.printf("Total cash on hand: %,.2f\n", user.cash.getBal());
        System.out.printf("Overall spending money: %,.2f\n\n", totalBal);

        return totalBal;
    }

    private void showInventory(){ //prints inventory
        LinkedList<Product<ProductType>> inventory = user.getInventory();
        System.out.println();
        System.out.printf("| %-18s                %13s |\n","Product Type","Purchase Price");
        System.out.printf("| %-18s                %14s |\n"," "," ");
        for (int i = 0; i < inventory.size(); i++) {
            Product<?> current = inventory.get(i);
        System.out.printf("| %-18s                $%,13.2f |\n",current.getType(),current.getPrice());
        }
        System.out.println();
    }

    private void credit(){//gives player options on their credit
        System.out.println();
        System.out.println("1: Check credit account status\n2: Check credit score\n3: Petition to the bank for a credit limit raise\n");

        boolean inputting = true;
        while (inputting) { //input loop
            System.out.println("(hint: type 'cancel' to cancel at any time)");
            String userInput;
            System.out.print("Enter an option: ");
            userInput = input.nextLine();
            
            if (userInput.equalsIgnoreCase("cancel")) {
                break;
            }else if (userInput.equalsIgnoreCase("1")) {//credit account status

                System.out.printf("\nCurrent balance: $%,.2f\n", user.creditCard.getLimit()-user.creditCard.getBal());
                System.out.printf("Available credit: $%,.2f of $%,.2f)\n", user.creditCard.getBal(), user.creditCard.getLimit());

            } else if (userInput.equalsIgnoreCase("2")){//credit score

                System.out.println("(hint: pay your credit card balance and improve your score!)");
                System.out.println("Your FICO Credit Score is "+ user.creditCard.getScore());
                
            }   else if (userInput.equalsIgnoreCase("3")){//credit limit raise

                user.creditCard.changeLimit(); //rv is the return value from the func changeLimit

                switch (user.creditCard.changeLimit()) {
                    case 0:// success
                        System.out.println("Okay! Your new credit limit is "+ (int)user.creditCard.getLimit());
                        break;
                    case -3: //low credit score
                        System.out.println("Not possible at this time. Please raise your credit score.");
                        break;
                    default: //-1 or -2 should be impossible in this case bc limit is always 350<x<850 and acc is always credit
                        System.out.println("An error has occurred :(");
                        break;
                }
                

            }else{
                System.out.println("Invalid input. Enter a num 1/2/3");
                System.out.println();
                continue;
            }
            System.out.println();//for reading
        }
        System.out.println();//for reading
    }

    private void buildShop(){ //builds the shop LinkedList
        shop = new LinkedList<>();
        Random rand = new Random();
        
        Product<ProductType> current;
        for (int i = 0; i < 25; i++) { //adds different products to shop with price being a random num from ((max - min) +1) + min
            double price;
            if (i<6) {//Artwork
                price = rand.nextInt((80000 - 5000) + 1) + 5000;
                current = new Product<>(ProductType.Art, price);
            }else if(i < 11){//Weaponry
                price = rand.nextInt((160000 - 30000) + 1) + 30000;
                current = new Product<>(ProductType.Weaponry, price);
            }else if(i < 16){//HeavyUtility
                price = rand.nextInt((250000 - 70000) + 1) + 70000;
                current = new Product<>(ProductType.HeavyUtility, price);
            }else if(i < 21){//Medicine
                price = rand.nextInt((300000 - 4000) + 1) + 4000;
                current = new Product<>(ProductType.Medicine, price);
            }else if(i == 21){//High Class Artwork
                price = rand.nextInt((1000000 - 500000) + 1) + 500000;
                current = new Product<>(ProductType.Art, price);
            }else if(i == 22){//High Class Weaponry
                price = rand.nextInt((5600000 - 2000000) + 1) + 2000000;
                current = new Product<>(ProductType.Weaponry, price);
            }else if(i == 23){//High Class HeavyUtility
                price = rand.nextInt((23000000 - 8000000) + 1) + 8000000;
                current = new Product<>(ProductType.HeavyUtility, price);
            }else{//High Class Medicine
                price = rand.nextInt((9000000 - 7000000) + 1) + 7000000;
                current = new Product<>(ProductType.Medicine, price);
            }
            shop.add(current);
        }
        
    }

    private PaymentType payType(int num){ //returns a payment type specified by parameter
        switch (num) {
            case 1:
                return user.debitCard;
            case 2:
                return user.creditCard;
            default:
                return user.cash;
        }
    }


}