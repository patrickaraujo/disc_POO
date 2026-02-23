public class VeiculoMain {

    public static void main(String[] args) {

        Veiculo v1 = new Veiculo();
        v1.placa = "ABC-1234";
        v1.modelo = "Fiat Uno";

        Veiculo v2 = new Veiculo();
        v2.placa = "XYZ-5678";
        v2.modelo = "Honda Civic";

        v1.entrar();
        v2.entrar();

        v1.exibirStatus();
        v2.exibirStatus();

        // Tentativa de entrar novamente (deve exibir aviso)
        v1.entrar();

        // v2 sai após 3 horas
        v2.sair(3);

        System.out.println();
        System.out.println("=== Status final ===");
        v1.exibirStatus();
        v2.exibirStatus();

        // v1 sai após 1 hora
        v1.sair(1);
    }
}
