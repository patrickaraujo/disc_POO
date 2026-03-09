public class Estacionamento {

    private String nome;
    private int totalVagas;
    private int vagasOcupadas;

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public int getTotalVagas() {
        return totalVagas;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    // Getter calculado
    public int getVagasDisponiveis() {
        return totalVagas - vagasOcupadas;
    }

    // --- Setters ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setTotalVagas(int total) {
        if (this.totalVagas != 0) {
            System.out.println("Erro: total de vagas já foi definido.");
            return;
        }
        if (total <= 0) {
            System.out.println("Erro: total de vagas deve ser positivo.");
            return;
        }
        this.totalVagas = total;
    }

    // --- Métodos de negócio ---

    public void entrarVeiculo() {
        if (vagasOcupadas >= totalVagas) {
            System.out.println("Estacionamento lotado.");
            return;
        }
        vagasOcupadas++;
        System.out.println("Veículo entrou. Vagas ocupadas: " + vagasOcupadas);
    }

    public void sairVeiculo() {
        if (vagasOcupadas <= 0) {
            System.out.println("Estacionamento vazio.");
            return;
        }
        vagasOcupadas--;
        System.out.println("Veículo saiu. Vagas ocupadas: " + vagasOcupadas);
    }

    public void exibirStatus() {
        System.out.println("=== " + nome + " ===");
        System.out.println("Total de vagas: " + totalVagas);
        System.out.println("Vagas ocupadas: " + vagasOcupadas);
        System.out.println("Vagas disponíveis: " + getVagasDisponiveis());
    }
}
