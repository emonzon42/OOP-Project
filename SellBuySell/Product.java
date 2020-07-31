package Projects.Proj4.SellBuySell;

public class Product<T> {

    private double buyPrice;
    private T type;

    public Product(T type, double buyPrice){//consttructor
        this.buyPrice = buyPrice;
        this.type = type;
    }

    public T getType(){//specified product type (I chose to represent Product by enums)
        return this.type;
    }

    public double getPrice(){//price
        return this.buyPrice;
    }

}