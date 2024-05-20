package Util;

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
}
