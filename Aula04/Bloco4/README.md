# Bloco 4 — Boas Práticas de Visibilidade e Consolidação

## Objetivos do Bloco

- Consolidar as boas práticas de encapsulamento em um checklist prático
- Entender por que evitar o acesso direto aos atributos mesmo quando "parece mais fácil"
- Resolver exercícios que integram todos os conceitos da aula

---

## 4.1 Checklist de boas práticas

Sempre que criar uma classe em Java, siga estas regras:

| Regra | Por quê |
|---|---|
| Atributos sempre `private` | Ninguém altera o estado sem passar pelo controle da classe |
| Métodos de acesso externo `public` | O mundo externo interage pelo que a classe permite |
| Setters validam antes de atribuir | Impede estados impossíveis ou incoerentes |
| Nem todo atributo precisa de setter | Alguns valores são definidos uma vez ou só mudam por métodos específicos |
| Prefira métodos de negócio a setters genéricos | `depositar()` é melhor que `setSaldo()` porque carrega regras |
| Exponha o mínimo necessário | Se o código externo não precisa de um getter, não crie |

---

## 4.2 Por que evitar o acesso direto — resumo visual

```
SEM ENCAPSULAMENTO                    COM ENCAPSULAMENTO

 main                                  main
   │                                     │
   ├─ conta.saldo = -500  ← perigo!     ├─ conta.setSaldo(-500) ← setter recusa
   ├─ conta.saldo = 99999               ├─ conta.depositar(100) ← regra aplicada
   └─ conta.titular = ""                └─ conta.setTitular("") ← setter recusa

   O objeto fica em estado              O objeto se protege
   inconsistente e ninguém              e permanece sempre válido
   percebe até dar erro.
```

O acesso direto é perigoso porque:
1. **Não valida** — qualquer valor é aceito, inclusive absurdos
2. **Não rastreia** — se algo deu errado, não há como saber onde o valor foi alterado
3. **Não evolui** — se amanhã precisar de uma regra nova, terá que encontrar todos os pontos que acessam o atributo

Com encapsulamento, a regra fica **em um único lugar** (o setter ou método de negócio). Mudar a regra é alterar uma linha.

---

## 4.3 Comparação lado a lado — Aula 03 vs. Aula 04

**Classe da Aula 03 (sem encapsulamento):**

```java
public class Produto {
    String nome;
    double preco;
    int quantidadeEstoque;

    void exibirInformacoes() { ... }
    void aplicarDesconto(double percentual) { ... }
}
```

**Mesma classe na Aula 04 (com encapsulamento):**

```java
public class Produto {
    private String nome;
    private double preco;
    private int quantidadeEstoque;

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) { ... return; }
        this.nome = nome;
    }
    public void setPreco(double preco) {
        if (preco < 0) { ... return; }
        this.preco = preco;
    }
    public void setQuantidadeEstoque(int quantidade) {
        if (quantidade < 0) { ... return; }
        this.quantidadeEstoque = quantidade;
    }

    public void exibirInformacoes() { ... }
    public void aplicarDesconto(double percentual) { ... }
}
```

Mais código? Sim. Mais seguro? **Muito mais.** A partir de agora, **todas as classes** do curso seguirão esse padrão.

---

## Exercício Guiado 1 — Classe Retangulo completa (professor + alunos)

Um exercício que integra validação, getters calculados e métodos de negócio.

### Passo 1 — `Retangulo.java`:

```java
public class Retangulo {

    private double largura;
    private double altura;

    // --- Getters ---

    public double getLargura() {
        return largura;
    }

    public double getAltura() {
        return altura;
    }

    // Getters calculados — sem atributo próprio
    public double getArea() {
        return largura * altura;
    }

    public double getPerimetro() {
        return 2 * (largura + altura);
    }

    // --- Setters com validação ---

    public void setLargura(double largura) {
        if (largura <= 0) {
            System.out.println("Erro: largura deve ser positiva.");
            return;
        }
        this.largura = largura;
    }

    public void setAltura(double altura) {
        if (altura <= 0) {
            System.out.println("Erro: altura deve ser positiva.");
            return;
        }
        this.altura = altura;
    }

    // --- Métodos ---

    public boolean isQuadrado() {
        return largura == altura;
    }

    public void exibirDados() {
        System.out.println("Retângulo: " + largura + " x " + altura);
        System.out.println("Área: " + getArea() + " | Perímetro: " + getPerimetro());
        System.out.println("É quadrado? " + (isQuadrado() ? "Sim" : "Não"));
        System.out.println("----------");
    }
}
```

### Passo 2 — `RetanguloMain.java`:

```java
public class RetanguloMain {

    public static void main(String[] args) {

        Retangulo r1 = new Retangulo();
        r1.setLargura(5.0);
        r1.setAltura(3.0);
        r1.exibirDados();

        Retangulo r2 = new Retangulo();
        r2.setLargura(4.0);
        r2.setAltura(4.0);
        r2.exibirDados();

        // Tentativa de largura inválida
        r1.setLargura(-2.0);
        r1.exibirDados();  // largura permanece 5.0
    }
}
```

**Saída esperada:**

```
Retângulo: 5.0 x 3.0
Área: 15.0 | Perímetro: 16.0
É quadrado? Não
----------
Retângulo: 4.0 x 4.0
Área: 16.0 | Perímetro: 16.0
É quadrado? Sim
----------
Erro: largura deve ser positiva.
Retângulo: 5.0 x 3.0
Área: 15.0 | Perímetro: 16.0
É quadrado? Não
----------
```

**Destaque:** `getArea()` e `getPerimetro()` são getters calculados. `isQuadrado()` segue a convenção `is` para boolean. Nenhum desses valores é armazenado como atributo — são derivados.

---

## Exercício Guiado 2 — Classe ContaEnergia (professor + alunos)

Uma conta de energia com tarifa, leitura e cálculo automático.

### Passo 1 — `ContaEnergia.java`:

```java
public class ContaEnergia {

    private String nomeCliente;
    private int leituraAnterior;
    private int leituraAtual;
    private double tarifaPorKwh;

    // --- Getters ---

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getLeituraAnterior() {
        return leituraAnterior;
    }

    public int getLeituraAtual() {
        return leituraAtual;
    }

    public double getTarifaPorKwh() {
        return tarifaPorKwh;
    }

    // Getter calculado
    public int getConsumo() {
        return leituraAtual - leituraAnterior;
    }

    // Getter calculado
    public double getValorConta() {
        return getConsumo() * tarifaPorKwh;
    }

    // --- Setters com validação ---

    public void setNomeCliente(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome do cliente não pode ser vazio.");
            return;
        }
        this.nomeCliente = nome;
    }

    public void setLeituraAnterior(int leitura) {
        if (leitura < 0) {
            System.out.println("Erro: leitura não pode ser negativa.");
            return;
        }
        this.leituraAnterior = leitura;
    }

    public void setLeituraAtual(int leitura) {
        if (leitura < 0) {
            System.out.println("Erro: leitura não pode ser negativa.");
            return;
        }
        if (leitura < leituraAnterior) {
            System.out.println("Erro: leitura atual não pode ser menor que a anterior.");
            return;
        }
        this.leituraAtual = leitura;
    }

    public void setTarifaPorKwh(double tarifa) {
        if (tarifa <= 0) {
            System.out.println("Erro: tarifa deve ser positiva.");
            return;
        }
        this.tarifaPorKwh = tarifa;
    }

    // --- Método de exibição ---

    public void exibirFatura() {
        System.out.println("=== Fatura de Energia ===");
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Leitura anterior: " + leituraAnterior + " kWh");
        System.out.println("Leitura atual:    " + leituraAtual + " kWh");
        System.out.println("Consumo:          " + getConsumo() + " kWh");
        System.out.println("Tarifa:           R$ " + tarifaPorKwh + " por kWh");
        System.out.println("TOTAL:            R$ " + getValorConta());
        System.out.println("=========================");
    }
}
```

### Passo 2 — `ContaEnergiaMain.java`:

```java
public class ContaEnergiaMain {

    public static void main(String[] args) {

        ContaEnergia conta = new ContaEnergia();
        conta.setNomeCliente("Maria Oliveira");
        conta.setLeituraAnterior(1200);
        conta.setLeituraAtual(1450);
        conta.setTarifaPorKwh(0.75);

        conta.exibirFatura();

        System.out.println();

        // Tentativa de leitura atual menor que anterior
        conta.setLeituraAtual(1100);

        // Tentativa de tarifa negativa
        conta.setTarifaPorKwh(-0.50);
    }
}
```

**Saída esperada:**

```
=== Fatura de Energia ===
Cliente: Maria Oliveira
Leitura anterior: 1200 kWh
Leitura atual:    1450 kWh
Consumo:          250 kWh
Tarifa:           R$ 0.75 por kWh
TOTAL:            R$ 187.5
=========================

Erro: leitura atual não pode ser menor que a anterior.
Erro: tarifa deve ser positiva.
```

---

## Exercício Autônomo 1 — Classe Estacionamento ⭐⭐

Crie uma classe `Estacionamento` com controle de vagas.

**Atributos** (todos `private`):
- `nome` (String)
- `totalVagas` (int) — definido uma vez, somente leitura depois
- `vagasOcupadas` (int) — começa em 0, sem setter direto

**Getters:** para todos + getter calculado `getVagasDisponiveis()`

**Setters:**
- `setNome(String nome)` → não aceitar vazio/null
- `setTotalVagas(int total)` → só aceitar se ainda não foi definido (== 0) e se total > 0

**Métodos de negócio:**
- `entrarVeiculo()` → incrementa vagas ocupadas se houver vaga disponível; senão, avisa "Estacionamento lotado"
- `sairVeiculo()` → decrementa vagas ocupadas se houver veículo; senão, avisa "Estacionamento vazio"
- `exibirStatus()` → imprime nome, vagas totais, ocupadas e disponíveis

**Teste:** crie um estacionamento com 3 vagas. Entre 3 veículos. Tente entrar um 4º. Saia 1. Exiba status.

---

## Exercício Autônomo 2 — Classe ContaPoupanca ⭐⭐

Crie uma classe `ContaPoupanca` com rendimento mensal.

**Atributos** (todos `private`):
- `titular` (String)
- `saldo` (double) — sem setter direto
- `taxaRendimentoMensal` (double) — exemplo: 0.005 para 0,5% ao mês

**Getters:** para todos os atributos.

**Setters:**
- `setTitular(String titular)` → não aceitar vazio/null
- `setTaxaRendimentoMensal(double taxa)` → aceitar somente entre 0 e 1 (0% a 100%)

**Métodos de negócio:**
- `depositar(double valor)` → adiciona ao saldo (valor > 0)
- `sacar(double valor)` → retira do saldo (não pode ficar negativo)
- `aplicarRendimento()` → calcula `saldo * taxaRendimentoMensal` e adiciona ao saldo
- `exibirExtrato()` → imprime titular, saldo e taxa

**Teste:** crie uma conta com saldo de R$ 1000 e taxa de 0.005. Aplique rendimento 3 vezes. Exiba o extrato a cada mês.

---

## Exercícios de consolidação — pasta de exercícios

Os exercícios autônomos deste bloco também estão organizados na pasta [`exercicios/`](./exercicios/README-Bl4Ex.md) com enunciados detalhados e gabarito.

---

## Resumo do Bloco 4

- Boas práticas: atributos `private`, métodos `public`, setters com validação
- Preferir métodos de negócio (como `depositar()`) a setters genéricos (como `setSaldo()`)
- Expor apenas o necessário — se ninguém precisa de um getter ou setter, não crie
- O encapsulamento centraliza as regras em um único lugar, facilitando manutenção e evolução

---

## Resumo geral da Aula 04

| Conceito | Em uma frase |
|---|---|
| `private` | Atributo/método acessível somente dentro da própria classe |
| `public` | Atributo/método acessível de qualquer lugar |
| `protected` | Acessível pela classe e suas subclasses (Aula 07) |
| Getter | Método público que retorna o valor de um atributo privado |
| Setter | Método público que altera o valor de um atributo privado, com validação |
| Getter calculado | Retorna um valor derivado de outros atributos, sem armazenar |
| `this` | Referência ao objeto atual — diferencia atributo de parâmetro |
| Encapsulamento | Proteger o estado interno do objeto, expondo apenas o necessário |

---

**Próxima aula →** Aula 05 — Construtores e Sobrecarga: como inicializar objetos de forma mais elegante do que chamar vários setters.
