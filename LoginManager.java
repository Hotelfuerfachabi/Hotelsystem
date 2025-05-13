import java.io.*;
import java.util.HashMap;

public class LoginManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String USER_DATA_FILE = "users.ser";

    private HashMap<String, String> users; // Stores username-password pairs

    public LoginManager() {
        loadUsers();
    }

    public boolean createUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean deleteUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            saveUsers();
            return true;
        }
        return false;
    }

    public String[] getAllUsers() {
        return users.keySet().toArray(new String[0]); // Return all usernames as an array
    }

    public boolean editUser(String username, String newPassword) {
        if (!users.containsKey(username)) {
            return false; // User does not exist
        }
        users.put(username, newPassword);
        saveUsers();
        return true;
    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (HashMap<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            users = new HashMap<>(); // Initialize empty user list if file not found
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
