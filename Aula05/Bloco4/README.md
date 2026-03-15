# Bloco 4 — Regras, Boas Práticas e Consolidação

## Objetivos do Bloco

- Consolidar as regras de construtores e sobrecarga em um checklist prático
- Entender quando criar múltiplos construtores e quando um só basta
- Resolver exercícios que integram todos os conceitos da aula

---

## 4.1 Regras dos construtores — resumo definitivo

| Regra | Descrição |
|---|---|
| Mesmo nome da classe | O construtor **deve** ter exatamente o mesmo nome da classe |
| Sem tipo de retorno | Nem `void`, nem `int`, nem nada — se tiver tipo de retorno, vira um método comum |
| Chamado com `new` | O construtor é executado automaticamente quando o objeto é criado |
| Construtor padrão desaparece | Se você declarar qualquer construtor, Java não gera mais o padrão (sem parâmetros) |
| `this()` na primeira linha | Se um construtor chama outro da mesma classe, deve ser a primeira instrução |
| Pode ter validação | Construtores devem validar e garantir que o objeto nasce em estado válido |

---

## 4.2 Quando criar múltiplos construtores?

**Crie múltiplos construtores quando:**
- Alguns dados são obrigatórios e outros são opcionais
- Há cenários comuns com diferentes níveis de informação disponível
- Você quer facilitar a vida do programador que usa a classe

**Um construtor basta quando:**
- Todos os dados são obrigatórios e sempre estão disponíveis
- A classe é simples e não tem variações de criação

**Exemplo de decisão:**

```
Classe Pessoa
  → nome e cpf são obrigatórios → vão no construtor
  → telefone é opcional → vai no setter

Classe ContaBancaria
  → titular e numero são obrigatórios → vão no construtor
  → saldoInicial pode ser 0 → dois construtores:
    - ContaBancaria(titular, numero, saldoInicial)
    - ContaBancaria(titular, numero) → delega com saldo 0
```

---

## 4.3 Comparação completa — Aula 03 vs. Aula 04 vs. Aula 05

**Aula 03 — sem proteção, sem construtor:**

```java
Pessoa p1 = new Pessoa();
p1.nome = "Ana";       // acesso direto, sem validação
p1.idade = -5;         // valor absurdo aceito
```

**Aula 04 — com proteção, sem construtor:**

```java
Pessoa p1 = new Pessoa();
p1.setNome("Ana");     // validação no setter
p1.setIdade(29);       // validação no setter
// Problema: e se esquecer de chamar setNome()?
```

**Aula 05 — com proteção e construtor:**

```java
Pessoa p1 = new Pessoa("Ana", 29, "111.222.333-44");
// Objeto nasce completo e válido em uma única linha
// Compilador obriga a passar os dados
```

Cada aula adicionou uma camada de segurança. A partir de agora, **todas as classes** do curso terão: atributos `private`, construtores que garantem a inicialização, e setters apenas quando a alteração posterior fizer sentido.

---

## 4.4 Construtores vs. setters — não são concorrentes

Uma dúvida comum: "se o construtor já define os valores, para que servem os setters?"

| Construtor | Setter |
|---|---|
| Define o estado **inicial** | Altera o estado **depois** |
| Chamado uma vez, na criação | Pode ser chamado várias vezes |
| Dados obrigatórios | Dados que podem mudar |
| Garante que o objeto nasce válido | Garante que alterações são válidas |

**Exemplo prático:**
- O **nome** de uma pessoa pode mudar → precisa de setter
- O **CPF** de uma pessoa não muda → definido no construtor, sem setter
- O **saldo** de uma conta não tem setter → muda apenas por `depositar()` e `sacar()`

---

## Exercício Guiado 1 — Classe Veiculo completa (professor + alunos)

Uma classe que integra construtores sobrecarregados, validação e métodos de negócio.

### Passo 1 — `Veiculo.java`:

```java
public class Veiculo {

    private String placa;
    private String modelo;
    private int ano;
    private double quilometragem;

    // Construtor completo
    public Veiculo(String placa, String modelo, int ano, double quilometragem) {
        this.placa = (placa == null || placa.isEmpty()) ? "AAA-0000" : placa;
        this.modelo = (modelo == null || modelo.isEmpty()) ? "Não informado" : modelo;
        this.ano = (ano < 1886 || ano > 2026) ? 2024 : ano;
        this.quilometragem = (quilometragem < 0) ? 0 : quilometragem;
    }

    // Construtor para veículo novo (km = 0)
    public Veiculo(String placa, String modelo, int ano) {
        this(placa, modelo, ano, 0.0);
    }

    // --- Getters ---

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public double getQuilometragem() {
        return quilometragem;
    }

    // Sem setter para placa (não muda) e ano (não muda)

    // --- Setters ---

    public void setModelo(String modelo) {
        if (modelo == null || modelo.isEmpty()) {
            System.out.println("Erro: modelo não pode ser vazio.");
            return;
        }
        this.modelo = modelo;
    }

    // --- Métodos de negócio ---

    // Versão simples
    public void registrarViagem(double km) {
        if (km <= 0) {
            System.out.println("Erro: quilometragem deve ser positiva.");
            return;
        }
        quilometragem = quilometragem + km;
        System.out.println("Viagem de " + km + " km registrada.");
    }

    // Versão com destino (sobrecarga)
    public void registrarViagem(double km, String destino) {
        if (km <= 0) {
            System.out.println("Erro: quilometragem deve ser positiva.");
            return;
        }
        quilometragem = quilometragem + km;
        System.out.println("Viagem de " + km + " km para " + destino + " registrada.");
    }

    public void exibirDados() {
        System.out.println("Placa: " + placa + " | Modelo: " + modelo);
        System.out.println("Ano: " + ano + " | Km: " + quilometragem);
        System.out.println("----------");
    }
}
```

### Passo 2 — `VeiculoMain.java`:

```java
public class VeiculoMain {

    public static void main(String[] args) {

        // Veículo seminovo (construtor completo)
        Veiculo v1 = new Veiculo("ABC-1234", "Civic", 2022, 35000.0);

        // Veículo novo (construtor sem km)
        Veiculo v2 = new Veiculo("XYZ-9876", "Corolla", 2025);

        v1.exibirDados();
        v2.exibirDados();

        // Registrando viagens (sobrecarga de método)
        v1.registrarViagem(120.5);
        v1.registrarViagem(280.0, "São Paulo");

        v2.registrarViagem(50.0, "Santos");

        System.out.println();
        v1.exibirDados();
        v2.exibirDados();
    }
}
```

**Saída esperada:**

```
Placa: ABC-1234 | Modelo: Civic
Ano: 2022 | Km: 35000.0
----------
Placa: XYZ-9876 | Modelo: Corolla
Ano: 2025 | Km: 0.0
----------
Viagem de 120.5 km registrada.
Viagem de 280.0 km para São Paulo registrada.
Viagem de 50.0 km para Santos registrada.

Placa: ABC-1234 | Modelo: Civic
Ano: 2022 | Km: 35400.5
----------
Placa: XYZ-9876 | Modelo: Corolla
Ano: 2025 | Km: 50.0
----------
```

**Destaque:** esta classe combina tudo da aula — dois construtores (com `this()`), sobrecarga de método (`registrarViagem` com e sem destino), atributos somente leitura (placa, ano), e validação tanto no construtor quanto nos métodos.

---

## Exercício Guiado 2 — Classe Turma (professor + alunos)

Uma classe que gerencia alunos com diferentes formas de criar e exibir.

### Passo 1 — `Turma.java`:

```java
public class Turma {

    private String codigo;
    private String disciplina;
    private int totalAlunos;
    private int capacidadeMaxima;

    // Construtor completo
    public Turma(String codigo, String disciplina, int capacidadeMaxima) {
        this.codigo = (codigo == null || codigo.isEmpty()) ? "T000" : codigo;
        this.disciplina = (disciplina == null || disciplina.isEmpty()) ? "Não definida" : disciplina;
        this.capacidadeMaxima = (capacidadeMaxima <= 0) ? 40 : capacidadeMaxima;
        this.totalAlunos = 0;
    }

    // Construtor sem capacidade (usa padrão de 40 alunos)
    public Turma(String codigo, String disciplina) {
        this(codigo, disciplina, 40);
    }

    // --- Getters ---

    public String getCodigo() {
        return codigo;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public int getTotalAlunos() {
        return totalAlunos;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    // Getter calculado
    public int getVagasDisponiveis() {
        return capacidadeMaxima - totalAlunos;
    }

    // --- Métodos de negócio ---

    // Matrícula simples (1 aluno)
    public void matricular() {
        if (totalAlunos >= capacidadeMaxima) {
            System.out.println("Turma " + codigo + " lotada. Matrícula recusada.");
            return;
        }
        totalAlunos++;
        System.out.println("Aluno matriculado na turma " + codigo + ". Total: " + totalAlunos);
    }

    // Matrícula em lote (vários alunos)
    public void matricular(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Erro: quantidade deve ser positiva.");
            return;
        }
        int vagasDisponiveis = getVagasDisponiveis();
        if (quantidade > vagasDisponiveis) {
            System.out.println("Erro: só há " + vagasDisponiveis + " vagas na turma " + codigo + ".");
            return;
        }
        totalAlunos = totalAlunos + quantidade;
        System.out.println(quantidade + " alunos matriculados na turma " + codigo + ". Total: " + totalAlunos);
    }

    // Exibição simples
    public void exibirDados() {
        System.out.println("Turma " + codigo + " — " + disciplina);
        System.out.println("Alunos: " + totalAlunos + "/" + capacidadeMaxima
                + " (vagas: " + getVagasDisponiveis() + ")");
        System.out.println("----------");
    }
}
```

### Passo 2 — `TurmaMain.java`:

```java
public class TurmaMain {

    public static void main(String[] args) {

        // Turma com capacidade personalizada
        Turma t1 = new Turma("POO-2025", "Programação Orientada a Objetos", 3);

        // Turma com capacidade padrão (40)
        Turma t2 = new Turma("BD-2025", "Banco de Dados");

        t1.exibirDados();
        t2.exibirDados();

        // Matrícula individual
        t1.matricular();
        t1.matricular();

        // Matrícula em lote
        t2.matricular(15);

        // Tentativa de exceder capacidade
        t1.matricular(5);

        System.out.println();
        t1.exibirDados();
        t2.exibirDados();
    }
}
```

**Saída esperada:**

```
Turma POO-2025 — Programação Orientada a Objetos
Alunos: 0/3 (vagas: 3)
----------
Turma BD-2025 — Banco de Dados
Alunos: 0/40 (vagas: 40)
----------
Aluno matriculado na turma POO-2025. Total: 1
Aluno matriculado na turma POO-2025. Total: 2
15 alunos matriculados na turma BD-2025. Total: 15
Erro: só há 1 vagas na turma POO-2025.

Turma POO-2025 — Programação Orientada a Objetos
Alunos: 2/3 (vagas: 1)
----------
Turma BD-2025 — Banco de Dados
Alunos: 15/40 (vagas: 25)
----------
```

**Discussão:** note a combinação de técnicas — construtor sobrecarregado (com e sem capacidade), método sobrecarregado (`matricular()` individual e em lote), getter calculado (`getVagasDisponiveis()`), e validação em todos os pontos.

---

## Exercício Autônomo 1 — Classe Pedido ⭐⭐

Crie uma classe `Pedido` para um sistema de restaurante.

**Atributos** (todos `private`):
- `numero` (int) — definido no construtor, somente leitura
- `nomeCliente` (String)
- `totalItens` (int) — começa em 0
- `valorTotal` (double) — começa em 0.0

**Construtores:**
- `Pedido(int numero, String nomeCliente)` — construtor completo
- Validação: número <= 0 → usar 1; nome vazio → "Cliente"

**Métodos sobrecarregados `adicionarItem()`:**
- `adicionarItem(double preco)` — adiciona 1 item com o preço informado
- `adicionarItem(double preco, int quantidade)` — adiciona múltiplos itens

**Outros métodos:**
- `exibirResumo()` — imprime número, cliente, total de itens e valor total

**Teste em `PedidoMain`:**
- Crie um pedido
- Adicione 1 item de R$ 25.00
- Adicione 3 itens de R$ 12.50
- Exiba o resumo (total: 4 itens, R$ 62.50)

---

## Exercício Autônomo 2 — Classe ContaPoupanca com construtor ⭐⭐

Reescreva a classe `ContaPoupanca` da Aula 04, agora com construtores.

**Construtores sobrecarregados:**
- `ContaPoupanca(String titular, double saldoInicial, double taxaRendimentoMensal)` — construtor completo
- `ContaPoupanca(String titular, double saldoInicial)` — taxa padrão de 0.005 (0,5%)
- `ContaPoupanca(String titular)` — saldo 0.0 e taxa padrão 0.005

**Validações:** titular vazio → "Sem nome"; saldo negativo → 0; taxa fora de 0–1 → 0.005

**Métodos:** mantidos da Aula 04 (`depositar`, `sacar`, `aplicarRendimento`, `exibirExtrato`)

**Teste em `ContaPoupancaMain`:**
- Crie uma conta com todos os dados
- Crie outra só com nome e saldo (taxa padrão)
- Crie outra só com nome (saldo 0 e taxa padrão)
- Deposite na terceira e aplique rendimento
- Exiba o extrato de todas

---

## Exercícios de consolidação — pasta de exercícios

Os exercícios autônomos deste bloco também estão organizados na pasta [`exercicios/`](./exercicios/README-Bl4Ex.md) com enunciados detalhados e gabarito.

---

## Resumo do Bloco 4

- Construtores garantem que o objeto nasce válido; setters permitem alterações posteriores
- Múltiplos construtores fazem sentido quando há dados obrigatórios e opcionais
- `this()` centraliza a validação no construtor mais completo
- Sobrecarga de métodos torna a API flexível — mesmo nome, comportamento adaptado ao contexto
- A partir de agora, todas as classes terão: `private`, construtores, getters, setters com validação e métodos de negócio

---

## Resumo geral da Aula 05

| Conceito | Em uma frase |
|---|---|
| Construtor | Método especial que inicializa o objeto no momento da criação com `new` |
| Construtor padrão | Gerado automaticamente por Java quando nenhum construtor é declarado |
| Construtor personalizado | Recebe parâmetros obrigatórios e garante que o objeto nasce válido |
| `this.atributo` | Referência ao atributo do objeto atual — diferencia de parâmetros com mesmo nome |
| `this()` | Chama outro construtor da mesma classe — deve ser a primeira instrução |
| Sobrecarga (overloading) | Mesmo nome de método/construtor, parâmetros diferentes |
| Resolução estática | Java decide qual versão sobrecarregada chamar em tempo de compilação |

---

**Próxima aula →** Aula 06 — Relacionamentos entre Classes: como objetos se conectam e colaboram, usando associação, agregação e composição.
