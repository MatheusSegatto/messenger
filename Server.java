import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    public Server() {
        // Construtor vazio
    }

    public static void main(String[] args) throws IOException {
        int port = 8000;
        startServer(port);
        System.out.println("Server started on port " + port);
    }

    private static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/hello", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello from the server!";
            sendResponse(exchange, response);
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
