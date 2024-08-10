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
    public static void chatMessage(String userNameDestinatario, int option) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String destinatario = userNameDestinatario;
        String message;

        do {
            System.out.print("Enter a message (type '--EXIT--' to quit): ");
            message = scanner.nextLine();
            if (!message.equals("--EXIT--")) {
                if (option == 1) {
                    //Parte 1: Nesse caso preciso verificar se esse nome de usuário existe
                    sendMessage(message, destinatario, "sendMessage");

                } else if (option == 2) {
                    sendMessage(message, destinatario, "sendMessageToALL");
                }
            }
        } while (!message.equals("--EXIT--"));
    }

    private static void sendMessage(String message, String destinatario, String type)
            throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        URL url = new URL("http://localhost:8000/" + type);

        HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();

        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-Type", "application/json; utf-8");

        // Criando o objeto mensagem
        Mensagem newMensage = new Mensagem(ClientHandler.getConectedUsername(), destinatario, message);

        try {
            String mensagemFormated = dataTools.objToString(newMensage);
            try (OutputStream os = postConnection.getOutputStream()) {
                byte[] input = mensagemFormated.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } catch (IOException e) {
            System.out.println("[CLIENT]: Error trying to send a Message");
        }

        int responseCode = postConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            ControllerArquive.addMessageToBeWriten(newMensage);
            BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            //Parte 2: Criar no Server a capacidade de armazenar mensagens de clientes OFFLINE
            System.out.println("[SERVER DENIED]: Message wasn´t delivered because this client is not online!");
        }else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            System.out.println("[SERVER DENIED]: O usuário de destino não está cadastrado em nosso sistema!");
        }
    }

}
