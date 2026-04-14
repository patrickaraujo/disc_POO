public class Gol {
    private int minuto;
    private Jogador marcador;    // ← ASSOCIAÇÃO: Gol referencia Jogador
    private String tipo;         // "normal", "pênalti", "falta", "contra"

    // Construtor package-private: só a classe Partida (mesmo pacote) cria gols!
    // Isso impede `new Gol(...)` fora da Partida e reforça a COMPOSIÇÃO.
    Gol(int minuto, Jogador marcador, String tipo) {
        this.minuto = minuto;
        this.marcador = marcador;
        this.tipo = tipo;
    }

    public int getMinuto() {
        return minuto;
    }

    public Jogador getMarcador() {
        return marcador;
    }

    public String getTipo() {
        return tipo;
    }

    public void exibir() {
        String nomeMarcador = (marcador != null) ? marcador.getNome() : "Gol contra";
        System.out.println("  " + minuto + "' — " + nomeMarcador + " (" + tipo + ")");
    }
}
