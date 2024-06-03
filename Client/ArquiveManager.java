package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import Model.Mensagem;
import Model.User;
import Util.dataTools;

public class ArquiveManager {

    private static TreeMap<Long, Mensagem> waitingToBeWritten = new TreeMap<>();

    public static void addMessageToBeWriten(Mensagem messageToBeWritten) throws InterruptedException {
        waitingToBeWritten.put(messageToBeWritten.getTimestamp(), messageToBeWritten);
        new Thread(() -> {
            try {
                writeFile();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();

    }

    private static void writeFile() throws InterruptedException {
        Thread.sleep(5000);

        if (!waitingToBeWritten.isEmpty()) {
            Mensagem message = waitingToBeWritten.get(waitingToBeWritten.firstKey());

            // System.out.println("Escrevendo mensagem: " + message.getContent());
            waitingToBeWritten.remove(waitingToBeWritten.firstKey());
            User userConected = ClientHandler.getUserConected();
            String fileName = userConected.getId();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // Note o 'true' aqui
                String content;
                if (message.getRemetente().equals(userConected.getUsername())) {
                    content = "[VOCÊ] -> " + "[" + message.getDestinatario() + "] ["
                            + dataTools.setSecondsToData(message.getTimestamp()) + "]: "
                            + message.getContent();
                } else {
                    content = "[" + message.getRemetente() + "] -> [VOCÊ] ["
                            + dataTools.setSecondsToData(message.getTimestamp())
                            + "]: " + message.getContent();
                }
                writer.write(content);
                writer.newLine(); // Adiciona uma nova linha antes de escrever o conteúdo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readFileAndPrint(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile() {
        User userConected = ClientHandler.getUserConected();
        String fileName = userConected.getId();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
