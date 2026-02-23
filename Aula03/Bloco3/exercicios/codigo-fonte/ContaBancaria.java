public class ContaBancaria {

    String titular;
    String numero;
    double saldo;

    void depositar(double valor) {
        if (valor > 0) {
            saldo = saldo + valor;
            System.out.println("Depósito de R$ " + valor + " realizado.");
            System.out.println("Novo saldo: R$ " + saldo);
        } else {
            System.out.println("Valor inválido para depósito.");
        }
    }

    void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para saque.");
        } else if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$ " + valor + " realizado.");
            System.out.println("Novo saldo: R$ " + saldo);
        }
    }

    void exibirSaldo() {
        System.out.println("=== Conta " + numero + " ===");
        System.out.println("Titular: " + titular);
        System.out.println("Saldo:   R$ " + saldo);
    }

    // Desafio extra do Exercício 03
    void transferir(ContaBancaria destino, double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para transferência.");
            return;
        }
        if (valor > saldo) {
            System.out.println("Saldo insuficiente para transferência.");
            return;
        }
        this.sacar(valor);
        destino.depositar(valor);
        System.out.println("Transferência de R$ " + valor + " concluída.");
    }
}
