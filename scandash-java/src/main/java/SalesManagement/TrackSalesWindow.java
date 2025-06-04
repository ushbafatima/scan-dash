package SalesManagement;

import Windows.ManagerInterfaceWindows.ManagerTasksWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import SalesManagement.SalesChart;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TrackSalesWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private SalesChart salesChart;
    private Timer updateTimer;
    private JButton backButton;
    private JPanel topPanel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                TrackSalesWindow frame = new TrackSalesWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TrackSalesWindow() {
        setupWindow();
        setupComponents();
        setupUpdateTimer();
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
        setBounds(500, 200, 800, 600);
        setTitle("Track Sales");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        // Set Icon
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());
        setLocation(x,y);
        // Add component listener for handling resize events
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                updateButtonPosition();
            }
        });
    }

    private void setupComponents() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Top panel with title and back button
        topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new Dimension(800, 60));
        topPanel.setLayout(null);
        contentPane.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("TOTAL REVENUE PER DAY");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(20, 10, 400, 40);
        topPanel.add(titleLabel);

        // Add Go Back button
        backButton = new JButton("Go Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);;
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new ManagerTasksWindow().setVisible(true);
            dispose();
        });
        topPanel.add(backButton);

        // Initial button position
        updateButtonPosition();

        // Sales chart
        salesChart = new SalesChart();
        contentPane.add(salesChart, BorderLayout.CENTER);

        // Initial update
        salesChart.updateChart();
    }

    private void updateButtonPosition() {
        if (backButton != null && topPanel != null) {
            int padding = 20;
            int buttonWidth = 100;
            int buttonHeight = 30;
            backButton.setBounds(
                    topPanel.getWidth() - buttonWidth - padding,
                    15,
                    buttonWidth,
                    buttonHeight
            );
            topPanel.revalidate();
            topPanel.repaint();
        }
    }

    private void setupUpdateTimer() {
        // Update chart every 5 seconds
        updateTimer = new Timer(5000, e -> salesChart.updateChart());
        updateTimer.start();
    }

    private void handleWindowClosing() {
        int response = JOptionPane.showConfirmDialog(this,
                "Do you want to quit Scan Dash?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            if (updateTimer != null) {
                updateTimer.stop();
            }
            System.exit(0);
        }
    }
}