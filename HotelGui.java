import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class HotelGui extends JFrame {
    private ArrayList<Gast> gaesteListe;
    private ArrayList<Zimmer> zimmerListe;
    private ArrayList<Buchung> buchungsListe;

    private DefaultTableModel gaesteModel, zimmerModel, buchungModel, genehmigteBuchungenModel, abgelehnteBuchungenModel;
    private JTable gaesteTabelle, zimmerTabelle, buchungTabelle, genehmigteBuchungenTabelle, abgelehnteBuchungenTabelle;

    private final String SAVE_FILE = "hotel_data.ser";

    public HotelGui() {
        setTitle("Hotel Verwaltung");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Lade Daten aus Datei
        ladeDaten();

        JTabbedPane tabbedPane = new JTabbedPane();

        // Gäste Panel
        JPanel gaestePanel = new JPanel(new BorderLayout());
        gaesteModel = new DefaultTableModel(new String[]{"ID", "Name", "Alter", "E-Mail", "Telefon"}, 0);
        gaesteTabelle = new JTable(gaesteModel);
        JButton addGastButton = new JButton("Gast hinzufügen");
        JButton deleteGastButton = new JButton("Gast löschen");
        addGastButton.addActionListener(e -> gastHinzufuegen());
        deleteGastButton.addActionListener(e -> gastLoeschen());
        JPanel gastBtnPanel = new JPanel();
        gastBtnPanel.add(addGastButton);
        gastBtnPanel.add(deleteGastButton);
        gaestePanel.add(new JScrollPane(gaesteTabelle), BorderLayout.CENTER);
        gaestePanel.add(gastBtnPanel, BorderLayout.SOUTH);

        // Zimmer Panel
        JPanel zimmerPanel = new JPanel(new BorderLayout());
        zimmerModel = new DefaultTableModel(new String[]{"Nr.", "Kategorie", "Preis", "Verfügbar", "Gereinigt"}, 0);
        zimmerTabelle = new JTable(zimmerModel);
        JButton addZimmerButton = new JButton("Zimmer hinzufügen");
        JButton reinigenZimmerButton = new JButton("Zimmer reinigen");
        addZimmerButton.addActionListener(e -> zimmerHinzufuegen());
        reinigenZimmerButton.addActionListener(e -> reinigenZimmer());
        JPanel zimmerBtnPanel = new JPanel();
        zimmerBtnPanel.add(addZimmerButton);
        zimmerBtnPanel.add(reinigenZimmerButton);
        zimmerPanel.add(new JScrollPane(zimmerTabelle), BorderLayout.CENTER);
        zimmerPanel.add(zimmerBtnPanel, BorderLayout.SOUTH);

        // Buchung Panel
        JPanel buchungPanel = new JPanel(new BorderLayout());
        buchungModel = new DefaultTableModel(new String[]{"Nr.", "Gast", "Zimmer", "Nächte", "Preis", "Status"}, 0);
        buchungTabelle = new JTable(buchungModel);
        JButton addBuchungButton = new JButton("Buchung hinzufügen");
        JButton deleteBuchungButton = new JButton("Buchung löschen");
        JButton genehmigenButton = new JButton("Genehmigen");
        JButton ablehnenButton = new JButton("Ablehnen");
        addBuchungButton.addActionListener(e -> buchungErstellen());
        deleteBuchungButton.addActionListener(e -> buchungLoeschen());
        genehmigenButton.addActionListener(e -> buchungStatusAendern("Genehmigt"));
        ablehnenButton.addActionListener(e -> buchungStatusAendern("Abgelehnt"));
        JPanel buchungBtnPanel = new JPanel();
        buchungBtnPanel.add(addBuchungButton);
        buchungBtnPanel.add(deleteBuchungButton);
        buchungBtnPanel.add(genehmigenButton);
        buchungBtnPanel.add(ablehnenButton);
        buchungPanel.add(new JScrollPane(buchungTabelle), BorderLayout.CENTER);
        buchungPanel.add(buchungBtnPanel, BorderLayout.SOUTH);

        // Genehmigte Buchungen Panel
        JPanel genehmigtePanel = new JPanel(new BorderLayout());
        genehmigteBuchungenModel = new DefaultTableModel(new String[]{"Nr.", "Datum", "Zimmer", "Nächte", "Preis", "Status"}, 0);
        genehmigteBuchungenTabelle = new JTable(genehmigteBuchungenModel);
        JButton deleteGenehmigtButton = new JButton("Löschen");
        deleteGenehmigtButton.addActionListener(e -> buchungLöschen(genehmigteBuchungenTabelle, genehmigteBuchungenModel));
        genehmigtePanel.add(new JScrollPane(genehmigteBuchungenTabelle), BorderLayout.CENTER);
        genehmigtePanel.add(deleteGenehmigtButton, BorderLayout.SOUTH);

        // Abgelehnte Buchungen Panel
        JPanel abgelehntePanel = new JPanel(new BorderLayout());
        abgelehnteBuchungenModel = new DefaultTableModel(new String[]{"Nr.", "Datum", "Zimmer", "Nächte", "Preis", "Status"}, 0);
        abgelehnteBuchungenTabelle = new JTable(abgelehnteBuchungenModel);
        JButton deleteAbgelehntButton = new JButton("Löschen");
        deleteAbgelehntButton.addActionListener(e -> buchungLöschen(abgelehnteBuchungenTabelle, abgelehnteBuchungenModel));
        abgelehntePanel.add(new JScrollPane(abgelehnteBuchungenTabelle), BorderLayout.CENTER);
        abgelehntePanel.add(deleteAbgelehntButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Gäste", gaestePanel);
        tabbedPane.addTab("Zimmer", zimmerPanel);
        tabbedPane.addTab("Buchungen", buchungPanel);
        tabbedPane.addTab("Genehmigte Buchungen", genehmigtePanel);
        tabbedPane.addTab("Abgelehnte Buchungen", abgelehntePanel);

        add(tabbedPane);

        // Fenster schließen => speichern
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                speichereDaten();
            }
        });

        datenInTabelleLaden();
        setVisible(true);
    }

    private void gastHinzufuegen() {
        JTextField name = new JTextField();
        JTextField alter = new JTextField();
        JTextField email = new JTextField();
        JTextField telefon = new JTextField();
        Object[] inputs = {"Name:", name, "Alter:", alter, "E-Mail:", email, "Telefon:", telefon};
        if (JOptionPane.showConfirmDialog(null, inputs, "Neuer Gast", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Gast g = new Gast(gaesteListe.size() + 1, name.getText(), Integer.parseInt(alter.getText()), email.getText(), telefon.getText());
            gaesteListe.add(g);
            gaesteModel.addRow(new Object[]{g.getGastID(), g.getName(), g.getAlter(), g.getEmail(), g.getTelefon()});
        }
    }

    private void gastLoeschen() {
        int row = gaesteTabelle.getSelectedRow();
        if (row != -1) {
            gaesteListe.remove(row);
            gaesteModel.removeRow(row);
        }
    }

    private void zimmerHinzufuegen() {
        JTextField nr = new JTextField();
        JTextField preis = new JTextField();
        JComboBox<String> kategorie = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        Object[] input = {"Zimmernummer:", nr, "Kategorie:", kategorie, "Preis:", preis};
        if (JOptionPane.showConfirmDialog(null, input, "Zimmer hinzufügen", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Zimmer z = new Zimmer(Integer.parseInt(nr.getText()), true, Float.parseFloat(preis.getText()), (String) kategorie.getSelectedItem());
            zimmerListe.add(z);
            zimmerModel.addRow(new Object[]{z.getNummer(), z.getKategorie(), z.getPPNacht(), "Ja", "Nein"});
        }
    }

    private void reinigenZimmer() {
        int row = zimmerTabelle.getSelectedRow();
        if (row != -1) {
            zimmerModel.setValueAt("Ja", row, 4);
        }
    }

    private void buchungErstellen() {
        if (gaesteListe.isEmpty() || zimmerListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte zuerst Gäste und Zimmer hinzufügen!");
            return;
        }

        JComboBox<String> gastBox = new JComboBox<>(gaesteListe.stream().map(Gast::getName).toArray(String[]::new));
        JComboBox<String> zimmerBox = new JComboBox<>(zimmerListe.stream().map(z -> String.valueOf(z.getNummer())).toArray(String[]::new));
        JTextField naechteField = new JTextField();

        Object[] fields = {"Gast:", gastBox, "Zimmer:", zimmerBox, "Nächte:", naechteField};
        if (JOptionPane.showConfirmDialog(null, fields, "Neue Buchung", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            int gIndex = gastBox.getSelectedIndex();
            int zIndex = zimmerBox.getSelectedIndex();
            int naechte = Integer.parseInt(naechteField.getText());
            Zimmer z = zimmerListe.get(zIndex);
            float preis = z.getPPNacht() * naechte;
            Buchung b = new Buchung(buchungsListe.size() + 1, new Datum(new Date()), naechte, preis, "Offen", "Kreditkarte");
            b.setGast(gaesteListe.get(gIndex));
            b.setZimmer(z);
            buchungsListe.add(b);
            buchungModel.addRow(new Object[]{b.getBuchungsnummer(), b.getGast().getName(), b.getZimmer().getNummer(), b.getAnzNächte(), b.getGesPreis(), b.getStatus()});
        }
    }

    private void buchungLoeschen() {
        int row = buchungTabelle.getSelectedRow();
        if (row != -1) {
            int buchungsnummer = (int) buchungModel.getValueAt(row, 0);
            buchungsListe.removeIf(b -> b.getBuchungsnummer() == buchungsnummer);
            buchungModel.removeRow(row);
        }
    }

    private void buchungStatusAendern(String neuerStatus) {
        int row = buchungTabelle.getSelectedRow();
        if (row != -1) {
            int buchungsnummer = (int) buchungModel.getValueAt(row, 0);
            for (Buchung b : buchungsListe) {
                if (b.getBuchungsnummer() == buchungsnummer && b.getStatus().equals("Offen")) {
                    b.setStatus(neuerStatus);
                    buchungModel.setValueAt(neuerStatus, row, 5);
                    DefaultTableModel ziel = neuerStatus.equals("Genehmigt") ? genehmigteBuchungenModel : abgelehnteBuchungenModel;
                    ziel.addRow(new Object[]{b.getBuchungsnummer(), b.getDatum().getFormatiertesDatum(), b.getZimmer().getNummer(), b.getAnzNächte(), b.getGesPreis(), neuerStatus});
                    break;
                }
            }
        }
    }

    private void buchungLöschen(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int buchungsnummer = (int) model.getValueAt(row, 0);
            buchungsListe.removeIf(b -> b.getBuchungsnummer() == buchungsnummer);
            model.removeRow(row);
        }
    }

    private void datenInTabelleLaden() {
        for (Gast g : gaesteListe) {
            gaesteModel.addRow(new Object[]{g.getGastID(), g.getName(), g.getAlter(), g.getEmail(), g.getTelefon()});
        }
        for (Zimmer z : zimmerListe) {
            zimmerModel.addRow(new Object[]{z.getNummer(), z.getKategorie(), z.getPPNacht(), "Ja", "Nein"});
        }
        for (Buchung b : buchungsListe) {
            if (b.getGast() != null && b.getZimmer() != null) {
                Object[] row = new Object[]{b.getBuchungsnummer(), b.getGast().getName(), b.getZimmer().getNummer(), b.getAnzNächte(), b.getGesPreis(), b.getStatus()};
                buchungModel.addRow(row);
                if (b.getStatus().equals("Genehmigt")) {
                    genehmigteBuchungenModel.addRow(new Object[]{b.getBuchungsnummer(), b.getDatum().getFormatiertesDatum(), b.getZimmer().getNummer(), b.getAnzNächte(), b.getGesPreis(), b.getStatus()});
                } else if (b.getStatus().equals("Abgelehnt")) {
                    abgelehnteBuchungenModel.addRow(new Object[]{b.getBuchungsnummer(), b.getDatum().getFormatiertesDatum(), b.getZimmer().getNummer(), b.getAnzNächte(), b.getGesPreis(), b.getStatus()});
                }
            }
        }
    }

    private void speichereDaten() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gaesteListe);
            oos.writeObject(zimmerListe);
            oos.writeObject(buchungsListe);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern: " + e.getMessage());
        }
    }

    private void ladeDaten() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            gaesteListe = (ArrayList<Gast>) ois.readObject();
            zimmerListe = (ArrayList<Zimmer>) ois.readObject();
            buchungsListe = (ArrayList<Buchung>) ois.readObject();
        } catch (Exception e) {
            gaesteListe = new ArrayList<>();
            zimmerListe = new ArrayList<>();
            buchungsListe = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelGui());
    }
}
