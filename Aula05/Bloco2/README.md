# Bloco 2 — Construtores Personalizados e a Palavra-Chave `this`

## Objetivos do Bloco

- Dominar o uso de `this` para diferenciar atributos de parâmetros
- Entender `this()` como chamada a outro construtor da mesma classe
- Criar construtores que delegam para outros construtores, evitando duplicação de código

---

## 2.1 O `this` — duas funções distintas

A palavra-chave `this` em Java tem dois usos completamente diferentes:

| Uso | Significado | Exemplo |
|---|---|---|
| `this.atributo` | "O atributo **do objeto atual**" | `this.nome = nome;` |
| `this(argumentos)` | "Chame **outro construtor** desta mesma classe" | `this(nome, 0.0);` |

O primeiro uso já vimos no Bloco 2 da Aula 04 (getters e setters). O segundo uso é novidade e será explorado neste bloco.

---

## 2.2 `this.atributo` — diferenciando atributo de parâmetro

Quando o parâmetro do construtor tem o **mesmo nome** do atributo, o Java precisa saber de qual você está falando:

```java
public Pessoa(String nome, int idade) {
    this.nome = nome;   // this.nome = atributo | nome = parâmetro
    this.idade = idade;  // this.idade = atributo | idade = parâmetro
}
```

**Sem `this`, o que acontece?**

```java
public Pessoa(String nome, int idade) {
    nome = nome;    // Atribui o parâmetro a ele mesmo — não faz nada!
    idade = idade;  // O atributo continua null / 0
}
```

O Java não dá erro — simplesmente ignora a atribuição porque `nome = nome` refere-se ao parâmetro nos dois lados. O atributo fica com o valor padrão.

> **Regra prática:** sempre use `this.atributo` no construtor quando o parâmetro tem o mesmo nome do atributo. É uma convenção tão forte em Java que se tornou padrão no mercado.

---

## 2.3 `this()` — chamando outro construtor

Quando uma classe tem mais de um construtor, um pode **chamar o outro** usando `this()`:

```java
public class Produto {

    private String nome;
    private double preco;

    // Construtor completo
    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    // Construtor parcial — delega para o completo
    public Produto(String nome) {
        this(nome, 0.0);  // Chama o construtor acima, passando preco = 0.0
    }
}
```

**Regra obrigatória:** `this()` deve ser a **primeira instrução** do construtor. Nada pode vir antes.

```java
// ERRADO — não compila:
public Produto(String nome) {
    System.out.println("Criando produto...");  // Não pode vir antes de this()
    this(nome, 0.0);
}
```

```
error: call to this must be first statement in constructor
```

**Por que usar `this()`?** Para evitar duplicar lógica. Se a validação está no construtor completo, os outros construtores simplesmente delegam e reaproveitam a validação.

---

## Exercício Guiado 1 — Funcionario com `this` completo (professor + alunos)

Vamos criar uma classe com dois construtores, onde um chama o outro.

### Passo 1 — `Funcionario.java`:

```java
public class Funcionario {

    private String nome;
    private String cargo;
    private double salario;

    // Construtor completo (com todos os atributos)
    public Funcionario(String nome, String cargo, double salario) {
        if (nome == null || nome.isEmpty()) {
            this.nome = "Sem nome";
        } else {
            this.nome = nome;
        }

        if (cargo == null || cargo.isEmpty()) {
            this.cargo = "Não definido";
        } else {
            this.cargo = cargo;
        }

        if (salario < 0) {
            this.salario = 0;
        } else {
            this.salario = salario;
        }
    }

    // Construtor parcial (só nome e cargo, salário começa em 0)
    public Funcionario(String nome, String cargo) {
        this(nome, cargo, 0.0);  // Delega para o construtor completo
    }

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }

    // --- Setters ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.isEmpty()) {
            System.out.println("Erro: cargo não pode ser vazio.");
            return;
        }
        this.cargo = cargo;
    }

    public void setSalario(double salario) {
        if (salario < 0) {
            System.out.println("Erro: salário não pode ser negativo.");
            return;
        }
        this.salario = salario;
    }

    // --- Métodos ---

    public void exibirDados() {
        System.out.println("Nome: " + nome + " | Cargo: " + cargo + " | Salário: R$ " + salario);
    }
}
```

### Passo 2 — `FuncionarioMain.java`:

```java
public class FuncionarioMain {

    public static void main(String[] args) {

        // Usando o construtor completo (3 parâmetros)
        Funcionario f1 = new Funcionario("Ana", "Desenvolvedora", 5000.0);

        // Usando o construtor parcial (2 parâmetros — salário fica 0.0)
        Funcionario f2 = new Funcionario("Carlos", "Estagiário");

        f1.exibirDados();
        f2.exibirDados();

        System.out.println();

        // Definindo salário depois via setter
        f2.setSalario(1500.0);
        f2.exibirDados();
    }
}
```

**Saída esperada:**

```
Nome: Ana | Cargo: Desenvolvedora | Salário: R$ 5000.0
Nome: Carlos | Cargo: Estagiário | Salário: R$ 0.0

Nome: Carlos | Cargo: Estagiário | Salário: R$ 1500.0
```

**Discussão em sala:** o construtor `Funcionario(String, String)` não duplica a validação — ele chama `this(nome, cargo, 0.0)` que executa toda a lógica do construtor completo. Se amanhã mudarmos as regras de validação, alteramos em **um único lugar**.

---

## Exercício Guiado 2 — Produto com `this()` encadeado (professor + alunos)

Vamos criar uma classe com **três construtores**, onde cada um delega para o mais completo.

### Passo 1 — `Produto.java`:

```java
public class Produto {

    private String nome;
    private double preco;
    private int quantidadeEstoque;

    // Construtor completo
    public Produto(String nome, double preco, int quantidadeEstoque) {
        if (nome == null || nome.isEmpty()) {
            this.nome = "Sem nome";
        } else {
            this.nome = nome;
        }

        if (preco < 0) {
            this.preco = 0;
        } else {
            this.preco = preco;
        }

        if (quantidadeEstoque < 0) {
            this.quantidadeEstoque = 0;
        } else {
            this.quantidadeEstoque = quantidadeEstoque;
        }
    }

    // Construtor parcial — sem estoque (começa em 0)
    public Produto(String nome, double preco) {
        this(nome, preco, 0);  // Delega para o completo
    }

    // Construtor mínimo — só nome (preço 0, estoque 0)
    public Produto(String nome) {
        this(nome, 0.0, 0);  // Delega para o completo
    }

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    // --- Setters ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            System.out.println("Erro: preço não pode ser negativo.");
            return;
        }
        this.preco = preco;
    }

    // Sem setter para estoque — controle por métodos de negócio

    // --- Métodos de negócio ---

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Erro: quantidade deve ser positiva.");
            return;
        }
        quantidadeEstoque = quantidadeEstoque + quantidade;
        System.out.println(quantidade + " unidades adicionadas ao estoque de " + nome + ".");
    }

    public void exibirInformacoes() {
        System.out.println("Produto: " + nome + " | Preço: R$ " + preco + " | Estoque: " + quantidadeEstoque);
    }
}
```

### Passo 2 — `ProdutoMain.java`:

```java
public class ProdutoMain {

    public static void main(String[] args) {

        // Três formas de criar um Produto
        Produto p1 = new Produto("Notebook", 3500.0, 10);   // completo
        Produto p2 = new Produto("Mouse", 89.90);            // sem estoque
        Produto p3 = new Produto("Teclado");                  // só nome

        p1.exibirInformacoes();
        p2.exibirInformacoes();
        p3.exibirInformacoes();

        System.out.println();

        // Completando os dados via setters e métodos
        p2.adicionarEstoque(50);
        p3.setPreco(149.90);
        p3.adicionarEstoque(30);

        p2.exibirInformacoes();
        p3.exibirInformacoes();
    }
}
```

**Saída esperada:**

```
Produto: Notebook | Preço: R$ 3500.0 | Estoque: 10
Produto: Mouse | Preço: R$ 89.9 | Estoque: 0
Produto: Teclado | Preço: R$ 0.0 | Estoque: 0

50 unidades adicionadas ao estoque de Mouse.
30 unidades adicionadas ao estoque de Teclado.
Produto: Mouse | Preço: R$ 89.9 | Estoque: 50
Produto: Teclado | Preço: R$ 149.9 | Estoque: 30
```

**Ponto-chave:** há três "portas de entrada" para criar um `Produto`. Todas convergem para o construtor completo via `this()`. A validação está centralizada — não importa qual construtor o programador use, as regras são aplicadas.

---

## Exercício Autônomo 1 — Livro com dois construtores ⭐

Crie uma classe `Livro` com:

**Atributos** (todos `private`): `titulo` (String), `autor` (String), `preco` (double)

**Construtores:**
- `Livro(String titulo, String autor, double preco)` — construtor completo com validação (titulo/autor vazios → "Desconhecido"; preço negativo → 0.0)
- `Livro(String titulo, String autor)` — delega para o completo com preço 0.0

**Getters:** para todos. **Setters:** `setPreco()` com validação (não aceitar negativo).

**Teste em `LivroMain`:**
- Crie um livro com todos os dados e outro sem preço
- Exiba os dois
- Defina o preço do segundo via setter
- Exiba novamente

---

## Exercício Autônomo 2 — ContaEnergia com construtor ⭐

Reescreva a classe `ContaEnergia` da Aula 04, agora com construtor.

**Construtor:** `ContaEnergia(String nomeCliente, int leituraAnterior, int leituraAtual, double tarifaPorKwh)`

**Validações no construtor:**
- Nome vazio → "Sem nome"
- Leitura negativa → 0
- Leitura atual menor que anterior → igualar à anterior
- Tarifa <= 0 → usar 0.01

**Getters calculados:** `getConsumo()` e `getValorConta()` (mantidos da Aula 04).

**Teste:** crie duas contas de energia e exiba a fatura de cada uma.

---

## Resumo do Bloco 2

- `this.atributo` diferencia o atributo do objeto do parâmetro com mesmo nome
- Sem `this`, `nome = nome` atribui o parâmetro a ele mesmo — o atributo fica `null`
- `this(argumentos)` chama outro construtor da mesma classe — deve ser a primeira instrução
- Encadear construtores com `this()` centraliza a validação e evita duplicação de código
- O construtor mais completo contém a lógica; os demais delegam para ele

---

**Próximo passo →** [Bloco 3](../Bloco3/README.md): vamos entender sobrecarga de métodos — o conceito que permite ter vários construtores na mesma classe.
