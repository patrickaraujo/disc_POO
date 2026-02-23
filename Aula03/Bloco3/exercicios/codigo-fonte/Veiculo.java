public class Veiculo {

    String placa;
    String modelo;
    boolean estacionado;
    int horasEstacionado;

    void entrar() {
        if (estacionado) {
            System.out.println("Veículo " + placa + " já está estacionado.");
            return;
        }
        estacionado = true;
        horasEstacionado = 0;
        System.out.println("Veículo " + placa + " (" + modelo + ") entrou no estacionamento.");
    }

    void sair(int horas) {
        if (!estacionado) {
            System.out.println("Veículo " + placa + " não está estacionado.");
            return;
        }
        horasEstacionado = horas;
        double valor = calcularValor();
        estacionado = false;
        System.out.println("Veículo " + placa + " saiu. Horas: " + horas + " | Total: R$ " + valor);
    }

    double calcularValor() {
        if (horasEstacionado <= 1) {
            return 5.0;
        }
        return 5.0 + (horasEstacionado - 1) * 3.0;
    }

    void exibirStatus() {
        String status = estacionado ? "Estacionado" : "Fora do estacionamento";
        System.out.println(placa + " | " + modelo + " | " + status);
    }
}
