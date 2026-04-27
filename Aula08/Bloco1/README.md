# Bloco 1 — Conceitos de Polimorfismo e Tipo Estático/Dinâmico

## Objetivos do Bloco

- Entender o que é polimorfismo e por que ele é a "alma" da OO
- Diferenciar **sobrescrita** (`@Override`) de **sobrecarga** (overload)
- Distinguir **tipo estático** (referência) de **tipo dinâmico** (objeto)
- Compreender como a JVM escolhe a versão correta de um método em runtime
- Aprender a usar **listas polimórficas** (`ArrayList<Animal>`)
- Aplicar **upcasting** e **downcasting** com `instanceof`
- Criar uma primeira aplicação polimórfica completa

---

## 1.1 O que é Polimorfismo?

A palavra vem do grego:
- **poli** = muitos
- **morfos** = formas

> **Polimorfismo** é a capacidade de objetos de **classes diferentes** responderem à **mesma chamada de método** de **formas diferentes**, sem que quem está chamando precise saber o tipo concreto.

### Analogia do mundo real

Imagine que você é um maestro segurando uma batuta. Você diz: "**Toquem!**"

```
    🎼 "Toquem!"
        │
   ┌────┼────┬─────────┐
   ▼    ▼    ▼         ▼
   🎻   🎺   🥁        🎹
   "lá lá" "tu-tu" "pá pá"  "dó-mi-sol"
   (violino) (trompete) (tambor) (piano)
```

- **Mesma ordem:** "tocar"
- **Comportamentos diferentes:** cada instrumento responde do seu jeito
- **Você (maestro)** não precisa saber o que cada um faz internamente

Isso é polimorfismo: **uma chamada, vários comportamentos**.

---

## 1.2 Sobrescrita revisitada (`@Override`)

Você já viu sobrescrita na Aula 07. Vamos relembrar e aprofundar.

### Sobrescrita = Mesmo método, comportamento diferente em cada subclasse

```java
public class Animal {
    public void emitirSom() {
        System.out.println("Som genérico de animal");
    }
}

public class Cachorro extends Animal {
    @Override
    public void emitirSom() {
        System.out.println("Au au!");
    }
}

public class Gato extends Animal {
    @Override
    public void emitirSom() {
        System.out.println("Miau!");
    }
}

public class Vaca extends Animal {
    @Override
    public void emitirSom() {
        System.out.println("Muuu!");
    }
}
```

### Regras da sobrescrita (revisão)

1. ✅ Mesmo **nome** do método
2. ✅ Mesma **lista de parâmetros**
3. ✅ Mesmo **tipo de retorno** (ou subtipo — covariância)
4. ✅ Modificador de acesso **igual ou mais permissivo**
5. ✅ A anotação `@Override` **não é obrigatória**, mas **fortemente recomendada**

> 💡 `@Override` faz o compilador **verificar** que você realmente está sobrescrevendo. Se errar a assinatura, o erro aparece em tempo de compilação — não em produção.

### Sobrescrita ≠ Sobrecarga

Esta é a fonte de **muita confusão**. Vamos esclarecer:

| Aspecto | Sobrescrita (Override) | Sobrecarga (Overload) |
|---------|------------------------|------------------------|
| Onde acontece | Em **subclasse** | Na **mesma classe** |
| Assinatura | **Igual** à da superclasse | **Diferente** (parâmetros mudam) |
| Tipo de retorno | Igual ou covariante | Pode ser qualquer |
| Anotação | `@Override` | Nenhuma |
| Resolução | Em **tempo de execução** | Em **tempo de compilação** |
| Para que serve | **Especializar** comportamento | Oferecer **múltiplas formas** de chamar |

**Exemplo de sobrecarga (overload):**

```java
public class Calculadora {
    public int somar(int a, int b) {
        return a + b;
    }

    public double somar(double a, double b) {  // Sobrecarga!
        return a + b;
    }

    public int somar(int a, int b, int c) {    // Sobrecarga!
        return a + b + c;
    }
}
```

**Exemplo de sobrescrita (override):**

```java
public class Animal {
    public void emitirSom() { ... }
}

public class Cachorro extends Animal {
    @Override                     // Sobrescrita!
    public void emitirSom() { ... }
}
```

> ⚠️ **Polimorfismo = sobrescrita + tipo dinâmico.** Sobrecarga **não** é polimorfismo de subtipo (é outro tipo: polimorfismo ad-hoc).

---

## 1.3 Tipo Estático × Tipo Dinâmico

Esta é a **distinção mais importante** desta aula. Leia com calma.

```java
Animal a = new Cachorro("Rex");
//    ^         ^
//    |         └─── Tipo DINÂMICO: o objeto que existe na memória é um Cachorro
//    └─────────────  Tipo ESTÁTICO: a referência foi declarada como Animal
```

### Visualizando na memória

```
PILHA (stack)             HEAP (memória)
                          ┌────────────────────────┐
   a ─────────────────────► objeto Cachorro        │
   (referência tipo Animal) │   - nome = "Rex"     │
                          │   - latir() ...        │
                          │   - emitirSom() ...    │
                          └────────────────────────┘
```

### Por que isso importa?

O **tipo estático** decide:
- Quais métodos o **compilador permite** chamar

O **tipo dinâmico** decide:
- Qual **implementação** do método é executada

### Exemplo prático

```java
Animal a = new Cachorro("Rex");

a.emitirSom();    // ✅ Compilador OK (Animal tem emitirSom)
                  // ✅ JVM executa versão de Cachorro: "Au au!"

a.latir();        // ❌ Erro de compilação!
                  //    Animal NÃO TEM o método latir.
                  //    Não importa que o objeto real seja Cachorro.
```

> **Lema:** O **compilador olha a referência** (estático). A **JVM olha o objeto** (dinâmico).

---

## 1.4 Polimorfismo em tempo de execução (Late Binding)

Quando você escreve:

```java
a.emitirSom();
```

O compilador **NÃO sabe** qual versão chamar. Ele apenas verifica que `Animal` tem o método. A decisão real é tomada **em tempo de execução** pela JVM, usando o tipo dinâmico do objeto.

Isso é chamado de:
- **Late binding** (ligação tardia)
- **Dynamic dispatch** (despacho dinâmico)
- **Polimorfismo em tempo de execução**

### Algoritmo da JVM (simplificado)

```
Quando a.emitirSom() é executado:

1. Pegue o objeto referenciado por 'a'
2. Olhe o tipo REAL desse objeto (tipo dinâmico)
3. Procure 'emitirSom()' nessa classe
4. Se encontrou → execute essa versão
5. Se não encontrou → suba para a superclasse
6. Repita até encontrar (ou chegar em Object)
```

### Exemplo demonstrativo

```java
public class TesteDispatch {
    public static void main(String[] args) {

        Animal[] zoo = new Animal[3];
        zoo[0] = new Cachorro("Rex");
        zoo[1] = new Gato("Mimi");
        zoo[2] = new Vaca("Mimosa");

        // Mesmo loop, comportamentos diferentes
        for (int i = 0; i < zoo.length; i++) {
            zoo[i].emitirSom();
        }
    }
}
```

**Saída:**
```
Au au!
Miau!
Muuu!
```

**O que aconteceu?**
- `zoo[i]` é declarado `Animal` (tipo estático)
- Mas cada posição tem um objeto **diferente** (tipo dinâmico)
- A JVM despacha **automaticamente** para a versão correta

> 🎯 **Esse é o polimorfismo em ação.** Note que o loop **não tem nenhum `if/else`** verificando o tipo. Isso é o que torna o código **flexível** e **extensível**.

---

## 1.5 Por que isso é tão poderoso?

### Antes do polimorfismo

```java
// ❌ Código rígido com if/else
public void fazerBarulho(Object animal) {
    if (animal instanceof Cachorro) {
        ((Cachorro) animal).latir();
    } else if (animal instanceof Gato) {
        ((Gato) animal).miar();
    } else if (animal instanceof Vaca) {
        ((Vaca) animal).mugir();
    }
    // Adicionar Pássaro? PRECISA mexer aqui!
}
```

### Com polimorfismo

```java
// ✅ Código flexível
public void fazerBarulho(Animal animal) {
    animal.emitirSom();  // Cada um faz o seu
}

// Adicionar Pássaro? NÃO precisa mexer aqui!
// Basta criar a classe Passaro extends Animal com seu emitirSom().
```

> 💎 **Princípio Aberto/Fechado (Open/Closed Principle):**
> *Software deve ser **aberto a extensão** mas **fechado a modificação**.*
> Polimorfismo é o mecanismo principal para conseguir isso em OO.

---

## 1.6 Listas polimórficas

A grande utilidade do polimorfismo é poder **agrupar tipos diferentes** em uma única estrutura de dados.

### Com array

```java
Animal[] zoo = new Animal[3];
zoo[0] = new Cachorro("Rex");
zoo[1] = new Gato("Mimi");
zoo[2] = new Vaca("Mimosa");
```

### Com `ArrayList` (mais flexível)

```java
import java.util.ArrayList;

ArrayList<Animal> zoo = new ArrayList<>();
zoo.add(new Cachorro("Rex"));
zoo.add(new Gato("Mimi"));
zoo.add(new Vaca("Mimosa"));
zoo.add(new Cachorro("Bobby"));   // Pode adicionar mais!

for (Animal a : zoo) {
    a.emitirSom();
}
```

**Vantagens do `ArrayList`:**
- ✅ Cresce dinamicamente (não precisa definir tamanho fixo)
- ✅ Métodos prontos: `add`, `remove`, `size`, `contains`...
- ✅ Combina perfeitamente com polimorfismo

### `<Animal>` é o segredo

A parte `<Animal>` é chamada de **parâmetro de tipo** (Generics). Significa:

> "Esta lista aceita qualquer objeto cujo tipo seja `Animal` **ou subtipo de Animal**."

Por isso ela aceita `Cachorro`, `Gato`, `Vaca` — todos são `Animal`.

---

## 1.7 Upcasting e Downcasting

### Upcasting (subir na hierarquia) — IMPLÍCITO e SEGURO

```java
Cachorro c = new Cachorro("Rex");
Animal a = c;                   // ✅ Upcasting implícito
                                //    Cachorro É UM Animal, sempre seguro

Animal a2 = new Cachorro("Bob"); // ✅ Mesmo upcasting, em uma linha
```

### Downcasting (descer na hierarquia) — EXPLÍCITO e PERIGOSO

```java
Animal a = new Cachorro("Rex");

Cachorro c = (Cachorro) a;       // ✅ Downcasting explícito
                                 //    Funciona porque o objeto REAL é Cachorro
c.latir();                       // ✅ Agora pode chamar latir()

// MAS:
Animal a2 = new Gato("Mimi");
Cachorro c2 = (Cachorro) a2;     // ❌ ClassCastException em runtime!
                                 //    Gato NÃO É Cachorro
```

### A solução: `instanceof`

Sempre que precisar fazer downcasting, **verifique antes** com `instanceof`:

```java
Animal a = obterAnimalAleatorio();

if (a instanceof Cachorro) {
    Cachorro c = (Cachorro) a;
    c.latir();
} else if (a instanceof Gato) {
    Gato g = (Gato) a;
    g.miar();
}
```

**A partir do Java 16,** existe sintaxe simplificada (pattern matching):

```java
if (a instanceof Cachorro c) {  // Declara e converte ao mesmo tempo
    c.latir();
}
```

> ⚠️ **Sinal de alerta:** Se você está usando **muito** `instanceof + cast`, provavelmente seu polimorfismo está mal modelado. Geralmente dá para resolver com mais sobrescrita.

---

## 1.8 Exemplo Completo: Hierarquia de Funcionários (motivação para o Bloco 2)

Vamos retomar o exemplo da Aula 07 e ver o polimorfismo brilhar.

### Superclasse: Funcionario

```java
public class Funcionario {
    protected String nome;
    protected String cpf;

    public Funcionario(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }

    // Será sobrescrito em cada tipo
    public double calcularSalario() {
        return 0.0;
    }

    public void exibirContracheque() {
        System.out.println("================================");
        System.out.println("Funcionário: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Salário: R$ " + String.format("%.2f", calcularSalario()));
        System.out.println("================================");
    }
}
```

### Subclasse: FuncionarioCLT

```java
public class FuncionarioCLT extends Funcionario {
    private double salarioBase;

    public FuncionarioCLT(String nome, String cpf, double salarioBase) {
        super(nome, cpf);
        this.salarioBase = salarioBase;
    }

    @Override
    public double calcularSalario() {
        return salarioBase;
    }
}
```

### Subclasse: FuncionarioHorista

```java
public class FuncionarioHorista extends Funcionario {
    private double valorHora;
    private int horasTrabalhadas;

    public FuncionarioHorista(String nome, String cpf,
                              double valorHora, int horasTrabalhadas) {
        super(nome, cpf);
        this.valorHora = valorHora;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    @Override
    public double calcularSalario() {
        return valorHora * horasTrabalhadas;
    }
}
```

### Subclasse: FuncionarioComissionado

```java
public class FuncionarioComissionado extends Funcionario {
    private double salarioBase;
    private double totalVendas;
    private double taxaComissao;

    public FuncionarioComissionado(String nome, String cpf, double salarioBase,
                                    double totalVendas, double taxaComissao) {
        super(nome, cpf);
        this.salarioBase = salarioBase;
        this.totalVendas = totalVendas;
        this.taxaComissao = taxaComissao;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + (totalVendas * taxaComissao);
    }
}
```

### Testando o polimorfismo

```java
import java.util.ArrayList;

public class TesteFolha {
    public static void main(String[] args) {

        ArrayList<Funcionario> folha = new ArrayList<>();

        folha.add(new FuncionarioCLT("Ana Silva", "111.111.111-11", 5000.0));
        folha.add(new FuncionarioHorista("Bruno Souza", "222.222.222-22", 50.0, 160));
        folha.add(new FuncionarioComissionado("Carla Lima", "333.333.333-33",
                                               2000.0, 30000.0, 0.05));

        double totalFolha = 0;

        for (Funcionario f : folha) {
            f.exibirContracheque();          // Cada um exibe diferente
            totalFolha += f.calcularSalario(); // Cada um calcula diferente
        }

        System.out.println("\nTotal da folha: R$ " + String.format("%.2f", totalFolha));
    }
}
```

**Saída:**
```
================================
Funcionário: Ana Silva
CPF: 111.111.111-11
Salário: R$ 5000,00
================================
================================
Funcionário: Bruno Souza
CPF: 222.222.222-22
Salário: R$ 8000,00
================================
================================
Funcionário: Carla Lima
CPF: 333.333.333-33
Salário: R$ 3500,00
================================

Total da folha: R$ 16500,00
```

**O que observar:**
1. ✅ Um único `ArrayList<Funcionario>` aceita os 3 tipos
2. ✅ Um único loop processa todos
3. ✅ `f.calcularSalario()` chama versão correta para cada um
4. ✅ `f.exibirContracheque()` (herdado) também usa polimorfismo internamente!
5. ✅ Para adicionar `FuncionarioEstagiario`, **não mexemos neste código**

> 💡 **Observação sutil mas importante:** Repare que `exibirContracheque()` está definido **na superclasse** e chama `calcularSalario()`. Quando esse método herdado executa em um `FuncionarioCLT`, o `calcularSalario()` chamado é o da CLT — não o da superclasse! Isso é polimorfismo funcionando "por dentro" também.

---

## 1.9 Erros Comuns

### ❌ Erro 1: Confundir tipo estático com dinâmico

```java
Animal a = new Cachorro("Rex");
a.latir();  // ❌ Erro de compilação!
            //    Animal não tem método latir.
            //    Mesmo que o objeto SEJA um Cachorro,
            //    o compilador olha o tipo da REFERÊNCIA.
```

**Solução:** ou use o tipo concreto, ou faça downcasting com `instanceof`:

```java
Cachorro c = new Cachorro("Rex");
c.latir();  // ✅ Tipo estático = Cachorro

// ou

Animal a = new Cachorro("Rex");
if (a instanceof Cachorro) {
    ((Cachorro) a).latir();  // ✅ Downcasting verificado
}
```

### ❌ Erro 2: Esquecer `@Override` e errar a assinatura

```java
public class Animal {
    public void emitirSom() { }
}

public class Cachorro extends Animal {
    public void emitirsom() {  // ❌ "s" minúsculo - método NOVO, não sobrescrita!
        System.out.println("Au au");
    }
}
```

**Resultado:** quando você fizer `Animal a = new Cachorro(); a.emitirSom();` vai chamar a versão **vazia** da superclasse — e nada vai imprimir.

**Solução:** sempre use `@Override`. O compilador detecta o erro:

```java
public class Cachorro extends Animal {
    @Override
    public void emitirsom() { ... }  // ❌ Compilador acusa: "não é sobrescrita!"
}
```

### ❌ Erro 3: Downcasting sem verificar

```java
Animal a = new Gato("Mimi");
Cachorro c = (Cachorro) a;  // ❌ Compila!
c.latir();                   // ❌ ClassCastException em runtime
```

**Solução:** sempre `instanceof` antes:

```java
if (a instanceof Cachorro) {
    Cachorro c = (Cachorro) a;
    c.latir();
}
```

### ❌ Erro 4: Confundir sobrescrita com sobrecarga

```java
public class Animal {
    public void emitirSom() { ... }
}

public class Cachorro extends Animal {
    // ❌ NÃO é sobrescrita - é SOBRECARGA (e não funciona como esperado!)
    public void emitirSom(int volume) {
        System.out.println("Au au com volume " + volume);
    }
}

// Uso:
Animal a = new Cachorro();
a.emitirSom();  // Chama a versão SEM parâmetro de Animal (vazia)
```

**Solução:** sobrescrita exige **mesma assinatura exata**:

```java
public class Cachorro extends Animal {
    @Override
    public void emitirSom() {  // ✅ Mesma assinatura
        System.out.println("Au au!");
    }
}
```

---

## 1.10 Quadro-resumo: o fluxo do polimorfismo

```
┌──────────────────────────────────────────────────────────┐
│  1. Você cria uma hierarquia (Aula 07)                   │
│     Animal ← Cachorro, Gato, Vaca                        │
└──────────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│  2. Sobrescreve métodos com @Override                    │
│     Cada subclasse implementa SUA versão                 │
└──────────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│  3. Cria referência da superclasse                       │
│     Animal a = new Cachorro();                           │
│     (tipo estático = Animal, dinâmico = Cachorro)        │
└──────────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│  4. Chama o método pela referência                       │
│     a.emitirSom();                                       │
└──────────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────┐
│  5. JVM faz dispatch dinâmico                            │
│     Olha o objeto REAL (Cachorro) e chama versão certa   │
│     → "Au au!"                                           │
└──────────────────────────────────────────────────────────┘
```

---

## Resumo do Bloco 1

Neste bloco você aprendeu:

✅ **Polimorfismo** = mesma chamada, comportamentos diferentes
✅ **Sobrescrita** (`@Override`) ≠ Sobrecarga (overload)
✅ **Tipo estático** = referência (compilador) | **Tipo dinâmico** = objeto (JVM)
✅ **Late binding** = JVM decide em runtime qual versão executar
✅ **Listas polimórficas** (`ArrayList<Funcionario>`) aceitam qualquer subtipo
✅ **Upcasting** é implícito e seguro | **Downcasting** exige `instanceof`
✅ **Princípio Aberto/Fechado** = polimorfismo permite estender sem modificar

**Frase para guardar:**
> *"O compilador olha a referência. A JVM olha o objeto."*

---

## Próximo Passo

No **Bloco 2**, você vai **aplicar** tudo isso em um **exercício guiado completo**: Sistema de Folha de Pagamento Polimórfica, fechando a problematização aberta na Aula 07.

[➡️ Ir para Bloco 2](../Bloco2/README.md)
