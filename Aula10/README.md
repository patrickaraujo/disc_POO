# Aula 10 — OO Avançado em Java: Interfaces

## 🎯 Problematização: Quando uma classe é "várias coisas ao mesmo tempo"

Na Aula 09 você refatorou a `Funcionario` da Aula 08 transformando-a em **classe abstrata**:

```java
public abstract class Funcionario {
    protected String nome;
    protected String cpf;
    public abstract double calcularSalario();
}
```

E criou subclasses concretas:

```java
public class FuncionarioCLT       extends Funcionario { ... }
public class FuncionarioHorista   extends Funcionario { ... }
public class FuncionarioComissionado extends Funcionario { ... }
```

Está tudo lindo. **Até a empresa decidir criar três programas internos:**

1. **Programa de Líderes:** alguns funcionários (de qualquer tipo) podem se tornar **líderes de equipe**, com responsabilidades extras: avaliar pares, conduzir reuniões, dar feedback.
2. **Programa de Mentoria:** outros funcionários (de qualquer tipo) atuam como **mentores**: registram sessões com mentorados, dão notas, geram relatórios.
3. **Programa de Avaliadores Internos:** alguns funcionários (de qualquer tipo) podem **avaliar candidatos** em processos seletivos.

E aqui o seu modelo da Aula 09 começa a quebrar. **Por quê?**

### Tentativa ingênua: criar subclasses para cada combinação

```java
public class FuncionarioCLTLider extends FuncionarioCLT { ... }
public class FuncionarioCLTMentor extends FuncionarioCLT { ... }
public class FuncionarioCLTLiderMentor extends FuncionarioCLT { ... }
public class FuncionarioCLTAvaliador extends FuncionarioCLT { ... }
public class FuncionarioCLTLiderAvaliador extends FuncionarioCLT { ... }
public class FuncionarioCLTMentorAvaliador extends FuncionarioCLT { ... }
public class FuncionarioCLTLiderMentorAvaliador extends FuncionarioCLT { ... }
// ... e tudo isso repetido para Horista e Comissionado!
```

**Problema:** **explosão combinatória.** 3 tipos × 8 combinações = **24 classes**. Adicione um quarto programa e dobra para 48. Insustentável.

### Tentativa 2: herança múltipla

"E se eu pudesse fazer `class FuncionarioLiderMentor extends Funcionario, Lider, Mentor`?"

```java
public class FuncionarioCLTLider extends FuncionarioCLT, Lider {  // ❌
    // ...
}
```

**Resposta do compilador Java:** `'{' expected`. **Java não permite herança múltipla de classes.** Esta foi uma decisão de projeto da linguagem (para evitar o famoso "problema do diamante" do C++).

Então a Aula 09 te deixou em uma **bifurcação sem saída**:

- ✅ Você sabe forçar contrato (`abstract method`)
- ✅ Você sabe reaproveitar código entre classes relacionadas (`extends`)
- ❌ Mas você **não consegue** dizer "esta classe **TAMBÉM PODE** fazer X, Y e Z" sem entrar em explosão combinatória

### Como Interfaces resolvem isso

Java oferece um mecanismo separado para expressar **capacidades** ou **contratos de comportamento** que **atravessam** hierarquias:

```java
public interface Lider {
    void avaliarPar(Funcionario par, int nota);
    void conduzirReuniao(String pauta);
}

public interface Mentor {
    void registrarSessao(Funcionario mentorado, String anotacoes);
    double calcularMediaNotas();
}

public interface Avaliador {
    boolean avaliarCandidato(String nomeCandidato, int nota);
}
```

E agora **qualquer** funcionário pode "implementar" **quantas** interfaces fizerem sentido, **sem mudar a hierarquia principal**:

```java
public class FuncionarioCLT
       extends Funcionario
       implements Lider, Mentor, Avaliador {       // ★ 3 interfaces de uma vez

    // ... obrigada a implementar TODOS os métodos das 3 interfaces

    @Override
    public void avaliarPar(Funcionario par, int nota) { ... }

    @Override
    public void conduzirReuniao(String pauta) { ... }

    @Override
    public void registrarSessao(Funcionario mentorado, String anotacoes) { ... }

    @Override
    public double calcularMediaNotas() { ... }

    @Override
    public boolean avaliarCandidato(String nomeCandidato, int nota) { ... }
}
```

**Vantagens:**
✅ **Uma única classe**, **múltiplas capacidades**
✅ Hierarquia principal (`extends Funcionario`) **preservada**
✅ Capacidades **transversais** (qualquer tipo pode ter qualquer combinação)
✅ Polimorfismo **por capacidade**: `List<Lider>` aceita CLTs, Horistas, Comissionados que sejam líderes
✅ Adicionar novo programa (ex: `Treinador`) = **apenas uma nova interface** + classes que quiserem
✅ Sem explosão combinatória

**Isso é o poder das Interfaces!**

> **A pergunta-chave da Aula 10:** Como expressar **capacidades** que atravessam hierarquias, sem cair em explosão combinatória, e como aproveitar isso para criar **polimorfismo por contrato** independente da árvore de herança?

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
Aula 09 → Classes Abstratas (abstract, métodos abstratos, Template Method)
Aula 10 → ★ Você está aqui: Interfaces (implements, contratos puros, múltiplas interfaces)
```

A Aula 07 te deu **estrutura**. A 08 deu **flexibilidade em runtime**. A 09 deu **rigor de contrato com herança**. A 10 fecha a trilha: **contratos sem herança** — você passa a expressar capacidades independentes da árvore de classes.

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Entender o que é uma **interface** e como difere de uma classe abstrata
- [ ] Declarar interfaces com a palavra-chave `interface`
- [ ] Implementar interfaces com a palavra-chave `implements`
- [ ] Combinar **múltiplas interfaces** em uma única classe (`implements A, B, C`)
- [ ] Justificar quando usar interface, classe abstrata ou herança normal
- [ ] Aplicar **polimorfismo por capacidade** (não por tipo)
- [ ] Reconhecer uma **interface funcional** e seu uso

---

## Organização da Aula

| Bloco | Tema | Formato | Tempo estimado |
|-------|------|---------|----------------|
| [Bloco 1](./Bloco1/README.md) | Conceitos de Interfaces, `implements` e múltiplas interfaces | Explicação + exemplo simples | 50–60 min |
| [Bloco 2](./Bloco2/README.md) | Exercício Guiado Completo: Sistema de Capacidades de Documentos | Codificação guiada passo a passo | 90–120 min |
| [Bloco 3](./Bloco3/README.md) | Exercício Autônomo: Sistema de Devices IoT | Desenvolvimento independente | 90–120 min |

> **Observação:** Os Blocos 2 e 3 contêm exercícios substanciais que podem ocupar uma aula inteira cada um. O Bloco 1 foca em **entender** o mecanismo das interfaces (e a diferença para classes abstratas), enquanto os Blocos 2 e 3 focam em **aplicar** através de projetos com **múltiplas interfaces** combinadas.

---

## Dinâmica da aula

### Bloco 1 — Fundamentos (1ª aula: ~50 min)
1. Recapitulação: a barreira da herança simples que a Aula 09 deixou
2. Sintaxe de `interface` e `implements`
3. Múltiplas interfaces em uma única classe
4. Comparação: Interface vs Classe Abstrata vs Herança
5. Polimorfismo por capacidade (não por tipo)
6. Default methods (Java 8+) — visão geral
7. Interfaces funcionais — noção breve
8. Exemplo simples: `Voador`, `Nadador`, `Andador` aplicados a animais variados

### Bloco 2 — Exercício Guiado (2ª aula: 90-120 min)
**Professor codifica junto com alunos:**
- 3 interfaces de capacidade: `Imprimivel`, `Assinavel`, `Auditavel`
- 3 classes concretas com combinações diferentes: `Contrato`, `RelatorioFinanceiro`, `EmailInterno`
- Polimorfismo por capacidade: `List<Imprimivel>`, `List<Assinavel>`, `List<Auditavel>`
- Discussão de decisões de design (qual interface cada classe implementa, por quê)

### Bloco 3 — Exercício Autônomo (3ª aula: 90-120 min)
**Alunos desenvolvem com supervisão:**
- Sistema completo de **devices IoT** (Lâmpada, Smart TV, Sensor, Robô Aspirador)
- 3 interfaces: `Conectavel`, `Controlavel`, `Atualizavel`
- Cada device implementa um **subconjunto** diferente das interfaces
- Hub central polimórfico — opera sobre listas filtradas por capacidade
- Apresentação de soluções ao final

---

## Como estudar esta aula

1. **Bloco 1:** Foque em **entender a diferença** entre "ser um" (herança) e "saber fazer" (interface)
2. **Bloco 2:** **Acompanhe** o professor digitando junto (não copie/cole)
3. **Bloco 3:** **Pense primeiro** em quais capacidades cada device tem, depois nas classes

💡 **Dica crucial:** Interfaces brilham quando você consegue identificar **capacidades** que atravessam classes não relacionadas. Pense em **"saber fazer"**, não em **"ser"**.

---

## O que NÃO será abordado nesta aula

- **Default methods avançados** (apenas noção geral) → leitura complementar
- **Static methods em interfaces** → leitura complementar
- **Private methods em interfaces** (Java 9+) → leitura complementar
- **Lambda expressions completas** (apenas noção de interface funcional) → aulas futuras
- **`record` types** → aulas futuras

Nesta aula, o foco é no **mecanismo principal** das interfaces e na sua aplicação combinada (múltiplas interfaces por classe).

---

## Conceitos-chave que você vai dominar

### 1. Interface

```java
public interface Voador {
    void voar();
    int getAltitudeMaxima();
}
```

> Uma interface declara **o quê** uma classe deve fazer, sem dizer **como**. É um **contrato puro de comportamento**, sem estado próprio (no formato clássico).

### 2. `implements`

```java
public class Aviao implements Voador {
    @Override
    public void voar() {
        System.out.println("Avião decolando...");
    }

    @Override
    public int getAltitudeMaxima() {
        return 12000;
    }
}
```

> Uma classe **implementa** uma interface usando `implements`. Ela é **obrigada** a fornecer corpo para todos os métodos da interface (a menos que ela própria seja abstrata).

### 3. Múltiplas interfaces

```java
public class Anfibio implements Andador, Nadador {
    @Override
    public void andar() { ... }

    @Override
    public void nadar() { ... }
}
```

> Uma classe pode implementar **quantas interfaces quiser** (separadas por vírgula). Isso resolve a limitação da herança simples.

### 4. Herança + Interfaces juntas

```java
public class Pato extends Ave implements Andador, Nadador {
    // herda de Ave + implementa duas interfaces
}
```

> Uma classe pode **estender uma única classe** (concreta ou abstrata) **e** **implementar várias interfaces** ao mesmo tempo.

### 5. Polimorfismo por capacidade

```java
List<Voador> coisasQueVoam = new ArrayList<>();
coisasQueVoam.add(new Aviao());
coisasQueVoam.add(new Passaro());
coisasQueVoam.add(new Drone());

for (Voador v : coisasQueVoam) {
    v.voar();   // funciona para Aviao, Passaro e Drone
}
```

> A lista é tipada pela **interface**, não pela classe concreta. Qualquer classe que **implemente** `Voador` cabe na lista, independente da hierarquia.

### 6. Interface vs Classe Abstrata

| Aspecto | Classe Abstrata | Interface |
|---------|----------------|-----------|
| Palavra-chave para criar | `abstract class` | `interface` |
| Palavra-chave para usar | `extends` | `implements` |
| Quantas se pode usar | Apenas **1** | **Quantas quiser** |
| Pode ter atributos de instância? | ✅ Sim | ❌ Não (só `public static final`) |
| Pode ter construtores? | ✅ Sim | ❌ Não |
| Pode ter métodos concretos? | ✅ Sim | ⚠️ Sim, com `default` (Java 8+) |
| Modelagem | "**É um**" | "**Sabe fazer**" / "**É capaz de**" |

### 7. Interfaces funcionais (noção)

```java
@FunctionalInterface
public interface Calculadora {
    double calcular(double a, double b);   // exatamente UM método abstrato
}
```

> Uma **interface funcional** tem **exatamente um método abstrato**. Pode ser usada com **lambda expressions**:
>
> ```java
> Calculadora soma = (a, b) -> a + b;
> double r = soma.calcular(3, 4);   // 7.0
> ```
>
> Exemplos da biblioteca: `Runnable`, `Comparator`, `Predicate`. Veremos com profundidade em aulas futuras.

---

## Quando usar Interface?

✅ **Use interface quando:**
- Várias classes **não relacionadas** precisam ter o mesmo comportamento
- Uma classe precisa ter **múltiplas naturezas** (múltiplas capacidades)
- Você quer expressar "**sabe fazer X**" sem dizer **como** nem **a que custa**
- Vai usar polimorfismo **por capacidade** (não por tipo concreto)
- Não há **estado** ou **implementação comum** a ser reaproveitada

❌ **NÃO use interface quando:**
- Há código comum que deveria ser **reaproveitado** (use classe abstrata)
- Há **estado** que deveria ser compartilhado (use classe abstrata)
- A relação é claramente "**é um**" estrutural (use herança)
- Existe **apenas uma** classe que vai implementar (provavelmente não vale a pena)

---

## Resultado esperado ao final da Aula 10

Ao terminar, você deve conseguir:

✅ Declarar e implementar interfaces corretamente
✅ Combinar **múltiplas interfaces** em uma única classe
✅ Justificar a escolha entre interface, classe abstrata e herança simples
✅ Trabalhar com **polimorfismo por capacidade**
✅ Reconhecer interfaces funcionais e seu papel
✅ Refatorar hierarquias para usar interfaces quando apropriado

E, principalmente, resolver o problema inicial:

> **"Como permitir que um `FuncionarioCLT`, um `FuncionarioHorista` e um `FuncionarioComissionado` possam, cada um, opcionalmente ser também `Lider`, `Mentor` e/ou `Avaliador` — sem explodir em 24 subclasses?"**

**Resposta:** Modelar **Líder**, **Mentor** e **Avaliador** como **interfaces**, e fazer cada classe concreta de funcionário implementar **apenas as interfaces correspondentes às responsabilidades que aquele indivíduo realmente assume**. A hierarquia principal de `Funcionario` continua intacta (`extends`), e as capacidades extras são adicionadas **por composição de contratos** (`implements A, B, C`).

---

## Visualizando a virada

### Antes (só herança simples — Aulas 07 a 09)

```
                Funcionario (abstract)
                /     |     \
              CLT  Horista  Comissionado
              /     |     \    ...
        CLT_Lider CLT_Mentor  ...   ← explosão de combinações
```

### Depois (herança + interfaces — Aula 10)

```
            ┌────────────────────────┐
            │ « abstract » Funcionario│
            └────────────┬───────────┘
                         │ extends
        ┌────────────────┼────────────────┐
        │                │                │
   FuncionarioCLT   FuncionarioHor.   FuncionarioCom.
        │                │                │
        │ implements     │ implements     │ implements
        ▼                ▼                ▼
   « interface »   « interface »   « interface »
     Lider           Mentor          Avaliador
                  ▲      ▲       ▲
                  └──────┴───────┘
              (qualquer combinação livre, sem subclasses extras)
```

**Leia como:** "Continuo herdando estrutura do tipo do funcionário (CLT/Horista/Comissionado), e adiciono **capacidades extras** por contrato — sem mexer na hierarquia."

---

## 🎯 Vamos começar!

Clique em [Bloco 1](./Bloco1/README.md) para iniciar com os **conceitos fundamentais de Interfaces**.
