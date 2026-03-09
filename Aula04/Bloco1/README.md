# Bloco 1 — O Problema do Acesso Direto e Modificadores de Acesso

## Objetivos do Bloco

- Perceber o perigo de deixar atributos acessíveis a qualquer código externo
- Conhecer os modificadores de acesso `private`, `public` e `protected`
- Aplicar `private` em atributos para impedir acesso direto

---

## 1.1 O que acontece quando os atributos ficam abertos?

Na Aula 03, criamos a classe `ContaBancaria` com atributos sem modificador de acesso. Isso significa que qualquer classe pode fazer coisas como:

```java
ContaBancaria conta = new ContaBancaria();
conta.saldo = -500.0;   // saldo negativo? Ninguém impediu.
conta.titular = "";      // titular vazio? Sem validação.
```

O método `sacar()` tinha regras (verificar saldo suficiente), mas se alguém altera `saldo` diretamente, **as regras são ignoradas**. Isso quebra a integridade do objeto.

> **O problema:** se o estado interno pode ser alterado por fora sem passar pelos métodos, não há garantia de que o objeto está em um estado válido.

---

## 1.2 Modificadores de acesso — em resumo

Java oferece três modificadores principais para controlar quem pode acessar atributos e métodos:

| Modificador | Quem pode acessar |
|---|---|
| `public` | Qualquer classe, em qualquer lugar |
| `private` | Somente a própria classe |
| `protected` | A própria classe + subclasses (veremos na Aula 07) |

**Regra prática para esta aula:**
- Atributos → `private` (sempre)
- Métodos que o mundo externo precisa usar → `public`
- `protected` → veremos em Herança (Aula 07), por agora apenas saiba que existe

---

## 1.3 Aplicando `private` — antes e depois

**Antes (Aula 03) — atributos abertos:**

```java
public class Produto {
    String nome;
    double preco;
    int quantidadeEstoque;
}
```

**Depois (Aula 04) — atributos protegidos:**

```java
public class Produto {
    private String nome;
    private double preco;
    private int quantidadeEstoque;
}
```

Se agora alguém tentar `produto.preco = -10.0;` de fora da classe, o compilador **não deixa**:

```
error: preco has private access in Produto
```

Esse é o primeiro passo do encapsulamento: **trancar a porta**.

---

## Exercício Guiado 1 — Protegendo a classe Pessoa (professor + alunos)

Vamos reescrever a classe `Pessoa` da Aula 03, agora com `private`.

### Passo 1 — Crie `Pessoa.java`:

```java
public class Pessoa {

    private String nome;
    private int idade;
    private String cpf;

    public void apresentar() {
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
        System.out.println("CPF: " + cpf);
    }
}
```

### Passo 2 — Crie `PessoaMain.java` e tente acessar diretamente:

```java
public class PessoaMain {

    public static void main(String[] args) {

        Pessoa p1 = new Pessoa();
        p1.nome = "Ana";       // ERRO de compilação!
        p1.idade = 25;         // ERRO de compilação!
        p1.apresentar();
    }
}
```

### Passo 3 — Compile e observe o erro:

```bash
javac Pessoa.java PessoaMain.java
```

```
error: nome has private access in Pessoa
        p1.nome = "Ana";
          ^
error: idade has private access in Pessoa
        p1.idade = 25;
          ^
```

**Discussão em sala:** O compilador está protegendo o objeto. Mas se não podemos mais acessar os atributos por fora... como definir o nome e a idade? → Essa é a pergunta que o Bloco 2 responde com getters e setters.

---

## Exercício Guiado 2 — Protegendo a ContaBancaria (professor + alunos)

Agora vamos proteger a `ContaBancaria` e ver como o acesso direto ao saldo deixa de funcionar.

### Passo 1 — Crie `ContaBancaria.java` com `private`:

```java
public class ContaBancaria {

    private String titular;
    private String numero;
    private double saldo;

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

### Passo 2 — Crie `BancoMain.java` e tente o acesso direto:

```java
public class BancoMain {

    public static void main(String[] args) {

        ContaBancaria c1 = new ContaBancaria();
        c1.titular = "Ana";     // ERRO!
        c1.saldo = 1000.0;      // ERRO!
        c1.saldo = -999.0;      // Isso era possível antes — agora não é mais

        c1.depositar(500.0);    // OK — método público
        c1.exibirSaldo();       // OK — método público
    }
}
```

### Passo 3 — Compile e discuta:

Os métodos `depositar()` e `sacar()` continuam funcionando porque são `public` e estão dentro da própria classe — têm acesso ao `saldo` privado. O `main` externo não tem mais acesso direto.

**Observação:** perceba que agora não temos como definir o titular e o número da conta. Isso será resolvido no Bloco 2.

---

## Exercício Autônomo 1 — Protegendo a classe Aluno ⭐

Pegue a classe `Aluno` da Aula 03 e aplique `private` em todos os atributos. Adicione `public` nos métodos.

**Classe original (Aula 03):**

```java
public class Aluno {
    String nome;
    String matricula;
    double nota1;
    double nota2;

    double calcularMedia() { ... }
    void exibirSituacao() { ... }
}
```

**Sua tarefa:**
1. Coloque `private` em todos os atributos
2. Coloque `public` nos métodos `calcularMedia()` e `exibirSituacao()`
3. Tente compilar um `AlunoMain` que acessa `a1.nome = "Maria"` diretamente
4. Confirme que o erro aparece

---

## Exercício Autônomo 2 — Protegendo a classe Livro ⭐

Crie uma classe `Livro` com:

- Atributos (todos `private`): `titulo` (String), `autor` (String), `preco` (double)
- Método `public void exibirDetalhes()` → imprime título, autor e preço

Crie uma classe `LivroMain` e tente:
1. Criar um objeto `Livro`
2. Atribuir `livro.titulo = "Java"` diretamente → deve dar erro
3. Chamar `livro.exibirDetalhes()` → deve funcionar (mesmo com atributos vazios/zerados)

**Pergunta para reflexão:** o que é impresso quando chamamos `exibirDetalhes()` sem ter definido os atributos? (Dica: valores padrão em Java — `null` para String, `0.0` para double.)

---

## Resumo do Bloco 1

- Atributos sem modificador de acesso podem ser manipulados por qualquer classe — isso é perigoso
- `private` impede o acesso externo: só a própria classe enxerga o atributo
- `public` permite acesso de qualquer lugar — usado nos métodos que o mundo externo precisa
- `protected` será visto na Aula 07 (Herança)
- Aplicar `private` nos atributos é o **primeiro passo** do encapsulamento

---

**Próximo passo →** [Bloco 2](../Bloco2/README.md): agora que trancamos a porta, vamos criar a chave — getters e setters.
