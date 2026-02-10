# 📘 Bloco 1 — Variáveis e Tipos Primitivos

> **Duração estimada:** 50 minutos  
> **Objetivo:** Entender como declarar variáveis e trabalhar com os tipos primitivos de dados em Java

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Compreender o que é uma variável
- Declarar variáveis em Java corretamente
- Conhecer e utilizar os 8 tipos primitivos de dados
- Entender a diferença entre tipos primitivos e tipos de referência
- Aplicar regras de nomenclatura de variáveis
- Realizar conversões entre tipos (casting)

---

## 💡 Conceitos Fundamentais

### O que é uma variável?

Uma variável é um **espaço nomeado na memória** que armazena um valor.

Pense em variáveis como **caixas etiquetadas**:
- Cada caixa tem um **nome** (identificador)
- Cada caixa pode guardar um **tipo específico** de coisa
- O conteúdo da caixa pode **mudar** durante a execução do programa

💡 **Ideia central:**  
Variáveis permitem que programas armazenem, manipulem e recuperem dados.

---

### Declarando uma variável

Em Java, para criar uma variável você precisa:

1. **Tipo** — que tipo de dado ela vai armazenar
2. **Nome** — como você vai referenciá-la
3. **Valor** (opcional) — valor inicial

**Sintaxe básica:**
```java
tipo nome;              // Declaração
tipo nome = valor;      // Declaração com inicialização
```

**Exemplos:**
```java
int idade;              // Declaração sem valor inicial
int idade = 25;         // Declaração com valor inicial
double altura = 1.75;   // Outro tipo de dado
```

---

## 🧩 Os 8 Tipos Primitivos em Java

Java possui **8 tipos primitivos** fundamentais, divididos em 4 categorias:

### 1️⃣ Números Inteiros

| Tipo | Tamanho | Faixa de valores | Uso comum |
|------|---------|------------------|-----------|
| `byte` | 8 bits | -128 a 127 | Economia de memória |
| `short` | 16 bits | -32.768 a 32.767 | Valores pequenos |
| `int` | 32 bits | -2³¹ a 2³¹-1 | **Tipo padrão para inteiros** |
| `long` | 64 bits | -2⁶³ a 2⁶³-1 | Valores muito grandes |

**Exemplos:**
```java
byte idade = 25;
short ano = 2024;
int populacao = 213000000;
long distanciaEstrelas = 9460730472580800L;  // Note o 'L' no final
```

💡 **Dica:** Use `int` como padrão. Use `long` apenas quando necessário (adicione 'L' no final do número).

---

### 2️⃣ Números Decimais (Ponto Flutuante)

| Tipo | Tamanho | Precisão | Uso comum |
|------|---------|----------|-----------|
| `float` | 32 bits | ~6-7 dígitos | Economia de memória |
| `double` | 64 bits | ~15 dígitos | **Tipo padrão para decimais** |

**Exemplos:**
```java
float preco = 19.99f;        // Note o 'f' no final
double pi = 3.14159265359;   // Maior precisão
double salario = 5500.50;
```

💡 **Dica:** Use `double` como padrão para números decimais. Use `float` apenas se precisar economizar memória.

---

### 3️⃣ Caractere

| Tipo | Tamanho | Descrição |
|------|---------|-----------|
| `char` | 16 bits | Um único caractere Unicode |

**Exemplos:**
```java
char letra = 'A';
char simbolo = '@';
char numero = '9';      // '9' é diferente de 9
char unicode = '\u0041'; // Também representa 'A'
```

⚠️ **Importante:** Use **aspas simples** para `char` e **aspas duplas** para `String`.

```java
char letra = 'A';      // ✅ Correto
String palavra = "A";  // ✅ Correto (mas é String, não char)
char erro = "A";       // ❌ Erro de compilação
```

---

### 4️⃣ Booleano

| Tipo | Valores possíveis | Uso comum |
|------|------------------|-----------|
| `boolean` | `true` ou `false` | Condições lógicas |

**Exemplos:**
```java
boolean ativo = true;
boolean maiorDeIdade = false;
boolean aprovado = true;
```

💡 **Uso principal:** Estruturas condicionais e loops (que você verá nos próximos blocos).

---

## 📋 Regras de Nomenclatura de Variáveis

### Regras obrigatórias (senão não compila):

1. ✅ Deve começar com letra, `_` ou `$`
2. ✅ Pode conter letras, números, `_` ou `$`
3. ❌ Não pode ser uma palavra reservada do Java
4. ❌ Não pode conter espaços

**Exemplos válidos:**
```java
int idade;
int idade2;
int _contador;
int $preco;
int nomeCompleto;
```

**Exemplos inválidos:**
```java
int 2idade;        // ❌ Começa com número
int meu nome;      // ❌ Contém espaço
int class;         // ❌ Palavra reservada
int nome-completo; // ❌ Contém hífen
```

---

### Convenções (boas práticas):

1. **camelCase** para nomes de variáveis
   ```java
   int idadeUsuario;
   double salarioBruto;
   boolean contaAtiva;
   ```

2. **Nomes descritivos**
   ```java
   // ❌ Ruim
   int x;
   double v;
   
   // ✅ Bom
   int idadeAluno;
   double valorTotal;
   ```

3. **Evite abreviações confusas**
   ```java
   // ❌ Confuso
   int qtd;
   double vlr;
   
   // ✅ Claro
   int quantidade;
   double valor;
   ```

---

## 🔄 Conversão entre Tipos (Casting)

### Conversão Automática (Widening)

Java converte automaticamente tipos menores para maiores:

```java
int numeroInteiro = 100;
long numeroLongo = numeroInteiro;     // ✅ Conversão automática
double numeroDecimal = numeroInteiro; // ✅ Conversão automática
```

**Hierarquia de conversão automática:**
```
byte → short → int → long → float → double
       char  →
```

---

### Conversão Manual (Narrowing/Casting)

Para converter tipos maiores em menores, é necessário **casting explícito**:

```java
double preco = 19.99;
int precoInteiro = (int) preco;  // precoInteiro = 19 (perde a parte decimal)

long numeroGrande = 1000L;
int numeroMedio = (int) numeroGrande;  // ✅ Funciona se o valor couber
```

⚠️ **Cuidado:** Casting pode causar perda de dados!

```java
double valor = 300.75;
byte pequeno = (byte) valor;  // Pode dar resultado inesperado
```

---

## 💻 Exemplos Práticos

### Exemplo 1: Declaração e uso básico
```java
public class ExemploVariaveis {
    public static void main(String[] args) {
        // Declaração de variáveis
        int idade = 25;
        double altura = 1.75;
        char inicial = 'J';
        boolean estudante = true;
        
        // Uso das variáveis
        System.out.println("Idade: " + idade);
        System.out.println("Altura: " + altura);
        System.out.println("Inicial: " + inicial);
        System.out.println("É estudante? " + estudante);
        
        // Modificando valores
        idade = 26;
        System.out.println("Nova idade: " + idade);
    }
}
```

---

### Exemplo 2: Operações matemáticas simples
```java
public class CalculoSimples {
    public static void main(String[] args) {
        int a = 10;
        int b = 3;
        
        int soma = a + b;
        int subtracao = a - b;
        int multiplicacao = a * b;
        int divisao = a / b;           // Divisão inteira: resultado = 3
        int resto = a % b;
        
        System.out.println("Soma: " + soma);
        System.out.println("Subtração: " + subtracao);
        System.out.println("Multiplicação: " + multiplicacao);
        System.out.println("Divisão: " + divisao);
        System.out.println("Resto: " + resto);
    }
}
```

---

### Exemplo 3: Cuidado com divisão de inteiros
```java
public class DivisaoInteiros {
    public static void main(String[] args) {
        int a = 10;
        int b = 3;
        
        int resultadoInteiro = a / b;        // 3 (perde a parte decimal)
        double resultadoDecimal = a / b;     // 3.0 (ainda é divisão inteira!)
        double resultadoCorreto = (double) a / b;  // 3.333... (correto)
        
        System.out.println("Divisão inteira: " + resultadoInteiro);
        System.out.println("Ainda inteira: " + resultadoDecimal);
        System.out.println("Divisão decimal: " + resultadoCorreto);
    }
}
```

---

## ✏️ Atividades Práticas

### 📝 Atividade 1 — Declaração de variáveis

**Objetivo:** Praticar a declaração e inicialização de variáveis.

**O que fazer:**
1. Crie um programa que declare variáveis para armazenar:
   - Seu nome (você precisará usar `String`, veremos mais sobre isso depois)
   - Sua idade
   - Sua altura
   - Se você é estudante
   - A primeira letra do seu nome

2. Imprima todas essas informações no console

**Exemplo de saída esperada:**
```
Nome: João Silva
Idade: 22
Altura: 1.78
É estudante? true
Inicial: J
```

---

### 📝 Atividade 2 — Calculadora simples

**Objetivo:** Praticar operações matemáticas com variáveis.

**O que fazer:**
1. Declare duas variáveis inteiras com valores de sua escolha
2. Calcule e imprima:
   - A soma
   - A subtração
   - A multiplicação
   - A divisão (inteira)
   - O resto da divisão
3. Agora declare duas variáveis `double` e faça a divisão decimal

---

### 📝 Atividade 3 — Conversão de tipos

**Objetivo:** Entender casting e conversão de tipos.

**O que fazer:**
1. Declare uma variável `double` com o valor 15.75
2. Converta-a para `int` e imprima o resultado
3. Declare uma variável `int` com o valor 100
4. Atribua-a a uma variável `double` e imprima
5. Observe o que acontece com os valores

---

### 📝 Atividade 4 — Desafio: Troca de valores

**Objetivo:** Trocar os valores de duas variáveis.

**Problema:**
```java
int a = 10;
int b = 20;

// Seu código aqui para trocar os valores

// Resultado esperado:
// a = 20
// b = 10
```

💡 **Dica:** Você precisará de uma variável auxiliar.

---

## 🧪 Erros Comuns

### Erro 1: Variável não inicializada
```java
int idade;
System.out.println(idade);  // ❌ Erro: variável não foi inicializada
```

**Solução:**
```java
int idade = 0;  // ou qualquer valor inicial apropriado
System.out.println(idade);  // ✅
```

---

### Erro 2: Incompatibilidade de tipos
```java
int numero = 10.5;  // ❌ Erro: 10.5 é double, não int
```

**Solução:**
```java
double numero = 10.5;  // ✅ Tipo correto
// ou
int numero = (int) 10.5;  // ✅ Casting explícito (numero = 10)
```

---

### Erro 3: Nome de variável inválido
```java
int 2numero = 10;  // ❌ Erro: nome não pode começar com número
```

**Solução:**
```java
int numero2 = 10;  // ✅
```

---

## ✅ Resumo do Bloco 1

Neste bloco você aprendeu:

- ✅ O conceito de variável em programação
- ✅ Os 8 tipos primitivos de Java e quando usar cada um
- ✅ Como declarar e inicializar variáveis
- ✅ Regras e convenções de nomenclatura
- ✅ Conversão entre tipos (casting)
- ✅ Operações matemáticas básicas

---

## 🎯 Pontos-Chave para Memorizar

1. **Use `int` para inteiros** e `double` para decimais (na maioria dos casos)
2. **`char` usa aspas simples** ('A'), **String usa aspas duplas** ("texto")
3. **`boolean` só aceita** `true` ou `false`
4. **Nomes de variáveis:** camelCase e descritivos
5. **Divisão de inteiros** resulta em inteiro (10 / 3 = 3, não 3.333...)

---

## ➡️ Próximos Passos

No próximo bloco você vai aprender:

- Operadores aritméticos (+, -, *, /, %)
- Operadores relacionais (==, !=, <, >, <=, >=)
- Operadores lógicos (&&, ||, !)
- Precedência de operadores
- Como combinar operadores para criar expressões complexas

---

## 📚 Observações Importantes

💡 **Diferença importante:**
- **Tipos primitivos:** armazenam valores diretamente (int, double, boolean, etc.)
- **Tipos de referência:** armazenam referências a objetos (String, arrays, objetos)

Você aprenderá sobre tipos de referência quando estudarmos classes e objetos.

> 💭 *"Variáveis são a base de qualquer programa. Domine-as e você terá domínio sobre seus dados."*
