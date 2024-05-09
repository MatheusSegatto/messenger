import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private HttpClient client;

    // Construtor
    public Client() {
        this.client = HttpClient.newHttpClient();
    }

    public void sendRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/hello"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response from server: " + response.body());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.sendRequest();
    }
}
