package Client;

import java.io.IOException;



import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.print("Qual seu Username: ");
        String userName = scanner.nextLine();
        ClientHandler.setUserName(userName);
        
        System.out.print("Qual o Username do destinatario: ");
        String userNameDestino = scanner.nextLine();

        ClientHandler.setUserNameDestino(userNameDestino);

        
        
        ClientHandler.stablishConection();

        MessageHandler.messageSide();
    }

}
 
