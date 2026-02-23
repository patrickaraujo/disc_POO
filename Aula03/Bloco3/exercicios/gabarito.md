# Gabarito — Exercícios Bloco 3 (Aula 03)

> Leia o gabarito apenas depois de tentar resolver por conta própria.
> O aprendizado acontece no erro — não no acerto imediato.

---

## Exercício 01 — Classe Aluno

### Aluno.java

```java
public class Aluno {

    String nome;
    String matricula;
    double nota1;
    double nota2;

    double calcularMedia() {
        return (nota1 + nota2) / 2;
    }

    void exibirSituacao() {
        double media = calcularMedia();
        String situacao = (media >= 6.0) ? "Aprovado" : "Reprovado";
        System.out.println("Aluno: " + nome + " | Média: " + media + " | " + situacao);
    }
}
```

### AlunoMain.java

```java
public class AlunoMain {

    public static void main(String[] args) {

        Aluno a1 = new Aluno();
        a1.nome = "Maria";
        a1.matricula = "2024001";
        a1.nota1 = 8.0;
        a1.nota2 = 7.0;

        Aluno a2 = new Aluno();
        a2.nome = "João";
        a2.matricula = "2024002";
        a2.nota1 = 3.0;
        a2.nota2 = 5.0;

        Aluno a3 = new Aluno();
        a3.nome = "Paula";
        a3.matricula = "2024003";
        a3.nota1 = 6.0;
        a3.nota2 = 6.0;

        a1.exibirSituacao();
        a2.exibirSituacao();
        a3.exibirSituacao();
    }
}
```

**Saída:**
```
Aluno: Maria | Média: 7.5 | Aprovado
Aluno: João  | Média: 4.0 | Reprovado
Aluno: Paula | Média: 6.0 | Aprovado
```

**Pontos de atenção:**

- `calcularMedia()` **retorna** um `double` — é o primeiro método com `return` que vemos neste curso
- `exibirSituacao()` chama `calcularMedia()` internamente — um método pode chamar outro da mesma classe
- O operador ternário `(media >= 6.0) ? "Aprovado" : "Reprovado"` é um `if` simplificado. Se preferir, use a versão completa com `if/else` — o resultado é o mesmo

---

## Exercício 02 — Classe Produto

### Produto.java

```java
public class Produto {

    String nome;
    double preco;
    int quantidadeEstoque;

    void exibirInformacoes() {
        System.out.println("Produto:  " + nome);
        System.out.println("Preço:    R$ " + preco);
        System.out.println("Estoque:  " + quantidadeEstoque + " unidades");
        System.out.println("----------");
    }

    void aplicarDesconto(double percentual) {
        if (percentual <= 0 || percentual >= 100) {
            System.out.println("Percentual inválido: " + percentual);
            return;
        }
        double desconto = preco * percentual / 100;
        preco = preco - desconto;
        System.out.println("Desconto de " + percentual + "% aplicado em " + nome + ".");
    }

    void reporEstoque(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida para reposição.");
            return;
        }
        quantidadeEstoque = quantidadeEstoque + quantidade;
        System.out.println("Estoque de " + nome + " reposto em " + quantidade + " unidades.");
    }
}
```

### ProdutoMain.java

```java
public class ProdutoMain {

    public static void main(String[] args) {

        Produto p1 = new Produto();
        p1.nome = "Notebook";
        p1.preco = 3500.0;
        p1.quantidadeEstoque = 10;

        Produto p2 = new Produto();
        p2.nome = "Mouse";
        p2.preco = 120.0;
        p2.quantidadeEstoque = 2;

        System.out.println("=== Antes ===");
        p1.exibirInformacoes();
        p2.exibirInformacoes();

        p1.aplicarDesconto(10.0);   // 10% de desconto no Notebook
        p2.reporEstoque(8);          // repõe 8 unidades do Mouse
        p1.aplicarDesconto(-5.0);   // deve exibir erro

        System.out.println();
        System.out.println("=== Depois ===");
        p1.exibirInformacoes();
        p2.exibirInformacoes();
    }
}
```

**Pontos de atenção:**

- O `return;` em `aplicarDesconto()` interrompe o método quando o percentual é inválido — isso evita executar o cálculo com dados ruins
- A alteração no preço de `p1` **não afeta `p2`** — estados independentes em ação

---

## Exercício 03 — Agência Bancária

### AgenciaMain.java

```java
public class AgenciaMain {

    public static void main(String[] args) {

        ContaBancaria c1 = new ContaBancaria();
        c1.titular = "Ana";
        c1.numero = "001-1";
        c1.saldo = 2000.0;

        ContaBancaria c2 = new ContaBancaria();
        c2.titular = "Bruno";
        c2.numero = "002-2";
        c2.saldo = 800.0;

        ContaBancaria c3 = new ContaBancaria();
        c3.titular = "Carla";
        c3.numero = "003-3";
        c3.saldo = 0.0;

        System.out.println("=== Operações ===");

        // 1. Ana realiza um depósito
        c1.depositar(500.0);

        // 2. Ana transfere R$ 300 para Bruno (saque + depósito)
        System.out.println("-- Transferência de Ana para Bruno --");
        c1.sacar(300.0);
        c2.depositar(300.0);

        // 3. Carla tenta sacar (saldo zero)
        c3.sacar(100.0);

        // 4. Carla recebe depósito
        c3.depositar(250.0);

        // Saldo final
        System.out.println();
        System.out.println("=== Saldo Final ===");
        c1.exibirSaldo();
        System.out.println();
        c2.exibirSaldo();
        System.out.println();
        c3.exibirSaldo();
    }
}
```

**Saldo final esperado:**

- Ana: R$ 2200.0 (2000 + 500 - 300)
- Bruno: R$ 1100.0 (800 + 300)
- Carla: R$ 250.0 (0 + 250)

**Desafio extra — método transferir:**

```java
// Adicionar dentro da classe ContaBancaria:
void transferir(ContaBancaria destino, double valor) {
    if (valor <= 0) {
        System.out.println("Valor inválido para transferência.");
        return;
    }
    if (valor > saldo) {
        System.out.println("Saldo insuficiente para transferência.");
        return;
    }
    this.sacar(valor);
    destino.depositar(valor);
    System.out.println("Transferência de R$ " + valor + " concluída.");
}
```

> O `this` aqui é opcional por enquanto, mas deixa explícito que `sacar` pertence ao objeto atual. Isso será aprofundado na Aula 05.

---

## Exercício 04 — Modelagem Livre

Este exercício não tem gabarito único — a resposta depende do contexto escolhido. A seguir, um exemplo para o contexto **Academia**.

### Veiculo.java (exemplo com contexto Estacionamento)

```java
public class Veiculo {

    String placa;
    String modelo;
    boolean estacionado;
    int horasEstacionado;

    void entrar() {
        if (estacionado) {
            System.out.println("Veículo " + placa + " já está estacionado.");
            return;
        }
        estacionado = true;
        horasEstacionado = 0;
        System.out.println("Veículo " + placa + " (" + modelo + ") entrou no estacionamento.");
    }

    void sair(int horas) {
        if (!estacionado) {
            System.out.println("Veículo " + placa + " não está estacionado.");
            return;
        }
        horasEstacionado = horas;
        double valor = calcularValor();
        estacionado = false;
        System.out.println("Veículo " + placa + " saiu. Horas: " + horas + " | Total: R$ " + valor);
    }

    double calcularValor() {
        // R$ 5,00 a primeira hora + R$ 3,00 por hora adicional
        if (horasEstacionado <= 1) {
            return 5.0;
        }
        return 5.0 + (horasEstacionado - 1) * 3.0;
    }

    void exibirStatus() {
        String status = estacionado ? "Estacionado" : "Fora do estacionamento";
        System.out.println(placa + " | " + modelo + " | " + status);
    }
}
```

**Critérios que esta solução atende:**
- Métodos operam sobre os atributos da própria classe ✔
- Há validações (`if (estacionado)`, `if (!estacionado)`) ✔
- Dois objetos demonstram independência de estado quando testados em `VeiculoMain` ✔

---

## O que revisar se tiver dificuldade

| Dificuldade | Onde revisar |
|---|---|
| Não sei criar a classe | Bloco 2 — seções 2.1 a 2.3 |
| Erro ao compilar (arquivo/classe) | Bloco 2 — seção 2.8 (erros comuns) |
| Não entendi o `return` | Exercício 01 — ponto de atenção |
| Não entendi estados independentes | Bloco 3 — seção 3.3 e exercício 02 |
| Não sei modelar atributos/métodos | Bloco 1 — atividade de modelagem |
