package Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import Util.dataTools;




public class MessageManager {

        
    private static HashMap<String, TreeMap<Long,Mensagem>> recentMessages = new HashMap<>();
    //Key = UserName de destino      //Key = TimeStamp


    public static boolean checkIfThereIsMessage(String userNameDestinatario) {
        if(recentMessages.containsKey(userNameDestinatario)){
            return true;
        }
        return false;
    }

    public static String getMessageRecent(String userNameDestinatario) throws IOException {
        
        TreeMap<Long,Mensagem> tempMap = recentMessages.get(userNameDestinatario);
        recentMessages.remove(userNameDestinatario);

        return dataTools.objetoParaString(tempMap);
    }

    public static void addNewMessage(Mensagem newMessage){
        if (recentMessages.containsKey(newMessage.getDestinatario())){
            TreeMap<Long,Mensagem> tempMap = recentMessages.get(newMessage.getDestinatario());
            tempMap.put(newMessage.getTimestamp(), newMessage);
            //recentMessages.put(newMessage.getDestinatario(), tempMap);
        }else{
            TreeMap<Long,Mensagem> tempMap = new TreeMap<>();
            tempMap.put(newMessage.getTimestamp(), newMessage);
    
            recentMessages.put(newMessage.getDestinatario(), tempMap);
        }
        

    }


    




}
