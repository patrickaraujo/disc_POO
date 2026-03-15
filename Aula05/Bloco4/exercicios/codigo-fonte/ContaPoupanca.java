public class ContaPoupanca {

    private String titular;
    private double saldo;
    private double taxaRendimentoMensal;

    // Construtor completo
    public ContaPoupanca(String titular, double saldoInicial, double taxaRendimentoMensal) {
        this.titular = (titular == null || titular.isEmpty()) ? "Sem nome" : titular;
        this.saldo = (saldoInicial < 0) ? 0 : saldoInicial;
        this.taxaRendimentoMensal = (taxaRendimentoMensal < 0 || taxaRendimentoMensal > 1) ? 0.005 : taxaRendimentoMensal;
    }

    // Construtor parcial — taxa padrão
    public ContaPoupanca(String titular, double saldoInicial) {
        this(titular, saldoInicial, 0.005);
    }

    // Construtor mínimo — saldo 0 e taxa padrão
    public ContaPoupanca(String titular) {
        this(titular, 0.0, 0.005);
    }

    // --- Getters ---

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getTaxaRendimentoMensal() {
        return taxaRendimentoMensal;
    }

    // --- Setters ---

    public void setTitular(String titular) {
        if (titular == null || titular.isEmpty()) {
            System.out.println("Erro: titular não pode ser vazio.");
            return;
        }
        this.titular = titular;
    }

    public void setTaxaRendimentoMensal(double taxa) {
        if (taxa < 0 || taxa > 1) {
            System.out.println("Erro: taxa deve estar entre 0 e 1.");
            return;
        }
        this.taxaRendimentoMensal = taxa;
    }

    // Sem setSaldo() — saldo só muda via depósito, saque ou rendimento

    // --- Métodos de negócio ---

    public void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: valor deve ser positivo.");
            return;
        }
        saldo = saldo + valor;
        System.out.println("Depósito de R$ " + valor + " realizado. Saldo: R$ " + saldo);
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: valor deve ser positivo.");
            return;
        }
        if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
            return;
        }
        saldo = saldo - valor;
        System.out.println("Saque de R$ " + valor + " realizado. Saldo: R$ " + saldo);
    }

    public void aplicarRendimento() {
        double rendimento = saldo * taxaRendimentoMensal;
        saldo = saldo + rendimento;
        System.out.println("Rendimento aplicado: R$ " + rendimento + " | Novo saldo: R$ " + saldo);
    }

    public void exibirExtrato() {
        System.out.println("=== Extrato ===");
        System.out.println("Titular: " + titular);
        System.out.println("Saldo: R$ " + saldo);
        System.out.println("Taxa mensal: " + (taxaRendimentoMensal * 100) + "%");
        System.out.println();
    }
}
