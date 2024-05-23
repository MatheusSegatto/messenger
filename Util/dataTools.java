package Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import Server.Mensagem;

public class dataTools {

    public static Mensagem convertStringToMensagem(String jsonString) {
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

    public static String objetoParaString(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toString("ISO-8859-1");
    }

    public static Object stringParaObjeto(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}

// public static String serializeHashMap(Payload payload) {
//     try {
//         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//         ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//         objectOutputStream.writeObject(payload);
//         objectOutputStream.close();
//         return byteArrayOutputStream.toString("ISO-8859-1");

//     } catch (IOException e) {
//         e.printStackTrace();
//         return null;
//     }
// }

// public static Mensagem deserializeHashMap(String payloadString) {
//         try {
//             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payloadString.getBytes("ISO-8859-1"));
//             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//             Mensagem mapa = (Mensagem) objectInputStream.readObject();
//             objectInputStream.close();
//             return mapa;

//         }catch (IOException | ClassNotFoundException e) {
//             e.printStackTrace();
//             return null;
//     }
// }