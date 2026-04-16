# Aula 07 — Fundamentos de OO: Herança

## 🎯 Problematização: Sistema de RH com diferentes tipos de funcionários

Imagine que você está desenvolvendo um sistema de Recursos Humanos para uma empresa. A empresa tem três tipos de funcionários:

- **Funcionários CLT:** salário fixo mensal
- **Funcionários Horistas:** pagos por hora trabalhada
- **Funcionários Comissionados:** salário base + comissão sobre vendas

Você sabe criar classes, mas começa a perceber um padrão:

```java
// Funcionário CLT
public class FuncionarioCLT {
    private String nome;
    private String cpf;
    private double salarioBase;
    
    public double calcularSalario() {
        return salarioBase;
    }
    
    public void exibirDados() {
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        // ...
    }
}

// Funcionário Horista
public class FuncionarioHorista {
    private String nome;        // ← Repetido!
    private String cpf;         // ← Repetido!
    private double valorHora;
    private int horasTrabalhadas;
    
    public double calcularSalario() {  // ← Assinatura repetida!
        return valorHora * horasTrabalhadas;
    }
    
    public void exibirDados() {  // ← Método repetido!
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        // ...
    }
}

// Funcionário Comissionado
public class FuncionarioComissionado {
    private String nome;        // ← Repetido de novo!
    private String cpf;         // ← Repetido de novo!
    private double salarioBase;
    private double totalVendas;
    private double taxaComissao;
    
    // Mais repetição...
}
```

**Problemas:**
1. ❌ Código duplicado (`nome`, `cpf`, `exibirDados()`)
2. ❌ Se mudar algo comum (ex: adicionar `email`), precisa alterar **3 classes**
3. ❌ Não há forma de tratar "qualquer funcionário" genericamente
4. ❌ Viola o princípio DRY (Don't Repeat Yourself)

### Como a Herança resolve isso

Com herança, você extrai o que é **comum** para uma **superclasse** e mantém apenas o que é **específico** nas subclasses:

```java
// SUPERCLASSE (o que é comum a todos)
public class Funcionario {
    protected String nome;
    protected String cpf;
    
    public Funcionario(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
    
    public void exibirDados() {
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
    }
    
    // Método que cada tipo implementa diferente
    public double calcularSalario() {
        return 0.0;  // Será sobrescrito
    }
}

// SUBCLASSE (apenas o que é específico)
public class FuncionarioCLT extends Funcionario {
    private double salarioBase;
    
    public FuncionarioCLT(String nome, String cpf, double salarioBase) {
        super(nome, cpf);  // Reutiliza construtor da superclasse
        this.salarioBase = salarioBase;
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase;
    }
}
```

**Vantagens:**
✅ Código comum em **um único lugar**  
✅ Mudanças comuns feitas **uma vez só**  
✅ Posso tratar todos como `Funcionario`  
✅ Reuso de código + especialização  

**Isso é o poder da Herança!**

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → Classes e Objetos
Aula 04 → Encapsulamento (private, getters, setters)
Aula 05 → Construtores e sobrecarga
Aula 06 → Relacionamentos entre Classes
Aula 07 → ★ Você está aqui: Herança (extends, super)
Aula 08 → Polimorfismo (@Override)
```

A Aula 06 terminou com classes conectadas através de relacionamentos. A Aula 07 responde: **como evitar duplicação de código quando classes compartilham características comuns?**

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Identificar quando usar herança (relação "é um")
- [ ] Criar hierarquias de classes com `extends`
- [ ] Utilizar `super` para acessar membros da superclasse
- [ ] Sobrescrever métodos para especializar comportamento
- [ ] Entender `protected` e quando usá-lo
- [ ] Reconhecer os **perigos** do mau uso da herança

---

## Organização da Aula

| Bloco | Tema | Formato | Tempo estimado |
|-------|------|---------|----------------|
| [Bloco 1](./Bloco1/README.md) | Conceitos de Herança e Hierarquia | Explicação + exemplo simples | 50–60 min |
| [Bloco 2](./Bloco2/README.md) | Exercício Guiado Completo: Sistema de Veículos | Codificação guiada passo a passo | 90–120 min |
| [Bloco 3](./Bloco3/README.md) | Exercício Autônomo: Sistema de Produtos | Desenvolvimento independente | 90–120 min |

> **Observação:** Os Blocos 2 e 3 contêm exercícios substanciais que podem ocupar uma aula inteira cada um. O Bloco 1 foca em **entender** herança, enquanto os Blocos 2 e 3 focam em **aplicar** através de projetos completos.

---

## Dinâmica da aula

### Bloco 1 — Fundamentos (1ª aula: ~50 min)
1. Explicação de herança com exemplos visuais
2. Sintaxe `extends`, `super`, `@Override`
3. Exemplo simples: hierarquia Animal → Cachorro/Gato
4. Modificador `protected`

### Bloco 2 — Exercício Guiado (2ª aula: 90-120 min)
**Professor codifica junto com alunos:**
- Sistema completo de Veículos (Carro, Moto, Caminhão)
- Implementação passo a passo
- Discussão de decisões de design
- Testes e validação

### Bloco 3 — Exercício Autônomo (3ª aula: 90-120 min)
**Alunos desenvolvem com supervisão:**
- Sistema completo de Produtos (Livro, Eletrônico, Alimento)
- Requisitos detalhados fornecidos
- Professor circula tirando dúvidas
- Apresentação de soluções ao final

---

## Como estudar esta aula

1. **Bloco 1:** Foque em **entender** o conceito antes de decorar sintaxe
2. **Bloco 2:** **Acompanhe** o professor digitando junto (não copie/cole)
3. **Bloco 3:** **Pense primeiro**, depois code — planeje a hierarquia no papel

💡 **Dica crucial:** Desenhe a hierarquia de classes **ANTES** de codificar!

---

## O que NÃO será abordado ainda

- **Polimorfismo em profundidade** → Aula 08
- **Classes abstratas** → Aula 09
- **Interfaces** → Aula 10
- **Casting de tipos** → Aula 08

Nesta aula, o foco é no **mecanismo** da herança e no **reuso de código**.

---

## Conceitos-chave que você vai dominar

### 1. Relação "É Um" (is-a)
> Herança modela relacionamentos de **especialização**

**Exemplos:**
- Cachorro **é um** Animal ✅
- Carro **é um** Veículo ✅
- Aluno **é uma** Pessoa ✅

**Contra-exemplos:**
- Carro **tem um** Motor ❌ (isso é composição!)
- Pessoa **tem um** Endereço ❌ (isso é agregação!)

### 2. Superclasse e Subclasse
- **Superclasse (ou classe pai):** contém características **comuns**
- **Subclasse (ou classe filha):** herda características + adiciona **específicas**

### 3. Palavra-chave `extends`
```java
public class Subclasse extends Superclasse {
    // Herda tudo da Superclasse
    // Pode adicionar novos membros
    // Pode sobrescrever métodos
}
```

### 4. Palavra-chave `super`
```java
super.metodo();         // Chama método da superclasse
super(argumentos);      // Chama construtor da superclasse
```

### 5. Modificador `protected`
- `private` → só a própria classe
- `protected` → a própria classe **+ subclasses**
- `public` → qualquer classe

### 6. Sobrescrita de métodos (`@Override`)
```java
@Override
public void metodo() {
    // Nova implementação na subclasse
}
```

---

## Quando usar Herança?

✅ **Use herança quando:**
- Existe clara relação "**é um**" (is-a)
- Há código **comum** entre classes
- As classes formam uma **hierarquia natural**
- Você quer **especializar** comportamento

❌ **NÃO use herança quando:**
- A relação é "**tem um**" (use composição!)
- Você só quer **reutilizar código** (pode ser composição)
- Não há relação semântica clara
- Vai criar hierarquia com mais de 3-4 níveis

### Exemplo de mau uso

```java
// ❌ ERRADO - Herança usada só para reutilizar código
public class Stack extends ArrayList {
    // Stack NÃO É UM ArrayList!
    // Deveria usar composição!
}

// ✅ CORRETO - Composição
public class Stack {
    private ArrayList elementos = new ArrayList();
    // ...
}
```

---

## Resultado esperado ao final da Aula 07

Ao terminar, você deve conseguir:

✅ Identificar oportunidades de usar herança em sistemas reais  
✅ Criar hierarquias de classes coesas  
✅ Usar `extends` e `super` corretamente  
✅ Sobrescrever métodos para especializar comportamento  
✅ Explicar por que escolheu herança vs. composição  
✅ Reconhecer quando **NÃO** usar herança  

E, principalmente, resolver o problema inicial:

> **"Como evitar código duplicado em FuncionarioCLT, FuncionarioHorista e FuncionarioComissionado?"**

**Resposta:** Criar uma superclasse `Funcionario` com atributos e métodos comuns, e usar `extends` para criar as especializações!

---

## Visualizando a hierarquia final

```
                    ┌─────────────────┐
                    │   Funcionario   │
                    ├─────────────────┤
                    │ - nome          │
                    │ - cpf           │
                    ├─────────────────┤
                    │ + calcularSalario() │
                    │ + exibirDados() │
                    └────────┬────────┘
                             │
                ┌────────────┼────────────┐
                │            │            │
        ┌───────▼──────┐ ┌──▼─────────┐ ┌▼──────────────┐
        │FuncionarioCLT│ │FuncionarioH│ │Funcionario    │
        │              │ │orista      │ │Comissionado   │
        ├──────────────┤ ├────────────┤ ├───────────────┤
        │- salarioBase │ │- valorHora │ │- salarioBase  │
        │              │ │- horasTrab │ │- totalVendas  │
        │              │ │            │ │- taxaComissao │
        ├──────────────┤ ├────────────┤ ├───────────────┤
        │@Override     │ │@Override   │ │@Override      │
        │calcularSal..│ │calcularSal │ │calcularSalario│
        └──────────────┘ └────────────┘ └───────────────┘
```

**Leia como:** "FuncionarioCLT **é um** Funcionario, FuncionarioHorista **é um** Funcionario..."

---

## 🎯 Vamos começar!

Clique em [Bloco 1](./Bloco1/README.md) para iniciar com os **conceitos fundamentais de Herança**.
