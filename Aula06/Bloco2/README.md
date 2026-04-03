# Bloco 2 — Agregação: "Tem Um" (Vida Independente)

## Objetivos do Bloco

- Entender o conceito de agregação como relacionamento todo-parte
- Diferenciar agregação de associação simples
- Implementar agregação em Java usando arrays ou coleções
- Reconhecer quando usar agregação baseado no ciclo de vida dos objetos

---

## 2.1 O que é Agregação?

**Agregação** é um tipo especial de associação que representa uma relação **"tem um"** (has-a), onde:

> "Um objeto **contém** ou **é composto de** outros objetos, mas esses objetos podem existir independentemente."

### Diferença entre Associação e Agregação

| Aspecto | Associação | Agregação |
|---------|------------|-----------|
| **Relação** | "conhece um" | "tem um" |
| **Exemplo** | Paciente conhece Médico | Time tem Jogadores |
| **Força** | Conexão fraca | Relação todo-parte |
| **Ciclo de vida** | Independentes | Independentes |

**A chave:** Na agregação, existe uma noção de **TODO** e **PARTE**, mas a parte pode existir sem o todo.

---

## 2.2 Analogia do mundo real

### Universidade e Alunos (Agregação)

```
┌──────────────────┐
│  Universidade    │
│                  │
│  TEM             │──┐
│  - alunos[]      │  │  Alunos podem existir
│                  │  │  sem a universidade
└──────────────────┘  │  (transferir, formar)
                      │
        ┌─────────────┼─────────────┐
        │             │             │
    ┌───▼───┐     ┌───▼───┐     ┌───▼───┐
    │ Aluno │     │ Aluno │     │ Aluno │
    │ João  │     │ Maria │     │ Pedro │
    └───────┘     └───────┘     └───────┘
```

**Características:**
- A universidade **TEM** alunos (todo-parte)
- Se a universidade fechar, os alunos **continuam existindo**
- Alunos podem **mudar de universidade**
- Isso é **AGREGAÇÃO**

---

## 2.3 Implementando Agregação em Java

### Exemplo: Time de Futebol

```java
public class Jogador {
    private String nome;
    private int numero;
    private String posicao;
    
    public Jogador(String nome, int numero, String posicao) {
        this.nome = nome;
        this.numero = numero;
        this.posicao = posicao;
    }
    
    public String getNome() { return nome; }
    public int getNumero() { return numero; }
    public String getPosicao() { return posicao; }
    
    public void apresentar() {
        System.out.println("#" + numero + " - " + nome + " (" + posicao + ")");
    }
}
```

```java
public class Time {
    private String nome;
    private Jogador[] jogadores;  // ← AGREGAÇÃO (array de jogadores)
    private int qtdJogadores;
    
    public Time(String nome, int capacidade) {
        this.nome = nome;
        this.jogadores = new Jogador[capacidade];
        this.qtdJogadores = 0;
    }
    
    // Adicionar jogador ao time
    public void adicionarJogador(Jogador jogador) {
        if (qtdJogadores < jogadores.length) {
            jogadores[qtdJogadores] = jogador;
            qtdJogadores++;
            System.out.println(jogador.getNome() + " foi adicionado ao time " + nome);
        } else {
            System.out.println("Time completo!");
        }
    }
    
    // Remover jogador (o jogador continua existindo!)
    public void removerJogador(String nomeJogador) {
        for (int i = 0; i < qtdJogadores; i++) {
            if (jogadores[i].getNome().equals(nomeJogador)) {
                System.out.println(nomeJogador + " foi removido do time " + nome);
                
                // Shift array (move todos para a esquerda)
                for (int j = i; j < qtdJogadores - 1; j++) {
                    jogadores[j] = jogadores[j + 1];
                }
                jogadores[qtdJogadores - 1] = null;
                qtdJogadores--;
                return;
            }
        }
        System.out.println(nomeJogador + " não encontrado no time.");
    }
    
    // Listar jogadores
    public void listarJogadores() {
        System.out.println("\n=== Time " + nome + " ===");
        if (qtdJogadores == 0) {
            System.out.println("Nenhum jogador cadastrado.");
        } else {
            for (int i = 0; i < qtdJogadores; i++) {
                jogadores[i].apresentar();
            }
        }
    }
    
    public String getNome() { return nome; }
    public int getQtdJogadores() { return qtdJogadores; }
}
```

### Testando a Agregação:

```java
public class TimeFutebolMain {
    public static void main(String[] args) {
        // Criar jogadores ANTES do time (podem existir independentemente)
        Jogador j1 = new Jogador("Neymar", 10, "Atacante");
        Jogador j2 = new Jogador("Casemiro", 5, "Volante");
        Jogador j3 = new Jogador("Alisson", 1, "Goleiro");
        
        // Criar time
        Time selecao = new Time("Brasil", 23);
        
        // Adicionar jogadores ao time (agregação)
        selecao.adicionarJogador(j1);
        selecao.adicionarJogador(j2);
        selecao.adicionarJogador(j3);
        
        // Listar time
        selecao.listarJogadores();
        
        // Remover jogador (ele continua existindo!)
        System.out.println("\n--- Removendo Casemiro ---");
        selecao.removerJogador("Casemiro");
        
        selecao.listarJogadores();
        
        // Casemiro ainda existe!
        System.out.println("\n--- Casemiro ainda está vivo! ---");
        j2.apresentar();
        
        // Pode ser adicionado a outro time
        Time timeReserva = new Time("Brasil Sub-23", 23);
        timeReserva.adicionarJogador(j2);
    }
}
```

### Saída:

```
Neymar foi adicionado ao time Brasil
Casemiro foi adicionado ao time Brasil
Alisson foi adicionado ao time Brasil

=== Time Brasil ===
#10 - Neymar (Atacante)
#5 - Casemiro (Volante)
#1 - Alisson (Goleiro)

--- Removendo Casemiro ---
Casemiro foi removido do time Brasil

=== Time Brasil ===
#10 - Neymar (Atacante)
#1 - Alisson (Goleiro)

--- Casemiro ainda está vivo! ---
#5 - Casemiro (Volante)
Casemiro foi adicionado ao time Brasil Sub-23
```

**O que isso prova?**
✅ Jogador pode existir **sem** time  
✅ Jogador pode **mudar** de time  
✅ Remover do time **não destroi** o jogador  
✅ Isso é **AGREGAÇÃO**

---

## 2.4 Representação em UML

```
┌──────────────────┐
│      Time        │
├──────────────────┤
│ - nome           │
│ - jogadores[]    │◇────────┐  Losango vazio = Agregação
├──────────────────┤          │  (vida independente)
│ + adicionarJogador() │      │
│ + removerJogador()   │      │
└──────────────────┘          │
       1                      0..*
                              │
                      ┌───────▼────────┐
                      │    Jogador     │
                      ├────────────────┤
                      │ - nome         │
                      │ - numero       │
                      │ - posicao      │
                      └────────────────┘
```

**Símbolos importantes:**
- **◇ (losango vazio)** = agregação
- **1** = um time
- **0..*** = zero ou mais jogadores

**Leia como:** "Um Time tem de zero a muitos Jogadores, e Jogadores podem existir sem Time."

---

## 2.5 Quando usar Agregação?

Use agregação quando:

✅ Existe relação **TODO-PARTE**  
✅ A **parte pode existir sem o todo**  
✅ A parte pode **pertencer a múltiplos todos** (ou mudar de todo)  
✅ Deletar o todo **não destroi** as partes  

**Exemplos clássicos:**
- 🏫 Universidade **TEM** Alunos
- ⚽ Time **TEM** Jogadores
- 🏢 Empresa **TEM** Funcionários
- 📚 Biblioteca **TEM** Livros
- 🎵 Playlist **TEM** Músicas

---

## Exercício Guiado 2 — Sistema Empresa-Funcionário (professor + alunos)

Vamos criar um sistema onde **Empresa TEM Funcionários**, mas funcionários podem mudar de empresa.

### Passo 1 — Crie `Funcionario.java`:

```java
public class Funcionario {
    private String nome;
    private String cpf;
    private double salario;
    private String cargo;
    
    public Funcionario(String nome, String cpf, double salario, String cargo) {
        this.nome = nome;
        this.cpf = cpf;
        this.salario = salario;
        this.cargo = cargo;
    }
    
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public double getSalario() { return salario; }
    public String getCargo() { return cargo; }
    
    public void exibirInfo() {
        System.out.println("Funcionário: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Cargo: " + cargo);
        System.out.println("Salário: R$ " + String.format("%.2f", salario));
    }
}
```

### Passo 2 — Crie `Empresa.java` com agregação:

```java
public class Empresa {
    private String razaoSocial;
    private String cnpj;
    private Funcionario[] funcionarios;  // ← AGREGAÇÃO
    private int qtdFuncionarios;
    
    public Empresa(String razaoSocial, String cnpj, int capacidade) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.funcionarios = new Funcionario[capacidade];
        this.qtdFuncionarios = 0;
    }
    
    public void contratar(Funcionario funcionario) {
        if (qtdFuncionarios < funcionarios.length) {
            funcionarios[qtdFuncionarios] = funcionario;
            qtdFuncionarios++;
            System.out.println(funcionario.getNome() + " foi contratado(a) por " + razaoSocial);
        } else {
            System.out.println("Empresa não tem vagas disponíveis.");
        }
    }
    
    public void demitir(String cpf) {
        for (int i = 0; i < qtdFuncionarios; i++) {
            if (funcionarios[i].getCpf().equals(cpf)) {
                System.out.println(funcionarios[i].getNome() + " foi demitido(a) de " + razaoSocial);
                
                // Remove do array
                for (int j = i; j < qtdFuncionarios - 1; j++) {
                    funcionarios[j] = funcionarios[j + 1];
                }
                funcionarios[qtdFuncionarios - 1] = null;
                qtdFuncionarios--;
                return;
            }
        }
        System.out.println("Funcionário com CPF " + cpf + " não encontrado.");
    }
    
    public void listarFuncionarios() {
        System.out.println("\n=== Funcionários de " + razaoSocial + " ===");
        if (qtdFuncionarios == 0) {
            System.out.println("Nenhum funcionário cadastrado.");
        } else {
            for (int i = 0; i < qtdFuncionarios; i++) {
                System.out.println((i + 1) + ". " + funcionarios[i].getNome() + 
                                   " - " + funcionarios[i].getCargo());
            }
        }
    }
    
    public double calcularFolhaPagamento() {
        double total = 0;
        for (int i = 0; i < qtdFuncionarios; i++) {
            total += funcionarios[i].getSalario();
        }
        return total;
    }
    
    public String getRazaoSocial() { return razaoSocial; }
    public String getCnpj() { return cnpj; }
    public int getQtdFuncionarios() { return qtdFuncionarios; }
}
```

### Passo 3 — Crie `EmpresaMain.java`:

```java
public class EmpresaMain {
    public static void main(String[] args) {
        // Criar funcionários (existem independentemente)
        Funcionario f1 = new Funcionario("Ana Silva", "111.222.333-44", 5000.0, "Desenvolvedora");
        Funcionario f2 = new Funcionario("Carlos Souza", "555.666.777-88", 4500.0, "Analista");
        Funcionario f3 = new Funcionario("Beatriz Lima", "999.888.777-66", 6000.0, "Gerente");
        
        // Criar empresa
        Empresa techCorp = new Empresa("TechCorp Solutions LTDA", "12.345.678/0001-90", 10);
        
        // Contratar funcionários (agregação)
        techCorp.contratar(f1);
        techCorp.contratar(f2);
        techCorp.contratar(f3);
        
        // Listar funcionários
        techCorp.listarFuncionarios();
        
        // Mostrar folha de pagamento
        System.out.println("\nFolha de pagamento total: R$ " + 
                          String.format("%.2f", techCorp.calcularFolhaPagamento()));
        
        // Demitir funcionário
        System.out.println("\n--- Demissão ---");
        techCorp.demitir("555.666.777-88");
        
        techCorp.listarFuncionarios();
        System.out.println("\nNova folha de pagamento: R$ " + 
                          String.format("%.2f", techCorp.calcularFolhaPagamento()));
        
        // Carlos ainda existe!
        System.out.println("\n--- Carlos ainda existe ---");
        f2.exibirInfo();
        
        // Pode ser contratado por outra empresa
        System.out.println("\n--- Nova contratação ---");
        Empresa startupXYZ = new Empresa("Startup XYZ", "98.765.432/0001-10", 5);
        startupXYZ.contratar(f2);
    }
}
```

### Observações importantes:

1. ✅ Funcionários criados **antes** da empresa
2. ✅ Funcionário pode ser **removido** da empresa mas continua existindo
3. ✅ Funcionário pode ser **contratado** por outra empresa
4. ✅ Empresa **agrega** funcionários (losango vazio ◇)

---

## Exercício Autônomo 2 — Sistema Biblioteca-Livro

**Contexto:** Uma biblioteca empresta livros para leitores, mas os livros continuam existindo mesmo que a biblioteca feche.

**Objetivo:** Implementar agregação onde Biblioteca TEM Livros.

### Requisitos:

1. Classe `Livro`:
   - Atributos: `titulo`, `autor`, `isbn`, `disponivel` (boolean)
   - Construtor (disponivel começa como true)
   - Getters
   - Métodos:
     - `void emprestar()` — marca como não disponível
     - `void devolver()` — marca como disponível
     - `void exibirInfo()` — mostra todos os dados

2. Classe `Biblioteca`:
   - Atributos: `nome`, `endereco`, `acervo[]` (array de Livro), `qtdLivros`
   - Construtor recebe nome, endereco e capacidade do acervo
   - Métodos:
     - `void adicionarLivro(Livro livro)` — adiciona ao acervo
     - `void removerLivro(String isbn)` — remove do acervo
     - `Livro buscarPorTitulo(String titulo)` — retorna o livro ou null
     - `void listarAcervo()` — lista todos os livros
     - `void listarDisponiveis()` — lista apenas livros disponíveis

3. Classe `BibliotecaMain`:
   - Crie 5 livros
   - Crie uma biblioteca
   - Adicione os livros ao acervo
   - Liste o acervo completo
   - Empreste 2 livros
   - Liste apenas disponíveis
   - Remova 1 livro do acervo
   - Mostre que o livro removido ainda existe

### Exemplo de saída esperada:

```
Clean Code foi adicionado ao acervo de Biblioteca Central
Design Patterns foi adicionado ao acervo de Biblioteca Central
...

=== Acervo de Biblioteca Central ===
1. Clean Code - Robert Martin (Disponível)
2. Design Patterns - GoF (Disponível)
...

--- Emprestando livros ---
Clean Code foi emprestado.
Design Patterns foi emprestado.

=== Livros disponíveis ===
3. Refactoring - Martin Fowler (Disponível)
...

--- Removendo livro do acervo ---
Clean Code foi removido do acervo de Biblioteca Central

--- Livro removido ainda existe ---
Título: Clean Code
Autor: Robert Martin
ISBN: 978-0132350884
Status: Emprestado
```

### Dicas:

- Use um método auxiliar para encontrar índice do livro no array
- Lembre-se: remover do acervo ≠ destruir o livro
- Emprestar/devolver muda o estado do livro (atributo `disponivel`)

---

## Resumo do Bloco 2

Neste bloco você aprendeu:

✅ **Agregação** = relação TODO-PARTE com vidas independentes  
✅ Representada por **array ou coleção** de objetos  
✅ Símbolo UML: **◇ (losango vazio)**  
✅ A parte pode **existir sem o todo**  
✅ A parte pode **mudar de todo**  
✅ Exemplos: Empresa-Funcionário, Time-Jogador, Biblioteca-Livro  

**Próximo passo:** No **Bloco 3**, você aprenderá sobre **Composição** — onde a parte **NÃO pode existir** sem o todo!

[➡️ Ir para Bloco 3](../Bloco3/README.md)
