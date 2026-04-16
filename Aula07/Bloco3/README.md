# Bloco 3 — Exercício Autônomo: Sistema de Produtos de E-commerce

## Objetivos do Bloco

- Aplicar herança de forma **independente** em um novo domínio
- Tomar decisões de design sem orientação direta
- Implementar hierarquia de classes completa
- Validar compreensão dos conceitos da aula
- Apresentar e discutir diferentes soluções

---

## 🎯 Problema: Loja Virtual com Múltiplos Tipos de Produtos

Você foi contratado para desenvolver um sistema de gerenciamento de produtos para um e-commerce. A loja vende três categorias principais de produtos:

### Tipos de Produtos

1. **Livros**
   - Autor
   - Número de páginas
   - Editora
   - ISBN

2. **Eletrônicos**
   - Marca
   - Garantia (em meses)
   - Voltagem (110V ou 220V)

3. **Alimentos**
   - Data de validade
   - Peso (em kg)
   - É orgânico? (sim/não)

### Características Comuns

Todos os produtos têm:
- Código único
- Nome
- Preço base
- Quantidade em estoque
- Categoria (Livro, Eletrônico, Alimento)

### Regras de Negócio

1. **Cálculo de preço final:**
   - **Livros:** preço base - 10% (desconto literário)
   - **Eletrônicos:** preço base + R$ 50 (taxa de garantia)
   - **Alimentos orgânicos:** preço base + 20%
   - **Alimentos não orgânicos:** preço base sem alteração

2. **Método de exibição:**
   - Todos devem exibir informações básicas
   - Cada tipo adiciona informações específicas
   - Livros devem formatar ISBN: XXX-X-XXXX-XXXX-X

3. **Controle de estoque:**
   - Método para adicionar estoque
   - Método para vender (diminui estoque)
   - Não pode vender se estoque zero

---

## Planejamento Necessário (FAÇA ANTES DE CODIFICAR!)

### Passo 1: Desenhe a hierarquia no papel

```
┌────────────────────────────────┐
│         Produto                │  ← Que atributos vão aqui?
│  (SUPERCLASSE)                 │  ← Que métodos vão aqui?
└────────────────┬───────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼─────┐  ┌───▼──────┐  ┌─▼──────────┐
│ Livro   │  │Eletronico│  │ Alimento   │
│         │  │          │  │            │
└─────────┘  └──────────┘  └────────────┘
```

**Perguntas para reflexão:**
- O que todos os produtos têm em comum?
- O que é específico de cada tipo?
- Quais métodos devem ser sobrescritos?
- Quais podem ser herdados sem alteração?

### Passo 2: Decida os modificadores de acesso

- Atributos comuns: `protected` ou `private` + getters?
- Atributos específicos: `private`
- Métodos: `public`

### Passo 3: Planeje os construtores

- `Produto`: quais parâmetros?
- `Livro`: parâmetros comuns + específicos
- Como usar `super()`?

---

## Requisitos Detalhados

### Classe `Produto` (Superclasse)

**Atributos:**
```java
- String codigo
- String nome
- double precoBase
- int quantidadeEstoque
```

**Construtor:**
```java
public Produto(String codigo, String nome, double precoBase, int quantidadeEstoque)
```

**Métodos:**

1. `public double calcularPrecoFinal()`
   - Retorna o preço (pode ser sobrescrito)

2. `public void exibirInfo()`
   - Mostra: código, nome, preço base, estoque
   - Pode ser sobrescrito para adicionar mais info

3. `public void adicionarEstoque(int quantidade)`
   - Aumenta o estoque
   - Imprime mensagem de confirmação

4. `public boolean vender(int quantidade)`
   - Diminui estoque se houver disponível
   - Retorna `true` se vendeu, `false` se não tinha estoque
   - Imprime mensagem adequada

5. Getters para todos os atributos

---

### Classe `Livro extends Produto`

**Atributos específicos:**
```java
- String autor
- int numeroPaginas
- String editora
- String isbn
```

**Construtor:**
```java
public Livro(String codigo, String nome, double precoBase, int quantidadeEstoque,
             String autor, int numeroPaginas, String editora, String isbn)
```

**Métodos a sobrescrever:**

1. `@Override public double calcularPrecoFinal()`
   - Retorna preço base com 10% de desconto

2. `@Override public void exibirInfo()`
   - Chama `super.exibirInfo()`
   - Adiciona: autor, páginas, editora, ISBN formatado

**Método auxiliar (opcional):**
```java
private String formatarISBN()
   - Recebe: "9780132350884"
   - Retorna: "978-0-1323-5088-4"
```

---

### Classe `Eletronico extends Produto`

**Atributos específicos:**
```java
- String marca
- int garantiaMeses
- String voltagem  // "110V" ou "220V"
```

**Construtor:**
```java
public Eletronico(String codigo, String nome, double precoBase, int quantidadeEstoque,
                  String marca, int garantiaMeses, String voltagem)
```

**Métodos a sobrescrever:**

1. `@Override public double calcularPrecoFinal()`
   - Retorna preço base + R$ 50

2. `@Override public void exibirInfo()`
   - Chama `super.exibirInfo()`
   - Adiciona: marca, garantia, voltagem

---

### Classe `Alimento extends Produto`

**Atributos específicos:**
```java
- String dataValidade  // "DD/MM/AAAA"
- double pesoKg
- boolean organico
```

**Construtor:**
```java
public Alimento(String codigo, String nome, double precoBase, int quantidadeEstoque,
                String dataValidade, double pesoKg, boolean organico)
```

**Métodos a sobrescrever:**

1. `@Override public double calcularPrecoFinal()`
   - Se orgânico: preço base + 20%
   - Se não orgânico: preço base sem alteração

2. `@Override public void exibirInfo()`
   - Chama `super.exibirInfo()`
   - Adiciona: validade, peso, tipo (orgânico/convencional)

---

### Classe `Loja` (Gerenciamento)

**Atributos:**
```java
- String nome
- Produto[] catalogo
- int quantidadeProdutos
```

**Construtor:**
```java
public Loja(String nome, int capacidade)
```

**Métodos:**

1. `public void adicionarProduto(Produto produto)`
   - Adiciona ao catálogo

2. `public Produto buscarPorCodigo(String codigo)`
   - Retorna produto ou null

3. `public void listarProdutos()`
   - Lista todos os produtos (código e nome)

4. `public void listarPorTipo(String tipo)`
   - Lista apenas produtos do tipo especificado
   - Tipos: "Livro", "Eletronico", "Alimento"
   - Dica: use `instanceof`

5. `public void realizarVenda(String codigo, int quantidade)`
   - Busca produto
   - Tenta vender
   - Mostra valor total da venda

6. `public double calcularValorTotalEstoque()`
   - Soma: precoFinal × quantidadeEstoque de todos os produtos

---

### Classe `SistemaLoja` (Main)

**Requisitos do teste:**

1. Criar uma loja
2. Cadastrar pelo menos:
   - 3 livros
   - 3 eletrônicos
   - 3 alimentos (pelo menos 1 orgânico)

3. Listar todos os produtos
4. Exibir detalhes completos de 1 produto de cada tipo
5. Realizar 3 vendas diferentes
6. Calcular valor total do estoque
7. Listar apenas produtos de um tipo específico

---

## Exemplo de Saída Esperada

```
╔═══════════════════════════════════════════════════╗
║      SISTEMA DE GERENCIAMENTO DE PRODUTOS         ║
╚═══════════════════════════════════════════════════╝

=== CADASTRANDO PRODUTOS ===

✓ Produto adicionado: Clean Code
✓ Produto adicionado: Notebook Dell
✓ Produto adicionado: Arroz Integral Orgânico
...

╔═══════════════════════════════════════════════════╗
║  CATÁLOGO DA LOJA TECH BOOKS FOOD                 ║
╠═══════════════════════════════════════════════════╣
║  [1] L001 - Clean Code
║  [2] L002 - Design Patterns
║  [3] E001 - Notebook Dell
║  [4] E002 - Mouse Logitech
║  [5] A001 - Arroz Integral Orgânico
...
╚═══════════════════════════════════════════════════╝

=== DETALHES DOS PRODUTOS ===

╔════════════════════════════════════════╗
║  INFORMAÇÕES DO PRODUTO                ║
╠════════════════════════════════════════╣
║  Código: L001
║  Nome: Clean Code
║  Preço base: R$ 100,00
║  Estoque: 50 unidades
║  Preço final: R$ 90,00
║  ──────────────────────────────────────
║  Autor: Robert Martin
║  Páginas: 464
║  Editora: Prentice Hall
║  ISBN: 978-0-1323-5088-4
╚════════════════════════════════════════╝

╔════════════════════════════════════════╗
║  INFORMAÇÕES DO PRODUTO                ║
╠════════════════════════════════════════╣
║  Código: E001
║  Nome: Notebook Dell
║  Preço base: R$ 3500,00
║  Estoque: 10 unidades
║  Preço final: R$ 3550,00
║  ──────────────────────────────────────
║  Marca: Dell
║  Garantia: 24 meses
║  Voltagem: 110V
╚════════════════════════════════════════╝

╔════════════════════════════════════════╗
║  INFORMAÇÕES DO PRODUTO                ║
╠════════════════════════════════════════╣
║  Código: A001
║  Nome: Arroz Integral Orgânico
║  Preço base: R$ 25,00
║  Estoque: 100 unidades
║  Preço final: R$ 30,00
║  ──────────────────────────────────────
║  Validade: 31/12/2024
║  Peso: 1,00 kg
║  Tipo: Orgânico
╚════════════════════════════════════════╝

=== REALIZANDO VENDAS ===

✓ Venda realizada: 5 unidades de Clean Code
  Valor total: R$ 450,00

✓ Venda realizada: 2 unidades de Notebook Dell
  Valor total: R$ 7100,00

✓ Venda realizada: 10 unidades de Arroz Integral Orgânico
  Valor total: R$ 300,00

=== VALOR TOTAL DO ESTOQUE ===

Valor total em estoque: R$ 125.450,00

=== PRODUTOS DA CATEGORIA: Livro ===

- L001: Clean Code (45 em estoque)
- L002: Design Patterns (30 em estoque)
- L003: Refactoring (25 em estoque)
```

---

## Critérios de Avaliação

Seu código será avaliado por:

### Funcionalidade (40%)
- ✅ Todas as classes criadas
- ✅ Herança implementada corretamente
- ✅ Métodos funcionam como especificado
- ✅ Sistema completo executa sem erros

### Uso de Herança (30%)
- ✅ Superclasse contém apenas o que é comum
- ✅ Subclasses contêm apenas o específico
- ✅ `extends` usado corretamente
- ✅ `super()` chamado nos construtores
- ✅ `@Override` usado nas sobrescritas

### Qualidade do Código (20%)
- ✅ Nomes descritivos de variáveis e métodos
- ✅ Encapsulamento adequado (private/protected/public)
- ✅ Código organizado e legível
- ✅ Comentários onde necessário

### Apresentação (10%)
- ✅ Saída formatada e profissional
- ✅ Mensagens claras para o usuário
- ✅ Trata casos de erro (ex: venda sem estoque)

---

## Desafios Opcionais (Bônus)

Se terminar antes do tempo, implemente:

### 1. Sistema de Categorias
- Enum `Categoria` { LIVRO, ELETRONICO, ALIMENTO }
- Adicionar em `Produto`
- Substituir `instanceof` por comparação de enum

### 2. Relatório de Vendas
- Classe `Venda` que registra: produto, quantidade, data, valor total
- Array de vendas em `Loja`
- Método `gerarRelatorioVendas()`

### 3. Promoções
- Método `aplicarPromocao(double percentualDesconto)` em `Produto`
- Desconto adicional sobre preço final
- Exibir preço com e sem promoção

### 4. Busca Avançada
- `buscarPorNome(String nome)` — busca parcial
- `buscarPorFaixaPreco(double min, double max)`
- `buscarComEstoqueBaixo(int limite)` — produtos com estoque < limite

---

## Dicas de Implementação

### Formatação de ISBN
```java
private String formatarISBN() {
    // De: "9780132350884"
    // Para: "978-0-1323-5088-4"
    
    if (isbn.length() != 13) return isbn;
    
    return isbn.substring(0, 3) + "-" +
           isbn.substring(3, 4) + "-" +
           isbn.substring(4, 8) + "-" +
           isbn.substring(8, 12) + "-" +
           isbn.substring(12, 13);
}
```

### Uso de `instanceof` para filtrar tipos
```java
public void listarPorTipo(String tipo) {
    for (int i = 0; i < quantidadeProdutos; i++) {
        Produto p = catalogo[i];
        
        if (tipo.equals("Livro") && p instanceof Livro) {
            System.out.println("- " + p.getCodigo() + ": " + p.getNome());
        } else if (tipo.equals("Eletronico") && p instanceof Eletronico) {
            System.out.println("- " + p.getCodigo() + ": " + p.getNome());
        } else if (tipo.equals("Alimento") && p instanceof Alimento) {
            System.out.println("- " + p.getCodigo() + ": " + p.getNome());
        }
    }
}
```

### Cálculo de estoque total
```java
public double calcularValorTotalEstoque() {
    double total = 0.0;
    
    for (int i = 0; i < quantidadeProdutos; i++) {
        Produto p = catalogo[i];
        total += p.calcularPrecoFinal() * p.getQuantidadeEstoque();
    }
    
    return total;
}
```

---

## Checklist de Desenvolvimento

Use este checklist para organizar seu trabalho:

**Fase 1: Planejamento (15 min)**
- [ ] Desenhar hierarquia no papel
- [ ] Listar atributos de cada classe
- [ ] Decidir quais métodos sobrescrever
- [ ] Planejar assinatura dos construtores

**Fase 2: Superclasse (20 min)**
- [ ] Criar classe `Produto`
- [ ] Implementar todos os métodos
- [ ] Testar isoladamente

**Fase 3: Subclasses (30 min)**
- [ ] Criar `Livro` com herança
- [ ] Criar `Eletronico` com herança
- [ ] Criar `Alimento` com herança
- [ ] Testar cada uma isoladamente

**Fase 4: Gerenciamento (25 min)**
- [ ] Criar classe `Loja`
- [ ] Implementar métodos de cadastro
- [ ] Implementar métodos de busca
- [ ] Implementar sistema de vendas

**Fase 5: Teste e Apresentação (30 min)**
- [ ] Criar `SistemaLoja` com main
- [ ] Cadastrar produtos de teste
- [ ] Executar todas as operações
- [ ] Ajustar formatação de saída
- [ ] Preparar apresentação

---

## Apresentação das Soluções

Ao final do bloco, cada aluno (ou dupla) deve:

1. **Demonstrar o sistema funcionando** (5 min)
   - Executar e mostrar saída
   - Explicar decisões de design

2. **Responder perguntas** (3 min)
   - Por que escolheu `protected` ou `private`?
   - Como decidiu o que vai em cada classe?
   - Quais foram as dificuldades?

3. **Discussão coletiva** (10 min no final)
   - Comparar diferentes abordagens
   - Identificar melhores práticas
   - Discutir melhorias possíveis

---

## Resumo do Bloco 3

Neste bloco você:

✅ Aplicou herança de forma **independente**  
✅ Tomou **decisões de design** sem orientação  
✅ Implementou sistema **completo** do zero  
✅ Praticou **análise** e **planejamento** antes de codificar  
✅ Apresentou e **defendeu** suas escolhas  

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## 🎓 Conclusão da Aula 07

Parabéns! Você completou a Aula 07 e agora domina:

✅ Conceito e sintaxe de **Herança**  
✅ Uso de `extends`, `super`, `@Override`, `protected`  
✅ Criação de **hierarquias de classes**  
✅ **Reuso de código** através de herança  
✅ **Especialização** de comportamentos  
✅ Quando usar (e quando **NÃO** usar) herança  

### Resposta à problematização inicial

> **"Como evitar código duplicado em diferentes tipos de Funcionário?"**

**Resposta:** Criar uma superclasse `Funcionario` com atributos e métodos comuns (`nome`, `cpf`, `exibirDados()`), e usar `extends` para criar especializações (`FuncionarioCLT`, `FuncionarioHorista`, `FuncionarioComissionado`) que herdam o comum e adicionam apenas o específico!

**Na próxima aula (Aula 08):** Você aprenderá **Polimorfismo** — como tratar objetos de diferentes tipos de forma genérica! 🚀
