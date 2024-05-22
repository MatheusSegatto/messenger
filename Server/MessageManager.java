package Server;

import java.util.HashMap;
import java.util.TreeMap;



public class MessageManager {

        
    private static HashMap<String, TreeMap<Long,Mensagem>> recentMessages = new HashMap<>();
    //Key = UserName de destino      //Key = TimeStamp


    private static void checkIfThereIsMessage() {

    }

    public static void addNewMessage(Mensagem newMessage){

        TreeMap<Long,Mensagem> tempMap = new TreeMap<>();
        tempMap.put(newMessage.getTimestamp(), newMessage);

        recentMessages.put(newMessage.getDestinatario(), tempMap);



    }


    




}
