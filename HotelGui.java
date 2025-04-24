import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class HotelGui extends JFrame {
    private ArrayList<Gast> gaesteListe = new ArrayList<>();
    private ArrayList<Zimmer> zimmerListe = new ArrayList<>();
    private ArrayList<Buchung> buchungsListe = new ArrayList<>();

    private DefaultTableModel gaesteModel, zimmerModel, buchungModel, genehmigteBuchungenModel, abgelehnteBuchungenModel;
    private JTable gaesteTabelle, zimmerTabelle, buchungTabelle, genehmigteBuchungenTabelle, abgelehnteBuchungenTabelle;

    public HotelGui() {
        setTitle("Hotel Verwaltung");
        setSize(900, 600);
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
        JButton reinigenZimmerButton = new JButton("Zimmer reinigen");
        zimmerTabelle = new JTable();
        zimmerModel = new DefaultTableModel(new String[]{"Nr.", "Kategorie", "Preis", "Verfügbar", "Gereinigt"}, 0);
        zimmerTabelle.setModel(zimmerModel);
        zimmerPanel.add(new JScrollPane(zimmerTabelle), BorderLayout.CENTER);
        JPanel zimmerButtonPanel = new JPanel();
        zimmerButtonPanel.add(addZimmerButton);
        zimmerButtonPanel.add(reinigenZimmerButton);
        zimmerPanel.add(zimmerButtonPanel, BorderLayout.SOUTH);
        addZimmerButton.addActionListener(e -> zimmerHinzufuegen());
        reinigenZimmerButton.addActionListener(e -> reinigenZimmer());

        // Buchungen Panel
        JPanel buchungPanel = new JPanel(new BorderLayout());
        JButton addBuchungButton = new JButton("Buchung erstellen");
        JButton deleteBuchungButton = new JButton("Buchung löschen");
        JButton genehmigenButton = new JButton("Genehmigen");
        JButton ablehnenButton = new JButton("Ablehnen");

        buchungTabelle = new JTable();
        buchungModel = new DefaultTableModel(new String[]{"Buchungsnr.", "Gast", "Zimmer", "Nächte", "Preis", "Status"}, 0);
        buchungTabelle.setModel(buchungModel);
        buchungPanel.add(new JScrollPane(buchungTabelle), BorderLayout.CENTER);

        JPanel buchungButtonPanel = new JPanel();
        buchungButtonPanel.add(addBuchungButton);
        buchungButtonPanel.add(deleteBuchungButton);
        buchungButtonPanel.add(genehmigenButton);
        buchungButtonPanel.add(ablehnenButton);
        buchungPanel.add(buchungButtonPanel, BorderLayout.SOUTH);

        addBuchungButton.addActionListener(e -> buchungErstellen());
        deleteBuchungButton.addActionListener(e -> buchungLoeschen());
        genehmigenButton.addActionListener(e -> buchungStatusAendern("Genehmigt"));
        ablehnenButton.addActionListener(e -> buchungStatusAendern("Abgelehnt"));

        // Genehmigte Buchungen Panel
        JPanel genehmigtePanel = new JPanel(new BorderLayout());
        genehmigteBuchungenTabelle = new JTable();
        genehmigteBuchungenModel = new DefaultTableModel(new String[]{"Buchungsnr.", "Datum", "Zimmer", "Nächte", "Preis", "Status", "Löschen"}, 0);
        genehmigteBuchungenTabelle.setModel(genehmigteBuchungenModel);
        genehmigtePanel.add(new JScrollPane(genehmigteBuchungenTabelle), BorderLayout.CENTER);
        JButton deleteGenehmigteButton = new JButton("Buchung löschen");
        deleteGenehmigteButton.addActionListener(e -> buchungLoeschenGenehmigtAbgelehnt("Genehmigt"));
        genehmigtePanel.add(deleteGenehmigteButton, BorderLayout.SOUTH);

        // Abgelehnte Buchungen Panel
        JPanel abgelehntePanel = new JPanel(new BorderLayout());
        abgelehnteBuchungenTabelle = new JTable();
        abgelehnteBuchungenModel = new DefaultTableModel(new String[]{"Buchungsnr.", "Datum", "Zimmer", "Nächte", "Preis", "Status", "Löschen"}, 0);
        abgelehnteBuchungenTabelle.setModel(abgelehnteBuchungenModel);
        abgelehntePanel.add(new JScrollPane(abgelehnteBuchungenTabelle), BorderLayout.CENTER);
        JButton deleteAbgelehnteButton = new JButton("Buchung löschen");
        deleteAbgelehnteButton.addActionListener(e -> buchungLoeschenGenehmigtAbgelehnt("Abgelehnt"));
        abgelehntePanel.add(deleteAbgelehnteButton, BorderLayout.SOUTH);

        // Tabs
        tabbedPane.addTab("Gäste", gaestePanel);
        tabbedPane.addTab("Zimmer", zimmerPanel);
        tabbedPane.addTab("Buchungen", buchungPanel);
        tabbedPane.addTab("Genehmigte Buchungen", genehmigtePanel);
        tabbedPane.addTab("Abgelehnte Buchungen", abgelehntePanel);

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
            Gast gast = new Gast(id, nameField.getText(), Integer.parseInt(alterField.getText()), emailField.getText(), "N/A");
            gaesteListe.add(gast);
            gaesteModel.addRow(new Object[]{id, gast.getName(), gast.getAlter(), gast.getEmail()});
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
        JTextField preisField = new JTextField();
        JComboBox<String> kategorieBox = new JComboBox<>(new String[]{"Suite", "Deluxe", "Standard"});

        Object[] fields = {
                "Zimmernummer:", nummerField,
                "Kategorie:", kategorieBox,
                "Preis pro Nacht:", preisField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Zimmer hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int nummer = Integer.parseInt(nummerField.getText());
            float preis = Float.parseFloat(preisField.getText());
            String kategorie = (String) kategorieBox.getSelectedItem();

            Zimmer zimmer = new Zimmer(nummer, true, preis, kategorie);
            zimmerListe.add(zimmer);
            zimmerModel.addRow(new Object[]{nummer, kategorie, preis, "Ja", "Nein"});
        }
    }

    private void reinigenZimmer() {
        int selectedRow = zimmerTabelle.getSelectedRow();
        if (selectedRow != -1) {
            zimmerModel.setValueAt("Ja", selectedRow, 4);
        } else {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Zimmer aus!");
        }
    }

    private void buchungErstellen() {
        if (gaesteListe.isEmpty() || zimmerListe.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte zuerst Gäste und Zimmer hinzufügen!");
            return;
        }

        JComboBox<String> gastBox = new JComboBox<>(gaesteListe.stream().map(Gast::getName).toArray(String[]::new));
        JComboBox<String> zimmerBox = new JComboBox<>(zimmerListe.stream().map(z -> z.getNummer() + " (" + z.getKategorie() + ")").toArray(String[]::new));
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

            Gast g = gaesteListe.get(gastIndex);
            Zimmer z = zimmerListe.get(zimmerIndex);
            float preis = z.getPPNacht() * naechte;

            // Buchung erstellen
            Buchung buchung = new Buchung(buchungsListe.size() + 1, new Datum(new Date()), naechte, preis, "Offen", "Kreditkarte");

            // Gast und Zimmer zuweisen
            buchung.setGast(g);
            buchung.setZimmer(z);

            // Buchung zur Liste hinzufügen und Tabelle aktualisieren
            buchungsListe.add(buchung);
            buchungModel.addRow(new Object[]{
                    buchung.getBuchungsnummer(),
                    buchung.getGast().getName(),  // Hier wird der Name des Gastes angezeigt
                    buchung.getZimmer().getNummer(),
                    buchung.getAnzNächte(),
                    buchung.getGesPreis(),
                    buchung.getStatus()
            });
        }
    }

    private void buchungLoeschen() {
        int selectedRow = buchungTabelle.getSelectedRow();
        if (selectedRow != -1) {
            int buchungsnummer = (int) buchungModel.getValueAt(selectedRow, 0);
            buchungsListe.removeIf(b -> b.getBuchungsnummer() == buchungsnummer);
            buchungModel.removeRow(selectedRow);
        }
    }

    private void buchungLoeschenGenehmigtAbgelehnt(String status) {
        JTable tabelle = status.equals("Genehmigt") ? genehmigteBuchungenTabelle : abgelehnteBuchungenTabelle;
        DefaultTableModel model = status.equals("Genehmigt") ? genehmigteBuchungenModel : abgelehnteBuchungenModel;

        int selectedRow = tabelle.getSelectedRow();
        if (selectedRow != -1) {
            int buchungsnummer = (int) model.getValueAt(selectedRow, 0);
            buchungsListe.removeIf(b -> b.getBuchungsnummer() == buchungsnummer);
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie eine Buchung zum Löschen aus!");
        }
    }

    private void buchungStatusAendern(String status) {
        int selectedRow = buchungTabelle.getSelectedRow();
        if (selectedRow != -1) {
            int buchungsnummer = (int) buchungModel.getValueAt(selectedRow, 0);
            Buchung buchung = buchungsListe.stream().filter(b -> b.getBuchungsnummer() == buchungsnummer).findFirst().orElse(null);

            if (buchung != null) {
                buchung.setStatus(status);
                buchungModel.setValueAt(status, selectedRow, 5);

                DefaultTableModel zielModel = status.equals("Genehmigt") ? genehmigteBuchungenModel : abgelehnteBuchungenModel;
                zielModel.addRow(new Object[]{
                        buchung.getBuchungsnummer(), buchung.getDatum().toString(), "---", buchung.getAnzNächte(), buchung.getGesPreis(), status
                });
            }
        }
    }

    public static void main(String[] args) {
        new HotelGui();
    }
}
