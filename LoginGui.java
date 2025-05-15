import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGui extends JFrame {
    private LoginManager loginManager;

    public LoginGui() {
        loginManager = new LoginManager();
        setTitle("Hotel-System-Management by Rafael und Jonas - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // für mehr Platz zwischen den Komponenten

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Moderner blauer Hintergrund
        JLabel headerLabel = new JLabel("Willkommen im Hotel-System-Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE); // Weißer Text für Kontrast
        headerPanel.add(headerLabel);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // für mehr Platz um die Komponenten
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // für mehr Platz zwischen den Komponenten
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Benutzername:");
        JLabel passwordLabel = new JLabel("Passwort:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = createModernButton("Anmelden");
        JButton createUserButton = createModernButton("Benutzer erstellen");
        JButton deleteUserButton = createModernButton("Benutzer löschen");

        // fügt die Komponenten zum Login-Panel hinzu
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
        footerPanel.setBackground(new Color(70, 130, 180)); 
        JLabel footerLabel = new JLabel("© 2023 Hotel-System-Management");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE); 
        footerPanel.add(footerLabel);

        // fügt die Panels zum Haupt-Frame hinzu
        add(headerPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Login Knopf 
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (loginManager.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login erfolgreich!");
                dispose(); // schließt das Login-Fenster
                SwingUtilities.invokeLater(() -> new HotelGui()); // öffnet die HotelGui
            } else {
                JOptionPane.showMessageDialog(this, "Ungültiger Benutzername oder Passwort.");
            }
        });

        // Knopf zum Erstellen eines neuen Benutzers
        createUserButton.addActionListener(e -> {
            try {
                String[] users = loginManager.getAllUsers();
                if (users.length > 0) {
                    // Auswahl des ersten Benutzers
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
                        return; // Abbrechen, wenn der Benutzer nicht fortfahren möchte
                    }
                }

                // Fortsetzung mit der Erstellung eines neuen Benutzers
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

        // Löschknopf
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

        setVisible(true); // Stellt das Fenster sichtbar
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 149, 237)); // Hellblauer Hintergrund
        button.setForeground(Color.WHITE); 
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Abgerundete Ecken
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginGui(); // Startet die Login-GUI
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
