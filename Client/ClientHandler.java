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

    private static String userName;
    private static String userNameDestino;

    public static boolean changePassword(String currentPassword, String newPassword) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8000/changePassword"); // URL do servidor
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String credentials = userName + "," + currentPassword + "," + newPassword;

            System.out.println(credentials);

            byte[] outputBytes = credentials.getBytes(StandardCharsets.UTF_8);
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Password changed successfully");
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                System.out.println("Password change failed");
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            System.out.println("Authentication successful");

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

                System.out.println("User: " + newUser.getUsername() + " logged in successfully");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            System.out.println("Authentication failed");
        } else {
            System.out.println("Request failed with response code: " + responseCode);
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

        // Define o userName no cabeçalho da solicitação
        connection.setRequestProperty("userName", userName);


        // Enviando para servidor
        // Escreve o corpo da requisição no fluxo de saída
        // try (DataOutputStream outputStream = new
        // DataOutputStream(connection.getOutputStream())) {
        // outputStream.writeBytes(requestBody);
        // outputStream.flush();
        // }

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

            for (Map.Entry<Long, Mensagem> entry : recevedMessages.entrySet()) {
                Mensagem mensagens = entry.getValue();
                System.out.println("[" + mensagens.getRemetente() + "]: " + mensagens.getContent());

            }
        }else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            //System.out.println("Não há nenhuma mensagem!");
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
    }

    public static String getListOfUsersConected() throws IOException,  ClassNotFoundException {
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
                if (keys.equals(userName)){

                    continue;
                }
                onlineClientsList.append(counter + " - " + keys + "\n");

                counter = counter + 1;
            }

            if (counter == 1){
                return "There is no Online Client!";
            }else{
                return onlineClientsList.toString();
            }

        }else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            
            return "There is no Online Client!";
        } 
        return "Request failed with response code: " + responseCode;
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

    public static User getUserConected() {
        return userConected;
    }

    public static void setUserConected(User userConected) {
        ClientHandler.userName = userConected.getUsername();
        ClientHandler.userConected = userConected;
    }

}
