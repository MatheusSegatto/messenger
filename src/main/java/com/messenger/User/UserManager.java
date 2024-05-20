package com.messenger.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "users.json";
    private Gson gson = new Gson();

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

    private void loadUsers() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userMapType = new TypeToken<HashMap<String, User>>() {
            }.getType();
            users = gson.fromJson(reader, userMapType);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado. Um novo será criado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
