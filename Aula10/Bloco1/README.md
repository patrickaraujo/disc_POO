# Bloco 1 — Conceitos de Interfaces e Múltiplas Implementações

## Objetivos do Bloco

- Entender o que é uma **interface** e por que ela existe
- Diferenciar **interface** de **classe abstrata** e de **herança simples**
- Aprender a sintaxe de `interface` e `implements`
- Implementar **múltiplas interfaces** em uma única classe
- Combinar **`extends`** + **`implements`** corretamente
- Aplicar **polimorfismo por capacidade**
- Conhecer (em visão geral) **default methods** e **interfaces funcionais**
- Construir uma primeira hierarquia com múltiplas interfaces

---

## 1.1 O que é uma Interface?

Uma **interface** em Java é um **contrato puro de comportamento**.

> Ela declara **o quê** uma classe deve saber fazer (a assinatura dos métodos), sem dizer **como** (sem o corpo). Quem promete cumprir o contrato é a classe que a **implementa**.

### Analogia do mundo real

Pense em uma **tomada elétrica**. A tomada na parede define um **contrato**:
- 3 furos (fase, neutro, terra)
- 220V ou 110V
- 10 ou 20 amperes

```
   Tomada (interface):
   - É preciso ter 3 pinos
   - É preciso aguentar 220V
   - É preciso aguentar 10A

   Plugues que IMPLEMENTAM o contrato:
   - Plug de notebook
   - Plug de carregador de celular
   - Plug de liquidificador
   - Plug de furadeira
```

A tomada **não sabe** nem **se importa** se o que está conectado é um notebook, um liquidificador ou uma furadeira. Ela só exige que o objeto **cumpra o contrato**.

Em Java, o mesmo princípio:

```java
public interface Tomada220V {
    void conectar(int voltagem);
    void desconectar();
}

public class Notebook implements Tomada220V { ... }
public class Liquidificador implements Tomada220V { ... }
public class Furadeira implements Tomada220V { ... }
```

E você pode escrever código **genérico** que opera sobre qualquer aparelho:

```java
public void usar(Tomada220V aparelho) {
    aparelho.conectar(220);
    // ... liga, faz seu trabalho ...
    aparelho.desconectar();
}
```

A função `usar` aceita **qualquer coisa** que implemente `Tomada220V`. **Sem saber** o que é.

---

## 1.2 Por que Interfaces existem?

Vamos retomar onde a Aula 09 nos deixou. Você tinha:

```java
public abstract class Funcionario { ... }

public class FuncionarioCLT       extends Funcionario { ... }
public class FuncionarioHorista   extends Funcionario { ... }
public class FuncionarioComissionado extends Funcionario { ... }
```

Tudo certo. Agora a empresa quer:

- Alguns funcionários (de qualquer tipo) podem ser **Líderes**.
- Alguns funcionários (de qualquer tipo) podem ser **Mentores**.

### Tentativa 1: subclasses para cada combinação

```java
public class FuncionarioCLTLider     extends FuncionarioCLT { ... }
public class FuncionarioCLTMentor    extends FuncionarioCLT { ... }
public class FuncionarioCLTLiderMentor extends FuncionarioCLT { ... }
// ... repete tudo para Horista e Comissionado
```

3 tipos × 4 combinações = **12 classes**. Adicione "Avaliador" e dobra para 24. **Insustentável.**

### Tentativa 2: herança múltipla

```java
public class FuncionarioCLT extends Funcionario, Lider, Mentor {  // ❌
    // ...
}
```

**Erro de compilação.** Java permite herdar de **apenas uma classe**. Esta foi uma decisão de design da linguagem.

### Tentativa 3: colocar tudo em `Funcionario`

```java
public abstract class Funcionario {
    public void avaliarPar(...) { ... }
    public void registrarSessaoMentoria(...) { ... }
    public void avaliarCandidato(...) { ... }
    // ... toda CLT tem isso? Toda Horista também? Mesmo se nunca for líder?
}
```

**Quebra o design.** Estagiários que nunca serão líderes ainda assim teriam `avaliarPar()`. Funcionários que nunca farão mentoria teriam `registrarSessaoMentoria()`. Estado e comportamento poluídos.

### Solução: Interfaces

Java oferece um mecanismo separado para expressar **capacidades**:

```java
public interface Lider {
    void avaliarPar(Funcionario par, int nota);
    void conduzirReuniao(String pauta);
}

public interface Mentor {
    void registrarSessao(Funcionario mentorado, String anotacoes);
}

public interface Avaliador {
    boolean avaliarCandidato(String nome, int nota);
}
```

E qualquer classe implementa **somente** as interfaces que fazem sentido:

```java
// Uma CLT que é líder e mentora
public class FuncionarioCLT
       extends Funcionario
       implements Lider, Mentor {

    // métodos das duas interfaces obrigatoriamente implementados
}

// Uma Horista que é só avaliadora
public class FuncionarioHorista
       extends Funcionario
       implements Avaliador {

    // só os métodos de Avaliador
}
```

✅ Hierarquia principal preservada (`extends Funcionario`)
✅ Cada classe declara **exatamente** quais capacidades extras tem
✅ Adicionar novo programa = **uma nova interface**, sem mexer no resto
✅ Polimorfismo por capacidade: `List<Lider>` aceita qualquer classe que implemente `Lider`

---

## 1.3 Sintaxe da Interface em Java

### Declarando uma interface

```java
public interface NomeDaInterface {

    // Métodos abstratos (sem corpo)
    // Por padrão são PUBLIC ABSTRACT (não precisa escrever)
    void metodo1();
    int metodo2(String parametro);

    // Constantes (PUBLIC STATIC FINAL implícitos)
    int LIMITE = 100;
}
```

**Pontos importantes:**

1. Usa a palavra-chave **`interface`** (não `class`)
2. Todos os métodos são, por padrão, **`public abstract`** (não precisa escrever esses modificadores)
3. Não há corpo `{ }` nos métodos — termina em `;`
4. Não pode ter **atributos de instância**
5. Pode ter **constantes** (são automaticamente `public static final`)
6. Não pode ter **construtores**

### Implementando uma interface

```java
public class MinhaClasse implements NomeDaInterface {

    @Override
    public void metodo1() {
        // implementação real
    }

    @Override
    public int metodo2(String parametro) {
        // implementação real
        return 42;
    }
}
```

**Pontos importantes:**

1. Usa a palavra-chave **`implements`** (não `extends`)
2. Métodos da interface têm que ser **`public`** na implementação (não pode reduzir visibilidade)
3. Use **`@Override`** (não é obrigatório, mas é fortemente recomendado)
4. Se faltar implementar algum método, **erro de compilação** (a menos que a classe seja `abstract`)

---

## 1.4 Múltiplas interfaces

Esta é a **grande virada** em relação à herança.

Uma classe pode implementar **quantas interfaces quiser**:

```java
public class Anfibio implements Andador, Nadador {

    @Override
    public void andar() {
        System.out.println("Andando em terra firme...");
    }

    @Override
    public void nadar() {
        System.out.println("Nadando na água...");
    }
}
```

E pode também combinar com herança:

```java
public class Pato extends Ave implements Andador, Nadador {

    // herda atributos e métodos de Ave
    // implementa Andador
    // implementa Nadador

    @Override
    public void andar() { ... }

    @Override
    public void nadar() { ... }
}
```

**Regra geral:**

```
class Filha extends ÚmaClasse implements Interface1, Interface2, Interface3 { ... }
              ▲                          ▲
              │                          │
              └── apenas UMA             └── quantas quiser
```

---

## 1.5 Interface vs Classe Abstrata

Esta é a comparação que **todo aluno** faz nessa altura do curso. Veja com calma:

| Aspecto | Classe Abstrata | Interface |
|---------|----------------|-----------|
| Palavra-chave para criar | `abstract class` | `interface` |
| Palavra-chave para usar | `extends` | `implements` |
| Quantas se pode usar | Apenas **1** (herança simples) | **Quantas quiser** |
| Pode ter atributos de instância? | ✅ Sim | ❌ Não (só constantes `public static final`) |
| Pode ter construtores? | ✅ Sim | ❌ Não |
| Pode ter métodos concretos? | ✅ Sim (parte normal) | ⚠️ Sim, com `default` (Java 8+) |
| Pode ter métodos abstratos? | ✅ Sim | ✅ Sim (são o padrão) |
| Pode ser instanciada? | ❌ Não | ❌ Não |
| Modelagem semântica | "**É um**" (estrutural) | "**Sabe fazer**" / "**É capaz de**" (capacidade) |
| Quando preferir | Há **estado** + **implementação comum** | Há **contrato** sem estado / capacidade transversal |

### Exemplo lado a lado

**Com classe abstrata:**

```java
public abstract class Animal {
    protected String nome;
    protected int idade;

    public Animal(String nome, int idade) {       // construtor
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() { return nome; }      // método concreto
    public abstract void emitirSom();             // método abstrato
}
```

**Com interface:**

```java
public interface Voador {
    int getAltitudeMaxima();                      // método abstrato
    void voar();                                  // método abstrato
    // sem atributos, sem construtor
}
```

**Combinando:**

```java
public class Aguia extends Animal implements Voador {

    private int altitudeMax;

    public Aguia(String nome, int idade, int altitudeMax) {
        super(nome, idade);
        this.altitudeMax = altitudeMax;
    }

    @Override
    public void emitirSom() {                     // de Animal
        System.out.println("Grasna!");
    }

    @Override
    public int getAltitudeMaxima() {              // de Voador
        return altitudeMax;
    }

    @Override
    public void voar() {                          // de Voador
        System.out.println(nome + " está voando!");
    }
}
```

---

## 1.6 Interface vs Herança (a diferença conceitual)

A diferença mais importante **não é** a sintaxe — é o **significado**.

### Herança (`extends`) → "É UM"

```java
public class Cachorro extends Animal { ... }
```

Cachorro **é um** Animal. Em termos estruturais, "é um Animal especializado em ser Cachorro". Estende a definição original.

- Compartilha **estado** (atributos da superclasse)
- Compartilha **implementação** (métodos da superclasse)
- A subclasse **continua** sendo daquela família

### Interface (`implements`) → "SABE FAZER" / "É CAPAZ DE"

```java
public class Cachorro implements Andador, Nadador, Latidor { ... }
```

Cachorro **sabe** andar, **sabe** nadar, **sabe** latir. São capacidades que o cachorro tem — não dizem **o que ele é**, dizem **o que ele faz**.

- Não compartilha estado
- Não compartilha implementação (no formato clássico)
- A classe apenas **promete cumprir contratos**

### Por que isso importa?

```java
// HERANÇA — diz "esse objeto é desse tipo"
Animal a = new Cachorro();

// INTERFACE — diz "esse objeto sabe fazer essa coisa"
Andador andador = new Cachorro();
Nadador nadador = new Cachorro();
Voador voador = new Aguia();
```

Você pode escrever código que opera sobre **capacidades**, sem se importar com a hierarquia:

```java
public void atravessarRio(Nadador n) {
    n.nadar();   // funciona para qualquer coisa que saiba nadar:
                 // peixe, pato, sapo, ser humano, robô anfíbio...
}
```

Isso é **muito mais flexível** que exigir um tipo específico.

---

## 1.7 Polimorfismo por capacidade

Você já viu polimorfismo na Aula 08 (por tipo da hierarquia). Interfaces trazem uma variante poderosa: **polimorfismo por capacidade**.

### Por tipo (Aula 08)

```java
List<Animal> zoo = new ArrayList<>();
zoo.add(new Cachorro("Rex"));
zoo.add(new Gato("Mimi"));

for (Animal a : zoo) {
    a.emitirSom();   // cada um do seu jeito
}
```

A lista exige que **todos sejam Animal**. Polimorfismo pela hierarquia.

### Por capacidade (Aula 10)

```java
List<Voador> coisasQueVoam = new ArrayList<>();
coisasQueVoam.add(new Aviao());     // não é Animal
coisasQueVoam.add(new Passaro());   // é Animal
coisasQueVoam.add(new Drone());     // não é Animal
coisasQueVoam.add(new Helicoptero());

for (Voador v : coisasQueVoam) {
    v.voar();
}
```

A lista exige apenas que **todos saibam voar** — não importa de que árvore de classe vêm. Polimorfismo **independente da hierarquia**.

> 💡 Isso é uma das ferramentas mais usadas em frameworks reais. `List`, `Comparable`, `Runnable`, `Iterable` são todas interfaces — e o Java te dá APIs inteiras que operam sobre essas capacidades.

---

## 1.8 Default methods (Java 8+) — visão geral

A partir de Java 8, interfaces ganharam a capacidade de ter **métodos com corpo** (chamados `default`):

```java
public interface Voador {
    void voar();                                  // abstrato (normal)

    default int getAltitudeMaxima() {             // concreto
        return 1000;                              // valor padrão
    }
}
```

Classes que implementam podem **sobrescrever** o default ou **aceitar** a implementação padrão:

```java
public class Pombo implements Voador {
    @Override
    public void voar() { System.out.println("Voando baixo..."); }

    // não sobrescreve getAltitudeMaxima — usa o default (1000)
}

public class Aviao implements Voador {
    @Override
    public void voar() { System.out.println("Decolando..."); }

    @Override
    public int getAltitudeMaxima() {              // sobrescreve
        return 12000;
    }
}
```

### Para que serve?

Default methods foram criados principalmente para permitir **evoluir interfaces existentes sem quebrar** todas as classes que já as implementam.

**Exemplo realista:** quando o Java 8 quis adicionar o método `forEach` à interface `Iterable`, ele não podia simplesmente adicionar um método abstrato — isso quebraria milhões de classes existentes. Solução: adicionou como `default`, com implementação padrão. Quem quisesse poderia sobrescrever.

> Nesta aula, **não vamos usar default methods intensivamente**. Basta saber que existem.

---

## 1.9 Interfaces funcionais — noção breve

Uma **interface funcional** é uma interface com **exatamente um** método abstrato.

```java
@FunctionalInterface
public interface Calculadora {
    double calcular(double a, double b);    // único método abstrato
}
```

A anotação `@FunctionalInterface` é **opcional**, mas avisa o compilador para verificar que há **só um** método abstrato (se você adicionar outro, dá erro de compilação).

### Por que existem?

Porque o Java 8 introduziu **lambda expressions**, e elas só funcionam para interfaces funcionais:

```java
Calculadora soma  = (a, b) -> a + b;
Calculadora produto = (a, b) -> a * b;

System.out.println(soma.calcular(3, 4));      // 7.0
System.out.println(produto.calcular(3, 4));   // 12.0
```

A expressão `(a, b) -> a + b` é uma **lambda** — uma forma compacta de criar uma instância da interface, sem precisar de classe nomeada.

### Exemplos da biblioteca padrão

- `Runnable` — `void run()`
- `Comparator<T>` — `int compare(T a, T b)`
- `Predicate<T>` — `boolean test(T t)`
- `Function<T, R>` — `R apply(T t)`

> Nesta aula, **não vamos usar lambdas nos exercícios**. Mas é importante reconhecer essa categoria de interface, pois você verá muito em frameworks (Spring, Streams, Reactor).

---

## 1.10 Exemplo Completo: Animais com Múltiplas Capacidades

Vamos juntar tudo num exemplo simples.

### Interfaces

```java
public interface Andador {
    void andar();
}

public interface Nadador {
    void nadar();
}

public interface Voador {
    void voar();
}
```

### Classe abstrata base

```java
public abstract class Animal {
    protected String nome;

    public Animal(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract void emitirSom();
}
```

### Subclasses concretas com diferentes capacidades

```java
public class Cachorro extends Animal implements Andador, Nadador {

    public Cachorro(String nome) { super(nome); }

    @Override public void emitirSom() { System.out.println(nome + ": Au au!"); }
    @Override public void andar()     { System.out.println(nome + " está andando."); }
    @Override public void nadar()     { System.out.println(nome + " está nadando."); }
}

public class Aguia extends Animal implements Andador, Voador {

    public Aguia(String nome) { super(nome); }

    @Override public void emitirSom() { System.out.println(nome + ": Grasna!"); }
    @Override public void andar()     { System.out.println(nome + " caminha pousada."); }
    @Override public void voar()      { System.out.println(nome + " plana alto."); }
}

public class Pato extends Animal implements Andador, Nadador, Voador {

    public Pato(String nome) { super(nome); }

    @Override public void emitirSom() { System.out.println(nome + ": Quack!"); }
    @Override public void andar()     { System.out.println(nome + " gingando."); }
    @Override public void nadar()     { System.out.println(nome + " na lagoa."); }
    @Override public void voar()      { System.out.println(nome + " voo curto."); }
}

public class Peixe extends Animal implements Nadador {

    public Peixe(String nome) { super(nome); }

    @Override public void emitirSom() { System.out.println(nome + " (silêncio)."); }
    @Override public void nadar()     { System.out.println(nome + " nadando rápido."); }
}
```

### Testando o polimorfismo por capacidade

```java
import java.util.ArrayList;
import java.util.List;

public class TesteAnimais {
    public static void main(String[] args) {

        List<Animal> animais = new ArrayList<>();
        animais.add(new Cachorro("Rex"));
        animais.add(new Aguia("Vento"));
        animais.add(new Pato("Donald"));
        animais.add(new Peixe("Nemo"));

        // Todos emitem som (polimorfismo POR TIPO — herança)
        System.out.println("--- Sons ---");
        for (Animal a : animais) {
            a.emitirSom();
        }

        // Apenas os que sabem nadar (polimorfismo POR CAPACIDADE — interface)
        System.out.println("\n--- Quem sabe nadar ---");
        for (Animal a : animais) {
            if (a instanceof Nadador n) {
                n.nadar();
            }
        }

        // Apenas os que sabem voar
        System.out.println("\n--- Quem sabe voar ---");
        for (Animal a : animais) {
            if (a instanceof Voador v) {
                v.voar();
            }
        }
    }
}
```

**Saída:**

```
--- Sons ---
Rex: Au au!
Vento: Grasna!
Donald: Quack!
Nemo (silêncio).

--- Quem sabe nadar ---
Rex está nadando.
Donald na lagoa.
Nemo nadando rápido.

--- Quem sabe voar ---
Vento plana alto.
Donald voo curto.
```

**O que observar:**
1. ✅ Cada classe **escolhe** quais capacidades implementa
2. ✅ Pato combina 3 interfaces — Cachorro combina 2 — Peixe combina 1
3. ✅ A iteração por **capacidade** (`instanceof Nadador`) filtra naturalmente
4. ✅ Não há explosão combinatória — apenas 3 interfaces + 4 classes

> 💡 Note também o **pattern matching for instanceof** usado: `if (a instanceof Nadador n)` declara e converte ao mesmo tempo. Disponível a partir de Java 16.

---

## 1.11 Erros Comuns

### ❌ Erro 1: Tentar instanciar uma interface

```java
public interface Voador { void voar(); }

Voador v = new Voador();   // ❌ Erro de compilação
```

**Solução:** instancie uma classe que implementa, e atribua à referência da interface:

```java
Voador v = new Aviao();   // ✅ OK
```

---

### ❌ Erro 2: Esquecer de implementar todos os métodos

```java
public interface Voador {
    void voar();
    int getAltitudeMaxima();
}

public class Aviao implements Voador {     // ❌ Erro: falta getAltitudeMaxima
    @Override
    public void voar() { ... }
}
```

**Mensagem do compilador:** `Aviao is not abstract and does not implement abstract method getAltitudeMaxima() in Voador`.

**Solução:** implementar **todos** os métodos abstratos da interface.

---

### ❌ Erro 3: Usar `extends` em vez de `implements` (ou vice-versa)

```java
public class Aviao extends Voador { ... }       // ❌ interface não se extende com 'extends'
public class Cachorro implements Animal { ... } // ❌ classe não se implementa com 'implements'
```

**Solução:** decore a tabela:

- Classe pra classe: `extends`
- Classe pra interface: `implements`
- Interface pra interface: `extends` (interfaces podem estender outras interfaces — uma ou várias!)

---

### ❌ Erro 4: Reduzir visibilidade na implementação

```java
public interface Voador {
    void voar();    // public por padrão
}

public class Aviao implements Voador {
    @Override
    void voar() { ... }    // ❌ Erro: precisa ser public
}
```

**Mensagem do compilador:** `Cannot reduce the visibility of the inherited method from Voador`.

**Solução:** sempre `public` ao implementar interface:

```java
@Override
public void voar() { ... }
```

---

### ❌ Erro 5: Tentar declarar atributos de instância

```java
public interface Voador {
    int altitude;          // ❌ Erro
    String marca = "X";    // ⚠️ Compila, mas vira PUBLIC STATIC FINAL (constante)
}
```

**Mensagem do compilador:** atributos sem inicialização não são permitidos.

**Solução:** interfaces **não têm estado**. Se precisar de estado, repense — provavelmente é uma classe abstrata.

---

## 1.12 Quadro-resumo: Quando usar o quê?

```
┌────────────────────────────────────────────────────────────┐
│                    CLASSE CONCRETA                         │
├────────────────────────────────────────────────────────────┤
│  Quando: a classe pode existir por si só                   │
│  Sintaxe: public class X { ... }                           │
│  Exemplo: String, Integer, Carro, Pedido                   │
└────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────┐
│                    CLASSE ABSTRATA                         │
├────────────────────────────────────────────────────────────┤
│  Quando: há ESTADO comum + comportamento parcial           │
│           há fluxo comum (Template Method)                 │
│           relação "é um" estrutural                        │
│  Sintaxe: public abstract class X { ... }                  │
│  Uso: public class Y extends X { ... }                     │
│  Exemplo: Funcionario, Notificacao, Pagamento              │
└────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────┐
│                       INTERFACE                            │
├────────────────────────────────────────────────────────────┤
│  Quando: capacidade transversal (várias classes não-       │
│           relacionadas precisam do mesmo comportamento)    │
│           uma classe precisa de múltiplas naturezas        │
│           relação "sabe fazer" / "é capaz de"              │
│  Sintaxe: public interface X { ... }                       │
│  Uso: public class Y implements X, Z, W { ... }            │
│  Exemplo: Comparable, Runnable, Voador, Imprimivel         │
└────────────────────────────────────────────────────────────┘
```

### Pergunta-guia para o aluno

> *Olhando para o problema que você quer modelar, faça-se três perguntas:*
> 1. *"As classes envolvidas têm estado comum?"* → se sim, **classe abstrata**
> 2. *"Há um fluxo comum com pontos de variação?"* → **classe abstrata + Template Method**
> 3. *"Várias classes não relacionadas precisam do mesmo comportamento?"* → **interface**
> 4. *"Uma classe precisa fazer N coisas independentes?"* → **múltiplas interfaces**

---

## Resumo do Bloco 1

Neste bloco você aprendeu:

✅ **Interface** = contrato puro de comportamento ("sabe fazer")
✅ **`interface`** = palavra-chave para declarar
✅ **`implements`** = palavra-chave para usar
✅ **Múltiplas interfaces** = uma classe pode implementar quantas quiser
✅ **`extends` + `implements`** = uma classe pode combinar herança simples com múltiplas interfaces
✅ **Polimorfismo por capacidade** = listas tipadas pela interface aceitam qualquer implementação
✅ **Default methods** = métodos com corpo em interface (Java 8+), visão geral
✅ **Interface funcional** = interface com exatamente 1 método abstrato; base das lambdas

**Frase para guardar:**
> *"Classe abstrata responde 'o que isso é'. Interface responde 'o que isso sabe fazer'."*

---

## Próximo Passo

No **Bloco 2**, você vai **aplicar** tudo isso em um **exercício guiado completo**: Sistema de Capacidades de Documentos Corporativos, com 3 interfaces e 3 classes implementando combinações diferentes.

[➡️ Ir para Bloco 2](../Bloco2/README.md)
