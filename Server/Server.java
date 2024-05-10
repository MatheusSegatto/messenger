package Server;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    private static List<String> clients = new ArrayList<>(); // Lista de clientes

    public static void main(String[] args) throws IOException {
        int port = 8000;
        startServer(port);
        System.out.println("Server started on port " + port);
    }

    private static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/stablishConection", new ClientHandler());
        server.createContext("/", new MessageHandler());
        server.setExecutor(null);
        server.start();
        
        new Thread(() -> {
            while (true) {
                pingClients();
                try {
                    Thread.sleep(5000); // Pings a cada 5 segundos
                    System.out.println(clients);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class ClientHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            // Adiciona o endereço IP do cliente à lista de clientes
            String clientAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
            clients.add(clientAddress);

            String response = "Conexão Estabelecida com o Servidor!";
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


    private static void pingClients() {
        for (String client : clients) {
            try {
                // Constrói a URL do cliente
                @SuppressWarnings("deprecation")
                URL clientURL = new URL("http://" + client + ":8001/ping"); // Assumindo que o cliente esteja escutando na porta 8001
                HttpURLConnection connection = (HttpURLConnection) clientURL.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Ping successful to client: " + client);
                } else {
                    System.out.println("Ping failed to client: " + client + ", response code: " + responseCode);
                }
            } catch (IOException e) {
                System.out.println("Failed to ping client: " + client);
                e.printStackTrace();
            }
        }
    }
}




