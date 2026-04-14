// ═══════════════════════════════════════════════════════════════════
// CLASSE DO BLOCO 2 (agregação): o grosso já está pronto.
//
// >>> NOVO NESTA AULA: adicionar o método buscarPorNumero() <<<
//     Vamos usá-lo em Partida para encontrar o jogador que marcou
//     um gol a partir do número da camisa.
// ═══════════════════════════════════════════════════════════════════

public class Time {
    private String nome;
    private Jogador[] jogadores;   // ← AGREGAÇÃO (array de jogadores)
    private int qtdJogadores;

    public Time(String nome, int capacidade) {
        this.nome = nome;
        this.jogadores = new Jogador[capacidade];
        this.qtdJogadores = 0;
    }

    public void adicionarJogador(Jogador jogador) {
        if (qtdJogadores < jogadores.length) {
            jogadores[qtdJogadores] = jogador;
            qtdJogadores++;
            System.out.println(jogador.getNome() + " foi adicionado ao time " + nome);
        } else {
            System.out.println("Time completo!");
        }
    }

    public void removerJogador(String nomeJogador) {
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].getNome().equals(nomeJogador)) {
                System.out.println(nomeJogador + " foi removido do time " + nome);
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

    // ───────────────────────────────────────────────────────────────
    // TODO 1 (em aula): implementar buscarPorNumero
    //
    // Este método deve percorrer o array `jogadores` e retornar
    // o Jogador cuja camisa tenha o `numero` passado como parâmetro.
    // Se nenhum jogador tiver esse número, retornar null.
    //
    // Dica: use um laço `for` até `qtdJogadores` e compare
    //       jogadores[i].getNumero() com o número recebido.
    // ───────────────────────────────────────────────────────────────
    public Jogador buscarPorNumero(int numero) {
        // TODO: implementar
        return null;
    }

    public String getNome() {
        return nome;
    }

    public int getQtdJogadores() {
        return qtdJogadores;
    }
}
