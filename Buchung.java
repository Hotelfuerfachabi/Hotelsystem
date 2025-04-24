public class Buchung {
    private int buchungsnummer;
    private Datum datum;
    private int anzNächte;
    private float gesPreis;
    private String status;
    private String zahlungsmethode;
    private boolean genehmigt; // Neues Feld zur Anzeige des Genehmigungsstatus

    public Buchung(int buchungsnummer, Datum datum, int anzNächte, float gesPreis, String status, String zahlungsmethode) {
        this.buchungsnummer = buchungsnummer;
        this.datum = datum;
        this.anzNächte = anzNächte;
        this.gesPreis = gesPreis;
        this.status = status;
        this.zahlungsmethode = zahlungsmethode;
        this.genehmigt = false;  // Standardmäßig nicht genehmigt
    }

    public int getBuchungsnummer() { return buchungsnummer; }
    public Datum getDatum() { return datum; }
    public int getAnzNächte() { return anzNächte; }
    public float getGesPreis() { return gesPreis; }
    public String getStatus() { return status; }
    public String getZahlungsmethode() { return zahlungsmethode; }
    public boolean isGenehmigt() { return genehmigt; }

    public void setZahlungsmethode(String methode) { this.zahlungsmethode = methode; }
    public void setStatus(String status) { this.status = status; }

    // Methode zur Genehmigung der Buchung
    public void genehmigeBuchung() {
        this.genehmigt = true;
        this.status = "Genehmigt";
    }

    // Methode zum Ablehnen der Buchung
    public void lehneBuchungAb() {
        this.genehmigt = false;
        this.status = "Abgelehnt";
    }

    public float berechneGesamtpreis() {
        return gesPreis * anzNächte;
    }
}
