# 📘 Bloco 2 — Operadores em Java

> **Duração estimada:** 50 minutos  
> **Objetivo:** Dominar os operadores que permitem manipular e comparar dados em Java

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Utilizar operadores aritméticos para cálculos
- Aplicar operadores de atribuição e incremento/decremento
- Comparar valores com operadores relacionais
- Criar expressões lógicas complexas
- Entender a precedência de operadores
- Combinar diferentes tipos de operadores

---

## 💡 Conceitos Fundamentais

### O que são operadores?

**Operadores** são símbolos especiais que realizam operações sobre um ou mais valores (operandos).

```java
int resultado = 10 + 5;
//              ↑   ↑  ↑
//          operando operador operando
```

Java possui várias categorias de operadores:
1. **Aritméticos** — cálculos matemáticos
2. **Atribuição** — atribuir valores
3. **Relacionais** — comparações
4. **Lógicos** — combinação de condições
5. **Incremento/Decremento** — aumentar/diminuir em 1

---

## 🔢 Operadores Aritméticos

### Operadores básicos

| Operador | Operação | Exemplo | Resultado |
|----------|----------|---------|-----------|
| `+` | Adição | `10 + 5` | `15` |
| `-` | Subtração | `10 - 5` | `5` |
| `*` | Multiplicação | `10 * 5` | `50` |
| `/` | Divisão | `10 / 5` | `2` |
| `%` | Módulo (resto) | `10 % 3` | `1` |

**Exemplos:**
```java
int a = 10;
int b = 3;

int soma = a + b;           // 13
int subtracao = a - b;      // 7
int multiplicacao = a * b;  // 30
int divisao = a / b;        // 3 (divisão inteira!)
int resto = a % b;          // 1
```

---

### ⚠️ Cuidados importantes

#### 1. Divisão inteira vs. divisão decimal

```java
// Divisão entre inteiros resulta em inteiro
int a = 10;
int b = 3;
int resultado1 = a / b;  // 3 (perde .333...)

// Para ter resultado decimal, pelo menos um deve ser double
double resultado2 = (double) a / b;  // 3.333...
double resultado3 = 10.0 / 3;        // 3.333...
```

#### 2. Divisão por zero

```java
int x = 10 / 0;      // ❌ Erro em tempo de execução!
double y = 10.0 / 0; // Infinity (infinito)
```

#### 3. Módulo (%) é para resto, não porcentagem

```java
int resto = 10 % 3;  // 1 (resto da divisão de 10 por 3)
// Para calcular porcentagem, use multiplicação e divisão
double porcentagem = (10.0 / 100) * 15;  // 1.5 (10% de 15)
```

---

### Uso prático do operador módulo (%)

O módulo é útil para:

**1. Verificar se número é par ou ímpar:**
```java
int numero = 10;
if (numero % 2 == 0) {
    System.out.println("Par");
} else {
    System.out.println("Ímpar");
}
```

**2. Verificar múltiplos:**
```java
int numero = 15;
if (numero % 5 == 0) {
    System.out.println("É múltiplo de 5");
}
```

**3. Fazer ciclos (ex: alternar entre valores):**
```java
// Obter último dígito de um número
int numero = 12345;
int ultimoDigito = numero % 10;  // 5
```

---

## 📝 Operadores de Atribuição

### Atribuição simples

```java
int x = 10;  // Atribui 10 a x
```

### Atribuição composta

Combina operação aritmética com atribuição:

| Operador | Equivalente | Exemplo |
|----------|-------------|---------|
| `+=` | `x = x + y` | `x += 5` |
| `-=` | `x = x - y` | `x -= 3` |
| `*=` | `x = x * y` | `x *= 2` |
| `/=` | `x = x / y` | `x /= 4` |
| `%=` | `x = x % y` | `x %= 3` |

**Exemplos:**
```java
int contador = 10;

contador += 5;   // contador = contador + 5  → 15
contador -= 3;   // contador = contador - 3  → 12
contador *= 2;   // contador = contador * 2  → 24
contador /= 4;   // contador = contador / 4  → 6
contador %= 4;   // contador = contador % 4  → 2
```

💡 **Por que usar?** Código mais conciso e, muitas vezes, mais legível.

---

## ➕➖ Operadores de Incremento e Decremento

### Incremento (++)

Adiciona 1 ao valor da variável:

```java
int x = 5;
x++;  // x agora é 6 (pós-incremento)
++x;  // x agora é 7 (pré-incremento)
```

### Decremento (--)

Subtrai 1 do valor da variável:

```java
int y = 10;
y--;  // y agora é 9 (pós-decremento)
--y;  // y agora é 8 (pré-decremento)
```

---

### Diferença entre pré e pós incremento/decremento

```java
int a = 5;
int b = a++;  // b = 5, depois a = 6 (usa primeiro, incrementa depois)

int c = 5;
int d = ++c;  // c = 6, depois d = 6 (incrementa primeiro, usa depois)
```

**Detalhamento:**
```java
// Pós-incremento (a++)
int a = 5;
int b = a++;
// Passo 1: b recebe o valor de a (5)
// Passo 2: a é incrementado (6)
// Resultado: b = 5, a = 6

// Pré-incremento (++a)
int c = 5;
int d = ++c;
// Passo 1: c é incrementado (6)
// Passo 2: d recebe o valor de c (6)
// Resultado: c = 6, d = 6
```

💡 **Dica:** Na maioria dos casos, use pós-incremento (`x++`). Use pré-incremento (`++x`) apenas quando necessário.

---

## ⚖️ Operadores Relacionais (Comparação)

Operadores relacionais **comparam dois valores** e retornam `true` ou `false`.

| Operador | Significado | Exemplo | Resultado |
|----------|-------------|---------|-----------|
| `==` | Igual a | `5 == 5` | `true` |
| `!=` | Diferente de | `5 != 3` | `true` |
| `>` | Maior que | `7 > 5` | `true` |
| `<` | Menor que | `3 < 10` | `true` |
| `>=` | Maior ou igual | `5 >= 5` | `true` |
| `<=` | Menor ou igual | `4 <= 3` | `false` |

**Exemplos:**
```java
int idade = 18;

boolean maior = idade >= 18;      // true
boolean menor = idade < 18;       // false
boolean exatos18 = idade == 18;   // true
boolean nao18 = idade != 18;      // false
```

---

### ⚠️ Cuidado: `==` vs `=`

```java
int x = 5;      // ✅ Atribuição (um sinal de igual)
boolean y = (x == 5);  // ✅ Comparação (dois sinais de igual)

if (x = 5) { }  // ❌ Erro! Você quis comparar, não atribuir
if (x == 5) { } // ✅ Correto!
```

💡 **Regra de ouro:** 
- `=` → **atribui** valor
- `==` → **compara** valores

---

## 🔗 Operadores Lógicos

Operadores lógicos **combinam expressões booleanas**.

| Operador | Nome | Descrição | Exemplo |
|----------|------|-----------|---------|
| `&&` | E (AND) | Verdadeiro se **ambos** forem verdadeiros | `(a > 5) && (b < 10)` |
| `\|\|` | OU (OR) | Verdadeiro se **pelo menos um** for verdadeiro | `(a > 5) \|\| (b < 10)` |
| `!` | NÃO (NOT) | Inverte o valor booleano | `!(a > 5)` |

---

### Operador E (&&)

**Ambas** as condições precisam ser verdadeiras:

```java
int idade = 20;
boolean temCarteira = true;

// Pode dirigir se tem 18+ anos E tem carteira
boolean podeDirigir = (idade >= 18) && temCarteira;  // true
```

**Tabela verdade do &&:**
| A | B | A && B |
|---|---|--------|
| true | true | true |
| true | false | false |
| false | true | false |
| false | false | false |

---

### Operador OU (||)

**Pelo menos uma** das condições precisa ser verdadeira:

```java
int idade = 16;
boolean temAutorizacao = true;

// Pode entrar se tem 18+ anos OU tem autorização
boolean podeEntrar = (idade >= 18) || temAutorizacao;  // true
```

**Tabela verdade do ||:**
| A | B | A \|\| B |
|---|---|----------|
| true | true | true |
| true | false | true |
| false | true | true |
| false | false | false |

---

### Operador NÃO (!)

**Inverte** o valor booleano:

```java
boolean chovendo = false;
boolean ensolarado = !chovendo;  // true

boolean ativo = true;
boolean inativo = !ativo;  // false
```

---

### Combinando operadores lógicos

```java
int idade = 25;
boolean estudante = true;
boolean temDesconto = false;

// Regra complexa: tem desconto se for estudante OU tiver menos de 18 anos
boolean ganhaDesconto = estudante || (idade < 18);  // true

// Regra: pode votar se tem 16+ anos E não está com título suspenso
boolean tituloSuspenso = false;
boolean podeVotar = (idade >= 16) && !tituloSuspenso;  // true
```

---

### ⚡ Avaliação em curto-circuito

Java usa **avaliação preguiçosa** para `&&` e `||`:

**Com `&&`:**
```java
// Se a primeira condição é falsa, a segunda não é avaliada
boolean resultado = (5 > 10) && (metodoComEfeitoColateral());
// metodoComEfeitoColateral() NÃO será executado
```

**Com `||`:**
```java
// Se a primeira condição é verdadeira, a segunda não é avaliada
boolean resultado = (5 < 10) || (metodoComEfeitoColateral());
// metodoComEfeitoColateral() NÃO será executado
```

💡 **Por que isso importa?** Pode evitar erros e melhorar performance.

---

## 📊 Precedência de Operadores

Quando há múltiplos operadores, Java segue uma **ordem de precedência**:

**Ordem (do maior para o menor):**

1. `()` — Parênteses
2. `!`, `++`, `--` — Operadores unários
3. `*`, `/`, `%` — Multiplicação, divisão, módulo
4. `+`, `-` — Adição, subtração
5. `<`, `<=`, `>`, `>=` — Relacionais
6. `==`, `!=` — Igualdade
7. `&&` — E lógico
8. `||` — OU lógico
9. `=`, `+=`, `-=`, etc. — Atribuição

**Exemplos:**
```java
int resultado = 10 + 5 * 2;  // 20 (não 30) - multiplicação primeiro

int x = 10 > 5 && 3 < 7;  // Erro de compilação! Precisa de parênteses
int x = (10 > 5) && (3 < 7);  // ✅ Correto: true && true = true

boolean b = 5 + 3 > 10 - 2;  // (5+3) > (10-2) → 8 > 8 → false
```

💡 **Dica:** **Use parênteses** para deixar clara sua intenção, mesmo que não sejam estritamente necessários.

```java
// Menos claro
int resultado = a + b * c - d / e;

// Mais claro
int resultado = a + (b * c) - (d / e);
```

---

## 💻 Exemplos Práticos Completos

### Exemplo 1: Calculadora de IMC
```java
public class CalculadoraIMC {
    public static void main(String[] args) {
        double peso = 70.0;      // em kg
        double altura = 1.75;    // em metros
        
        double imc = peso / (altura * altura);
        
        System.out.println("Seu IMC: " + imc);
        
        // Interpretação
        boolean abaixoDoPeso = imc < 18.5;
        boolean pesoNormal = imc >= 18.5 && imc < 25;
        boolean sobrepeso = imc >= 25 && imc < 30;
        boolean obesidade = imc >= 30;
        
        System.out.println("Abaixo do peso? " + abaixoDoPeso);
        System.out.println("Peso normal? " + pesoNormal);
        System.out.println("Sobrepeso? " + sobrepeso);
        System.out.println("Obesidade? " + obesidade);
    }
}
```

---

### Exemplo 2: Verificação de elegibilidade para voto
```java
public class Eleitor {
    public static void main(String[] args) {
        int idade = 17;
        
        boolean votoObrigatorio = idade >= 18 && idade < 70;
        boolean votoFacultativo = (idade >= 16 && idade < 18) || idade >= 70;
        boolean nãoPodeVotar = idade < 16;
        
        System.out.println("Voto obrigatório? " + votoObrigatorio);
        System.out.println("Voto facultativo? " + votoFacultativo);
        System.out.println("Não pode votar? " + nãoPodeVotar);
    }
}
```

---

### Exemplo 3: Validação de senha
```java
public class ValidadorSenha {
    public static void main(String[] args) {
        String senha = "abc123";
        int tamanho = senha.length();
        
        boolean tamanhoValido = tamanho >= 8;
        boolean temNumero = senha.matches(".*\\d.*");  // Verifica se tem dígito
        
        boolean senhaForte = tamanhoValido && temNumero;
        
        System.out.println("Tamanho >= 8? " + tamanhoValido);
        System.out.println("Tem número? " + temNumero);
        System.out.println("Senha forte? " + senhaForte);
    }
}
```

---

## ✏️ Atividades Práticas

### 📝 Atividade 1 — Operações matemáticas

**Objetivo:** Praticar operadores aritméticos.

**O que fazer:**
1. Declare duas variáveis `a = 17` e `b = 5`
2. Calcule e imprima:
   - Soma, subtração, multiplicação
   - Divisão inteira e resto
   - Divisão decimal (pelo menos um deve ser `double`)
3. Use operadores compostos para modificar `a`:
   - Adicione 10
   - Multiplique por 2
   - Divida por 3

---

### 📝 Atividade 2 — Comparações

**Objetivo:** Praticar operadores relacionais.

**O que fazer:**
1. Crie um programa que verifique se uma pessoa pode:
   - Tirar CNH (idade >= 18)
   - Pagar meia-entrada (idade < 18 OU idade >= 60)
   - Votar obrigatoriamente (idade >= 18 E idade < 70)

---

### 📝 Atividade 3 — Lógica de acesso

**Objetivo:** Combinar operadores lógicos.

**O que fazer:**

Um sistema permite acesso se:
- Usuário é admin OU
- (Usuário está ativo E tem permissão)

Implemente essa lógica:
```java
boolean isAdmin = false;
boolean isAtivo = true;
boolean temPermissao = true;

boolean podeAcessar = // sua expressão aqui

System.out.println("Pode acessar? " + podeAcessar);
```

---

### 📝 Atividade 4 — Desafio: Calculadora de desconto

**Objetivo:** Aplicar todos os tipos de operadores.

**Regras:**
- Produto custa R$ 100
- Se quantidade >= 10: 10% de desconto
- Se cliente é VIP: mais 5% de desconto
- Calcule o valor final

```java
double precoProduto = 100.0;
int quantidade = 12;
boolean clienteVIP = true;

// Seu código aqui

System.out.println("Valor final: R$ " + valorFinal);
```

---

## ✅ Resumo do Bloco 2

Neste bloco você aprendeu:

- ✅ Operadores aritméticos (+, -, *, /, %)
- ✅ Operadores de atribuição (=, +=, -=, etc.)
- ✅ Incremento e decremento (++, --)
- ✅ Operadores relacionais (==, !=, <, >, <=, >=)
- ✅ Operadores lógicos (&&, ||, !)
- ✅ Precedência de operadores
- ✅ Como combinar operadores em expressões complexas

---

## 🎯 Pontos-Chave para Memorizar

1. **Divisão de inteiros** resulta em inteiro (use casting para decimal)
2. **`=` é atribuição**, `==` é comparação
3. **`&&` exige ambas** as condições verdadeiras
4. **`||` exige apenas uma** condição verdadeira
5. **Use parênteses** para deixar expressões claras
6. **Incremento:** `x++` usa depois incrementa, `++x` incrementa depois usa

---

## ➡️ Próximos Passos

No próximo bloco você vai aprender:

- Estruturas condicionais (`if`, `else`, `else if`)
- Operador ternário
- Estrutura `switch-case`
- Como usar os operadores que você aprendeu para controlar o fluxo do programa

---

> 💭 *"Operadores são as ferramentas que transformam dados em decisões e ações."*
