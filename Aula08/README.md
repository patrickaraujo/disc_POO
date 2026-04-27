# Aula 08 — Fundamentos de OO: Polimorfismo

## 🎯 Problematização: Processando a folha de pagamento da empresa

Lembra do Sistema de RH da Aula 07? Você criou a hierarquia de funcionários:

```
                    ┌─────────────────┐
                    │   Funcionario   │
                    └────────┬────────┘
                             │
                ┌────────────┼─────────────────┐
                │            │                 │
        ┌───────▼──────┐ ┌───▼──────────────┐ ┌▼──────────────────────┐
        │FuncionarioCLT│ │FuncionarioHorista│ │FuncionarioComissionado│
        └──────────────┘ └──────────────────┘ └───────────────────────┘
```

E sobrescreveu `calcularSalario()` em cada subclasse. Agora chegou o **dia do pagamento**: o RH precisa processar a folha de **todos** os 500 funcionários da empresa de uma vez.

**Como você faria isso sem polimorfismo?**

```java
// ❌ Código que vamos evitar
public void processarFolha(FuncionarioCLT[] clts,
                           FuncionarioHorista[] horistas,
                           FuncionarioComissionado[] comissionados) {

    double total = 0;

    for (int i = 0; i < clts.length; i++) {
        double salario = clts[i].calcularSalario();
        System.out.println(clts[i].getNome() + ": R$ " + salario);
        total += salario;
    }

    for (int i = 0; i < horistas.length; i++) {
        double salario = horistas[i].calcularSalario();
        System.out.println(horistas[i].getNome() + ": R$ " + salario);
        total += salario;
    }

    for (int i = 0; i < comissionados.length; i++) {
        double salario = comissionados[i].calcularSalario();
        System.out.println(comissionados[i].getNome() + ": R$ " + salario);
        total += salario;
    }
}
```

**Problemas:**
1. ❌ Três arrays separados — código repetido
2. ❌ Três loops idênticos (só muda o array)
3. ❌ Se criar um novo tipo (ex: `FuncionarioEstagiario`), precisa **alterar este método**
4. ❌ Não consigo ordenar todos os funcionários por salário
5. ❌ Viola o **Princípio Aberto/Fechado** (aberto a extensão, fechado a modificação)

### Como o Polimorfismo resolve isso

Com polimorfismo, você trata **todos** como `Funcionario` e o Java escolhe **automaticamente** a versão correta de `calcularSalario()` em tempo de execução:

```java
// ✅ Com polimorfismo
public void processarFolha(Funcionario[] funcionarios) {

    double total = 0;

    for (int i = 0; i < funcionarios.length; i++) {
        // Java chama a versão correta automaticamente!
        // CLT → versão da CLT
        // Horista → versão da Horista
        // Comissionado → versão da Comissionado
        double salario = funcionarios[i].calcularSalario();

        System.out.println(funcionarios[i].getNome() + ": R$ " + salario);
        total += salario;
    }

    System.out.println("Total da folha: R$ " + total);
}
```

**Vantagens:**
✅ **Um único array** com todos os funcionários
✅ **Um único loop** que funciona para qualquer tipo
✅ Adicionar novo tipo (ex: `FuncionarioEstagiario`) **NÃO** quebra este método
✅ Posso ordenar, filtrar, somar — tudo de forma genérica
✅ Código mais **simples** e mais **flexível**

**Isso é o poder do Polimorfismo!**

> **A pergunta-chave da Aula 08:** Como o Java sabe **qual** `calcularSalario()` chamar, se a referência é do tipo `Funcionario`? A resposta envolve **tipo estático vs tipo dinâmico** e o **dispatch em tempo de execução**.

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
Aula 08 → ★ Você está aqui: Polimorfismo (@Override em runtime)
Aula 09 → Classes Abstratas
```

A Aula 07 deu a você a **estrutura** (hierarquia + sobrescrita). A Aula 08 mostra **o que essa estrutura permite fazer**: tratar muitos tipos diferentes através de uma única referência comum, com cada objeto se comportando de acordo com seu tipo real.

---

## Objetivos da Aula

Ao final desta aula, você será capaz de:

- [ ] Diferenciar **sobrescrita** (`@Override`) de **sobrecarga** (overload)
- [ ] Distinguir **tipo estático** (declaração) de **tipo dinâmico** (objeto real)
- [ ] Explicar como funciona o **polimorfismo em tempo de execução**
- [ ] Usar **listas polimórficas** (`ArrayList<Funcionario>`) para tratar tipos heterogêneos
- [ ] Aplicar **upcasting** implícito e **downcasting** com `instanceof`
- [ ] Entender por que o polimorfismo é a **base de Design Patterns**

---

## Organização da Aula

| Bloco | Tema | Formato | Tempo estimado |
|-------|------|---------|----------------|
| [Bloco 1](./Bloco1/README.md) | Conceitos de Polimorfismo + Tipo Estático/Dinâmico | Explicação + exemplo simples | 50–60 min |
| [Bloco 2](./Bloco2/README.md) | Exercício Guiado Completo: Folha de Pagamento Polimórfica | Codificação guiada passo a passo | 90–120 min |
| [Bloco 3](./Bloco3/README.md) | Exercício Autônomo: Sistema de Pagamentos (PIX, Boleto, Cartão) | Desenvolvimento independente | 90–120 min |

> **Observação:** Os Blocos 2 e 3 contêm exercícios substanciais que podem ocupar uma aula inteira cada um. O Bloco 1 foca em **entender** o mecanismo do polimorfismo (incluindo o que acontece "por baixo dos panos" na JVM), enquanto os Blocos 2 e 3 focam em **aplicar** através de projetos completos.

---

## Dinâmica da aula

### Bloco 1 — Fundamentos (1ª aula: ~50 min)
1. Recapitulação: sobrescrita (`@Override`) da Aula 07
2. **Distinção crucial:** tipo estático (referência) × tipo dinâmico (objeto)
3. Polimorfismo em tempo de execução (late binding)
4. Listas polimórficas: `ArrayList<Animal>` aceita Cachorro e Gato
5. Upcasting implícito e downcasting com `instanceof`
6. Exemplo simples: hierarquia Animal → Cachorro/Gato/Pássaro

### Bloco 2 — Exercício Guiado (2ª aula: 90-120 min)
**Professor codifica junto com alunos:**
- Sistema completo de Folha de Pagamento
- Hierarquia `Funcionario` (CLT, Horista, Comissionado, Estagiário)
- `ArrayList<Funcionario>` processado polimorficamente
- Demonstração visual de tipo estático vs dinâmico
- Adição de novo tipo **sem alterar** código existente

### Bloco 3 — Exercício Autônomo (3ª aula: 90-120 min)
**Alunos desenvolvem com supervisão:**
- Sistema completo de Pagamentos (PIX, Boleto, Cartão de Crédito, Cartão de Débito)
- Requisitos detalhados fornecidos
- Professor circula tirando dúvidas
- Apresentação de soluções ao final

---

## Como estudar esta aula

1. **Bloco 1:** Foque em **entender por que** o Java sabe qual método chamar (não decore termos)
2. **Bloco 2:** **Acompanhe** o professor digitando junto (não copie/cole)
3. **Bloco 3:** **Pense primeiro** na hierarquia, depois nos comportamentos polimórficos

💡 **Dica crucial:** Polimorfismo só funciona porque você criou **uma boa hierarquia** na Aula 07. Se a herança estiver errada, o polimorfismo não vai te salvar!

---

## O que NÃO será abordado ainda

- **Classes abstratas** (`abstract class`, métodos sem corpo) → Aula 09
- **Interfaces** (`interface`, `implements`) → Aula 10
- **Polimorfismo paramétrico** (Generics avançados, `<T>`) → Aulas futuras
- **Sobrecarga avançada** (regras de resolução do compilador) → leitura complementar

Nesta aula, o foco é no **polimorfismo de subtipo** (também chamado de polimorfismo de inclusão), que é a forma mais usada no dia a dia de quem programa com OO.

---

## Conceitos-chave que você vai dominar

### 1. Sobrescrita (`@Override`) revisitada

```java
public class Funcionario {
    public double calcularSalario() {
        return 0.0;
    }
}

public class FuncionarioCLT extends Funcionario {
    private double salarioBase;

    @Override
    public double calcularSalario() {
        return salarioBase;
    }
}
```

> **Sobrescrita ≠ Sobrecarga.** Sobrescrita é mesma assinatura em **classes diferentes** (subclasse redefine). Sobrecarga é assinaturas diferentes na **mesma classe** (mesmo nome, parâmetros diferentes).

### 2. Tipo Estático × Tipo Dinâmico

```java
Funcionario f = new FuncionarioCLT("Ana", "111", 5000);
//    ^                ^
//    |                └─── Tipo DINÂMICO (objeto real na memória)
//    └────────────────────  Tipo ESTÁTICO (declaração da referência)

f.calcularSalario();  // Chama versão de FuncionarioCLT
                      // (decisão em tempo de EXECUÇÃO)
```

| Aspecto | Tipo Estático | Tipo Dinâmico |
|---------|---------------|---------------|
| O que é | Tipo da **referência** | Tipo do **objeto** real |
| Quem decide | Compilador | JVM em tempo de execução |
| Influencia | Quais métodos podem ser **chamados** | Qual implementação **executa** |

### 3. Polimorfismo em tempo de execução

Quando você chama `f.calcularSalario()`, o Java:
1. Olha o **tipo dinâmico** do objeto (não a referência!)
2. Busca o método na classe real do objeto
3. Se não achar, sobe na hierarquia (subclasse → superclasse)
4. Executa a versão **mais específica** encontrada

**Isso é chamado de:**
- **Late binding** (ligação tardia)
- **Dynamic dispatch** (despacho dinâmico)
- **Polimorfismo em tempo de execução** (runtime polymorphism)

### 4. Listas polimórficas

```java
// Uma lista que aceita QUALQUER subtipo de Funcionario
ArrayList<Funcionario> folha = new ArrayList<>();

folha.add(new FuncionarioCLT("Ana", "111", 5000));
folha.add(new FuncionarioHorista("Bruno", "222", 50, 160));
folha.add(new FuncionarioComissionado("Carla", "333", 2000, 30000, 0.05));

for (Funcionario f : folha) {
    System.out.println(f.getNome() + ": R$ " + f.calcularSalario());
    // Cada um chama SUA versão de calcularSalario!
}
```

### 5. Upcasting e Downcasting

```java
// UPCASTING (implícito, sempre seguro)
Funcionario f = new FuncionarioCLT(...);  // CLT → Funcionario

// DOWNCASTING (explícito, perigoso, exige verificação)
if (f instanceof FuncionarioCLT) {
    FuncionarioCLT clt = (FuncionarioCLT) f;
    clt.metodoEspecificoDeCLT();
}
```

---

## Quando o Polimorfismo brilha?

✅ **Use polimorfismo quando:**
- Várias classes compartilham uma operação **conceitualmente igual**, mas com implementações diferentes (`calcularSalario`, `desenhar`, `processarPagamento`)
- Você precisa processar uma **coleção heterogênea** de objetos
- Você quer **estender o sistema sem modificar** código existente
- Está aplicando padrões de projeto (Strategy, Template Method, Observer...)

❌ **Polimorfismo NÃO faz milagre:**
- Não conserta hierarquias mal projetadas
- Não substitui boa modelagem
- Não resolve casos onde os comportamentos são **realmente** diferentes (não há nada em comum)

---

## Resultado esperado ao final da Aula 08

Ao terminar, você deve conseguir:

✅ Ler `Funcionario f = new FuncionarioCLT(...)` e identificar tipo estático/dinâmico
✅ Prever **qual** versão de um método será executada
✅ Escrever loops genéricos sobre listas polimórficas
✅ Adicionar novos tipos sem reescrever código antigo
✅ Justificar por que polimorfismo é a "alma" da OO
✅ Reconhecer quando casting é (e quando **não é**) necessário

E, principalmente, resolver o problema inicial:

> **"Como processar uma folha de pagamento com 500 funcionários de tipos diferentes em um único loop?"**

**Resposta:** Tratar todos como `Funcionario` em uma `ArrayList<Funcionario>`, e deixar a JVM escolher (em tempo de execução) qual versão de `calcularSalario()` chamar para cada objeto, com base no seu **tipo dinâmico**.

---

## Visualizando o que acontece em memória

```
   ArrayList<Funcionario> folha
   ┌──────────────────────────┐
   │ [0] ──────► FuncionarioCLT("Ana", 5000)         ← tipo DINÂMICO
   │ [1] ──────► FuncionarioHorista("Bruno", 50, 160)
   │ [2] ──────► FuncionarioComissionado("Carla",...)
   └──────────────────────────┘
     ▲
     └── Lista de referências do tipo ESTÁTICO Funcionario


   for (Funcionario f : folha) {
       f.calcularSalario();  ─────► JVM olha o objeto real
                                    e chama a versão certa
   }
```

**Leia como:** "A referência diz como eu posso usar o objeto. O objeto real diz como ele se comporta."

---

## 🎯 Vamos começar!

Clique em [Bloco 1](./Bloco1/README.md) para iniciar com os **conceitos fundamentais de Polimorfismo**.
