public class Pedido {

    private int numero;
    private String nomeCliente;
    private int totalItens;
    private double valorTotal;

    // Construtor
    public Pedido(int numero, String nomeCliente) {
        this.numero = (numero <= 0) ? 1 : numero;
        this.nomeCliente = (nomeCliente == null || nomeCliente.isEmpty()) ? "Cliente" : nomeCliente;
        this.totalItens = 0;
        this.valorTotal = 0.0;
    }

    // --- Getters ---

    public int getNumero() {
        return numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getTotalItens() {
        return totalItens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // --- Métodos sobrecarregados ---

    // Adiciona 1 item
    public void adicionarItem(double preco) {
        if (preco <= 0) {
            System.out.println("Erro: preço deve ser positivo.");
            return;
        }
        totalItens++;
        valorTotal = valorTotal + preco;
        System.out.println("Item adicionado: R$ " + preco
                + " | Itens: " + totalItens + " | Total: R$ " + valorTotal);
    }

    // Adiciona múltiplos itens
    public void adicionarItem(double preco, int quantidade) {
        if (preco <= 0) {
            System.out.println("Erro: preço deve ser positivo.");
            return;
        }
        if (quantidade <= 0) {
            System.out.println("Erro: quantidade deve ser positiva.");
            return;
        }
        totalItens = totalItens + quantidade;
        valorTotal = valorTotal + (preco * quantidade);
        System.out.println(quantidade + " itens adicionados: R$ " + preco
                + " cada | Itens: " + totalItens + " | Total: R$ " + valorTotal);
    }

    // --- Exibição ---

    public void exibirResumo() {
        System.out.println("=== Pedido #" + numero + " ===");
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Itens: " + totalItens);
        System.out.println("Valor total: R$ " + valorTotal);
    }
}
