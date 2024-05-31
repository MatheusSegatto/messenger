package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Model.Mensagem;
import Model.User;
import Util.dataTools;

public class ArquiveManager {
    public static void writeFile(User userConected, Mensagem message) {

        String fileName = userConected.getId();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String content;
            if (message.getRemetente().equals(userConected.getUsername())){
                content = "[VOCÊ] [" + dataTools.setSecondsToData(message.getTimestamp()) + "]: " + message.getContent();
            }else{
                content = "[" + message.getRemetente() + "] [" + dataTools.setSecondsToData(message.getTimestamp()) + "]" + message.getContent();
            }
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void readFile() {
        String fileName = "example.txt";
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
