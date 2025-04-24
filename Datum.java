import java.text.SimpleDateFormat;
import java.util.Date;

public class Datum {
    private Date datum;

    public Datum(Date datum) {
        this.datum = datum;
    }

    public Date getDatum() {
        return datum;
    }

    public String getFormatiertesDatum() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(datum);
    }

    @Override
    public String toString() {
        return getFormatiertesDatum();
    }
}
