package com.messenger.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Classe Estatica

public class ClientHandler {
    private static String sessionID;

    public static void stablishConection() throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/stablishConection"); // URL do servidor
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

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
                sessionID = response.toString();

                System.out.println("Conection established with the server side!");
            }
        } catch (Exception e) {
            System.out.println("FAILED: Wasn't possible to stablish a conection to the Server!");
            System.exit(0);
        }

        new Thread(() -> {
            while (true) {
                try {
                    pingServer();
                    Thread.sleep(5000);
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
        connection.setRequestProperty("sessionID", sessionID);

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

}
