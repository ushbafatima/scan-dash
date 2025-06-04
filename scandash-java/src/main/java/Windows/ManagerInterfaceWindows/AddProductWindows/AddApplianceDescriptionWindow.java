package Windows.ManagerInterfaceWindows.AddProductWindows;

import DatabaseConfig.DBConnection;
import ProductManagement.ApplianceProduct;
import Windows.ManagerInterfaceWindows.ManageInventoryWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;



import ProductManagement.ProductManagement;

public class AddApplianceDescriptionWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField capacityField;
    private JTextField efficiencyField;
    JButton addDescriptionbtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddApplianceDescriptionWindow frame = new AddApplianceDescriptionWindow(null);
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
    public AddApplianceDescriptionWindow(ApplianceProduct applianceProduct) {
        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(AddApplianceDescriptionWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setTitle("Add Appliance Product");
        setResizable(false);
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setBounds(0, 0, 450, 40);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("Appliances");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        managerLabel.setBounds(20, 0, 200, 40);
        topPanel.add(managerLabel);

        addDescriptionbtn = new JButton("Add Description");
        addDescriptionbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEmptyFields()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!validateDescriptions()) {
                    return;
                }

                setDescription(applianceProduct);
                if (ProductManagement.addApplianceDescription(applianceProduct)) {
                    JOptionPane.showMessageDialog(null, "Product description added successfully.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new ManageInventoryWindow().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add product description.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                dispose();
            }
        });
        addDescriptionbtn.setBounds(150, 170, 140, 21);
        addDescriptionbtn.setBackground(new Color(0, 180, 0));
        addDescriptionbtn.setForeground(Color.black);
        contentPane.add(addDescriptionbtn);


        capacityField = new JTextField();
        capacityField.setBounds(200, 75, 96, 19);
        contentPane.add(capacityField);
        capacityField.setColumns(10);

        efficiencyField = new JTextField();
        efficiencyField.setBounds(200, 120, 96, 19);
        contentPane.add(efficiencyField);
        efficiencyField.setColumns(10);

        JLabel perecnt = new JLabel("%");
        perecnt.setFont(new Font("Helvetica", Font.PLAIN, 12));
        perecnt.setBounds(250, 120, 96, 19);
        contentPane.add(perecnt);

        JLabel capacitylbl = new JLabel("Capacity:");
        capacitylbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        capacitylbl.setBounds(100, 75, 80, 20);
        contentPane.add(capacitylbl);

        JLabel efficiencylbl = new JLabel("Efficiency Rate:");
        efficiencylbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        efficiencylbl.setBounds(100, 120, 100, 20);
        contentPane.add(efficiencylbl);
        JLabel percentlbl = new JLabel("%");
        percentlbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        percentlbl.setBounds(300, 120, 100, 20);
        contentPane.add(percentlbl);

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 450;
        int height = 300;
        setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
    }

   public Boolean isEmptyFields() {
        return capacityField.getText().isEmpty() || efficiencyField.getText().isEmpty();
    }

   public Boolean validateDescriptions() {
        try {
            double capacity = Double.parseDouble(capacityField.getText());
            if (capacity < 0) {
                JOptionPane.showMessageDialog(null, "Capacity must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid capacity value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double efficiencyRate = Double.parseDouble(efficiencyField.getText());
            if (efficiencyRate < 0 || efficiencyRate > 100) {
                JOptionPane.showMessageDialog(null, "Efficiency rate must be a number between 0 and 100", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid efficiency rate value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setDescription(ApplianceProduct applianceProduct) {
        double capacity = Double.parseDouble(capacityField.getText());
        double efficiencyRate = Double.parseDouble(efficiencyField.getText());

        applianceProduct.setCapacity(capacity);
        applianceProduct.setEfficiencyRate(efficiencyRate);
    }
}
