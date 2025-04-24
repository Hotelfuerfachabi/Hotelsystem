import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DatenLaden {
    public static ArrayList<Gast> ladeGaeste() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hotelDaten.ser"))) {
            ArrayList<Gast> gaeste = (ArrayList<Gast>) in.readObject();
            return gaeste;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<Zimmer> ladeZimmer() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hotelDaten.ser"))) {
            in.readObject(); // Die Gäste-Daten überspringen
            ArrayList<Zimmer> zimmer = (ArrayList<Zimmer>) in.readObject();
            return zimmer;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<Buchung> ladeBuchungen() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hotelDaten.ser"))) {
            in.readObject(); // Die Gäste-Daten überspringen
            in.readObject(); // Die Zimmer-Daten überspringen
            ArrayList<Buchung> buchungen = (ArrayList<Buchung>) in.readObject();
            return buchungen;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
