import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DatenSpeichern {
    public static void speichereDaten(ArrayList<Gast> gaeste, ArrayList<Zimmer> zimmer, ArrayList<Buchung> buchungen) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelDaten.ser"))) {
            out.writeObject(gaeste);
            out.writeObject(zimmer);
            out.writeObject(buchungen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
