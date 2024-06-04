package Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TreeMap;

import Model.Mensagem;

public class dataTools {

    // public static String objToString(Object obj) throws IOException {
    // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // ObjectOutputStream objectOutputStream = new
    // ObjectOutputStream(byteArrayOutputStream);
    // objectOutputStream.writeObject(obj);
    // objectOutputStream.flush();
    // objectOutputStream.close();
    // return byteArrayOutputStream.toString("ISO-8859-1");
    // }

    // public static Object stringToObj(String str) throws IOException,
    // ClassNotFoundException {
    // ByteArrayInputStream byteArrayInputStream = new
    // ByteArrayInputStream(str.getBytes("ISO-8859-1"));
    // ObjectInputStream objectInputStream = new
    // ObjectInputStream(byteArrayInputStream);
    // return objectInputStream.readObject();
    // }

    public static String objToString(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static Object stringToObj(String str) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(str);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    @SuppressWarnings("unchecked")
    public static TreeMap<Long, Mensagem> deserializeStringToTreeMap(String data) {
        try {
            byte[] bytes = Base64.getDecoder().decode(data);
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                    ObjectInputStream in = new ObjectInputStream(byteIn)) {
                return (TreeMap<Long, Mensagem>) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            // Lidar com a exceção
            e.printStackTrace();
            return null; // ou outra ação apropriada
        }
    }

    public static String serializeTreeMapToString(TreeMap<Long, Mensagem> treeMap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(treeMap);
        }
        byte[] serializedBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(serializedBytes);
    }

    public static String setSecondsToData(Long seconds){
        Date date = new Date(seconds);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }
}

