package Windows.CustomerInterfaceWindows.ViewProductWindows;

import DatabaseConfig.DBConnection;
import ProductManagement.ApplianceProduct;
import ProductManagement.ProductManagement;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ViewApplianceDescriptionWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField capacityField;
    private JTextField efficiencyField;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApplianceProduct applianceProduct= new ApplianceProduct();
                    applianceProduct.setCapacity(2);
                    applianceProduct.setEfficiencyRate(100.0);

                    ViewApplianceDescriptionWindow frame = new ViewApplianceDescriptionWindow(applianceProduct);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewApplianceDescriptionWindow(ApplianceProduct applianceProduct) {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(ViewApplianceDescriptionWindow.this, "Do you want to quit Scan Dash?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        }); setBounds(100, 100, 400, 300);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setTitle("View Appliance Product");
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

        JLabel managerLabel = new JLabel("Appliances");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 20)); // Bigger and bold font
        managerLabel.setBounds(20, 0, 200, 40); // Position at top left
        topPanel.add(managerLabel);

        JButton closebtn = new JButton("Close");
        closebtn.setBounds(180, 170, 85, 21);
        closebtn.setBackground(Color.red);
        closebtn.setForeground(Color.black);
        contentPane.add(closebtn);

        closebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        capacityField = new JTextField();
        capacityField.setBounds(200, 75, 96, 19);
        contentPane.add(capacityField);
        capacityField.setColumns(10);
        capacityField.setEditable(false);


        efficiencyField = new JTextField();
        efficiencyField.setBounds(200, 120, 96, 19);
        contentPane.add(efficiencyField);
        efficiencyField.setColumns(10);
        efficiencyField.setEditable(false);
        efficiencyField.setText(Double.toString(applianceProduct.getEfficiencyRate()));

        JLabel capacitylbl = new JLabel("Capacity:");
        capacitylbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        capacitylbl.setBounds(100, 75, 80, 20);
        contentPane.add(capacitylbl);

        JLabel efficiencylbl = new JLabel("Efficiency Rate:");
        efficiencylbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        efficiencylbl.setBounds(100, 120, 100, 20);
        contentPane.add(efficiencylbl);

        setDescription(applianceProduct);
    }

    public void setDescription(ApplianceProduct applianceProduct ) {
        capacityField.setText(Double.toString(applianceProduct.getCapacity()));
        efficiencyField.setText(Double.toString(applianceProduct.getEfficiencyRate()));
    }

}
