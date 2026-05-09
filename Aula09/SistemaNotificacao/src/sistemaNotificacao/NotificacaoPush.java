package sistemaNotificacao;

public class NotificacaoPush extends Notificacao {

    private String titulo;
    private boolean comSom;

    public NotificacaoPush(String id, String destinatario,
                           String titulo, String mensagem, boolean comSom) {
        super(id, destinatario, mensagem);
        this.titulo = titulo;
        this.comSom = comSom;
    }

    @Override
    public String getTipoCanal() {
        return "Push";
    }

    @Override
    protected boolean validarDestinatario() {
        // Token de dispositivo: começa com "device_" e tem mais de 10 caracteres
        return destinatario != null
            && destinatario.startsWith("device_")
            && destinatario.length() > 10;
    }

    @Override
    protected String formatarMensagem() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"titulo\": \"").append(titulo).append("\", ");
        sb.append("\"corpo\": \"").append(mensagem).append("\", ");
        sb.append("\"som\": ").append(comSom);
        sb.append(" }");
        return sb.toString();
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    🔔 Enviando push para " + destinatario);
        System.out.println("    Payload: " + mensagemFormatada);

        // Simulação: 85% de sucesso
        boolean sucesso = Math.random() < 0.85;
        if (sucesso) {
            System.out.println("    ✓ Push entregue ao FCM.");
        } else {
            System.out.println("    ✗ Token de dispositivo expirado. Retentando...");
        }
        return sucesso;
    }
}