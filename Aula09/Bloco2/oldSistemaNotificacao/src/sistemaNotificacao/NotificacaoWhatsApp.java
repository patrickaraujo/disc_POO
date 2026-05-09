package sistemaNotificacao;

public class NotificacaoWhatsApp extends Notificacao {

    private String modelo;  // ex: "boas_vindas", "promocao_ativa"

    public NotificacaoWhatsApp(String id, String destinatario,
                               String modelo, String mensagem) {
        super(id, destinatario, mensagem);
        this.modelo = modelo;
    }

    @Override
    public String getTipoCanal() {
        return "WhatsApp";
    }

    @Override
    protected boolean validarDestinatario() {
        // Telefone internacional: começa com +55 e tem 14 caracteres no total (+55 + 11 dígitos)
        if (destinatario == null) return false;
        if (!destinatario.startsWith("+55")) return false;
        String apenasDigitos = destinatario.substring(3).replaceAll("\\D", "");
        return apenasDigitos.length() == 11;
    }

    @Override
    protected String formatarMensagem() {
        return "[modelo: " + modelo + "] " + mensagem;
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    💬 Enviando WhatsApp para " + destinatario);
        System.out.println("    Conteúdo: " + mensagemFormatada);

        // Simulação: 88% de sucesso
        boolean sucesso = Math.random() < 0.88;
        if (sucesso) {
            System.out.println("    ✓ WhatsApp entregue via API oficial.");
        } else {
            System.out.println("    ✗ API WhatsApp temporariamente fora. Retentando...");
        }
        return sucesso;
    }
}