import javax.swing.*;
import java.awt.*;

public class LoginGui extends JFrame {
    private LoginManager loginManager;

    public LoginGui() {
        loginManager = new LoginManager();
        setTitle("Hotel-System-Management by Rafael und Jonas - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add padding around the layout

        // Header Panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Willkommen im Hotel-System-Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(headerLabel);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Benutzername:");
        JLabel passwordLabel = new JLabel("Passwort:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Anmelden");
        JButton createUserButton = new JButton("Benutzer erstellen");
        JButton deleteUserButton = new JButton("Benutzer löschen");

        // Add components to the login panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        loginPanel.add(createUserButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(deleteUserButton, gbc);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        JLabel footerLabel = new JLabel("© 2023 Hotel-System-Management");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

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
            try {
                String[] users = loginManager.getAllUsers();
                if (users.length > 0) {
                    // Prompt for the first user's password
                    JPasswordField passwordFieldPrompt = new JPasswordField();
                    Object[] inputs = {"Passwort des ersten Benutzers eingeben:", passwordFieldPrompt};
                    int option = JOptionPane.showConfirmDialog(this, inputs, "Benutzer erstellen", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String firstUser = users[0];
                        String password = new String(passwordFieldPrompt.getPassword());
                        if (!loginManager.authenticate(firstUser, password)) {
                            JOptionPane.showMessageDialog(this, "Ungültiges Passwort. Benutzer kann nicht erstellt werden.");
                            return;
                        }
                    } else {
                        return; // Cancel user creation
                    }
                }

                // Proceed with user creation
                String username = JOptionPane.showInputDialog(this, "Benutzername:");
                String password = JOptionPane.showInputDialog(this, "Passwort:");
                if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                    if (loginManager.createUser(username, password)) {
                        JOptionPane.showMessageDialog(this, "Benutzer erfolgreich erstellt!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Benutzername existiert bereits.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Benutzername und Passwort dürfen nicht leer sein.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ein Fehler ist aufgetreten: " + ex.getMessage());
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
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginGui(); // Launch LoginGui
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
