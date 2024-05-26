package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArquiveManager {
    public static void writeFile() {
        String fileName = "example.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Hello, World!");
            writer.newLine();
            writer.write("This is a new line in the text file.");
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
