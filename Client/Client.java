package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.Scanner;

import Server.Mensagem;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String message;
        
        ClientHandler.stablishConection();

        Mensagem teste = new Mensagem("ok", "ok", "ok");



        do {
            System.out.print("Enter a message (type '--EXIT--' to quit): ");
            message = scanner.nextLine();
            if (!message.equals("--EXIT--")) {
                sendMessage(message);
            }
        } while (!message.equals("--EXIT--"));

        scanner.close();
        System.out.println("Client stopped."); 
    }


    // private static void sendMessage(String message) throws IOException, InterruptedException {
    //     HttpClient client = HttpClient.newHttpClient();
    //     HttpRequest request = HttpRequest.newBuilder()
    //         .uri(URI.create("http://localhost:8000/message"))
    //         .header("Content-Type", "text/plain")
    //         .POST(HttpRequest.BodyPublishers.ofString(message))
    //         .build();
    //         connection.setRequestMethod("POST");
    //         connection.setDoOutput(true);
            
    //         // Definir os cabeçalhos HTTP
    //         connection.setRequestProperty("Content-Type", "application/json; utf-8");
    //         connection.setRequestProperty("Accept", "application/json");
    //         connection.setRequestProperty("Origin", "https://example.com");
    //         connection.setRequestProperty("Referer", "https://example.com/page");
        
    //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    //     System.out.println("Response from server: " + response.body());
    // }

    private static void sendMessage(String message) throws IOException, InterruptedException {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8000/sendMessage");

            HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
            
            String sessionID = ClientHandler.getSessionID();

            // Definir os cabeçalhos HTTP
            // postConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            // postConnection.setRequestProperty("Accept", "application/json");
            // postConnection.setRequestProperty("Origin", sessionID);
            // postConnection.setRequestProperty("Referer", "teste");

            postConnection.setRequestMethod("POST");
            postConnection.setDoOutput(true);
            postConnection.setRequestProperty("Content-Type", "application/json; utf-8");


            
            String jsonInputString = "{\"Origem\": \"John\", \"Destino\": 30, \"Conteudo\": 30,}";

            try (OutputStream os = postConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }


            int responseCode = postConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                sessionID = response.toString();

                System.out.println("Response from Server: " + response.toString());
            }  
            
    }
}
 
