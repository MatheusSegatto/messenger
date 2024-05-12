package Server;
import java.io.IOException;


public class Server {
    
    public static void main(String[] args) throws IOException {
        int port = 8000;
        ServerStartUP.startServer(port);
        System.out.println("Server started on port " + port);
    }
}




