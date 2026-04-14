public class Partida {
    private Time mandante;         // ← ASSOCIAÇÃO
    private Time visitante;        // ← ASSOCIAÇÃO
    private String data;
    private String estadio;
    private Gol[] gols;            // ← COMPOSIÇÃO
    private int qtdGols;
    private int placarMandante;
    private int placarVisitante;

    public Partida(Time mandante, Time visitante, String data, String estadio) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.data = data;
        this.estadio = estadio;
        this.gols = new Gol[30];   // limite generoso
        this.qtdGols = 0;
        this.placarMandante = 0;
        this.placarVisitante = 0;
    }

    // COMPOSIÇÃO: cria o Gol AQUI DENTRO (new Gol(...))
    public void registrarGol(Time time, int numeroJogador, int minuto, String tipo) {
        // Valida o time
        if (time != mandante && time != visitante) {
            System.out.println("Time " + time.getNome() + " não participa desta partida.");
            return;
        }

        // Busca o jogador no time
        Jogador marcador = time.buscarPorNumero(numeroJogador);
        if (marcador == null) {
            System.out.println("Jogador #" + numeroJogador + " não encontrado em " + time.getNome());
            return;
        }

        // Valida espaço no array de gols
        if (qtdGols >= gols.length) {
            System.out.println("Limite de gols da partida atingido.");
            return;
        }

        // Cria o Gol DENTRO da partida (composição!)
        gols[qtdGols] = new Gol(minuto, marcador, tipo);
        qtdGols++;

        // Atualiza placar
        if (time == mandante) {
            placarMandante++;
        } else {
            placarVisitante++;
        }

        System.out.println("⚽ GOL de " + marcador.getNome() + " aos " + minuto + "'!");
    }

    // Gol contra: o jogador marca, mas o ponto vai pro adversário
    public void registrarGolContra(Time timeDoJogador, int numeroJogador, int minuto) {
        if (timeDoJogador != mandante && timeDoJogador != visitante) {
            System.out.println("Time " + timeDoJogador.getNome() + " não participa desta partida.");
            return;
        }

        Jogador marcador = timeDoJogador.buscarPorNumero(numeroJogador);
        if (marcador == null) {
            System.out.println("Jogador #" + numeroJogador + " não encontrado em " + timeDoJogador.getNome());
            return;
        }

        if (qtdGols >= gols.length) {
            System.out.println("Limite de gols da partida atingido.");
            return;
        }

        gols[qtdGols] = new Gol(minuto, marcador, "contra");
        qtdGols++;

        // Ponto vai para o adversário
        if (timeDoJogador == mandante) {
            placarVisitante++;
        } else {
            placarMandante++;
        }

        System.out.println("⚽ GOL CONTRA de " + marcador.getNome() + " aos " + minuto + "'!");
    }

    public void exibirSumula() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  SÚMULA — " + data + " — " + estadio);
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  " + mandante.getNome() + "  " +
                           placarMandante + " x " + placarVisitante +
                           "  " + visitante.getNome());
        System.out.println("───────────────────────────────────────────");
        System.out.println("  Gols:");
        if (qtdGols == 0) {
            System.out.println("  (partida sem gols)");
        } else {
            for (int i = 0; i < qtdGols; i++) {
                gols[i].exibir();
            }
        }
        System.out.println("═══════════════════════════════════════════\n");
    }

    public int getPlacarMandante() {
        return placarMandante;
    }

    public int getPlacarVisitante() {
        return placarVisitante;
    }

    public int getQtdGols() {
        return qtdGols;
    }

    public String getData() {
        return data;
    }

    public String getEstadio() {
        return estadio;
    }
}
