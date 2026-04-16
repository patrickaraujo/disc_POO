# Bloco 1 — Conceitos de Herança e Hierarquia

## Objetivos do Bloco

- Entender o que é herança e por que ela existe
- Reconhecer a relação "é um" (is-a)
- Aprender a sintaxe `extends`
- Usar `super` para acessar a superclasse
- Conhecer o modificador `protected`
- Criar uma primeira hierarquia simples

---

## 1.1 O que é Herança?

**Herança** é um mecanismo da orientação a objetos que permite criar **novas classes** baseadas em **classes existentes**.

> A classe filha (subclasse) **herda** atributos e métodos da classe pai (superclasse) e pode **adicionar** ou **modificar** comportamentos.

### Analogia do mundo real

Pense na classificação biológica:

```
         Animal
         /    \
      Mamífero  Ave
       /  \      |
   Cachorro Gato Pássaro
```

- **Animal** define características comuns: respirar, se mover, se alimentar
- **Mamífero** herda de Animal e adiciona: amamentar, ter pelos
- **Cachorro** herda de Mamífero e adiciona: latir, abanar rabo

**Cachorro:**
- ✅ Respira (herdado de Animal)
- ✅ Amamenta (herdado de Mamífero)
- ✅ Late (específico de Cachorro)

---

## 1.2 Por que usar Herança?

### Problema: Código Duplicado

Sem herança:

```java
public class Cachorro {
    private String nome;
    private int idade;
    
    public void comer() {
        System.out.println("Comendo...");
    }
    
    public void dormir() {
        System.out.println("Dormindo...");
    }
    
    public void latir() {
        System.out.println("Au au!");
    }
}

public class Gato {
    private String nome;        // ← Duplicado!
    private int idade;          // ← Duplicado!
    
    public void comer() {       // ← Duplicado!
        System.out.println("Comendo...");
    }
    
    public void dormir() {      // ← Duplicado!
        System.out.println("Dormindo...");
    }
    
    public void miar() {
        System.out.println("Miau!");
    }
}
```

### Solução: Extrair para Superclasse

Com herança:

```java
// SUPERCLASSE (características comuns)
public class Animal {
    protected String nome;    // protected = acessível nas subclasses
    protected int idade;
    
    public Animal(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }
    
    public void comer() {
        System.out.println(nome + " está comendo...");
    }
    
    public void dormir() {
        System.out.println(nome + " está dormindo...");
    }
    
    public String getNome() {
        return nome;
    }
    
    public int getIdade() {
        return idade;
    }
}

// SUBCLASSE 1 (especialização)
public class Cachorro extends Animal {
    
    public Cachorro(String nome, int idade) {
        super(nome, idade);  // Chama construtor da superclasse
    }
    
    // Método específico de Cachorro
    public void latir() {
        System.out.println(nome + " faz: Au au!");
    }
}

// SUBCLASSE 2 (outra especialização)
public class Gato extends Animal {
    
    public Gato(String nome, int idade) {
        super(nome, idade);
    }
    
    // Método específico de Gato
    public void miar() {
        System.out.println(nome + " faz: Miau!");
    }
}
```

**Testando:**

```java
public class TesteAnimais {
    public static void main(String[] args) {
        Cachorro dog = new Cachorro("Rex", 3);
        Gato cat = new Gato("Mimi", 2);
        
        // Métodos herdados de Animal
        dog.comer();      // Rex está comendo...
        dog.dormir();     // Rex está dormindo...
        cat.comer();      // Mimi está comendo...
        
        // Métodos específicos
        dog.latir();      // Rex faz: Au au!
        cat.miar();       // Mimi faz: Miau!
        
        // Atributos herdados (através de getter)
        System.out.println(dog.getNome() + " tem " + dog.getIdade() + " anos");
    }
}
```

---

## 1.3 Sintaxe da Herança em Java

### Palavra-chave `extends`

```java
public class Subclasse extends Superclasse {
    // Herda TUDO da superclasse
    // Pode adicionar novos membros
    // Pode sobrescrever métodos
}
```

**Regras importantes:**
1. Java permite apenas **herança simples** (extends de apenas uma classe)
2. A subclasse herda **todos** os membros não-privados
3. A subclasse **NÃO** herda construtores (mas pode chamá-los com `super`)
4. Membros `private` existem, mas não são acessíveis diretamente

---

## 1.4 Palavra-chave `super`

`super` tem dois usos principais:

### 1. Chamar construtor da superclasse

```java
public class Animal {
    protected String nome;
    
    public Animal(String nome) {
        this.nome = nome;
    }
}

public class Cachorro extends Animal {
    private String raca;
    
    public Cachorro(String nome, String raca) {
        super(nome);        // ← DEVE ser a primeira linha!
        this.raca = raca;
    }
}
```

**Regras do `super()` em construtores:**
- ✅ Deve ser a **primeira linha** do construtor
- ✅ Se você não chamar explicitamente, Java chama `super()` (construtor sem parâmetros)
- ❌ Se a superclasse não tem construtor sem parâmetros, você **DEVE** chamar `super(args)`

### 2. Acessar membros da superclasse

```java
public class Animal {
    protected String nome;
    
    public void emitirSom() {
        System.out.println("Som genérico de animal");
    }
}

public class Cachorro extends Animal {
    
    @Override
    public void emitirSom() {
        super.emitirSom();  // Chama versão da superclasse
        System.out.println("Au au!");  // Adiciona comportamento
    }
}
```

---

## 1.5 Modificadores de Acesso: `protected`

| Modificador | Mesma Classe | Mesmo Pacote | Subclasse | Qualquer Lugar |
|-------------|--------------|--------------|-----------|----------------|
| `private` | ✅ | ❌ | ❌ | ❌ |
| **`protected`** | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

**Quando usar `protected`:**
- ✅ Quando subclasses precisam acessar diretamente
- ✅ Para atributos que fazem parte da "essência" da hierarquia
- ❌ Evite se não há necessidade (prefira `private` + getters/setters)

**Exemplo:**

```java
public class Veiculo {
    protected String marca;      // Subclasses podem acessar
    private String chassi;       // Só Veiculo acessa
    
    public Veiculo(String marca, String chassi) {
        this.marca = marca;
        this.chassi = chassi;
    }
}

public class Carro extends Veiculo {
    
    public Carro(String marca, String chassi) {
        super(marca, chassi);
    }
    
    public void exibirInfo() {
        System.out.println("Marca: " + marca);     // ✅ OK (protected)
        // System.out.println(chassi);             // ❌ Erro (private)
    }
}
```

---

## 1.6 Sobrescrita de Métodos (`@Override`)

**Sobrescrever** significa fornecer uma **nova implementação** de um método herdado.

```java
public class Animal {
    public void emitirSom() {
        System.out.println("Som genérico");
    }
}

public class Cachorro extends Animal {
    
    @Override  // Anotação indica sobrescrita
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
```

**Testando:**

```java
Animal a = new Animal();
Cachorro c = new Cachorro("Rex", 3);
Gato g = new Gato("Mimi", 2);

a.emitirSom();  // Som genérico
c.emitirSom();  // Au au!
g.emitirSom();  // Miau!
```

**Regras da sobrescrita:**
1. ✅ Mesmo nome do método
2. ✅ Mesmos parâmetros (quantidade e tipos)
3. ✅ Mesmo tipo de retorno (ou subtipo)
4. ✅ Modificador de acesso igual ou **mais permissivo**
5. ✅ Use `@Override` (não é obrigatório, mas é **altamente recomendado**)

**Por que usar `@Override`?**
- Detecta erros em tempo de compilação
- Deixa claro que é sobrescrita, não método novo
- Facilita manutenção

---

## 1.7 O que é herdado?

| Membro da Superclasse | É herdado? | Acessível na subclasse? |
|----------------------|------------|-------------------------|
| Atributos `public` | ✅ Sim | ✅ Sim |
| Atributos `protected` | ✅ Sim | ✅ Sim |
| Atributos `private` | ⚠️ Existem, mas não acessíveis | ❌ Não |
| Métodos `public` | ✅ Sim | ✅ Sim |
| Métodos `protected` | ✅ Sim | ✅ Sim |
| Métodos `private` | ❌ Não | ❌ Não |
| Construtores | ❌ Não (mas podem ser chamados com `super`) | - |

---

## 1.8 Relação "É Um" (is-a)

Herança modela relacionamentos de **especialização**.

**Teste do "É Um":**
- Cachorro **é um** Animal? ✅ Sim → pode usar herança
- Carro **é um** Veículo? ✅ Sim → pode usar herança
- Quadrado **é um** Retângulo? ⚠️ Sim, mas cuidado (problema clássico)

**Contra-exemplos:**
- Carro **é um** Motor? ❌ Não → Carro **TEM UM** Motor (composição)
- Pessoa **é um** Endereço? ❌ Não → Pessoa **TEM UM** Endereço (agregação)
- Cachorro **é uma** Casa? ❌ Não → sem relação

---

## 1.9 Hierarquia de Classes

Classes podem formar hierarquias com múltiplos níveis:

```
              Animal
             /      \
        Mamifero    Ave
         /    \      |
    Cachorro  Gato  Aguia
```

**Em código:**

```java
public class Animal { }

public class Mamifero extends Animal { }

public class Cachorro extends Mamifero { }
```

**Herança transitiva:**
- `Cachorro` herda de `Mamifero`
- `Cachorro` também herda de `Animal` (transitivamente)

**⚠️ Cuidado:** Hierarquias muito profundas (5+ níveis) são difíceis de manter!

---

## 1.10 Exemplo Completo: Hierarquia de Formas Geométricas

Vamos criar um exemplo prático do zero.

### Superclasse: FormaGeometrica

```java
public class FormaGeometrica {
    protected String cor;
    
    public FormaGeometrica(String cor) {
        this.cor = cor;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    // Método genérico (será sobrescrito)
    public double calcularArea() {
        return 0.0;
    }
    
    // Método genérico (será sobrescrito)
    public double calcularPerimetro() {
        return 0.0;
    }
    
    public void exibirInfo() {
        System.out.println("Forma: " + this.getClass().getSimpleName());
        System.out.println("Cor: " + cor);
        System.out.println("Área: " + calcularArea());
        System.out.println("Perímetro: " + calcularPerimetro());
    }
}
```

### Subclasse 1: Retangulo

```java
public class Retangulo extends FormaGeometrica {
    private double largura;
    private double altura;
    
    public Retangulo(String cor, double largura, double altura) {
        super(cor);  // Chama construtor da superclasse
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
    
    // Getters específicos
    public double getLargura() { return largura; }
    public double getAltura() { return altura; }
}
```

### Subclasse 2: Circulo

```java
public class Circulo extends FormaGeometrica {
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

### Subclasse 3: Triangulo

```java
public class Triangulo extends FormaGeometrica {
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
public class TesteFormas {
    public static void main(String[] args) {
        
        Retangulo ret = new Retangulo("Azul", 5.0, 3.0);
        Circulo circ = new Circulo("Vermelho", 4.0);
        Triangulo tri = new Triangulo("Verde", 3.0, 4.0, 5.0);
        
        System.out.println("=== RETÂNGULO ===");
        ret.exibirInfo();  // Método herdado!
        
        System.out.println("\n=== CÍRCULO ===");
        circ.exibirInfo();
        
        System.out.println("\n=== TRIÂNGULO ===");
        tri.exibirInfo();
    }
}
```

**Saída:**
```
=== RETÂNGULO ===
Forma: Retangulo
Cor: Azul
Área: 15.0
Perímetro: 16.0

=== CÍRCULO ===
Forma: Circulo
Cor: Vermelho
Área: 50.26548245743669
Perímetro: 25.132741228718345

=== TRIÂNGULO ===
Forma: Triangulo
Cor: Verde
Área: 6.0
Perímetro: 12.0
```

**O que observar:**
1. ✅ Todas as formas **herdam** `cor` e `exibirInfo()`
2. ✅ Cada forma **sobrescreve** `calcularArea()` e `calcularPerimetro()`
3. ✅ O método `exibirInfo()` funciona para todas sem modificação
4. ✅ Evitamos duplicação de código

---

## 1.11 Erros Comuns

### ❌ Erro 1: Esquecer `super()` no construtor

```java
public class Cachorro extends Animal {
    private String raca;
    
    public Cachorro(String nome, String raca) {
        // ❌ Esqueceu super(nome)
        this.raca = raca;
        // Compilador tenta chamar super() sem argumentos
        // Se Animal não tem construtor sem args, dá erro!
    }
}

// ✅ CORRETO
public Cachorro(String nome, String raca) {
    super(nome);  // Primeira linha!
    this.raca = raca;
}
```

### ❌ Erro 2: Sobrescrever com assinatura diferente

```java
public class Animal {
    public void emitirSom() { }
}

public class Cachorro extends Animal {
    // ❌ ERRADO - parâmetros diferentes (não é sobrescrita!)
    public void emitirSom(String volume) {
        System.out.println("Au au " + volume);
    }
}

// ✅ CORRETO - mesma assinatura
@Override
public void emitirSom() {
    System.out.println("Au au!");
}
```

### ❌ Erro 3: Usar herança para "tem um"

```java
// ❌ ERRADO - Carro NÃO É UM Motor!
public class Carro extends Motor {
    // ...
}

// ✅ CORRETO - Carro TEM UM Motor (composição)
public class Carro {
    private Motor motor;
    // ...
}
```

---

## Resumo do Bloco 1

Neste bloco você aprendeu:

✅ **Herança** = mecanismo de reuso baseado em "é um"  
✅ **`extends`** = palavra-chave para herdar  
✅ **`super`** = acessa membros da superclasse  
✅ **`protected`** = acessível na própria classe + subclasses  
✅ **`@Override`** = sobrescreve métodos herdados  
✅ **Hierarquia** = superclasse (comum) → subclasses (específicas)  

**Teste crucial:** Se a relação não for "é um", **NÃO use herança!**

---

## Próximo Passo

No **Bloco 2**, você vai **aplicar** tudo isso em um **exercício guiado completo**: Sistema de Veículos com múltiplas especializações.

[➡️ Ir para Bloco 2](../Bloco2/README.md)
