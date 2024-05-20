package User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "users.ser";

    public UserManager() {
        loadUsers();
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Usuário já existe
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            return false; // Usuário não encontrado
        }
        return user.checkPassword(password);
    }

    public void listUsers() {
        System.out.println("Usuários cadastrados:");
        for (User user : users.values()) {
            System.out.println(user);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado. Um novo será criado.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
