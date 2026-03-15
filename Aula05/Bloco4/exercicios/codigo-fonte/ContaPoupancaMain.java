public class ContaPoupancaMain {

    public static void main(String[] args) {

        // Três formas de criar conta
        ContaPoupanca conta1 = new ContaPoupanca("Ana", 1000.0, 0.01);
        ContaPoupanca conta2 = new ContaPoupanca("Carlos", 500.0);
        ContaPoupanca conta3 = new ContaPoupanca("Maria");

        // Depositar na conta3
        conta3.depositar(200.0);
        System.out.println();

        // Aplicar rendimento em todas
        conta1.aplicarRendimento();
        conta2.aplicarRendimento();
        conta3.aplicarRendimento();
        System.out.println();

        // Exibir extratos
        conta1.exibirExtrato();
        conta2.exibirExtrato();
        conta3.exibirExtrato();
    }
}
