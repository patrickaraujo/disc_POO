public class ContaPoupancaMain {

    public static void main(String[] args) {

        ContaPoupanca cp = new ContaPoupanca();
        cp.setTitular("Ana");
        cp.setTaxaRendimentoMensal(0.005);
        cp.depositar(1000.0);

        System.out.println();

        // Simulando 3 meses de rendimento
        for (int mes = 1; mes <= 3; mes++) {
            System.out.println("--- Mês " + mes + " ---");
            cp.aplicarRendimento();
            cp.exibirExtrato();
        }
    }
}
