package Model;

import java.io.Serializable;
import java.util.UUID;

public class Mensagem implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String remetente; // Colocar como objetos
    private String destinatario; // Colocar como objetos
    private String content;
    private Long timestamp = System.currentTimeMillis();

    public Mensagem() {

    }

    public Mensagem(String remetente, String content) {
        this.remetente = remetente;
        this.content = content;
    }

    public Mensagem(String remetente, String destinatario, String content) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
