import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/message"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response from server: " + response.body());
    }
}