import java.io.Serializable;

public class Zimmer implements Serializable {
    private static final long serialVersionUID = 1L; // Add a unique ID for serialization

    private int nummer;
    private boolean verfuegbar;
    private float pPNacht;
    private String kategorie;
    private boolean gereinigt;  // Neues Attribut

    public Zimmer(int nummer, boolean verfuegbar, float pPNacht, String kategorie) {
        this.nummer = nummer;
        this.verfuegbar = verfuegbar;
        this.pPNacht = pPNacht;
        this.kategorie = kategorie;
        this.gereinigt = false;  // Standardmäßig nicht gereinigt
    }

    public int getNummer() { return nummer; }
    public boolean isVerfuegbar() { return verfuegbar; }
    public float getPPNacht() { return pPNacht; }
    public String getKategorie() { return kategorie; }
    public boolean isGereinigt() { return gereinigt; }  // Getter für gereinigt

    public void setVerfuegbar(boolean verfuegbar) { this.verfuegbar = verfuegbar; }
    public void setPPNacht(float preis) { this.pPNacht = preis; }
    public void reinigenZimmer() {
        this.gereinigt = true;
        System.out.println("Zimmer " + nummer + " wurde gereinigt.");
    }
}