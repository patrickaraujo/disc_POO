# Bloco 2 — Exercício Guiado Completo: Sistema de Veículos

## Objetivos do Bloco

- Aplicar herança em um sistema real completo
- Praticar criação de hierarquias de classes
- Usar `extends`, `super` e `@Override` em contexto real
- Tomar decisões de design (o que é comum vs específico)
- Implementar um sistema funcional passo a passo com o professor

---

## 🎯 Problema: Locadora de Veículos

Uma locadora aluga três tipos de veículos:
- **Carros:** têm número de portas e ar-condicionado
- **Motos:** têm cilindradas (125cc, 300cc, etc.)
- **Caminhões:** têm capacidade de carga em toneladas

**Regras de negócio:**
1. Todos os veículos têm: marca, modelo, ano, placa, valor da diária
2. Cada tipo calcula o valor do aluguel de forma diferente:
   - **Carro:** valor base + R$ 10/dia se tiver ar-condicionado
   - **Moto:** valor base - 20% (motos são mais baratas)
   - **Caminhão:** valor base + R$ 50/tonelada de capacidade

3. Todos podem exibir suas informações, mas cada um mostra dados específicos

---

## Planejamento da Hierarquia

Antes de codificar, vamos **planejar no papel**:

```
                    ┌──────────────┐
                    │   Veiculo    │  ← SUPERCLASSE
                    ├──────────────┤
                    │ - marca      │
                    │ - modelo     │
                    │ - ano        │
                    │ - placa      │
                    │ - valorDiaria│
                    ├──────────────┤
                    │ + calcularAluguel(dias) │
                    │ + exibirInfo() │
                    └───────┬──────┘
                            │
            ┌───────────────┼───────────────┐
            │               │               │
      ┌─────▼─────┐   ┌────▼────┐   ┌──────▼──────┐
      │   Carro   │   │  Moto   │   │  Caminhao   │
      ├───────────┤   ├─────────┤   ├─────────────┤
      │- numPortas│   │- cilind │   │- capacidadeT│
      │- temArCond│   │         │   │             │
      ├───────────┤   ├─────────┤   ├─────────────┤
      │@Override  │   │@Override│   │@Override    │
      │calcular..│   │calcular │   │calcularAlug │
      │exibirInfo│   │exibirIn │   │exibirInfo   │
      └───────────┘   └─────────┘   └─────────────┘
```

**Decisões de design:**
- ✅ `marca`, `modelo`, `ano`, `placa`, `valorDiaria` → **comuns** (vão para `Veiculo`)
- ✅ `calcularAluguel()` → **assinatura comum**, **implementação específica** (método abstrato ou sobrescrito)
- ✅ `exibirInfo()` → **base comum**, **especializado** em cada subclasse
- ✅ Atributos específicos → cada subclasse tem os seus

---

## Passo 1 — Criar a Superclasse `Veiculo`

**O que vai aqui?** Tudo que é **comum** a todos os veículos.

```java
public class Veiculo {
    // Atributos comuns (protected = subclasses podem acessar)
    protected String marca;
    protected String modelo;
    protected int ano;
    protected String placa;
    protected double valorDiaria;
    
    // Construtor
    public Veiculo(String marca, String modelo, int ano, String placa, double valorDiaria) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
        this.valorDiaria = valorDiaria;
    }
    
    // Getters (subclasses e código externo podem usar)
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public String getPlaca() { return placa; }
    public double getValorDiaria() { return valorDiaria; }
    
    // Método que será sobrescrito (cada tipo calcula diferente)
    public double calcularAluguel(int dias) {
        return valorDiaria * dias;  // Cálculo base (será sobrescrito)
    }
    
    // Método que mostra informações básicas
    public void exibirInfo() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  INFORMAÇÕES DO VEÍCULO                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  Tipo: " + this.getClass().getSimpleName());
        System.out.println("║  Marca: " + marca);
        System.out.println("║  Modelo: " + modelo);
        System.out.println("║  Ano: " + ano);
        System.out.println("║  Placa: " + placa);
        System.out.println("║  Diária: R$ " + String.format("%.2f", valorDiaria));
    }
    
    // Método auxiliar para subclasses fecharem a exibição
    protected void exibirRodape() {
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
```

**Discussão com os alunos:**
- Por que `protected` nos atributos?
- Por que `calcularAluguel()` retorna cálculo base?
- `this.getClass().getSimpleName()` mostra o nome da classe real (Carro, Moto, etc.)

---

## Passo 2 — Criar Subclasse `Carro`

**O que vai aqui?** Atributos **específicos** de carro + **especialização** dos métodos.

```java
public class Carro extends Veiculo {
    // Atributos específicos de Carro
    private int numeroPortas;
    private boolean temArCondicionado;
    
    // Construtor
    public Carro(String marca, String modelo, int ano, String placa, 
                 double valorDiaria, int numeroPortas, boolean temArCondicionado) {
        // Chama construtor da superclasse
        super(marca, modelo, ano, placa, valorDiaria);
        
        // Inicializa atributos próprios
        this.numeroPortas = numeroPortas;
        this.temArCondicionado = temArCondicionado;
    }
    
    // Getters específicos
    public int getNumeroPortas() {
        return numeroPortas;
    }
    
    public boolean isTemArCondicionado() {
        return temArCondicionado;
    }
    
    // Sobrescreve o cálculo do aluguel (regra específica de Carro)
    @Override
    public double calcularAluguel(int dias) {
        double valorBase = super.calcularAluguel(dias);  // Chama cálculo da superclasse
        
        if (temArCondicionado) {
            return valorBase + (10.0 * dias);  // +R$ 10/dia se tiver ar
        }
        
        return valorBase;
    }
    
    // Sobrescreve exibição para adicionar informações específicas
    @Override
    public void exibirInfo() {
        super.exibirInfo();  // Exibe info básica da superclasse
        
        // Adiciona informações específicas de Carro
        System.out.println("║  Portas: " + numeroPortas);
        System.out.println("║  Ar-condicionado: " + (temArCondicionado ? "Sim" : "Não"));
        
        super.exibirRodape();  // Fecha a exibição
    }
}
```

**Discussão com os alunos:**
- Por que chamamos `super(...)` primeiro?
- Por que usamos `super.calcularAluguel(dias)` em vez de refazer o cálculo?
- Por que `numeroPortas` é `private` mas `marca` é `protected`?

---

## Passo 3 — Criar Subclasse `Moto`

```java
public class Moto extends Veiculo {
    private int cilindradas;  // 125, 300, 500, 1000...
    
    public Moto(String marca, String modelo, int ano, String placa, 
                double valorDiaria, int cilindradas) {
        super(marca, modelo, ano, placa, valorDiaria);
        this.cilindradas = cilindradas;
    }
    
    public int getCilindradas() {
        return cilindradas;
    }
    
    // Motos são 20% mais baratas
    @Override
    public double calcularAluguel(int dias) {
        double valorBase = super.calcularAluguel(dias);
        return valorBase * 0.8;  // Desconto de 20%
    }
    
    @Override
    public void exibirInfo() {
        super.exibirInfo();
        System.out.println("║  Cilindradas: " + cilindradas + "cc");
        super.exibirRodape();
    }
}
```

---

## Passo 4 — Criar Subclasse `Caminhao`

```java
public class Caminhao extends Veiculo {
    private double capacidadeToneladas;
    
    public Caminhao(String marca, String modelo, int ano, String placa, 
                    double valorDiaria, double capacidadeToneladas) {
        super(marca, modelo, ano, placa, valorDiaria);
        this.capacidadeToneladas = capacidadeToneladas;
    }
    
    public double getCapacidadeToneladas() {
        return capacidadeToneladas;
    }
    
    // Caminhões cobram +R$ 50 por tonelada
    @Override
    public double calcularAluguel(int dias) {
        double valorBase = super.calcularAluguel(dias);
        double adicionalCarga = capacidadeToneladas * 50.0;
        return valorBase + adicionalCarga;
    }
    
    @Override
    public void exibirInfo() {
        super.exibirInfo();
        System.out.println("║  Capacidade: " + capacidadeToneladas + " toneladas");
        super.exibirRodape();
    }
}
```

---

## Passo 5 — Criar Classe de Gerenciamento `Locadora`

Agora vamos criar uma classe que **gerencia** os veículos:

```java
public class Locadora {
    private String nome;
    private Veiculo[] frota;  // ← Polimorfismo! Array de Veiculo aceita Car, Moto, Caminhao
    private int quantidadeVeiculos;
    
    public Locadora(String nome, int capacidade) {
        this.nome = nome;
        this.frota = new Veiculo[capacidade];
        this.quantidadeVeiculos = 0;
    }
    
    // Adiciona qualquer tipo de veículo
    public void adicionarVeiculo(Veiculo veiculo) {
        if (quantidadeVeiculos < frota.length) {
            frota[quantidadeVeiculos] = veiculo;
            quantidadeVeiculos++;
            System.out.println("✓ Veículo adicionado: " + veiculo.getModelo());
        } else {
            System.out.println("✗ Frota cheia!");
        }
    }
    
    // Busca veículo por placa
    public Veiculo buscarPorPlaca(String placa) {
        for (int i = 0; i < quantidadeVeiculos; i++) {
            if (frota[i].getPlaca().equalsIgnoreCase(placa)) {
                return frota[i];
            }
        }
        return null;
    }
    
    // Lista todos os veículos
    public void listarFrota() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  FROTA DA LOCADORA " + nome.toUpperCase());
        System.out.println("╠═══════════════════════════════════════════════════╣");
        
        if (quantidadeVeiculos == 0) {
            System.out.println("║  Nenhum veículo cadastrado.");
        } else {
            for (int i = 0; i < quantidadeVeiculos; i++) {
                Veiculo v = frota[i];
                System.out.printf("║  [%d] %s %s %d - Placa: %s%n", 
                    (i+1), v.getMarca(), v.getModelo(), v.getAno(), v.getPlaca());
            }
        }
        
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
    }
    
    // Simula aluguel
    public void simularAluguel(String placa, int dias) {
        Veiculo v = buscarPorPlaca(placa);
        
        if (v == null) {
            System.out.println("✗ Veículo não encontrado!");
            return;
        }
        
        double valor = v.calcularAluguel(dias);  // ← Polimorfismo! Chama versão correta
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  SIMULAÇÃO DE ALUGUEL                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  Veículo: " + v.getMarca() + " " + v.getModelo());
        System.out.println("║  Período: " + dias + " dias");
        System.out.println("║  Valor total: R$ " + String.format("%.2f", valor));
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
```

**Discussão importante:**
- `Veiculo[]` pode conter `Carro`, `Moto` e `Caminhao` (polimorfismo!)
- `v.calcularAluguel(dias)` chama a versão **correta** de cada tipo
- Isso é possível por causa da **herança** + **sobrescrita**

---

## Passo 6 — Classe Principal para Testar

```java
public class SistemaLocadora {
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║      SISTEMA DE LOCADORA DE VEÍCULOS              ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");
        
        // Criar locadora
        Locadora locadora = new Locadora("AutoRent", 20);
        
        // Criar veículos de diferentes tipos
        System.out.println("=== CADASTRANDO VEÍCULOS ===\n");
        
        Carro carro1 = new Carro("Toyota", "Corolla", 2022, "ABC-1234", 150.0, 4, true);
        Carro carro2 = new Carro("Volkswagen", "Gol", 2020, "XYZ-5678", 100.0, 4, false);
        
        Moto moto1 = new Moto("Honda", "CG 160", 2023, "MOT-1111", 80.0, 160);
        Moto moto2 = new Moto("Yamaha", "MT-03", 2022, "MOT-2222", 120.0, 321);
        
        Caminhao caminhao1 = new Caminhao("Mercedes", "Atego", 2021, "CAM-9999", 300.0, 8.0);
        
        // Adicionar à frota
        locadora.adicionarVeiculo(carro1);
        locadora.adicionarVeiculo(carro2);
        locadora.adicionarVeiculo(moto1);
        locadora.adicionarVeiculo(moto2);
        locadora.adicionarVeiculo(caminhao1);
        
        // Listar frota
        locadora.listarFrota();
        
        // Exibir detalhes de cada veículo
        System.out.println("\n=== DETALHES DOS VEÍCULOS ===\n");
        
        carro1.exibirInfo();
        moto1.exibirInfo();
        caminhao1.exibirInfo();
        
        // Simular aluguéis
        System.out.println("\n=== SIMULAÇÕES DE ALUGUEL ===\n");
        
        locadora.simularAluguel("ABC-1234", 5);  // Carro com ar (150*5 + 10*5 = 800)
        locadora.simularAluguel("XYZ-5678", 5);  // Carro sem ar (100*5 = 500)
        locadora.simularAluguel("MOT-1111", 3);  // Moto (80*3*0.8 = 192)
        locadora.simularAluguel("CAM-9999", 2);  // Caminhão (300*2 + 8*50 = 1000)
        
        // Buscar veículo específico
        System.out.println("\n=== BUSCA POR PLACA ===\n");
        
        Veiculo encontrado = locadora.buscarPorPlaca("MOT-2222");
        if (encontrado != null) {
            System.out.println("✓ Veículo encontrado:");
            encontrado.exibirInfo();
        }
    }
}
```

---

## Saída Esperada (Parcial)

```
╔═══════════════════════════════════════════════════╗
║      SISTEMA DE LOCADORA DE VEÍCULOS              ║
╚═══════════════════════════════════════════════════╝

=== CADASTRANDO VEÍCULOS ===

✓ Veículo adicionado: Corolla
✓ Veículo adicionado: Gol
✓ Veículo adicionado: CG 160
✓ Veículo adicionado: MT-03
✓ Veículo adicionado: Atego

╔═══════════════════════════════════════════════════╗
║  FROTA DA LOCADORA AUTORENT
╠═══════════════════════════════════════════════════╣
║  [1] Toyota Corolla 2022 - Placa: ABC-1234
║  [2] Volkswagen Gol 2020 - Placa: XYZ-5678
║  [3] Honda CG 160 2023 - Placa: MOT-1111
║  [4] Yamaha MT-03 2022 - Placa: MOT-2222
║  [5] Mercedes Atego 2021 - Placa: CAM-9999
╚═══════════════════════════════════════════════════╝

=== DETALHES DOS VEÍCULOS ===

╔════════════════════════════════════════╗
║  INFORMAÇÕES DO VEÍCULO                ║
╠════════════════════════════════════════╣
║  Tipo: Carro
║  Marca: Toyota
║  Modelo: Corolla
║  Ano: 2022
║  Placa: ABC-1234
║  Diária: R$ 150,00
║  Portas: 4
║  Ar-condicionado: Sim
╚════════════════════════════════════════╝

...

╔════════════════════════════════════════╗
║  SIMULAÇÃO DE ALUGUEL                  ║
╠════════════════════════════════════════╣
║  Veículo: Toyota Corolla
║  Período: 5 dias
║  Valor total: R$ 800,00
╚════════════════════════════════════════╝
```

---

## Discussão Final com os Alunos

### Perguntas para reflexão:

1. **Por que usamos herança aqui?**
   - Evitar duplicação de código (marca, modelo, etc.)
   - Reutilizar comportamentos comuns (exibirInfo base)
   - Poder tratar todos como `Veiculo`

2. **Por que `Veiculo[]` aceita Carro, Moto e Caminhão?**
   - Porque todos **são** Veículos (relação is-a)
   - Isso é **polimorfismo** (veremos mais na Aula 08)

3. **O que acontece se não usar `@Override`?**
   - Funciona, mas não há verificação do compilador
   - Se errar a assinatura, cria método novo sem querer

4. **Por que `protected` nos atributos de Veiculo?**
   - Para subclasses acessarem diretamente
   - Mas cuidado: quebra encapsulamento parcialmente

5. **Poderíamos usar composição em vez de herança?**
   - Não faz sentido: Carro **É UM** Veículo, não **TEM UM** Veículo
   - Use herança para "is-a", composição para "has-a"

---

## Atividades de Extensão (Opcional)

Se houver tempo, proponha:

1. **Adicionar novo tipo de veículo:**
   - Exemplo: `Van` (tem `numeroPassageiros`)
   - Cálculo: valorBase + R$ 5 por passageiro

2. **Adicionar método de desconto:**
   - Aluguéis de 7+ dias ganham 10% de desconto
   - Implementar em `Veiculo` ou sobrescrever em cada subclasse?

3. **Sistema de disponibilidade:**
   - Atributo `boolean alugado` em `Veiculo`
   - Métodos `alugar()` e `devolver()`

---

## Resumo do Bloco 2

Neste bloco você:

✅ Implementou um **sistema completo** usando herança  
✅ Praticou criação de **hierarquia de classes**  
✅ Usou `extends`, `super`, `@Override` em contexto real  
✅ Entendeu **polimorfismo básico** (array de Veiculo)  
✅ Tomou **decisões de design** (comum vs. específico)  

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## Próximo Passo

No **Bloco 3**, você vai **desenvolver sozinho** um sistema similar: **Sistema de Produtos** para um e-commerce.

[➡️ Ir para Bloco 3](../Bloco3/README.md)
