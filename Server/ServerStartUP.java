package Server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import Util.dataTools;

public class ServerStartUP {

    private static HashMap<String, Long> activeClients = new HashMap<>();
    
    public static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/stablishConection", new SetClientOnServer());
        server.createContext("/ping", new PingHandler());
        server.createContext("/sendMessage", new MessageReceiver());
        server.setExecutor(null);
        server.start();
        checkClientActivity();
    }

    static class SetClientOnServer implements HttpHandler {
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

    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            //verificar se tem alguma mensagem pendente
            Long timeStamp = System.currentTimeMillis();
            // Extrai o sessionID do cabeçalho da solicitação
            String userNameConected = exchange.getRequestHeaders().getFirst("userName");

            activeClients.put(userNameConected, timeStamp);

            //Verificar se tem alguma mensagem
            if (MessageManager.checkIfThereIsMessage(userNameConected)){

            }

            // Envia uma resposta de sucesso para o cliente
            String response = "Pong from server";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MessageReceiver implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê os cabeçalhos da requisição
                //exchange.getRequestHeaders().forEach((k, v) -> System.out.println("Header: " + k + " = " + String.join(", ", v)));

                // Lê o corpo da requisição
                InputStream requestBodyStream = exchange.getRequestBody();
                String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);
                

                try {
                    Mensagem newMessage = (Mensagem) dataTools.stringParaObjeto(requestBody);
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

        private String getMessageFromRequest(HttpExchange exchange) throws IOException {
            byte[] requestBodyBytes = exchange.getRequestBody().readAllBytes();
            return new String(requestBodyBytes);
        }

        private void writeMessageToFile(String message) throws IOException {
            String caminhoArquivo = ".\\messages.txt";
            
            try {
                System.out.println("ENTROU AQUI");
                // Cria um FileWriter para o arquivo especificado
                FileWriter fileWriter = new FileWriter(caminhoArquivo);

                // Cria um BufferedWriter para escrever texto de forma eficiente
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                // Escreve a mensagem no arquivo
                bufferedWriter.write(message);
                bufferedWriter.newLine(); // Adiciona uma nova linha

                // Feche o BufferedWriter
                bufferedWriter.close();


            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao escrever no arquivo.");
                e.printStackTrace();
            }
        }
            

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }




    //OK
    private static void checkClientActivity() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                System.out.println(activeClients);

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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Deu Ruim na Thread de [checkClientActivity]");
                    // e.printStackTrace();
                }
            }
        }).start();
    }
}
