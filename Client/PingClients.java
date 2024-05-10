package Client;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class PingClients {
    private static List<String> clients = new ArrayList<>(); // Lista de clientes

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/ping", new PingHandler());
        server.createContext("/addClient", new AddClientHandler());
        server.setExecutor(Executors.newFixedThreadPool(10)); // define o número de threads no pool
        server.start();
        System.out.println("Server started on port 8000...");
        
        // Thread para pingar os clientes periodicamente
        new Thread(() -> {
            while (true) {
                pingClients();
                try {
                    Thread.sleep(5000); // Pings a cada 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Pong from server";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    static class AddClientHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Adiciona o endereço IP do cliente à lista de clientes
            String clientAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
            clients.add(clientAddress);

            String response = "Client added successfully";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    // Método para pingar todos os clientes
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
