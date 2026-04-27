# Bloco 2 — Exercício Guiado Completo: Folha de Pagamento Polimórfica

## Objetivos do Bloco

- Aplicar polimorfismo em um sistema real e completo
- Praticar a relação entre **hierarquia** (Aula 07) e **polimorfismo** (Aula 08)
- Usar `ArrayList<Funcionario>` como coleção polimórfica
- Demonstrar **dispatch dinâmico** ao vivo
- Mostrar como adicionar **novo tipo** sem alterar código existente
- Implementar relatórios e filtros polimórficos passo a passo com o professor

---

## 🎯 Problema: Sistema de Folha de Pagamento

A empresa **TechBR** tem 4 tipos de funcionários e precisa processar a folha de pagamento mensal:

- **Funcionários CLT:** salário fixo mensal
- **Funcionários Horistas:** valor da hora × horas trabalhadas no mês
- **Funcionários Comissionados:** salário base + percentual sobre vendas
- **Estagiários:** bolsa fixa + auxílio-transporte (R$ 200)

**Regras de negócio:**

1. Todos os funcionários têm: nome, CPF e matrícula
2. Cada tipo calcula seu salário de forma diferente:
   - **CLT:** `salarioBase`
   - **Horista:** `valorHora × horasTrabalhadas`
   - **Comissionado:** `salarioBase + (totalVendas × taxaComissao)`
   - **Estagiário:** `bolsa + 200` (auxílio-transporte fixo)

3. O sistema deve:
   - Calcular folha total
   - Listar todos os funcionários com seus salários
   - Encontrar o **maior** e o **menor** salário
   - Calcular **média** salarial
   - Filtrar funcionários por tipo
   - Aumentar o salário de todos em **X%** (reajuste polimórfico)

4. Adicionar um novo tipo de funcionário **NÃO** pode quebrar o sistema

---

## Planejamento da Hierarquia

Antes de codificar, vamos **planejar no papel**:

```
                       ┌──────────────────────┐
                       │    Funcionario       │  ← SUPERCLASSE
                       ├──────────────────────┤
                       │ # nome               │
                       │ # cpf                │
                       │ # matricula          │
                       ├──────────────────────┤
                       │ + calcularSalario()  │ ← Polimórfico
                       │ + exibirContracheque │ ← Usa polimorfismo
                       │ + aplicarReajuste()  │ ← Polimórfico
                       └──────────┬───────────┘
                                  │
        ┌───────────────┬─────────┼─────────────┬──────────────────┐
        │               │         │             │                  │
   ┌────▼─────┐   ┌────▼──────┐  ┌▼────────────┐  ┌───────────────▼┐
   │   CLT    │   │  Horista  │  │ Comissionado │  │   Estagiario   │
   ├──────────┤   ├───────────┤  ├──────────────┤  ├────────────────┤
   │-salBase  │   │-valorHora │  │-salBase      │  │-bolsa          │
   │          │   │-horasTrab │  │-totalVendas  │  │                │
   │          │   │           │  │-taxaComissao │  │                │
   ├──────────┤   ├───────────┤  ├──────────────┤  ├────────────────┤
   │@Override │   │@Override  │  │@Override     │  │@Override       │
   │calcular..│   │calcular   │  │calcular..    │  │calcular..      │
   │@Override │   │@Override  │  │@Override     │  │@Override       │
   │aplicar.. │   │aplicar    │  │aplicar..     │  │aplicar..       │
   └──────────┘   └───────────┘  └──────────────┘  └────────────────┘
```

**Decisões de design:**
- ✅ `nome`, `cpf`, `matricula` → **comuns** (vão para `Funcionario`, `protected`)
- ✅ `calcularSalario()` → **polimórfico** (cada subclasse implementa diferente)
- ✅ `exibirContracheque()` → **herdado**, mas chama `calcularSalario()` polimorficamente
- ✅ `aplicarReajuste(double)` → **polimórfico** (reajuste pode afetar atributos diferentes)

---

## Passo 1 — Criar a Superclasse `Funcionario`

**O que vai aqui?** Tudo que é **comum** + métodos com **assinatura comum** que serão sobrescritos.

```java
public class Funcionario {

    // Atributos comuns - protected para subclasses acessarem
    protected String nome;
    protected String cpf;
    protected String matricula;

    // Construtor
    public Funcionario(String nome, String cpf, String matricula) {
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getMatricula() { return matricula; }

    // Método POLIMÓRFICO - cada subclasse implementa diferente
    public double calcularSalario() {
        return 0.0;  // Será sobrescrito
    }

    // Método POLIMÓRFICO - cada subclasse aplica de forma diferente
    public void aplicarReajuste(double percentual) {
        // Será sobrescrito - a base não faz nada
    }

    // Método HERDADO - usa polimorfismo internamente!
    public void exibirContracheque() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  CONTRACHEQUE                          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  Funcionário: " + nome);
        System.out.println("║  CPF: " + cpf);
        System.out.println("║  Matrícula: " + matricula);
        System.out.println("║  Categoria: " + this.getClass().getSimpleName());

        // ↓ AQUI ESTÁ O POLIMORFISMO ↓
        // Mesmo sendo chamado a partir da superclasse,
        // o calcularSalario() executado é o da subclasse REAL.
        System.out.println("║  Salário: R$ " + String.format("%.2f", calcularSalario()));
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
```

**Discussão com os alunos:**
- Por que `protected` nos atributos?
- O método `exibirContracheque()` é definido na superclasse, mas chama `calcularSalario()`. Quando o objeto real é um `FuncionarioCLT`, qual versão de `calcularSalario()` executa?
- Por que `aplicarReajuste()` na superclasse não faz nada?

> 💡 **Ponto-chave:** `exibirContracheque()` é o **mesmo método** para todos. Mas quando ele chama `calcularSalario()`, **cada subclasse responde do seu jeito**. Isso é polimorfismo dentro de um método herdado!

---

## Passo 2 — Criar Subclasse `FuncionarioCLT`

```java
public class FuncionarioCLT extends Funcionario {

    private double salarioBase;

    public FuncionarioCLT(String nome, String cpf, String matricula,
                          double salarioBase) {
        super(nome, cpf, matricula);
        this.salarioBase = salarioBase;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    // SOBRESCRITA: salário CLT é fixo
    @Override
    public double calcularSalario() {
        return salarioBase;
    }

    // SOBRESCRITA: reajuste afeta salário base
    @Override
    public void aplicarReajuste(double percentual) {
        this.salarioBase = this.salarioBase * (1 + percentual / 100);
    }
}
```

**Discussão:**
- Note que `aplicarReajuste()` modifica **`salarioBase`** — o atributo específico da CLT
- Cada subclasse vai modificar **atributos diferentes** ao receber reajuste

---

## Passo 3 — Criar Subclasse `FuncionarioHorista`

```java
public class FuncionarioHorista extends Funcionario {

    private double valorHora;
    private int horasTrabalhadas;

    public FuncionarioHorista(String nome, String cpf, String matricula,
                              double valorHora, int horasTrabalhadas) {
        super(nome, cpf, matricula);
        this.valorHora = valorHora;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public double getValorHora() { return valorHora; }
    public int getHorasTrabalhadas() { return horasTrabalhadas; }

    @Override
    public double calcularSalario() {
        return valorHora * horasTrabalhadas;
    }

    // Para horista, o reajuste afeta o VALOR DA HORA
    @Override
    public void aplicarReajuste(double percentual) {
        this.valorHora = this.valorHora * (1 + percentual / 100);
    }
}
```

---

## Passo 4 — Criar Subclasse `FuncionarioComissionado`

```java
public class FuncionarioComissionado extends Funcionario {

    private double salarioBase;
    private double totalVendas;
    private double taxaComissao;

    public FuncionarioComissionado(String nome, String cpf, String matricula,
                                    double salarioBase,
                                    double totalVendas,
                                    double taxaComissao) {
        super(nome, cpf, matricula);
        this.salarioBase = salarioBase;
        this.totalVendas = totalVendas;
        this.taxaComissao = taxaComissao;
    }

    public double getSalarioBase() { return salarioBase; }
    public double getTotalVendas() { return totalVendas; }
    public double getTaxaComissao() { return taxaComissao; }

    @Override
    public double calcularSalario() {
        return salarioBase + (totalVendas * taxaComissao);
    }

    // Para comissionado, o reajuste afeta apenas o salário base
    // (a comissão depende das vendas, não do reajuste)
    @Override
    public void aplicarReajuste(double percentual) {
        this.salarioBase = this.salarioBase * (1 + percentual / 100);
    }
}
```

---

## Passo 5 — Criar Subclasse `Estagiario`

```java
public class Estagiario extends Funcionario {

    private static final double AUXILIO_TRANSPORTE = 200.0;

    private double bolsa;

    public Estagiario(String nome, String cpf, String matricula, double bolsa) {
        super(nome, cpf, matricula);
        this.bolsa = bolsa;
    }

    public double getBolsa() { return bolsa; }

    @Override
    public double calcularSalario() {
        return bolsa + AUXILIO_TRANSPORTE;
    }

    // Para estagiário, reajuste afeta a bolsa
    @Override
    public void aplicarReajuste(double percentual) {
        this.bolsa = this.bolsa * (1 + percentual / 100);
    }
}
```

**Discussão importante:**
- Cada classe sobrescreveu `calcularSalario()` e `aplicarReajuste()` da sua maneira
- A `assinatura` é a mesma — mas o **comportamento** é específico
- Isso é **a essência do polimorfismo**

---

## Passo 6 — Criar a Classe `FolhaPagamento` (gerenciamento polimórfico)

```java
import java.util.ArrayList;

public class FolhaPagamento {

    private String empresa;
    private ArrayList<Funcionario> funcionarios;

    public FolhaPagamento(String empresa) {
        this.empresa = empresa;
        // ★ ArrayList polimórfico - aceita qualquer subtipo de Funcionario ★
        this.funcionarios = new ArrayList<>();
    }

    // Aceita QUALQUER tipo de funcionário
    public void contratar(Funcionario f) {
        funcionarios.add(f);
        System.out.println("✓ Contratado: " + f.getNome() +
                           " (" + f.getClass().getSimpleName() + ")");
    }

    // Processa folha completa - polimorfismo em ação!
    public void processarFolha() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROCESSAMENTO DA FOLHA - " + empresa);
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        double total = 0;

        // ★ Loop ÚNICO para qualquer tipo ★
        for (Funcionario f : funcionarios) {
            f.exibirContracheque();           // Polimórfico
            total += f.calcularSalario();     // Polimórfico
        }

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TOTAL DA FOLHA: R$ " + String.format("%.2f", total));
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
    }

    // Calcula valor total da folha
    public double calcularTotalFolha() {
        double total = 0;
        for (Funcionario f : funcionarios) {
            total += f.calcularSalario();
        }
        return total;
    }

    // Encontra o maior salário
    public Funcionario maiorSalario() {
        if (funcionarios.isEmpty()) return null;

        Funcionario maior = funcionarios.get(0);
        for (Funcionario f : funcionarios) {
            if (f.calcularSalario() > maior.calcularSalario()) {
                maior = f;
            }
        }
        return maior;
    }

    // Encontra o menor salário
    public Funcionario menorSalario() {
        if (funcionarios.isEmpty()) return null;

        Funcionario menor = funcionarios.get(0);
        for (Funcionario f : funcionarios) {
            if (f.calcularSalario() < menor.calcularSalario()) {
                menor = f;
            }
        }
        return menor;
    }

    // Calcula média salarial
    public double mediaSalarial() {
        if (funcionarios.isEmpty()) return 0;
        return calcularTotalFolha() / funcionarios.size();
    }

    // Aplica reajuste em TODOS - polimorfismo!
    public void aplicarReajusteGeral(double percentual) {
        System.out.println("\n>>> Aplicando reajuste de " + percentual +
                           "% para todos os funcionários...");

        for (Funcionario f : funcionarios) {
            f.aplicarReajuste(percentual);  // Cada um aplica DO SEU JEITO
        }

        System.out.println(">>> Reajuste concluído!\n");
    }

    // Filtro polimórfico usando instanceof
    public ArrayList<Funcionario> filtrarPorTipo(Class<?> tipo) {
        ArrayList<Funcionario> resultado = new ArrayList<>();
        for (Funcionario f : funcionarios) {
            if (tipo.isInstance(f)) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    // Lista resumida
    public void listarFuncionarios() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  FUNCIONÁRIOS DA EMPRESA " + empresa.toUpperCase());
        System.out.println("╠═══════════════════════════════════════════════════╣");

        if (funcionarios.isEmpty()) {
            System.out.println("║  Nenhum funcionário cadastrado.");
        } else {
            for (int i = 0; i < funcionarios.size(); i++) {
                Funcionario f = funcionarios.get(i);
                System.out.printf("║  [%d] %-25s | %-15s | R$ %8.2f%n",
                    (i + 1),
                    f.getNome(),
                    f.getClass().getSimpleName(),
                    f.calcularSalario());
            }
        }

        System.out.println("╚═══════════════════════════════════════════════════╝\n");
    }
}
```

**Discussão importante:**
- `ArrayList<Funcionario>` aceita CLT, Horista, Comissionado e Estagiário
- `f.calcularSalario()` chama a versão **correta** de cada tipo (dispatch dinâmico)
- O método `filtrarPorTipo(Class<?>)` usa **reflexão** + `instanceof` para filtrar
- **Para adicionar novo tipo de funcionário, NÃO precisa mexer em `FolhaPagamento`!**

---

## Passo 7 — Classe Principal para Testar

```java
import java.util.ArrayList;

public class SistemaRH {
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║      SISTEMA DE FOLHA DE PAGAMENTO - TechBR       ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        // Criar folha de pagamento
        FolhaPagamento folha = new FolhaPagamento("TechBR");

        // Contratar funcionários de TIPOS diferentes
        System.out.println("=== CONTRATAÇÕES ===\n");

        folha.contratar(new FuncionarioCLT(
            "Ana Silva", "111.111.111-11", "M001", 5000.0));

        folha.contratar(new FuncionarioCLT(
            "João Pereira", "444.444.444-44", "M004", 7500.0));

        folha.contratar(new FuncionarioHorista(
            "Bruno Souza", "222.222.222-22", "M002", 50.0, 160));

        folha.contratar(new FuncionarioHorista(
            "Daniela Costa", "555.555.555-55", "M005", 80.0, 120));

        folha.contratar(new FuncionarioComissionado(
            "Carla Lima", "333.333.333-33", "M003",
            2000.0, 30000.0, 0.05));

        folha.contratar(new FuncionarioComissionado(
            "Eduardo Rocha", "666.666.666-66", "M006",
            3000.0, 50000.0, 0.04));

        folha.contratar(new Estagiario(
            "Felipe Alves", "777.777.777-77", "M007", 1200.0));

        folha.contratar(new Estagiario(
            "Gabriela Mota", "888.888.888-88", "M008", 1500.0));

        // Listar todos
        folha.listarFuncionarios();

        // Processar folha completa (mostra contracheques)
        folha.processarFolha();

        // Estatísticas
        System.out.println("=== ESTATÍSTICAS ===\n");

        System.out.printf("Total da folha: R$ %.2f%n", folha.calcularTotalFolha());
        System.out.printf("Média salarial: R$ %.2f%n", folha.mediaSalarial());

        Funcionario maior = folha.maiorSalario();
        System.out.printf("Maior salário: %s - R$ %.2f%n",
            maior.getNome(), maior.calcularSalario());

        Funcionario menor = folha.menorSalario();
        System.out.printf("Menor salário: %s - R$ %.2f%n",
            menor.getNome(), menor.calcularSalario());

        // Filtros polimórficos
        System.out.println("\n=== FILTROS POR TIPO ===\n");

        ArrayList<Funcionario> clts = folha.filtrarPorTipo(FuncionarioCLT.class);
        System.out.println("Funcionários CLT: " + clts.size());
        for (Funcionario f : clts) {
            System.out.println("  - " + f.getNome());
        }

        ArrayList<Funcionario> estagiarios = folha.filtrarPorTipo(Estagiario.class);
        System.out.println("\nEstagiários: " + estagiarios.size());
        for (Funcionario f : estagiarios) {
            System.out.println("  - " + f.getNome());
        }

        // Aplicar reajuste polimórfico
        System.out.println("\n=== REAJUSTE GERAL DE 10% ===");
        folha.aplicarReajusteGeral(10.0);

        // Mostrar nova lista após reajuste
        folha.listarFuncionarios();
    }
}
```

---

## Passo 8 — Demonstrar Tipo Estático vs Dinâmico AO VIVO

Faça este experimento com a turma para fixar o conceito:

```java
public class DemoTipoDinamico {
    public static void main(String[] args) {

        // Mesma referência (Funcionario), objetos REAIS diferentes
        Funcionario f1 = new FuncionarioCLT("Ana", "111", "M01", 5000);
        Funcionario f2 = new FuncionarioHorista("Bruno", "222", "M02", 50, 160);
        Funcionario f3 = new Estagiario("Carla", "333", "M03", 1200);

        System.out.println("=== TIPOS ===");
        System.out.println("f1 (estático = Funcionario, dinâmico = "
            + f1.getClass().getSimpleName() + ")");
        System.out.println("f2 (estático = Funcionario, dinâmico = "
            + f2.getClass().getSimpleName() + ")");
        System.out.println("f3 (estático = Funcionario, dinâmico = "
            + f3.getClass().getSimpleName() + ")");

        System.out.println("\n=== SALÁRIOS ===");
        System.out.println("f1.calcularSalario() = R$ " + f1.calcularSalario());
        System.out.println("f2.calcularSalario() = R$ " + f2.calcularSalario());
        System.out.println("f3.calcularSalario() = R$ " + f3.calcularSalario());

        // O compilador vê f1 como Funcionario.
        // Tente chamar um método específico de CLT:

        // f1.getSalarioBase();  // ❌ Erro de compilação!
                                 //    Funcionario não tem getSalarioBase

        // Para acessar, precisa de instanceof + cast:
        if (f1 instanceof FuncionarioCLT) {
            FuncionarioCLT clt = (FuncionarioCLT) f1;
            System.out.println("\nSalário base de f1 (após cast): R$ "
                + clt.getSalarioBase());
        }
    }
}
```

**Saída esperada:**
```
=== TIPOS ===
f1 (estático = Funcionario, dinâmico = FuncionarioCLT)
f2 (estático = Funcionario, dinâmico = FuncionarioHorista)
f3 (estático = Funcionario, dinâmico = Estagiario)

=== SALÁRIOS ===
f1.calcularSalario() = R$ 5000.0
f2.calcularSalario() = R$ 8000.0
f3.calcularSalario() = R$ 1400.0

Salário base de f1 (após cast): R$ 5000.0
```

---

## Saída Esperada (parcial) do programa principal

```
╔═══════════════════════════════════════════════════╗
║      SISTEMA DE FOLHA DE PAGAMENTO - TechBR       ║
╚═══════════════════════════════════════════════════╝

=== CONTRATAÇÕES ===

✓ Contratado: Ana Silva (FuncionarioCLT)
✓ Contratado: João Pereira (FuncionarioCLT)
✓ Contratado: Bruno Souza (FuncionarioHorista)
✓ Contratado: Daniela Costa (FuncionarioHorista)
✓ Contratado: Carla Lima (FuncionarioComissionado)
✓ Contratado: Eduardo Rocha (FuncionarioComissionado)
✓ Contratado: Felipe Alves (Estagiario)
✓ Contratado: Gabriela Mota (Estagiario)

╔═══════════════════════════════════════════════════╗
║  FUNCIONÁRIOS DA EMPRESA TECHBR
╠═══════════════════════════════════════════════════╣
║  [1] Ana Silva                 | FuncionarioCLT   | R$  5000,00
║  [2] João Pereira              | FuncionarioCLT   | R$  7500,00
║  [3] Bruno Souza               | FuncionarioHorista | R$  8000,00
║  [4] Daniela Costa             | FuncionarioHorista | R$  9600,00
║  [5] Carla Lima                | FuncionarioComissionado | R$  3500,00
║  [6] Eduardo Rocha             | FuncionarioComissionado | R$  5000,00
║  [7] Felipe Alves              | Estagiario       | R$  1400,00
║  [8] Gabriela Mota             | Estagiario       | R$  1700,00
╚═══════════════════════════════════════════════════╝

╔═══════════════════════════════════════════════════╗
║  TOTAL DA FOLHA: R$ 41700,00
╚═══════════════════════════════════════════════════╝

=== ESTATÍSTICAS ===

Total da folha: R$ 41700,00
Média salarial: R$ 5212,50
Maior salário: Daniela Costa - R$ 9600,00
Menor salário: Felipe Alves - R$ 1400,00

=== FILTROS POR TIPO ===

Funcionários CLT: 2
  - Ana Silva
  - João Pereira

Estagiários: 2
  - Felipe Alves
  - Gabriela Mota

=== REAJUSTE GERAL DE 10% ===

>>> Aplicando reajuste de 10.0% para todos os funcionários...
>>> Reajuste concluído!
```

---

## Passo 9 — Adicionando NOVO Tipo (a magia do polimorfismo)

Imagine que a empresa contratou **terceirizados** (PJ). Vamos adicionar:

```java
public class FuncionarioPJ extends Funcionario {

    private double valorMensal;
    private double percentualImpostos;  // ex: 0.13 para 13%

    public FuncionarioPJ(String nome, String cpf, String matricula,
                         double valorMensal, double percentualImpostos) {
        super(nome, cpf, matricula);
        this.valorMensal = valorMensal;
        this.percentualImpostos = percentualImpostos;
    }

    public double getValorMensal() { return valorMensal; }

    @Override
    public double calcularSalario() {
        // PJ recebe valor bruto - impostos
        return valorMensal * (1 - percentualImpostos);
    }

    @Override
    public void aplicarReajuste(double percentual) {
        this.valorMensal = this.valorMensal * (1 + percentual / 100);
    }
}
```

**Use no `main` sem alterar nenhuma outra classe:**

```java
folha.contratar(new FuncionarioPJ("Henrique Tavares", "999.999.999-99",
                                   "M009", 12000.0, 0.13));

folha.processarFolha();  // ✅ Funciona automaticamente!
folha.aplicarReajusteGeral(5.0);  // ✅ Reajuste do PJ também aplicado!
```

**Discussão crítica:**
- ✅ Não tocamos em `Funcionario`
- ✅ Não tocamos em `FolhaPagamento`
- ✅ Não tocamos em `SistemaRH` (apenas adicionamos a contratação)
- ✅ Apenas **estendemos** com nova subclasse

> 💎 **Esse é o Princípio Aberto/Fechado funcionando na prática.** Você acabou de evoluir o sistema **sem modificar** o que já estava feito.

---

## Discussão Final com os Alunos

### Perguntas para reflexão:

1. **Onde acontece o polimorfismo neste sistema?**
   - Dentro de `processarFolha()`, ao chamar `f.calcularSalario()`
   - Dentro de `exibirContracheque()`, ao chamar `calcularSalario()` (mesmo herdado!)
   - Em `aplicarReajusteGeral()`, ao chamar `f.aplicarReajuste(percentual)`

2. **Qual o tipo estático e dinâmico do `f` no loop?**
   - Estático: `Funcionario` (declaração do `for-each`)
   - Dinâmico: muda a cada iteração (CLT, Horista, etc.)

3. **Por que `calcularSalario()` na superclasse retorna `0`?**
   - Para que o método possa ser chamado como tipo `Funcionario`
   - Ele será sempre **sobrescrito** nas subclasses
   - **Na Aula 09** veremos como usar `abstract` para forçar a sobrescrita

4. **O método `exibirContracheque()` funciona corretamente para todos os tipos?**
   - Sim! Mesmo sendo definido na superclasse, ele usa polimorfismo internamente
   - Quando chama `calcularSalario()`, a JVM pega a versão correta do objeto real

5. **E se eu tentar chamar `getSalarioBase()` em `f` (do loop)?**
   - **Erro de compilação:** `Funcionario` não tem esse método
   - O compilador olha o **tipo estático**, não o objeto real
   - Para acessar, precisa de `instanceof + cast`

6. **Por que isso é melhor que `if (tipo == "CLT") ... else if ...`?**
   - Aberto a extensão (novos tipos sem mexer no código)
   - Sem encadeamento longo de `if/else`
   - A JVM faz o trabalho por você
   - Manutenção e testes mais fáceis

---

## Atividades de Extensão (Opcional)

Se houver tempo, proponha:

1. **Bonificação de fim de ano (13º salário):**
   - Método `calcularDecimoTerceiro()` polimórfico
   - CLT: salário base completo
   - Horista: média das horas dos últimos 12 meses
   - Comissionado: salário base + média das comissões
   - Estagiário: não recebe (retorna 0)

2. **Imposto de renda:**
   - Método `calcularIR()` na superclasse usando faixas
   - Cada tipo pode ter regras de dedução específicas

3. **Ranking de funcionários:**
   - Ordenar `ArrayList<Funcionario>` por salário (maior para o menor)
   - Implementar `Comparable<Funcionario>` na superclasse
   - Imprimir ranking polimórfico

4. **Exportar relatório:**
   - Método `gerarLinhaCSV()` polimórfico
   - Cada tipo gera campos diferentes
   - `FolhaPagamento` consolida em um arquivo único

---

## Resumo do Bloco 2

Neste bloco você:

✅ Implementou um **sistema completo polimórfico**
✅ Criou hierarquia com **4 (depois 5) tipos de funcionários**
✅ Usou `ArrayList<Funcionario>` como **coleção polimórfica**
✅ Aplicou **dispatch dinâmico** em vários contextos
✅ Demonstrou que `exibirContracheque()` (herdado) também é polimórfico por dentro
✅ Adicionou novo tipo (`FuncionarioPJ`) **sem modificar** código existente
✅ Aplicou o **Princípio Aberto/Fechado** na prática

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## Próximo Passo

No **Bloco 3**, você vai **desenvolver sozinho** um sistema diferente: **Sistema de Pagamentos Polimórfico** (PIX, Boleto, Cartão de Crédito, Débito).

[➡️ Ir para Bloco 3](../Bloco3/README.md)
