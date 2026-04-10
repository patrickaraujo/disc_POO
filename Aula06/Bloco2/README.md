# Bloco 2–4 (Unificado) — Agregação, Composição e Integração

> ⏱️ Tempo estimado: 50–60 minutos

---

## Objetivos

- Entender a diferença entre **Agregação** e **Composição**
- Identificar qual usar baseado no ciclo de vida dos objetos
- Implementar os dois tipos em Java

---

## Revisão rápida: os três tipos de relacionamento

| Tipo | Símbolo | Significa | A parte sobrevive sem o todo? |
|------|---------|-----------|-------------------------------|
| Associação | → | "conhece um" | ✅ Sim |
| **Agregação** | **◇** | **"tem um"** | **✅ Sim** |
| **Composição** | **◆** | **"é composto de"** | **❌ Não** |

### Pergunta-chave para decidir:
> "Se eu **deletar o TODO**, a **PARTE** deve ser deletada junto?"
> - **SIM** → Composição ◆
> - **NÃO** → Agregação ◇

### Exemplos fáceis de lembrar:

| Situação | Tipo | Por quê? |
|----------|------|----------|
| Time tem Jogadores | Agregação ◇ | Jogador pode mudar de time |
| Pedido tem Itens | Composição ◆ | Item não existe fora do pedido |
| Empresa tem Funcionários | Agregação ◇ | Funcionário pode mudar de empresa |
| Casa tem Quartos | Composição ◆ | Quarto não existe sem a casa |

---

## Como identificar no código Java

### Agregação — a parte é criada **fora** e passada por parâmetro:

```java
// Jogador existe antes do Time
Jogador j1 = new Jogador("João");
time.adicionarJogador(j1);  // ← recebe pronto → AGREGAÇÃO
```

### Composição — a parte é criada **dentro** da classe:

```java
// ItemPedido só existe dentro de Pedido
public void adicionarItem(String produto, double preco) {
    itens[qtd] = new ItemPedido(produto, preco);  // ← cria aqui → COMPOSIÇÃO
}
```

---

## Exercício Tutoriado — Sistema Escola (faremos juntos)

**Cenário:** Uma escola tem turmas (agregação). Cada turma tem aulas que só existem dentro dela (composição).

### Passo 1 — Crie `Aluno.java`

```java
public class Aluno {
    private String nome;
    private String matricula;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }

    public void apresentar() {
        System.out.println("Aluno: " + nome + " | Matrícula: " + matricula);
    }
}
```

---

### Passo 2 — Crie `Aula.java`

> ⚠️ Repare: construtor sem `public` — só `Turma` pode criar uma `Aula`.

```java
public class Aula {
    private String data;
    private String assunto;

    // Construtor package-private (só Turma cria)
    Aula(String data, String assunto) {
        this.data = data;
        this.assunto = assunto;
    }

    public void exibir() {
        System.out.println("  [" + data + "] " + assunto);
    }
}
```

---

### Passo 3 — Crie `Turma.java`

```java
public class Turma {
    private String codigo;
    private Aluno[] alunos;       // ← AGREGAÇÃO (aluno pode mudar de turma)
    private int qtdAlunos;
    private Aula[] aulas;         // ← COMPOSIÇÃO (aula não existe sem turma)
    private int qtdAulas;

    public Turma(String codigo) {
        this.codigo = codigo;
        this.alunos = new Aluno[30];
        this.qtdAlunos = 0;
        this.aulas = new Aula[50];
        this.qtdAulas = 0;
    }

    // AGREGAÇÃO: recebe aluno pronto
    public void matricularAluno(Aluno aluno) {
        alunos[qtdAlunos] = aluno;
        qtdAlunos++;
        System.out.println(aluno.getNome() + " matriculado em " + codigo);
    }

    // COMPOSIÇÃO: cria a aula aqui dentro
    public void agendarAula(String data, String assunto) {
        aulas[qtdAulas] = new Aula(data, assunto);  // ← cria aqui!
        qtdAulas++;
        System.out.println("Aula agendada: " + assunto);
    }

    public void exibirInfo() {
        System.out.println("\n=== Turma " + codigo + " ===");

        System.out.println("Alunos:");
        for (int i = 0; i < qtdAlunos; i++) {
            System.out.print("  ");
            alunos[i].apresentar();
        }

        System.out.println("Aulas:");
        for (int i = 0; i < qtdAulas; i++) {
            aulas[i].exibir();
        }
    }
}
```

---

### Passo 4 — Crie `EscolaMain.java` e teste

```java
public class EscolaMain {
    public static void main(String[] args) {

        // Alunos existem ANTES da turma (podem mudar de turma)
        Aluno a1 = new Aluno("Maria", "2024001");
        Aluno a2 = new Aluno("Pedro", "2024002");
        Aluno a3 = new Aluno("Lucas", "2024003");

        // Criar turma
        Turma turma = new Turma("POO-101");

        // AGREGAÇÃO: matricular alunos existentes
        turma.matricularAluno(a1);
        turma.matricularAluno(a2);
        turma.matricularAluno(a3);

        // COMPOSIÇÃO: agendar aulas (criadas dentro da turma)
        turma.agendarAula("10/04", "Classes e Objetos");
        turma.agendarAula("12/04", "Encapsulamento");
        turma.agendarAula("15/04", "Relacionamentos entre Classes");

        // Exibir resultado
        turma.exibirInfo();

        // Demonstrar diferença de ciclo de vida
        System.out.println("\n--- Se deletarmos a turma: ---");
        System.out.println("Alunos CONTINUAM existindo (Agregação):");
        a1.apresentar();
        a2.apresentar();
        System.out.println("Aulas seriam deletadas junto (Composição).");
    }
}
```

### Saída esperada:

```
Maria matriculado em POO-101
Pedro matriculado em POO-101
Lucas matriculado em POO-101
Aula agendada: Classes e Objetos
Aula agendada: Encapsulamento
Aula agendada: Relacionamentos entre Classes

=== Turma POO-101 ===
Alunos:
  Aluno: Maria | Matrícula: 2024001
  Aluno: Pedro | Matrícula: 2024002
  Aluno: Lucas | Matrícula: 2024003
Aulas:
  [10/04] Classes e Objetos
  [12/04] Encapsulamento
  [15/04] Relacionamentos entre Classes

--- Se deletarmos a turma: ---
Alunos CONTINUAM existindo (Agregação):
Aluno: Maria | Matrícula: 2024001
Aluno: Pedro | Matrícula: 2024002
Aulas seriam deletadas junto (Composição).
```

---

## Exercício Autônomo — Sistema de Playlist

**Cenário:** Uma playlist tem músicas que existem independentemente (agregação). Cada música tem letra, que só existe dentro da música (composição).

> 🎯 Nível: fácil — siga o mesmo padrão do exercício tutoriado.

---

### O que você deve implementar:

#### Classe `Musica`
- Atributos: `titulo` (String), `artista` (String), `duracao` (int, em segundos)
- Construtor normal com `public`
- Getters
- Método `void apresentar()` — exibe título, artista e duração formatada (ex: "3:45")

#### Classe `Trecho` *(composição — só existe dentro de Música)*
- Atributos: `parte` (String, ex: "Refrão"), `texto` (String)
- Construtor **sem `public`** (package-private)
- Método `void exibir()`

#### Classe `Playlist`
- Atributos: `nome` (String), `musicas[]` (array de Música, capacidade 20), `qtdMusicas`
- Construtor recebe `nome`
- Método `void adicionarMusica(Musica m)` — **agregação**, recebe música pronta
- Método `void listarMusicas()` — exibe todas as músicas
- Método `int calcularDuracaoTotal()` — soma duração de todas as músicas

#### Classe `PlaylistMain`
1. Crie 4 músicas
2. Adicione trechos a pelo menos 1 música (composição)
3. Crie uma playlist
4. Adicione as músicas à playlist
5. Liste a playlist e mostre a duração total
6. Demonstre que a música continua existindo mesmo fora da playlist

---

### Dica para formatar duração:

```java
// Converter segundos em mm:ss
int minutos = duracao / 60;
int segundos = duracao % 60;
System.out.printf("%d:%02d%n", minutos, segundos);
```

---

### Checklist de entrega:

- [ ] `Musica.java` criado com construtor `public`
- [ ] `Trecho.java` criado com construtor package-private (sem `public`)
- [ ] `Playlist.java` com método `adicionarMusica` recebendo objeto pronto (agregação)
- [ ] `Musica` cria os `Trecho` internamente (composição)
- [ ] `PlaylistMain.java` demonstra que músicas continuam existindo fora da playlist

---

## Resumo dos conceitos

```
AGREGAÇÃO ◇
  - A parte é passada por parâmetro (já existe antes)
  - Parte pode mudar de "todo"
  - Deletar o todo NÃO deleta as partes
  - Ex: Time → Jogadores, Empresa → Funcionários

COMPOSIÇÃO ◆
  - A parte é criada DENTRO da classe
  - Construtor da parte pode ser package-private
  - Deletar o todo DELETA as partes
  - Ex: Pedido → Itens, Turma → Aulas
```

**Próxima aula:** Herança com `extends` e `super` 🚀
