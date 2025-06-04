package Windows.ManagerInterfaceWindows.RemoveProductWindows;

import DatabaseConfig.DBConnection;
import ProductManagement.Product;
import ProductManagement.ProductManagement;
import Windows.ManagerInterfaceWindows.ManageInventoryWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class RemoveProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField IDField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField discountField;
    private JLabel categorylbl;

    private JTextField categoryField;

    static Product scannedProduct = new Product();
    // Add this block at the end of the RemoveProductWindow class
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        // Create a new instance of RemoveProductWindow and set it to visible
        RemoveProductWindow removeProductWindow = new RemoveProductWindow(null);
        removeProductWindow.setVisible(true);
    }


    public RemoveProductWindow(String productID) {

        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(RemoveProductWindow.this, "Do you want to quit Scan Dash?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });
        setSize( 500, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setTitle("Remove Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("REMOVE PRODUCT");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        managerLabel.setBounds(20, 10, 300, 40); // Position at top left
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

        JLabel quantitylbl = new JLabel("Quantity");
        quantitylbl.setBounds(60, 210, 100, 13);
        contentPane.add(quantitylbl);

        JLabel discountlbl = new JLabel("Discount");
        discountlbl.setBounds(60, 250, 100, 13);
        contentPane.add(discountlbl);

        JLabel percentlbl = new JLabel("%");
        percentlbl.setBounds(365, 252, 100, 13);
        contentPane.add(percentlbl);

        categorylbl = new JLabel("Category:");
        categorylbl.setBounds(60, 290, 100, 17);
        contentPane.add(categorylbl);

        IDField = new JTextField();
        IDField.setBounds(160, 90, 200, 19);
        contentPane.add(IDField);
        IDField.setColumns(10);
        IDField.setEditable(false);

        nameField = new JTextField();
        nameField.setBounds(160, 130, 200, 19);
        contentPane.add(nameField);
        nameField.setColumns(10);
        nameField.setEditable(false);

        priceField = new JTextField();
        priceField.setBounds(160, 170, 200, 19);
        contentPane.add(priceField);
        priceField.setColumns(10);
        priceField.setEditable(false);

        quantityField = new JTextField();
        quantityField.setBounds(160, 210, 200, 19);
        contentPane.add(quantityField);
        quantityField.setColumns(10);
        quantityField.setEditable(false);

        discountField = new JTextField();
        discountField.setBounds(160, 250, 200, 19);
        contentPane.add(discountField);
        discountField.setColumns(10);
        discountField.setEditable(false);

        categoryField = new JTextField();
        categoryField.setBounds(160, 290, 200, 21);
        contentPane.add(categoryField);
        categoryField.setColumns(10);
        categoryField.setEditable(false);

        scannedProduct = ProductManagement.getProductFromInventory(productID);

        // Set retrieved details to the corresponding fields

        if (scannedProduct == null) {
            JOptionPane.showMessageDialog(null, "Product does not exist", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String name = scannedProduct.getName();
        double price = scannedProduct.getPrice();
        double discount = scannedProduct.getDiscount();
        int quantity = scannedProduct.getQuantity();
        String category = scannedProduct.getCategory();

        // Set retrieved details to the corresponding fields
        IDField.setText(productID);
        nameField.setText(name);
        priceField.setText(String.valueOf(price)); // Convert price to String
        quantityField.setText(String.valueOf(quantity)); // Convert quantity to String
        discountField.setText(String.valueOf(discount)); // Convert discount to String
        categoryField.setText(category);

        JButton removeProductbtn = new JButton("Remove Product");
        removeProductbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (ProductManagement.removeProductFromDB(scannedProduct)) {
                    JOptionPane.showMessageDialog(rootPane, "Product Removed", "Removed",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new ManageInventoryWindow().setVisible(true);

                    return;
                } else {
                    // Error message if the student already exists based in the Student ID
                    JOptionPane.showMessageDialog(null, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    new ManageInventoryWindow().setVisible(true);

                }
            }
        });

        removeProductbtn.setBounds(130, 320, 140, 21);
        removeProductbtn.setBackground(new Color(0, 180, 0));
        removeProductbtn.setForeground(Color.black);
        contentPane.add(removeProductbtn);
        contentPane.add(removeProductbtn);

        JButton cancelbtn = new JButton("Cancel");
        cancelbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ManageInventoryWindow().setVisible(true);
            }
        });
        cancelbtn.setBackground(Color.red);
        cancelbtn.setForeground(Color.black);
        cancelbtn.setBounds(300, 320, 85, 21);
        contentPane.add(cancelbtn);
        setLocationRelativeTo(null);

    }
}