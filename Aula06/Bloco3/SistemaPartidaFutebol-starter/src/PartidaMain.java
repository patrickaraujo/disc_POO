// ═══════════════════════════════════════════════════════════════════
// CLASSE PRINCIPAL — monta o cenário e testa tudo que vocês implementaram.
//
// A criação dos jogadores e times já está pronta (é só Bloco 2).
// Falta criar a partida e registrar os gols em aula.
// ═══════════════════════════════════════════════════════════════════

public class PartidaMain {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE PARTIDA DE FUTEBOL         ║");
        System.out.println("║     Aula 06 - Bloco 3                     ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        // ─── PARTE PRONTA: agregação (Bloco 2) ─────────────────────
        System.out.println("--- Criando jogadores ---");
        Jogador vini     = new Jogador("Vini Jr",    7, "Atacante");
        Jogador rodrygo  = new Jogador("Rodrygo",   11, "Atacante");
        Jogador casemiro = new Jogador("Casemiro",   5, "Volante");
        Jogador alisson  = new Jogador("Alisson",    1, "Goleiro");

        Jogador messi    = new Jogador("Messi",     10, "Atacante");
        Jogador dimaria  = new Jogador("Di María",  11, "Meia");
        Jogador dibu     = new Jogador("Dibu",       1, "Goleiro");

        System.out.println("\n--- Montando os times (agregação) ---");
        Time brasil    = new Time("Brasil",    11);
        Time argentina = new Time("Argentina", 11);

        brasil.adicionarJogador(vini);
        brasil.adicionarJogador(rodrygo);
        brasil.adicionarJogador(casemiro);
        brasil.adicionarJogador(alisson);

        argentina.adicionarJogador(messi);
        argentina.adicionarJogador(dimaria);
        argentina.adicionarJogador(dibu);

        brasil.listarJogadores();
        argentina.listarJogadores();

        // ─── PARTE A CODAR EM AULA ─────────────────────────────────
        //
        // TODO 10: criar a partida
        //
        // Partida classico = new Partida(brasil, argentina, "21/11/2026", "Maracanã");
        //
        // Depois, descomente os registros de gol abaixo:
        //
        // classico.registrarGol(brasil,     7, 12, "normal");   // Vini Jr
        // classico.registrarGol(argentina, 10, 34, "pênalti");  // Messi
        // classico.registrarGol(argentina, 11, 67, "falta");    // Di María
        // classico.registrarGol(brasil,    11, 89, "normal");   // Rodrygo
        //
        // classico.exibirSumula();
        //
        // ───────────────────────────────────────────────────────────


        // ─── DISCUSSÃO FINAL (depois que tudo estiver funcionando) ─
        //
        // Pergunte à turma:
        //   1) O que acontece com os gols se o VAR anular a partida?
        //   2) O jogador Vini Jr continua existindo após o fim do jogo?
        //   3) Por que o construtor de Gol não tem `public`?
        //
        // Experimento opcional — descomente para ver que Vini Jr
        // continua existindo:
        //
        // System.out.println("\n--- Prova: Vini Jr ainda existe ---");
        // vini.apresentar();
        //
        // ───────────────────────────────────────────────────────────
    }
}
