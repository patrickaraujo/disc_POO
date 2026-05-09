package sistemaNotificacao;

public class NotificacaoEmail extends Notificacao {

    private String assunto;

    public NotificacaoEmail(String id, String destinatario, String assunto, String mensagem) {
        super(id, destinatario, mensagem);
        this.assunto = assunto;
    }

    public String getAssunto() { return assunto; }

    @Override
    public String getTipoCanal() {
        return "E-mail";
    }

    @Override
    protected boolean validarDestinatario() {
        // Validação simplificada: contém @ e tem ao menos um ponto após
        if (destinatario == null) return false;
        int posArroba = destinatario.indexOf('@');
        if (posArroba < 1) return false;
        return destinatario.indexOf('.', posArroba) > posArroba;
    }

    @Override
    protected String formatarMensagem() {
        StringBuilder sb = new StringBuilder();
        sb.append("De: nao-responder@techbr.com\n");
        sb.append("Para: ").append(destinatario).append("\n");
        sb.append("Assunto: ").append(assunto).append("\n");
        sb.append("\n");
        sb.append(mensagem);
        sb.append("\n\n--\nEnviado pela TechBR");
        return sb.toString();
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    ✉️  Enviando e-mail para " + destinatario);
        System.out.println("    Assunto: \"" + assunto + "\"");

        // Simulação: 95% de sucesso
        boolean sucesso = Math.random() < 0.95;
        if (sucesso) {
            System.out.println("    ✓ E-mail entregue ao servidor SMTP.");
        } else {
            System.out.println("    ✗ Servidor SMTP rejeitou. Retentando...");
        }
        return sucesso;
    }
}