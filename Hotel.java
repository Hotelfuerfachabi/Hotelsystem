import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private int hotelID;
    private String name;
    private String ort;
    private int sterne;
    private int anzZimmer;
    private List<Zimmer> zimmerListe = new ArrayList<>();

    public Hotel(int hotelID, String name, String ort, int sterne, int anzZimmer) {
        this.hotelID = hotelID;
        this.name = name;
        this.ort = ort;
        this.sterne = sterne;
        this.anzZimmer = anzZimmer;
    }

    public int getHotelID() { return hotelID; }
    public String getName() { return name; }
    public String getOrt() { return ort; }
    public int getSterne() { return sterne; }
    public int getAnzZimmer() { return anzZimmer; }

    public void setName(String name) { this.name = name; }
    public void setOrt(String ort) { this.ort = ort; }
    public void setSterne(int sterne) { this.sterne = sterne; }
    public void setAnzZimmer(int anz) { this.anzZimmer = anz; }

    public float berechneAuslastung() {
        int belegteZimmer = 0;
        for (Zimmer z : zimmerListe) {
            if (!z.isVerfuegbar()) belegteZimmer++;
        }
        return (float) belegteZimmer / anzZimmer * 100;
    }
}

    public float berechneAuslastung(){
        int belegteZimmer = 0;
        for (Zimmer z : zimmerListe) {
            if (!z.isVerfuegbar()) belegteZimmer++;
        }
        return (float) belegteZimmer / anzZimmer * 100;
    }
