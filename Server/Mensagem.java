package Server;

import java.util.UUID;

public class Mensagem {
    private UUID id = UUID.randomUUID();
    private String content;
    private String remetente; //Colocar como objetos
    private String destinatario; //Colocar como objetos
    private Long timestamp = System.currentTimeMillis();

    public Mensagem(String content, String remetente, String destinatario) {
        this.content = content;
        this.remetente = remetente;
        this.destinatario = destinatario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
