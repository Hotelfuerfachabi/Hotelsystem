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
    private final String HOTEL_SAVE_FILE = "selected_hotel.ser"; // Datei für das ausgewählte Hotel

    private Hotel selectedHotel; // Feld für das ausgewählte Hotel

public HotelGui() {
    setTitle("Hotel Verwaltung");
    setSize(1200, 800); // Größere Größe für bessere Platzierung
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Daten zuerst aus Datei laden
    ladeDaten();

    // Hotel auswählen oder erstellen, falls keines geladen ist
    hotelAuswahl();

    if (selectedHotel == null) {
        JOptionPane.showMessageDialog(this, "Kein Hotel ausgewählt. Bitte ein Hotel erstellen oder auswählen.");
        System.exit(0); // Beenden, wenn kein Hotel ausgewählt ist
    }

    JTabbedPane tabbedPane = new JTabbedPane();

    // Hotel-Panel
    JPanel hotelPanel = new JPanel(new BorderLayout(10, 10)); // Padding hinzugefügt
    JPanel hotelFormPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Abstand zwischen Komponenten hinzugefügt
    JPanel hotelButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Zentrierte Buttons mit Abstand

    JLabel nameLabel = new JLabel("Name:");
    JLabel ortLabel = new JLabel("Ort:");
    JLabel sterneLabel = new JLabel("Sterne:");
    JLabel anzZimmerLabel = new JLabel("Anzahl Zimmer:");
    JTextField nameField = new JTextField(selectedHotel.getName());
    JTextField ortField = new JTextField(selectedHotel.getOrt());
    JTextField sterneField = new JTextField(String.valueOf(selectedHotel.getSterne()));
    JTextField anzZimmerField = new JTextField(String.valueOf(selectedHotel.getAnzZimmer()));
    JButton saveHotelButton = new JButton("Speichern");
    JButton createHotelButton = new JButton("Hotel erstellen");
    JButton deleteHotelButton = new JButton("Hotel löschen");
    JButton loadHotelDataButton = new JButton("Daten laden");

    hotelFormPanel.add(nameLabel);
    hotelFormPanel.add(nameField);
    hotelFormPanel.add(ortLabel);
    hotelFormPanel.add(ortField);
    hotelFormPanel.add(sterneLabel);
    hotelFormPanel.add(sterneField);
    hotelFormPanel.add(anzZimmerLabel);
    hotelFormPanel.add(anzZimmerField);

    hotelButtonPanel.add(createHotelButton);
    hotelButtonPanel.add(deleteHotelButton);
    hotelButtonPanel.add(saveHotelButton);
    hotelButtonPanel.add(loadHotelDataButton);

    hotelPanel.add(hotelFormPanel, BorderLayout.CENTER);
    hotelPanel.add(hotelButtonPanel, BorderLayout.SOUTH);

    JComboBox<String> hotelDropdown = new JComboBox<>();
    updateHotelDropdown(hotelDropdown);

    hotelDropdown.addActionListener(e -> {
        String selectedHotelName = (String) hotelDropdown.getSelectedItem();
        if (selectedHotelName != null) {
            for (Hotel hotel : getAvailableHotels()) {
                if (hotel.getName().equals(selectedHotelName)) {
                    selectedHotel = hotel;

                    // Lädt die die Daten des ausgewählten Hotels
                    loadHotelData(selectedHotel);

                    // Aktualisiert die Textfelder mit den geladenen Daten
                    nameField.setText(selectedHotel.getName());
                    ortField.setText(selectedHotel.getOrt());
                    sterneField.setText(String.valueOf(selectedHotel.getSterne()));
                    anzZimmerField.setText(String.valueOf(selectedHotel.getAnzZimmer()));

                    // Leert die Tabellen
                    clearTableData();

                    // Aktualiesiert die tabelle mit den geladenen Daten
                    datenInTabelleLaden();

                    JOptionPane.showMessageDialog(this, "Daten für das Hotel \"" + selectedHotel.getName() + "\" wurden geladen.");
                    break;
                }
            }
        }
    });

    createHotelButton.addActionListener(e -> {
        JTextField newNameField = new JTextField();
        JTextField newOrtField = new JTextField();
        JTextField newSterneField = new JTextField();
        JTextField newAnzZimmerField = new JTextField();
        Object[] inputs = {
            "Hotel Name:", newNameField,
            "Ort:", newOrtField,
            "Sterne:", newSterneField,
            "Anzahl Zimmer:", newAnzZimmerField
        };

        int option = JOptionPane.showConfirmDialog(null, inputs, "Neues Hotel erstellen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ArrayList<Hotel> hotels = getAvailableHotels();
            Hotel newHotel = new Hotel(hotels.size() + 1, newNameField.getText(), newOrtField.getText(),
                    Integer.parseInt(newSterneField.getText()), Integer.parseInt(newAnzZimmerField.getText()));
            hotels.add(newHotel);
            saveAvailableHotels(hotels);

            // Aktualisiere dropdown menu
            updateHotelDropdown(hotelDropdown);
            JOptionPane.showMessageDialog(this, "Neues Hotel erstellt!");
        }
    });

    deleteHotelButton.addActionListener(e -> {
        if (selectedHotel == null) {
            JOptionPane.showMessageDialog(this, "Kein Hotel ausgewählt, das gelöscht werden kann.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie das aktuelle Hotel wirklich löschen?", "Hotel löschen", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ArrayList<Hotel> hotels = getAvailableHotels();
            hotels.removeIf(hotel -> hotel.getId() == selectedHotel.getId()); // Entferne das Hotel mit der ID des ausgewählten Hotels
            saveAvailableHotels(hotels);

            selectedHotel = null; // Entferne die Referenz auf das gelöschte Hotel
            updateHotelDropdown(hotelDropdown); // Aktualisiere das Dropdown-Menü

            JOptionPane.showMessageDialog(this, "Hotel gelöscht. Bitte ein neues Hotel auswählen oder erstellen.");
        }
    });

    saveHotelButton.addActionListener(e -> {
        if (selectedHotel == null) {
            JOptionPane.showMessageDialog(this, "Kein Hotel ausgewählt, das bearbeitet werden kann.");
            return;
        }

        selectedHotel.setName(nameField.getText());
        selectedHotel.setOrt(ortField.getText());
        selectedHotel.setSterne(Integer.parseInt(sterneField.getText()));
        selectedHotel.setAnzZimmer(Integer.parseInt(anzZimmerField.getText()));

        // Save the updated hotel
        saveSelectedHotel();
        JOptionPane.showMessageDialog(this, "Hotelinformationen gespeichert!");
    });

    JPanel hotelDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Abstand hinzugefügt
    hotelDropdownPanel.add(new JLabel("Hotel auswählen:"));
    hotelDropdownPanel.add(hotelDropdown);
    hotelPanel.add(hotelDropdownPanel, BorderLayout.NORTH);

    tabbedPane.addTab("Hotel", hotelPanel);

        // Gäste-Panel
        JPanel gaestePanel = new JPanel(new BorderLayout(10, 10)); // Padding hinzugefügt
        gaesteModel = new DefaultTableModel(new String[]{"ID", "Name", "Alter", "E-Mail", "Telefon"}, 0);
        gaesteTabelle = new JTable(gaesteModel);
        JButton addGastButton = new JButton("Gast hinzufügen");
        JButton deleteGastButton = new JButton("Gast löschen");
        JPanel gastBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Zentrierte Buttons mit Abstand
        gastBtnPanel.add(addGastButton);
        gastBtnPanel.add(deleteGastButton);
        gaestePanel.add(new JScrollPane(gaesteTabelle), BorderLayout.CENTER);
        gaestePanel.add(gastBtnPanel, BorderLayout.SOUTH);

        addGastButton.addActionListener(e -> gastHinzufuegen());

        // Zimmer-Panel
        JPanel zimmerPanel = new JPanel(new BorderLayout(10, 10)); // Padding hinzugefügt
        zimmerModel = new DefaultTableModel(new String[]{"Nr.", "Kategorie", "Preis", "Verfügbar", "Gereinigt"}, 0);
        zimmerTabelle = new JTable(zimmerModel);
        JButton addZimmerButton = new JButton("Zimmer hinzufügen");
        JButton reinigenZimmerButton = new JButton("Zimmer reinigen");
        JButton deleteZimmerButton = new JButton("Zimmer löschen");
        JButton adjustCategoryPriceButton = new JButton("Kategoriepreis anpassen"); // Neuer Button für Kategoriepreise

        addZimmerButton.addActionListener(e -> zimmerHinzufuegen());
        reinigenZimmerButton.addActionListener(e -> reinigenZimmer());
        deleteZimmerButton.addActionListener(e -> zimmerLoeschen());
        adjustCategoryPriceButton.addActionListener(e -> kategoriePreisAnpassen()); // ActionListener für Kategoriepreis

        JPanel zimmerBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Zentrierte Buttons mit Abstand
        zimmerBtnPanel.add(addZimmerButton);
        zimmerBtnPanel.add(reinigenZimmerButton);
        zimmerBtnPanel.add(deleteZimmerButton);
        zimmerBtnPanel.add(adjustCategoryPriceButton); // Neuen Button zum Panel hinzufügen
        zimmerPanel.add(new JScrollPane(zimmerTabelle), BorderLayout.CENTER);
        zimmerPanel.add(zimmerBtnPanel, BorderLayout.SOUTH);

        // Buchung-Panel
        JPanel buchungPanel = new JPanel(new BorderLayout(10, 10)); // Padding hinzugefügt
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
        JPanel buchungBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Zentrierte Buttons mit Abstand
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

private void hotelAuswahl() {
        if (selectedHotel != null) {
            // Wenn ein Hotel bereits ausgewählt ist, werden die Daten geladen
            return;
        }

        JTextField nameField = new JTextField();
        JTextField ortField = new JTextField();
        JTextField sterneField = new JTextField();
        JTextField anzZimmerField = new JTextField();
        Object[] inputs = {
            "Hotel Name:", nameField,
            "Ort:", ortField,
            "Sterne:", sterneField,
            "Anzahl Zimmer:", anzZimmerField
        };

     int option = JOptionPane.showConfirmDialog(null, inputs, "Hotel auswählen oder erstellen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            selectedHotel = new Hotel(1, nameField.getText(), ortField.getText(), Integer.parseInt(sterneField.getText()), Integer.parseInt(anzZimmerField.getText()));

            // Save the selected hotel
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HOTEL_SAVE_FILE))) {
                oos.writeObject(selectedHotel);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Hotels: " + e.getMessage());
            }
        } else {
            System.exit(0); // Die Anwendung wird beendet, wenn kein Hotel ausgewählt wird
        }
    }

private void updateHotelDropdown(JComboBox<String> hotelDropdown) {
    hotelDropdown.removeAllItems();
    for (Hotel hotel : getAvailableHotels()) {
        hotelDropdown.addItem(hotel.getName());
    }
}

private void saveSelectedHotel() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HOTEL_SAVE_FILE))) {
        oos.writeObject(selectedHotel);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Hotels: " + e.getMessage());
    }
}

private ArrayList<Hotel> getAvailableHotels() {
    ArrayList<Hotel> hotels = new ArrayList<>();
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("hotels.ser"))) {
        hotels = (ArrayList<Hotel>) ois.readObject();
    } catch (Exception e) {
        hotels = new ArrayList<>(); // Gibt eine leere Liste zurück, wenn keine Hotels gefunden werden
    }
    return hotels;
}

private void saveAvailableHotels(ArrayList<Hotel> hotels) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("hotels.ser"))) {
        oos.writeObject(hotels);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Hotels: " + e.getMessage());
    }
}

private void loadHotelData(Hotel hotel) {
    String hotelDataFile = "hotel_" + hotel.getId() + "_data.ser"; // Einzigartige Datei für jedes Hotel
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(hotelDataFile))) {
        gaesteListe = (ArrayList<Gast>) ois.readObject();
        zimmerListe = (ArrayList<Zimmer>) ois.readObject();
        buchungsListe = (ArrayList<Buchung>) ois.readObject();
    } catch (Exception e) {
        gaesteListe = new ArrayList<>();
        zimmerListe = new ArrayList<>();
        buchungsListe = new ArrayList<>();
        JOptionPane.showMessageDialog(this, "Keine gespeicherten Daten für das Hotel \"" + hotel.getName() + "\" gefunden.");
    }
}

private void saveHotelData(Hotel hotel) {
    String hotelDataFile = "hotel_" + hotel.getId() + "_data.ser"; // Einzigartige Datei für jedes Hotel
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(hotelDataFile))) {
        oos.writeObject(gaesteListe);
        oos.writeObject(zimmerListe);
        oos.writeObject(new ArrayList<>(buchungsListe)); // Stellt sicher, dass alle Objekte in buchungsListe serialisiert werden
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Hoteldaten: " + e.getMessage());
    }
}

private void clearTableData() {
    // Entfernt alle Zeilen aus der Tabelle
    gaesteModel.setRowCount(0);
    zimmerModel.setRowCount(0);
    buchungModel.setRowCount(0);
    genehmigteBuchungenModel.setRowCount(0);
    abgelehnteBuchungenModel.setRowCount(0);
}

private void gastHinzufuegen() {
    if (selectedHotel == null) {
        JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Hotel aus, bevor Sie einen Gast hinzufügen.");
        return;
    }

    JTextField name = new JTextField();
    JTextField alter = new JTextField();
    JTextField email = new JTextField();
    JTextField telefon = new JTextField(); // Stellt sicher, dass alle Felder initialisiert sind
    telefon.setEditable(true); // Erlaubt die Bearbeitung des Telefonfeldes

    Object[] inputs = {"Name:", name, "Alter:", alter, "E-Mail:", email, "Telefon:", telefon};

    if (JOptionPane.showConfirmDialog(null, inputs, "Neuer Gast", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        try {
            String gastName = name.getText().trim();
            int gastAlter = Integer.parseInt(alter.getText().trim());
            String gastEmail = email.getText().trim();
            String gastTelefon = telefon.getText().trim();

            if (gastName.isEmpty() || gastEmail.isEmpty() || gastTelefon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.");
                return;
            }

            if (!gastTelefon.matches("\\+?[0-9\\- ]+")) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Telefonnummer ein.");
                return;
            }

            Gast g = new Gast(gaesteListe.size() + 1, gastName, gastAlter, gastEmail, gastTelefon);
            gaesteListe.add(g);
            gaesteModel.addRow(new Object[]{g.getGastID(), g.getName(), g.getAlter(), g.getEmail(), g.getTelefon()});
            JOptionPane.showMessageDialog(this, "Gast erfolgreich hinzugefügt.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie ein gültiges Alter ein.");
        }
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

    private void zimmerLoeschen() {
        int row = zimmerTabelle.getSelectedRow();
        if (row != -1) {
            int zimmerNummer = (int) zimmerModel.getValueAt(row, 0); // Erhält die Zimmernummer
            zimmerListe.removeIf(z -> z.getNummer() == zimmerNummer); // entfernt das Zimmer aus der Liste
            zimmerModel.removeRow(row); // Entfernt die Zeile aus der Tabelle
            JOptionPane.showMessageDialog(this, "Zimmer gelöscht.");
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Zimmer aus, das gelöscht werden soll.");
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
        if (selectedHotel != null) {
            saveHotelData(selectedHotel); // Speichert die Daten des ausgewählten Hotels
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gaesteListe);
            oos.writeObject(zimmerListe);
            oos.writeObject(new ArrayList<>(buchungsListe)); // Stellt sicher, dass alle Objekte in buchungsListe initialisiert werden
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der allgemeinen Daten: " + e.getMessage());
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
            System.out.println("Keine allgemeinen Daten gefunden. Initialisiere leere Listen.");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HOTEL_SAVE_FILE))) {
            selectedHotel = (Hotel) ois.readObject();
            if (selectedHotel != null) {
                loadHotelData(selectedHotel); // Lädt die Daten des ausgewählten Hotels
            } else {
                System.out.println("Kein gespeichertes Hotel gefunden.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Keine gespeicherten Hotelinformationen gefunden. Bitte ein neues Hotel erstellen.");
            selectedHotel = null;
        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Hotelinformationen: " + e.getMessage());
            selectedHotel = null;
        }
    }

    private void kategoriePreisAnpassen() {
        JComboBox<String> kategorieBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        JTextField newPriceField = new JTextField();
        Object[] inputs = {"Kategorie auswählen:", kategorieBox, "Neuer Preis:", newPriceField};

        int option = JOptionPane.showConfirmDialog(null, inputs, "Kategoriepreis anpassen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String selectedKategorie = (String) kategorieBox.getSelectedItem();
                float newPrice = Float.parseFloat(newPriceField.getText().trim());

                for (Zimmer z : zimmerListe) {
                    if (z.getKategorie().equals(selectedKategorie)) {
                        z.setPPNacht(newPrice); // Aktualisiert den Preis des Zimmers
                    }
                }

                // Aktualisiert die Tabelle
                for (int i = 0; i < zimmerModel.getRowCount(); i++) {
                    if (zimmerModel.getValueAt(i, 1).equals(selectedKategorie)) {
                        zimmerModel.setValueAt(newPrice, i, 2);
                    }
                }

                JOptionPane.showMessageDialog(this, "Preise für Kategorie \"" + selectedKategorie + "\" erfolgreich angepasst.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen gültigen Preis ein.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGui()); // Startet die LoginGui als erstes.
    }
}