# Bloco 3 — Composição: "É Composto De" (Vida Dependente)

## Objetivos do Bloco

- Entender composição como o relacionamento mais forte entre classes
- Diferenciar composição de agregação pelo ciclo de vida
- Implementar composição criando objetos internamente
- Reconhecer quando usar composição vs agregação

---

## 3.1 O que é Composição?

**Composição** é o tipo mais forte de relacionamento entre classes, onde:

> "Um objeto **é composto de** outros objetos, e essas partes **não podem existir** sem o todo."

### Diferença crucial: Agregação vs Composição

| Aspecto | Agregação | Composição |
|---------|-----------|------------|
| **Relação** | "tem um" (independente) | "é composto de" (dependente) |
| **Ciclo de vida** | Partes existem sem o todo | Partes morrem com o todo |
| **Criação** | Partes criadas fora | Partes criadas dentro |
| **Exemplo** | Time tem Jogadores | Casa tem Quartos |
| **UML** | ◇ (losango vazio) | ◆ (losango cheio) |

**A chave:** Na composição, **destruir o todo significa destruir as partes**.

---

## 3.2 Analogia do mundo real

### Casa e Quartos (Composição)

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
    │   new Sala(...)        │
    └────────────────────────┘
```

**Características:**
- A casa **É COMPOSTA DE** quartos (não existe quarto sem casa)
- Se demolir a casa, os quartos **deixam de existir**
- Quartos são **criados no construtor** da casa
- Isso é **COMPOSIÇÃO**

---

## 3.3 Implementando Composição em Java

### Exemplo: Pedido de Compra e Itens

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
    
    public String getProduto() {
        return produto;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
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
    
    // Construtor: cria o array de itens
    public Pedido(int numero, String cliente, String data, int capacidade) {
        this.numero = numero;
        this.cliente = cliente;
        this.data = data;
        this.itens = new ItemPedido[capacidade];  // ← Criando as partes
        this.qtdItens = 0;
    }
    
    // Método que CRIA itens (composição)
    public void adicionarItem(String produto, int quantidade, double precoUnitario) {
        if (qtdItens < itens.length) {
            // Cria o ItemPedido AQUI DENTRO (composição!)
            itens[qtdItens] = new ItemPedido(produto, quantidade, precoUnitario);
            qtdItens++;
            System.out.println("Item adicionado: " + produto);
        } else {
            System.out.println("Pedido cheio! Não é possível adicionar mais itens.");
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
        System.out.println("\n========================================");
        System.out.println("           PEDIDO #" + numero);
        System.out.println("========================================");
        System.out.println("Cliente: " + cliente);
        System.out.println("Data: " + data);
        System.out.println("----------------------------------------");
        System.out.println("Itens:");
        
        for (int i = 0; i < qtdItens; i++) {
            itens[i].exibir();
        }
        
        System.out.println("----------------------------------------");
        System.out.println("TOTAL: R$ " + String.format("%.2f", calcularTotal()));
        System.out.println("========================================\n");
    }
    
    public int getNumero() { return numero; }
    public String getCliente() { return cliente; }
}
```

### Testando a Composição:

```java
public class PedidoMain {
    public static void main(String[] args) {
        // Criar pedido
        Pedido pedido1 = new Pedido(101, "João Silva", "15/03/2024", 10);
        
        // Adicionar itens (COMPOSIÇÃO - criados dentro do pedido)
        pedido1.adicionarItem("Mouse Gamer", 2, 89.90);
        pedido1.adicionarItem("Teclado Mecânico", 1, 349.90);
        pedido1.adicionarItem("Monitor 24\"", 1, 899.90);
        
        // Exibir pedido
        pedido1.exibirPedido();
        
        // NÃO PODEMOS fazer isso:
        // ItemPedido item = new ItemPedido("Produto", 1, 10.0);  
        // ❌ Construtor é package-private!
        
        // Os itens SÓ existem DENTRO do pedido
        // Se pedido1 for deletado/garbage collected, os itens também são!
    }
}
```

### Saída:

```
Item adicionado: Mouse Gamer
Item adicionado: Teclado Mecânico
Item adicionado: Monitor 24"

========================================
           PEDIDO #101
========================================
Cliente: João Silva
Data: 15/03/2024
----------------------------------------
Itens:
  - Mouse Gamer | 2x | R$ 89,90 | Subtotal: R$ 179,80
  - Teclado Mecânico | 1x | R$ 349,90 | Subtotal: R$ 349,90
  - Monitor 24" | 1x | R$ 899,90 | Subtotal: R$ 899,90
----------------------------------------
TOTAL: R$ 1429,60
========================================
```

**O que isso prova?**
✅ ItemPedido é criado **DENTRO** de Pedido  
✅ ItemPedido **NÃO PODE** existir fora de Pedido  
✅ Se deletar Pedido, ItemPedido é deletado também  
✅ Isso é **COMPOSIÇÃO**

---

## 3.4 Representação em UML

```
┌──────────────────┐
│     Pedido       │
├──────────────────┤
│ - numero         │
│ - cliente        │
│ - data           │
│ - itens[]        │◆────────┐  Losango CHEIO = Composição
├──────────────────┤          │  (vida dependente)
│ + adicionarItem()│          │
│ + calcularTotal()│          │
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
- **1** = um pedido
- **1..*** = um ou mais itens (pedido vazio não faz sentido)

**Leia como:** "Um Pedido é composto de um ou mais ItemPedido, e ItemPedido não existe sem Pedido."

---

## 3.5 Características da Composição

✅ **Partes criadas DENTRO do todo** (no construtor ou métodos)  
✅ **Ciclo de vida dependente** — destruir o todo destrói as partes  
✅ **Relacionamento mais forte** possível  
✅ **Partes não fazem sentido** sozinhas  
✅ **Construtor da parte pode ser package-private** (só o todo cria)  

---

## 3.6 Quando usar Composição?

Use composição quando:

✅ A parte **NÃO FAZ SENTIDO** sem o todo  
✅ Destruir o todo **deve destruir** as partes  
✅ A parte é **criada e gerenciada** pelo todo  
✅ A parte **não pode ser compartilhada** entre múltiplos todos  

**Exemplos clássicos:**
- 🏠 Casa **TEM** Quartos (composição)
- 📄 Pedido **TEM** ItensPedido (composição)
- 🚗 Carro **TEM** Motor (composição)
- 📝 Formulário **TEM** Campos (composição)
- 📖 Livro **TEM** Páginas (composição)

---

## 3.7 Comparação direta: Agregação vs Composição

| Cenário | Tipo | Razão |
|---------|------|-------|
| Time ◇ Jogadores | Agregação | Jogador pode mudar de time |
| Pedido ◆ Itens | Composição | Item não existe fora do pedido |
| Universidade ◇ Alunos | Agregação | Aluno pode transferir |
| Prova ◆ Questões | Composição | Questão não existe fora da prova |
| Playlist ◇ Músicas | Agregação | Música pode estar em várias playlists |
| Casa ◆ Quartos | Composição | Quarto não existe fora da casa |

**Pergunta-chave:** "Se eu deletar o TODO, a PARTE deve ser deletada também?"
- **SIM** → Composição (◆)
- **NÃO** → Agregação (◇)

---

## Exercício Guiado 3 — Sistema Nota Fiscal-Itens (professor + alunos)

Vamos criar um sistema onde **NotaFiscal é composta de ItensNotaFiscal**.

### Passo 1 — Crie `ItemNotaFiscal.java`:

```java
public class ItemNotaFiscal {
    private String descricao;
    private int quantidade;
    private double valorUnitario;
    private double aliquotaImposto;  // em percentual (ex: 18.0 para 18%)
    
    // Construtor package-private (só NotaFiscal cria)
    ItemNotaFiscal(String descricao, int quantidade, double valorUnitario, double aliquotaImposto) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.aliquotaImposto = aliquotaImposto;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public double getValorUnitario() {
        return valorUnitario;
    }
    
    public double getAliquotaImposto() {
        return aliquotaImposto;
    }
    
    public double getValorBruto() {
        return quantidade * valorUnitario;
    }
    
    public double getValorImposto() {
        return getValorBruto() * (aliquotaImposto / 100);
    }
    
    public double getValorLiquido() {
        return getValorBruto() + getValorImposto();
    }
    
    public void exibirDetalhes() {
        System.out.printf("  %-30s | %3dx | R$ %8.2f | Imposto: R$ %7.2f | Total: R$ %9.2f%n",
                         descricao, quantidade, valorUnitario, getValorImposto(), getValorLiquido());
    }
}
```

### Passo 2 — Crie `NotaFiscal.java` com composição:

```java
public class NotaFiscal {
    private String numero;
    private String dataEmissao;
    private String cnpjEmitente;
    private String razaoSocialEmitente;
    private ItemNotaFiscal[] itens;  // ← COMPOSIÇÃO
    private int qtdItens;
    
    public NotaFiscal(String numero, String dataEmissao, String cnpjEmitente, 
                      String razaoSocialEmitente, int capacidade) {
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.cnpjEmitente = cnpjEmitente;
        this.razaoSocialEmitente = razaoSocialEmitente;
        this.itens = new ItemNotaFiscal[capacidade];  // ← Criando array
        this.qtdItens = 0;
    }
    
    // Método que CRIA itens (composição!)
    public void adicionarItem(String descricao, int quantidade, double valorUnitario, double aliquotaImposto) {
        if (qtdItens < itens.length) {
            // Cria ItemNotaFiscal AQUI DENTRO
            itens[qtdItens] = new ItemNotaFiscal(descricao, quantidade, valorUnitario, aliquotaImposto);
            qtdItens++;
            System.out.println("Item adicionado: " + descricao);
        } else {
            System.out.println("Nota fiscal cheia!");
        }
    }
    
    public double calcularTotalBruto() {
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            total += itens[i].getValorBruto();
        }
        return total;
    }
    
    public double calcularTotalImpostos() {
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            total += itens[i].getValorImposto();
        }
        return total;
    }
    
    public double calcularTotalLiquido() {
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            total += itens[i].getValorLiquido();
        }
        return total;
    }
    
    public void emitir() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           NOTA FISCAL ELETRÔNICA                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ NF-e: " + numero);
        System.out.println("║ Data de Emissão: " + dataEmissao);
        System.out.println("║ ───────────────────────────────────────────────────────────────────────── ║");
        System.out.println("║ Emitente: " + razaoSocialEmitente);
        System.out.println("║ CNPJ: " + cnpjEmitente);
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ ITENS:                                                                    ║");
        System.out.println("║ ───────────────────────────────────────────────────────────────────────── ║");
        
        for (int i = 0; i < qtdItens; i++) {
            System.out.print("║ ");
            itens[i].exibirDetalhes();
        }
        
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Bruto:    R$ %56.2f ║%n", calcularTotalBruto());
        System.out.printf("║ Total Impostos: R$ %56.2f ║%n", calcularTotalImpostos());
        System.out.printf("║ TOTAL A PAGAR:  R$ %56.2f ║%n", calcularTotalLiquido());
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝\n");
    }
    
    public String getNumero() { return numero; }
}
```

### Passo 3 — Crie `NotaFiscalMain.java`:

```java
public class NotaFiscalMain {
    public static void main(String[] args) {
        // Criar nota fiscal
        NotaFiscal nf = new NotaFiscal(
            "000.123.456",
            "15/03/2024",
            "12.345.678/0001-90",
            "TechStore Informática LTDA",
            20
        );
        
        // Adicionar itens (COMPOSIÇÃO - criados dentro da NF)
        nf.adicionarItem("Notebook Dell Inspiron 15", 1, 3500.00, 18.0);
        nf.adicionarItem("Mouse Logitech MX Master", 2, 350.00, 18.0);
        nf.adicionarItem("Teclado Mecânico Keychron", 1, 650.00, 18.0);
        nf.adicionarItem("Monitor LG UltraWide 29\"", 1, 1200.00, 18.0);
        nf.adicionarItem("Webcam Logitech C920", 1, 450.00, 18.0);
        
        // Emitir nota
        nf.emitir();
        
        // Os itens SÓ existem dentro da nota fiscal
        // Se a NF for deletada, os itens também são!
    }
}
```

### Observações importantes:

1. ✅ `ItemNotaFiscal` tem construtor **package-private** (só `NotaFiscal` cria)
2. ✅ Itens são criados **dentro do método** `adicionarItem()`
3. ✅ Itens **não existem** fora da nota fiscal
4. ✅ Isso é **COMPOSIÇÃO** (◆)

---

## Exercício Autônomo 3 — Sistema Prova-Questões

**Contexto:** Uma prova de vestibular é composta de questões. Se a prova for anulada, as questões também são.

**Objetivo:** Implementar composição onde Prova é composta de Questões.

### Requisitos:

1. Classe `Questao`:
   - Atributos: `numero`, `enunciado`, `alternativaCorreta` (char: 'A', 'B', 'C', 'D', 'E'), `pontos`
   - Construtor **package-private**
   - Getters
   - Método `void exibir()` — mostra número, enunciado e pontos

2. Classe `Prova`:
   - Atributos: `titulo`, `disciplina`, `data`, `questoes[]`, `qtdQuestoes`
   - Construtor recebe titulo, disciplina, data e capacidade
   - Métodos:
     - `void adicionarQuestao(String enunciado, char alternativaCorreta, int pontos)` — CRIA questão
     - `int calcularPontuacaoTotal()` — soma pontos de todas as questões
     - `void exibirProva()` — mostra cabeçalho e todas as questões
     - `boolean corrigirQuestao(int numeroQuestao, char respostaAluno)` — retorna true se acertou

3. Classe `ProvaMain`:
   - Crie uma prova de Matemática
   - Adicione 5 questões com diferentes pontuações
   - Exiba a prova
   - Mostre a pontuação total
   - Simule correção de 3 questões e mostre quantas acertou

### Exemplo de saída esperada:

```
Questão adicionada: Qual é a derivada de x²?
Questão adicionada: Calcule a integral de 2x dx
...

========================================
PROVA DE Matemática
Data: 15/03/2024
========================================

Questão 1 (10 pontos):
Qual é a derivada de x²?

Questão 2 (15 pontos):
Calcule a integral de 2x dx
...

========================================
Pontuação Total: 65 pontos
========================================

--- Corrigindo questão 1 ---
Resposta: A
Resultado: ✓ Correto!

--- Corrigindo questão 2 ---
Resposta: C
Resultado: ✗ Incorreto!
...
```

### Dicas:

- Numere as questões automaticamente (1, 2, 3...)
- Use switch para comparar alternativas
- Questões SÓ existem dentro da prova (composição!)

---

## Resumo do Bloco 3

Neste bloco você aprendeu:

✅ **Composição** = parte depende totalmente do todo  
✅ Símbolo UML: **◆ (losango CHEIO)**  
✅ Partes criadas **DENTRO** do todo  
✅ Destruir o todo **destrói as partes**  
✅ Relacionamento **mais forte** possível  
✅ Exemplos: NotaFiscal-Itens, Prova-Questões, Casa-Quartos  

**Comparação final:**
- **Agregação (◇):** partes existem sem o todo
- **Composição (◆):** partes morrem com o todo

**Próximo passo:** No **Bloco 4**, você vai **integrar** associação, agregação e composição em um projeto completo!

[➡️ Ir para Bloco 4](../Bloco4/README.md)
