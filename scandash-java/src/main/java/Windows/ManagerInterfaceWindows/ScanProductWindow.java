package Windows.ManagerInterfaceWindows;

import DatabaseConfig.DBConnection;
import ProductManagement.Product;
import ProductManagement.ProductManagement;

import ProductManagement.ProductScanner;
import Windows.ManagerInterfaceWindows.AddProductWindows.AddProductWindow;
import Windows.ManagerInterfaceWindows.RemoveProductWindows.RemoveProductWindow;
import Windows.ManagerInterfaceWindows.UpdateProductWindows.UpdateProductWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScanProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String productUID;
    private String action; // Initialized in the constructor

    public ScanProductWindow(String action) {
        this.action = action;
        System.out.println("Scanning Product...");

        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();

        setUndecorated(true);
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
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel scanLabel = new JLabel("SCAN PRODUCT");
        scanLabel.setForeground(Color.WHITE);
        scanLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        scanLabel.setBounds(20, 10, 200, 40);
        topPanel.add(scanLabel);

        JLabel ScanProductIcon = new JLabel("");
        ScanProductIcon.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Images/ScanProduct.gif"));
        ScanProductIcon.setIcon(icon1);
        ScanProductIcon.setBounds(116, 116, 268, 200);
        contentPane.add(ScanProductIcon);

        JButton closebtn = new JButton("Close");
        closebtn.addActionListener(e -> {
            new ManageInventoryWindow().setVisible(true);
            dispose();
        });
        closebtn.setForeground(Color.WHITE);
        closebtn.setBackground(Color.RED);
        closebtn.setBounds(205, 338, 85, 21);
        contentPane.add(closebtn);

        setLocationRelativeTo(null);

        // Start a thread for product processing logic
        new Thread(this::processProduct).start();
    }

    private void processProduct() {
        try {
            productUID = scanProduct(); // Simulate product scanning
        } catch (Exception e) {
            showErrorAndRestart("Scanning failed: " + e.getMessage());
            return;
        }
        if (productUID == null ){
            JOptionPane.showMessageDialog(this, "Please Connect Product Scanner", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            new ManagerTasksWindow().setVisible(true);
            return;
        }
        if (!isValidUID(productUID)) {
            showErrorAndRestart("Invalid Product UID. Please try again.");
            return;
        }
            SwingUtilities.invokeLater(this::handleAction);

    }

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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            new ScanProductWindow(action).setVisible(true);
        });
    }

    public static String scanProduct() {
        try {
            return ProductScanner.getProductID();
        } catch (Exception e) {
            System.err.println("Failed to scan product: " + e.getMessage());
            return null;
        }
    }

    public Boolean isValidUID(String UID) {
        return UID != null && UID.length() >= 8;
    }

    private void handleAction() {
        Product scannedProduct = ProductManagement.getProductFromInventory(productUID);
        dispose();
        switch (action) {
            case "Add":
                if (scannedProduct != null) {
                    JOptionPane.showMessageDialog(null, "Product Already Exists", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    dispose();
                    return;
                }
                new AddProductWindow(productUID).setVisible(true);
                dispose();
                break;
            case "Remove":
                if (scannedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Product Does not Exist", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    dispose();
                    return;
                }
                new RemoveProductWindow(productUID).setVisible(true);
                dispose();
                break;
            case "Update":
                if (scannedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Product Does not Exist", "Info", JOptionPane.INFORMATION_MESSAGE);
                    new ManageInventoryWindow().setVisible(true);
                    dispose();
                    return;
                }
                new UpdateProductWindow(productUID).setVisible(true);
                dispose();
                break;
        }
    }

}
