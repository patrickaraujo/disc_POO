public class Pedido {
    private int numero;
    private String cliente;
    private String data;
    private ItemPedido[] itens;   // ← COMPOSIÇÃO
    private int qtdItens;

    public Pedido(int numero, String cliente, String data, int capacidade) {
        this.numero = numero;
        this.cliente = cliente;
        this.data = data;
        this.itens = new ItemPedido[capacidade];
        this.qtdItens = 0;
    }

    // Método que CRIA itens (composição!)
    public void adicionarItem(String produto, int quantidade, double precoUnitario) {
        if (qtdItens < itens.length) {
            // Cria o ItemPedido AQUI DENTRO (composição!)
            itens[qtdItens] = new ItemPedido(produto, quantidade, precoUnitario);
            qtdItens++;
            System.out.println("Item adicionado: " + produto);
        } else {
            System.out.println("Pedido cheio! Não é possível adicionar mais itens.");
        }
    }

    // Remove um item do pedido pelo nome do produto
    // (na composição, remover = destruir a parte)
    public void removerItem(String produto) {
        for (int i = 0; i < qtdItens; i++) {
            if (itens[i].getProduto().equals(produto)) {
                System.out.println("Item removido: " + produto);
                // Shift para esquerda
                for (int j = i; j < qtdItens - 1; j++) {
                    itens[j] = itens[j + 1];
                }
                itens[qtdItens - 1] = null;   // libera a referência (item será coletado)
                qtdItens--;
                return;
            }
        }
        System.out.println("Item '" + produto + "' não encontrado no pedido.");
    }

    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            total += itens[i].getSubtotal();
        }
        return total;
    }

    public void exibirPedido() {
        System.out.println("\n========================================");
        System.out.println("           PEDIDO #" + numero);
        System.out.println("========================================");
        System.out.println("Cliente: " + cliente);
        System.out.println("Data: " + data);
        System.out.println("----------------------------------------");
        System.out.println("Itens:");
        if (qtdItens == 0) {
            System.out.println("  (pedido vazio)");
        } else {
            for (int i = 0; i < qtdItens; i++) {
                itens[i].exibir();
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("TOTAL: R$ " + String.format("%.2f", calcularTotal()));
        System.out.println("========================================\n");
    }

    public int getNumero() {
        return numero;
    }

    public String getCliente() {
        return cliente;
    }

    public int getQtdItens() {
        return qtdItens;
    }
}
