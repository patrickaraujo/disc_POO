// ═══════════════════════════════════════════════════════════════════
// CLASSE NOVA — o "todo" que integra os TRÊS relacionamentos da aula.
//
//   Partida → Time  (ASSOCIAÇÃO: mandante e visitante)
//   Partida ◆ Gol   (COMPOSIÇÃO: gols criados DENTRO da partida)
//   Gol    → Jogador (ASSOCIAÇÃO: referência a quem marcou)
//
// CONCEITO-CHAVE (será discutido em aula):
//   O método registrarGol() é onde a composição acontece de fato.
//   Em vez de receber um `Gol` pronto por parâmetro (o que seria
//   agregação), ele vai CRIAR o Gol internamente com `new Gol(...)`.
//   Isso é a essência da composição.
// ═══════════════════════════════════════════════════════════════════

public class Partida {

    // ───────────────────────────────────────────────────────────────
    // TODO 6 (em aula): declarar os atributos da Partida
    //
    // Precisamos de:
    //   - mandante        (Time)    ← ASSOCIAÇÃO
    //   - visitante       (Time)    ← ASSOCIAÇÃO
    //   - data            (String)
    //   - estadio         (String)
    //   - gols            (Gol[])   ← COMPOSIÇÃO
    //   - qtdGols         (int)     ← contador de gols no array
    //   - placarMandante  (int)
    //   - placarVisitante (int)
    //
    // Todos `private`.
    // ───────────────────────────────────────────────────────────────

    // TODO: declarar atributos aqui


    // ───────────────────────────────────────────────────────────────
    // TODO 7 (em aula): construtor
    //
    // Assinatura:
    //   public Partida(Time mandante, Time visitante, String data, String estadio)
    //
    // Deve:
    //   - atribuir os 4 parâmetros aos atributos correspondentes
    //   - inicializar `gols = new Gol[30];`  (30 é um limite generoso)
    //   - inicializar qtdGols, placarMandante, placarVisitante com 0
    // ───────────────────────────────────────────────────────────────

    // TODO: construtor aqui


    // ───────────────────────────────────────────────────────────────
    // TODO 8 (em aula — O CORAÇÃO DA COMPOSIÇÃO!): registrarGol
    //
    // Assinatura:
    //   public void registrarGol(Time time, int numeroJogador, int minuto, String tipo)
    //
    // Passos:
    //   1) Usar `time.buscarPorNumero(numeroJogador)` para encontrar
    //      o jogador que marcou. Guardar numa variável Jogador marcador.
    //      Se marcador for null, imprimir mensagem de erro e return.
    //
    //   2) CRIAR o Gol AQUI DENTRO (essa é a composição!):
    //        gols[qtdGols] = new Gol(minuto, marcador, tipo);
    //        qtdGols++;
    //
    //   3) Atualizar o placar:
    //        - se `time == mandante`, incrementar placarMandante
    //        - senão, incrementar placarVisitante
    //
    //   4) Imprimir: "⚽ GOL de <nome do marcador> aos <minuto>'!"
    // ───────────────────────────────────────────────────────────────

    public void registrarGol(Time time, int numeroJogador, int minuto, String tipo) {
        // TODO: implementar
    }


    // ───────────────────────────────────────────────────────────────
    // TODO 9 (em aula): exibirSumula
    //
    // Deve imprimir o cabeçalho, placar e a lista de gols.
    // Formato sugerido:
    //
    //   ═══════════════════════════════════════════
    //     SÚMULA — <data> — <estadio>
    //   ═══════════════════════════════════════════
    //     <nome mandante>  <placar> x <placar>  <nome visitante>
    //   ───────────────────────────────────────────
    //     Gols:
    //     (iterar de 0 até qtdGols-1 chamando gols[i].exibir())
    //   ═══════════════════════════════════════════
    //
    // Se qtdGols == 0, imprimir "(partida sem gols)".
    // ───────────────────────────────────────────────────────────────

    public void exibirSumula() {
        // TODO: implementar
    }


    // ───────────────────────────────────────────────────────────────
    // Getters simples (pode deixar para o final)
    // ───────────────────────────────────────────────────────────────

    // TODO (opcional): getPlacarMandante(), getPlacarVisitante(), getQtdGols()
}
