# 📘 Bloco 3 — Estruturas Condicionais

> **Duração estimada:** 50 minutos  
> **Objetivo:** Dominar estruturas de decisão para criar programas que respondem a diferentes condições

---

## 🎯 O que você vai aprender neste bloco

Ao final deste bloco, você será capaz de:

- Implementar decisões simples com `if`
- Criar alternativas com `if-else`
- Encadear múltiplas condições com `else if`
- Usar o operador ternário para decisões simples
- Aplicar a estrutura `switch-case` para múltiplas opções
- Escolher a estrutura condicional adequada para cada situação

---

## 💡 Conceitos Fundamentais

### O que são estruturas condicionais?

**Estruturas condicionais** permitem que o programa execute diferentes blocos de código dependendo de condições específicas.

Até agora, seus programas executavam linha por linha, sempre na mesma ordem. Com estruturas condicionais, você pode criar **ramificações** no fluxo de execução.

```
Início
   ↓
Condição?
   ├── Verdadeiro → Ação A
   └── Falso → Ação B
   ↓
Continua...
```

💡 **Analogia:** É como um GPS que escolhe diferentes rotas dependendo do trânsito.

---

## 🔀 Estrutura IF

### Sintaxe básica

```java
if (condição) {
    // código executado se a condição for verdadeira
}
```

**A condição deve ser uma expressão que resulte em `boolean` (`true` ou `false`).**

---

### Exemplo simples

```java
int idade = 20;

if (idade >= 18) {
    System.out.println("Você é maior de idade");
}

System.out.println("Programa continua...");
```

**Fluxo:**
1. Verifica se `idade >= 18`
2. Se `true`: executa o bloco
3. Se `false`: pula o bloco
4. Continua com o restante do programa

---

### Exemplo com expressões mais complexas

```java
int nota = 75;
boolean presencaSuficiente = true;

if (nota >= 70 && presencaSuficiente) {
    System.out.println("Aprovado!");
}
```

---

### ⚠️ Erro comum: condição não-booleana

```java
int x = 5;

if (x) {  // ❌ Erro! x não é boolean
    System.out.println("Algo");
}

if (x == 5) {  // ✅ Correto! x == 5 retorna boolean
    System.out.println("x é 5");
}
```

💡 Em Java, a condição **SEMPRE** precisa resultar em `boolean`. Diferente de outras linguagens (como C), onde `0` é falso e qualquer outro valor é verdadeiro.

---

## 🔀 Estrutura IF-ELSE

### Sintaxe

```java
if (condição) {
    // código se condição verdadeira
} else {
    // código se condição falsa
}
```

**Garante que um dos dois blocos será executado.**

---

### Exemplo

```java
int idade = 16;

if (idade >= 18) {
    System.out.println("Pode tirar CNH");
} else {
    System.out.println("Não pode tirar CNH ainda");
}
```

---

### Exemplo prático: validação de login

```java
String usuario = "admin";
String senha = "1234";

if (usuario.equals("admin") && senha.equals("1234")) {
    System.out.println("Login bem-sucedido!");
} else {
    System.out.println("Usuário ou senha incorretos");
}
```

⚠️ **Nota:** Para comparar Strings, use `.equals()`, não `==`.

---

## 🔀 Estrutura ELSE IF

### Sintaxe

```java
if (condição1) {
    // código se condição1 verdadeira
} else if (condição2) {
    // código se condição1 falsa e condição2 verdadeira
} else if (condição3) {
    // código se condições anteriores falsas e condição3 verdadeira
} else {
    // código se todas as condições forem falsas
}
```

**Permite testar múltiplas condições em sequência.**

---

### Exemplo: classificação de notas

```java
int nota = 75;

if (nota >= 90) {
    System.out.println("Conceito A");
} else if (nota >= 80) {
    System.out.println("Conceito B");
} else if (nota >= 70) {
    System.out.println("Conceito C");
} else if (nota >= 60) {
    System.out.println("Conceito D");
} else {
    System.out.println("Conceito F - Reprovado");
}
```

**Fluxo:**
- Java testa cada condição **na ordem**
- Quando encontra uma verdadeira, executa aquele bloco
- **Não testa** as condições seguintes (mesmo que também sejam verdadeiras)
- Se nenhuma for verdadeira, executa o `else` (se houver)

---

### ⚠️ Ordem importa!

```java
int nota = 95;

// ❌ Ordem errada
if (nota >= 60) {
    System.out.println("Aprovado");  // Será executado!
} else if (nota >= 90) {
    System.out.println("Excelente");  // Nunca será executado
}

// ✅ Ordem correta
if (nota >= 90) {
    System.out.println("Excelente");
} else if (nota >= 60) {
    System.out.println("Aprovado");
}
```

💡 **Regra:** Coloque as condições mais específicas primeiro.

---

## 🔀 IFs Aninhados

Você pode colocar um `if` dentro de outro:

```java
int idade = 20;
boolean temCarteira = true;

if (idade >= 18) {
    if (temCarteira) {
        System.out.println("Pode dirigir");
    } else {
        System.out.println("Precisa tirar carteira");
    }
} else {
    System.out.println("Muito jovem para dirigir");
}
```

**Equivalente com operador lógico:**
```java
if (idade >= 18 && temCarteira) {
    System.out.println("Pode dirigir");
} else if (idade >= 18 && !temCarteira) {
    System.out.println("Precisa tirar carteira");
} else {
    System.out.println("Muito jovem para dirigir");
}
```

💡 **Dica:** Evite aninhar muitos níveis. Use operadores lógicos quando possível.

---

## ❓ Operador Ternário

### Sintaxe

```java
variável = (condição) ? valorSeVerdadeiro : valorSeFalso;
```

É uma forma **compacta** de escrever um `if-else` simples.

---

### Exemplo

```java
// Com if-else tradicional
int idade = 20;
String categoria;

if (idade >= 18) {
    categoria = "Adulto";
} else {
    categoria = "Menor";
}

// Com operador ternário
String categoria = (idade >= 18) ? "Adulto" : "Menor";
```

---

### Quando usar

✅ **Use quando:**
- Atribuir valor a uma variável baseado em uma condição simples
- A expressão cabe confortavelmente em uma linha

❌ **Evite quando:**
- A lógica é complexa
- Há múltiplas condições
- Prejudica a legibilidade

```java
// ✅ Bom uso
int max = (a > b) ? a : b;
String status = (ativo) ? "Ativo" : "Inativo";

// ❌ Uso ruim (muito complexo)
String resultado = (x > 10 && y < 5) ? ((z == 0) ? "A" : "B") : "C";
```

---

## 🔀 Estrutura SWITCH-CASE

### Quando usar switch?

Use `switch` quando você precisa comparar uma variável com **múltiplos valores específicos**.

**Melhor que `if-else` quando:**
- Há muitas opções (mais de 3-4)
- Todas as comparações são por igualdade (`==`)
- A variável é do tipo: `int`, `byte`, `short`, `char`, `String`, ou `enum`

---

### Sintaxe

```java
switch (variável) {
    case valor1:
        // código para valor1
        break;
    case valor2:
        // código para valor2
        break;
    case valor3:
        // código para valor3
        break;
    default:
        // código se nenhum case corresponder
}
```

---

### Exemplo: dias da semana

```java
int dia = 3;
String nomeDia;

switch (dia) {
    case 1:
        nomeDia = "Domingo";
        break;
    case 2:
        nomeDia = "Segunda-feira";
        break;
    case 3:
        nomeDia = "Terça-feira";
        break;
    case 4:
        nomeDia = "Quarta-feira";
        break;
    case 5:
        nomeDia = "Quinta-feira";
        break;
    case 6:
        nomeDia = "Sexta-feira";
        break;
    case 7:
        nomeDia = "Sábado";
        break;
    default:
        nomeDia = "Dia inválido";
}

System.out.println(nomeDia);  // Terça-feira
```

---

### A importância do BREAK

```java
int opcao = 2;

// ❌ Sem break (fall-through)
switch (opcao) {
    case 1:
        System.out.println("Opção 1");
    case 2:
        System.out.println("Opção 2");
    case 3:
        System.out.println("Opção 3");
    default:
        System.out.println("Padrão");
}
// Saída:
// Opção 2
// Opção 3
// Padrão

// ✅ Com break
switch (opcao) {
    case 1:
        System.out.println("Opção 1");
        break;
    case 2:
        System.out.println("Opção 2");
        break;
    case 3:
        System.out.println("Opção 3");
        break;
    default:
        System.out.println("Padrão");
}
// Saída:
// Opção 2
```

💡 **Regra:** Sempre use `break` ao final de cada `case`, exceto quando quiser propositalmente o "fall-through".

---

### Agrupando cases

Você pode agrupar múltiplos `case` para executar o mesmo código:

```java
int mes = 2;

switch (mes) {
    case 12:
    case 1:
    case 2:
        System.out.println("Verão");
        break;
    case 3:
    case 4:
    case 5:
        System.out.println("Outono");
        break;
    case 6:
    case 7:
    case 8:
        System.out.println("Inverno");
        break;
    case 9:
    case 10:
    case 11:
        System.out.println("Primavera");
        break;
    default:
        System.out.println("Mês inválido");
}
```

---

### Switch com String

A partir do Java 7, você pode usar `String` no `switch`:

```java
String comando = "sair";

switch (comando) {
    case "iniciar":
        System.out.println("Iniciando sistema...");
        break;
    case "pausar":
        System.out.println("Sistema pausado");
        break;
    case "sair":
        System.out.println("Encerrando...");
        break;
    default:
        System.out.println("Comando desconhecido");
}
```

⚠️ **Atenção:** Switch com String é **case-sensitive** (diferencia maiúsculas/minúsculas).

---

## 🆚 IF vs SWITCH: Quando usar cada um?

### Use IF quando:

- ✅ Precisa testar **intervalos** ou **condições complexas**
  ```java
  if (idade >= 18 && idade < 60) { ... }
  ```

- ✅ Usa operadores diferentes de igualdade
  ```java
  if (salario > 5000 || bonus > 1000) { ... }
  ```

- ✅ Compara tipos não suportados pelo switch
  ```java
  if (valor == 3.14) { ... }  // double não funciona em switch
  ```

---

### Use SWITCH quando:

- ✅ Compara uma variável com **múltiplos valores exatos**
  ```java
  switch (opcaoMenu) {
      case 1: ...
      case 2: ...
      case 3: ...
  }
  ```

- ✅ Há **muitas opções** (mais de 3-4)
- ✅ A variável é `int`, `char`, `String`, ou `enum`
- ✅ Cada opção executa código **independente**

---

## 💻 Exemplos Práticos Completos

### Exemplo 1: Calculadora simples

```java
public class Calculadora {
    public static void main(String[] args) {
        double num1 = 10;
        double num2 = 5;
        char operador = '+';
        double resultado = 0;
        
        switch (operador) {
            case '+':
                resultado = num1 + num2;
                break;
            case '-':
                resultado = num1 - num2;
                break;
            case '*':
                resultado = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    resultado = num1 / num2;
                } else {
                    System.out.println("Erro: divisão por zero");
                    return;
                }
                break;
            default:
                System.out.println("Operador inválido");
                return;
        }
        
        System.out.println("Resultado: " + resultado);
    }
}
```

---

### Exemplo 2: Sistema de aprovação

```java
public class SistemaAprovacao {
    public static void main(String[] args) {
        double nota = 7.5;
        double frequencia = 80.0;
        
        if (nota >= 7.0 && frequencia >= 75.0) {
            System.out.println("Aprovado");
        } else if (nota >= 5.0 && frequencia >= 75.0) {
            System.out.println("Recuperação");
        } else {
            System.out.println("Reprovado");
        }
        
        // Mensagem adicional para quem foi aprovado com louvor
        if (nota >= 9.0 && frequencia >= 90.0) {
            System.out.println("Parabéns! Aprovado com louvor!");
        }
    }
}
```

---

### Exemplo 3: Classificação de IMC completa

```java
public class ClassificadorIMC {
    public static void main(String[] args) {
        double peso = 75.0;
        double altura = 1.75;
        
        double imc = peso / (altura * altura);
        String classificacao;
        
        if (imc < 18.5) {
            classificacao = "Abaixo do peso";
        } else if (imc < 25) {
            classificacao = "Peso normal";
        } else if (imc < 30) {
            classificacao = "Sobrepeso";
        } else if (imc < 35) {
            classificacao = "Obesidade grau I";
        } else if (imc < 40) {
            classificacao = "Obesidade grau II";
        } else {
            classificacao = "Obesidade grau III";
        }
        
        System.out.printf("IMC: %.2f%n", imc);
        System.out.println("Classificação: " + classificacao);
    }
}
```

---

## ✏️ Atividades Práticas

### 📝 Atividade 1 — Verificador de idade

**Objetivo:** Praticar `if-else`.

**O que fazer:**
Crie um programa que:
1. Declara uma variável `idade`
2. Verifica e imprime:
   - "Criança" (0-12)
   - "Adolescente" (13-17)
   - "Adulto" (18-59)
   - "Idoso" (60+)

---

### 📝 Atividade 2 — Calculadora de desconto

**Objetivo:** Combinar condições.

**Regras:**
- Compra acima de R$ 100: 10% desconto
- Compra acima de R$ 200: 15% desconto
- Compra acima de R$ 500: 20% desconto
- Cliente VIP ganha +5% em qualquer valor

Implemente e teste com diferentes valores.

---

### 📝 Atividade 3 — Menu com switch

**Objetivo:** Praticar `switch-case`.

**O que fazer:**
Crie um menu de opções:
```
1 - Cadastrar
2 - Listar
3 - Editar
4 - Excluir
5 - Sair
```

Use `switch` para imprimir mensagens diferentes para cada opção.

---

### 📝 Atividade 4 — Conversor de notas

**Objetivo:** Aplicar tudo que aprendeu.

**O que fazer:**
Crie um programa que:
1. Recebe uma nota de 0 a 100
2. Converte para conceito:
   - 90-100: A
   - 80-89: B
   - 70-79: C
   - 60-69: D
   - 0-59: F
3. Imprime: "Nota: X - Conceito: Y - Status: Aprovado/Reprovado"
4. Use `if-else if` para a conversão
5. Use operador ternário para o status (aprovado se nota >= 60)

---

### 📝 Atividade 5 — Desafio: Jogo Pedra, Papel, Tesoura

**Objetivo:** Integrar múltiplos conceitos.

**O que fazer:**
```java
int jogador1 = 1;  // 1=Pedra, 2=Papel, 3=Tesoura
int jogador2 = 2;

// Seu código aqui
// Determine o vencedor ou empate
```

**Dica:** Use `if` aninhado ou `switch` criativo.

---

## 🧪 Erros Comuns

### Erro 1: Esquecer as chaves
```java
// ❌ Perigoso
if (x > 5)
    System.out.println("Maior que 5");
    System.out.println("Essa linha sempre executa!");  // Bug!

// ✅ Correto
if (x > 5) {
    System.out.println("Maior que 5");
    System.out.println("Essa linha só executa se x > 5");
}
```

---

### Erro 2: Usar = ao invés de ==
```java
int x = 5;

if (x = 10) {  // ❌ Erro! Tentando atribuir, não comparar
    // ...
}

if (x == 10) {  // ✅ Correto
    // ...
}
```

---

### Erro 3: Esquecer break no switch
```java
// ❌ Bug de fall-through não intencional
switch (dia) {
    case 1:
        System.out.println("Segunda");
        // faltou break!
    case 2:
        System.out.println("Terça");  // Executa mesmo se dia = 1
        break;
}
```

---

## ✅ Resumo do Bloco 3

Neste bloco você aprendeu:

- ✅ Estrutura `if` para decisões simples
- ✅ Estrutura `if-else` para alternativas
- ✅ Estrutura `else if` para múltiplas condições
- ✅ Operador ternário para decisões compactas
- ✅ Estrutura `switch-case` para múltiplas opções
- ✅ Quando usar cada estrutura condicional

---

## 🎯 Pontos-Chave para Memorizar

1. **Condições devem ser `boolean`** (`true` ou `false`)
2. **Ordem importa** em `else if` (mais específico primeiro)
3. **Sempre use `break`** em `switch` (exceto fall-through intencional)
4. **Use `==` para comparar**, não `=`
5. **Operador ternário:** para decisões simples de uma linha
6. **`switch`:** para comparar uma variável com múltiplos valores exatos

---

## ➡️ Próximos Passos

No próximo bloco você vai aprender:

- Estruturas de repetição (`while`, `do-while`, `for`)
- Loop `for-each` para arrays
- Palavras-chave `break` e `continue`
- Conceito de escopo de variáveis
- Como combinar loops com condicionais

---

> 💭 *"Condicionais dão ao programa o poder de pensar e decidir."*
