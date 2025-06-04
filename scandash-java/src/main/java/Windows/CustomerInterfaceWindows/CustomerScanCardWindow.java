package Windows.CustomerInterfaceWindows;

import CardManagement.CardManagement;
import DatabaseConfig.DBConnection;
import CardManagement.CardScanner;
import UserManagement.CustomerAuthentication;
import CardManagement.Card;
import UserManagement.CustomerManagement;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static CardManagement.CardScanner.getCardID;

public class CustomerScanCardWindow extends JFrame {
    private JPanel contentPane;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                CustomerScanCardWindow frame = new CustomerScanCardWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CustomerScanCardWindow() {
        // Start a thread to connect to the database when the window is launched
        new Thread(() -> CardManagement.conn = DBConnection.connectToDB()).start();

        setUndecorated(true);
        setBounds(500, 200, 500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        setContentPane(contentPane);

        setTitle("Scan Card");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new Dimension(500, 60));
        topPanel.setLayout(null);

        JLabel scanningCardLabel = new JLabel("SCANNING CARD");
        scanningCardLabel.setForeground(Color.WHITE);
        scanningCardLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        scanningCardLabel.setBounds(150, 10, 200, 40);
        topPanel.add(scanningCardLabel);

        // Red close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(460, 10, 30, 30);
        closeButton.addActionListener(e -> {
            dispose(); // Close the current window
            SwingUtilities.invokeLater(() -> {
                FirstWindow firstWindow = new FirstWindow(); // Open the first window
                firstWindow.setVisible(true);
            });
        });
        topPanel.add(closeButton);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel gifPanel = new JPanel();
        gifPanel.setLayout(new BorderLayout());
        JLabel gifLabel = new JLabel("");
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon gifIcon = new ImageIcon(this.getClass().getResource("/Images/scanCard.gif"));
        gifLabel.setIcon(gifIcon);
        gifPanel.add(gifLabel, BorderLayout.CENTER);
        contentPane.add(gifPanel, BorderLayout.CENTER);

        // Use a separate thread for card scanning logic
        new Thread(this::processCard).start();
    }

    private void processCard() {
        String cardID = null;
        try {
            cardID = scanCard(); // Fetch card ID
        } catch (Exception e) {
            showErrorAndRestart("Scanning failed: " + e.getMessage());
            return;
        }

        if (cardID == null || !CustomerAuthentication.isValidCardID(cardID)) {
            showErrorAndRestart("Invalid CardID. Please try again.");
            return;
        }

        try {
            Card card = CardManagement.getCardFromDB(cardID); // Fetch card from database
            if (!CustomerAuthentication.cardFound(card)) {
                showErrorAndRestart("Card Not Found. Restarting scanning...");
                return;
            }
            if (!CustomerAuthentication.isCardActive(card)) {
                showErrorAndRestart("Card is not active. Restarting scanning...");
                return;
            }
            // Authentication successful
            String finalCardID = cardID;
            SwingUtilities.invokeLater(() -> {
                dispose();
                CustomerCardPinWindow newWindow = new CustomerCardPinWindow(finalCardID);
                newWindow.setVisible(true);
            });
        } catch (SQLException e) {
            showErrorAndRestart("Database Error: " + e.getMessage());
        } catch (Exception e) {
            showErrorAndRestart("Unexpected Error: " + e.getMessage());
        }
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
                Thread.sleep(500); // Add a short delay to ensure resources are released
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            CustomerScanCardWindow newWindow = new CustomerScanCardWindow();
            newWindow.setVisible(true);
        });
    }

    // Simplified method to scan the card
    public static String scanCard() {
        try {
            return getCardID();
        } catch (Exception e) {
            System.err.println("Failed to scan card: " + e.getMessage());
            return null; // Return null if scanning fails
        }
    }
}
