package ProductManagement;

import java.time.LocalDate;

// FreshGrocery class (subclass of Grocery)
public class FreshGrocery extends Grocery {
    private Double weight;

    // Constructor
    public FreshGrocery(String name, String prodID, double price, int quantity, double discount, LocalDate expiryDate, LocalDate manufactureDate, Double weight) {
        super(name, prodID, price, quantity, discount, expiryDate, manufactureDate);
        this.weight = weight;
    }

    public FreshGrocery() {

    }

    // Getters and setters
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight=weight;
    }


}
