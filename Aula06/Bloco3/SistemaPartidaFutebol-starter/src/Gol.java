// ═══════════════════════════════════════════════════════════════════
// CLASSE NOVA — é a "parte" da composição Partida ◆ Gol.
//
// Um Gol só faz sentido DENTRO de uma Partida. Se a partida for
// anulada, seus gols deixam de existir.
//
// CONCEITO-CHAVE (será discutido em aula):
//   O construtor desta classe NÃO terá o modificador `public`.
//   Ele será "package-private" — ou seja, só classes do mesmo
//   pacote poderão chamar `new Gol(...)`. Como Partida está no
//   mesmo pacote, só ela conseguirá criar gols. Código externo
//   fica impedido de instanciar Gol por fora — exatamente o que
//   queremos na composição.
// ═══════════════════════════════════════════════════════════════════

public class Gol {

    // ───────────────────────────────────────────────────────────────
    // TODO 2 (em aula): declarar os atributos do Gol
    //
    // Um gol tem:
    //   - minuto em que aconteceu (int)
    //   - jogador que marcou (Jogador)  ← ASSOCIAÇÃO com Jogador
    //   - tipo do gol: "normal", "pênalti", "falta", "contra" (String)
    //
    // Todos os atributos devem ser `private`.
    // ───────────────────────────────────────────────────────────────

    // TODO: declarar atributos aqui


    // ───────────────────────────────────────────────────────────────
    // TODO 3 (em aula): criar o construtor SEM `public`
    //
    // Assinatura: Gol(int minuto, Jogador marcador, String tipo)
    //
    // IMPORTANTE: NÃO coloque `public` antes de Gol(...).
    // O construtor precisa ser package-private para impedir
    // que código externo faça `new Gol(...)`.
    //
    // O corpo deve apenas atribuir os parâmetros aos atributos
    // (this.minuto = minuto; etc).
    // ───────────────────────────────────────────────────────────────

    // TODO: construtor package-private aqui


    // ───────────────────────────────────────────────────────────────
    // TODO 4 (em aula): criar os getters
    //
    // Precisamos de: getMinuto(), getMarcador(), getTipo()
    // ───────────────────────────────────────────────────────────────

    // TODO: getters aqui


    // ───────────────────────────────────────────────────────────────
    // TODO 5 (em aula): implementar exibir()
    //
    // Deve imprimir algo no formato:
    //   "  12' — Vini Jr (normal)"
    //
    // Atenção: se `marcador` for null, imprimir "Gol contra"
    // no lugar do nome (gols contra não têm marcador definido).
    // ───────────────────────────────────────────────────────────────

    public void exibir() {
        // TODO: implementar
    }
}
