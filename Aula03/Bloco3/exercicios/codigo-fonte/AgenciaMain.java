public class AgenciaMain {

    public static void main(String[] args) {

        ContaBancaria c1 = new ContaBancaria();
        c1.titular = "Ana";
        c1.numero = "001-1";
        c1.saldo = 2000.0;

        ContaBancaria c2 = new ContaBancaria();
        c2.titular = "Bruno";
        c2.numero = "002-2";
        c2.saldo = 800.0;

        ContaBancaria c3 = new ContaBancaria();
        c3.titular = "Carla";
        c3.numero = "003-3";
        c3.saldo = 0.0;

        System.out.println("=== Operações ===");

        // 1. Ana realiza um depósito
        c1.depositar(500.0);

        // 2. Ana transfere R$ 300 para Bruno
        System.out.println("-- Transferência de Ana para Bruno --");
        c1.sacar(300.0);
        c2.depositar(300.0);

        // 3. Carla tenta sacar com saldo zero
        c3.sacar(100.0);

        // 4. Carla recebe depósito
        c3.depositar(250.0);

        // Saldo final
        System.out.println();
        System.out.println("=== Saldo Final ===");
        c1.exibirSaldo();
        System.out.println();
        c2.exibirSaldo();
        System.out.println();
        c3.exibirSaldo();
    }
}
