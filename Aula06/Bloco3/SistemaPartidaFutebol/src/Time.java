public class Time {
    private String nome;
    private Jogador[] jogadores;   // ← AGREGAÇÃO (array de jogadores)
    private int qtdJogadores;

    public Time(String nome, int capacidade) {
        this.nome = nome;
        this.jogadores = new Jogador[capacidade];
        this.qtdJogadores = 0;
    }

    // Adicionar jogador ao time
    public void adicionarJogador(Jogador jogador) {
        if (qtdJogadores < jogadores.length) {
            jogadores[qtdJogadores] = jogador;
            qtdJogadores++;
            System.out.println(jogador.getNome() + " foi adicionado ao time " + nome);
        } else {
            System.out.println("Time completo!");
        }
    }

    // Remover jogador (o jogador continua existindo!)
    public void removerJogador(String nomeJogador) {
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].getNome().equals(nomeJogador)) {
                System.out.println(nomeJogador + " foi removido do time " + nome);

                // Shift array (move todos para a esquerda)
                for (int j = i; j < qtdJogadores - 1; j++) {
                    jogadores[j] = jogadores[j + 1];
                }
                jogadores[qtdJogadores - 1] = null;
                qtdJogadores--;
                return;
            }
        }
        System.out.println(nomeJogador + " não encontrado no time.");
    }

    // Listar jogadores
    public void listarJogadores() {
        System.out.println("\n=== Time " + nome + " ===");
        if (qtdJogadores == 0) {
            System.out.println("Nenhum jogador cadastrado.");
        } else {
            for (int i = 0; i < qtdJogadores; i++) {
                jogadores[i].apresentar();
            }
        }
    }

    // NOVO no Bloco 3: busca um jogador do time pelo número da camisa
    public Jogador buscarPorNumero(int numero) {
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].getNumero() == numero) {
                return jogadores[i];
            }
        }
        return null;
    }

    public String getNome() {
        return nome;
    }

    public int getQtdJogadores() {
        return qtdJogadores;
    }
}
