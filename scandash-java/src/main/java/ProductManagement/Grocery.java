package ProductManagement;

import java.sql.Date;
import java.time.LocalDate;

// Grocery class (subclass of Product)
public class Grocery extends Product {
    private LocalDate expiryDate;
    private LocalDate manufactureDate;

    // Constructor
    public Grocery(String name, String prodID, double price, int quantity, double discount, LocalDate expiryDate, LocalDate manufactureDate) {
        super(name, prodID, price, quantity, discount, "Grocery");
        this.expiryDate = expiryDate;
        this.manufactureDate = manufactureDate;
    }

    public Grocery() {
    }

    // Getters and setters
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

}
