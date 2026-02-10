# 📘 Bloco 4 — Estruturas de Repetição e Escopo

> **Duração estimada:** 60 minutos  
> **Objetivo:** Dominar loops para automatizar tarefas repetitivas e entender o conceito de escopo de variáveis

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Criar loops com `while`, `do-while` e `for`
- Iterar sobre arrays com `for-each`
- Usar `break` e `continue` para controlar loops
- Entender o que é escopo de variáveis
- Identificar onde variáveis podem ser acessadas
- Evitar erros relacionados a escopo

---

## 💡 Conceitos Fundamentais

### O que são estruturas de repetição?

**Estruturas de repetição (loops)** permitem executar um bloco de código **múltiplas vezes** sem precisar reescrevê-lo.

**Imagine:**
```java
// ❌ Sem loops
System.out.println("Contagem: 1");
System.out.println("Contagem: 2");
System.out.println("Contagem: 3");
// ... (repetir 100 vezes?)

// ✅ Com loops
for (int i = 1; i <= 100; i++) {
    System.out.println("Contagem: " + i);
}
```

💡 **Ideia central:** Automatizar tarefas repetitivas.

---

## 🔁 Loop WHILE

### Sintaxe

```java
while (condição) {
    // código a ser repetido enquanto condição for verdadeira
}
```

**Fluxo:**
1. Testa a condição
2. Se `true`: executa o bloco e volta ao passo 1
3. Se `false`: sai do loop

---

### Exemplo básico

```java
int contador = 1;

while (contador <= 5) {
    System.out.println("Contador: " + contador);
    contador++;  // IMPORTANTE: atualizar a condição
}

System.out.println("Fim do loop");
```

**Saída:**
```
Contador: 1
Contador: 2
Contador: 3
Contador: 4
Contador: 5
Fim do loop
```

---

### ⚠️ Cuidado: Loop infinito

```java
int x = 1;

while (x <= 5) {
    System.out.println(x);
    // ❌ Esqueceu de incrementar x!
    // Loop nunca termina
}
```

💡 **Regra de ouro:** Sempre garanta que a condição eventualmente se torne `false`.

---

### Quando usar while

✅ **Use `while` quando:**
- Não sabe quantas iterações serão necessárias
- A repetição depende de uma condição externa
- Quer testar a condição **antes** de executar

**Exemplos de uso:**
```java
// 1. Validação de entrada
Scanner scanner = new Scanner(System.in);
int idade = -1;

while (idade < 0 || idade > 120) {
    System.out.print("Digite uma idade válida: ");
    idade = scanner.nextInt();
}

// 2. Processamento até condição específica
String linha = leitorArquivo.lerLinha();
while (linha != null) {
    processarLinha(linha);
    linha = leitorArquivo.lerLinha();
}
```

---

## 🔁 Loop DO-WHILE

### Sintaxe

```java
do {
    // código a ser executado
} while (condição);
```

**Diferença do `while`:** Executa o bloco **pelo menos uma vez**, depois testa a condição.

---

### Exemplo

```java
int contador = 1;

do {
    System.out.println("Contador: " + contador);
    contador++;
} while (contador <= 5);
```

---

### Diferença crucial: while vs do-while

```java
// WHILE: pode não executar nenhuma vez
int x = 10;
while (x < 5) {
    System.out.println("Executou");  // Não será impresso
}

// DO-WHILE: executa pelo menos uma vez
int y = 10;
do {
    System.out.println("Executou");  // Será impresso uma vez
} while (y < 5);
```

---

### Quando usar do-while

✅ **Use `do-while` quando:**
- Precisa executar o bloco **pelo menos uma vez**
- A condição só faz sentido após a primeira execução

**Exemplo clássico: menu de opções**
```java
Scanner scanner = new Scanner(System.in);
int opcao;

do {
    System.out.println("=== MENU ===");
    System.out.println("1 - Opção A");
    System.out.println("2 - Opção B");
    System.out.println("0 - Sair");
    System.out.print("Escolha: ");
    opcao = scanner.nextInt();
    
    switch (opcao) {
        case 1:
            System.out.println("Você escolheu A");
            break;
        case 2:
            System.out.println("Você escolheu B");
            break;
        case 0:
            System.out.println("Saindo...");
            break;
        default:
            System.out.println("Opção inválida!");
    }
    
} while (opcao != 0);
```

---

## 🔁 Loop FOR

### Sintaxe

```java
for (inicialização; condição; atualização) {
    // código a ser repetido
}
```

**Componentes:**
1. **Inicialização:** executada uma vez no início
2. **Condição:** testada antes de cada iteração
3. **Atualização:** executada ao final de cada iteração

---

### Exemplo básico

```java
for (int i = 1; i <= 5; i++) {
    System.out.println("Contador: " + i);
}
```

**Equivalente com while:**
```java
int i = 1;           // inicialização
while (i <= 5) {     // condição
    System.out.println("Contador: " + i);
    i++;             // atualização
}
```

---

### Anatomia do for

```java
for (int i = 0; i < 10; i++) {
//   ┬─────┬  ┬────┬  ┬──┬
//   │     │  │    │  │  └─ Atualização (executa após cada iteração)
//   │     │  │    └──┴──── Condição (testa antes de cada iteração)
//   │     └──┴─────────── Declaração e inicialização
//   └────────────────────── Tipo e nome da variável
    System.out.println(i);
}
```

---

### Variações do for

**1. Contagem regressiva:**
```java
for (int i = 10; i >= 1; i--) {
    System.out.println(i);
}
System.out.println("Feliz Ano Novo!");
```

**2. Incremento de 2 em 2:**
```java
for (int i = 0; i <= 10; i += 2) {
    System.out.println(i);  // 0, 2, 4, 6, 8, 10
}
```

**3. Múltiplas variáveis:**
```java
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println("i = " + i + ", j = " + j);
}
```

**4. Loop sem corpo (só pelos efeitos da atualização):**
```java
int soma = 0;
for (int i = 1; i <= 100; soma += i, i++);
System.out.println("Soma: " + soma);  // 5050
```

---

### Quando usar for

✅ **Use `for` quando:**
- Sabe o número exato de iterações
- Precisa de um contador
- Quer uma estrutura compacta e clara

**Exemplos de uso:**
```java
// 1. Iterar um número fixo de vezes
for (int i = 1; i <= 10; i++) {
    System.out.println("Linha " + i);
}

// 2. Iterar sobre índices de um array
int[] numeros = {10, 20, 30, 40, 50};
for (int i = 0; i < numeros.length; i++) {
    System.out.println("Posição " + i + ": " + numeros[i]);
}

// 3. Tabuada
int numero = 7;
for (int i = 1; i <= 10; i++) {
    System.out.println(numero + " x " + i + " = " + (numero * i));
}
```

---

## 🔁 Loop FOR-EACH

### Sintaxe

```java
for (tipo variável : coleção) {
    // código usando a variável
}
```

**Também chamado de "enhanced for" ou "for aprimorado".**

---

### Quando usar for-each

✅ **Use `for-each` quando:**
- Quer iterar sobre **todos** os elementos de um array ou coleção
- **Não precisa** do índice
- Só precisa **ler** os valores (não modificar)

---

### Exemplo com array

```java
String[] frutas = {"Maçã", "Banana", "Laranja", "Uva"};

// FOR tradicional
for (int i = 0; i < frutas.length; i++) {
    System.out.println(frutas[i]);
}

// FOR-EACH (mais limpo)
for (String fruta : frutas) {
    System.out.println(fruta);
}
```

**Leia como:** "para cada fruta em frutas..."

---

### Limitações do for-each

❌ **NÃO use for-each quando:**

**1. Precisa do índice:**
```java
// ❌ Não funciona com for-each
for (String fruta : frutas) {
    System.out.println("Índice: ???");  // Não tem acesso ao índice
}

// ✅ Use for tradicional
for (int i = 0; i < frutas.length; i++) {
    System.out.println("Índice " + i + ": " + frutas[i]);
}
```

**2. Precisa modificar o array:**
```java
int[] numeros = {1, 2, 3, 4, 5};

// ❌ Não modifica o array original
for (int num : numeros) {
    num = num * 2;  // Modifica apenas a cópia local
}

// ✅ Use for tradicional
for (int i = 0; i < numeros.length; i++) {
    numeros[i] = numeros[i] * 2;  // Modifica o array
}
```

**3. Precisa iterar em ordem diferente ou pular elementos:**
```java
// ❌ For-each sempre vai do início ao fim
// ✅ Use for tradicional
for (int i = numeros.length - 1; i >= 0; i--) {
    System.out.println(numeros[i]);  // Ordem reversa
}
```

---

## 🛑 Palavras-chave BREAK e CONTINUE

### BREAK: sair do loop

```java
for (int i = 1; i <= 10; i++) {
    if (i == 5) {
        break;  // Sai do loop quando i = 5
    }
    System.out.println(i);
}
// Saída: 1 2 3 4
```

**Uso comum:**
```java
// Procurar por um elemento
int[] numeros = {10, 20, 30, 40, 50};
int procurado = 30;
boolean encontrado = false;

for (int num : numeros) {
    if (num == procurado) {
        encontrado = true;
        break;  // Não precisa continuar procurando
    }
}

System.out.println("Encontrado? " + encontrado);
```

---

### CONTINUE: pular para a próxima iteração

```java
for (int i = 1; i <= 5; i++) {
    if (i == 3) {
        continue;  // Pula o resto quando i = 3
    }
    System.out.println(i);
}
// Saída: 1 2 4 5
```

**Uso comum:**
```java
// Processar apenas números pares
for (int i = 1; i <= 10; i++) {
    if (i % 2 != 0) {
        continue;  // Pula ímpares
    }
    System.out.println(i + " é par");
}
```

---

### Break em loops aninhados

```java
// Break só sai do loop mais interno
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (j == 2) {
            break;  // Sai apenas do loop interno
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}

// Para sair de ambos os loops, use labels:
externo:
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (j == 2) {
            break externo;  // Sai do loop externo
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
```

---

## 🔒 Escopo de Variáveis

### O que é escopo?

**Escopo** é a **região do código** onde uma variável existe e pode ser acessada.

💡 **Regra geral:** Uma variável só existe dentro do bloco `{ }` onde foi declarada.

---

### Tipos de escopo

**1. Escopo de classe (você verá mais quando estudar OO):**
```java
public class MinhaClasse {
    int variavelDeClasse;  // Visível em toda a classe
    
    public void metodo() {
        variavelDeClasse = 10;  // ✅ Pode acessar
    }
}
```

**2. Escopo de método:**
```java
public static void main(String[] args) {
    int x = 10;  // Existe apenas dentro do main
    System.out.println(x);  // ✅
}
// System.out.println(x);  // ❌ Erro! x não existe aqui
```

**3. Escopo de bloco:**
```java
public static void main(String[] args) {
    int x = 10;
    
    if (x > 5) {
        int y = 20;  // y só existe dentro do if
        System.out.println(x);  // ✅ x é visível aqui
        System.out.println(y);  // ✅ y é visível aqui
    }
    
    System.out.println(x);  // ✅ x ainda existe
    System.out.println(y);  // ❌ Erro! y não existe fora do if
}
```

---

### Escopo em loops

**Variável declarada no for:**
```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);  // ✅ i existe aqui
}
System.out.println(i);  // ❌ Erro! i não existe fora do for
```

**Variável declarada antes do loop:**
```java
int i = 0;  // Declarada fora

for (i = 0; i < 5; i++) {
    System.out.println(i);  // ✅
}

System.out.println(i);  // ✅ i ainda existe (valor = 5)
```

---

### Sombreamento (Shadowing)

Você **não pode** declarar variáveis com o mesmo nome em escopos aninhados:

```java
public static void main(String[] args) {
    int x = 10;
    
    if (x > 5) {
        int x = 20;  // ❌ Erro! x já foi declarada
    }
}
```

Mas pode reutilizar o nome em escopos **separados**:

```java
public static void main(String[] args) {
    if (condicao1) {
        int x = 10;  // ✅ x existe só aqui
    }
    
    if (condicao2) {
        int x = 20;  // ✅ Outro escopo, pode reutilizar
    }
}
```

---

### Por que escopo importa?

1. **Organização:** Variáveis existem apenas onde são necessárias
2. **Economia de memória:** Variáveis são liberadas quando saem de escopo
3. **Menos erros:** Reduz conflitos de nomes
4. **Clareza:** Fica claro o ciclo de vida de cada variável

💡 **Boa prática:** Declare variáveis no menor escopo possível.

```java
// ❌ Escopo maior que necessário
int soma = 0;
int produto = 0;
int divisao = 0;

if (condicao) {
    soma = a + b;  // soma só é usada aqui
}

// ✅ Escopo adequado
if (condicao) {
    int soma = a + b;  // soma existe só onde é necessária
}
```

---

## 💻 Exemplos Práticos Completos

### Exemplo 1: Tabuada completa

```java
public class Tabuada {
    public static void main(String[] args) {
        int numero = 7;
        
        System.out.println("=== Tabuada do " + numero + " ===");
        
        for (int i = 1; i <= 10; i++) {
            int resultado = numero * i;
            System.out.println(numero + " x " + i + " = " + resultado);
        }
    }
}
```

---

### Exemplo 2: Contagem de dígitos

```java
public class ContadorDigitos {
    public static void main(String[] args) {
        int numero = 12345;
        int contador = 0;
        int temp = numero;
        
        while (temp > 0) {
            temp = temp / 10;  // Remove último dígito
            contador++;
        }
        
        System.out.println("O número " + numero + " tem " + contador + " dígitos");
    }
}
```

---

### Exemplo 3: Números primos

```java
public class NumerosPrimos {
    public static void main(String[] args) {
        int limite = 50;
        
        System.out.println("Números primos até " + limite + ":");
        
        for (int num = 2; num <= limite; num++) {
            boolean ehPrimo = true;
            
            for (int divisor = 2; divisor <= Math.sqrt(num); divisor++) {
                if (num % divisor == 0) {
                    ehPrimo = false;
                    break;  // Não é primo, sai do loop interno
                }
            }
            
            if (ehPrimo) {
                System.out.print(num + " ");
            }
        }
    }
}
```

---

### Exemplo 4: Pirâmide de asteriscos

```java
public class Piramide {
    public static void main(String[] args) {
        int altura = 5;
        
        for (int linha = 1; linha <= altura; linha++) {
            // Espaços antes dos asteriscos
            for (int espaco = 1; espaco <= altura - linha; espaco++) {
                System.out.print(" ");
            }
            
            // Asteriscos
            for (int asterisco = 1; asterisco <= 2 * linha - 1; asterisco++) {
                System.out.print("*");
            }
            
            System.out.println();  // Nova linha
        }
    }
}
```

**Saída:**
```
    *
   ***
  *****
 *******
*********
```

---

## ✏️ Atividades Práticas

### 📝 Atividade 1 — Soma de números

**Objetivo:** Praticar `while`.

**O que fazer:**
Crie um programa que soma todos os números de 1 a 100 usando `while`.

---

### 📝 Atividade 2 — Fatorial

**Objetivo:** Praticar `for`.

**O que fazer:**
Calcule o fatorial de um número (ex: 5! = 5 × 4 × 3 × 2 × 1 = 120).

---

### 📝 Atividade 3 — Verificador de palíndromo numérico

**Objetivo:** Combinar loops e lógica.

**O que fazer:**
Verifique se um número é palíndromo (ex: 12321 é, 12345 não é).

**Dica:** Inverta o número e compare.

---

### 📝 Atividade 4 — Série Fibonacci

**Objetivo:** Praticar loops e variáveis.

**O que fazer:**
Imprima os primeiros 15 números da série Fibonacci (0, 1, 1, 2, 3, 5, 8, 13...).

---

### 📝 Atividade 5 — Desafio: Jogo de adivinhação

**Objetivo:** Integrar tudo.

**O que fazer:**
```java
int numeroSecreto = 42;
// Use Scanner para ler palpites
// Dê dicas ("maior" ou "menor")
// Conte tentativas
// Use do-while para permitir jogar novamente
```

---

## ✅ Resumo do Bloco 4

Neste bloco você aprendeu:

- ✅ Loop `while` para repetições com condição inicial
- ✅ Loop `do-while` para garantir pelo menos uma execução
- ✅ Loop `for` para contagens controladas
- ✅ Loop `for-each` para iterar coleções
- ✅ Palavras-chave `break` e `continue`
- ✅ Conceito de escopo de variáveis
- ✅ Onde variáveis podem ser acessadas

---

## 🎯 Pontos-Chave para Memorizar

1. **`while`:** testa antes, pode não executar
2. **`do-while`:** executa uma vez, depois testa
3. **`for`:** ideal quando sabe o número de iterações
4. **`for-each`:** ideal para percorrer coleções (só leitura)
5. **`break`:** sai do loop
6. **`continue`:** pula para próxima iteração
7. **Escopo:** variável só existe dentro do bloco `{ }` onde foi declarada

---

## 🎓 Conclusão da Aula 02

Parabéns! Você completou a Aula 02 e agora domina:

- ✅ Variáveis e tipos primitivos
- ✅ Operadores (aritméticos, relacionais, lógicos)
- ✅ Estruturas condicionais (if, else, switch)
- ✅ Estruturas de repetição (while, do-while, for, for-each)
- ✅ Controle de fluxo (break, continue)
- ✅ Escopo de variáveis

**Você está pronto para começar a pensar em Orientação a Objetos!**

Na próxima aula, você aprenderá a organizar código usando **Classes e Objetos**.

---

> 💭 *"Loops são o coração da automação. Escopo é a disciplina que mantém o código organizado."*
