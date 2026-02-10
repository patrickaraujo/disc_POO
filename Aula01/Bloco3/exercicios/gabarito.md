# 📘 Gabarito de Exercícios — Java (Introdução)

Este material contém o **gabarito completo dos exercícios práticos de Java**, com códigos prontos para execução, exemplos de saída e observações importantes para iniciantes.

---

## 🧪 Exercício 1 — Alterando mensagens no console

```java
public class Exercicio1 {
    public static void main(String[] args) {
        System.out.println("Maria Silva");
        System.out.println("Ciência da Computação");
        System.out.println("Universidade de São Paulo");
        System.out.println("Aprender programação é abrir portas para o futuro!");
    }
}
```

> ✅ Substitua os dados pelos seus próprios antes de executar.

---

## 🧪 Exercício 2 — Ordem de execução importa

```java
public class Exercicio2 {
    public static void main(String[] args) {
        System.out.println("Início do programa");
        System.out.println("Executando...");
        System.out.println("Final do programa");
    }
}
```

> ⚠️ A ordem dos `println` define a sequência de saída — essencial em lógica de programação.

---

## 🧪 Exercício 3 — Mensagem em múltiplas linhas

```java
public class Exercicio3 {
    public static void main(String[] args) {
        System.out.println("O mar é vasto e misterioso,");
        System.out.println("suas águas guardam segredos milenares.");
        System.out.println("Nas profundezas, vida pulsa em formas únicas,");
        System.out.println("enquanto as ondas dançam sob o sol.");
        System.out.println("Respeitar o oceano é respeitar a própria vida.");
    }
}
```

> 💡 Cada `println` gera uma nova linha automaticamente.

---

## 🧪 Exercício 4 — Explorando erros (de propósito)

### Passo 1: Código com erro proposital (falta ponto e vírgula)

```java
public class Exercicio4 {
    public static void main(String[] args) {
        System.out.println("Teste de erro")
        System.out.println("Esta linha nem será analisada");
    }
}
```

### Passo 2: Erro esperado ao compilar

```
Exercicio4.java:4: error: ';' expected
        System.out.println("Teste de erro")
                                          ^
1 error
```

### Passo 3: Código corrigido

```java
public class Exercicio4 {
    public static void main(String[] args) {
        System.out.println("Teste de erro");
        System.out.println("Agora compilou corretamente!");
    }
}
```

> 🔍 Lição: aprender a ler mensagens do compilador é fundamental.

---

## 🧪 Exercício 5 — Criando seu próprio "Olá Mundo"

```java
// Arquivo: MeuOlaMundo.java
public class MeuOlaMundo {
    public static void main(String[] args) {
        System.out.println("🌟 Olá, Mundo Java! 🌟");
        System.out.println("Este é meu primeiro programa do zero!");
    }
}
```

### Como executar:

```bash
javac MeuOlaMundo.java
java MeuOlaMundo
```

---

## 💡 Dicas finais (Parte 1)

1. O nome do arquivo `.java` deve ser igual ao nome da classe pública.
2. Java diferencia maiúsculas e minúsculas.
3. Todo programa Java precisa do método `main`.
4. `print` não quebra linha, `println` quebra.

---

# 📘 Parte 2 — Entrada de Dados com Scanner

## 📌 Pré-requisito

```java
import java.util.Scanner;
```

---

## 🧪 Exercício 6 — Lendo e imprimindo um número inteiro

```java
import java.util.Scanner;

public class Exercicio6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite um número inteiro: ");
        int numero = scanner.nextInt();

        System.out.println("Número lido: " + numero);

        scanner.close();
    }
}
```

---

## 🧪 Exercício 7 — Trabalhando com tipos básicos

```java
import java.util.Scanner;

public class Exercicio7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite um número float: ");
        float valorFloat = scanner.nextFloat();

        scanner.nextLine();

        System.out.print("Digite um caractere: ");
        char caractere = scanner.nextLine().charAt(0);

        int valorInteiro = (int) caractere;

        System.out.println("Caractere lido: " + caractere);
        System.out.println("Valor inteiro correspondente: " + valorInteiro);

        scanner.close();
    }
}
```

---

## 🧪 Exercício 8 — Leitura múltipla e ordem inversa

```java
import java.util.Scanner;

public class Exercicio8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o primeiro inteiro: ");
        int inteiro1 = scanner.nextInt();

        System.out.print("Digite o segundo inteiro: ");
        int inteiro2 = scanner.nextInt();

        System.out.print("Digite o primeiro float: ");
        float float1 = scanner.nextFloat();

        System.out.print("Digite o segundo float: ");
        float float2 = scanner.nextFloat();

        System.out.println("\nValores na ordem inversa:");
        System.out.println("Float 2: " + float2);
        System.out.println("Float 1: " + float1);
        System.out.println("Inteiro 2: " + inteiro2);
        System.out.println("Inteiro 1: " + inteiro1);

        scanner.close();
    }
}
```

---

🚀 Bons estudos e boa prática com Java!
