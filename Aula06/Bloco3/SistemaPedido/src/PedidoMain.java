public class PedidoMain {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE PEDIDOS DE COMPRA        ║");
        System.out.println("║   Aula 06 - Bloco 3 - Exemplo Composição  ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        // --- Criar um pedido ---
        System.out.println("--- Criando pedido #101 ---");
        Pedido pedido1 = new Pedido(101, "João Silva", "15/03/2024", 10);

        // --- Adicionar itens (COMPOSIÇÃO - criados DENTRO do pedido) ---
        System.out.println("\n--- Adicionando itens (composição) ---");
        pedido1.adicionarItem("Mouse Gamer", 2, 89.90);
        pedido1.adicionarItem("Teclado Mecânico", 1, 349.90);
        pedido1.adicionarItem("Monitor 24\"", 1, 899.90);

        // --- Exibir pedido ---
        pedido1.exibirPedido();

        // --- Demonstrar remoção ---
        System.out.println("--- Cliente desistiu do monitor ---");
        pedido1.removerItem("Monitor 24\"");
        pedido1.exibirPedido();

        // --- Teste: tentar remover item que não existe ---
        System.out.println("--- Tentando remover item inexistente ---");
        pedido1.removerItem("Notebook");

        // --- Teste: limite de capacidade ---
        System.out.println("\n--- Criando pedido pequeno (capacidade = 2) ---");
        Pedido pedido2 = new Pedido(102, "Maria Santos", "16/03/2024", 2);
        pedido2.adicionarItem("Headset", 1, 199.90);
        pedido2.adicionarItem("Webcam", 1, 149.90);
        pedido2.adicionarItem("Microfone", 1, 299.90);   // ← deve estourar capacidade
        pedido2.exibirPedido();

        // --- Um segundo pedido independente: cada pedido tem seus PRÓPRIOS itens ---
        System.out.println("--- Criando pedido #103 (itens totalmente novos) ---");
        Pedido pedido3 = new Pedido(103, "Carlos Souza", "17/03/2024", 5);
        pedido3.adicionarItem("Mouse Gamer", 1, 89.90);   // mesmo nome, mas é OUTRO ItemPedido!
        pedido3.adicionarItem("Mousepad XL", 1, 79.90);
        pedido3.exibirPedido();

        // --- Análise didática ---
        System.out.println("=== ANÁLISE DA COMPOSIÇÃO ===\n");
        System.out.println("1. ItemPedido tem construtor package-private:");
        System.out.println("   → `new ItemPedido(...)` só funciona dentro do pacote.");
        System.out.println("   → Fora do pacote, só a classe Pedido pode criar itens.\n");

        System.out.println("2. Cada Pedido tem SEUS PRÓPRIOS itens:");
        System.out.println("   → O 'Mouse Gamer' do pedido #101 e o do #103 são objetos distintos.\n");

        System.out.println("3. Se um Pedido for descartado, seus itens também são:");
        System.out.println("   → Quando a referência ao pedido é perdida, os ItemPedido");
        System.out.println("     ficam inalcançáveis e são coletados pelo garbage collector.\n");

        System.out.println("Isso é COMPOSIÇÃO (◆): a parte não existe sem o todo.");

        // Exemplo: deixar pedido2 ser "descartado"
        System.out.println("\n--- Descartando pedido #102 ---");
        pedido2 = null;
        System.gc();   // sugestão ao GC (não garantida, mas didática)
        System.out.println("pedido2 = null → os ItemPedido do pedido #102");
        System.out.println("ficam inalcançáveis e serão coletados pelo GC.");
    }
}
