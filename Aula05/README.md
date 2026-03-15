# Aula 05 — Fundamentos de OO: Construtores e Sobrecarga

Na Aula 04, aprendemos a proteger os atributos com `private` e a expor o acesso por getters e setters. Porém, para criar um objeto e deixá-lo em um estado válido, precisamos chamar vários setters em sequência — e nada garante que o programador não esqueça de chamar algum. Nesta aula, vamos resolver esse problema com **construtores**: métodos especiais que inicializam o objeto no momento da criação, e com **sobrecarga**, que permite criar múltiplas versões de um mesmo método ou construtor com parâmetros diferentes.

O foco desta aula é **prático**. Os conceitos são apresentados de forma breve e seguidos imediatamente de código para ser escrito em sala.

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → Classes e Objetos
Aula 04 → Encapsulamento (private, getters, setters)
Aula 05 → ★ Você está aqui: Construtores e Sobrecarga
Aula 06 → Relacionamentos entre classes
```

A Aula 04 terminou com objetos sendo configurados por chamadas sucessivas a setters. A Aula 05 responde à pergunta: **como garantir que o objeto já nasce em um estado válido, sem depender de o programador lembrar de chamar todos os setters?**

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Entender o papel do construtor padrão e por que ele existe
- [ ] Criar construtores personalizados que inicializam atributos obrigatórios
- [ ] Usar a palavra-chave `this` para diferenciar atributos de parâmetros e para chamar outro construtor
- [ ] Aplicar sobrecarga em construtores e métodos (mesmo nome, parâmetros diferentes)
- [ ] Definir regras para parâmetros obrigatórios e opcionais em construtores

---

## Organização da Aula

| Bloco | Tema | Tipo | Tempo estimado |
|-------|------|------|----------------|
| [Bloco 1](./Bloco1/README.md) | O problema da inicialização manual e o construtor padrão | Conceito breve + codificação guiada | 40–50 min |
| [Bloco 2](./Bloco2/README.md) | Construtores personalizados e a palavra-chave `this` | Codificação guiada + exercícios | 50–60 min |
| [Bloco 3](./Bloco3/README.md) | Sobrecarga de métodos e construtores | Codificação guiada + exercícios | 50–60 min |
| [Bloco 4](./Bloco4/README.md) | Regras, boas práticas e consolidação | Discussão breve + exercícios de consolidação | 40–50 min |

---

## Dinâmica da aula

Cada bloco segue o formato:

1. **Explicação breve** do conceito (5–10 min)
2. **Exercícios guiados** — professor codifica junto com os alunos (2 por bloco)
3. **Exercícios autônomos** — alunos resolvem com orientação do professor (2 por bloco)

---

## O que NÃO será abordado ainda

- **Relacionamentos entre classes** (associação, agregação, composição) → Aula 06
- **Herança e o uso de `super` em construtores** → Aula 07
- **Construtores em classes abstratas** → Aula 09

---

## Resultado esperado ao final da Aula 05

Ao terminar, você deve conseguir ler e escrever naturalmente frases como:

> *"O construtor recebe nome e saldo — o objeto já nasce pronto."*
> *"`this.nome = nome` diferencia o atributo do parâmetro."*
> *"Tenho dois construtores: um com todos os dados e outro só com o nome."*
> *"O método `calcular()` tem duas versões — uma com desconto e outra sem."*
> *"`this(nome, 0.0)` chama o outro construtor da mesma classe."*

Esse padrão será usado em **todas as classes** daqui em diante no curso.
