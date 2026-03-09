public class EstacionamentoMain {

    public static void main(String[] args) {

        Estacionamento e1 = new Estacionamento();
        e1.setNome("Estacionamento Central");
        e1.setTotalVagas(3);

        e1.entrarVeiculo();
        e1.entrarVeiculo();
        e1.entrarVeiculo();
        e1.entrarVeiculo();  // lotado

        e1.sairVeiculo();

        System.out.println();
        e1.exibirStatus();
    }
}
