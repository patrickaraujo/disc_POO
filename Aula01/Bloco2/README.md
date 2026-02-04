# 📘 Bloco 2 — Java como Plataforma e Ambiente

> **Duração estimada:** 50 minutos

> **Objetivo:** Entender o que é Java além da linguagem e como um programa Java é executado

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Entender Java como **plataforma**, não apenas como linguagem
- Compreender o conceito de **independência de plataforma**
- Diferenciar **JVM**, **JRE** e **JDK**
- Entender como um programa Java é compilado e executado
- Reconhecer o papel do ambiente de desenvolvimento Java

---

## 💡 Conceitos Fundamentais

### O que é Java?

Java **não é apenas uma linguagem de programação**.

Java é:
- Uma linguagem **+**
- Uma plataforma de execução

💡 **Ideia central:**  
Programas Java **não são executados diretamente** pelo sistema operacional.

---

### Independência de plataforma

Em muitas linguagens:
- O código é compilado diretamente para o sistema operacional

Em Java:
- O código é compilado para uma **linguagem intermediária**
- Essa linguagem é executada por uma **máquina virtual**

📌 **Slogan clássico do Java:**  
 - *Write Once, Run Anywhere (WORA)*
 - Tradução: "Escreva uma vez, execute em qualquer lugar."

---

## 🧠 A Máquina Virtual Java (JVM)

### O que é a JVM?

A **JVM (Java Virtual Machine)** é um software que:

- Executa programas Java
- Funciona sobre o sistema operacional
- Garante comportamento padronizado da execução

📌 **Função principal da JVM:**  
Executar o mesmo programa Java em diferentes plataformas.

---

### Por que a JVM é importante?

- Permite portabilidade
- Ajuda na segurança da execução
- Controla o uso de memória
- Padroniza o ambiente de execução

💡 **Conexão conceitual:**  
Assim como a Orientação a Objetos organiza o código, a JVM organiza a execução.

---

## 🧩 [JDK, JRE e JVM — quem é quem?](https://share.google/K7AlP3wgC9lXtpkC8)

### JDK — Java Development Kit

- Conjunto de ferramentas para **desenvolver** programas Java
- Inclui:
  - Compilador (`javac`)
  - JVM
  - Bibliotecas padrão
  - Outras ferramentas

👉 Usado por quem **programa** em Java.

---

### JRE — Java Runtime Environment

- Ambiente necessário para **executar** programas Java
- Inclui:
  - JVM
  - Bibliotecas padrão

👉 Usado por quem **executa** programas Java.

---

### JVM — Java Virtual Machine

- Executa o código Java compilado
- Interpreta e executa o bytecode
- É parte essencial da plataforma Java

📌 **Resumo rápido:**
- JDK → desenvolver
- JRE → executar
- JVM → executar de fato

---

## 🔄 Como um programa Java é executado?

### Fluxo de execução

1. O programador escreve o código-fonte (`.java`)
2. O compilador Java (`javac`) gera o bytecode (`.class`)
3. A JVM carrega o bytecode na memória
4. A JVM executa o programa

```
.java → javac → .class → JVM → execução
```

💡 **Importante:**  
O mesmo arquivo `.class` pode ser executado em diferentes sistemas operacionais.

---

## 🖥️ Ambiente de Desenvolvimento Java

### O que compõe o ambiente?

Um ambiente Java básico envolve:

- Editor de código
- Compilador Java
- Máquina Virtual Java
- Bibliotecas padrão

📌 **Neste curso:**
- Vamos começar sem IDE
- O foco é entender o processo completo
- IDEs serão introduzidas depois

---

### Por que não começar direto com IDE?

- Para entender o que acontece “por baixo do capô”
- Para aprender a resolver erros básicos
- Para não depender apenas de ferramentas gráficas

💡 IDE facilita, mas **não substitui o entendimento**.

---

## ✏️ Atividade Rápida (reflexão guiada)

### 🧠 Atividade — Pensando na execução

Responda e discuta com a turma:

- Por que Java utiliza uma máquina virtual?
- Qual vantagem isso traz para sistemas grandes?
- O que muda quando o sistema operacional é diferente?

📌 **Objetivo:** Fixar o conceito de Java como plataforma.

---

## ✅ Resumo do Bloco 2

Neste bloco você aprendeu:

- Java como linguagem e plataforma
- O papel da JVM na execução de programas
- A diferença entre JVM, JRE e JDK
- Como funciona o fluxo de execução do Java
- O que compõe o ambiente de desenvolvimento Java

---

## ➡️ Próximos Passos

No próximo bloco você vai aprender:

- Estrutura mínima de um programa Java
- O método `main`
- Como compilar e executar seu primeiro programa Java
- Primeiros comandos de saída no console

---

## 📚 Observações Importantes

🚫 **Neste bloco NÃO focamos em:**
- Sintaxe detalhada
- Lógica de programação
- Orientação a Objetos formal

✅ **O foco agora está em:**
- Plataforma Java
- Ambiente de execução
- Funcionamento interno do Java

> 💭 *“Antes de escrever código, é preciso entender como ele será executado.”*
