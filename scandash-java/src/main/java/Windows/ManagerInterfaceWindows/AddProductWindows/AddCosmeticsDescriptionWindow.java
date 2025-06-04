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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DatabaseConfig.DBConnection;
import ProductManagement.CosmeticsProduct;

import ProductManagement.ProductManagement;
import Windows.ManagerInterfaceWindows.ManageInventoryWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;

public class AddCosmeticsDescriptionWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField brandField;
    private JTextField ingredientsField;
    JButton addDescriptionbtn;

    /**
     * Launch the application.
     */

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddCosmeticsDescriptionWindow frame = new AddCosmeticsDescriptionWindow(null);
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
    public AddCosmeticsDescriptionWindow(CosmeticsProduct cosmeticsProduct) {
        // Start a thread to connect to the database when the window is launched
        new Thread(() -> ProductManagement.con = DBConnection.connectToDB()).start();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(AddCosmeticsDescriptionWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });

        setTitle("Add Cosmetic Product");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());
        setResizable(false); // Disable resizing
        setSize(450, 300); // Set window size

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getSize().width / 2, screenSize.height / 2 - getSize().height / 2);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100)); // Dark blue color
        topPanel.setBounds(0, 0, 450, 40);
        contentPane.add(topPanel);
        topPanel.setLayout(null);

        JLabel managerLabel = new JLabel("Cosmetics");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Tahoma", Font.BOLD, 20)); // Bigger and bold font
        managerLabel.setBounds(20, 0, 200, 40); // Position at top left
        topPanel.add(managerLabel);

        addDescriptionbtn = new JButton("Add Description");
        addDescriptionbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEmptyFields()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!validateDescriptions()) {
                    return;
                }

                setDescription(cosmeticsProduct);
                if (ProductManagement.addCosmeticsDescription(cosmeticsProduct)) {
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
        contentPane.setLayout(null);
        addDescriptionbtn.setBounds(150,170, 140, 21);
        addDescriptionbtn.setBackground(new Color(0,180,0));
        addDescriptionbtn.setForeground(Color.black);
        contentPane.add(addDescriptionbtn);

        brandField = new JTextField();
        brandField.setBounds(200, 75, 96, 19);
        contentPane.add(brandField);
        brandField.setColumns(10);

        ingredientsField = new JTextField();
        ingredientsField.setBounds(200, 120, 96, 19);
        contentPane.add(ingredientsField);
        ingredientsField.setColumns(10);

        JLabel brandlbl = new JLabel("Brand:");
        brandlbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        brandlbl.setBounds(100, 75, 80, 20);
        contentPane.add(brandlbl);

        JLabel ingredientslbl = new JLabel("Ingredients:");
        ingredientslbl.setFont(new Font("Helvetica", Font.PLAIN, 12));
        ingredientslbl.setBounds(100, 120, 100, 20);
        contentPane.add(ingredientslbl);
    }

    public Boolean isEmptyFields() {
        return brandField.getText().isEmpty() || ingredientsField.getText().isEmpty();
    }

    public Boolean validateDescriptions() {
        String brand = brandField.getText();
        String ingredients = ingredientsField.getText();

        // Validate brand
        if (brand == null || brand.trim().isEmpty() || brand.matches("^\\d+$") || !brand.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter a valid brand name", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Validate ingredients
        if (ingredients == null || ingredients.trim().isEmpty() || ingredients.matches("^\\d+$")
                || !ingredients.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter valid ingredients", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void setDescription(CosmeticsProduct product) {
        String brand = brandField.getText();
        String ingredients = ingredientsField.getText();

        product.setBrand(brand);
        product.setIngredients(ingredients);
    }
}
