package Projects.Proj4.SellBuySell;


import java.util.ArrayList;

/**
 * Eli Monzon
 * 4.20.20
 * ICSI 311
 * PaymentDriver
 */
public class SBSDriver {

    public static void main(String[] args) {     
        SellBuySell sbs = new SellBuySell();
        boolean playing = true;

        while (playing) {
            playing = sbs.run();
        }
    }
}