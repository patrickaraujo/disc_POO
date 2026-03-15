# Bloco 1 — O Problema da Inicialização Manual e o Construtor Padrão

## Objetivos do Bloco

- Perceber o problema de depender de múltiplos setters para inicializar um objeto
- Entender o que é o construtor padrão e quando Java o fornece automaticamente
- Criar o primeiro construtor personalizado para garantir que o objeto nasce válido

---

## 1.1 O que acontece quando dependemos de setters para inicializar?

Na Aula 04, criamos classes com encapsulamento completo. Para usar um objeto, fazemos algo assim:

```java
ContaBancaria c1 = new ContaBancaria();
c1.setTitular("Ana");
c1.setNumero("001-2");
c1.depositar(1000.0);
```

Funciona? Sim. Mas tem problemas sérios:

**Problema 1 — O programador pode esquecer um setter:**

```java
ContaBancaria c1 = new ContaBancaria();
c1.setNumero("001-2");
c1.depositar(1000.0);
// Esqueceu setTitular()!
c1.exibirSaldo();  // Titular: null ← objeto em estado incompleto
```

**Problema 2 — O objeto existe em um estado inválido entre a criação e os setters:**

```java
ContaBancaria c1 = new ContaBancaria();
// Neste ponto, c1 existe com titular=null, numero=null, saldo=0.0
// O objeto está "vivo" mas incompleto — é um zumbi
```

**Problema 3 — Nenhuma regra obriga a inicialização:**

O compilador não reclama se você criar o objeto e nunca chamar nenhum setter. O código compila, mas gera resultados inesperados em execução.

> **O problema:** não há garantia de que o objeto nasce em um estado válido. Quem cria o objeto precisa "lembrar" de tudo — e memória de programador não é confiável.

---

## 1.2 O que é um construtor?

Um **construtor** é um método especial que:

1. **Tem o mesmo nome da classe** (exatamente, incluindo maiúsculas)
2. **Não tem tipo de retorno** (nem `void`)
3. **É chamado automaticamente** quando usamos `new`

```java
ContaBancaria c1 = new ContaBancaria();
//                     ^^^^^^^^^^^^^^^^
//                     Isso é uma chamada ao construtor
```

Toda vez que você escreve `new NomeDaClasse()`, Java chama o construtor da classe. Até agora, estávamos usando o **construtor padrão** sem saber.

---

## 1.3 O construtor padrão — o que Java faz por você

Quando você **não declara nenhum construtor** na sua classe, Java gera automaticamente um construtor padrão invisível:

```java
public class Produto {
    private String nome;
    private double preco;

    // Java gera isso automaticamente (você não vê no código):
    // public Produto() { }
}
```

Esse construtor padrão:
- Não recebe parâmetros
- Não faz nada além de criar o objeto
- Deixa os atributos com seus valores padrão (`null`, `0`, `0.0`, `false`)

**Regra importante:** o construtor padrão **só existe se você não declarar nenhum construtor**. Se você criar qualquer construtor personalizado, o padrão desaparece.

---

## 1.4 Criando um construtor personalizado — antes e depois

**Antes (Aula 04) — inicialização por setters:**

```java
Pessoa p1 = new Pessoa();
p1.setNome("Ana");
p1.setIdade(29);
p1.setCpf("111.222.333-44");
```

**Depois (Aula 05) — inicialização por construtor:**

```java
Pessoa p1 = new Pessoa("Ana", 29, "111.222.333-44");
// O objeto já nasce com nome, idade e CPF definidos
```

Uma linha em vez de quatro. E o mais importante: **o compilador obriga** a passar os dados. Não dá para esquecer.

---

## Exercício Guiado 1 — Construtor em Pessoa (professor + alunos)

Vamos adicionar um construtor à classe `Pessoa` da Aula 04.

### Passo 1 — `Pessoa.java` com construtor:

```java
public class Pessoa {

    private String nome;
    private int idade;
    private String cpf;

    // Construtor personalizado
    public Pessoa(String nome, int idade, String cpf) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
    }

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getCpf() {
        return cpf;
    }

    // --- Setters (nome e idade podem ser alterados; CPF não) ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setIdade(int idade) {
        if (idade < 0 || idade > 150) {
            System.out.println("Erro: idade inválida.");
            return;
        }
        this.idade = idade;
    }

    // Sem setCpf() — CPF é definido no construtor e nunca muda

    // --- Outros métodos ---

    public void apresentar() {
        System.out.println("Nome: " + nome + " | Idade: " + idade + " | CPF: " + cpf);
    }
}
```

### Passo 2 — `PessoaMain.java`:

```java
public class PessoaMain {

    public static void main(String[] args) {

        // Antes (Aula 04): new Pessoa() + vários setters
        // Agora (Aula 05): tudo no construtor
        Pessoa p1 = new Pessoa("Ana", 29, "111.222.333-44");
        Pessoa p2 = new Pessoa("Carlos", 35, "555.666.777-88");

        p1.apresentar();
        p2.apresentar();

        // Alterando nome via setter (ainda funciona)
        p1.setNome("Ana Maria");
        p1.apresentar();

        // Tentativa de criar sem argumentos → ERRO de compilação!
        // Pessoa p3 = new Pessoa();  // Não compila!
    }
}
```

### Passo 3 — Compile e execute:

```bash
javac Pessoa.java PessoaMain.java
java PessoaMain
```

**Saída esperada:**

```
Nome: Ana | Idade: 29 | CPF: 111.222.333-44
Nome: Carlos | Idade: 35 | CPF: 555.666.777-88
Nome: Ana Maria | Idade: 29 | CPF: 111.222.333-44
```

**Discussão em sala:** descomente a linha `Pessoa p3 = new Pessoa();` e tente compilar. O que acontece?

```
error: constructor Pessoa in class Pessoa cannot be applied to given types;
        Pessoa p3 = new Pessoa();
                    ^
  required: String,int,String
  found:    no arguments
```

Como declaramos um construtor com parâmetros, o construtor padrão (sem parâmetros) **desapareceu**. Agora é obrigatório informar nome, idade e CPF para criar uma Pessoa. Isso é uma **garantia do compilador**.

---

## Exercício Guiado 2 — Construtor em ContaBancaria (professor + alunos)

Agora vamos aplicar o construtor à `ContaBancaria`, com validação.

### Passo 1 — `ContaBancaria.java` com construtor:

```java
public class ContaBancaria {

    private String titular;
    private String numero;
    private double saldo;

    // Construtor com validação
    public ContaBancaria(String titular, String numero, double saldoInicial) {
        if (titular == null || titular.isEmpty()) {
            System.out.println("Erro: titular não pode ser vazio. Usando 'Sem nome'.");
            this.titular = "Sem nome";
        } else {
            this.titular = titular;
        }

        if (numero == null || numero.isEmpty()) {
            System.out.println("Erro: número da conta não pode ser vazio. Usando '000-0'.");
            this.numero = "000-0";
        } else {
            this.numero = numero;
        }

        if (saldoInicial < 0) {
            System.out.println("Erro: saldo inicial não pode ser negativo. Usando 0.");
            this.saldo = 0;
        } else {
            this.saldo = saldoInicial;
        }
    }

    // --- Getters ---

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    // --- Setters ---

    public void setTitular(String titular) {
        if (titular == null || titular.isEmpty()) {
            System.out.println("Erro: titular não pode ser vazio.");
            return;
        }
        this.titular = titular;
    }

    // Sem setNumero() — número da conta é definido no construtor
    // Sem setSaldo() — saldo só muda via depositar() e sacar()

    // --- Métodos de negócio ---

    public void depositar(double valor) {
        if (valor > 0) {
            saldo = saldo + valor;
            System.out.println("Depósito de R$ " + valor + " realizado.");
        } else {
            System.out.println("Valor inválido para depósito.");
        }
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para saque.");
        } else if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$ " + valor + " realizado.");
        }
    }

    public void exibirSaldo() {
        System.out.println("Conta " + numero + " | Titular: " + titular + " | Saldo: R$ " + saldo);
    }
}
```

### Passo 2 — `BancoMain.java`:

```java
public class BancoMain {

    public static void main(String[] args) {

        // Antes: new ContaBancaria() + setTitular() + setNumero() + depositar()
        // Agora: tudo no construtor
        ContaBancaria c1 = new ContaBancaria("Ana", "001-2", 1000.0);
        ContaBancaria c2 = new ContaBancaria("Carlos", "002-5", 500.0);

        c1.exibirSaldo();
        c2.exibirSaldo();

        System.out.println();

        // Operações normais continuam funcionando
        c1.depositar(200.0);
        c2.sacar(100.0);

        c1.exibirSaldo();
        c2.exibirSaldo();

        System.out.println();

        // Testando validação do construtor
        ContaBancaria c3 = new ContaBancaria("", "003-1", -500.0);
        c3.exibirSaldo();
    }
}
```

**Saída esperada:**

```
Conta 001-2 | Titular: Ana | Saldo: R$ 1000.0
Conta 002-5 | Titular: Carlos | Saldo: R$ 500.0

Depósito de R$ 200.0 realizado.
Saque de R$ 100.0 realizado.
Conta 001-2 | Titular: Ana | Saldo: R$ 1200.0
Conta 002-5 | Titular: Carlos | Saldo: R$ 400.0

Erro: titular não pode ser vazio. Usando 'Sem nome'.
Erro: saldo inicial não pode ser negativo. Usando 0.
Conta 003-1 | Titular: Sem nome | Saldo: R$ 0.0
```

**Ponto-chave:** o construtor também pode (e deve) validar os dados. A diferença é que no construtor não podemos simplesmente fazer `return` e deixar o atributo vazio — o objeto precisa existir em algum estado. Por isso usamos valores padrão seguros quando o dado é inválido.

---

## Exercício Autônomo 1 — Construtor para Produto ⭐

Adicione um construtor à classe `Produto` da Aula 04.

**Construtor:** `public Produto(String nome, double preco, int quantidadeEstoque)`

**Validações no construtor:**
- Nome vazio ou null → usar "Sem nome"
- Preço negativo → usar 0.0
- Quantidade negativa → usar 0

**Teste em `ProdutoMain`:**
- Crie 2 produtos com dados válidos
- Crie 1 produto com dados inválidos (preço negativo)
- Exiba todos
- Tente `new Produto()` sem argumentos → confirme que não compila

---

## Exercício Autônomo 2 — Construtor para Aluno ⭐

Adicione um construtor à classe `Aluno` da Aula 04.

**Construtor:** `public Aluno(String nome, String matricula, double nota1, double nota2)`

**Validações no construtor:**
- Nome vazio ou null → usar "Sem nome"
- Matrícula vazia ou null → usar "000000"
- Nota fora de 0.0–10.0 → usar 0.0

**Teste em `AlunoMain`:**
- Crie 3 alunos com notas diferentes (um aprovado, um reprovado, um na fronteira)
- Crie 1 aluno com nota inválida (ex.: 12.0) → deve usar 0.0
- Chame `exibirSituacao()` para todos

---

## Resumo do Bloco 1

- Depender de setters para inicializar gera objetos "zumbis" (existem mas estão incompletos)
- O construtor padrão (sem parâmetros) é gerado automaticamente **apenas** se nenhum construtor for declarado
- Construtores personalizados recebem os dados obrigatórios e inicializam o objeto de uma vez
- Se você declarar qualquer construtor, o padrão desaparece — o compilador força a inicialização
- Construtores também devem validar os dados recebidos

---

**Próximo passo →** [Bloco 2](../Bloco2/README.md): vamos entender o `this` a fundo e explorar como inicializar atributos de forma mais elegante.
