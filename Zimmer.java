public class Zimmer {
    private int nummer;
    private boolean verfuegbar;
    private float pPNacht;
    private String kategorie;

    public Zimmer(int nummer, boolean verfuegbar, float pPNacht, String kategorie) {
        this.nummer = nummer;
        this.verfuegbar = verfuegbar;
        this.pPNacht = pPNacht;
        this.kategorie = kategorie;
    }

    public int getNummer() { return nummer; }
    public boolean isVerfuegbar() { return verfuegbar; }
    public float getPPNacht() { return pPNacht; }
    public String getKategorie() { return kategorie; }

    public void setVerfuegbar(boolean verfuegbar) { this.verfuegbar = verfuegbar; }
    public void setPPNacht(float preis) { this.pPNacht = preis; }

    public void reinigenZimmer() {
        System.out.println("Zimmer " + nummer + " wurde gereinigt.");
    }
}
