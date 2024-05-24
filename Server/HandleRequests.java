package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import Model.Mensagem;
import Util.dataTools;

public class HandleRequests {

    private static HashMap<String, Long> activeClients = new HashMap<>();
    
    static public class SetClientOnServer implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê os cabeçalhos da requisição
                //exchange.getRequestHeaders().forEach((k, v) -> System.out.println("Header: " + k + " = " + String.join(", ", v)));

                // Lê o corpo da requisição
                InputStream requestBodyStream = exchange.getRequestBody();
                String userNameConected = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                
                Long timeStamp = System.currentTimeMillis();
                activeClients.put(userNameConected, timeStamp);

                String response = "[SERVER] Client Conected Successfully!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static public class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            //verificar se tem alguma mensagem pendente
            Long timeStamp = System.currentTimeMillis();
            // Extrai o sessionID do cabeçalho da solicitação
            String userNameConected = exchange.getRequestHeaders().getFirst("userName");

            activeClients.put(userNameConected, timeStamp);
            
            //Verificar se tem alguma mensagem
            if (MessageManager.checkIfThereIsMessage(userNameConected)){
                TreeMap<Long, Mensagem> messagesReceved = MessageManager.getRecentMessages(userNameConected);
                String response = dataTools.serializeTreeMapToString(messagesReceved);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
            
            // Envia uma resposta de sucesso para o cliente
            String response = "false";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            
        }
    }

    static public class MessageReceiver implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
            
                // Lê o corpo da requisição
                InputStream requestBodyStream = exchange.getRequestBody();
                String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);
                

                try {
                    Mensagem newMessage = (Mensagem) dataTools.stringToObj(requestBody);
                    System.out.println("newMessage: " + newMessage.getContent() + " " + newMessage.getDestinatario() + " " + newMessage.getRemetente());
                    MessageManager.addNewMessage(newMessage);

                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("[SERVER]: Message Error!");
                } 
                



                // Envia uma resposta de sucesso para o cliente
                String response = "[SERVER] Message Receved!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                
            } else {
                // Método não suportado
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

        }

        

        // private String getMessageFromRequest(HttpExchange exchange) throws IOException {
        //     byte[] requestBodyBytes = exchange.getRequestBody().readAllBytes();
        //     return new String(requestBodyBytes);
        // }

        // private void writeMessageToFile(String message) throws IOException {
        //     String caminhoArquivo = ".\\messages.txt";
            
        //     try {
        //         System.out.println("ENTROU AQUI");
        //         // Cria um FileWriter para o arquivo especificado
        //         FileWriter fileWriter = new FileWriter(caminhoArquivo);

        //         // Cria um BufferedWriter para escrever texto de forma eficiente
        //         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //         // Escreve a mensagem no arquivo
        //         bufferedWriter.write(message);
        //         bufferedWriter.newLine(); // Adiciona uma nova linha

        //         // Feche o BufferedWriter
        //         bufferedWriter.close();


        //     } catch (IOException e) {
        //         System.out.println("Ocorreu um erro ao escrever no arquivo.");
        //         e.printStackTrace();
        //     }
        // }
            

        // private void sendResponse(HttpExchange exchange, String response) throws IOException {
        //     exchange.sendResponseHeaders(200, response.getBytes().length);
        //     OutputStream os = exchange.getResponseBody();
        //     os.write(response.getBytes());
        //     os.close();
        // }
    }

    public static void checkClientActivity() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                //System.out.println(activeClients);

                List<String> tempRemoveArray = new ArrayList<>();
                try {
                    for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                        String clientId = entry.getKey();
                        Long lastActiveTime = entry.getValue();
                        if (currentTime - lastActiveTime > 10000) { // Tempo limite de inatividade de 10 segundos
                            System.out.println("Client " + clientId + " disconnected due to inactivity.");
                            tempRemoveArray.add(clientId);
                        }
                    }
                    for(String item : tempRemoveArray){
                        activeClients.remove(item);
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println("Deu Ruim na Thread de [checkClientActivity]");
                    // e.printStackTrace();
                }
            }
        }).start();
    }





}
