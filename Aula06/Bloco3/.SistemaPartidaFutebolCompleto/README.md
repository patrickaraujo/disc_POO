# Sistema de Partida de Futebol

Projeto Java do **Exercício Guiado 3** da **Aula 06 — Relacionamentos entre Classes**.

Este sistema demonstra, em um único contexto, os **três tipos de relacionamento** entre classes em Programação Orientada a Objetos:

| Relacionamento | Símbolo UML | Onde aparece no código |
|---|:-:|---|
| **Associação** | → | `Partida` → `Time` (mandante/visitante) e `Gol` → `Jogador` (marcador) |
| **Agregação** | ◇ | `Time` ◇ `Jogador` (array `jogadores[]`) |
| **Composição** | ◆ | `Partida` ◆ `Gol` (array `gols[]`, criados dentro de `registrarGol`) |

---

## Estrutura do projeto

```
SistemaPartidaFutebol/
└── src/
    ├── Jogador.java      (reaproveitado do Bloco 2)
    ├── Time.java         (Bloco 2 + método buscarPorNumero)
    ├── Gol.java          (novo, construtor package-private)
    ├── Partida.java      (novo, integra os três relacionamentos)
    └── PartidaMain.java  (classe principal de teste)
```

---

## Como compilar e executar

Na pasta `src/`:

```bash
javac *.java
java PartidaMain
```

Requer **Java 8 ou superior** (não usa nenhum recurso moderno específico).

---

## O que o programa demonstra

1. **Criação de jogadores** — existem de forma independente
2. **Montagem dos times** — agregação (Brasil e Argentina)
3. **Criação da partida** — associação com os dois times
4. **Registro de gols** — composição (gols criados dentro da `Partida`)
5. **Validação** — tenta registrar gol com jogador inexistente
6. **Exibição da súmula** — mostra placar e lista de gols
7. **Prova dos ciclos de vida:**
   - Vini Jr continua existindo após o fim da partida
   - Vini Jr pode ser **transferido** para o Real Madrid (agregação permite mudança)
   - Uma **segunda partida** tem seu próprio conjunto de gols (composição é por instância)

---

## Saída esperada (resumo)

```
=== Time Brasil ===
#7 - Vini Jr (Atacante)
#11 - Rodrygo (Atacante)
#5 - Casemiro (Volante)
#1 - Alisson (Goleiro)

⚽ GOL de Vini Jr aos 12'!
⚽ GOL de Messi aos 34'!
⚽ GOL de Di María aos 67'!
⚽ GOL de Rodrygo aos 89'!

═══════════════════════════════════════════
  SÚMULA — 21/11/2026 — Maracanã
═══════════════════════════════════════════
  Brasil  2 x 2  Argentina
───────────────────────────────────────────
  Gols:
  12' — Vini Jr (normal)
  34' — Messi (pênalti)
  67' — Di María (falta)
  89' — Rodrygo (normal)
═══════════════════════════════════════════
```

---

## Pontos didáticos para discutir em sala

1. **Por que `Gol` tem construtor sem `public`?**
   → Para impedir `new Gol(...)` fora do pacote — só `Partida` pode criar gols. Isso é **composição** aplicada com proteção de visibilidade.

2. **Se o VAR anular a partida, o que acontece com os gols?**
   → Deixam de existir junto com a `Partida` (composição). Os times e jogadores permanecem.

3. **Por que `Partida` tem referências para `Time`, e não um array?**
   → Porque uma partida tem **exatamente dois** times, e apenas os **conhece** — não os possui. Isso é **associação**, não agregação.

4. **Por que `Gol` referencia `Jogador` em vez de guardar só o nome?**
   → Para manter a informação ligada ao objeto real. Se o jogador mudar de nome ou posição, o gol continua apontando para o jogador correto. É **associação**.

5. **O que aconteceria se `registrarGol` recebesse um `Gol` pronto em vez de criar internamente?**
   → Deixaria de ser composição e viraria agregação. A partida perderia o controle sobre o ciclo de vida do gol.
