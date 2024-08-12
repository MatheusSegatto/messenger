package Server;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

import Model.Mensagem;

public class ControllerMessage implements Serializable {

    private static HashMap<String, TreeMap<Long, Mensagem>> recentMessages = new HashMap<>();

    public static boolean checkIfThereIsMessage(String userNameDestinatario) {
        if (recentMessages.containsKey(userNameDestinatario)) {
            return true;
        }
        return false;
    }

    public static TreeMap<Long, Mensagem> getRecentMessages(String userNameDestinatario) throws IOException {
        TreeMap<Long, Mensagem> tempMap = recentMessages.get(userNameDestinatario);
        recentMessages.remove(userNameDestinatario);

        //debug
        System.out.println("Verificacao de mensagem recente");
        System.out.println(tempMap.toString());
        System.out.println(recentMessages.toString());
        System.out.println("-------------");




        return tempMap;
    }

    public static void addNewMessage(Mensagem newMessage) {
        if (recentMessages.containsKey(newMessage.getDestinatario())) {
            TreeMap<Long, Mensagem> tempMap = recentMessages.get(newMessage.getDestinatario());
            tempMap.put(newMessage.getTimestamp(), newMessage);

            //debug
            System.out.println("Ja existe");
            System.out.println(tempMap.toString());
            System.out.println(recentMessages.toString());
            System.out.println("--------------------");

        } else {
            TreeMap<Long, Mensagem> tempMap = new TreeMap<>();
            tempMap.put(newMessage.getTimestamp(), newMessage);
            recentMessages.put(newMessage.getDestinatario(), tempMap);

            //debug
            System.out.println("Novo existe");
            System.out.println(recentMessages.toString());
            System.out.println("--------------------");
        }

    }

}
