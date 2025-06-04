package Windows.ManagerInterfaceWindows.AddProductWindows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ProductManagement.ElectronicsProduct;
import ProductManagement.ProductManagement;
import Windows.ManagerInterfaceWindows.ManageInventoryWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

public class AddElectronicsDescriptionWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField modelField;
    private JTextField warrantyField;
    JButton addDescriptionbtn;
    private JComboBox<String> periodBox;

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
                    AddElectronicsDescriptionWindow frame = new AddElectronicsDescriptionWindow(null);
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
    public AddElectronicsDescriptionWindow(ElectronicsProduct electronicsProduct) {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        AddElectronicsDescriptionWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setTitle("Add Electronic Product");
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

        JLabel managerLabel = new JLabel("Electronics");
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

                setDescription(electronicsProduct);
                if (ProductManagement.addElectronicsDescription(electronicsProduct)) {
                    JOptionPane.showMessageDialog(null, "Product description added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new ManageInventoryWindow().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add product description.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                dispose();
            }
        });
        addDescriptionbtn.setBounds(150, 170, 140, 21);
        addDescriptionbtn.setBackground(new Color(0, 180, 0));
        addDescriptionbtn.setForeground(Color.black);
        contentPane.add(addDescriptionbtn);


        modelField = new JTextField();
        modelField.setBounds(200, 75, 96, 19);
        contentPane.add(modelField);
        modelField.setColumns(10);

        warrantyField = new JTextField();
        warrantyField.setBounds(200, 120, 60, 19);
        contentPane.add(warrantyField);
        warrantyField.setColumns(10);

        JLabel modellbl = new JLabel("Model:");
        modellbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        modellbl.setBounds(100, 75, 80, 20);
        contentPane.add(modellbl);

        JLabel warrantylbl = new JLabel("Warranty Period:");
        warrantylbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        warrantylbl.setBounds(100, 120, 100, 20);
        contentPane.add(warrantylbl);

        JLabel periodlbl = new JLabel("Months");
        periodlbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        periodlbl.setBounds(270, 120, 90, 19);
        contentPane.add(periodlbl);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 450;
        int height = 300;
        setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
    }
    public Boolean isEmptyFields() {
        return modelField.getText().isEmpty() || warrantyField.getText().isEmpty();
    }

    public Boolean validateDescriptions() {
        String model = modelField.getText();
        String warranty = warrantyField.getText();

        if (model == null || model.trim().isEmpty() || model.matches("^\\d+$") || !model.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter a valid model name", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (warranty == null || !warranty.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Enter a valid warranty period (digits only)", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void setDescription(ElectronicsProduct product) {
        String model = modelField.getText();
        int warrantyPeriod = Integer.parseInt(warrantyField.getText());
        product.setModel(model);
        product.setWarrantyPeriod(warrantyPeriod);
    }
}
