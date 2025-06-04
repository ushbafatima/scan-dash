package Windows.CustomerInterfaceWindows.ViewProductWindows;

import ProductManagement.FreshGrocery;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ViewFreshGroceryDescriptionWindow extends JFrame{

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField expiryDateDD;
    private JTextField expiryDateMM;
    private JTextField expiryDateYY;
    private JTextField manufactureDateDD;
    private JTextField manufactureDateMM;
    private JTextField manufactureDateYY;
    private JLabel weightlbl;
    private JTextField weightField;
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewFreshGroceryDescriptionWindow frame = new ViewFreshGroceryDescriptionWindow(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Create the frame.
     */
    public ViewFreshGroceryDescriptionWindow(FreshGrocery freshGroceryItem) {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(ViewFreshGroceryDescriptionWindow.this, "Do you want to quit Scan Dash?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });
        setSize( 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setTitle("View Fresh Grocery Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 450, 40);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("Fresh Groceries");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 20)); // Bigger and bold font
        managerLabel.setBounds(20, 0, 200, 40); // Position at top left
        topPanel.add(managerLabel);

        JLabel expiryDatelbl = new JLabel("Expiry Date:");
        expiryDatelbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        expiryDatelbl.setBounds(90, 70, 80, 20);
        contentPane.add(expiryDatelbl);

        JLabel manufactureDatelbl = new JLabel("Manufacture Date:");
        manufactureDatelbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        manufactureDatelbl.setBounds(90, 110, 100, 20);
        contentPane.add(manufactureDatelbl);

        expiryDateDD = new JTextField();
        expiryDateDD.setBounds(200, 70, 30, 19);
        contentPane.add(expiryDateDD);
        expiryDateDD.setColumns(10);

        JButton closebtn = new JButton("Close");
        closebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        closebtn.setBounds(205, 180, 85, 21);
        closebtn.setBackground(Color.red);
        closebtn.setForeground(Color.black);
        contentPane.add(closebtn);

        expiryDateMM = new JTextField();
        expiryDateMM.setColumns(10);
        expiryDateMM.setBounds(240, 70, 30, 19);
        contentPane.add(expiryDateMM);

        expiryDateYY = new JTextField();
        expiryDateYY.setColumns(10);
        expiryDateYY.setBounds(280, 70, 45, 19);
        contentPane.add(expiryDateYY);

        manufactureDateDD = new JTextField();
        manufactureDateDD.setColumns(10);
        manufactureDateDD.setBounds(200, 110, 30, 19);
        contentPane.add(manufactureDateDD);

        manufactureDateMM = new JTextField();
        manufactureDateMM.setColumns(10);
        manufactureDateMM.setBounds(240, 110, 30, 19);
        contentPane.add(manufactureDateMM);

        manufactureDateYY = new JTextField();
        manufactureDateYY.setColumns(10);
        manufactureDateYY.setBounds(280, 110, 45, 19);
        contentPane.add(manufactureDateYY);

        weightlbl = new JLabel("Weight:");
        weightlbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        weightlbl.setBounds(90, 150, 100, 20);
        contentPane.add(weightlbl);

        weightField = new JTextField();
        weightField.setBounds(200, 150, 96, 19);
        contentPane.add(weightField);
        weightField.setColumns(10);

        setDescription(freshGroceryItem );

        // Set all fields non-editable
        expiryDateDD.setEditable(false);
        expiryDateMM.setEditable(false);
        expiryDateYY.setEditable(false);
        manufactureDateDD.setEditable(false);
        manufactureDateMM.setEditable(false);
        manufactureDateYY.setEditable(false);
        weightField.setEditable(false);


    }
    public void setDescription(FreshGrocery product) {
        // Set expiry date text
        LocalDate expiryDate = product.getExpiryDate();
        if (expiryDate != null) {
            int day = expiryDate.getDayOfMonth();
            int month = expiryDate.getMonthValue();
            int year = expiryDate.getYear();

            // Set the date fields
            expiryDateDD.setText(String.valueOf(day));  // Day
            expiryDateMM.setText(String.valueOf(month));  // Month
            expiryDateYY.setText(String.valueOf(year));  // Year
        }


        // Set manufacture date text
        LocalDate manufactureDate = product.getManufactureDate();
        if (manufactureDate != null) {
            int day = manufactureDate.getDayOfMonth();
            int month = manufactureDate.getMonthValue();
            int year = manufactureDate.getYear();

            // Set the date fields
            manufactureDateDD.setText(String.valueOf(day));  // Day
            manufactureDateMM.setText(String.valueOf(month));  // Month
            manufactureDateYY.setText(String.valueOf(year));  // Year
        }


        weightField.setText(String.valueOf(product.getWeight()));


    }

}
