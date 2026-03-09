# Bloco 3 — Proteção do Estado e Integridade dos Dados

## Objetivos do Bloco

- Implementar classes onde a validação nos setters previne estados impossíveis
- Criar atributos que são somente leitura (getter sem setter)
- Perceber que encapsulamento vai além de "colocar private e gerar getter/setter"

---

## 3.1 O conceito em uma frase

> Encapsular não é apenas esconder dados — é garantir que **o objeto nunca fique em um estado inválido**, não importa o que o código externo tente fazer.

Exemplos de estados inválidos que queremos impedir:

- Um `Funcionario` com salário negativo
- Um `Termometro` com temperatura abaixo do zero absoluto
- Um `Pedido` com quantidade zero de itens
- Uma `Pessoa` com idade de 300 anos

Os setters são a **linha de defesa**. Se a validação está no setter, todo código que tentar alterar o atributo passa obrigatoriamente pela verificação.

---

## 3.2 Atributos somente leitura

Nem todo atributo precisa de setter. Alguns valores são definidos uma vez e nunca mais mudam — ou só mudam por métodos específicos.

**Padrão: getter sem setter**

```java
private String cpf;

public String getCpf() {
    return cpf;
}

// Sem setCpf() → CPF não pode ser alterado depois de definido
```

Neste caso, o CPF só é definido internamente (por exemplo, no construtor — Aula 05). De fora, só é possível **ler**.

---

## Exercício Guiado 1 — Classe Funcionario (professor + alunos)

Vamos criar uma classe que demonstra validação robusta e atributos somente leitura.

### Passo 1 — `Funcionario.java`:

```java
public class Funcionario {

    private String nome;
    private String cargo;
    private double salario;
    private String matricula;  // somente leitura após definição

    // --- Getters ---

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }

    public String getMatricula() {
        return matricula;
    }

    // --- Setters com validação ---

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("Erro: nome não pode ser vazio.");
            return;
        }
        this.nome = nome;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.isEmpty()) {
            System.out.println("Erro: cargo não pode ser vazio.");
            return;
        }
        this.cargo = cargo;
    }

    public void setSalario(double salario) {
        if (salario < 0) {
            System.out.println("Erro: salário não pode ser negativo.");
            return;
        }
        this.salario = salario;
    }

    // Matrícula só pode ser definida UMA VEZ
    public void setMatricula(String matricula) {
        if (this.matricula != null) {
            System.out.println("Erro: matrícula já foi definida e não pode ser alterada.");
            return;
        }
        if (matricula == null || matricula.isEmpty()) {
            System.out.println("Erro: matrícula não pode ser vazia.");
            return;
        }
        this.matricula = matricula;
    }

    // --- Métodos de negócio ---

    public void aplicarAumento(double percentual) {
        if (percentual <= 0 || percentual > 100) {
            System.out.println("Percentual inválido: " + percentual);
            return;
        }
        double aumento = salario * percentual / 100;
        salario = salario + aumento;
        System.out.println("Aumento de " + percentual + "% aplicado a " + nome + ".");
        System.out.println("Novo salário: R$ " + salario);
    }

    public void exibirDados() {
        System.out.println("Matrícula: " + matricula);
        System.out.println("Nome: " + nome + " | Cargo: " + cargo);
        System.out.println("Salário: R$ " + salario);
        System.out.println("----------");
    }
}
```

### Passo 2 — `FuncionarioMain.java`:

```java
public class FuncionarioMain {

    public static void main(String[] args) {

        Funcionario f1 = new Funcionario();
        f1.setMatricula("F001");
        f1.setNome("Ana Silva");
        f1.setCargo("Desenvolvedora");
        f1.setSalario(5000.0);

        f1.exibirDados();

        // Tentativa de alterar matrícula → deve ser recusada
        f1.setMatricula("F999");

        // Tentativa de salário negativo → deve ser recusada
        f1.setSalario(-100.0);

        // Aumento válido
        f1.aplicarAumento(10.0);

        f1.exibirDados();
    }
}
```

**Saída esperada:**

```
Matrícula: F001
Nome: Ana Silva | Cargo: Desenvolvedora
Salário: R$ 5000.0
----------
Erro: matrícula já foi definida e não pode ser alterada.
Erro: salário não pode ser negativo.
Aumento de 10.0% aplicado a Ana Silva.
Novo salário: R$ 5500.0
Matrícula: F001
Nome: Ana Silva | Cargo: Desenvolvedora
Salário: R$ 5500.0
----------
```

**Discussão:** note que o salário não muda para -100 porque o setter rejeitou. E a matrícula não muda porque já foi definida. O objeto se protege.

---

## Exercício Guiado 2 — Classe Termometro (professor + alunos)

Uma classe simples que mostra como limitar faixas de valores e expor apenas leitura do estado calculado.

### Passo 1 — `Termometro.java`:

```java
public class Termometro {

    private double temperaturaCelsius;

    // Getter
    public double getTemperaturaCelsius() {
        return temperaturaCelsius;
    }

    // Setter com validação de faixa
    public void setTemperaturaCelsius(double temperatura) {
        if (temperatura < -273.15) {
            System.out.println("Erro: temperatura abaixo do zero absoluto (-273.15 °C).");
            return;
        }
        this.temperaturaCelsius = temperatura;
    }

    // Getter calculado — não tem atributo correspondente
    public double getTemperaturaFahrenheit() {
        return (temperaturaCelsius * 9.0 / 5.0) + 32.0;
    }

    // Getter calculado
    public double getTemperaturaKelvin() {
        return temperaturaCelsius + 273.15;
    }

    public void exibirLeituras() {
        System.out.println("Celsius:    " + temperaturaCelsius + " °C");
        System.out.println("Fahrenheit: " + getTemperaturaFahrenheit() + " °F");
        System.out.println("Kelvin:     " + getTemperaturaKelvin() + " K");
        System.out.println("----------");
    }
}
```

### Passo 2 — `TermometroMain.java`:

```java
public class TermometroMain {

    public static void main(String[] args) {

        Termometro t1 = new Termometro();

        t1.setTemperaturaCelsius(25.0);
        t1.exibirLeituras();

        t1.setTemperaturaCelsius(100.0);
        t1.exibirLeituras();

        // Tentativa de valor impossível
        t1.setTemperaturaCelsius(-300.0);

        // Temperatura não mudou — continua 100.0
        t1.exibirLeituras();
    }
}
```

**Saída esperada:**

```
Celsius:    25.0 °C
Fahrenheit: 77.0 °F
Kelvin:     298.15 K
----------
Celsius:    100.0 °C
Fahrenheit: 212.0 °F
Kelvin:     373.15 K
----------
Erro: temperatura abaixo do zero absoluto (-273.15 °C).
Celsius:    100.0 °C
Fahrenheit: 212.0 °F
Kelvin:     373.15 K
----------
```

**Ponto-chave:** `getTemperaturaFahrenheit()` e `getTemperaturaKelvin()` são **getters calculados** — não existem atributos para Fahrenheit e Kelvin. Os valores são derivados de `temperaturaCelsius`. Isso evita dados duplicados e mantém a consistência.

---

## Exercício Autônomo 1 — Classe Reservatorio ⭐

Crie uma classe `Reservatorio` que controla o nível de água de um tanque.

**Atributos** (todos `private`):
- `capacidadeMaxima` (double) — definida uma vez, somente leitura depois
- `nivelAtual` (double) — começa em 0

**Getters:** para `capacidadeMaxima` e `nivelAtual`.

**Setter:**
- `setCapacidadeMaxima(double capacidade)` → só aceita se ainda não foi definida (valor atual == 0) e se capacidade > 0

**Métodos de negócio:**
- `adicionarAgua(double litros)` → adiciona ao nível, mas **não pode ultrapassar a capacidade máxima**. Se ultrapassar, adiciona só o que cabe e avisa.
- `retirarAgua(double litros)` → retira do nível, mas **não pode ficar abaixo de zero**. Se não houver o suficiente, avisa.
- `exibirStatus()` → imprime capacidade, nível atual e percentual de ocupação

**Teste sugerido:**
- Crie um reservatório com capacidade 1000 litros
- Adicione 800 litros
- Tente adicionar mais 300 litros → deve adicionar só 200 e avisar
- Retire 500 litros
- Exiba o status

---

## Exercício Autônomo 2 — Classe Placar ⭐

Crie uma classe `Placar` para um jogo simples entre dois times.

**Atributos** (todos `private`):
- `nomeTime1` (String)
- `nomeTime2` (String)
- `golsTime1` (int) — começa em 0, somente leitura (sem setter direto)
- `golsTime2` (int) — começa em 0, somente leitura (sem setter direto)

**Getters:** para todos os atributos.

**Setters:** apenas para `nomeTime1` e `nomeTime2` (não aceitar vazio/null).

**Métodos de negócio:**
- `golTime1()` → incrementa os gols do time 1 em 1
- `golTime2()` → incrementa os gols do time 2 em 1
- `exibirPlacar()` → imprime no formato: `Flamengo 2 x 1 Palmeiras`
- `exibirVencedor()` → imprime quem está vencendo ou se está empatado

**Teste sugerido:**
- Defina os dois times
- Registre alguns gols
- Exiba o placar e o vencedor

---

## Resumo do Bloco 3

- Encapsulamento não é só "private + getter + setter" — é **proteger o estado do objeto contra inconsistências**
- Setters com validação são a linha de defesa contra dados inválidos
- Nem todo atributo precisa de setter — alguns são somente leitura
- Getters calculados derivam valores de outros atributos sem armazenar dados redundantes
- O objeto é responsável por manter sua própria integridade

---

**Próximo passo →** [Bloco 4](../Bloco4/README.md): boas práticas de visibilidade e exercícios de consolidação.
