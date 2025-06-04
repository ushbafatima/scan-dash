package Windows.CustomerInterfaceWindows;

import CartManagement.CustomerCart;
import DatabaseConfig.DBConnection;
import ProductManagement.Product;
import Windows.CustomerInterfaceWindows.ViewProductWindows.*;
import Windows.GeneralWindows.FirstWindow;
import Windows.ManagerInterfaceWindows.AddProductWindows.AddFreshGroceryDescriptionWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import ProductManagement.*;

import static ProductManagement.ProductManagement.*;

public class ShopProductsWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField productIDField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField discountField;
    private JTextField categoryField;
    private JTextField quantityField;

    static Product scannedProduct = new Product();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                ShopProductsWindow frame = new ShopProductsWindow("exampleID"); // Example product ID
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ShopProductsWindow(String productID) {
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(ShopProductsWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setSize(500, 400);
        setResizable(false);
        setTitle("Shop Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setBounds(0, 0, 500, 50);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("PRODUCT");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        managerLabel.setBounds(20, 10, 200, 30);
        topPanel.add(managerLabel);

        int labelX = 40, labelWidth = 100, fieldX = 150, fieldWidth = 250, rowHeight = 30;
        int yStart = 70;

        JLabel productIDlbl = new JLabel("Product ID:");
        productIDlbl.setBounds(labelX, yStart, labelWidth, rowHeight);
        contentPane.add(productIDlbl);

        productIDField = new JTextField();
        productIDField.setEditable(false);
        productIDField.setBounds(fieldX, yStart, fieldWidth, 25);
        contentPane.add(productIDField);

        JLabel productNamelbl = new JLabel("Product Name:");
        productNamelbl.setBounds(labelX, yStart += 40, labelWidth, rowHeight);
        contentPane.add(productNamelbl);

        productNameField = new JTextField();
        productNameField.setEditable(false);
        productNameField.setBounds(fieldX, yStart, fieldWidth, 25);
        contentPane.add(productNameField);

        JLabel pricelbl = new JLabel("Product Price:");
        pricelbl.setBounds(labelX, yStart += 40, labelWidth, rowHeight);
        contentPane.add(pricelbl);

        productPriceField = new JTextField();
        productPriceField.setEditable(false);
        productPriceField.setBounds(fieldX, yStart, fieldWidth - 30, 25);
        contentPane.add(productPriceField);

        JLabel Rslbl = new JLabel("Rs");
        Rslbl.setBounds(fieldX + fieldWidth - 20, yStart, 20, rowHeight);
        contentPane.add(Rslbl);

        JLabel discountlbl = new JLabel("Discount:");
        discountlbl.setBounds(labelX, yStart += 40, labelWidth, rowHeight);
        contentPane.add(discountlbl);

        discountField = new JTextField();
        discountField.setEditable(false);
        discountField.setBounds(fieldX, yStart, fieldWidth - 30, 25);
        contentPane.add(discountField);

        JLabel percentlbl = new JLabel("%");
        percentlbl.setBounds(fieldX + fieldWidth - 20, yStart, 20, rowHeight);
        contentPane.add(percentlbl);

        JLabel categorylbl = new JLabel("Category:");
        categorylbl.setBounds(labelX, yStart += 40, labelWidth, rowHeight);
        contentPane.add(categorylbl);

        categoryField = new JTextField();
        categoryField.setEditable(false);
        categoryField.setBounds(fieldX, yStart, fieldWidth, 25);
        contentPane.add(categoryField);

        JLabel quantitylbl = new JLabel("Quantity:");
        quantitylbl.setBounds(labelX, yStart += 40, labelWidth, rowHeight);
        contentPane.add(quantitylbl);

        quantityField = new JTextField();
        quantityField.setBounds(fieldX + 50, yStart, 50, 25);
        contentPane.add(quantityField);

        // Red close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(440, 10, 30, 30);
        closeButton.addActionListener(e -> {
            dispose();
            new CustomerShoppingWindow().setVisible(true);
        });
        topPanel.add(closeButton);

        JButton minusbtn = new JButton("-");
        minusbtn.setBounds(fieldX, yStart, 40, 25);
        contentPane.add(minusbtn);
        minusbtn.addActionListener(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity > 0) quantity--;
            quantityField.setText(Integer.toString(quantity));
        });

        JButton plusbtn = new JButton("+");
        plusbtn.setBounds(fieldX + 110, yStart, 40, 25);
        contentPane.add(plusbtn);
        plusbtn.addActionListener(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            quantity++;
            quantityField.setText(Integer.toString(quantity));
        });


        // Load product details from the database
        scannedProduct = ProductManagement.getProductFromInventory(productID);
        if (scannedProduct != null) {
            productIDField.setText(productID);
            productNameField.setText(scannedProduct.getName());
            productPriceField.setText(String.valueOf(scannedProduct.getPrice()));
            discountField.setText(String.valueOf(scannedProduct.getDiscount()));
            categoryField.setText(scannedProduct.getCategory());
            quantityField.setText("0");
        } else {
            JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        int buttonWidth = 120;
        JButton addProductbtn = new JButton("Add Product");
        addProductbtn.addActionListener(e -> {
            try {
                // Validate the entered quantity
                if (!isValidQuantity()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid quantity.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantity = Integer.parseInt(quantityField.getText());

                // Check if the requested quantity is available
                if (quantity > scannedProduct.getQuantity()) {
                    JOptionPane.showMessageDialog(null, "Insufficient quantity available.\nQuantity in Store: " + scannedProduct.getQuantity(), "Quantity Unavailable", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Add the scanned product to the cart
                boolean addedSuccessfully = CustomerCart.addToCart(scannedProduct, quantity);
                if (addedSuccessfully) {
                    JOptionPane.showMessageDialog(null, "Product added to cart successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add product to cart.", "Error", JOptionPane.ERROR_MESSAGE);
                }


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addProductbtn.setBackground(new Color(0, 0, 100));
        addProductbtn.setForeground(Color.WHITE);
        addProductbtn.setBounds(40, 310, buttonWidth, 30);
        contentPane.add(addProductbtn);

        JButton viewDescriptionbtn = new JButton("View Description");
        viewDescriptionbtn.setBounds(170, 310, buttonWidth + 40, 30);
        viewDescriptionbtn.setBackground(new Color(0, 0, 100));
        viewDescriptionbtn.setForeground(Color.WHITE);
        contentPane.add(viewDescriptionbtn);
        viewDescriptionbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewProductDescriptionWindow(scannedProduct.getCategory());
            }
        });

        JButton removeProductbtn = new JButton("Remove Product");
        removeProductbtn.addActionListener(e -> {
            if (scannedProduct == null) {
                JOptionPane.showMessageDialog(null, "No product scanned.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!CustomerCart.isProductInCart(scannedProduct)) {
                JOptionPane.showMessageDialog(null, "Product not found in cart.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int quantityToRemove = Integer.parseInt(quantityField.getText());
                if (quantityToRemove <= 0) {
                    JOptionPane.showMessageDialog(null, "Quantity must be greater than 0!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int currentQuantityInCart = CustomerCart.getCurrentQuantityInCart(scannedProduct);

                if (quantityToRemove > currentQuantityInCart) {
                    JOptionPane.showMessageDialog(null, "Quantity to remove exceeds quantity in cart.\nAvailable quantity in cart: " + currentQuantityInCart, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Use scannedProduct directly
                boolean removedSuccessfully = CustomerCart.removeFromCart(scannedProduct, quantityToRemove);
                if (removedSuccessfully) {
                    JOptionPane.showMessageDialog(null, "Product removed from cart successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove product from cart.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeProductbtn.setBounds(340, 310, buttonWidth + 10, 30);
        removeProductbtn.setBackground(new Color(0, 0, 100));
        removeProductbtn.setForeground(Color.WHITE);
        contentPane.add(removeProductbtn);


    }

    public Boolean isQuantityAvailable() {
        int setQuantity = Integer.parseInt(quantityField.getText());
        int availableQuantity = scannedProduct.getQuantity();
        return setQuantity <= availableQuantity;
    }

    public boolean isValidQuantity() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void viewProductDescriptionWindow(String category) {
        JFrame descriptionWindow;
        switch (category) {
            case "Electronics":
                ElectronicsProduct electronicsProduct = ProductManagement.createElectronicsProduct(scannedProduct);
                electronicsProduct=getElectronicsProduct(electronicsProduct);
                descriptionWindow = new ViewElectronicsDescriptionWindow(electronicsProduct);
                break;
            case "Packaged Product":
                PackagedProduct packagedProduct = ProductManagement.createPackagedProduct(scannedProduct);
                packagedProduct= getPackagedProduct(packagedProduct);
                descriptionWindow = new ViewPackagedGroceryDescriptionWindow(packagedProduct);
                break;
            case "Cosmetics":
                CosmeticsProduct cosmeticsProduct = ProductManagement.createCosmeticsProduct(scannedProduct);
                cosmeticsProduct=getCosmeticsProduct(cosmeticsProduct);
                descriptionWindow = new ViewCosmeticsDescriptionWindow(cosmeticsProduct);
                break;
            case "Appliances":
                ApplianceProduct applianceProduct = ProductManagement.createApplianceProduct(scannedProduct);
                applianceProduct=getApplianceProduct(applianceProduct);
                descriptionWindow = new ViewApplianceDescriptionWindow(applianceProduct);
                break;
            case "Fresh Grocery":
                FreshGrocery freshGroceryProduct = ProductManagement.createFreshGroceryProduct(scannedProduct);
                freshGroceryProduct= getFreshGrocery(freshGroceryProduct);
                descriptionWindow = new ViewFreshGroceryDescriptionWindow(freshGroceryProduct);
                break;
            default:
                return; // Exit if category is not recognized
        }

        // Show the description window
        descriptionWindow.setVisible(true);
    }
}
