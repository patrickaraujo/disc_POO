# Bloco 1 — Paradigma Procedural vs. Orientado a Objetos

## Objetivos do Bloco

- Identificar os problemas de estrutura do paradigma procedural em sistemas maiores
- Compreender o paradigma OO como uma forma de pensar, não apenas uma sintaxe
- Entender **classe** como molde e **objeto** como instância
- Identificar atributos (estado) e métodos (comportamento) em exemplos do mundo real

---

## 1.1 De onde viemos: o paradigma procedural

Nas aulas anteriores, você escreveu código dentro do método `main`: declarou variáveis, usou `if`, `for`, leu entradas com `Scanner`. Esse estilo de programar — uma sequência de instruções que manipula dados — é chamado de **paradigma procedural**.

Veja um exemplo típico: um pequeno programa de conta bancária escrito de forma procedural.

```java
public class ProgramaBanco {
    public static void main(String[] args) {

        // Dados da conta espalhados como variáveis soltas
        String titular = "Ana";
        String numeroConta = "001-2";
        double saldo = 1000.0;

        // Lógica misturada diretamente no main
        double valorSaque = 200.0;
        if (valorSaque <= saldo) {
            saldo = saldo - valorSaque;
            System.out.println("Saque realizado. Saldo: " + saldo);
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }
}
```

Para um programa simples, esse código funciona perfeitamente. O problema aparece quando o sistema cresce.

---

## 1.2 O problema não é a lógica — é a estrutura

> Você já encontrou essa discussão na **Atividade 2 do Bloco 1 da Aula 01**. Este é o momento de entendê-la na prática.

Imagine que o banco agora precisa gerenciar **100 contas** diferentes. O que acontece com o código procedural?

- As variáveis se multiplicam (`saldo1`, `saldo2`, ..., `saldo100`) ou viram arrays que se misturam
- A lógica de saque/depósito precisa ser repetida ou adaptada para cada conta
- Corrigir uma regra (ex.: cobrar taxa no saque) exige mexer em vários lugares
- Um novo programador no projeto leva horas para entender quem é responsável por quê

**O problema não é que o código está errado. É que a estrutura não escala.**

A Orientação a Objetos surge como resposta direta a esse problema.

---

## 1.3 A virada: pensar em objetos

No paradigma orientado a objetos, em vez de pensar em "sequência de instruções que manipula dados soltos", passamos a pensar em **entidades que têm estado e comportamento próprios**.

| Paradigma Procedural | Paradigma Orientado a Objetos |
|---|---|
| Dados e lógica separados | Dados e lógica encapsulados juntos |
| Programa = sequência de instruções | Programa = objetos colaborando entre si |
| Foco em *o que fazer* | Foco em *quem faz o quê* |
| Difícil de escalar | Projetado para crescer com organização |

A ideia central é simples: **cada entidade do problema vira um objeto no código**, e esse objeto carrega seus próprios dados e sabe o que fazer com eles.

> 🎬 **Vídeo recomendado:** [Programação Orientada a Objetos | Explicação Simples](https://www.youtube.com/watch?v=pbb0jzXt_xA) (3min 44s)
> Uma visão geral do paradigma OO de forma simples e didática, com exemplos. Assista antes de continuar — vai reforçar o modelo mental desta seção.

---

## 1.4 Classe: o molde

Uma **classe** é a definição de como um tipo de objeto será. Ela descreve:

- Quais **dados** esse tipo de objeto carrega (chamados de **atributos**)
- Quais **ações** esse tipo de objeto pode realizar (chamados de **métodos**)

A classe em si **não é um objeto**. Ela é o molde a partir do qual objetos serão criados.

**Analogias:**

| Molde (Classe) | Instância (Objeto) |
|---|---|
| Planta baixa de uma casa | A casa construída |
| Forma de biscoito | O biscoito assado |
| Receita de bolo | O bolo pronto |
| Definição de "Conta Bancária" | A conta da Ana, a conta do Carlos |

A planta baixa define que a casa terá 2 quartos, 1 banheiro e cozinha. Cada casa construída a partir dessa planta é independente: pintar uma casa não afeta a outra.

---

## 1.5 Objeto: a instância

Um **objeto** é uma instância concreta criada a partir de uma classe. Quando você cria um objeto, você está construindo uma "casa" a partir da "planta".

Cada objeto:

- Tem sua **própria cópia** dos atributos definidos na classe
- Pode executar os métodos definidos na classe
- É **independente** dos outros objetos da mesma classe

Exemplo: se a classe é `ContaBancaria`, então:

- `conta1` → titular: "Ana", saldo: R$ 1000,00
- `conta2` → titular: "Carlos", saldo: R$ 500,00

Alterar o saldo de `conta1` não afeta `conta2`. Elas são objetos diferentes, com estados independentes.

---

## 1.6 Atributos e Métodos

Toda classe é composta de duas coisas:

**Atributos** representam o **estado** do objeto — as informações que ele carrega.

- Exemplos em `ContaBancaria`: `titular`, `numeroConta`, `saldo`
- Exemplos em `Pessoa`: `nome`, `idade`, `cpf`
- Exemplos em `Produto`: `nome`, `preco`, `quantidadeEstoque`

**Métodos** representam o **comportamento** do objeto — o que ele sabe fazer, geralmente sobre seus próprios atributos.

- Exemplos em `ContaBancaria`: `depositar()`, `sacar()`, `exibirSaldo()`
- Exemplos em `Pessoa`: `apresentar()`, `fazerAniversario()`
- Exemplos em `Produto`: `aplicarDesconto()`, `reporEstoque()`

> **Regra de ouro:** os dados e as regras que os governam ficam juntos, dentro da classe. Isso é o oposto do problema do código procedural.

---

## Atividade — Modelagem sem código

Antes de escrever qualquer código, pratique o raciocínio OO em linguagem natural.

Escolha **um** dos contextos abaixo e responda para **duas classes** diferentes:

> **Contextos:** Biblioteca | Escola | Loja | Clínica médica | Estacionamento

Para cada classe identificada, preencha a tabela:

| | Classe 1 | Classe 2 |
|---|---|---|
| **Nome da classe** | | |
| **Atributo 1** | | |
| **Atributo 2** | | |
| **Atributo 3** | | |
| **Método 1** | | |
| **Método 2** | | |

**Perguntas para guiar sua análise:**
- Quais são as entidades principais desse contexto?
- O que cada entidade *sabe sobre si mesma*? (→ atributos)
- O que cada entidade *sabe fazer*? (→ métodos)

**Exemplo resolvido (contexto: biblioteca):**

| | Classe 1 | Classe 2 |
|---|---|---|
| **Nome da classe** | `Livro` | `Emprestimo` |
| **Atributo 1** | `titulo` | `dataInicio` |
| **Atributo 2** | `autor` | `prazoEmDias` |
| **Atributo 3** | `disponivel` | `nomeUsuario` |
| **Método 1** | `exibirInformacoes()` | `verificarAtraso()` |
| **Método 2** | `marcarComoEmprestado()` | `calcularMulta()` |

Note que cada classe tem **responsabilidades claras**. Um `Livro` sabe se está disponível. Um `Emprestimo` sabe se está em atraso. Essa divisão de responsabilidades é a essência da OO.

---

## Resumo do Bloco 1

- O paradigma procedural separa dados e lógica, o que não escala bem em sistemas maiores
- O paradigma OO organiza o código em objetos que combinam **estado + comportamento**
- **Classe** é o molde que define a estrutura; **objeto** é a instância concreta criada a partir dele
- **Atributos** = o que o objeto sabe (estado); **Métodos** = o que o objeto faz (comportamento)
- OO não é uma linguagem — é uma forma de pensar, que Java implementa

### Recursos complementares

| Tipo | Recurso |
|------|---------|
| 🎬 Vídeo | [Programação Orientada a Objetos \| Explicação Simples](https://www.youtube.com/watch?v=pbb0jzXt_xA) — 3min 44s |
---

**Próximo passo →** [Bloco 2](../Bloco2/README.md): agora que o modelo mental está construído, vamos ver como tudo isso vira código Java.
