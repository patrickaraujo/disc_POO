// ═══════════════════════════════════════════════════════════════════
// CLASSE PRONTA (Bloco 2 - agregação). Não precisa modificar nada.
// ═══════════════════════════════════════════════════════════════════

public class Jogador {
    private String nome;
    private int numero;
    private String posicao;

    public Jogador(String nome, int numero, String posicao) {
        this.nome = nome;
        this.numero = numero;
        this.posicao = posicao;
    }

    public String getNome() {
        return nome;
    }

    public int getNumero() {
        return numero;
    }

    public String getPosicao() {
        return posicao;
    }

    public void apresentar() {
        System.out.println("#" + numero + " - " + nome + " (" + posicao + ")");
    }
}
