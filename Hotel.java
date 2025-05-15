import java.io.Serializable;

public class Hotel implements Serializable {
    private static final long serialVersionUID = 1L; //fügt eine eindeutige ID für die Serialisierung hinzu

    private int id;
    private String name;
    private String ort;
    private int sterne;
    private int anzZimmer;

    public Hotel(int id, String name, String ort, int sterne, int anzZimmer) {
        this.id = id;
        this.name = name;
        this.ort = ort;
        this.sterne = sterne;
        this.anzZimmer = anzZimmer;
    }

    // Getter und setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getSterne() {
        return sterne;
    }

    public void setSterne(int sterne) {
        this.sterne = sterne;
    }

    public int getAnzZimmer() {
        return anzZimmer;
    }

    public void setAnzZimmer(int anzZimmer) {
        this.anzZimmer = anzZimmer;
    }
}