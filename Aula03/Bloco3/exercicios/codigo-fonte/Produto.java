public class Produto {

    String nome;
    double preco;
    int quantidadeEstoque;

    void exibirInformacoes() {
        System.out.println("Produto:  " + nome);
        System.out.println("Preço:    R$ " + preco);
        System.out.println("Estoque:  " + quantidadeEstoque + " unidades");
        System.out.println("----------");
    }

    void aplicarDesconto(double percentual) {
        if (percentual <= 0 || percentual >= 100) {
            System.out.println("Percentual inválido: " + percentual);
            return;
        }
        double desconto = preco * percentual / 100;
        preco = preco - desconto;
        System.out.println("Desconto de " + percentual + "% aplicado em " + nome + ".");
    }

    void reporEstoque(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida para reposição.");
            return;
        }
        quantidadeEstoque = quantidadeEstoque + quantidade;
        System.out.println("Estoque de " + nome + " reposto em " + quantidade + " unidades.");
    }
}
