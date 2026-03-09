# Bloco 2 — Getters e Setters: Acesso Controlado

## Objetivos do Bloco

- Entender o papel de getters (leitura) e setters (escrita) como porta de entrada controlada
- Implementar getters e setters seguindo a convenção Java
- Adicionar validações dentro de setters para proteger o estado do objeto

---

## 2.1 O conceito em uma frase

> **Getter** = método público que **retorna** o valor de um atributo privado.
> **Setter** = método público que **altera** o valor de um atributo privado (com ou sem validação).

A convenção Java é simples:

| Atributo | Getter | Setter |
|---|---|---|
| `private String nome` | `public String getNome()` | `public void setNome(String nome)` |
| `private int idade` | `public int getIdade()` | `public void setIdade(int idade)` |
| `private double saldo` | `public double getSaldo()` | `public void setSaldo(double saldo)` |
| `private boolean ativo` | `public boolean isAtivo()` | `public void setAtivo(boolean ativo)` |

Note: para `boolean`, a convenção do getter é `is` em vez de `get`.

---

## 2.2 Anatomia de um getter e um setter

```java
public class Pessoa {

    private String nome;

    // Getter — retorna o valor do atributo
    public String getNome() {
        return nome;
    }

    // Setter — recebe um valor e atribui ao atributo
    public void setNome(String nome) {
        this.nome = nome;
    }
}
```

**O `this`:** quando o parâmetro tem o mesmo nome do atributo, usamos `this.nome` para dizer "o atributo do objeto" e `nome` sozinho se refere ao parâmetro. Isso será aprofundado na Aula 05, mas aqui já é necessário para evitar ambiguidade.

---

## Exercício Guiado 1 — Pessoa com getters e setters (professor + alunos)

Vamos completar a classe `Pessoa` do Bloco 1, agora com acesso controlado.

### Passo 1 — `Pessoa.java` completa:

```java
public class Pessoa {

    private String nome;
    private int idade;
    private String cpf;

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

    // --- Setters ---

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // --- Outros métodos ---

    public void apresentar() {
        System.out.println("Nome: " + nome + " | Idade: " + idade + " | CPF: " + cpf);
    }
}
```

### Passo 2 — `PessoaMain.java` usando getters e setters:

```java
public class PessoaMain {

    public static void main(String[] args) {

        Pessoa p1 = new Pessoa();

        // Antes: p1.nome = "Ana"        → ERRO (private)
        // Agora: p1.setNome("Ana")       → OK (setter público)
        p1.setNome("Ana");
        p1.setIdade(29);
        p1.setCpf("111.222.333-44");

        p1.apresentar();

        // Lendo um atributo específico via getter
        System.out.println("Nome via getter: " + p1.getNome());
        System.out.println("Idade via getter: " + p1.getIdade());
    }
}
```

**Saída esperada:**

```
Nome: Ana | Idade: 29 | CPF: 111.222.333-44
Nome via getter: Ana
Idade via getter: 29
```

**Discussão em sala:** Parece mais código para fazer a mesma coisa? Sim — por enquanto. Mas o setter é uma **porta com validação**. Vamos ver isso no próximo exercício.

---

## Exercício Guiado 2 — ContaBancaria com getters, setters e validação (professor + alunos)

Agora a parte mais importante: setters que **recusam valores inválidos**.

### Passo 1 — `ContaBancaria.java` com acesso controlado:

```java
public class ContaBancaria {

    private String titular;
    private String numero;
    private double saldo;

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

    // --- Setters com validação ---

    public void setTitular(String titular) {
        if (titular == null || titular.isEmpty()) {
            System.out.println("Erro: titular não pode ser vazio.");
            return;
        }
        this.titular = titular;
    }

    public void setNumero(String numero) {
        if (numero == null || numero.isEmpty()) {
            System.out.println("Erro: número da conta não pode ser vazio.");
            return;
        }
        this.numero = numero;
    }

    // Nota: NÃO criamos setSaldo() público!
    // O saldo só muda via depositar() e sacar().

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

        ContaBancaria c1 = new ContaBancaria();

        // Definindo via setters
        c1.setTitular("Ana");
        c1.setNumero("001-2");

        // Tentativa de titular vazio — setter recusa
        c1.setTitular("");

        // Saldo só muda por depósito/saque
        c1.depositar(1000.0);
        c1.sacar(200.0);

        c1.exibirSaldo();

        // Lendo saldo via getter
        System.out.println("Saldo atual: R$ " + c1.getSaldo());
    }
}
```

**Saída esperada:**

```
Erro: titular não pode ser vazio.
Depósito de R$ 1000.0 realizado.
Saque de R$ 200.0 realizado.
Conta 001-2 | Titular: Ana | Saldo: R$ 800.0
Saldo atual: R$ 800.0
```

**Ponto-chave:** não existe `setSaldo()` público. O saldo é controlado exclusivamente por `depositar()` e `sacar()`, que aplicam suas próprias regras. Isso é encapsulamento em ação — o objeto protege sua própria integridade.

---

## Exercício Autônomo 1 — Produto com getters e setters ⭐

Reescreva a classe `Produto` da Aula 03 com encapsulamento completo.

**Atributos** (todos `private`): `nome`, `preco`, `quantidadeEstoque`

**Getters:** para todos os atributos.

**Setters com validação:**
- `setNome(String nome)` → não aceitar nome vazio ou null
- `setPreco(double preco)` → não aceitar preço negativo
- `setQuantidadeEstoque(int quantidade)` → não aceitar quantidade negativa

**Método existente:** `exibirInformacoes()` continua imprimindo nome, preço e estoque.

**Teste em `ProdutoMain`:**
- Crie um produto, defina via setters
- Tente passar um preço negativo → deve ser recusado
- Tente passar nome vazio → deve ser recusado
- Exiba as informações finais

---

## Exercício Autônomo 2 — Aluno com getters e setters ⭐

Reescreva a classe `Aluno` da Aula 03 com encapsulamento.

**Atributos** (todos `private`): `nome`, `matricula`, `nota1`, `nota2`

**Getters:** para todos os atributos.

**Setters com validação:**
- `setNome(String nome)` → não aceitar vazio ou null
- `setMatricula(String matricula)` → não aceitar vazio ou null
- `setNota1(double nota1)` → aceitar somente entre 0.0 e 10.0
- `setNota2(double nota2)` → aceitar somente entre 0.0 e 10.0

**Métodos existentes:** `calcularMedia()` e `exibirSituacao()` continuam funcionando normalmente.

**Teste em `AlunoMain`:**
- Crie 2 alunos via setters
- Tente passar nota 11.0 → deve ser recusado
- Tente passar nota -2.0 → deve ser recusado
- Exiba a situação de cada aluno

---

## Resumo do Bloco 2

- Getters retornam o valor de atributos privados; setters alteram com controle
- A convenção é `getNome()` / `setNome()` — para `boolean`, usa-se `isAtivo()`
- `this.atributo` diferencia o atributo do objeto do parâmetro do método
- Setters são o lugar ideal para colocar **validações** de entrada
- Nem todo atributo precisa de setter — o saldo de uma conta, por exemplo, só muda por operações específicas

---

**Próximo passo →** [Bloco 3](../Bloco3/README.md): vamos explorar cenários mais ricos onde a proteção do estado faz diferença real.
