# Gabarito — Exercícios Bloco 4 (Aula 04)

> Leia o gabarito apenas depois de tentar resolver por conta própria.
> O aprendizado acontece no erro — não no acerto imediato.

---

## Exercício 01 — Classe Estacionamento

### Estacionamento.java

```java
public class Estacionamento {

    private String nome;
    private int totalVagas;
    private int vagasOcupadas;

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public int getTotalVagas() {
        return totalVagas;
    }

    public int getVagasOcupadas() {
        return vagasOcupadas;
    }

    // Getter calculado
    public int getVagasDisponiveis() {
        return totalVagas - vagasOcupadas;
    }

    // --- Setters ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setTotalVagas(int total) {
        if (this.totalVagas != 0) {
            System.out.println("Erro: total de vagas já foi definido.");
            return;
        }
        if (total <= 0) {
            System.out.println("Erro: total de vagas deve ser positivo.");
            return;
        }
        this.totalVagas = total;
    }

    // --- Métodos de negócio ---

    public void entrarVeiculo() {
        if (vagasOcupadas >= totalVagas) {
            System.out.println("Estacionamento lotado.");
            return;
        }
        vagasOcupadas++;
        System.out.println("Veículo entrou. Vagas ocupadas: " + vagasOcupadas);
    }

    public void sairVeiculo() {
        if (vagasOcupadas <= 0) {
            System.out.println("Estacionamento vazio.");
            return;
        }
        vagasOcupadas--;
        System.out.println("Veículo saiu. Vagas ocupadas: " + vagasOcupadas);
    }

    public void exibirStatus() {
        System.out.println("=== " + nome + " ===");
        System.out.println("Total de vagas: " + totalVagas);
        System.out.println("Vagas ocupadas: " + vagasOcupadas);
        System.out.println("Vagas disponíveis: " + getVagasDisponiveis());
    }
}
```

### EstacionamentoMain.java

```java
public class EstacionamentoMain {

    public static void main(String[] args) {

        Estacionamento e1 = new Estacionamento();
        e1.setNome("Estacionamento Central");
        e1.setTotalVagas(3);

        e1.entrarVeiculo();
        e1.entrarVeiculo();
        e1.entrarVeiculo();
        e1.entrarVeiculo();  // lotado

        e1.sairVeiculo();

        System.out.println();
        e1.exibirStatus();
    }
}
```

**Saída:**

```
Veículo entrou. Vagas ocupadas: 1
Veículo entrou. Vagas ocupadas: 2
Veículo entrou. Vagas ocupadas: 3
Estacionamento lotado.
Veículo saiu. Vagas ocupadas: 2

=== Estacionamento Central ===
Total de vagas: 3
Vagas ocupadas: 2
Vagas disponíveis: 1
```

**Pontos de atenção:**

- `vagasOcupadas` não tem setter — só muda via `entrarVeiculo()` e `sairVeiculo()`
- `totalVagas` só pode ser definido uma vez — a verificação `this.totalVagas != 0` garante isso
- `getVagasDisponiveis()` é um getter calculado — o valor nunca fica desatualizado porque é sempre derivado

---

## Exercício 02 — Classe ContaPoupanca

### ContaPoupanca.java

```java
public class ContaPoupanca {

    private String titular;
    private double saldo;
    private double taxaRendimentoMensal;

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

        ContaPoupanca cp = new ContaPoupanca();
        cp.setTitular("Ana");
        cp.setTaxaRendimentoMensal(0.005);
        cp.depositar(1000.0);

        System.out.println();

        // Simulando 3 meses de rendimento
        for (int mes = 1; mes <= 3; mes++) {
            System.out.println("--- Mês " + mes + " ---");
            cp.aplicarRendimento();
            cp.exibirExtrato();
        }
    }
}
```

**Saída:**

```
Depósito de R$ 1000.0 realizado. Saldo: R$ 1000.0

--- Mês 1 ---
Rendimento aplicado: R$ 5.0 | Novo saldo: R$ 1005.0
=== Extrato ===
Titular: Ana
Saldo: R$ 1005.0
Taxa mensal: 0.5%

--- Mês 2 ---
Rendimento aplicado: R$ 5.025 | Novo saldo: R$ 1010.025
=== Extrato ===
Titular: Ana
Saldo: R$ 1010.025
Taxa mensal: 0.5%

--- Mês 3 ---
Rendimento aplicado: R$ 5.050125 | Novo saldo: R$ 1015.075125
=== Extrato ===
Titular: Ana
Saldo: R$ 1015.075125
Taxa mensal: 0.5%
```

**Pontos de atenção:**

- `saldo` não tem setter — só muda por `depositar()`, `sacar()` e `aplicarRendimento()`
- O rendimento é composto: cada mês aplica sobre o saldo atualizado do mês anterior
- A taxa é armazenada como decimal (0.005) mas exibida como percentual (0.5%) — a conversão acontece em `exibirExtrato()`

---

## O que revisar se tiver dificuldade

| Dificuldade | Onde revisar |
|---|---|
| Não lembro a sintaxe de getter/setter | Bloco 2 — seção 2.2 |
| Não sei quando usar `this` | Bloco 2 — seção 2.2 |
| Não sei quando NÃO criar setter | Bloco 2 — ponto-chave do Guiado 2 / Bloco 3 — seção 3.2 |
| Validação no setter não funciona | Verifique se tem `return` após a mensagem de erro |
| Getter calculado retorna valor errado | Verifique se está usando os atributos corretos na fórmula |
| Não entendi atributo "somente leitura" | Bloco 3 — seção 3.2 |
