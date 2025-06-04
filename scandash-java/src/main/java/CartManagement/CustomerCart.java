package CartManagement;

import DatabaseConfig.DBConnection;
import ProductManagement.Product;
import ProductManagement.ProductManagement;
import UserManagement.CurrentCustomerSession;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomerCart {
    // Thread-safe cart storage
    private static final Map<Product, Integer> cart;
    static {
        // Ensure cart is initialized when the class is loaded
        cart = new HashMap<>();
    }

    public CustomerCart() {
        // No-args constructor
    }

    public static boolean addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        // Check if product exists in inventory and is available
        Product inventoryProduct = ProductManagement.getProductFromInventory(product.getProdID());
        if (inventoryProduct == null || quantity > inventoryProduct.getQuantity()) {
            return false;
        }

        // Update cart and reduce inventory quantity
        int currentQuantity = getCurrentQuantityInCart(product);
        cart.put(product, currentQuantity + quantity);

        inventoryProduct.setQuantity(inventoryProduct.getQuantity() - quantity);
        return ProductManagement.updateProductInDB(inventoryProduct);
    }

    // Remove product from the cart
    public static boolean removeFromCart(Product product, int quantity) {
        if (product == null) return false;

        synchronized (cart) {
            int currentQuantity = cart.getOrDefault(product, 0);
            if (quantity > currentQuantity) return false;

            if (quantity == currentQuantity) {
                cart.remove(product);
            } else {
                cart.put(product, currentQuantity - quantity);
            }
        }

        Product inventoryProduct = ProductManagement.getProductFromInventory(product.getProdID());
        if (inventoryProduct != null) {
            inventoryProduct.setQuantity(inventoryProduct.getQuantity() + quantity);
            return ProductManagement.updateProductInDB(inventoryProduct);
        }
        return false;
    }

    // Clear the cart and return all quantities to inventory
    public static void clearCart() {
        synchronized (cart) {
            cart.forEach((product, quantity) -> {
                Product inventoryProduct = ProductManagement.getProductFromInventory(product.getProdID());
                if (inventoryProduct != null) {
                    inventoryProduct.setQuantity(inventoryProduct.getQuantity() + quantity);
                    ProductManagement.updateProductInDB(inventoryProduct);
                }
            });
            cart.clear();
        }
    }

    // Calculate total bill
    public static double calculateBill() {
        synchronized (cart) {
            return cart.entrySet().stream()
                    .mapToDouble(entry -> {
                        Product product = entry.getKey();
                        int quantity = entry.getValue();
                        double itemPrice = product.getPrice() * (1 - product.getDiscount() / 100);
                        return itemPrice * quantity;
                    })
                    .sum();
        }
    }

    // Checkout the cart
    public static boolean checkout() {
        if (cart.isEmpty()) {
            System.out.println("HI");
            return false;
        }

        // Get current card ID from session
        String cardId = CurrentCustomerSession.getInstance().getCurrentCardId();
        if (cardId == null) {
            return false;
        }

        try (Connection conn = DBConnection.connectToDB()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try {
                // Calculate total bill
                double totalAmount = calculateBill();

                // Insert sale record into salesPerCard
                String insertSaleQuery = "INSERT INTO \"salesPerCard\" (\"cardid\", \"date\", \"totalAmount\") VALUES (?, CURRENT_DATE, ?)";
                try (PreparedStatement pst = conn.prepareStatement(insertSaleQuery)) {
                    pst.setString(1, cardId);
                    pst.setDouble(2, totalAmount);
                    pst.executeUpdate();
                }

                conn.commit();
                cart.clear();// Clear the cart after successful checkout
                return true;

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get the current quantity of a product in the cart
    public static int getCurrentQuantityInCart(Product product) {
        synchronized (cart) {
            return cart.getOrDefault(product, 0);
        }
    }

    // Check if a product is in the cart
    public static boolean isProductInCart(Product product) {
        synchronized (cart) {
            return cart.containsKey(product);
        }
    }
    public static Map<Product, Integer> getCart() {
        return new HashMap<>(cart);
    }

}
