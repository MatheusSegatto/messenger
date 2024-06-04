package Server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import Model.User;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "users.ser";
    private static UserManager instance;

    public UserManager() {
        loadUsers();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Usuário já existe
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public boolean removeUser(String username) {
        if (users.containsKey(username)) {
            users.remove(username);
            saveUsers();
            return true;
        }
        return false;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);

        if (user != null && user.checkPassword(password)) {
            return user;
        }

        return null;
    }

    public void listUsers() {
        System.out.println("Usuários cadastrados:");
        for (User user : users.values()) {
            System.out.println(user);
        }
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = users.get(username);

        if (user != null && user.checkPassword(currentPassword)) {
            user.setPassword(newPassword);
            saveUsers();
            return true;
        }
        return false;
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
