package Server;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

import Model.Mensagem;





public class ControllerMessage implements Serializable{

        
    private static HashMap<String, TreeMap<Long,Mensagem>> recentMessages = new HashMap<>();
    //Key = UserName de destino      //Key = TimeStamp


    public static boolean checkIfThereIsMessage(String userNameDestinatario) {
        if(recentMessages.containsKey(userNameDestinatario)){
            return true;
        }
        return false;
    }

    public static TreeMap<Long,Mensagem> getRecentMessages(String userNameDestinatario) throws IOException {
        
        TreeMap<Long,Mensagem> tempMap = recentMessages.get(userNameDestinatario);
        recentMessages.remove(userNameDestinatario);

        return tempMap;
    }

    public static void addNewMessage(Mensagem newMessage){
        //System.out.println("Array Inicial: " + recentMessages);
        if (recentMessages.containsKey(newMessage.getDestinatario())){
            TreeMap<Long,Mensagem> tempMap = recentMessages.get(newMessage.getDestinatario());
            tempMap.put(newMessage.getTimestamp(), newMessage);

            //System.out.println("Já tem mensagem: "+ recentMessages);
            //recentMessages.put(newMessage.getDestinatario(), tempMap);
        }else{
            TreeMap<Long,Mensagem> tempMap = new TreeMap<>();
            tempMap.put(newMessage.getTimestamp(), newMessage);
    
            recentMessages.put(newMessage.getDestinatario(), tempMap);

            //System.out.println("Não tem mensagens: "+ recentMessages);
        }
        

    }


    




}
