package Windows.CustomerInterfaceWindows;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import CardManagement.CardManagement;
import DatabaseConfig.DBConnection;
import ProductManagement.Product;
import ProductManagement.ProductManagement;
import ProductManagement.ProductScanner;
import Windows.ManagerInterfaceWindows.ManagerTasksWindow;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ScanProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static String productUID;
    private String action;

    public ScanProductWindow() {
        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();


        setUndecorated(true);

        this.action = action;
        System.out.println("Scanning Product...");
        setBounds(500, 200, 500, 400);
        setTitle("Scan Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel scanLabel = new JLabel("SCAN PRODUCT");
        scanLabel.setForeground(Color.WHITE);
        scanLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        scanLabel.setBounds(20, 10, 200, 40); // Position at top left
        topPanel.add(scanLabel);

        JLabel ScanProductIcon = new JLabel("");
        ScanProductIcon.setHorizontalAlignment(SwingConstants.CENTER);
        ScanProductIcon.setBackground(Color.GREEN);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Images/ScanProduct.gif"));
        ScanProductIcon.setIcon(icon1);
        ScanProductIcon.setBounds(116, 116, 268, 200);
        contentPane.add(ScanProductIcon);

        JButton closebtn = new JButton("Close");
        closebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new CustomerShoppingWindow().setVisible(true);
                dispose();

            }
        });
        closebtn.setForeground(Color.WHITE);
        closebtn.setBackground(Color.RED);
        closebtn.setBounds(205, 338, 85, 21);
        contentPane.add(closebtn);

        setLocationRelativeTo(null);
        // Start a thread for product processing logic
        new Thread(this::processProduct).start();
    }
    /**
     * Threaded method to handle product scanning and validation.
     */
    private void processProduct() {
        String productUID = null;
        try {
            productUID = scanProduct(); // Simulate product scanning or fetch UID
        } catch (Exception e) {
            showErrorAndRestart("Scanning failed: " + e.getMessage());
            return;
        }

        if (productUID == null ){

            JOptionPane.showMessageDialog(this, "Product Scanner Disconnected", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            new CustomerShoppingWindow().setVisible(true);
            return;
        }
        if (!isValidUID(productUID)) {
            showErrorAndRestart("Invalid Product UID. Please try again.");
            return;
        }

        try {
            Product product = ProductManagement.getProductFromInventory(productUID);
            if (product==null) {
                showErrorAndRestart("Product Not Found. Restarting scanning...");
                return;
            }
            // Successful validation
            String finalUID = productUID;
            SwingUtilities.invokeLater(() -> {
                dispose();
                // Redirect to next window or process further
                ShopProductsWindow shoppingWindow = new ShopProductsWindow(finalUID);
                shoppingWindow.setVisible(true);
            });
        } catch (Exception e) {
            showErrorAndRestart("Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Handles displaying errors and restarting the window.
     *
     * @param message Error message to display.
     */
    private void showErrorAndRestart(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            restartScanningWindow();
        });
    }

    private void restartScanningWindow() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            try {
                Thread.sleep(500); // Short delay before restarting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            ScanProductWindow newWindow = new ScanProductWindow();
            newWindow.setVisible(true);
        });
    }



    /**
     * Simplified method to scan a product UID.
     * This method should integrate with the actual scanning hardware or logic.
     *
     * @return The scanned product UID.
     */
    public static String scanProduct() {
        try {
            return ProductScanner.getProductID();
        } catch (Exception e) {
            System.err.println("Failed to scan product: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validates a product UID.
     *
     * @param UID The product UID to validate.
     * @return True if valid, false otherwise.
     */
    public Boolean isValidUID(String UID) {
        return UID != null && UID.length() >= 8;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ScanProductWindow frame = new ScanProductWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

