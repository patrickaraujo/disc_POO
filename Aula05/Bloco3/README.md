# Bloco 3 — Sobrecarga de Métodos e Construtores

## Objetivos do Bloco

- Entender o conceito de sobrecarga (overloading): mesmo nome, parâmetros diferentes
- Aplicar sobrecarga em métodos comuns (não apenas construtores)
- Reconhecer as regras que Java usa para diferenciar métodos sobrecarregados

---

## 3.1 O que é sobrecarga?

**Sobrecarga (overloading)** é ter **dois ou mais métodos com o mesmo nome** na mesma classe, diferenciados pelos parâmetros.

Já usamos sobrecarga sem perceber — no Bloco 2, criamos classes com dois ou três construtores:

```java
public Produto(String nome, double preco, int estoque) { ... }
public Produto(String nome, double preco) { ... }
public Produto(String nome) { ... }
```

Todos se chamam `Produto`, mas Java sabe qual chamar pela **quantidade e tipo dos argumentos**. Isso é sobrecarga.

**A sobrecarga vale para qualquer método, não apenas construtores.**

---

## 3.2 Regras da sobrecarga

Java diferencia métodos sobrecarregados por:

| O que diferencia | Exemplo |
|---|---|
| Quantidade de parâmetros | `calcular(double a)` vs `calcular(double a, double b)` |
| Tipo dos parâmetros | `imprimir(String s)` vs `imprimir(int n)` |
| Ordem dos tipos | `registrar(String s, int n)` vs `registrar(int n, String s)` |

**O que NÃO diferencia:**

| O que NÃO diferencia | Por quê |
|---|---|
| Nome do parâmetro | `calcular(double valor)` e `calcular(double taxa)` são iguais para o compilador |
| Tipo de retorno | `int calcular()` e `double calcular()` causam erro — Java não diferencia por retorno |

```java
// ERRADO — não compila (mesmo nome, mesmos tipos de parâmetro):
public double calcular(double valor) { ... }
public int calcular(double valor) { ... }
```

```
error: method calcular(double) is already defined in class Calculadora
```

---

## 3.3 Sobrecarga em métodos comuns — por que usar?

Sobrecarga torna a API da classe mais **flexível** e **intuitiva**. O programador que usa a classe não precisa decorar nomes diferentes — usa o mesmo nome e Java resolve qual versão chamar.

**Sem sobrecarga:**
```java
conta.depositarComDescricao(100.0, "Salário");
conta.depositarSemDescricao(100.0);
```

**Com sobrecarga:**
```java
conta.depositar(100.0, "Salário");
conta.depositar(100.0);
```

Mesmo nome, comportamento adaptado ao contexto.

---

## Exercício Guiado 1 — Calculadora com métodos sobrecarregados (professor + alunos)

Vamos criar uma classe utilitária com várias versões do método `somar()`.

### Passo 1 — `Calculadora.java`:

```java
public class Calculadora {

    // Soma de dois inteiros
    public int somar(int a, int b) {
        System.out.println("Chamou somar(int, int)");
        return a + b;
    }

    // Soma de dois doubles
    public double somar(double a, double b) {
        System.out.println("Chamou somar(double, double)");
        return a + b;
    }

    // Soma de três inteiros
    public int somar(int a, int b, int c) {
        System.out.println("Chamou somar(int, int, int)");
        return a + b + c;
    }

    // Soma de um inteiro com um double
    public double somar(int a, double b) {
        System.out.println("Chamou somar(int, double)");
        return a + b;
    }
}
```

### Passo 2 — `CalculadoraMain.java`:

```java
public class CalculadoraMain {

    public static void main(String[] args) {

        Calculadora calc = new Calculadora();

        int r1 = calc.somar(3, 5);
        System.out.println("Resultado: " + r1);
        System.out.println();

        double r2 = calc.somar(2.5, 3.7);
        System.out.println("Resultado: " + r2);
        System.out.println();

        int r3 = calc.somar(1, 2, 3);
        System.out.println("Resultado: " + r3);
        System.out.println();

        double r4 = calc.somar(10, 4.5);
        System.out.println("Resultado: " + r4);
    }
}
```

**Saída esperada:**

```
Chamou somar(int, int)
Resultado: 8

Chamou somar(double, double)
Resultado: 6.2

Chamou somar(int, int, int)
Resultado: 6

Chamou somar(int, double)
Resultado: 14.5
```

**Discussão em sala:** Java decide qual `somar()` chamar em **tempo de compilação**, olhando os tipos dos argumentos. Isso se chama **resolução estática**. Não é o objeto que decide — é o compilador.

---

## Exercício Guiado 2 — ContaBancaria com depositar sobrecarregado (professor + alunos)

Vamos adicionar duas versões de `depositar()` na `ContaBancaria`: uma simples e outra com descrição.

### Passo 1 — `ContaBancaria.java` (adicionar aos métodos existentes):

```java
public class ContaBancaria {

    private String titular;
    private String numero;
    private double saldo;

    // Construtor
    public ContaBancaria(String titular, String numero, double saldoInicial) {
        this.titular = (titular == null || titular.isEmpty()) ? "Sem nome" : titular;
        this.numero = (numero == null || numero.isEmpty()) ? "000-0" : numero;
        this.saldo = (saldoInicial < 0) ? 0 : saldoInicial;
    }

    // --- Getters ---

    public String getTitular() { return titular; }
    public String getNumero() { return numero; }
    public double getSaldo() { return saldo; }

    // --- Métodos de negócio (com sobrecarga) ---

    // Versão 1: depósito simples
    public void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para depósito.");
            return;
        }
        saldo = saldo + valor;
        System.out.println("Depósito de R$ " + valor + " realizado.");
    }

    // Versão 2: depósito com descrição
    public void depositar(double valor, String descricao) {
        if (valor <= 0) {
            System.out.println("Valor inválido para depósito.");
            return;
        }
        saldo = saldo + valor;
        System.out.println("Depósito de R$ " + valor + " realizado. (" + descricao + ")");
    }

    // Versão 1: saque simples
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

    // Versão 2: saque com descrição
    public void sacar(double valor, String descricao) {
        if (valor <= 0) {
            System.out.println("Valor inválido para saque.");
        } else if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$ " + valor + " realizado. (" + descricao + ")");
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

        ContaBancaria conta = new ContaBancaria("Ana", "001-2", 1000.0);

        // Depósito simples (1 parâmetro)
        conta.depositar(500.0);

        // Depósito com descrição (2 parâmetros)
        conta.depositar(3000.0, "Salário");

        // Saque simples
        conta.sacar(200.0);

        // Saque com descrição
        conta.sacar(150.0, "Supermercado");

        System.out.println();
        conta.exibirSaldo();
    }
}
```

**Saída esperada:**

```
Depósito de R$ 500.0 realizado.
Depósito de R$ 3000.0 realizado. (Salário)
Saque de R$ 200.0 realizado.
Saque de R$ 150.0 realizado. (Supermercado)

Conta 001-2 | Titular: Ana | Saldo: R$ 4150.0
```

**Ponto-chave:** o programador que usa a classe escolhe a versão mais conveniente. Se quer só depositar, passa um argumento. Se quer registrar uma descrição, passa dois. O mesmo nome `depositar()` serve nos dois casos — Java resolve pela quantidade de argumentos.

---

## Exercício Autônomo 1 — Classe Retangulo com construtores sobrecarregados ⭐

Crie uma classe `Retangulo` com:

**Atributos** (todos `private`): `largura` (double), `altura` (double)

**Construtores sobrecarregados:**
- `Retangulo(double largura, double altura)` — construtor completo (validar > 0; se inválido, usar 1.0)
- `Retangulo(double lado)` — cria um quadrado (delega para o completo com `this(lado, lado)`)

**Getters:** para `largura`, `altura` + getters calculados `getArea()` e `getPerimetro()`

**Método:** `exibirDados()` e `isQuadrado()`

**Teste em `RetanguloMain`:**
- Crie um retângulo 5x3 e um quadrado de lado 4
- Exiba os dados de ambos
- Confirme que `isQuadrado()` retorna `true` para o segundo

---

## Exercício Autônomo 2 — Classe Mensagem com métodos sobrecarregados ⭐

Crie uma classe `Mensagem` com:

**Atributos** (todos `private`): `remetente` (String), `destinatario` (String)

**Construtor:** `Mensagem(String remetente, String destinatario)`

**Métodos sobrecarregados `enviar()`:**
- `enviar(String texto)` → imprime "[remetente → destinatario]: texto"
- `enviar(String texto, boolean urgente)` → se urgente, imprime "[URGENTE] " antes da mensagem
- `enviar(String texto, int repeticoes)` → imprime a mensagem o número de vezes indicado

**Teste em `MensagemMain`:**
- Crie uma mensagem de "Ana" para "Carlos"
- Envie uma mensagem simples
- Envie uma mensagem urgente
- Envie uma mensagem repetida 3 vezes

---

## Resumo do Bloco 3

- Sobrecarga (overloading) = mesmo nome de método, parâmetros diferentes
- Java diferencia por quantidade, tipo e ordem dos parâmetros — **nunca** pelo tipo de retorno
- A sobrecarga vale tanto para construtores quanto para métodos comuns
- O compilador resolve qual versão chamar em tempo de compilação (resolução estática)
- Sobrecarga torna a API da classe mais flexível sem exigir nomes diferentes para cada variação

---

**Próximo passo →** [Bloco 4](../Bloco4/README.md): regras de ouro para construtores, boas práticas e exercícios de consolidação.
