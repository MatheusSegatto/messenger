package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import Model.Mensagem;
import Util.dataTools;

public class MessageHandler {
    //Criando o chatzin de teste
    public static void messageSide() throws IOException, InterruptedException{
        Scanner scanner = new Scanner(System.in);
        String message;
        
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
    
    private static void sendMessage(String message) throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/sendMessage");

        HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
        
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-Type", "application/json; utf-8");

        //Criando o objeto mensagem
        Mensagem newMensage = new Mensagem(ClientHandler.getUserName(), ClientHandler.getUserNameDestino(), message);

        // O que falta nessa fun

        try{
            String mensagemFormated = dataTools.objToString(newMensage);
            try (OutputStream os = postConnection.getOutputStream()) {
                byte[] input = mensagemFormated.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }catch(IOException e){
            System.out.println("[CLIENT]: Error Mensagem");
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


            System.out.println("Response from Server: " + response.toString());
        }  
            
    }

}
