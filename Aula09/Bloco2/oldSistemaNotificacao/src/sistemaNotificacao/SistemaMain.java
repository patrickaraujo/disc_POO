package sistemaNotificacao;

import java.util.ArrayList;

public class SistemaMain {

	public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║      CENTRAL DE NOTIFICAÇÕES - TechBR             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        CentralNotificacoes central = new CentralNotificacoes("TechBR");

        // ❌ Tentativa de instanciar a abstrata (deixe COMENTADO):
        // Notificacao n = new Notificacao("X", "y", "z");
        //                                             ^^^^^
        //                  Erro de compilação: Notificacao is abstract

        // SMS — destinatário válido
        central.enfileirar(new NotificacaoSMS(
            "N001",
            "11987654321",
            "Seu pedido #4521 foi enviado e chega amanhã!"
        ));

        // E-mail — destinatário válido
        central.enfileirar(new NotificacaoEmail(
            "N002",
            "ana.silva@email.com",
            "Confirmação de Pagamento",
            "Recebemos seu pagamento de R$ 250,00. Obrigado!"
        ));

        // Push — destinatário válido
        central.enfileirar(new NotificacaoPush(
            "N003",
            "device_abc12345xyz",
            "Promoção Relâmpago",
            "20% OFF em toda a loja por 1 hora!",
            true
        ));

        // WhatsApp — destinatário válido
        central.enfileirar(new NotificacaoWhatsApp(
            "N004",
            "+5511987654321",
            "boas_vindas",
            "Olá! Seja bem-vindo à TechBR."
        ));

        // SMS — destinatário INVÁLIDO (telefone com poucos dígitos)
        central.enfileirar(new NotificacaoSMS(
            "N005",
            "1198765",  // ❌ formato inválido
            "Mensagem de teste"
        ));

        // E-mail — destinatário INVÁLIDO (sem @)
        central.enfileirar(new NotificacaoEmail(
            "N006",
            "email-invalido.com",  // ❌ sem @
            "Teste",
            "Mensagem de teste"
        ));

        // Listar tudo
        central.listarTodas();

        // Estatísticas
        central.exibirEstatisticas();

        // Filtrar por canal
        System.out.println("\n=== FILTRO: APENAS E-MAILS ===");
        ArrayList<Notificacao> emails = central.filtrarPorTipo(NotificacaoEmail.class);
        for (Notificacao n : emails) {
            System.out.println("- " + n.getId() + " | " + n.getDestinatario()
                    + " | " + n.getStatus());
        }
    }
}
