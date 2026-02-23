# Aula 03 — Fundamentos de OO: O que é uma Classe e um Objeto?

Nesta aula, iniciamos a transição da programação procedural para a orientação a objetos em Java. O foco é compreender como problemas do mundo real podem ser representados por **classes** (moldes) e **objetos** (instâncias), organizando dados (atributos) e comportamentos (métodos) em uma estrutura mais clara e escalável.

Ao final, você será capaz de criar suas primeiras classes em Java, instanciar objetos com `new` e executar métodos em exemplos práticos como `Pessoa` e `ContaBancaria`.

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → ★ Você está aqui: Classes e Objetos
Aula 04 → Encapsulamento (private, getters, setters)
Aula 05 → Construtores e sobrecarga
```

A Aula 02 terminou com código solto dentro do `main`. A Aula 03 responde à pergunta: **como organizar esse código de forma que ele represente entidades do problema?**

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Explicar a diferença entre o paradigma procedural e o orientado a objetos
- [ ] Entender **classe** como molde/modelo e **objeto** como instância concreta
- [ ] Declarar atributos (estado) e métodos (comportamento) em uma classe Java
- [ ] Criar objetos com a palavra-chave `new`
- [ ] Acessar atributos e chamar métodos de um objeto
- [ ] Perceber que dois objetos da mesma classe têm **estados independentes**

---

## Organização da Aula

| Bloco | Tema | Tipo | Tempo estimado |
|-------|------|------|----------------|
| [Bloco 1](./Bloco1/README.md) | Paradigma procedural vs. OO — conceitos | Conceitual + atividade sem código | 40–50 min |
| [Bloco 2](./Bloco2/README.md) | Primeira classe em Java — sintaxe e exemplos | Sintático + exemplo `Pessoa` | 50–60 min |
| [Bloco 3](./Bloco3/README.md) | Aplicação prática com múltiplos objetos | Prático + exercícios `ContaBancaria` | 50–60 min |

---

## Como estudar esta aula

1. Leia o **Bloco 1** sem abrir o editor. O objetivo é construir o modelo mental antes da sintaxe.
2. No **Bloco 2**, abra o editor e acompanhe os exemplos digitando o código — não copie e cole.
3. No **Bloco 3**, tente resolver os exercícios antes de consultar o gabarito.

---

## O que NÃO será abordado ainda

Para não sobrecarregar, os tópicos abaixo ficam para as próximas aulas:

- **Encapsulamento** (`private`, `public`, getters e setters) → Aula 04
- **Construtores personalizados e sobrecarga** → Aula 05
- **Relacionamentos entre classes** → Aula 06

Nesta aula, os atributos ficam sem modificador de acesso de forma intencional e temporária. Isso será corrigido e explicado na Aula 04.

---

## Resultado esperado ao final da Aula 03

Ao terminar, você deve conseguir ler e escrever naturalmente frases como:

> *"Tenho uma **classe** `Pessoa`."*
> *"Criei um **objeto** `p1` com `new Pessoa()`."*
> *"`p1.nome` e `p1.idade` representam o **estado** do objeto."*
> *"`p1.apresentar()` é um **comportamento** do objeto."*
> *"Dois objetos da mesma classe têm **dados independentes**."*

Esse vocabulário e esse modelo mental são o alicerce de todo o restante do curso.
