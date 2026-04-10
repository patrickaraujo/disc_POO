# Aula 06 — Fundamentos de OO: Relacionamentos entre Classes

## 🎯 Problematização: O sistema de uma biblioteca

Imagine que você precisa criar um sistema para gerenciar uma biblioteca. Você já sabe criar classes individuais: `Livro`, `Usuario`, `Emprestimo`. Mas surge a pergunta:

**Como um `Usuario` "possui" uma lista de `Emprestimo`?**  
**Como um `Emprestimo` "referencia" um `Livro` e um `Usuario`?**  
**Se eu deletar um `Usuario`, os `Emprestimo` devem ser deletados também?**

Essas perguntas não são sobre sintaxe — são sobre **relacionamentos entre classes**. Até agora, criamos classes isoladas. Nesta aula, aprenderemos como elas se **conectam** para formar sistemas complexos.

### O problema real

```java
// Você sabe fazer isso (classes isoladas):
Livro livro1 = new Livro();
Usuario joao = new Usuario();

// Mas como fazer isso?
// "João pegou emprestado o livro 'Clean Code'"
// Onde guardar essa relação?
// Como representar que o empréstimo PRECISA de um livro e um usuário?
```

### Como os relacionamentos resolvem isso

Ao final desta aula, você será capaz de escrever:

```java
Livro livro = new Livro("Clean Code", "Robert Martin");
Usuario joao = new Usuario("João Silva");

// Empréstimo CONTÉM referências a livro e usuário
Emprestimo emp = new Emprestimo(livro, joao, "15/03/2024");

// E pode acessar informações através dessas relações
System.out.println("Quem pegou? " + emp.getUsuario().getNome());
System.out.println("Qual livro? " + emp.getLivro().getTitulo());
```

**Isso é o poder dos relacionamentos entre classes.**

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → Classes e Objetos
Aula 04 → Encapsulamento (private, getters, setters)
Aula 05 → Construtores e sobrecarga
Aula 06 → ★ Você está aqui: Relacionamentos entre Classes
Aula 07 → Herança (extends, super)
```

A Aula 05 terminou com classes bem encapsuladas e construtores robustos. A Aula 06 responde à pergunta: **como essas classes se conectam para formar sistemas complexos?**

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Explicar os três tipos de relacionamento: **Associação**, **Agregação** e **Composição**
- [ ] Implementar o relacionamento "tem um" (has-a) em código Java
- [ ] Diferenciar agregação de composição através do **ciclo de vida dos objetos**
- [ ] Modelar sistemas do mundo real usando relacionamentos adequados
- [ ] Entender o impacto dos relacionamentos na arquitetura do sistema

---

## Organização da Aula

| Bloco | Tema | Tipo | Tempo estimado |
|-------|------|------|----------------|
| [Bloco 1](./Bloco1/README.md) | Conceito de Associação — "conhece um" | Conceitual + codificação guiada | 40–50 min |
| [Bloco 2–4](./Bloco2-4/README.md) | Agregação, Composição e Integração | Codificação guiada + exercício autônomo | 50–60 min |

---

## Dinâmica da aula

Cada bloco segue o formato:

1. **Explicação breve** do conceito com analogia do mundo real (10 min)
2. **Exercício guiado** — professor codifica junto com os alunos (1 por bloco)
3. **Exercício autônomo** — alunos resolvem com orientação do professor (1 por bloco)

**Diferencial desta aula:** Começamos sempre com um **problema do mundo real**, depois modelamos a solução usando relacionamentos.

---

## Como estudar esta aula

1. **Bloco 1:** Entenda a diferença entre criar objetos isolados vs. objetos conectados
2. **Bloco 2–4:** Pratique agregação e composição, e integre os três tipos de relacionamento em um exercício completo

💡 **Dica importante:** Desenhe diagramas! Relacionamentos são muito mais fáceis de entender visualmente.

---

## O que NÃO será abordado ainda

Para não sobrecarregar, os tópicos abaixo ficam para as próximas aulas:

- **Herança** (extends, super) → Aula 07
- **Polimorfismo** (@Override) → Aula 08
- **Interfaces e contratos** → Aula 10
- **Coleções** (ArrayList, HashMap) → Aula 12

Nesta aula, usaremos apenas **arrays simples** quando precisar de múltiplos objetos. ArrayList virá depois.

---

## Conceitos-chave que você vai dominar

### 1. Associação
> "Um objeto **conhece** outro objeto e pode se comunicar com ele."

**Analogia:** Você tem o telefone de um amigo. Vocês se conhecem, mas não dependem um do outro para existir.

### 2. Agregação  
> "Um objeto **tem** outro objeto, mas ambos podem existir independentemente."

**Analogia:** Uma universidade tem alunos. Se a universidade fechar, os alunos continuam existindo em outras instituições.

### 3. Composição
> "Um objeto **é composto de** outro objeto. Se o todo morre, as partes também."

**Analogia:** Uma casa tem quartos. Se a casa é demolida, os quartos deixam de existir.

---

## Resultado esperado ao final da Aula 06

Ao terminar, você deve conseguir:

✅ Ler código e identificar: "Isso é agregação" ou "Isso é composição"  
✅ Modelar um sistema real e decidir qual tipo de relacionamento usar  
✅ Escrever classes que se relacionam de forma coerente  
✅ Explicar por que escolheu agregação ou composição em um design  

E, principalmente, responder a pergunta inicial:

> **"Como representar que um Empréstimo precisa de um Livro e um Usuário?"**

Resposta: **Composição!** O empréstimo não existe sem livro e usuário. Se deleto o empréstimo, a relação acaba — mas livro e usuário continuam existindo.

---

## Conexão com o problema da biblioteca

Ao final da aula, você terá implementado:

```java
// Sistema de Biblioteca completo
public class SistemaBiblioteca {
    public static void main(String[] args) {
        // Objetos independentes (podem existir sozinhos)
        Livro livro = new Livro("Clean Code", "Robert Martin");
        Usuario joao = new Usuario("João Silva", "12345678900");
        
        // Empréstimo DEPENDE de livro e usuário (composição)
        Emprestimo emp = new Emprestimo(livro, joao, "15/03/2024");
        
        // Usuário TEM empréstimos (agregação)
        joao.adicionarEmprestimo(emp);
        
        // Navegação através dos relacionamentos
        System.out.println("Empréstimos de " + joao.getNome() + ":");
        for (Emprestimo e : joao.getEmprestimos()) {
            System.out.println("- " + e.getLivro().getTitulo());
        }
    }
}
```

**Esse código resolve o problema inicial — e você vai construí-lo do zero nesta aula!**

---

## 🎯 Vamos começar!

Clique em [Bloco 1](./Bloco1/README.md) para iniciar com o conceito de **Associação**.
