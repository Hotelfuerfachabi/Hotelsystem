import java.io.Serializable;

public class Buchung implements Serializable {
    private static final long serialVersionUID = 1L; // Add a unique ID for serialization

    private int buchungsnummer;
    private Datum datum;
    private int anzNächte;
    private float gesPreis;
    private String status;
    private String zahlungsart;
    private Gast gast;
    private Zimmer zimmer;

    // Konstruktor, der alle Felder initialisiert
    public Buchung(int buchungsnummer, Datum datum, int anzNächte, float gesPreis, String status, String zahlungsart) {
        this.buchungsnummer = buchungsnummer;
        this.datum = datum;
        this.anzNächte = anzNächte;
        this.gesPreis = gesPreis;
        this.status = status;
        this.zahlungsart = zahlungsart;
    }

    // Getter und Setter
    public int getBuchungsnummer() {
        return buchungsnummer;
    }

    public Datum getDatum() {
        return datum;
    }

    public int getAnzNächte() {
        return anzNächte;
    }

    public float getGesPreis() {
        return gesPreis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZahlungsart() {
        return zahlungsart;
    }

    public Gast getGast() {
        return gast;
    }

    public void setGast(Gast gast) {
        this.gast = gast;
    }

    public Zimmer getZimmer() {
        return zimmer;
    }

    public void setZimmer(Zimmer zimmer) {
        this.zimmer = zimmer;
    }
}
