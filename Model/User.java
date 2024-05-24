package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String password;
                       //TimeStamp //obj
    private static TreeMap<Long, Mensagem> exchangedMenssages = new TreeMap<>();


    public User(String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', username='" + username + "'}";
    }
}
