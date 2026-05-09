# Bloco 1 — Conceitos de Classes Abstratas e Template Method

## Objetivos do Bloco

- Entender o que é uma classe abstrata e por que ela existe
- Diferenciar **classe abstrata** de **classe concreta**
- Aprender a sintaxe de `abstract class` e `abstract method`
- Misturar métodos abstratos e concretos na mesma classe
- Entender por que **não se pode instanciar** uma classe abstrata
- Aplicar o **Template Method Pattern**
- Comparar (preview) classes abstratas e interfaces
- Construir uma primeira hierarquia abstrata completa

---

## 1.1 O que é uma Classe Abstrata?

Uma **classe abstrata** representa um **conceito genérico** que **não existe na realidade de forma concreta**.

> Pense em "Animal", "Veículo" ou "FormaGeométrica". Esses conceitos **existem na sua mente**, mas **nunca na realidade pura**. Você nunca encontra um "animal" sem ser cachorro, gato, pássaro ou alguma espécie específica. O conceito puro existe só como **categoria**, não como **objeto**.

### Analogia do mundo real

Imagine ir a uma loja e pedir: "Quero comprar **um veículo**, por favor."

```
   Cliente: "Quero um veículo"
   Vendedor: "Que tipo? Carro? Moto? Caminhão?"
   Cliente: "Não, só um veículo. Genérico."
   Vendedor: "...isso não existe."
```

**"Veículo" puro não existe.** Existe carro, moto, caminhão. Cada um **é um veículo**, mas o conceito "veículo" sozinho é abstrato.

Em Java, uma classe abstrata **proíbe** que você crie esse "veículo genérico":

```java
public abstract class Veiculo { ... }

Veiculo v = new Veiculo(...);  // ❌ Erro de compilação
Veiculo v = new Carro(...);    // ✅ OK — Carro é concreto
```

---

## 1.2 Por que Classes Abstratas existem?

Vamos retomar o problema da Aula 08. Veja este código:

```java
public class Funcionario {
    protected String nome;
    protected String cpf;

    public double calcularSalario() {
        return 0.0;  // ← Implementação "falsa"
    }
}
```

### Problema 1: Instanciação sem sentido

```java
Funcionario f = new Funcionario("João", "111.111.111-11");
f.calcularSalario();  // R$ 0,00 — completamente sem sentido
```

Esse `Funcionario` não é CLT, não é Horista, não é Comissionado. **Não é nada.** Mas o Java permite criar.

### Problema 2: Subclasse pode "esquecer" de implementar

```java
public class FuncionarioTerceirizado extends Funcionario {
    private double valorContrato;
    // ❌ Esqueci de sobrescrever calcularSalario()!
}

FuncionarioTerceirizado t = new FuncionarioTerceirizado(...);
t.calcularSalario();  // R$ 0,00 — herdou da superclasse, bug silencioso
```

O compilador **não avisa** que faltou implementar. O bug só aparece em produção.

### Problema 3: Mentira no código

`return 0.0;` em `calcularSalario()` é uma **mentira**. Não existe "salário de Funcionario genérico". Só existe salário de **algum tipo específico**. Mas o método finge que sim, só para o código compilar.

### Solução: declarar a classe e o método como abstratos

```java
public abstract class Funcionario {            // ← classe abstrata
    protected String nome;
    protected String cpf;

    public abstract double calcularSalario();  // ← método abstrato (sem corpo)
}
```

Agora o compilador:
1. ❌ Proíbe `new Funcionario(...)` — mensagem clara de erro
2. ❌ Proíbe `class FuncionarioTerceirizado extends Funcionario` sem implementar `calcularSalario()`
3. ✅ Não exige mais o `return 0.0` falso

**O design vira uma promessa que o compilador faz cumprir.**

---

## 1.3 Sintaxe da Classe Abstrata em Java

### Palavra-chave `abstract`

```java
public abstract class NomeDaClasse {
    // pode ter atributos
    // pode ter construtores (chamados via super pelas subclasses)
    // pode ter métodos concretos
    // pode ter métodos abstratos
}
```

**Regras importantes:**
1. A classe é declarada com `abstract` **antes** de `class`
2. Só pode ser **estendida**, nunca **instanciada** diretamente
3. Pode (mas não precisa) ter métodos abstratos
4. Pode ter construtores normais — eles são chamados via `super(...)` pelas subclasses concretas

---

## 1.4 Métodos Abstratos

Um método abstrato é uma **assinatura sem implementação**: declara o que existe, mas não diz como funciona.

```java
public abstract double calcularSalario();
//     ^                 ^                ^
//     |                 |                └── ponto-e-vírgula (NÃO chaves)
//     |                 └─── nome e parâmetros normais
//     └─── palavra-chave abstract
```

**Compare:**

```java
// Método CONCRETO (com corpo)
public double calcularSalario() {
    return 1000.0;
}

// Método ABSTRATO (sem corpo)
public abstract double calcularSalario();
```

**Regras dos métodos abstratos:**
1. ✅ Devem ser declarados com `abstract`
2. ✅ Não têm corpo `{ }` — terminam em `;`
3. ✅ Só podem existir em **classes abstratas**
4. ❌ Não podem ser `private` (subclasses precisam acessar para implementar)
5. ❌ Não podem ser `static` (não fazem sentido sem objeto)
6. ❌ Não podem ser `final` (precisam ser sobrescritos)

> ⚠️ Se uma classe **não é abstrata** mas tem **um único método abstrato**, o compilador exige que você marque a classe como abstrata também.

---

## 1.5 Mistura: abstrato + concreto na mesma classe

Esta é uma das partes **mais poderosas** das classes abstratas. Você pode misturar:

- Atributos comuns
- Construtores
- Métodos concretos (com corpo)
- Métodos abstratos (sem corpo)

**Exemplo:**

```java
public abstract class Forma {
    protected String cor;        // ← atributo concreto

    public Forma(String cor) {   // ← construtor concreto
        this.cor = cor;
    }

    public String getCor() {     // ← método concreto
        return cor;
    }

    public abstract double calcularArea();      // ← método abstrato
    public abstract double calcularPerimetro(); // ← método abstrato

    public void exibir() {       // ← método concreto que USA os abstratos
        System.out.println("Cor: " + cor);
        System.out.println("Área: " + calcularArea());           // polimorfismo!
        System.out.println("Perímetro: " + calcularPerimetro()); // polimorfismo!
    }
}
```

**Por que isso é interessante?**
- O método `exibir()` está implementado **uma única vez** na superclasse
- Ele chama `calcularArea()` e `calcularPerimetro()` — que são abstratos
- Quando uma subclasse concreta (ex: `Circulo`) for usada, o **dispatch dinâmico** (Aula 08) garante que as versões corretas são chamadas

**Combinação perfeita:** classe abstrata + polimorfismo = código reutilizável **e** seguro.

---

## 1.6 Por que NÃO se pode instanciar Classes Abstratas?

```java
public abstract class Forma {
    public abstract double calcularArea();
}

Forma f = new Forma("vermelho");  // ❌ Erro de compilação
```

A pergunta natural é: **por quê?**

**Imagine se fosse permitido:**

```java
Forma f = new Forma("vermelho");
f.calcularArea();  // 🚨 O QUE ACONTECE AQUI?
```

`calcularArea()` é abstrato — não tem implementação. Se o objeto fosse criado, a JVM não saberia o que fazer ao chamar esse método. **Crash garantido.**

Por isso o Java toma uma decisão segura: **proibir a criação no compilador**, antes mesmo do programa rodar.

> 💡 **Mas atenção:** você **pode** ter uma **referência** do tipo da classe abstrata. Só não pode criar **objeto** com `new` da abstrata.

```java
Forma f;                       // ✅ OK — só uma referência
f = new Circulo("azul", 5.0);  // ✅ OK — objeto é Circulo (concreto)
                               //         referência é Forma (abstrata)

f.calcularArea();              // ✅ OK — chama versão de Circulo (polimorfismo!)
```

Isso é **fundamental** para usar classes abstratas em coleções polimórficas:

```java
ArrayList<Forma> formas = new ArrayList<>();
formas.add(new Circulo("azul", 5.0));
formas.add(new Retangulo("verde", 3.0, 4.0));
formas.add(new Triangulo("vermelho", 3.0, 4.0, 5.0));

for (Forma f : formas) {
    f.exibir();  // Cada um exibe com sua própria área
}
```

---

## 1.7 Subclasses concretas DEVEM implementar todos os métodos abstratos

Quando uma subclasse herda de uma classe abstrata, ela **precisa** implementar todos os métodos abstratos pendentes — ou então **ela própria** precisa ser declarada abstrata.

```java
public abstract class Forma {
    public abstract double calcularArea();
    public abstract double calcularPerimetro();
}

// ✅ CORRETO — implementa AMBOS os métodos abstratos
public class Circulo extends Forma {
    private double raio;

    public Circulo(double raio) { this.raio = raio; }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * raio;
    }
}

// ❌ ERRADO — falta implementar calcularPerimetro()
public class Triangulo extends Forma {
    @Override
    public double calcularArea() { return 0; }
    // Compilador: "Triangulo não é abstrata e não implementa calcularPerimetro"
}

// ✅ ALTERNATIVA — declarar a subclasse também como abstrata
public abstract class Triangulo extends Forma {
    @Override
    public double calcularArea() { return 0; }
    // calcularPerimetro continua abstrato — outra subclasse vai implementar
}
```

**Resumindo a regra:**

| Subclasse implementa **todos** os abstratos? | A subclasse pode ser concreta? |
|---|---|
| Sim | ✅ Sim |
| Não | ❌ Precisa ser abstrata também |

---

## 1.8 Template Method Pattern

Esta é a aplicação **mais elegante** de classes abstratas. É um **padrão de projeto** muito usado.

### O conceito

> **Template Method:** define o **esqueleto** de um algoritmo na superclasse, deixando que subclasses preencham os passos específicos sem alterar a estrutura geral.

### Exemplo: preparar uma bebida quente

Imagine três bebidas: chá, café e chocolate quente. O processo geral é o mesmo:

```
1. Ferver água
2. Colocar a base no copo (folhas/pó/cacau)
3. Despejar água quente
4. Adicionar complementos (limão/leite/marshmallow)
```

Os passos 1 e 3 são **iguais** para todas. Os passos 2 e 4 são **específicos** de cada bebida.

### Implementação

```java
public abstract class BebidaQuente {

    // ESTE é o Template Method (final = subclasses não podem alterar a ordem)
    public final void preparar() {
        ferverAgua();
        colocarBase();           // específico (abstrato)
        despejarAguaQuente();
        adicionarComplementos(); // específico (abstrato)
    }

    // Passos comuns (concretos)
    private void ferverAgua() {
        System.out.println("Fervendo a água...");
    }

    private void despejarAguaQuente() {
        System.out.println("Despejando água quente no copo...");
    }

    // Passos específicos (abstratos)
    protected abstract void colocarBase();
    protected abstract void adicionarComplementos();
}
```

```java
public class Cha extends BebidaQuente {
    @Override
    protected void colocarBase() {
        System.out.println("Colocando saquinho de chá...");
    }

    @Override
    protected void adicionarComplementos() {
        System.out.println("Adicionando limão e mel.");
    }
}
```

```java
public class Cafe extends BebidaQuente {
    @Override
    protected void colocarBase() {
        System.out.println("Colocando pó de café...");
    }

    @Override
    protected void adicionarComplementos() {
        System.out.println("Adicionando leite e açúcar.");
    }
}
```

### Usando

```java
BebidaQuente cha = new Cha();
cha.preparar();
// Fervendo a água...
// Colocando saquinho de chá...
// Despejando água quente no copo...
// Adicionando limão e mel.

BebidaQuente cafe = new Cafe();
cafe.preparar();
// Fervendo a água...
// Colocando pó de café...
// Despejando água quente no copo...
// Adicionando leite e açúcar.
```

### Por que isso é tão poderoso?

✅ **A ordem dos passos** é definida **uma única vez** na superclasse
✅ Subclasses **não podem alterar** a ordem (`final` no método template)
✅ Subclasses só **preenchem** os pontos de variação
✅ Adicionar nova bebida = criar nova subclasse, **sem mexer no fluxo**
✅ É a base de muitos frameworks (Spring, JUnit, Servlets...)

> 💎 **Lembre-se:** sempre que você identificar um **fluxo comum com pequenas variações**, considere Template Method. É um dos padrões mais aplicáveis no dia a dia.

---

## 1.9 Construtores em Classes Abstratas

Apesar de **não serem instanciáveis**, classes abstratas **podem (e geralmente devem) ter construtores**. Esses construtores são chamados pelas subclasses concretas via `super(...)`.

```java
public abstract class Forma {
    protected String cor;

    public Forma(String cor) {       // ✅ Construtor em classe abstrata
        this.cor = cor;
        System.out.println("Forma criada com cor " + cor);
    }

    public abstract double calcularArea();
}

public class Circulo extends Forma {
    private double raio;

    public Circulo(String cor, double raio) {
        super(cor);                   // ✅ Chama construtor da abstrata
        this.raio = raio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }
}

// Uso:
Circulo c = new Circulo("azul", 5.0);
// Saída: "Forma criada com cor azul"
```

> 💡 Construtores em classes abstratas servem para **inicializar atributos comuns**. Eles **não criam** o objeto da abstrata — apenas participam da construção do objeto da subclasse concreta.

---

## 1.10 Exemplo Completo: Hierarquia de Formas Geométricas (Refatoração)

Vamos refatorar o exemplo de `Forma` para usar classes abstratas corretamente.

### Superclasse abstrata: Forma

```java
public abstract class Forma {

    protected String cor;

    public Forma(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }

    // Métodos abstratos — toda forma DEVE implementar
    public abstract double calcularArea();
    public abstract double calcularPerimetro();

    // Método concreto que usa os abstratos (Template Method simplificado)
    public final void exibirRelatorio() {
        System.out.println("=== Relatório de " + this.getClass().getSimpleName() + " ===");
        System.out.println("Cor: " + cor);
        System.out.printf("Área: %.2f%n", calcularArea());
        System.out.printf("Perímetro: %.2f%n", calcularPerimetro());
        System.out.println("==============================");
    }
}
```

### Subclasse concreta 1: Circulo

```java
public class Circulo extends Forma {
    private double raio;

    public Circulo(String cor, double raio) {
        super(cor);
        this.raio = raio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * raio;
    }

    public double getRaio() { return raio; }
}
```

### Subclasse concreta 2: Retangulo

```java
public class Retangulo extends Forma {
    private double largura;
    private double altura;

    public Retangulo(String cor, double largura, double altura) {
        super(cor);
        this.largura = largura;
        this.altura = altura;
    }

    @Override
    public double calcularArea() {
        return largura * altura;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * (largura + altura);
    }
}
```

### Subclasse concreta 3: Triangulo

```java
public class Triangulo extends Forma {
    private double lado1;
    private double lado2;
    private double lado3;

    public Triangulo(String cor, double lado1, double lado2, double lado3) {
        super(cor);
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
    }

    @Override
    public double calcularArea() {
        // Fórmula de Heron
        double s = calcularPerimetro() / 2;
        return Math.sqrt(s * (s - lado1) * (s - lado2) * (s - lado3));
    }

    @Override
    public double calcularPerimetro() {
        return lado1 + lado2 + lado3;
    }
}
```

### Testando a hierarquia

```java
import java.util.ArrayList;

public class TesteFormas {
    public static void main(String[] args) {

        // Forma f = new Forma("preto");  // ❌ Erro de compilação

        ArrayList<Forma> formas = new ArrayList<>();
        formas.add(new Circulo("azul", 5.0));
        formas.add(new Retangulo("verde", 3.0, 4.0));
        formas.add(new Triangulo("vermelho", 3.0, 4.0, 5.0));

        for (Forma f : formas) {
            f.exibirRelatorio();  // método concreto da abstrata
        }

        // Soma de áreas usando polimorfismo
        double areaTotal = 0;
        for (Forma f : formas) {
            areaTotal += f.calcularArea();
        }
        System.out.printf("%nÁrea total: %.2f%n", areaTotal);
    }
}
```

**Saída:**

```
=== Relatório de Circulo ===
Cor: azul
Área: 78,54
Perímetro: 31,42
==============================
=== Relatório de Retangulo ===
Cor: verde
Área: 12,00
Perímetro: 14,00
==============================
=== Relatório de Triangulo ===
Cor: vermelho
Área: 6,00
Perímetro: 12,00
==============================

Área total: 96,54
```

**O que observar:**
1. ✅ `Forma` é abstrata — não foi possível criar `new Forma()`
2. ✅ Cada subclasse implementou os métodos abstratos
3. ✅ `exibirRelatorio()` (concreto, herdado) usa os métodos abstratos polimorficamente
4. ✅ `ArrayList<Forma>` aceita qualquer subclasse concreta
5. ✅ O design exprime exatamente a intenção: "Forma é um conceito abstrato, cada tipo concreto define como calcula sua área"

---

## 1.11 Quando usar Classe Abstrata vs Concreta vs Interface (Preview)

| Situação | Use |
|----------|-----|
| Classe que pode existir por si só | Classe **concreta** |
| Várias classes compartilham **estado + comportamento parcial** | Classe **abstrata** |
| Há um **fluxo comum** com pontos de variação | Classe **abstrata** + Template Method |
| Precisa **forçar implementação** de certos métodos | Classe **abstrata** ou interface |
| Várias classes não relacionadas precisam de **um mesmo contrato** | **Interface** (Aula 10) |
| Precisa de "herança múltipla" de comportamento | **Interface** (Aula 10) |

### Resumindo em uma frase

> *Use **classe concreta** quando faz sentido instanciar; **classe abstrata** quando há base comum mas a versão genérica não existe; **interface** quando só precisa definir um contrato (sem estado).*

---

## 1.12 Erros Comuns

### ❌ Erro 1: Tentar instanciar uma classe abstrata

```java
public abstract class Forma { ... }

Forma f = new Forma("preto");  // ❌ Erro de compilação
```

**Solução:** instancie uma subclasse concreta:

```java
Forma f = new Circulo("preto", 5.0);  // ✅ OK
```

---

### ❌ Erro 2: Esquecer de implementar todos os métodos abstratos

```java
public abstract class Forma {
    public abstract double calcularArea();
    public abstract double calcularPerimetro();
}

public class Circulo extends Forma {  // ❌ Erro: falta calcularPerimetro
    @Override
    public double calcularArea() {
        return Math.PI * 5 * 5;
    }
}
```

**Mensagem do compilador:** `Circulo is not abstract and does not override abstract method calcularPerimetro() in Forma`

**Solução:** implementar **todos** os métodos abstratos, ou tornar a subclasse abstrata também.

---

### ❌ Erro 3: Método abstrato com corpo

```java
public abstract class Forma {
    public abstract double calcularArea() { return 0; }  // ❌ Erro
}
```

**Solução:** método abstrato termina em `;`, não tem `{ }`:

```java
public abstract double calcularArea();  // ✅ OK
```

---

### ❌ Erro 4: Método abstrato em classe concreta

```java
public class Forma {  // ❌ Não é abstract
    public abstract double calcularArea();  // ❌ Erro
}
```

**Mensagem do compilador:** `abstract method in non-abstract class`

**Solução:** declarar a classe como abstrata:

```java
public abstract class Forma {
    public abstract double calcularArea();
}
```

---

### ❌ Erro 5: Usar `private` em método abstrato

```java
public abstract class Forma {
    private abstract double calcularArea();  // ❌ Erro
}
```

**Por quê?** Métodos `private` não são herdados. Se ninguém pode herdar, ninguém pode implementar — então o método nunca seria realizado.

**Solução:** use `public` ou `protected`:

```java
public abstract class Forma {
    protected abstract double calcularArea();  // ✅ OK
}
```

---

## 1.13 Quadro-resumo: classe concreta vs abstrata

```
┌────────────────────────────────────────────────────────┐
│              CLASSE CONCRETA                           │
├────────────────────────────────────────────────────────┤
│  • Pode ser instanciada com new                        │
│  • Todos os métodos têm corpo                          │
│  • Pode ser estendida ou não                           │
│  • Representa entidades reais                          │
│                                                        │
│  Exemplo: String, Integer, Cliente, Pedido             │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│              CLASSE ABSTRATA                           │
├────────────────────────────────────────────────────────┤
│  • NÃO pode ser instanciada com new                    │
│  • Pode ter métodos abstratos (sem corpo)              │
│  • Pode ter métodos concretos                          │
│  • Pode ter atributos e construtores                   │
│  • Existe para ser estendida                           │
│  • Subclasses concretas DEVEM implementar abstratos    │
│                                                        │
│  Exemplo: Forma, Funcionario, Notificacao              │
└────────────────────────────────────────────────────────┘
```

---

## Resumo do Bloco 1

Neste bloco você aprendeu:

✅ **Classe abstrata** = conceito genérico, não instanciável
✅ **`abstract class`** = palavra-chave para declarar
✅ **Método abstrato** = assinatura sem corpo, terminada em `;`
✅ **Mistura** = classe abstrata pode ter abstratos + concretos + atributos + construtores
✅ **Subclasses concretas** = obrigadas a implementar todos os abstratos
✅ **Template Method** = fluxo comum na superclasse, pontos de variação nas subclasses
✅ **Polimorfismo** continua valendo (referência abstrata, objeto concreto)

**Frase para guardar:**
> *"Classe abstrata é uma promessa que o compilador faz cumprir."*

---

## Próximo Passo

No **Bloco 2**, você vai **aplicar** tudo isso em um **exercício guiado completo**: Sistema de Notificações com Template Method (SMS, Email, Push, WhatsApp).

[➡️ Ir para Bloco 2](../Bloco2/README.md)
