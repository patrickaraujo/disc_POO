package sistemaNotificacao;

public class NotificacaoSMS extends Notificacao {

    private static final int LIMITE_CARACTERES = 160;

    public NotificacaoSMS(String id, String destinatario, String mensagem) {
        super(id, destinatario, mensagem);
    }

    @Override
    public String getTipoCanal() {
        return "SMS";
    }

    @Override
    protected boolean validarDestinatario() {
        // Telefone deve ter 11 dígitos (DDD + número, ex: 11987654321)
        if (destinatario == null) return false;
        String apenasDigitos = destinatario.replaceAll("\\D", "");
        return apenasDigitos.length() == 11;
    }

    @Override
    protected String formatarMensagem() {
        // Truncar em 160 caracteres
        if (mensagem.length() <= LIMITE_CARACTERES) {
            return mensagem;
        }
        return mensagem.substring(0, LIMITE_CARACTERES - 3) + "...";
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    📱 Enviando SMS para " + destinatario);
        System.out.println("    Conteúdo: \"" + mensagemFormatada + "\"");

        // Simulação: 90% de sucesso
        boolean sucesso = Math.random() < 0.9;
        if (sucesso) {
            System.out.println("    ✓ SMS entregue pela operadora.");
        } else {
            System.out.println("    ✗ Operadora indisponível. Retentando...");
        }
        return sucesso;
    }
}