# Sistema de Partida de Futebol — Starter

Esqueleto do **Exercício Guiado 3** da **Aula 06 — Bloco 3** para codar em sala com os alunos.

O que já foi visto em aulas anteriores está pronto. O que é conteúdo novo desta aula está marcado como `TODO` para ser implementado em conjunto com a turma.

---

## Status de cada arquivo

| Arquivo | Status | O que falta |
|---|---|---|
| `Jogador.java` | ✅ Completo (Bloco 2) | Nada |
| `Time.java` | 🔶 Quase completo (Bloco 2) | Implementar `buscarPorNumero()` — TODO 1 |
| `Gol.java` | 🆕 Esqueleto | Atributos, construtor package-private, getters, `exibir()` — TODOs 2 a 5 |
| `Partida.java` | 🆕 Esqueleto | Atributos, construtor, `registrarGol()`, `exibirSumula()` — TODOs 6 a 9 |
| `PartidaMain.java` | 🔶 Parcialmente pronto | Criar a partida e registrar gols — TODO 10 |

> **O esqueleto compila do zero.** Os alunos podem rodar `javac *.java` a qualquer momento e ir testando incrementalmente conforme cada TODO é resolvido.

---

## Sequência sugerida de aula

### Momento 1 — Motivação e design (~10 min)
Desenhar no quadro:
```
Partida → Time     (associação, 2x)
Partida ◆ Gol      (composição — foco da aula!)
Gol    → Jogador   (associação)
Time   ◇ Jogador   (agregação — já visto)
```
Discutir por que cada tipo. Chegar à pergunta: **"Se o VAR anular a partida, o que acontece com os gols?"** → deixam de existir → composição.

### Momento 2 — Preparação em Time (~5 min)
**TODO 1:** implementar `buscarPorNumero()` em `Time.java`. Conceito simples (busca linear), serve de aquecimento.

### Momento 3 — Classe Gol (~15 min)
**TODOs 2 a 5:** construir a classe `Gol`.

Foco pedagógico: o construtor **sem `public`** (package-private). Explique:
> "Por que não vamos deixar `public`? Porque se deixarmos, qualquer lugar do código poderia fazer `new Gol(...)` — e isso quebra nossa regra de que gols só existem dentro de partidas. Sem `public`, só classes do mesmo pacote (ou seja, `Partida`) poderão criar gols. Isso é composição ensinada pela própria linguagem."

### Momento 4 — Classe Partida (~20 min)
**TODOs 6 a 9:** construir `Partida`.

O **TODO 8 (`registrarGol`)** é o ponto alto da aula. Peça para a turma observar esta linha:
```java
gols[qtdGols] = new Gol(minuto, marcador, tipo);
```
Pergunte: *"Onde esse `new Gol` está acontecendo? Dentro ou fora da Partida?"* → Dentro → **composição**.

### Momento 5 — Integração e teste (~10 min)
**TODO 10:** descomentar e rodar `PartidaMain`. Ver a súmula imprimindo o clássico Brasil × Argentina.

### Momento 6 — Discussão final (~5 min)
Três perguntas de fechamento (estão comentadas no fim de `PartidaMain`):
1. O que acontece com os gols se o VAR anular a partida?
2. O jogador Vini Jr continua existindo após o fim do jogo?
3. Por que o construtor de `Gol` não tem `public`?

---

## Como compilar e executar

Na pasta `src/`:

```bash
javac *.java
java PartidaMain
```

Requer **Java 8 ou superior**. O projeto **compila mesmo com os TODOs vazios** (pois os métodos têm corpos stub), então dá para compilar e rodar a qualquer momento durante a aula e ver o progresso.

---

## Saída esperada ao final (com tudo implementado)

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

## Dicas de condução

- **Deixe o aluno errar um pouco** no TODO 3 — vários vão colocar `public` por hábito. Essa é a oportunidade perfeita para discutir visibilidade.
- **Ao chegar no TODO 8**, pare e pergunte: *"E se eu recebesse um `Gol` pronto por parâmetro em vez de criar aqui dentro? O que mudaria?"* → Resposta: deixaria de ser composição.
- **Compile e rode após cada TODO** para dar feedback imediato.
- **Em caso de sobra de tempo:** proponha implementar `registrarGolContra(Time, int numeroJogador, int minuto)` — o jogador marca mas o ponto vai pro adversário.
