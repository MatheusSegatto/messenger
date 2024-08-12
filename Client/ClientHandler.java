package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import Util.dataTools;
import Model.Mensagem;
import Model.User;

//Classe Estatica

public class ClientHandler {
    private static User userConected;

    public static boolean deleteAccount() {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8000/deleteAccount"); // URL do servidor
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String username = userConected.getUsername();

            byte[] outputBytes = username.getBytes(StandardCharsets.UTF_8);
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ControllerArquive.deleteUserFile();
                ClientHandler.logOutUser();
                return true;
            } 
        } catch (IOException e) {
            System.out.println("[CLIENT]: Error trying to comunicate with Server Side!");
            
        }
        return false;
    }

    public static boolean createAccount(String username, String password) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8000/createAccount"); // URL do servidor
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String credentials = username + "," + password;

            byte[] outputBytes = credentials.getBytes(StandardCharsets.UTF_8);
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } 
        } catch (IOException e) {
            System.out.println("[CLIENT]: Error trying to comunicate with Server Side!");
        }
        return false;
    }

    public static boolean changePassword(String currentPassword, String newPassword) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8000/changePassword"); // URL do servidor
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String credentials = userConected.getUsername() + "," + currentPassword + "," + newPassword;

            byte[] outputBytes = credentials.getBytes(StandardCharsets.UTF_8);
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } 
        } catch (IOException e) {
            System.out.println("[CLIENT]: Error trying to comunicate with Server Side!");
        }
        return false;

    }

    public static boolean authenticate(String username, String password) throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/authenticate"); // URL do servidor
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String credentials = username + "," + password;

        byte[] outputBytes = credentials.getBytes(StandardCharsets.UTF_8);
        OutputStream os = connection.getOutputStream();
        os.write(outputBytes);
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String requestBody = response.toString();
            try {
                User newUser = (User) dataTools.stringToObj(requestBody);
                setUserConected(newUser);
                return true;

            } catch (Exception e) {
                System.out.println("[CLIENT]: Error trying to comunicate with Server Side!");
            }

        } 
        return false;
    }

    public static void stablishConection() throws IOException {

        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/stablishConection"); // URL do servidor
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = userConected.getUsername().getBytes(StandardCharsets.UTF_8);
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
                new Thread(() -> {
                    while (userConected != null) {
                        try {
                            pingServer();
                            Thread.sleep(2000);
                        } catch (IOException | InterruptedException e) {

                            System.out.println("[CLIENT]: Error when was trying to execute a Thread!");
                        }
                    }
                }).start();
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                System.out.println("Esse login já esta em uso no momento!");
                ClientHandler.logOutUser();
            }

        } catch (Exception e) {
            System.out.println("[CLIENT]: Error trying to comunicate with Server Side!");
        }

    }

    private static void pingServer() throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/ping");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Define o userName no cabeçalho da solicitação
        connection.setRequestProperty("userName", userConected.getUsername());

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            TreeMap<Long, Mensagem> recevedMessages = dataTools.deserializeStringToTreeMap(response.toString());
            System.out.println(recevedMessages.toString());

            for (Map.Entry<Long, Mensagem> entry : recevedMessages.entrySet()) {
                Mensagem mensagens = entry.getValue();
                System.out.println(mensagens.toString());
                ControllerArquive.addMessageToBeWriten(mensagens);
                // Thread.sleep(3000);
            }
        }
    }

    public static String getListOfUsersConected() throws IOException, ClassNotFoundException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/getOnlineClients");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Define o userName no cabeçalho da solicitação

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            StringBuilder onlineClientsList = new StringBuilder();
            onlineClientsList.append("Online Clint List\n");
            int counter = 1;

            @SuppressWarnings("unchecked")
            HashMap<String, Long> onlineClientes = (HashMap<String, Long>) dataTools.stringToObj(response.toString());

            for (Map.Entry<String, Long> entry : onlineClientes.entrySet()) {
                String keys = entry.getKey();
                if (keys.equals(userConected.getUsername())) {

                    continue;
                }
                onlineClientsList.append(counter + " - " + keys + "\n");

                counter = counter + 1;
            }

            if (counter == 1) {
                return "There is no Online Client!";
            } else {
                return onlineClientsList.toString();
            }

        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {

            return "There is no Online Client!";
        }
        return "Request failed with response code: " + responseCode;
    }

    public static User getUserConnected() {
        return userConected;
    }

    public static void setUserConected(User user) {
        userConected = user;

    }

    public static String getConectedUsername() {
        return userConected.getUsername();
    }

    public static void logOutUser() {
        userConected = null;
    }

    public static boolean isConected() {
        return userConected != null;
    }

}
