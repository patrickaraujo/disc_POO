public class ItemPedido {
    private String produto;
    private int quantidade;
    private double precoUnitario;

    // Construtor package-private (só a classe Pedido, no mesmo pacote, pode criar)
    // Isso impede `new ItemPedido(...)` fora do pacote e reforça a COMPOSIÇÃO.
    ItemPedido(String produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    public void exibir() {
        System.out.println("  - " + produto + " | " + quantidade + "x | R$ " +
                          String.format("%.2f", precoUnitario) + " | Subtotal: R$ " +
                          String.format("%.2f", getSubtotal()));
    }
}
