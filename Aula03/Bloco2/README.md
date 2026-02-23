# Bloco 2 — Primeira Classe em Java

## Objetivos do Bloco

- Escrever a estrutura básica de uma classe Java
- Declarar atributos e métodos dentro de uma classe
- Entender a diferença entre a classe de domínio e a classe com o `main`
- Criar objetos com `new` e acessar seus atributos e métodos com o operador `.`
- Perceber o que acontece quando dois objetos são criados a partir da mesma classe

---

## 2.1 Estrutura de uma classe Java

Em Java, uma classe é declarada com a palavra-chave `class`. Dentro das chaves, ficam os atributos e os métodos.

```java
public class NomeDaClasse {

    // Atributos (estado)
    TipoDado nomeDoAtributo;

    // Métodos (comportamento)
    TipoDeRetorno nomeDoMetodo() {
        // corpo do método
    }
}
```

**Regras importantes:**

- O nome da classe começa com **letra maiúscula** (convenção PascalCase: `Pessoa`, `ContaBancaria`, `Produto`)
- O **nome do arquivo `.java`** deve ser idêntico ao nome da classe pública
- Atributos e métodos ficam **dentro das chaves** da classe
- Por enquanto, os atributos ficam sem modificador de acesso — isso será corrigido na Aula 04

---

## 2.2 Dois tipos de arquivo que você verá nesta aula

A partir de agora, seus projetos terão dois tipos de arquivo Java com papéis diferentes:

| Arquivo | Papel | Tem `main`? |
|---|---|---|
| `Pessoa.java` | Classe de domínio — define o molde | Não |
| `PessoaMain.java` (ou `App.java`) | Classe de teste/execução — usa o molde | Sim |

Essa separação é importante: a classe `Pessoa` descreve o que é uma pessoa. A classe `PessoaMain` é o programa que *usa* pessoas. Misturar os dois dentro de um único arquivo funciona, mas vai contra a organização que aprenderemos ao longo do curso.

---

## 2.3 Criando a classe Pessoa

Vamos criar nossa primeira classe de domínio. Abra seu editor e crie o arquivo `Pessoa.java`:

```java
public class Pessoa {

    // Atributos — representam o estado de cada objeto Pessoa
    String nome;
    int idade;
    String cpf;

    // Método — representa um comportamento do objeto
    void apresentar() {
        System.out.println("Olá! Meu nome é " + nome);
        System.out.println("Tenho " + idade + " anos.");
    }

    void fazerAniversario() {
        idade = idade + 1;
        System.out.println(nome + " fez aniversário! Agora tem " + idade + " anos.");
    }
}
```

Observe que dentro do método `apresentar()`, usamos `nome` e `idade` diretamente — sem precisar passar por parâmetro. Isso é possível porque os atributos pertencem ao objeto, e o método tem acesso automático a eles.

---

## 2.4 A palavra-chave `new`

Para criar um objeto a partir de uma classe, usamos `new` seguido do nome da classe e parênteses:

```java
Pessoa p1 = new Pessoa();
```

O que acontece nessa linha:

1. `Pessoa p1` → declara uma variável do tipo `Pessoa` chamada `p1`
2. `new Pessoa()` → cria um novo objeto na memória
3. `=` → faz `p1` apontar para esse objeto

> **Atenção:** `p1` não *é* o objeto — ela é uma **referência** que aponta para ele. Esse conceito será aprofundado ao longo do curso.

---

## 2.5 O operador ponto `.`

Para acessar os atributos ou chamar os métodos de um objeto, usamos o operador ponto (`.`):

```java
p1.nome = "Ana";       // acessa e define o atributo nome do objeto p1
p1.apresentar();       // chama o método apresentar() do objeto p1
```

Leia o operador `.` como: *"no objeto `p1`, acesse..."*

---

## 2.6 Programa completo com a classe Pessoa

Agora crie o arquivo `PessoaMain.java` na mesma pasta:

```java
public class PessoaMain {

    public static void main(String[] args) {

        // Criando o primeiro objeto
        Pessoa p1 = new Pessoa();
        p1.nome = "Ana";
        p1.idade = 29;
        p1.cpf = "111.222.333-44";

        // Criando o segundo objeto
        Pessoa p2 = new Pessoa();
        p2.nome = "Carlos";
        p2.idade = 35;
        p2.cpf = "555.666.777-88";

        // Usando os objetos
        p1.apresentar();
        p1.fazerAniversario();

        System.out.println("---");

        p2.apresentar();
    }
}
```

**Saída esperada:**

```
Olá! Meu nome é Ana
Tenho 29 anos.
Ana fez aniversário! Agora tem 30 anos.
---
Olá! Meu nome é Carlos
Tenho 35 anos.
```

**Para compilar e executar (linha de comando):**

```bash
javac Pessoa.java PessoaMain.java
java PessoaMain
```

---

## 2.7 O que acabou de acontecer?

Leia o código que você escreveu e responda mentalmente:

- `p1` e `p2` são dois **objetos diferentes** criados a partir do mesmo molde (`Pessoa`)
- Cada um tem sua **própria cópia** dos atributos `nome`, `idade` e `cpf`
- Chamar `p1.fazerAniversario()` alterou a `idade` de `p1` — mas **não afetou `p2`**
- Os métodos `apresentar()` e `fazerAniversario()` pertencem à classe, mas operam sobre **o objeto que os chamou**

Isso é independência de estado entre instâncias — um dos pilares da OO.

---

## 2.8 Erros comuns nesta etapa

| Erro | Causa | Como resolver |
|---|---|---|
| `cannot find symbol: Pessoa` | Arquivo `Pessoa.java` não foi compilado junto | Compile os dois: `javac Pessoa.java PessoaMain.java` |
| `NullPointerException` | Tentou usar um objeto que não foi criado com `new` | Verifique se a linha `new Pessoa()` está antes do uso |
| Nome do arquivo ≠ nome da classe | `Pessoa.java` com a classe chamada `pessoa` dentro | Nome do arquivo e da classe **devem ser idênticos** (maiúsculas importam) |
| `non-static variable cannot be referenced from a static context` | Tentou usar atributo da classe diretamente no `main` sem criar um objeto | Crie o objeto com `new` primeiro |

---

## Resumo do Bloco 2

- Uma classe Java é declarada com `class`; atributos e métodos ficam dentro das chaves
- Separe a classe de domínio (ex.: `Pessoa.java`) da classe com o `main` (ex.: `PessoaMain.java`)
- A palavra-chave `new` cria um objeto na memória a partir do molde da classe
- O operador `.` acessa atributos e chama métodos de um objeto
- Dois objetos da mesma classe têm estados independentes: alterar um não afeta o outro

---

**Próximo passo →** [Bloco 3](../Bloco3/README.md): vamos aplicar tudo isso em um exemplo mais rico — a `ContaBancaria` — com múltiplos objetos e exercícios práticos.
