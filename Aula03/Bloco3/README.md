# Bloco 3 — Aplicação Prática: ContaBancaria e Múltiplos Objetos

## Objetivos do Bloco

- Implementar uma classe com regras de negócio reais nos métodos
- Criar e operar múltiplos objetos da mesma classe
- Comparar a versão OO da conta bancária com a versão procedural da Aula 01
- Resolver os exercícios práticos de forma autônoma

---

## 3.1 Retomando o problema da Aula 01

No Bloco 1 desta aula vimos que o código procedural de uma conta bancária não escala. Agora vamos reescrever esse mesmo exemplo usando OO — e perceber a diferença de organização.

A regra é clara: **os dados da conta e as regras que os governam ficam juntos, dentro da classe**.

---

## 3.2 A classe ContaBancaria

Crie o arquivo `ContaBancaria.java`:

```java
public class ContaBancaria {

    // Atributos — estado da conta
    String titular;
    String numero;
    double saldo;

    // Métodos — comportamento da conta

    void depositar(double valor) {
        if (valor > 0) {
            saldo = saldo + valor;
            System.out.println("Depósito de R$ " + valor + " realizado.");
            System.out.println("Novo saldo: R$ " + saldo);
        } else {
            System.out.println("Valor inválido para depósito.");
        }
    }

    void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para saque.");
        } else if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$ " + valor + " realizado.");
            System.out.println("Novo saldo: R$ " + saldo);
        }
    }

    void exibirSaldo() {
        System.out.println("=== Conta " + numero + " ===");
        System.out.println("Titular: " + titular);
        System.out.println("Saldo:   R$ " + saldo);
    }
}
```

Compare com a versão procedural da Aula 01:

- Antes: a lógica de saque estava no `main`, junto com todas as outras instruções
- Agora: a lógica de saque **pertence à própria conta** — ela sabe se pode ou não executar a operação
- O `main` passa a ser apenas um orquestrador: cria objetos e pede que eles se comportem

---

## 3.3 Usando a ContaBancaria com múltiplos objetos

Crie o arquivo `BancoMain.java`:

```java
public class BancoMain {

    public static void main(String[] args) {

        // Criando duas contas independentes
        ContaBancaria conta1 = new ContaBancaria();
        conta1.titular = "Ana";
        conta1.numero = "001-2";
        conta1.saldo = 1000.0;

        ContaBancaria conta2 = new ContaBancaria();
        conta2.titular = "Carlos";
        conta2.numero = "002-5";
        conta2.saldo = 500.0;

        // Operações na conta1
        System.out.println("--- Operações de Ana ---");
        conta1.depositar(300.0);
        conta1.sacar(150.0);
        conta1.exibirSaldo();

        System.out.println();

        // Tentativa de saque acima do saldo em conta2
        System.out.println("--- Operações de Carlos ---");
        conta2.sacar(600.0);  // deve recusar
        conta2.depositar(200.0);
        conta2.exibirSaldo();
    }
}
```

**Saída esperada:**

```
--- Operações de Ana ---
Depósito de R$ 300.0 realizado.
Novo saldo: R$ 1300.0
Saque de R$ 150.0 realizado.
Novo saldo: R$ 1150.0
=== Conta 001-2 ===
Titular: Ana
Saldo:   R$ 1150.0

--- Operações de Carlos ---
Saldo insuficiente. Saldo atual: R$ 500.0
Depósito de R$ 200.0 realizado.
Novo saldo: R$ 700.0
=== Conta 002-5 ===
Titular: Carlos
Saldo:   R$ 700.0
```

Note que `conta1` e `conta2` são completamente independentes. As operações em `conta1` não afetam `conta2` de forma alguma.

---

## 3.4 Pontos de atenção

**Métodos com parâmetros:** `depositar(double valor)` e `sacar(double valor)` recebem um valor como entrada. Esse valor é usado apenas dentro do método — não é um atributo da classe.

**Métodos que operam sobre atributos:** dentro de `sacar()`, usamos `saldo` diretamente (sem `this.saldo` por enquanto). Isso é possível porque o método pertence à classe e tem acesso direto aos atributos do objeto que o chamou.

**Regras de negócio nos métodos:** a verificação `if (valor > saldo)` está dentro de `sacar()`, e não no `main`. Isso garante que a regra seja respeitada sempre que o método for chamado, independentemente de quem o chamou.

---

## 3.5 Exercícios práticos

Os exercícios estão organizados na pasta [`exercicios/`](./exercicios/README-Bl3Ex.md). Tente resolvê-los antes de consultar o gabarito.

| Exercício | Classe | Descrição |
|---|---|---|
| 01 | `Aluno` | Calcular média e exibir situação |
| 02 | `Produto` | Aplicar desconto e repor estoque |
| 03 | `ContaBancaria` | Simular agência com 3 contas |
| 04 | `Veiculo` | Modelagem e implementação livres |

---

## Resumo do Bloco 3

- A classe de domínio carrega as **regras de negócio** nos métodos — não no `main`
- Múltiplos objetos da mesma classe têm **estados completamente independentes**
- O `main` orquestra: cria objetos e coordena suas interações
- A versão OO da conta bancária escala: para 100 contas, basta criar 100 objetos

---

## Resumo geral da Aula 03

| Conceito | Em uma frase |
|---|---|
| Classe | Molde que define atributos e métodos |
| Objeto | Instância criada com `new` |
| Atributo | Dado que representa o estado do objeto |
| Método | Ação que o objeto sabe executar |
| `new` | Palavra-chave que cria um objeto na memória |
| `.` | Operador para acessar atributos e métodos |
| Independência | Cada objeto tem sua própria cópia dos atributos |

---

**Próxima aula →** Aula 04 — Encapsulamento: por que e como proteger os atributos com `private`, getters e setters.
