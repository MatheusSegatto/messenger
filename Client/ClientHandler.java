package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


//Classe Estatica

public class ClientHandler {
    private static String sessionID;
    private static String userName;
    private static String userNameDestino;

    public static void stablishConection() throws IOException {

        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/stablishConection"); // URL do servidor
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = userName.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Conection established with the server side!: " + response.toString());
            }   

        } catch (Exception e) {
            System.out.println("FAILED: Wasn't possible to stablish a conection to the Server!");
            System.exit(0);
        }

        new Thread(() -> {
            while (true) {
                try {
                    pingServer();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void pingServer() throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/ping");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        // Define o sessionID no cabeçalho da solicitação
        connection.setRequestProperty("userName", userName);
        
        int responseCode = connection.getResponseCode();
    
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
        
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } 
    }

    public static void setSessionID(String sessionID) {
        ClientHandler.sessionID = sessionID;
    }

    
    public static String getSessionID() {
        return sessionID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String username) {
        ClientHandler.userName = username;
    }

    public static String getUserNameDestino() {
        return userNameDestino;
    }

    public static void setUserNameDestino(String userNameDestino) {
        ClientHandler.userNameDestino = userNameDestino;
    }

    

}

