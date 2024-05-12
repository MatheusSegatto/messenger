package Server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerStartUP {

    private static HashMap<String, Long> activeClients = new HashMap<>();
    
    public static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/stablishConection", new SetClientOnServer());
        server.createContext("/ping", new PingHandler());
        server.createContext("/message", new MessageHandler());
        server.setExecutor(null);
        server.start();
        checkClientActivity();
    }

    static class SetClientOnServer implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Adiciona o endereço IP do cliente à lista de clientes
            String clientAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
            Long timeStamp = System.currentTimeMillis();
            String randomUUID = SessionManager.generateSessionId();

            String sessionID = (clientAddress + "|" + randomUUID);
            activeClients.put(sessionID, timeStamp);
            
            exchange.sendResponseHeaders(200, sessionID.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(sessionID.getBytes());
            os.close();
        }
    }

    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Long timeStamp = System.currentTimeMillis();
            // Extrai o sessionID do cabeçalho da solicitação
            String sessionID = exchange.getRequestHeaders().getFirst("sessionID");

            activeClients.put(sessionID, timeStamp);

            // Envia uma resposta de sucesso para o cliente
            String response = "Pong from server";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MessageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String message = getMessageFromRequest(exchange);
                writeMessageToFile(message);
                sendResponse(exchange, "Message received and saved!");
            } else {
                sendResponse(exchange, "Only POST requests are allowed.");
            }
        }

        private String getMessageFromRequest(HttpExchange exchange) throws IOException {
            byte[] requestBodyBytes = exchange.getRequestBody().readAllBytes();
            return new String(requestBodyBytes);
        }

        private void writeMessageToFile(String message) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("messages.txt", true))) {
                writer.write(message);
                writer.newLine();
            }
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static void checkClientActivity() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                System.out.println(activeClients);
                for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                    String clientId = entry.getKey();
                    Long lastActiveTime = entry.getValue();
                    if (currentTime - lastActiveTime > 10000) { // Tempo limite de inatividade de 10 segundos
                        activeClients.remove(clientId);
                        System.out.println("Client " + clientId + " disconnected due to inactivity.");
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
