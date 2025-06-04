package Windows.CustomerInterfaceWindows;

import CartManagement.CustomerCart;
import Windows.CustomerInterfaceWindows.PayBillWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class CustomerBillWindow extends JFrame {

    private JPanel contentPane;

    public CustomerBillWindow(double totalBill) {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(CustomerBillWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });
        setSize(500, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setTitle("Customer Bill");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Add the blue panel with the title label
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new Dimension(400, 60));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(null);

        JLabel titleLabel = new JLabel("YOUR BILL");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(20, 10, 200, 40);
        topPanel.add(titleLabel);

        // Create the bill panel
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new BorderLayout());
        contentPane.add(billPanel, BorderLayout.CENTER);

        JTextArea billText = new JTextArea();
        billText.setForeground(new Color(255, 255, 255));
        billText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        billText.setText("Total Bill: PKR " + String.format("%.2f", totalBill));
        billText.setEditable(false);
        billPanel.add(billText, BorderLayout.CENTER);

        // Create a panel specifically for the Pay Bill button
        JPanel payBillPanel = new JPanel();
        payBillPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the button
        billPanel.add(payBillPanel, BorderLayout.SOUTH);

        JButton payBillbtn = new JButton("Pay Bill");
        payBillbtn.setForeground(SystemColor.desktop);
        payBillbtn.setBackground(SystemColor.activeCaption);
        payBillbtn.setPreferredSize(new Dimension(100, 30)); // Explicitly set button size
        payBillbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (totalBill == 0) {
                    JOptionPane.showMessageDialog(null, "Add items to cart before processing the bill.", "Oops",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                CustomerCart.checkout();
                new PayBillWindow().setVisible(true);
                dispose();
            }
        });
        payBillPanel.add(payBillbtn);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

    }

}
