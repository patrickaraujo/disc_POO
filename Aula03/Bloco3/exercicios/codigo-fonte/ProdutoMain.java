public class ProdutoMain {

    public static void main(String[] args) {

        Produto p1 = new Produto();
        p1.nome = "Notebook";
        p1.preco = 3500.0;
        p1.quantidadeEstoque = 10;

        Produto p2 = new Produto();
        p2.nome = "Mouse";
        p2.preco = 120.0;
        p2.quantidadeEstoque = 2;

        System.out.println("=== Antes ===");
        p1.exibirInformacoes();
        p2.exibirInformacoes();

        p1.aplicarDesconto(10.0);
        p2.reporEstoque(8);
        p1.aplicarDesconto(-5.0);   // deve exibir erro

        System.out.println();
        System.out.println("=== Depois ===");
        p1.exibirInformacoes();
        p2.exibirInformacoes();
    }
}
