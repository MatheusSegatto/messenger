package Client;

import java.io.IOException;



import java.util.Scanner;

import Util.commandPrompt;


public class Client {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.print("Qual seu Username: ");
        String userName = scanner.nextLine();
        ClientHandler.setUserName(userName);
        
        System.out.print("Qual o Username do destinatario: ");
        String userNameDestino = scanner.nextLine();

        ClientHandler.setUserNameDestino(userNameDestino);
        
        ClientHandler.stablishConection();

        
        System.out.println(ClientHandler.getListOfUsersConected());
        
        MessageHandler.messageSide();


    }
}
 
