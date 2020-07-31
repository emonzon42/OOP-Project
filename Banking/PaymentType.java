package Projects.Proj4.Banking;
/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * PaymentType
 */
public interface PaymentType {

    double getBal(); //return balance

    int add(double amount); //add to balance

    int charge(double amount); //remove from balance

}