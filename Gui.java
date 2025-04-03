import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {
    private JTextField nameField, ortField, sterneField, zimmerField;
    private JButton speichernButton;

    public Gui() {
        setTitle("Hotel Verwaltung");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Hotel Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Ort:"));
        ortField = new JTextField();
        add(ortField);

        add(new JLabel("Sterne:"));
        sterneField = new JTextField();
        add(sterneField);

        add(new JLabel("Anzahl Zimmer:"));
        zimmerField = new JTextField();
        add(zimmerField);

        speichernButton = new JButton("Speichern");
        add(speichernButton);

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String ort = ortField.getText();
                int sterne = Integer.parseInt(sterneField.getText());
                int anzZimmer = Integer.parseInt(zimmerField.getText());

                Hotel hotel = new Hotel(1, name, ort, sterne, anzZimmer);
                JOptionPane.showMessageDialog(null, "Hotel gespeichert:\n" + hotel.getName());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Gui();
    }
}
