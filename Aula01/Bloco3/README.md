# 📘 Bloco 3 — Primeiro Programa Java + Mão na Massa

> **Duração estimada:** 50 minutos  
> **Objetivo:** Criar, compilar e executar o primeiro programa Java, entendendo sua estrutura mínima

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Entender a **estrutura mínima** de um programa Java
- Reconhecer o papel da **classe** e do método `main`
- Criar um arquivo `.java` corretamente
- Compilar e executar um programa Java pelo terminal
- Utilizar comandos básicos de saída no console

---

## 💡 Conceitos Fundamentais

### Estrutura básica de um programa Java

Todo programa Java precisa, no mínimo, de:

- Uma **classe**
- Um método chamado **`main`**
- Instruções a serem executadas

📌 **Ideia central:**  
Todo programa Java começa sua execução pelo método `main`.

---

### Exemplo de programa mínimo

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Olá, mundo!");
    }
}
```

⚠️ **Importante:**  
Neste momento, **não se preocupe** com:
- `public`
- `static`
- `String[] args`

Esses conceitos serão explicados ao longo do curso.

---

## 🧩 Entendendo cada parte (visão geral)

- `class Main`  
  Define uma classe chamada `Main`

- `main`  
  Ponto de entrada do programa

- `System.out.println`  
  Comando para imprimir mensagens no console

📌 **Objetivo aqui:** reconhecer papéis, não memorizar detalhes.

---

## 📁 Arquivo e nome da classe

Em Java:

- O nome do arquivo deve ser **igual** ao nome da classe
- O arquivo deve ter a extensão `.java`
- Java diferencia letras maiúsculas e minúsculas

📌 Exemplo correto:
- Arquivo: `Main.java`
- Classe: `Main`

---

## 🛠️ Compilando e executando um programa Java

### Passo 1 — Criar o arquivo

Crie um arquivo chamado `Main.java` e insira o código do exemplo.

---

### Passo 2 — Compilar

No terminal, execute:

```
javac Main.java
```

✔️ Se não houver erros, será gerado um arquivo `Main.class`.

---

### Passo 3 — Executar

Execute o programa com:

```
java Main
```

✔️ A mensagem será exibida no console.

---

## 🧪 Erros comuns (e esperados)

Alguns erros normais neste momento:

- Nome do arquivo diferente do nome da classe
- Erros de digitação
- Esquecer de compilar antes de executar

💡 **Importante:**  
Errar faz parte do processo de aprendizagem.

---

## ✏️ Atividade Prática

### 📝 Atividade — Modificando o programa

Faça as seguintes modificações:

1. Altere a mensagem exibida no console
2. Imprima:
   - Seu nome
   - Seu curso
   - Uma frase livre

Exemplo:

```java
System.out.println("Meu nome é João");
System.out.println("Curso: Ciência da Computação");
System.out.println("Estou aprendendo Java!");
```

📌 **Objetivo:** ganhar confiança ao escrever e executar código Java.

---

## ✅ Resumo do Bloco 3

Neste bloco você aprendeu:

- A estrutura mínima de um programa Java
- O papel da classe e do método `main`
- Como criar, compilar e executar um programa Java
- Como usar comandos simples de saída no console

---

## ➡️ Próximos Passos

Nos próximos blocos você vai aprender:

- Variáveis e tipos de dados em Java
- Estruturas de decisão e repetição
- Primeiros passos reais em lógica usando Java

---

## 📚 Observações Importantes

🚫 **Neste bloco NÃO aprofundamos:**
- Orientação a Objetos formal
- Estruturas de controle (`if`, `for`, `while`)
- Detalhes avançados da linguagem

✅ **O foco agora está em:**
- Entender a estrutura básica do Java
- Perder o medo de escrever código
- Preparar o terreno para a lógica de programação

> 💭 *“Todo programador começa com um ‘Olá, mundo!’.”*
