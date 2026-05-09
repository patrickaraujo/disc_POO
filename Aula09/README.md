# Aula 09 — Fundamentos de OO: Classes Abstratas

## 🎯 Problematização: O "Funcionário Genérico" não existe

Lembra do sistema de Folha de Pagamento da Aula 08? Você criou a hierarquia:

```
                    ┌─────────────────┐
                    │   Funcionario   │
                    └────────┬────────┘
                             │
        ┌────────────────────┼─────────────────┐
        │                    │                 │
   ┌────▼────────┐ ┌─────────▼────────┐ ┌──────▼────────────────┐
   │FuncionarioCLT│ │FuncionarioHorista│ │FuncionarioComissionado│
   └─────────────┘ └──────────────────┘ └───────────────────────┘
```

E na superclasse `Funcionario`, você escreveu este método:

```java
public class Funcionario {
    // ...

    public double calcularSalario() {
        return 0.0;  // ← Será sobrescrito pelas subclasses
    }
}
```

Aparentemente está tudo certo. **Mas três coisas estão errando em silêncio.**

### Problema 1: Posso criar um "Funcionario" que não é nada

```java
Funcionario f = new Funcionario("João", "111.111.111-11", "M001");
f.calcularSalario();  // Retorna R$ 0,00 — e isso é absurdo
```

Esse objeto **não é** um CLT, **não é** um Horista, **não é** um Comissionado. É só um... funcionário sem categoria? **Isso não existe na realidade.** Todo funcionário **PRECISA** ser de algum tipo.

### Problema 2: Promessa que ninguém é obrigado a cumprir

Imagine que um colega seu cria uma nova subclasse e **esquece** de sobrescrever `calcularSalario()`:

```java
public class FuncionarioTerceirizado extends Funcionario {
    private String empresaContratante;
    private double valorContrato;

    public FuncionarioTerceirizado(String nome, String cpf, String matricula,
                                    String empresaContratante, double valorContrato) {
        super(nome, cpf, matricula);
        this.empresaContratante = empresaContratante;
        this.valorContrato = valorContrato;
    }

    // ❌ ESQUECEU de sobrescrever calcularSalario()!
}
```

Resultado:

```java
FuncionarioTerceirizado t = new FuncionarioTerceirizado("Maria", "...", "M005",
                                                         "Acme", 8000.0);
t.calcularSalario();  // Retorna R$ 0,00 — herdado da superclasse!
```

**O compilador NÃO reclama.** O programa compila e roda. Mas Maria recebe R$ 0,00 todo mês. O bug só aparece quando alguém olhar o contracheque dela.

### Problema 3: Implementação "falsa" só para satisfazer o compilador

O `return 0.0;` em `Funcionario.calcularSalario()` não tem **nenhum significado**. Ele só existe porque o compilador exige um corpo de método. É uma **mentira sintática**: você está dizendo "calcular salário de Funcionario é zero", mas isso não é verdade — não existe "calcular salário de Funcionario", **só existe calcular salário de cada tipo específico**.

### Como Classes Abstratas resolvem tudo isso

Java oferece um mecanismo formal para dizer:

> *"Esta classe representa um conceito abstrato. Não pode ser instanciada diretamente. E estes métodos aqui são uma promessa que toda subclasse concreta DEVE cumprir."*

```java
// ✅ Com classe abstrata
public abstract class Funcionario {
    protected String nome;
    protected String cpf;
    protected String matricula;

    public Funcionario(String nome, String cpf, String matricula) {
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
    }

    // Método ABSTRATO: sem corpo, terminado em ponto-e-vírgula
    public abstract double calcularSalario();

    // Métodos concretos podem coexistir
    public String getNome() { return nome; }
    // ...
}
```

**Resultado:**

```java
// ❌ Erro de COMPILAÇÃO — não pode instanciar abstrata
Funcionario f = new Funcionario(...);

// ❌ Erro de COMPILAÇÃO — FuncionarioTerceirizado precisa implementar calcularSalario()
public class FuncionarioTerceirizado extends Funcionario {
    // sem calcularSalario()
}
```

**Vantagens:**
✅ O compilador **proíbe** instanciar a classe genérica
✅ O compilador **obriga** subclasses concretas a implementar os métodos abstratos
✅ Não preciso mais inventar `return 0.0` falso
✅ A intenção do design fica **explícita** no código
✅ Bugs como o "salário zero da Maria" são impossíveis

**Isso é o poder das Classes Abstratas!**

> **A pergunta-chave da Aula 09:** Como **forçar** subclasses a implementar comportamentos específicos, sem que a superclasse precise inventar implementações falsas? E como aproveitar isso para criar **fluxos reutilizáveis** (Template Method)?

---

## Onde estamos na trilha do curso

```
Aula 01 → Ambiente, JVM, primeiro programa
Aula 02 → Variáveis, operadores, condicionais, loops
Aula 03 → Classes e Objetos
Aula 04 → Encapsulamento (private, getters, setters)
Aula 05 → Construtores e sobrecarga
Aula 06 → Relacionamentos entre Classes
Aula 07 → Herança (extends, super)
Aula 08 → Polimorfismo (@Override em runtime)
Aula 09 → ★ Você está aqui: Classes Abstratas (abstract, métodos abstratos)
Aula 10 → Interfaces (implements)
```

A Aula 07 te deu a **estrutura**. A Aula 08 te deu **flexibilidade em runtime**. A Aula 09 te dá **rigor de contrato**: você passa a expressar no código o que **deve** e o que **não pode** ser feito.

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Identificar quando uma classe deve ser **abstrata**
- [ ] Declarar classes abstratas com a palavra-chave `abstract`
- [ ] Criar **métodos abstratos** (sem corpo)
- [ ] Misturar métodos abstratos e concretos na mesma classe
- [ ] Entender por que **não se pode instanciar** classes abstratas
- [ ] Implementar o padrão **Template Method**
- [ ] Distinguir classe abstrata de classe concreta de interface (preview Aula 10)

---

## Organização da Aula

| Bloco | Tema | Formato | Tempo estimado |
|-------|------|---------|----------------|
| [Bloco 1](./Bloco1/README.md) | Conceitos de Classes Abstratas + Template Method | Explicação + exemplo simples | 50–60 min |
| [Bloco 2](./Bloco2/README.md) | Exercício Guiado Completo: Sistema de Notificações | Codificação guiada passo a passo | 90–120 min |
| [Bloco 3](./Bloco3/README.md) | Exercício Autônomo: Sistema de Importação de Dados | Desenvolvimento independente | 90–120 min |

> **Observação:** Os Blocos 2 e 3 contêm exercícios substanciais que podem ocupar uma aula inteira cada um. O Bloco 1 foca em **entender** o mecanismo das classes abstratas (incluindo Template Method), enquanto os Blocos 2 e 3 focam em **aplicar** através de projetos completos.

---

## Dinâmica da aula

### Bloco 1 — Fundamentos (1ª aula: ~50 min)
1. Recapitulação: o problema da `Funcionario` da Aula 08
2. Sintaxe de `abstract class` e `abstract method`
3. Por que não se pode instanciar uma abstrata
4. Mistura de abstrato + concreto em uma mesma classe
5. **Template Method Pattern:** a aplicação mais elegante de classes abstratas
6. Exemplo simples: `FormaGeometrica` → `Circulo`, `Retangulo`, `Triangulo`
7. Preview: classe abstrata vs interface (Aula 10)

### Bloco 2 — Exercício Guiado (2ª aula: 90-120 min)
**Professor codifica junto com alunos:**
- Sistema completo de Notificações (SMS, Email, Push, WhatsApp)
- Hierarquia abstrata `Notificacao`
- Aplicação clara de **Template Method**: `enviar()` é o fluxo comum
- Métodos abstratos: `validarDestinatario()`, `formatarMensagem()`, `transmitir()`
- Discussão de decisões de design (o que é abstrato vs concreto)

### Bloco 3 — Exercício Autônomo (3ª aula: 90-120 min)
**Alunos desenvolvem com supervisão:**
- Sistema completo de Importação de Dados (CSV, JSON, XML, Excel)
- Requisitos detalhados fornecidos
- Professor circula tirando dúvidas
- Apresentação de soluções ao final

---

## Como estudar esta aula

1. **Bloco 1:** Foque em **entender quando** usar abstrata (não decore só a sintaxe)
2. **Bloco 2:** **Acompanhe** o professor digitando junto (não copie/cole)
3. **Bloco 3:** **Pense primeiro** no fluxo comum, depois nos pontos de variação

💡 **Dica crucial:** Classes abstratas brilham quando você consegue identificar um **fluxo comum com pontos de variação**. Esse é o **Template Method**.

---

## O que NÃO será abordado ainda

- **Interfaces** (`interface`, `implements`, métodos default) → Aula 10
- **Herança múltipla** via interfaces → Aula 10
- **Classes anônimas e internas** → leitura complementar
- **Generics avançados** (`<T extends Forma>`) → aulas futuras

Nesta aula, o foco é no **mecanismo** de classes abstratas e no **padrão Template Method** que elas viabilizam.

---

## Conceitos-chave que você vai dominar

### 1. Classe abstrata

```java
public abstract class Forma {
    // ...
}
```

> Uma classe abstrata **não pode ser instanciada** diretamente. Ela existe para ser **estendida**.

### 2. Método abstrato

```java
public abstract double calcularArea();   // Sem corpo, com ponto-e-vírgula
```

> Um método abstrato é uma **promessa** sem implementação. Toda subclasse concreta **DEVE** implementá-lo.

### 3. Mistura abstrato + concreto

```java
public abstract class Forma {
    protected String cor;                // Atributo concreto

    public Forma(String cor) {           // Construtor concreto
        this.cor = cor;
    }

    public abstract double calcularArea();  // Abstrato (promessa)

    public void exibir() {                  // Concreto (implementação real)
        System.out.println("Cor: " + cor);
        System.out.println("Área: " + calcularArea());  // Polimorfismo!
    }
}
```

> Uma classe abstrata pode ter atributos, construtores, métodos concretos **e** métodos abstratos.

### 4. Template Method (o padrão estrela)

```java
public abstract class Importador {

    // ESTE é o Template Method (concreto, geralmente final)
    public final void importar() {
        abrirArquivo();      // 1
        ler();               // 2
        validar();           // 3
        salvarBanco();       // 4
        fecharArquivo();     // 5
    }

    // Pontos de variação (abstratos)
    protected abstract void abrirArquivo();
    protected abstract void ler();
    protected abstract void validar();

    // Passos comuns (concretos)
    protected void salvarBanco() { /* ... */ }
    protected void fecharArquivo() { /* ... */ }
}
```

> **Template Method:** define um **esqueleto algorítmico** na superclasse, permitindo que subclasses preencham os passos específicos. Uma das aplicações mais belas de classes abstratas.

### 5. Classes concretas vs abstratas

| Aspecto | Concreta | Abstrata |
|---------|----------|----------|
| Pode ser instanciada? | ✅ Sim | ❌ Não |
| Pode ter métodos abstratos? | ❌ Não | ✅ Sim |
| Pode ter métodos concretos? | ✅ Sim | ✅ Sim |
| Pode ter construtores? | ✅ Sim | ✅ Sim (chamados pelo `super`) |
| Para que serve? | Criar objetos | Servir de base para outras classes |

### 6. Preview: Abstrata vs Interface (Aula 10)

```java
abstract class Animal { ... }       // Tem ESTADO + comportamento parcial
interface Voador { void voar(); }   // Só comportamento (na sua forma clássica)
```

Voltaremos a essa diferença na Aula 10. Por enquanto: **classes abstratas têm estado e implementação parcial; interfaces, classicamente, só declaram contratos.**

---

## Quando usar Classe Abstrata?

✅ **Use classe abstrata quando:**
- Várias classes compartilham **estado** + **comportamento parcial**
- Você tem um **fluxo comum** com **pontos de variação** (Template Method)
- Quer **proibir** a instanciação direta da classe genérica
- Quer **forçar** subclasses a implementar certos métodos
- A relação é claramente "**é um**" (herança ainda se aplica)

❌ **NÃO use classe abstrata quando:**
- Não há código comum entre as subclasses (use interface)
- Você precisa de "herança múltipla" de comportamento (use interfaces)
- A classe pode existir por si só (use concreta)
- Você só quer agrupar constantes (use enum ou classe utilitária)

### Exemplo de mau uso

```java
// ❌ ERRADO - sem nenhum atributo nem método concreto
public abstract class Calculavel {
    public abstract double calcular();
}

// ✅ MELHOR - isso é um trabalho para interface (Aula 10)
public interface Calculavel {
    double calcular();
}
```

---

## Resultado esperado ao final da Aula 09

Ao terminar, você deve conseguir:

✅ Reconhecer quando uma classe **deve** ser abstrata
✅ Declarar `abstract class` e `abstract method` corretamente
✅ Implementar o **Template Method Pattern** em sistemas reais
✅ Justificar por que escolheu abstrata em vez de concreta
✅ Antecipar quando uma interface seria mais apropriada (Aula 10)
✅ Refatorar a hierarquia da Aula 08 transformando `Funcionario` em abstrata

E, principalmente, resolver o problema inicial:

> **"Como impedir que alguém crie um `new Funcionario(...)` genérico, e como garantir que toda nova subclasse implemente `calcularSalario()`?"**

**Resposta:** Marcar `Funcionario` como `abstract class` e `calcularSalario()` como `abstract method`. O compilador passa a **proibir** a instanciação direta e **exigir** a implementação em qualquer subclasse concreta.

---

## Visualizando a refatoração da Aula 08

### Antes (Aula 08)

```
   ┌─────────────────────┐
   │   Funcionario       │  ← Concreta (instanciável!)
   ├─────────────────────┤
   │ + calcularSalario() │  ← Implementação fake (return 0.0)
   └─────────┬───────────┘
             │
       (3 subclasses)
```

### Depois (Aula 09)

```
   ┌──────────────────────────────┐
   │   « abstract » Funcionario   │  ← NÃO instanciável
   ├──────────────────────────────┤
   │ + calcularSalario() : abstract │  ← Promessa sem corpo
   └─────────┬────────────────────┘
             │
       (3 subclasses concretas, OBRIGADAS a implementar)
```

**Leia como:** "Funcionario é um conceito abstrato. Não existe Funcionario sem categoria. Toda subclasse concreta deve dizer COMO calcula seu salário."

---

## 🎯 Vamos começar!

Clique em [Bloco 1](./Bloco1/README.md) para iniciar com os **conceitos fundamentais de Classes Abstratas**.
