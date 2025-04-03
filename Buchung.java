public class Buchung {
    private int buchungsnummer;
    private Datum datum;
    private int anzNächte;
    private float gesPreis;
    private String status;
    private String zahlungsmethode;

    public Buchung(int buchungsnummer, Datum datum, int anzNächte, float gesPreis, String status, String zahlungsmethode) {
        this.buchungsnummer = buchungsnummer;
        this.datum = datum;
        this.anzNächte = anzNächte;
        this.gesPreis = gesPreis;
        this.status = status;
        this.zahlungsmethode = zahlungsmethode;
    }

    public int getBuchungsnummer() { return buchungsnummer; }
    public Datum getDatum() { return datum; }
    public int getAnzNächte() { return anzNächte; }
    public float getGesPreis() { return gesPreis; }
    public String getStatus() { return status; }
    public String getZahlungsmethode() { return zahlungsmethode; }

    public void setZahlungsmethode(String methode) { this.zahlungsmethode = methode; }
    public void setStatus(String status) { this.status = status; }

    public float berechneGesamtpreis() {
        return gesPreis * anzNächte;
    }
}
