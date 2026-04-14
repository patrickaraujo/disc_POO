public class PartidaMain {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE PARTIDA DE FUTEBOL         ║");
        System.out.println("║     Aula 06 - Bloco 3 - Exercício Guiado  ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");

        // --- AGREGAÇÃO (Bloco 2): criar jogadores e times ---
        System.out.println("--- Criando jogadores (existem independentemente) ---");
        Jogador vini     = new Jogador("Vini Jr",    7, "Atacante");
        Jogador rodrygo  = new Jogador("Rodrygo",   11, "Atacante");
        Jogador casemiro = new Jogador("Casemiro",   5, "Volante");
        Jogador alisson  = new Jogador("Alisson",    1, "Goleiro");

        Jogador messi    = new Jogador("Messi",     10, "Atacante");
        Jogador dimaria  = new Jogador("Di María",  11, "Meia");
        Jogador dibu     = new Jogador("Dibu",       1, "Goleiro");

        System.out.println("\n--- Montando times (agregação) ---");
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

        // --- ASSOCIAÇÃO + COMPOSIÇÃO: criar a partida ---
        System.out.println("\n--- Criando a partida (associação com os times) ---");
        Partida classico = new Partida(brasil, argentina, "21/11/2026", "Maracanã");

        // --- COMPOSIÇÃO: registrar gols (criados DENTRO da partida) ---
        System.out.println("\n--- Partida começou! Registrando gols (composição) ---");
        classico.registrarGol(brasil,     7, 12, "normal");   // Vini Jr
        classico.registrarGol(argentina, 10, 34, "pênalti");  // Messi
        classico.registrarGol(argentina, 11, 67, "falta");    // Di María
        classico.registrarGol(brasil,    11, 89, "normal");   // Rodrygo

        // Teste de validação: jogador inexistente
        System.out.println("\n--- Tentando registrar gol com número inexistente ---");
        classico.registrarGol(brasil, 99, 90, "normal");

        // Exibir súmula final
        classico.exibirSumula();

        // --- DEMONSTRANDO OS CICLOS DE VIDA ---
        System.out.println("=== ANÁLISE DOS CICLOS DE VIDA ===\n");
        System.out.println("Após o fim da partida:");
        System.out.println("  • Os times continuam existindo ........ (associação) ✓");
        System.out.println("  • Os jogadores continuam existindo .... (agregação no time) ✓");
        System.out.println("  • Os gols SÓ existem dentro da partida  (composição) ✓");

        System.out.println("\nProva: Vini Jr ainda existe, mesmo depois do apito final:");
        vini.apresentar();

        System.out.println("\nE pode até ser transferido para outro time (agregação):");
        Time realMadrid = new Time("Real Madrid", 25);
        brasil.removerJogador("Vini Jr");
        realMadrid.adicionarJogador(vini);

        // --- SEGUNDA PARTIDA: mostrando que a composição é por partida ---
        System.out.println("\n\n--- Nova partida: os gols anteriores NÃO vêm junto ---");
        Partida amistoso = new Partida(brasil, argentina, "15/06/2027", "Monumental");
        amistoso.registrarGol(argentina, 10, 5, "normal");
        amistoso.registrarGol(brasil, 11, 78, "normal");
        amistoso.exibirSumula();

        System.out.println("Observe: cada Partida tem SEU PRÓPRIO conjunto de gols.");
        System.out.println("Isso é COMPOSIÇÃO: gols pertencem à partida que os criou.");
    }
}
