package sistemaNotificacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Notificacao {
	
	public enum StatusEnvio{
		PENDENTE,
		ENVIADO,
		FALHOU
	}


    protected static final int MAX_TENTATIVAS = 3;

    // Atributos comuns
    protected String id;
    protected String destinatario;
    protected String mensagem;
    protected LocalDateTime dataEnvio;
    protected StatusEnvio status;
    protected int tentativas;

    // Construtor (chamado pelas subclasses via super)
    public Notificacao(String id, String destinatario, String mensagem) {
        this.id = id;
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.status = StatusEnvio.PENDENTE;
        this.tentativas = 0;
    }

    // ┌─────────────────────────────────────────────────────┐
    // │  TEMPLATE METHOD                                    │
    // │  Define o esqueleto do algoritmo de envio.          │
    // │  É 'final' — subclasses NÃO podem alterar a ordem.  │
    // └─────────────────────────────────────────────────────┘
    public final void enviar() {
        System.out.println("\n>>> Iniciando envio: " + id + " (" + getTipoCanal() + ")");

        while (tentativas < MAX_TENTATIVAS && status != StatusEnvio.ENVIADO) {
            tentativas++;
            System.out.println("    Tentativa " + tentativas + " de " + MAX_TENTATIVAS);

            try {
                // 1. Validação (abstrato)
                if (!validarDestinatario()) {
                    System.out.println("    ✗ Destinatário inválido: " + destinatario);
                    status = StatusEnvio.FALHOU;
                    break;  // não adianta retentar — destinatário sempre será inválido
                }

                // 2. Formatação (abstrato)
                String mensagemFormatada = formatarMensagem();

                // 3. Transmissão (abstrato)
                boolean sucesso = transmitir(mensagemFormatada);

                if (sucesso) {
                    status = StatusEnvio.ENVIADO;
                    dataEnvio = LocalDateTime.now();
                } else if (tentativas >= MAX_TENTATIVAS) {
                    status = StatusEnvio.FALHOU;
                }
            } catch (Exception e) {
                System.out.println("    ✗ Erro: " + e.getMessage());
                if (tentativas >= MAX_TENTATIVAS) {
                    status = StatusEnvio.FALHOU;
                }
            }
        }

        // 4. Log (concreto — comum a todos)
        registrarLog();
    }

    // ┌─────────────────────────────────────────────────────┐
    // │  MÉTODOS ABSTRATOS — pontos de variação             │
    // │  Cada subclasse implementa do seu jeito.            │
    // └─────────────────────────────────────────────────────┘
    protected abstract boolean validarDestinatario();
    protected abstract String formatarMensagem();
    protected abstract boolean transmitir(String mensagemFormatada);
    public abstract String getTipoCanal();

    // ┌─────────────────────────────────────────────────────┐
    // │  MÉTODO CONCRETO — comum a todos                    │
    // └─────────────────────────────────────────────────────┘
    protected void registrarLog() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataStr = (dataEnvio != null) ? dataEnvio.format(fmt) : "—";

        System.out.println("    [LOG] " + id + " | " + getTipoCanal()
                + " | Status: " + status
                + " | Tentativas: " + tentativas
                + " | Data: " + dataStr);
    }

    // Getters
    public String getId() { return id; }
    public String getDestinatario() { return destinatario; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public StatusEnvio getStatus() { return status; }
    public int getTentativas() { return tentativas; }
}