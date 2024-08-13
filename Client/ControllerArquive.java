package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import Model.Mensagem;
import Model.User;
import Util.dataTools;

public class ControllerArquive {

    private static final Semaphore semaphore = new Semaphore(1);

    private static TreeMap<Long, Mensagem> waitingToBeWritten = new TreeMap<>();

    public static void addMessageToBeWriten(Mensagem messageToBeWritten) throws InterruptedException {
        waitingToBeWritten.put(messageToBeWritten.getTimestamp(), messageToBeWritten);

        new Thread(() -> {
            try {
                writeFile();
            } catch (InterruptedException e) {
                System.out.println("[CLIENT]: Error when was trying to execute a Thread!");
            }
        }).start();
    }

    private static void writeFile() throws InterruptedException {

        semaphore.acquire();

        try {
            Thread.sleep(5000);

            if (!waitingToBeWritten.isEmpty()) {
                Long firstKey = waitingToBeWritten.firstKey();
                Mensagem message = waitingToBeWritten.get(firstKey);

                User userConnected = ClientHandler.getUserConnected();
                String fileName = userConnected.getId();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // Note o 'true' aqui
                    String content;
                    if (message.getRemetente().equals(userConnected.getUsername())) {
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
                    System.out.println("[CLIENT]: Error trying to write in a file!");
                }
                waitingToBeWritten.remove(firstKey);
            }
        } finally {
            // Libera a permissão após a escrita no arquivo
            semaphore.release();
        }
    }

    public static void deleteUserFile() {
        User userConnected = ClientHandler.getUserConnected();
        String fileName = userConnected.getId();

        File userFile = new File(fileName);

        if (userFile.exists()) {
            boolean deleted = userFile.delete();

            if (deleted) {
                System.out.println("Arquivo do usuário excluído com sucesso.");
            } else {
                System.out.println("Não foi possível excluir o arquivo do usuário.");
            }
        } else {
            System.out.println("O arquivo do usuário não existe.");
        }
    }

    public static void readFile() {
        User userConnected = ClientHandler.getUserConnected();
        String fileName = userConnected.getId();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("[CLIENT]: Error trying to read a file!");
        }
    }
}
