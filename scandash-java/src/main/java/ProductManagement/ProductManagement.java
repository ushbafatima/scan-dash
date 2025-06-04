package ProductManagement;

import DatabaseConfig.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class ProductManagement {
    public static Connection con;
    private static PreparedStatement pst;
    private static ResultSet rs;

    public ProductManagement() {
        // Initialize the connection, statement, and result set to null
        con = null;
        pst = null;
        rs = null;
    }

    /******************* METHODS TO MANAGE PRODUCT OPERATIONS ***************/
    public static Product getProductFromInventory(String productUID) {
        Product product = null;
        try {
            // Prepare the query
            String query = "SELECT * FROM inventory WHERE productID = ?";
            pst = con.prepareStatement(query);

            // Set the parameter
            pst.setString(1, productUID);

            // Execute the query
            rs = pst.executeQuery();

            // Process the result
            if (rs.next()) {
                product = new Product();
                // Map the result to the Product object
                product.setProdID(rs.getString("productID"));
                product.setName(rs.getString("productName"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDiscount(rs.getDouble("discount"));
                product.setCategory(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return product;
    }

    public static Boolean addProductToInventory(Product product) {
        PreparedStatement pst = null; // Local declaration for better resource management
        try {
            // Check if the product already exists
            if (getProductFromInventory(product.getProdID()) != null) {
                return false; // Return false if the product already exists
            }

            // Prepare SQL statement to insert the product
            String query = "INSERT INTO inventory (productID, productName, price, quantity, discount, category) VALUES (?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, product.getProdID());
            pst.setString(2, product.getName());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getQuantity());
            pst.setDouble(5, product.getDiscount());
            pst.setString(6, product.getCategory());

            // Execute the SQL statement to insert the product into the database
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // Return true if a row was successfully inserted

        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
            return false; // Return false to indicate unsuccessful addition of the product
        } finally {
            // Close resources
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Log the exception for debugging
            }
        }
    }


    public static boolean removeProductFromDB(Product product) {
        // Check if the product exists in the database
        if (getProductFromInventory(product.getProdID()) == null) {
            return false; // Product does not exist, so return false
        }

        try {
            // Prepare SQL statement to delete the product
            String query = "DELETE FROM inventory WHERE productID = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, product.getProdID());

            // Execute the SQL statement to delete the product from the database
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                return true; // Return true to indicate successful removal of the product
            } else {
                return false; // Return false if no rows were affected (product not found or deletion failed)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate unsuccessful removal of the product
        } finally {
            closeResources();
        }
    }

    public static boolean updateProductInDB(Product product) {
        // Check if the product exists in the database
        if (getProductFromInventory(product.getProdID()) == null) {
            return false; // Product does not exist, so return false
        }

        try {
            // Prepare SQL statement to update only discount, quantity, and price
            String query = "UPDATE inventory SET price = ?, quantity = ?, discount = ? WHERE productID = ?";
            pst = con.prepareStatement(query);
            pst.setDouble(1, product.getPrice());
            pst.setInt(2, product.getQuantity());
            pst.setDouble(3, product.getDiscount());
            pst.setString(4, product.getProdID());

            // Execute the SQL statement to update the product in the database
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                return true; // Return true to indicate successful update of the product
            } else {
                return false; // Return false if no rows were affected (update failed)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate unsuccessful update of the product
        } finally {
            closeResources();
        }
    }


    /******************* METHODS TO MANAGE PRODUCT OPERATIONS ***************/

    /******************* METHODS TO MANAGE CASTING ***************/

    // Helper methods for each product type
    public static ElectronicsProduct createElectronicsProduct(Product product) {
        ElectronicsProduct newProduct = new ElectronicsProduct();
        copyCommonProductDetails(product, newProduct);
        return newProduct;
    }

    public static PackagedProduct createPackagedProduct(Product product) {
        PackagedProduct newProduct = new PackagedProduct();
        copyCommonProductDetails(product, newProduct);
        return newProduct;
    }

    public static CosmeticsProduct createCosmeticsProduct(Product product) {
        CosmeticsProduct newProduct = new CosmeticsProduct();
        copyCommonProductDetails(product, newProduct);
        return newProduct;
    }

    public static ApplianceProduct createApplianceProduct(Product product) {
        ApplianceProduct newProduct = new ApplianceProduct();
        copyCommonProductDetails(product, newProduct);
        return newProduct;
    }

    public static FreshGrocery createFreshGroceryProduct(Product product) {
        FreshGrocery newProduct = new FreshGrocery();
        copyCommonProductDetails(product, newProduct);
        return newProduct;
    }


    // Helper method to copy common details from source to destination
    private static void copyCommonProductDetails(Product source, Product destination) {
        destination.setProdID(source.getProdID());
        destination.setName(source.getName());
        destination.setPrice(source.getPrice());
        destination.setQuantity(source.getQuantity());
        destination.setDiscount(source.getDiscount());
        destination.setCategory(source.getCategory());
    }
    /******************* METHODS TO MANAGE CASTING ***************/

    /******************* METHODS TO ADD CATEGORY SPECIFIC PRODUCT ***************/
    public static Boolean addElectronicsDescription(ElectronicsProduct electronicsProduct) {
        try {
            String query = "INSERT INTO electronics (productID, warrantyPeriod, model) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "warrantyPeriod = EXCLUDED.warrantyPeriod, model = EXCLUDED.model";

            pst = con.prepareStatement(query);
            pst.setString(1, electronicsProduct.getProdID());
            pst.setInt(2, electronicsProduct.getWarrantyPeriod());
            pst.setString(3, electronicsProduct.getModel());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }


    public static Boolean addApplianceDescription(ApplianceProduct applianceProduct) {
        try {
            String query = "INSERT INTO appliances (productID, capacity, efficiency) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "capacity = EXCLUDED.capacity, efficiency = EXCLUDED.efficiency";

            pst = con.prepareStatement(query);
            pst.setString(1, applianceProduct.getProdID()); // productID as String
            pst.setBigDecimal(2, BigDecimal.valueOf(applianceProduct.getCapacity())); // capacity as BigDecimal
            pst.setBigDecimal(3, BigDecimal.valueOf(applianceProduct.getEfficiencyRate())); // efficiency as BigDecimal

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public static Boolean addCosmeticsDescription(CosmeticsProduct cosmeticProduct) {
        try {
            String query = "INSERT INTO cosmetics (productID, ingredients, brand) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "ingredients = EXCLUDED.ingredients, brand = EXCLUDED.brand";

            pst = con.prepareStatement(query);
            pst.setString(1, cosmeticProduct.getProdID()); // productID as String
            pst.setString(2, cosmeticProduct.getIngredients()); // ingredients as String
            pst.setString(3, cosmeticProduct.getBrand()); // brand as String

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public static Boolean addPackagedGroceryDescription(PackagedProduct packagedProduct) {
        try {
            // First insert into grocery2 table with expiryDate, manufactureDate, and subcategory set to "Packaged Grocery"
            String queryGrocery = "INSERT INTO grocery (productID, expiryDate, manufactureDate, subcategory) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "expiryDate = EXCLUDED.expiryDate, manufactureDate = EXCLUDED.manufactureDate, subcategory = EXCLUDED.subcategory";

            pst = con.prepareStatement(queryGrocery);
            pst.setString(1, packagedProduct.getProdID());
            pst.setDate(2, java.sql.Date.valueOf(packagedProduct.getExpiryDate()));  // Convert LocalDate to java.sql.Date
            pst.setDate(3, java.sql.Date.valueOf(packagedProduct.getManufactureDate()));  // Convert LocalDate to java.sql.Date
            pst.setString(4, packagedProduct.getCategory());  // Setting subcategory to "Packaged Grocery"

            int rowsAffectedGrocery = pst.executeUpdate();

            // Now insert into packagedGrocery table with brand
            String queryPackagedGrocery = "INSERT INTO packagedProduct (productID, brand) " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "brand = EXCLUDED.brand";

            pst = con.prepareStatement(queryPackagedGrocery);
            pst.setString(1, packagedProduct.getProdID());
            pst.setString(2, packagedProduct.getBrand());  // Assuming brand is a String

            int rowsAffectedPackagedGrocery = pst.executeUpdate();

            return rowsAffectedGrocery > 0 && rowsAffectedPackagedGrocery > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }public static Boolean addFreshGroceryDescription(FreshGrocery freshGrocery) {
        try {
            // First insert into grocery2 table with expiryDate, manufactureDate, and subcategory set to "Fresh Grocery"
            String queryGrocery = "INSERT INTO grocery (productID, expiryDate, manufactureDate, subcategory) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "expiryDate = EXCLUDED.expiryDate, " +
                    "manufactureDate = EXCLUDED.manufactureDate, " +
                    "subcategory = EXCLUDED.subcategory";

            pst = con.prepareStatement(queryGrocery);
            pst.setString(1, freshGrocery.getProdID());
            pst.setDate(2, java.sql.Date.valueOf(freshGrocery.getExpiryDate()));  // expiryDate as VARCHAR
            pst.setDate(3, java.sql.Date.valueOf(freshGrocery.getManufactureDate()));  // manufactureDate as VARCHAR
            pst.setString(4, freshGrocery.getCategory());  // Setting subcategory to "Fresh Grocery"


            int rowsAffectedGrocery = pst.executeUpdate();

            // Insert into freshGrocery2 table after inserting into grocery2
            String freshGroceryQuery = "INSERT INTO freshGrocery (productID, weight) " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT (productID) DO UPDATE SET " +
                    "weight = EXCLUDED.weight";

            pst = con.prepareStatement(freshGroceryQuery);
            pst.setString(1, freshGrocery.getProdID());
            pst.setDouble(2, freshGrocery.getWeight());
            int rowsAffectedFreshGrocery = pst.executeUpdate();

            return rowsAffectedGrocery > 0 && rowsAffectedFreshGrocery > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }



    /******************* METHODS TO ADD CATEGORY SPECIFIC PRODUCT ***************/

    /******************* METHODS TO GET CATEGORY SPECIFIC PRODUCT ***************/
    public static ApplianceProduct getApplianceProduct(ApplianceProduct applianceProduct) {
        try {
            String query = "SELECT * FROM appliances WHERE productID = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, applianceProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                double capacity = rs.getDouble("capacity");
                double efficiencyRate = rs.getDouble("efficiency");

                // Update the existing ApplianceProduct object with the retrieved information
                applianceProduct.setCapacity(capacity);
                applianceProduct.setEfficiencyRate(efficiencyRate);
                // Return the updated ApplianceProduct object
                return applianceProduct;
            } else {
                // Product not found, set capacity and efficiency rate to 0
                applianceProduct.setCapacity(0);
                applianceProduct.setEfficiencyRate(0.0);
                // Return the modified ApplianceProduct object
                return applianceProduct;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    public static FreshGrocery getFreshGrocery(FreshGrocery freshGroceryProduct) {
    try {
        String query = "SELECT g.expiryDate, g.manufactureDate, f.weight " +
                "FROM grocery g " +
                "INNER JOIN freshGrocery f ON g.productID = p.productID " +
                "WHERE g.productID = ?";

        pst = con.prepareStatement(query);
        pst.setString(1, freshGroceryProduct.getProdID());
        ResultSet rs = pst.executeQuery();

        // Check if the product with the given prodID exists
        if (rs.next()) {
            String expiryDate = rs.getString("expiryDate");
            String manufactureDate = rs.getString("manufactureDate");
            String weight = rs.getString("weight");

            // Update the existing FreshGrocery object with the retrieved information
            freshGroceryProduct.setExpiryDate(LocalDate.parse(expiryDate));
            freshGroceryProduct.setManufactureDate(LocalDate.parse(manufactureDate));
            freshGroceryProduct.setWeight(Double.valueOf(weight)); // Assuming it's a String, or could be a decimal if needed
            // Return the updated FreshGrocery object
            return freshGroceryProduct;
        } else {
            return null;  // No matching product found
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;  // Error occurred during retrieval
    } finally {
        closeResources();  // Ensure resources are closed in the end
    }
}


   public static PackagedProduct getPackagedProduct(PackagedProduct packedGroceryProduct) {
    try {
        // SQL query with JOIN to retrieve data from both grocery and packagedProduct tables
        String query = "SELECT g.expiryDate, g.manufactureDate, p.brand " +
                       "FROM grocery g " +
                       "INNER JOIN packagedProduct p ON g.productID = p.productID " +
                       "WHERE g.productID = ?";

        pst = con.prepareStatement(query);
        pst.setString(1, packedGroceryProduct.getProdID());
        ResultSet rs = pst.executeQuery();

        // Check if the product with the given prodID exists
        if (rs.next()) {
            String expiryDate = rs.getString("expiryDate");
            String manufactureDate = rs.getString("manufactureDate");
            String brand = rs.getString("brand");

            // Update the existing PackedGrocery object with the retrieved information
            packedGroceryProduct.setExpiryDate(LocalDate.parse(expiryDate));
            packedGroceryProduct.setManufactureDate(LocalDate.parse(manufactureDate));
            packedGroceryProduct.setBrand(brand);

            // Return the updated PackedGrocery object
            return packedGroceryProduct;
        } else {
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null; // Error occurred during retrieval
    } finally {
        closeResources();
    }
}


    public static CosmeticsProduct getCosmeticsProduct(CosmeticsProduct cosmeticsProduct) {
        try {
            String query = "SELECT * FROM cosmetics WHERE productID = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, cosmeticsProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String ingredients = rs.getString("ingredients");
                String brand = rs.getString("brand");

                // Update the existing CosmeticsProduct object with the retrieved information
                cosmeticsProduct.setIngredients(ingredients);
                cosmeticsProduct.setBrand(brand);
                // Return the updated CosmeticsProduct object
                return cosmeticsProduct;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    public static ElectronicsProduct getElectronicsProduct(ElectronicsProduct electronicsProduct) {
        try {
            String query = "SELECT * FROM electronics WHERE productID= ?";
            pst = con.prepareStatement(query);
            pst.setString(1, electronicsProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String warrantyPeriod = rs.getString("warrantyPeriod");
                String model = rs.getString("model"); // Retrieve the "model" column from the database

                // Update the existing ElectronicsProduct object with the retrieved information
                electronicsProduct.setWarrantyPeriod(Integer.parseInt(warrantyPeriod));
                electronicsProduct.setModel(model); // Set the model in the ElectronicsProduct object
                // Return the updated ElectronicsProduct object
                return electronicsProduct;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    /******************* METHODS TO GET CATEGORY SPECIFIC PRODUCT ***************/

    private static void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}