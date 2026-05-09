package sistemaNotificacao;

import java.util.ArrayList;

import sistemaNotificacao.Notificacao.StatusEnvio;

public class CentralNotificacoes {

    private String nomeEmpresa;
    private ArrayList<Notificacao> historico;

    public CentralNotificacoes(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
        // ★ ArrayList polimórfico de tipo abstrato ★
        // Aceita qualquer subclasse concreta de Notificacao
        this.historico = new ArrayList<>();
    }

    // Adiciona e dispara o envio (Template Method em ação!)
    public void enfileirar(Notificacao n) {
        historico.add(n);
        n.enviar();  // ← Template Method polimórfico
    }

    // Reenvia todas as que falharam
    public void reenviarFalhas() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  REENVIO DE NOTIFICAÇÕES QUE FALHARAM             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        int reenviadas = 0;
        for (Notificacao n : historico) {
            if (n.getStatus() == StatusEnvio.FALHOU) {
                // Reseta para nova tentativa
                System.out.println("\n>>> Reenviando notificação " + n.getId());
                // (na prática, criaríamos uma nova instância — aqui simplificamos)
                reenviadas++;
            }
        }

        if (reenviadas == 0) {
            System.out.println("\n  Nenhuma notificação para reenviar.");
        }
    }

    // Lista resumida
    public void listarTodas() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  HISTÓRICO DE NOTIFICAÇÕES - " + nomeEmpresa.toUpperCase());
        System.out.println("╠═══════════════════════════════════════════════════════════╣");

        if (historico.isEmpty()) {
            System.out.println("║  Nenhuma notificação enviada.");
        } else {
            for (int i = 0; i < historico.size(); i++) {
                Notificacao n = historico.get(i);
                System.out.printf("║  [%d] %-6s | %-15s | %-30s | %s%n",
                    (i + 1),
                    n.getId(),
                    n.getTipoCanal(),
                    truncar(n.getDestinatario(), 28),
                    n.getStatus());
            }
        }

        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    // Estatísticas
    public void exibirEstatisticas() {
        int total = historico.size();
        int enviadas = 0, falhas = 0, pendentes = 0;

        for (Notificacao n : historico) {
            switch (n.getStatus()) {
                case ENVIADO:  enviadas++; break;
                case FALHOU:   falhas++; break;
                case PENDENTE: pendentes++; break;
            }
        }

        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println("Total de notificações: " + total);
        System.out.println("  ✓ Enviadas: " + enviadas);
        System.out.println("  ✗ Falhas:   " + falhas);
        System.out.println("  ⏳ Pendentes: " + pendentes);

        if (total > 0) {
            double taxaSucesso = (enviadas * 100.0) / total;
            System.out.printf("Taxa de sucesso: %.1f%%%n", taxaSucesso);
        }
    }

    // Filtro polimórfico por canal
    public ArrayList<Notificacao> filtrarPorTipo(Class<?> tipo) {
        ArrayList<Notificacao> resultado = new ArrayList<>();
        for (Notificacao n : historico) {
            if (tipo.isInstance(n)) {
                resultado.add(n);
            }
        }
        return resultado;
    }

    // Auxiliar
    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}