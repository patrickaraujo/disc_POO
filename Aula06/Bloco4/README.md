# Bloco 4 — Múltiplos Relacionamentos e Aplicação Prática

## Objetivos do Bloco

- Integrar associação, agregação e composição em um único sistema
- Modelar sistemas complexos usando múltiplos tipos de relacionamento
- Tomar decisões de design baseadas no ciclo de vida dos objetos
- Aplicar todos os conceitos da aula em um projeto completo

---

## 4.1 Recapitulação: Os Três Tipos de Relacionamento

Antes de integrarmos tudo, vamos revisar:

| Tipo | Símbolo | Significado | Ciclo de Vida | Criação |
|------|---------|-------------|---------------|---------|
| **Associação** | → | "conhece um" | Independentes | Fora |
| **Agregação** | ◇ | "tem um" | Independentes | Fora |
| **Composição** | ◆ | "é composto de" | Dependentes | Dentro |

### Exemplos práticos:

```
Paciente → Médico          (Associação: paciente conhece médico)
Time ◇ Jogadores           (Agregação: time tem jogadores, jogador pode mudar)
Pedido ◆ ItensPedido       (Composição: item não existe sem pedido)
```

**Pergunta-chave para decidir:**
1. "É só uma referência?" → **Associação**
2. "É uma relação todo-parte, mas a parte pode existir sozinha?" → **Agregação**
3. "É uma relação todo-parte, e a parte NÃO pode existir sozinha?" → **Composição**

---

## 4.2 Sistema Integrado: Escola (Todos os Relacionamentos)

Vamos modelar um sistema de escola que usa **os três tipos**:

```
┌─────────────────────────┐
│       Escola            │
├─────────────────────────┤
│ - nome                  │
│ - turmas[]              │◇────────┐  Agregação
└─────────────────────────┘          │
                                     │
                    ┌────────────────▼──────┐
                    │       Turma           │
                    ├───────────────────────┤
                    │ - codigo              │
                    │ - professor           │──────> Professor (Associação)
                    │ - aulas[]             │◆────┐  Composição
                    │ - alunos[]            │◇─┐  │
                    └───────────────────────┘  │  │
                                               │  │
    ┌──────────────────────────────────────────┘  │
    │                                              │
┌───▼─────┐                              ┌────────▼────┐
│  Aluno  │                              │    Aula     │
└─────────┘                              └─────────────┘
```

**Análise:**
- **Escola ◇ Turmas:** Agregação (turma pode existir sem a escola, pode mudar de escola)
- **Turma → Professor:** Associação (turma conhece professor)
- **Turma ◇ Alunos:** Agregação (aluno pode mudar de turma)
- **Turma ◆ Aulas:** Composição (aula não existe sem turma)

---

## 4.3 Implementação do Sistema Completo

Vamos implementar passo a passo.

### Classes Base (Associação e Agregação)

**Professor.java:**
```java
public class Professor {
    private String nome;
    private String cpf;
    private String especialidade;
    
    public Professor(String nome, String cpf, String especialidade) {
        this.nome = nome;
        this.cpf = cpf;
        this.especialidade = especialidade;
    }
    
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEspecialidade() { return especialidade; }
}
```

**Aluno.java:**
```java
public class Aluno {
    private String nome;
    private String matricula;
    private int idade;
    
    public Aluno(String nome, String matricula, int idade) {
        this.nome = nome;
        this.matricula = matricula;
        this.idade = idade;
    }
    
    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }
    public int getIdade() { return idade; }
}
```

**Aula.java (Composição - só Turma cria):**
```java
public class Aula {
    private String data;
    private String assunto;
    private int duracaoMinutos;
    
    // Construtor package-private (só Turma cria)
    Aula(String data, String assunto, int duracaoMinutos) {
        this.data = data;
        this.assunto = assunto;
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public String getData() { return data; }
    public String getAssunto() { return assunto; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    
    public void exibir() {
        System.out.println("  [" + data + "] " + assunto + " (" + duracaoMinutos + " min)");
    }
}
```

**Turma.java (Integra tudo):**
```java
public class Turma {
    private String codigo;
    private String disciplina;
    private Professor professor;      // ← ASSOCIAÇÃO
    private Aluno[] alunos;           // ← AGREGAÇÃO
    private int qtdAlunos;
    private Aula[] aulas;             // ← COMPOSIÇÃO
    private int qtdAulas;
    
    public Turma(String codigo, String disciplina, Professor professor, int capacidadeAlunos, int capacidadeAulas) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.alunos = new Aluno[capacidadeAlunos];
        this.qtdAlunos = 0;
        this.aulas = new Aula[capacidadeAulas];
        this.qtdAulas = 0;
    }
    
    // Agregação: adiciona aluno existente
    public void matricularAluno(Aluno aluno) {
        if (qtdAlunos < alunos.length) {
            alunos[qtdAlunos] = aluno;
            qtdAlunos++;
            System.out.println(aluno.getNome() + " matriculado em " + codigo);
        } else {
            System.out.println("Turma cheia!");
        }
    }
    
    // Composição: CRIA aula
    public void agendarAula(String data, String assunto, int duracaoMinutos) {
        if (qtdAulas < aulas.length) {
            aulas[qtdAulas] = new Aula(data, assunto, duracaoMinutos);  // ← CRIA AQUI
            qtdAulas++;
            System.out.println("Aula agendada: " + assunto);
        } else {
            System.out.println("Limite de aulas atingido!");
        }
    }
    
    public void exibirInfo() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TURMA: " + codigo);
        System.out.println("║  Disciplina: " + disciplina);
        System.out.println("║  Professor: " + professor.getNome() + " (" + professor.getEspecialidade() + ")");
        System.out.println("╠════════════════════════════════════════╣");
        
        System.out.println("║  ALUNOS (" + qtdAlunos + "):");
        for (int i = 0; i < qtdAlunos; i++) {
            System.out.println("║  " + (i+1) + ". " + alunos[i].getNome() + " - " + alunos[i].getMatricula());
        }
        
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  CRONOGRAMA DE AULAS (" + qtdAulas + "):");
        for (int i = 0; i < qtdAulas; i++) {
            System.out.print("║  ");
            aulas[i].exibir();
        }
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    public String getCodigo() { return codigo; }
    public String getDisciplina() { return disciplina; }
}
```

**Escola.java:**
```java
public class Escola {
    private String nome;
    private String endereco;
    private Turma[] turmas;  // ← AGREGAÇÃO
    private int qtdTurmas;
    
    public Escola(String nome, String endereco, int capacidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.turmas = new Turma[capacidade];
        this.qtdTurmas = 0;
    }
    
    public void adicionarTurma(Turma turma) {
        if (qtdTurmas < turmas.length) {
            turmas[qtdTurmas] = turma;
            qtdTurmas++;
            System.out.println("Turma " + turma.getCodigo() + " adicionada à escola " + nome);
        }
    }
    
    public void exibirResumo() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  ESCOLA: " + nome);
        System.out.println("║  Endereço: " + endereco);
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  Total de turmas: " + qtdTurmas);
        for (int i = 0; i < qtdTurmas; i++) {
            System.out.println("║  - " + turmas[i].getCodigo() + " (" + turmas[i].getDisciplina() + ")");
        }
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }
    
    public String getNome() { return nome; }
}
```

---

## Exercício Guiado 4 — Montando o Sistema Completo (professor + alunos)

### SistemaEscola.java:

```java
public class SistemaEscola {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GERENCIAMENTO ESCOLAR ===\n");
        
        // 1. Criar escola
        Escola escola = new Escola("Colégio Excelência", "Rua das Flores, 123", 10);
        
        // 2. Criar professores (existem independentemente)
        Professor prof1 = new Professor("Dr. Carlos Silva", "111.222.333-44", "Matemática");
        Professor prof2 = new Professor("Dra. Ana Costa", "555.666.777-88", "Programação");
        
        // 3. Criar alunos (existem independentemente)
        Aluno aluno1 = new Aluno("João Pedro", "2024001", 16);
        Aluno aluno2 = new Aluno("Maria Clara", "2024002", 15);
        Aluno aluno3 = new Aluno("Lucas Mendes", "2024003", 16);
        Aluno aluno4 = new Aluno("Beatriz Lima", "2024004", 15);
        
        // 4. Criar turmas (ASSOCIAÇÃO com professor)
        Turma turmaMatematica = new Turma("MAT-101", "Matemática Avançada", prof1, 30, 50);
        Turma turmaPOO = new Turma("INFO-201", "Programação Orientada a Objetos", prof2, 25, 40);
        
        // 5. Matricular alunos (AGREGAÇÃO)
        System.out.println("\n--- Matrículas ---");
        turmaMatematica.matricularAluno(aluno1);
        turmaMatematica.matricularAluno(aluno2);
        turmaMatematica.matricularAluno(aluno3);
        
        turmaPOO.matricularAluno(aluno2);  // Maria está nas duas turmas!
        turmaPOO.matricularAluno(aluno3);
        turmaPOO.matricularAluno(aluno4);
        
        // 6. Agendar aulas (COMPOSIÇÃO - criadas dentro da turma)
        System.out.println("\n--- Agendando aulas de Matemática ---");
        turmaMatematica.agendarAula("15/03/2024", "Introdução a Derivadas", 90);
        turmaMatematica.agendarAula("17/03/2024", "Regras de Derivação", 90);
        turmaMatematica.agendarAula("20/03/2024", "Aplicações de Derivadas", 90);
        
        System.out.println("\n--- Agendando aulas de POO ---");
        turmaPOO.agendarAula("16/03/2024", "Classes e Objetos", 120);
        turmaPOO.agendarAula("18/03/2024", "Encapsulamento", 120);
        turmaPOO.agendarAula("21/03/2024", "Relacionamentos entre Classes", 120);
        
        // 7. Adicionar turmas à escola (AGREGAÇÃO)
        System.out.println("\n--- Adicionando turmas à escola ---");
        escola.adicionarTurma(turmaMatematica);
        escola.adicionarTurma(turmaPOO);
        
        // 8. Exibir informações
        escola.exibirResumo();
        turmaMatematica.exibirInfo();
        turmaPOO.exibirInfo();
        
        // 9. Demonstrar independência de ciclos de vida
        System.out.println("\n\n=== DEMONSTRANDO CICLOS DE VIDA ===");
        System.out.println("\n1. Alunos e Professores existem antes das turmas ✓");
        System.out.println("2. Maria está em duas turmas diferentes ✓");
        System.out.println("3. Se deletar turmaMatematica:");
        System.out.println("   - Alunos continuam existindo (Agregação) ✓");
        System.out.println("   - Professor continua existindo (Associação) ✓");
        System.out.println("   - Aulas são deletadas (Composição) ✓");
    }
}
```

### O que observar na execução:

1. ✅ **Associação:** Turma **conhece** Professor
2. ✅ **Agregação:** Escola **tem** Turmas, Turma **tem** Alunos
3. ✅ **Composição:** Turma **cria** Aulas
4. ✅ Maria está em **duas turmas** (agregação permite)
5. ✅ Aulas são criadas **dentro** da turma (composição)

---

## Exercício Autônomo 4 — Sistema Completo de E-commerce

**Contexto:** Criar um sistema de e-commerce integrando os três tipos de relacionamento.

### Requisitos:

**Classes a criar:**

1. **Cliente** (existe independentemente)
   - Atributos: `nome`, `cpf`, `email`
   - Construtor e getters

2. **Produto** (existe independentemente)
   - Atributos: `nome`, `preco`, `estoque`
   - Construtor, getters e métodos para adicionar/remover estoque

3. **ItemCarrinho** (composição - só existe dentro de carrinho)
   - Atributos: `produto` (referência), `quantidade`
   - Construtor **package-private**
   - Método `getSubtotal()`

4. **Carrinho** (composição com ItemCarrinho, agregação com Produto)
   - Atributos: `cliente` (associação), `itens[]` (composição), `qtdItens`
   - Construtor recebe cliente
   - Método `adicionarProduto(Produto produto, int quantidade)` — CRIA ItemCarrinho
   - Método `calcularTotal()`, `exibirCarrinho()`, `finalizarCompra()`

5. **Loja** (agregação com Carrinho)
   - Atributos: `nome`, `carrinhos[]`, `qtdCarrinhos`
   - Método `criarCarrinho(Cliente cliente)` — retorna novo Carrinho
   - Método `listarCarrinhosAtivos()`

### Comportamentos esperados:

- Cliente pode ter múltiplos carrinhos em lojas diferentes (agregação)
- Produto pode estar em múltiplos carrinhos (agregação)
- ItemCarrinho SÓ existe dentro de Carrinho (composição)
- Se deletar Carrinho, ItemCarrinho é deletado, mas Produto continua existindo

### Diagrama de relacionamentos:

```
Cliente ←─────── Carrinho ◆────────→ ItemCarrinho
                    ↓ (referencia)
Loja ◇─────────→ Carrinho              ↓
                                    Produto
```

### Exemplo de uso:

```java
// Criar produtos
Produto p1 = new Produto("Mouse Gamer", 89.90, 50);
Produto p2 = new Produto("Teclado Mecânico", 349.90, 30);

// Criar cliente
Cliente joao = new Cliente("João Silva", "111.222.333-44", "joao@email.com");

// Criar loja
Loja techStore = new Loja("TechStore", 100);

// Criar carrinho para João
Carrinho carrinho = techStore.criarCarrinho(joao);

// Adicionar produtos (cria ItemCarrinho internamente)
carrinho.adicionarProduto(p1, 2);
carrinho.adicionarProduto(p2, 1);

// Exibir carrinho
carrinho.exibirCarrinho();

// Finalizar compra
carrinho.finalizarCompra();
```

### Dicas:

- Use `ArrayList` ou arrays conforme preferir
- Implemente validação de estoque antes de adicionar ao carrinho
- Finalize a compra removendo a quantidade do estoque
- Mostre como ItemCarrinho referencia Produto mas é criado dentro de Carrinho

---

## 4.4 Guia de Decisão de Design

Ao modelar um sistema, use este fluxograma mental:

```
┌─────────────────────────────────┐
│ Um objeto precisa conhecer      │
│ ou conter outro?                │
└──────────┬──────────────────────┘
           │
           ▼
    ┌──────────────┐
    │ É relação     │
    │ TODO-PARTE?   │
    └──┬───────┬───┘
       │ NÃO   │ SIM
       ▼       ▼
  ASSOCIAÇÃO   ┌──────────────────┐
     (→)       │ A parte pode      │
              │ existir sem todo? │
              └──┬───────┬────────┘
                 │ SIM   │ NÃO
                 ▼       ▼
            AGREGAÇÃO  COMPOSIÇÃO
               (◇)        (◆)
```

### Perguntas para cada situação:

**1. É só uma referência?**
- Exemplo: Paciente → Médico
- Resposta: **Associação**

**2. É todo-parte, mas a parte pode existir sozinha?**
- Exemplo: Time ◇ Jogador
- Resposta: **Agregação**

**3. É todo-parte, e a parte NÃO pode existir sozinha?**
- Exemplo: Pedido ◆ ItemPedido
- Resposta: **Composição**

---

## 4.5 Erros Comuns e Como Evitar

### ❌ Erro 1: Confundir agregação com composição

```java
// ❌ ERRADO - Parece agregação mas é composição
public class Pedido {
    private ItemPedido[] itens;
    
    public void adicionarItem(ItemPedido item) {  // ← Recebe pronto
        // Item criado fora? Então é agregação!
    }
}

// ✅ CORRETO - Composição
public class Pedido {
    private ItemPedido[] itens;
    
    public void adicionarItem(String produto, int qtd, double preco) {
        itens[qtdItens] = new ItemPedido(produto, qtd, preco);  // ← Cria aqui
    }
}
```

### ❌ Erro 2: Não validar null em associações

```java
// ❌ ERRADO
public void mostrarMedico() {
    System.out.println(medicoResponsavel.getNome());  // ← NullPointerException!
}

// ✅ CORRETO
public void mostrarMedico() {
    if (medicoResponsavel != null) {
        System.out.println(medicoResponsavel.getNome());
    } else {
        System.out.println("Sem médico cadastrado");
    }
}
```

### ❌ Erro 3: Expor partes de composição

```java
// ❌ ERRADO - Expõe itens
public ItemPedido[] getItens() {
    return itens;  // Código externo pode modificar!
}

// ✅ CORRETO - Retorna cópia ou apenas info necessária
public int getQuantidadeItens() {
    return qtdItens;
}

public double getTotal() {
    // Calcula internamente
}
```

---

## Resumo do Bloco 4

Neste bloco você aprendeu:

✅ Integrar **associação**, **agregação** e **composição** em um único sistema  
✅ Tomar **decisões de design** baseadas no ciclo de vida  
✅ Modelar sistemas **complexos** do mundo real  
✅ Implementar **múltiplos relacionamentos** de forma coerente  
✅ Evitar **erros comuns** de design  

---

## 🎓 Conclusão da Aula 06

Parabéns! Você completou a Aula 06 e agora domina:

✅ **Associação** (→) — "conhece um"  
✅ **Agregação** (◇) — "tem um" (vida independente)  
✅ **Composição** (◆) — "é composto de" (vida dependente)  
✅ Como **decidir** qual relacionamento usar  
✅ Como **implementar** cada tipo em Java  
✅ Como **integrar** múltiplos relacionamentos  

### Resposta à problematização inicial:

> **"Como representar que um Empréstimo precisa de um Livro e um Usuário?"**

**Resposta completa:**
- `Emprestimo` tem **associação** com `Livro` e `Usuario` (conhece ambos)
- Ou melhor: `Emprestimo` tem **composição** com referências (o empréstimo depende delas)
- `Biblioteca` tem **agregação** com `Livro` (livros podem existir sem biblioteca)
- `Biblioteca` tem **agregação** com `Usuario` (usuários podem existir sem biblioteca)

```java
public class Emprestimo {
    private Livro livro;        // Associação/Composição
    private Usuario usuario;    // Associação/Composição
    private String dataEmprestimo;
    private String dataDevolucao;
    
    public Emprestimo(Livro livro, Usuario usuario, String data) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = data;
    }
}
```

**Você está preparado para a Aula 07 — Herança!** 🚀
