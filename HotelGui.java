import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HotelGui extends JFrame {
    private ArrayList<Gast> gaesteListe = new ArrayList<>();
    private ArrayList<Zimmer> zimmerListe = new ArrayList<>();
    private ArrayList<Buchung> buchungsListe = new ArrayList<>();

    private DefaultTableModel gaesteModel, zimmerModel, buchungModel;
    private JTable gaesteTabelle;

    public HotelGui() {
        setTitle("Hotel Verwaltung");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Gäste Panel
        JPanel gaestePanel = new JPanel(new BorderLayout());
        JButton addGastButton = new JButton("Gast hinzufügen");
        JButton deleteGastButton = new JButton("Gast löschen");
        gaesteTabelle = new JTable();
        gaesteModel = new DefaultTableModel(new String[]{"ID", "Name", "Alter", "E-Mail"}, 0);
        gaesteTabelle.setModel(gaesteModel);
        JPanel gastButtonPanel = new JPanel();
        gastButtonPanel.add(addGastButton);
        gastButtonPanel.add(deleteGastButton);
        gaestePanel.add(new JScrollPane(gaesteTabelle), BorderLayout.CENTER);
        gaestePanel.add(gastButtonPanel, BorderLayout.SOUTH);

        addGastButton.addActionListener(e -> gastHinzufuegen());
        deleteGastButton.addActionListener(e -> gastLoeschen());

        // Zimmer Panel
        JPanel zimmerPanel = new JPanel(new BorderLayout());
        JButton addZimmerButton = new JButton("Zimmer hinzufügen");
        JTable zimmerTabelle = new JTable();
        zimmerModel = new DefaultTableModel(new String[]{"Nr.", "Kategorie", "Preis", "Verfügbar"}, 0);
        zimmerTabelle.setModel(zimmerModel);
        zimmerPanel.add(new JScrollPane(zimmerTabelle), BorderLayout.CENTER);
        zimmerPanel.add(addZimmerButton, BorderLayout.SOUTH);
        addZimmerButton.addActionListener(e -> zimmerHinzufuegen());

        // Buchungen Panel
        JPanel buchungPanel = new JPanel(new BorderLayout());
        JButton addBuchungButton = new JButton("Buchung erstellen");
        JTable buchungTabelle = new JTable();
        buchungModel = new DefaultTableModel(new String[]{"Buchungsnr.", "Gast", "Zimmer", "Nächte", "Preis"}, 0);
        buchungTabelle.setModel(buchungModel);
        buchungPanel.add(new JScrollPane(buchungTabelle), BorderLayout.CENTER);
        buchungPanel.add(addBuchungButton, BorderLayout.SOUTH);
        addBuchungButton.addActionListener(e -> buchungErstellen());

        tabbedPane.addTab("Gäste", gaestePanel);
        tabbedPane.addTab("Zimmer", zimmerPanel);
        tabbedPane.addTab("Buchungen", buchungPanel);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void gastHinzufuegen() {
        JTextField nameField = new JTextField();
        JTextField alterField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Alter:", alterField,
                "E-Mail:", emailField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Gast hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = gaesteListe.size() + 1;
            int alter = Integer.parseInt(alterField.getText());
            String email = emailField.getText();
            Gast gast = new Gast(id, nameField.getText(), alter, email, "N/A");
            gaesteListe.add(gast);
            gaesteModel.addRow(new Object[]{id, nameField.getText(), alter, email});
        }
    }

    private void gastLoeschen() {
        int selectedRow = gaesteTabelle.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) gaesteModel.getValueAt(selectedRow, 0);
            gaesteListe.removeIf(g -> g.getGastID() == id);
            gaesteModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Gast zum Löschen aus!");
        }
    }

    private void zimmerHinzufuegen() {
        JTextField nummerField = new JTextField();
        JTextField kategorieField = new JTextField();
        JTextField preisField = new JTextField();

        Object[] fields = {
                "Zimmernummer:", nummerField,
                "Kategorie:", kategorieField,
                "Preis pro Nacht:", preisField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Zimmer hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int nummer = Integer.parseInt(nummerField.getText());
            float preis = Float.parseFloat(preisField.getText());
            Zimmer zimmer = new Zimmer(nummer, true, preis, kategorieField.getText());
            zimmerListe.add(zimmer);
            zimmerModel.addRow(new Object[]{nummer, kategorieField.getText(), preis, "Ja"});
        }
    }

    private void buchungErstellen() {
        if (gaesteListe.isEmpty() || zimmerListe.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Es müssen mindestens ein Gast und ein Zimmer existieren!");
            return;
        }

        String[] gaesteNamen = gaesteListe.stream().map(Gast::getName).toArray(String[]::new);
        String[] zimmerNummern = zimmerListe.stream().map(z -> "Zimmer " + z.getNummer()).toArray(String[]::new);

        JComboBox<String> gastBox = new JComboBox<>(gaesteNamen);
        JComboBox<String> zimmerBox = new JComboBox<>(zimmerNummern);
        JTextField naechteField = new JTextField();

        Object[] fields = {
                "Gast:", gastBox,
                "Zimmer:", zimmerBox,
                "Nächte:", naechteField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Buchung erstellen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int gastIndex = gastBox.getSelectedIndex();
            int zimmerIndex = zimmerBox.getSelectedIndex();
            int naechte = Integer.parseInt(naechteField.getText());

            Gast gast = gaesteListe.get(gastIndex);
            Zimmer zimmer = zimmerListe.get(zimmerIndex);
            float gesamtPreis = zimmer.getPPNacht() * naechte;

            Buchung buchung = new Buchung(buchungsListe.size() + 1, new Datum(new java.util.Date()), naechte, gesamtPreis, "Bestätigt", "Kreditkarte");
            buchungsListe.add(buchung);
            buchungModel.addRow(new Object[]{buchung.getBuchungsnummer(), gast.getName(), zimmer.getNummer(), naechte, gesamtPreis});
        }
    }

    public static void main(String[] args) {
        new HotelGui();
    }
}