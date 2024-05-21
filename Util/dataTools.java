package Util;

import java.util.HashMap;
import java.util.Map;

import Server.Mensagem;

public class dataTools {

    public static Mensagem convertStringToObj(String jsonString) {
        // Remover chaves desnecessárias
        jsonString = jsonString.replaceAll("[{}\"]", "");
        
        // Dividir a string em pares chave-valor
        String[] pairs = jsonString.split(",");
        
        // Criar um mapa para armazenar os pares chave-valor
        Map<String, String> map = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0].trim(), keyValue[1].trim());
        }
        
        // Criar e preencher o objeto Dados
        Mensagem newMensage = new Mensagem();
        newMensage.setRemetente(map.get("Remetente"));
        newMensage.setDestinatario(map.get("Destinatario"));
        newMensage.setContent(map.get("Content"));
        
        return newMensage;
    }

    public static String setStringMessage(String remetente, String destinatario, String content) {
        String mensagem = "{\"Remetente\":" + remetente + "\"Destinatario\":" + destinatario + "\"Content\":" + content + "}";
        return mensagem;
    }
}
