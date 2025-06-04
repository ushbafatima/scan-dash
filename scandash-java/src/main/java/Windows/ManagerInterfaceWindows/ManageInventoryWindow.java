package Windows.ManagerInterfaceWindows;

import Windows.ManagerInterfaceWindows.ManagerTasksWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ManageInventoryWindow extends JFrame {

    private JPanel contentPane;
    private JPanel leftPanel; // Blue panel on the left
    private JPanel detailsPanel; // Panel for dynamic content
    private Timer slideTimer; // Timer for sliding animation
    private int slideOffset = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                ManageInventoryWindow frame = new ManageInventoryWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManageInventoryWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 500, 400); // Initial window size
        setTitle("Manage Inventory");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setLocationRelativeTo(null); // Center the window on the screen

        contentPane = new JPanel(null);
        setContentPane(contentPane);

        // Left blue panel
        leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 100)); // Blue background
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setBounds(0, 0, 150, 500); // Fixed width on the left
        leftPanel.setLayout(null);
        contentPane.add(leftPanel);

        // Add "Inventory" label
        JLabel inventoryLabel = new JLabel("Inventory");
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryLabel.setBounds(10, 10, 130, 30); // Positioned at the top-left corner
        leftPanel.add(inventoryLabel);

        // Buttons on the left panel
        JButton addProductBtn = new JButton("Add Product");
        JButton removeProductBtn = new JButton("Remove Product");
        JButton updateProductBtn = new JButton("Update Product");
        JButton backButton = new JButton("Back");

        configureButton(addProductBtn);
        configureButton(removeProductBtn);
        configureButton(updateProductBtn);

        // Configure back button to be red
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        leftPanel.add(addProductBtn);
        leftPanel.add(removeProductBtn);
        leftPanel.add(updateProductBtn);
        leftPanel.add(backButton);

        // Add action listeners to buttons
        addProductBtn.addActionListener(e -> openScanProductWindow("Add"));
        removeProductBtn.addActionListener(e -> openScanProductWindow("Remove"));
        updateProductBtn.addActionListener(e -> openScanProductWindow("Update"));
        backButton.addActionListener(e -> {
            ManagerTasksWindow managerTasksWindow = new ManagerTasksWindow();
            managerTasksWindow.setVisible(true);
            dispose(); // Close this window
        });

        // Resize behavior
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeComponents();
            }
        });

        resizeComponents();
    }

    private void configureButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        leftPanel.setBounds(0, 0, 150, height); // Fixed size on the left

        // Position buttons in leftPanel
        int buttonWidth = 120;
        int buttonHeight = 35;
        int buttonX = (leftPanel.getWidth() - buttonWidth) / 2;

        int spacing = 20; // Spacing between buttons
        int totalHeight = (buttonHeight * 5) + (spacing * 4);

        int startY = (height - totalHeight) / 2 - 50; // Adjust to make buttons higher

        for (int i = 1; i <= leftPanel.getComponentCount(); i++) {
            if (leftPanel.getComponent(i - 1) instanceof JButton) {
                JButton button = (JButton) leftPanel.getComponent(i - 1);
                button.setBounds(buttonX, startY + ((i - 1) * (buttonHeight + spacing)), buttonWidth, buttonHeight);
            }
        }
    }

    // Method to open ScanProductWindow
    private void openScanProductWindow(String action) {
        ScanProductWindow scanProductWindow = new ScanProductWindow(action);
        scanProductWindow.setVisible(true);
        dispose(); // Close this window
    }
    private void slidePanel(boolean slideIn) {
        int start = slideIn ? getWidth() : 150;
        int end = slideIn ? 150 : getWidth();
        slideOffset = start;

        slideTimer = new Timer(10, e -> {
            if ((slideIn && slideOffset > end) || (!slideIn && slideOffset < end)) {
                detailsPanel.setBounds(slideOffset, 0, getWidth() - 150, getHeight());
                slideOffset += slideIn ? -15 : 15;
                detailsPanel.repaint();
            } else {
                slideTimer.stop();
                if (!slideIn) detailsPanel.setVisible(false);
            }
        });
        detailsPanel.setVisible(true);
        slideTimer.start();
    }
}