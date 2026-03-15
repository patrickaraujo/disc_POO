# Gabarito — Exercícios Bloco 4 (Aula 05)

> Leia o gabarito apenas depois de tentar resolver por conta própria.
> O aprendizado acontece no erro — não no acerto imediato.

---

## Exercício 01 — Classe Pedido

### Pedido.java

```java
public class Pedido {

    private int numero;
    private String nomeCliente;
    private int totalItens;
    private double valorTotal;

    // Construtor
    public Pedido(int numero, String nomeCliente) {
        this.numero = (numero <= 0) ? 1 : numero;
        this.nomeCliente = (nomeCliente == null || nomeCliente.isEmpty()) ? "Cliente" : nomeCliente;
        this.totalItens = 0;
        this.valorTotal = 0.0;
    }

    // --- Getters ---

    public int getNumero() {
        return numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getTotalItens() {
        return totalItens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // --- Métodos sobrecarregados ---

    // Adiciona 1 item
    public void adicionarItem(double preco) {
        if (preco <= 0) {
            System.out.println("Erro: preço deve ser positivo.");
            return;
        }
        totalItens++;
        valorTotal = valorTotal + preco;
        System.out.println("Item adicionado: R$ " + preco
                + " | Itens: " + totalItens + " | Total: R$ " + valorTotal);
    }

    // Adiciona múltiplos itens
    public void adicionarItem(double preco, int quantidade) {
        if (preco <= 0) {
            System.out.println("Erro: preço deve ser positivo.");
            return;
        }
        if (quantidade <= 0) {
            System.out.println("Erro: quantidade deve ser positiva.");
            return;
        }
        totalItens = totalItens + quantidade;
        valorTotal = valorTotal + (preco * quantidade);
        System.out.println(quantidade + " itens adicionados: R$ " + preco
                + " cada | Itens: " + totalItens + " | Total: R$ " + valorTotal);
    }

    // --- Exibição ---

    public void exibirResumo() {
        System.out.println("=== Pedido #" + numero + " ===");
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Itens: " + totalItens);
        System.out.println("Valor total: R$ " + valorTotal);
    }
}
```

### PedidoMain.java

```java
public class PedidoMain {

    public static void main(String[] args) {

        Pedido p1 = new Pedido(1, "Ana");

        p1.adicionarItem(25.0);
        p1.adicionarItem(12.50, 3);
        p1.adicionarItem(-5.0);  // deve ser recusado

        System.out.println();
        p1.exibirResumo();
    }
}
```

**Saída:**

```
Item adicionado: R$ 25.0 | Itens: 1 | Total: R$ 25.0
3 itens adicionados: R$ 12.5 cada | Itens: 4 | Total: R$ 62.5
Erro: preço deve ser positivo.

=== Pedido #1 ===
Cliente: Ana
Itens: 4
Valor total: R$ 62.5
```

**Pontos de atenção:**

- `adicionarItem(double)` e `adicionarItem(double, int)` são sobrecarga — mesmo nome, parâmetros diferentes
- O `numero` e `nomeCliente` são definidos no construtor. O `totalItens` e `valorTotal` começam em 0 e só mudam pelos métodos de negócio
- A validação no construtor usa operador ternário para manter o código conciso

---

## Exercício 02 — Classe ContaPoupanca

### ContaPoupanca.java

```java
public class ContaPoupanca {

    private String titular;
    private double saldo;
    private double taxaRendimentoMensal;

    // Construtor completo
    public ContaPoupanca(String titular, double saldoInicial, double taxaRendimentoMensal) {
        this.titular = (titular == null || titular.isEmpty()) ? "Sem nome" : titular;
        this.saldo = (saldoInicial < 0) ? 0 : saldoInicial;
        this.taxaRendimentoMensal = (taxaRendimentoMensal < 0 || taxaRendimentoMensal > 1) ? 0.005 : taxaRendimentoMensal;
    }

    // Construtor parcial — taxa padrão
    public ContaPoupanca(String titular, double saldoInicial) {
        this(titular, saldoInicial, 0.005);
    }

    // Construtor mínimo — saldo 0 e taxa padrão
    public ContaPoupanca(String titular) {
        this(titular, 0.0, 0.005);
    }

    // --- Getters ---

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getTaxaRendimentoMensal() {
        return taxaRendimentoMensal;
    }

    // --- Setters ---

    public void setTitular(String titular) {
        if (titular == null || titular.isEmpty()) {
            System.out.println("Erro: titular não pode ser vazio.");
            return;
        }
        this.titular = titular;
    }

    public void setTaxaRendimentoMensal(double taxa) {
        if (taxa < 0 || taxa > 1) {
            System.out.println("Erro: taxa deve estar entre 0 e 1.");
            return;
        }
        this.taxaRendimentoMensal = taxa;
    }

    // Sem setSaldo() — saldo só muda via depósito, saque ou rendimento

    // --- Métodos de negócio ---

    public void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: valor deve ser positivo.");
            return;
        }
        saldo = saldo + valor;
        System.out.println("Depósito de R$ " + valor + " realizado. Saldo: R$ " + saldo);
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: valor deve ser positivo.");
            return;
        }
        if (valor > saldo) {
            System.out.println("Saldo insuficiente. Saldo atual: R$ " + saldo);
            return;
        }
        saldo = saldo - valor;
        System.out.println("Saque de R$ " + valor + " realizado. Saldo: R$ " + saldo);
    }

    public void aplicarRendimento() {
        double rendimento = saldo * taxaRendimentoMensal;
        saldo = saldo + rendimento;
        System.out.println("Rendimento aplicado: R$ " + rendimento + " | Novo saldo: R$ " + saldo);
    }

    public void exibirExtrato() {
        System.out.println("=== Extrato ===");
        System.out.println("Titular: " + titular);
        System.out.println("Saldo: R$ " + saldo);
        System.out.println("Taxa mensal: " + (taxaRendimentoMensal * 100) + "%");
        System.out.println();
    }
}
```

### ContaPoupancaMain.java

```java
public class ContaPoupancaMain {

    public static void main(String[] args) {

        // Três formas de criar conta
        ContaPoupanca conta1 = new ContaPoupanca("Ana", 1000.0, 0.01);
        ContaPoupanca conta2 = new ContaPoupanca("Carlos", 500.0);
        ContaPoupanca conta3 = new ContaPoupanca("Maria");

        // Depositar na conta3
        conta3.depositar(200.0);
        System.out.println();

        // Aplicar rendimento em todas
        conta1.aplicarRendimento();
        conta2.aplicarRendimento();
        conta3.aplicarRendimento();
        System.out.println();

        // Exibir extratos
        conta1.exibirExtrato();
        conta2.exibirExtrato();
        conta3.exibirExtrato();
    }
}
```

**Saída:**

```
Depósito de R$ 200.0 realizado. Saldo: R$ 200.0

Rendimento aplicado: R$ 10.0 | Novo saldo: R$ 1010.0
Rendimento aplicado: R$ 2.5 | Novo saldo: R$ 502.5
Rendimento aplicado: R$ 1.0 | Novo saldo: R$ 201.0

=== Extrato ===
Titular: Ana
Saldo: R$ 1010.0
Taxa mensal: 1.0%

=== Extrato ===
Titular: Carlos
Saldo: R$ 502.5
Taxa mensal: 0.5%

=== Extrato ===
Titular: Maria
Saldo: R$ 201.0
Taxa mensal: 0.5%
```

**Pontos de atenção:**

- Há três construtores: completo, parcial e mínimo. Os dois últimos delegam para o completo via `this()`
- A taxa padrão de 0.005 (0,5%) é usada quando não informada — centralizada no construtor completo
- Conta3 foi criada só com nome (saldo 0, taxa padrão). O depósito posterior demonstra que setters e construtores trabalham juntos
- O rendimento de Ana (1%) é diferente do de Carlos e Maria (0,5%) — cada objeto tem seu próprio estado

---

## O que revisar se tiver dificuldade

| Dificuldade | Onde revisar |
|---|---|
| Não lembro a sintaxe do construtor | Bloco 1 — seção 1.2 |
| Construtor padrão desapareceu e não sei por quê | Bloco 1 — seção 1.3 |
| Confusão entre `this.nome` e `nome` | Bloco 2 — seção 2.2 |
| `this()` dá erro de compilação | Bloco 2 — seção 2.3 (deve ser a primeira instrução) |
| Não sei quando usar sobrecarga | Bloco 3 — seção 3.3 |
| Métodos sobrecarregados dão erro | Bloco 3 — seção 3.2 (verificar se diferem nos parâmetros, não no retorno) |
| Não sei quando usar construtor vs setter | Bloco 4 — seção 4.4 |
