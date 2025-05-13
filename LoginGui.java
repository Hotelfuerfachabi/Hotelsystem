import javax.swing.*;
import java.awt.*;

public class LoginGui extends JFrame {
    private LoginManager loginManager;

    public LoginGui() {
        loginManager = new LoginManager();
        setTitle("Hotel-System-Management by Rafael and Jonas - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add padding around the layout

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Willkommen im Hotel-System-Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // Adjusted grid for additional buttons
        JLabel usernameLabel = new JLabel("Benutzername:");
        JLabel passwordLabel = new JLabel("Passwort:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Anmelden");
        JButton createUserButton = new JButton("Benutzer erstellen");
        JButton deleteUserButton = new JButton("Benutzer löschen");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(createUserButton);
        loginPanel.add(deleteUserButton);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        // Action Listener for Login Button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (loginManager.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login erfolgreich!");
                dispose(); // Close login window
                SwingUtilities.invokeLater(() -> new HotelGui()); // Open main GUI
            } else {
                JOptionPane.showMessageDialog(this, "Ungültiger Benutzername oder Passwort.");
            }
        });

        // Action Listener for Create User Button
        createUserButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Benutzername:");
            String password = JOptionPane.showInputDialog(this, "Passwort:");
            if (username != null && password != null && loginManager.createUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Benutzer erfolgreich erstellt!");
            } else {
                JOptionPane.showMessageDialog(this, "Benutzername existiert bereits oder Eingabe ungültig.");
            }
        });

        // Action Listener for Delete User Button
        deleteUserButton.addActionListener(e -> {
            String[] users = loginManager.getAllUsers();
            if (users.length == 0) {
                JOptionPane.showMessageDialog(this, "Keine Benutzer vorhanden, die gelöscht werden können.");
                return;
            }

            JComboBox<String> userDropdown = new JComboBox<>(users);
            JPasswordField passwordFieldDelete = new JPasswordField();
            Object[] inputs = {"Benutzer auswählen:", userDropdown, "Passwort eingeben:", passwordFieldDelete};

            int option = JOptionPane.showConfirmDialog(this, inputs, "Benutzer löschen", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String selectedUser = (String) userDropdown.getSelectedItem();
                String password = new String(passwordFieldDelete.getPassword());

                if (loginManager.authenticate(selectedUser, password)) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie den Benutzer \"" + selectedUser + "\" wirklich löschen?", "Benutzer löschen", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (loginManager.deleteUser(selectedUser)) {
                            JOptionPane.showMessageDialog(this, "Benutzer erfolgreich gelöscht!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Benutzers.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Ungültiges Passwort.");
                }
            }
        });

        setVisible(true); // Ensure the window is visible
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGui::new); // Launch LoginGui
    }
}
