package Windows.ManagerInterfaceWindows.AddProductWindows;

import DatabaseConfig.DBConnection;
import ProductManagement.*;
import Windows.ManagerInterfaceWindows.ManageInventoryWindow;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField IDField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField discountField;
    private JLabel categorylbl;
    private JComboBox<String> categoryBox;

    static Product scannedProduct = new Product();

    /**
     * Create the frame.
     */
    public AddProductWindow(String productID) {

        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(AddProductWindow.this, "Do you want to quit Scan Dash?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setBounds(500, 200, 500, 400);
        setResizable(false); // Prevent resizing
        setTitle("Add Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("PRODUCT");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        managerLabel.setBounds(20, 10, 200, 40);
        topPanel.add(managerLabel);

        JLabel productIDlbl = new JLabel("Product ID:");
        productIDlbl.setBounds(60, 90, 100, 13);
        contentPane.add(productIDlbl);

        JLabel productNamelbl = new JLabel("Product Name:");
        productNamelbl.setBounds(60, 130, 100, 13);
        contentPane.add(productNamelbl);

        JLabel pricelbl = new JLabel("Product Price:");
        pricelbl.setBounds(60, 170, 100, 13);
        contentPane.add(pricelbl);

        JLabel Rslbl = new JLabel("Rs");
        Rslbl.setBounds(365, 172, 100, 13);
        contentPane.add(Rslbl);

        JLabel quantitylbl = new JLabel("Quantity:");
        quantitylbl.setBounds(60, 210, 100, 13);
        contentPane.add(quantitylbl);

        JLabel discountlbl = new JLabel("Discount:");
        discountlbl.setBounds(60, 250, 100, 13);
        contentPane.add(discountlbl);

        JLabel percentlbl = new JLabel("%");
        percentlbl.setBounds(365, 252, 100, 13);
        contentPane.add(percentlbl);

        categorylbl = new JLabel("Category:");
        categorylbl.setBounds(60, 290, 100, 13);
        contentPane.add(categorylbl);

        IDField = new JTextField();
        IDField.setBounds(160, 90, 200, 19);
        contentPane.add(IDField);
        IDField.setColumns(10);
        IDField.setText(productID);
        IDField.setEditable(false);

        nameField = new JTextField();
        nameField.setBounds(160, 130, 200, 19);
        contentPane.add(nameField);
        nameField.setColumns(10);

        priceField = new JTextField();
        priceField.setBounds(160, 170, 200, 19);
        contentPane.add(priceField);
        priceField.setColumns(10);

        quantityField = new JTextField();
        quantityField.setBounds(160, 210, 200, 19);
        contentPane.add(quantityField);
        quantityField.setColumns(10);

        discountField = new JTextField();
        discountField.setBounds(160, 250, 200, 19);
        contentPane.add(discountField);
        discountField.setColumns(10);

        categoryBox = new JComboBox<>();
        categoryBox.setBounds(160, 290, 200, 21);
        contentPane.add(categoryBox);

        // Add categories to the JComboBox
        categoryBox.addItem("Electronics");
        categoryBox.addItem("Cosmetics");
        categoryBox.addItem("Appliances");
        categoryBox.addItem("Fresh Grocery");
        categoryBox.addItem("Packaged Product");

        JButton addProductbtn = new JButton("Add Product");
        addProductbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEmptyFields()) {
                    JOptionPane.showMessageDialog(null, "Please Fill in All Fields", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!validateDetails()) {
                    return;
                }

                // Create a new Product object
                scannedProduct = createProduct(productID);

                // Add the created product to the inventory
                boolean success = ProductManagement.addProductToInventory(scannedProduct);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Product added successfully", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Open the appropriate product description window based on the category

                    openProductDescriptionWindow(scannedProduct.getCategory());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add product. Product may already exist.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    dispose();
                }
            }
        });

        addProductbtn.setBounds(180, 320, 120, 21);
        addProductbtn.setBackground(new Color(0, 180, 0));
        addProductbtn.setForeground(Color.black);
        contentPane.add(addProductbtn);

        // Red close button
        JButton closeButton = new JButton("BACK");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(370, 10, 100, 30);
        closeButton.addActionListener(e -> {
            dispose(); // Close the current window
            SwingUtilities.invokeLater(() -> {
                new ManageInventoryWindow().setVisible(true);
            });
        });
        topPanel.add(closeButton);
    }

    private Boolean isEmptyFields() {
        String prodID = IDField.getText();
        String name = nameField.getText();
        String price = priceField.getText();
        String quantity = quantityField.getText();
        String discount = discountField.getText();

        return prodID.isEmpty() || name.isEmpty() || price.isEmpty() || quantity.isEmpty() || discount.isEmpty();
    }

    private void openProductDescriptionWindow(String category) {
        JFrame descriptionWindow;
        switch (category) {
            case "Electronics":
                ElectronicsProduct electronicsProduct = ProductManagement.createElectronicsProduct(scannedProduct);
                descriptionWindow = new AddElectronicsDescriptionWindow(electronicsProduct);
                break;
            case "Packaged Product":
                PackagedProduct packagedProduct = ProductManagement.createPackagedProduct(scannedProduct);
                descriptionWindow = new AddPackagedGroceryDescriptionWindow(packagedProduct);
                break;
            case "Cosmetics":
                CosmeticsProduct cosmeticsProduct = ProductManagement.createCosmeticsProduct(scannedProduct);
                descriptionWindow = new AddCosmeticsDescriptionWindow(cosmeticsProduct);
                break;
            case "Appliances":
                ApplianceProduct applianceProduct = ProductManagement.createApplianceProduct(scannedProduct);
                descriptionWindow = new AddApplianceDescriptionWindow(applianceProduct);
                break;
            case "Fresh Grocery":
                FreshGrocery freshGroceryProduct = ProductManagement.createFreshGroceryProduct(scannedProduct);
                descriptionWindow = new AddFreshGroceryDescriptionWindow(freshGroceryProduct);
                break;
            default:
                return; // Exit if category is not recognized
        }

        // Show the description window
        descriptionWindow.setVisible(true);
    }


    public Product createProduct(String productID) {
        Product product = new Product();
        product.setProdID(productID);
        product.setName(nameField.getText());
        product.setPrice(Double.parseDouble(priceField.getText()));
        product.setQuantity(Integer.parseInt(quantityField.getText()));
        product.setDiscount(Double.parseDouble(discountField.getText()));
        product.setCategory((String) categoryBox.getSelectedItem());
        return product;
    }

    private boolean validateDetails() {
        try {
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double discount = Double.parseDouble(discountField.getText());

            if (!isValidPrice(price)) {
                JOptionPane.showMessageDialog(this, "Invalid Price!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity should be a positive integer!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidDiscount(discount)) {
                JOptionPane.showMessageDialog(this, "Invalid Discount!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean isValidPrice(double price) {
        return price > 0;
    }

    private boolean isValidDiscount(double discount) {
        return discount >= 0 && discount <= 100;
    }
}
