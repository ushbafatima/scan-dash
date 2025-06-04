package Windows.CustomerInterfaceWindows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import CardManagement.Card;
import CardManagement.CardManagement;
import UserManagement.CustomerAuthentication;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;

import static CardManagement.CardScanner.getCardID;

public class PayBillWindow extends JFrame {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PayBillWindow frame = new PayBillWindow();
                frame.setVisible(true);
            }
        });
    }

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static String cardUID;

    public PayBillWindow() {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(PayBillWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 500, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setTitle("Bill Payment");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 500, 60);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel billLabel = new JLabel("BILL PAYMENT");
        billLabel.setForeground(Color.WHITE);
        billLabel.setFont(new Font("Tahoma", Font.BOLD, 24)); // Bigger and bold font
        billLabel.setBounds(20, 10, 200, 40); // Position at top left
        topPanel.add(billLabel);

        JLabel ScanProductIcon = new JLabel("");
        ScanProductIcon.setBounds(61, 112, 363, 182);
        ScanProductIcon.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Images/scanCard.gif"));
        ScanProductIcon.setIcon(icon1);
        contentPane.add(ScanProductIcon);

        JLabel lblScanYourCredit = new JLabel("Scan Your Store Card to Pay the Bill");
        lblScanYourCredit.setHorizontalAlignment(SwingConstants.CENTER);
        lblScanYourCredit.setForeground(Color.WHITE); // Change text color to blue
        lblScanYourCredit.setBounds(110, 70, 277, 25);
        contentPane.add(lblScanYourCredit);

        centerWindow() ;
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

        if (cardID == null || cardID.length() < 8) {
            showErrorAndRestart("Invalid Card. Please try again.");
            return;
        }

        // Display "Thanks for shopping" message
        JOptionPane.showMessageDialog(null, "Thanks for shopping!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Dispose the current window
        dispose();
        new FirstWindow().setVisible(true);
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
            PayBillWindow newWindow = new PayBillWindow();
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

    private void centerWindow() {
        // Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Calculate the position of the window to be centered
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        // Set the window to be positioned at the calculated coordinates
        setLocation(x, y);
    }
}
