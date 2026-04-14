# Bloco 3 — Composição e Múltiplos Relacionamentos

## Objetivos do Bloco

- Entender **composição** como o relacionamento mais forte entre classes
- Diferenciar composição de agregação pelo **ciclo de vida** dos objetos
- Implementar composição criando objetos **dentro** da classe-todo
- Integrar **associação + agregação + composição** em um único sistema
- Tomar decisões de design baseadas no mundo real

---

## Parte A — Composição: "É Composto De" (Vida Dependente)

### 3.1 O que é Composição?

**Composição** é o tipo mais forte de relacionamento entre classes, onde:

> "Um objeto **é composto de** outros objetos, e essas partes **não podem existir** sem o todo."

### Diferença crucial: Agregação vs Composição

| Aspecto | Agregação (Bloco 2) | Composição (agora) |
|---------|-----------|------------|
| **Relação** | "tem um" (independente) | "é composto de" (dependente) |
| **Ciclo de vida** | Partes existem sem o todo | Partes morrem com o todo |
| **Criação** | Partes criadas **fora** | Partes criadas **dentro** |
| **Exemplo** | Time tem Jogadores | Casa é composta de Quartos |
| **UML** | ◇ (losango vazio) | ◆ (losango cheio) |

**A chave:** na composição, **destruir o todo significa destruir as partes**.

---

### 3.2 Analogia do mundo real

#### Casa e Quartos (Composição)

```
┌──────────────────────────────┐
│          Casa                │
│                              │
│  COMPOSTA DE                 │
│  - sala                      │
│  - cozinha                   │
│  - quartos[]                 │
│                              │
│  Se a casa for demolida,     │
│  os quartos deixam de existir│
└──────────────────────────────┘
        │
        │ Quartos são criados
        │ JUNTO com a casa
        │
    ┌───▼────────────────────┐
    │   new Quarto(...)      │
    │   new Cozinha(...)     │
    └────────────────────────┘
```

**Características:**
- A casa **É COMPOSTA DE** quartos (não existe quarto sem casa)
- Se demolir a casa, os quartos **deixam de existir**
- Quartos são **criados no construtor** da casa
- Isso é **COMPOSIÇÃO**

---

### 3.3 Implementando Composição em Java

#### Exemplo: Pedido de Compra e Itens

Um `Pedido` é composto de `ItemPedido`. Os itens não fazem sentido fora do pedido.

```java
public class ItemPedido {
    private String produto;
    private int quantidade;
    private double precoUnitario;

    // Construtor package-private (só a classe Pedido cria)
    ItemPedido(String produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    public void exibir() {
        System.out.println("  - " + produto + " | " + quantidade + "x | R$ " +
                          String.format("%.2f", precoUnitario) + " | Subtotal: R$ " +
                          String.format("%.2f", getSubtotal()));
    }
}
```

```java
public class Pedido {
    private int numero;
    private String cliente;
    private String data;
    private ItemPedido[] itens;  // ← COMPOSIÇÃO
    private int qtdItens;

    public Pedido(int numero, String cliente, String data, int capacidade) {
        this.numero = numero;
        this.cliente = cliente;
        this.data = data;
        this.itens = new ItemPedido[capacidade];
        this.qtdItens = 0;
    }

    // Método que CRIA itens (composição!)
    public void adicionarItem(String produto, int quantidade, double precoUnitario) {
        if (qtdItens < itens.length) {
            // Cria o ItemPedido AQUI DENTRO (composição!)
            itens[qtdItens] = new ItemPedido(produto, quantidade, precoUnitario);
            qtdItens++;
            System.out.println("Item adicionado: " + produto);
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            total += itens[i].getSubtotal();
        }
        return total;
    }

    public void exibirPedido() {
        System.out.println("\n=== PEDIDO #" + numero + " ===");
        System.out.println("Cliente: " + cliente + " | Data: " + data);
        for (int i = 0; i < qtdItens; i++) {
            itens[i].exibir();
        }
        System.out.println("TOTAL: R$ " + String.format("%.2f", calcularTotal()));
    }
}
```

**O que isso prova?**
- ✅ `ItemPedido` é criado **DENTRO** de `Pedido`
- ✅ `ItemPedido` **NÃO PODE** ser instanciado de fora (construtor package-private)
- ✅ Se `Pedido` for destruído, seus `ItemPedido` também são
- ✅ Isso é **COMPOSIÇÃO**

---

### 3.4 Representação em UML

```
┌──────────────────┐
│     Pedido       │
├──────────────────┤
│ - numero         │
│ - cliente        │
│ - itens[]        │◆────────┐  Losango CHEIO = Composição
└──────────────────┘          │
       1                      1..*
                              │
                      ┌───────▼────────┐
                      │  ItemPedido    │
                      ├────────────────┤
                      │ - produto      │
                      │ - quantidade   │
                      │ - precoUnitario│
                      └────────────────┘
```

**Símbolos importantes:**
- **◆ (losango CHEIO)** = composição
- **1..*** = um ou mais itens (pedido vazio não faz sentido)

---

### 3.5 Quando usar Composição?

Use composição quando:

- ✅ A parte **NÃO FAZ SENTIDO** sem o todo
- ✅ Destruir o todo **deve destruir** as partes
- ✅ A parte é **criada e gerenciada** pelo todo
- ✅ A parte **não é compartilhada** entre múltiplos todos

**Exemplos clássicos:**
- 🏠 Casa **◆** Quartos
- 📄 Pedido **◆** ItensPedido
- 🚗 Carro **◆** Motor
- 📝 Prova **◆** Questões
- ⚽ Partida **◆** Gols (veremos no exercício guiado!)

---

### 3.6 Pergunta-chave para decidir

> **"Se eu deletar o TODO, a PARTE deve ser deletada também?"**

- **SIM** → Composição (◆)
- **NÃO** → Agregação (◇)

---

## Parte B — Múltiplos Relacionamentos em um Sistema

### 3.7 Recapitulação: Os Três Tipos

Antes de integrarmos tudo, vamos revisar os três tipos vistos até aqui:

| Tipo | Símbolo | Significado | Ciclo de Vida | Criação |
|------|---------|-------------|---------------|---------|
| **Associação** | → | "conhece um" | Independentes | Fora |
| **Agregação** | ◇ | "tem um" | Independentes | Fora |
| **Composição** | ◆ | "é composto de" | Dependentes | Dentro |

### Fluxograma de decisão

```
┌─────────────────────────────────┐
│ Um objeto precisa conhecer      │
│ ou conter outro?                │
└──────────┬──────────────────────┘
           ▼
    ┌──────────────┐
    │ É relação    │
    │ TODO-PARTE?  │
    └──┬───────┬───┘
       │ NÃO   │ SIM
       ▼       ▼
  ASSOCIAÇÃO   ┌──────────────────┐
     (→)       │ A parte pode     │
               │ existir sem todo?│
               └──┬───────┬───────┘
                  │ SIM   │ NÃO
                  ▼       ▼
             AGREGAÇÃO  COMPOSIÇÃO
                (◇)        (◆)
```

---

### 3.8 Sistemas reais combinam os três tipos

Na prática, um sistema raramente usa um só tipo de relacionamento. Vamos ver um exemplo didático: **um sistema de partida de futebol**.

```
┌─────────────────────────┐
│       Partida           │
├─────────────────────────┤
│ - timeMandante          │──────► Time (Associação)
│ - timeVisitante         │──────► Time (Associação)
│ - gols[]                │◆────┐  Composição
└─────────────────────────┘     │
                                │
                        ┌───────▼────────┐
                        │      Gol       │
                        ├────────────────┤
                        │ - minuto       │
                        │ - marcador     │───► Jogador (Associação)
                        └────────────────┘

E, do Bloco 2, continuamos tendo:
   Time ◇ Jogadores (Agregação)
```

**Análise do design:**
- **Partida → Time:** a partida **conhece** dois times, mas não os possui (associação)
- **Partida ◆ Gol:** um gol é um **evento da partida** — sem partida, não existe gol (composição)
- **Gol → Jogador:** o gol **referencia** quem marcou, mas não possui o jogador (associação)
- **Time ◇ Jogadores:** já vimos no Bloco 2 — jogadores existem fora do time (agregação)

É esse sistema que você vai implementar no **Exercício Guiado 3**!

---

### 3.9 Erros comuns ao combinar relacionamentos

#### ❌ Erro 1: Confundir agregação com composição

```java
// ❌ ERRADO — parece composição, mas é agregação
public class Partida {
    private Gol[] gols;

    public void registrarGol(Gol gol) {  // ← Recebe pronto de fora!
        // Se o gol foi criado fora, é agregação, não composição.
    }
}

// ✅ CORRETO — composição verdadeira
public class Partida {
    private Gol[] gols;

    public void registrarGol(int minuto, Jogador marcador) {
        gols[qtdGols++] = new Gol(minuto, marcador);  // ← Cria DENTRO
    }
}
```

#### ❌ Erro 2: Não validar null em associações

```java
// ❌ ERRADO
public void exibirMarcador() {
    System.out.println(marcador.getNome());  // NullPointerException se marcador for null
}

// ✅ CORRETO
public void exibirMarcador() {
    if (marcador != null) {
        System.out.println(marcador.getNome());
    } else {
        System.out.println("Gol contra");
    }
}
```

#### ❌ Erro 3: Expor as partes de uma composição

```java
// ❌ ERRADO — expõe o array interno
public Gol[] getGols() {
    return gols;  // Código externo pode modificar/anular os gols!
}

// ✅ CORRETO — expõe apenas informação derivada
public int getQuantidadeGols() {
    return qtdGols;
}

public void exibirGols() {
    for (int i = 0; i < qtdGols; i++) gols[i].exibir();
}
```

---

## Exercício Guiado 3 — Sistema de Partida de Futebol (professor + alunos)

**Contexto:** No **Bloco 2** você implementou `Time` e `Jogador` (agregação). Agora vamos modelar uma **Partida** entre dois times, com registro de gols (súmula). Esse exercício vai usar os **três tipos de relacionamento ao mesmo tempo**.

### Design do sistema

```
Partida → Time (associação, 2x: mandante e visitante)
Partida ◆ Gol (composição: gol não existe fora da partida)
Gol → Jogador (associação: referência a quem marcou)
Time ◇ Jogador (agregação: já feito no Bloco 2)
```

### Passo 1 — Reaproveitando `Jogador` e `Time` do Bloco 2

As classes `Jogador` e `Time` já foram feitas no Bloco 2. **Reutilize os arquivos**. Vamos apenas adicionar um pequeno incremento: um método utilitário em `Time` para buscar um jogador por número, que usaremos ao registrar gols.

**Adicione em `Time.java`:**

```java
    // Busca um jogador do time pelo número da camisa
    public Jogador buscarPorNumero(int numero) {
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].getNumero() == numero) {
                return jogadores[i];
            }
        }
        return null;
    }
```

### Passo 2 — Crie `Gol.java` (parte da composição)

```java
public class Gol {
    private int minuto;
    private Jogador marcador;    // ← ASSOCIAÇÃO: Gol referencia Jogador
    private String tipo;         // "normal", "pênalti", "falta", "contra"

    // Construtor package-private: só a classe Partida cria gols!
    Gol(int minuto, Jogador marcador, String tipo) {
        this.minuto = minuto;
        this.marcador = marcador;
        this.tipo = tipo;
    }

    public int getMinuto() { return minuto; }
    public Jogador getMarcador() { return marcador; }
    public String getTipo() { return tipo; }

    public void exibir() {
        String nomeMarcador = (marcador != null) ? marcador.getNome() : "Gol contra";
        System.out.println("  " + minuto + "' — " + nomeMarcador + " (" + tipo + ")");
    }
}
```

**Observação didática:** o construtor **sem modificador** (`Gol(...)`) é `package-private`. Isso significa que, fora do pacote, **ninguém pode fazer `new Gol(...)`**. Só a classe `Partida` (no mesmo pacote) poderá criar gols — reforçando a composição.

### Passo 3 — Crie `Partida.java` (integra tudo)

```java
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

    // COMPOSIÇÃO: cria o Gol AQUI DENTRO
    public void registrarGol(Time time, int numeroJogador, int minuto, String tipo) {
        // Descobre qual time marcou e busca o jogador
        Jogador marcador = time.buscarPorNumero(numeroJogador);
        if (marcador == null) {
            System.out.println("Jogador #" + numeroJogador + " não encontrado em " + time.getNome());
            return;
        }

        // Cria o Gol DENTRO da partida (composição!)
        gols[qtdGols] = new Gol(minuto, marcador, tipo);
        qtdGols++;

        // Atualiza placar
        if (time == mandante) {
            placarMandante++;
        } else if (time == visitante) {
            placarVisitante++;
        }

        System.out.println("⚽ GOL de " + marcador.getNome() + " aos " + minuto + "'!");
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

    public int getPlacarMandante() { return placarMandante; }
    public int getPlacarVisitante() { return placarVisitante; }
    public int getQtdGols() { return qtdGols; }
}
```

### Passo 4 — Crie `PartidaMain.java`

```java
public class PartidaMain {
    public static void main(String[] args) {
        // --- AGREGAÇÃO (Bloco 2): criar jogadores e times ---
        Jogador vini     = new Jogador("Vini Jr",    7, "Atacante");
        Jogador rodrygo  = new Jogador("Rodrygo",   11, "Atacante");
        Jogador casemiro = new Jogador("Casemiro",   5, "Volante");
        Jogador alisson  = new Jogador("Alisson",    1, "Goleiro");

        Jogador messi    = new Jogador("Messi",     10, "Atacante");
        Jogador dimaria  = new Jogador("Di María",  11, "Meia");
        Jogador dibu     = new Jogador("Dibu",       1, "Goleiro");

        Time brasil    = new Time("Brasil",    11);
        Time argentina = new Time("Argentina", 11);

        brasil.adicionarJogador(vini);
        brasil.adicionarJogador(rodrygo);
        brasil.adicionarJogador(casemiro);
        brasil.adicionarJogador(alisson);

        argentina.adicionarJogador(messi);
        argentina.adicionarJogador(dimaria);
        argentina.adicionarJogador(dibu);

        // --- ASSOCIAÇÃO + COMPOSIÇÃO: criar a partida ---
        Partida classico = new Partida(brasil, argentina, "21/11/2026", "Maracanã");

        // --- COMPOSIÇÃO: registrar gols (criados DENTRO da partida) ---
        classico.registrarGol(brasil,     7, 12, "normal");   // Vini Jr
        classico.registrarGol(argentina, 10, 34, "pênalti");  // Messi
        classico.registrarGol(argentina, 11, 67, "falta");    // Di María
        classico.registrarGol(brasil,    11, 89, "normal");   // Rodrygo

        // Exibir súmula final
        classico.exibirSumula();

        // --- DEMONSTRANDO OS CICLOS DE VIDA ---
        System.out.println("=== O QUE ACONTECE SE A PARTIDA SUMIR? ===");
        System.out.println("- Os times continuam existindo (associação) ✓");
        System.out.println("- Os jogadores continuam existindo (agregação no time) ✓");
        System.out.println("- Os gols DEIXAM de existir (composição) ✓");
        System.out.println();
        System.out.println("Vini Jr ainda existe? Sim:");
        vini.apresentar();
    }
}
```

### Saída esperada (resumida)

```
Vini Jr foi adicionado ao time Brasil
Rodrygo foi adicionado ao time Brasil
...
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

### O que observar com os alunos

| Relacionamento | Onde aparece | Por que esse tipo? |
|---|---|---|
| **Associação** (→) | `Partida` conhece dois `Time` | Times existem antes e depois da partida |
| **Associação** (→) | `Gol` referencia `Jogador` | O jogador não é "dono" pelo gol |
| **Agregação** (◇) | `Time` tem `Jogador[]` | Jogador pode trocar de time |
| **Composição** (◆) | `Partida` tem `Gol[]` | Gol **só faz sentido** dentro da partida |

**Perguntas para provocar a turma:**
1. "Se o VAR anular a partida, o que acontece com os gols?" → Deixam de existir (composição)
2. "Se o jogo terminar, o jogador Vini Jr some?" → Não (agregação com o time)
3. "Por que `Gol` tem construtor sem `public`?" → Para impedir `new Gol(...)` fora da `Partida` (reforço da composição)

---

## Exercício Autônomo 3 — Sistema de E-commerce

**Contexto:** Agora você vai aplicar os três tipos de relacionamento em um domínio **diferente**: uma loja online. Isso testa se você entendeu os conceitos para além do contexto de futebol.

**Objetivo:** Modelar uma loja onde clientes montam carrinhos com produtos.

### Classes a criar

1. **`Cliente`** (existe independentemente)
   - Atributos: `nome`, `cpf`, `email`
   - Construtor e getters

2. **`Produto`** (existe independentemente)
   - Atributos: `nome`, `preco`, `estoque`
   - Construtor, getters
   - Métodos: `void darBaixaEstoque(int qtd)`, `boolean temEstoque(int qtd)`

3. **`ItemCarrinho`** (composição — só existe dentro de `Carrinho`)
   - Atributos: `produto` (associação com Produto), `quantidade`
   - Construtor **package-private**
   - Método `double getSubtotal()`
   - Método `void exibir()`

4. **`Carrinho`** (integra os três tipos)
   - Atributos: `cliente` (**associação**), `itens[]` (**composição** com `ItemCarrinho`), `qtdItens`
   - Construtor recebe `Cliente` e capacidade
   - Método `void adicionarProduto(Produto produto, int quantidade)` — **CRIA** um `ItemCarrinho` internamente (note que o `Produto` é apenas referenciado = associação, mas o `ItemCarrinho` é criado dentro = composição)
   - Método `double calcularTotal()`
   - Método `void exibirCarrinho()`
   - Método `void finalizarCompra()` — dá baixa no estoque dos produtos

5. **`Loja`** (agregação com `Carrinho`)
   - Atributos: `nome`, `carrinhos[]`, `qtdCarrinhos`
   - Método `Carrinho criarCarrinho(Cliente cliente)` — retorna um novo carrinho e o registra
   - Método `void listarCarrinhosAtivos()`

### Comportamentos esperados

- Um mesmo `Cliente` pode ter vários carrinhos em lojas diferentes (agregação)
- Um mesmo `Produto` pode aparecer em múltiplos carrinhos (agregação)
- `ItemCarrinho` **só existe dentro de um Carrinho** (composição)
- Se deletar o carrinho, os `ItemCarrinho` somem, mas o `Produto` e o `Cliente` continuam existindo

### Diagrama de relacionamentos

```
 Cliente ◄──── Carrinho ◆────► ItemCarrinho ────► Produto
                 ▲                                   ▲
                 │ (agregação)                       │ (agregação)
                 │                                   │
                Loja ◄─────────────────────────── estoque
```

### Exemplo de uso (esqueleto)

```java
Produto mouse   = new Produto("Mouse Gamer", 89.90, 50);
Produto teclado = new Produto("Teclado Mecânico", 349.90, 30);

Cliente joao = new Cliente("João Silva", "111.222.333-44", "joao@email.com");

Loja techStore = new Loja("TechStore", 100);

Carrinho carrinho = techStore.criarCarrinho(joao);
carrinho.adicionarProduto(mouse, 2);
carrinho.adicionarProduto(teclado, 1);
carrinho.exibirCarrinho();
carrinho.finalizarCompra();
```

### Dicas

- Use arrays simples (sem `ArrayList` ainda, conforme combinado no README)
- Valide estoque **antes** de adicionar o item ao carrinho
- Em `finalizarCompra()`, percorra os itens e chame `darBaixaEstoque` em cada `Produto`
- Pense: **por que `ItemCarrinho` é composição e não agregação?** Porque um item como "2 unidades do Mouse Gamer no carrinho do João" não faz sentido fora daquele carrinho específico

---

## Resumo do Bloco 3

Neste bloco único você aprendeu:

- ✅ **Composição** = parte depende totalmente do todo (**◆**)
- ✅ Partes criadas **DENTRO** do todo (construtor package-private)
- ✅ Destruir o todo **destrói as partes**
- ✅ Como **combinar** os três tipos (associação, agregação, composição) em um mesmo sistema
- ✅ Como **decidir** qual tipo usar via a pergunta: *"Se eu deletar o todo, a parte some?"*
- ✅ Erros comuns: receber parte pronta (vira agregação), não validar null, expor o array interno

### Comparação final dos três tipos

| Tipo | Símbolo | Ciclo de vida | Onde a parte é criada |
|------|:-:|:-:|:-:|
| Associação | → | independentes | fora |
| Agregação | ◇ | independentes | fora |
| Composição | ◆ | parte morre com o todo | dentro |

---

## 🎓 Conclusão da Aula 06

Parabéns! Você completou a Aula 06 e agora domina:

- ✅ **Associação** (→) — "conhece um"
- ✅ **Agregação** (◇) — "tem um" (vida independente)
- ✅ **Composição** (◆) — "é composto de" (vida dependente)
- ✅ Como **decidir** qual relacionamento usar
- ✅ Como **integrar** múltiplos relacionamentos em um único sistema

### Resposta à problematização inicial

> **"Como representar que um Empréstimo precisa de um Livro e um Usuário?"**

- `Biblioteca` **◇ Livros** (agregação — livros existem fora da biblioteca)
- `Biblioteca` **◇ Usuários** (agregação — usuários existem fora da biblioteca)
- `Emprestimo` **→ Livro** e **→ Usuario** (associações — o empréstimo referencia, não possui)
- Se a `Biblioteca` remove um empréstimo quitado, livro e usuário permanecem ✓

**Você está preparado para a Aula 07 — Herança!** 🚀
