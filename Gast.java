import java.io.Serializable;

public class Gast implements Serializable {
    private static final long serialVersionUID = 1L;

    private int gastID;
    private String name;
    private int alter;
    private String email;
    private String telefon;

    public Gast(int gastID, String name, int alter, String email, String telefon) {
        this.gastID = gastID;
        this.name = name;
        this.alter = alter;
        this.email = email;
        this.telefon = telefon;
    }

    public int getGastID() {
        return gastID;
    }

    public String getName() {
        return name;
    }

    public int getAlter() {
        return alter;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public boolean validiereEmail() {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    @Override
    public String toString() {
        return name;
    }
}
