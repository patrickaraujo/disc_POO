# Aula 04 — Fundamentos de OO: Encapsulamento

Na Aula 03, criamos classes com atributos acessíveis diretamente — qualquer código externo podia ler e alterar o estado dos objetos sem restrição. Nesta aula, vamos entender por que isso é um problema e como resolvê-lo com **encapsulamento**: proteger os atributos com `private` e expor o acesso controlado por meio de **getters** e **setters**.

O foco desta aula é **prático**. Os conceitos são apresentados de forma breve e seguidos imediatamente de código para ser escrito em sala.

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → Classes e Objetos
Aula 04 → ★ Você está aqui: Encapsulamento
Aula 05 → Construtores e sobrecarga
```

A Aula 03 terminou com atributos sem modificador de acesso — acessíveis por qualquer classe. A Aula 04 responde à pergunta: **como proteger o estado de um objeto para que ele não fique inconsistente?**

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Aplicar os modificadores de acesso `private`, `public` e `protected`
- [ ] Implementar métodos getters e setters com validação
- [ ] Proteger o estado interno de um objeto contra alterações inválidas
- [ ] Seguir boas práticas de visibilidade em projetos Java
- [ ] Explicar por que o acesso direto aos atributos é perigoso

---

## Organização da Aula

| Bloco | Tema | Tipo | Tempo estimado |
|-------|------|------|----------------|
| [Bloco 1](./Bloco1/README.md) | O problema do acesso direto e modificadores de acesso | Conceito breve + codificação guiada | 40–50 min |
| [Bloco 2](./Bloco2/README.md) | Getters e Setters — acesso controlado | Codificação guiada + exercícios | 50–60 min |
| [Bloco 3](./Bloco3/README.md) | Proteção do estado e integridade dos dados | Codificação guiada + exercícios | 50–60 min |
| [Bloco 4](./Bloco4/README.md) | Boas práticas e consolidação | Discussão breve + exercícios de consolidação | 40–50 min |

---

## Dinâmica da aula

Cada bloco segue o formato:

1. **Explicação breve** do conceito (5–10 min)
2. **Exercícios guiados** — professor codifica junto com os alunos (2 por bloco)
3. **Exercícios autônomos** — alunos resolvem com orientação do professor (2 por bloco)

---

## O que NÃO será abordado ainda

- **Construtores personalizados** → Aula 05
- **A palavra-chave `this` em construtores** → Aula 05
- **Relacionamentos entre classes** → Aula 06

---

## Resultado esperado ao final da Aula 04

Ao terminar, você deve conseguir ler e escrever naturalmente frases como:

> *"Os atributos são `private` — ninguém acessa diretamente."*
> *"Para ler o nome, chame `getNome()`."*
> *"O setter valida antes de alterar — não aceita saldo negativo."*
> *"A classe expõe apenas o que é necessário — o resto fica escondido."*

Esse padrão será usado em **todas as classes** daqui em diante no curso.
