# Bloco 1 — Conceito de Associação: "Conhece Um"

## Objetivos do Bloco

- Entender o que é associação entre classes
- Diferenciar objetos isolados de objetos conectados
- Implementar referências entre objetos em Java
- Navegar através de relacionamentos usando getters

---

## 1.1 O que é Associação?

**Associação** é o relacionamento mais básico entre classes. Significa que:

> "Um objeto **conhece** outro objeto e pode se comunicar com ele."

Isso é representado em código quando **uma classe tem um atributo que é do tipo de outra classe**.

### Analogia do mundo real

Pense em **você** e seu **médico**:
- Você tem o nome e telefone do seu médico
- Você pode ligar para ele quando precisar
- **MAS:** vocês existem independentemente
- Se você mudar de médico, ambos continuam existindo

Isso é **associação**: uma conexão, mas sem dependência de vida.

---

## 1.2 Código sem associação vs. com associação

### Sem associação (classes isoladas)

```java
public class Aluno {
    private String nome;
    private String matricula;
    
    // ... getters, setters, construtor
}

public class Turma {
    private String codigo;
    private String disciplina;
    
    // ... getters, setters, construtor
}

// No main:
Aluno joao = new Aluno("João", "2024001");
Turma turmaJava = new Turma("POO-01", "Programação OO");

// Problema: como saber que João está na turma de POO?
// Não há ligação entre os objetos!
```

### Com associação (classes conectadas)

```java
public class Aluno {
    private String nome;
    private String matricula;
    private Turma turma;  // ← Associação! Aluno CONHECE sua turma
    
    // Construtor
    public Aluno(String nome, String matricula, Turma turma) {
        this.nome = nome;
        this.matricula = matricula;
        this.turma = turma;
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }
    public Turma getTurma() { return turma; }
    
    // Método que usa a associação
    public void mostrarInfo() {
        System.out.println("Aluno: " + nome);
        System.out.println("Matrícula: " + matricula);
        System.out.println("Turma: " + turma.getDisciplina()); // ← Navegando!
    }
}

public class Turma {
    private String codigo;
    private String disciplina;
    
    // Construtor
    public Turma(String codigo, String disciplina) {
        this.codigo = codigo;
        this.disciplina = disciplina;
    }
    
    // Getters
    public String getCodigo() { return codigo; }
    public String getDisciplina() { return disciplina; }
}

// No main:
Turma turmaJava = new Turma("POO-01", "Programação OO");
Aluno joao = new Aluno("João", "2024001", turmaJava);

joao.mostrarInfo();
// Saída:
// Aluno: João
// Matrícula: 2024001
// Turma: Programação OO
```

**O que mudou?**
1. `Aluno` agora **tem um atributo** do tipo `Turma`
2. Podemos **navegar** de Aluno para Turma usando `getTurma()`
3. `joao` **conhece** sua turma e pode acessar informações dela

---

## 1.3 Representação em UML (Diagrama de Classes)

```
┌─────────────┐              ┌─────────────┐
│   Aluno     │              │    Turma    │
├─────────────┤              ├─────────────┤
│ - nome      │              │ - codigo    │
│ - matricula │              │ - disciplina│
│ - turma     │─────────────>│             │
└─────────────┘              └─────────────┘
     1                              1
```

A **seta** indica: "Aluno conhece Turma"  
Os números indicam **multiplicidade**: 1 aluno está em 1 turma

**Leia como:** "Um Aluno conhece uma Turma"

---

## 1.4 Características da Associação

✅ **Um objeto tem referência para outro**  
✅ **Pode navegar de um para outro** (através de getters)  
✅ **Ciclos de vida independentes** — se deletar Aluno, Turma continua existindo  
✅ **Relacionamento mais fraco** — é só uma "conexão"  

⚠️ **Cuidado:** Se `turma` for `null`, `joao.getTurma().getDisciplina()` vai dar **NullPointerException**!

```java
Aluno maria = new Aluno("Maria", "2024002", null); // sem turma
maria.mostrarInfo(); // ❌ Erro em runtime!
```

**Solução:** Sempre verificar antes de navegar:

```java
public void mostrarInfo() {
    System.out.println("Aluno: " + nome);
    System.out.println("Matrícula: " + matricula);
    
    if (turma != null) {
        System.out.println("Turma: " + turma.getDisciplina());
    } else {
        System.out.println("Turma: Não matriculado");
    }
}
```

---

## 1.5 Associação Bidirecional (opcional, mas importante conhecer)

Até agora, só `Aluno` conhece `Turma`. E se quisermos que `Turma` também conheça seus alunos?

```java
public class Turma {
    private String codigo;
    private String disciplina;
    private Aluno[] alunos;  // ← Turma conhece seus alunos
    private int qtdAlunos;
    
    public Turma(String codigo, String disciplina, int capacidade) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.alunos = new Aluno[capacidade];
        this.qtdAlunos = 0;
    }
    
    public void adicionarAluno(Aluno aluno) {
        if (qtdAlunos < alunos.length) {
            alunos[qtdAlunos] = aluno;
            qtdAlunos++;
        }
    }
    
    public void listarAlunos() {
        System.out.println("Alunos da turma " + codigo + ":");
        for (int i = 0; i < qtdAlunos; i++) {
            System.out.println("- " + alunos[i].getNome());
        }
    }
    
    // ... outros métodos
}
```

Agora temos **associação bidirecional**:
- `Aluno` conhece `Turma`
- `Turma` conhece seus `Alunos`

---

## Exercício Guiado 1 — Sistema Médico-Paciente (professor + alunos)

Vamos criar um sistema onde **Pacientes conhecem seus Médicos**.

### Passo 1 — Crie a classe `Medico.java`:

```java
public class Medico {
    private String nome;
    private String crm;
    private String especialidade;
    
    public Medico(String nome, String crm, String especialidade) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCrm() {
        return crm;
    }
    
    public String getEspecialidade() {
        return especialidade;
    }
    
    public void atender() {
        System.out.println("Dr(a). " + nome + " está atendendo.");
    }
}
```

### Passo 2 — Crie a classe `Paciente.java` com associação:

```java
public class Paciente {
    private String nome;
    private String cpf;
    private int idade;
    private Medico medicoResponsavel;  // ← ASSOCIAÇÃO
    
    public Paciente(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.medicoResponsavel = null;  // Ainda não tem médico
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public int getIdade() {
        return idade;
    }
    
    public Medico getMedicoResponsavel() {
        return medicoResponsavel;
    }
    
    // Método para atribuir um médico
    public void setMedicoResponsavel(Medico medico) {
        this.medicoResponsavel = medico;
    }
    
    // Método que usa a associação
    public void marcarConsulta() {
        if (medicoResponsavel != null) {
            System.out.println("Paciente: " + nome);
            System.out.println("Médico: Dr(a). " + medicoResponsavel.getNome());
            System.out.println("Especialidade: " + medicoResponsavel.getEspecialidade());
            medicoResponsavel.atender();
        } else {
            System.out.println("Paciente " + nome + " ainda não tem médico responsável.");
        }
    }
}
```

### Passo 3 — Crie `SistemaMedico.java` para testar:

```java
public class SistemaMedico {
    public static void main(String[] args) {
        // Criar médicos
        Medico dra_ana = new Medico("Ana Silva", "12345-SP", "Cardiologia");
        Medico dr_carlos = new Medico("Carlos Souza", "67890-SP", "Ortopedia");
        
        // Criar pacientes
        Paciente joao = new Paciente("João Oliveira", "111.222.333-44", 45);
        Paciente maria = new Paciente("Maria Santos", "555.666.777-88", 32);
        
        // Associar pacientes a médicos
        joao.setMedicoResponsavel(dra_ana);
        maria.setMedicoResponsavel(dr_carlos);
        
        // Usar as associações
        System.out.println("=== Consulta 1 ===");
        joao.marcarConsulta();
        
        System.out.println("\n=== Consulta 2 ===");
        maria.marcarConsulta();
        
        // Paciente sem médico
        System.out.println("\n=== Consulta 3 ===");
        Paciente pedro = new Paciente("Pedro Lima", "999.888.777-66", 28);
        pedro.marcarConsulta();
    }
}
```

### Saída esperada:

```
=== Consulta 1 ===
Paciente: João Oliveira
Médico: Dr(a). Ana Silva
Especialidade: Cardiologia
Dr(a). Ana Silva está atendendo.

=== Consulta 2 ===
Paciente: Maria Santos
Médico: Dr(a). Carlos Souza
Especialidade: Ortopedia
Dr(a). Carlos Souza está atendendo.

=== Consulta 3 ===
Paciente Pedro Lima ainda não tem médico responsável.
```

### O que observar:

1. ✅ `Paciente` tem um atributo `Medico` — isso é **associação**
2. ✅ Podemos **navegar** de Paciente para Médico com `getMedicoResponsavel()`
3. ✅ Médico e Paciente **existem independentemente**
4. ✅ Sempre **validamos null** antes de usar a associação

---

## Exercício Autônomo 1 — Sistema Produto-Fornecedor

**Contexto:** Uma loja precisa registrar produtos e seus fornecedores.

**Objetivo:** Criar classes com associação onde um Produto conhece seu Fornecedor.

### Requisitos:

1. Classe `Fornecedor`:
   - Atributos: `nome`, `cnpj`, `telefone`
   - Construtor e getters
   - Método `void exibirInfo()` que imprime os dados do fornecedor

2. Classe `Produto`:
   - Atributos: `nome`, `preco`, `quantidade`, `fornecedor` (do tipo `Fornecedor`)
   - Construtor (recebe nome, preco, quantidade — fornecedor começa null)
   - Getters e setter para `fornecedor`
   - Método `void exibirDetalhes()` que mostra:
     - Nome do produto
     - Preço
     - Quantidade em estoque
     - Dados do fornecedor (se houver)

3. Classe `LojaMain`:
   - Crie 2 fornecedores
   - Crie 3 produtos
   - Associe produtos a fornecedores
   - Chame `exibirDetalhes()` de cada produto

### Exemplo de saída esperada:

```
Produto: Mouse Gamer
Preço: R$ 89.90
Estoque: 50 unidades
Fornecedor: TechSupply - CNPJ: 12.345.678/0001-90 - Tel: (11) 3456-7890

Produto: Teclado Mecânico
Preço: R$ 349.90
Estoque: 25 unidades
Fornecedor: PeriféricosPro - CNPJ: 98.765.432/0001-10 - Tel: (11) 9876-5432

Produto: Monitor 24"
Preço: R$ 899.90
Estoque: 15 unidades
Fornecedor: Não cadastrado
```

### Dicas:

- Sempre valide se `fornecedor != null` antes de acessar
- Use `String.format()` ou concatenação para formatar o preço
- Lembre-se: a associação permite **navegar** de Produto para Fornecedor

---

## Resumo do Bloco 1

Neste bloco você aprendeu:

✅ **Associação** = um objeto conhece outro objeto  
✅ Implementar associação = adicionar **atributo do tipo de outra classe**  
✅ **Navegar** através de relacionamentos usando getters  
✅ **Sempre validar null** antes de usar referências  
✅ Ciclos de vida **independentes** — deletar um não afeta o outro  

---

## Próximo passo

No **Bloco 2**, você aprenderá sobre **Agregação** — uma associação especial onde um objeto "tem" outro, representando um relacionamento do tipo "todo-parte" com vidas independentes.

[➡️ Ir para Bloco 2](../Bloco2/README.md)
