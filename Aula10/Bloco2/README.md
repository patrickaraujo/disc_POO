# Bloco 2 — Exercício Guiado: Sistema de Capacidades de Documentos

## Objetivos do Bloco

- Aplicar interfaces em um sistema realista
- Implementar **múltiplas interfaces** em uma única classe
- Mostrar como classes diferentes implementam **combinações diferentes** das mesmas interfaces
- Usar **polimorfismo por capacidade** (`List<Imprimivel>`, `List<Assinavel>`, ...)
- Demonstrar a combinação **herança + interfaces** quando faz sentido
- Mostrar como adicionar uma **nova capacidade** (interface) sem alterar código existente

---

## 🎯 Problema: Capacidades de Documentos em um Sistema Corporativo

A empresa **TechBR** tem um sistema interno que lida com **vários tipos de documentos**, e cada tipo tem **capacidades diferentes**:

| Documento | Pode imprimir? | Precisa assinar? | É auditável? |
|-----------|:--:|:--:|:--:|
| **Contrato** (jurídico) | ✅ | ✅ | ✅ |
| **Relatório Financeiro** | ✅ | ❌ | ✅ |
| **E-mail Interno** | ✅ | ❌ | ❌ |

Note: **as capacidades não se acumulam por hierarquia**. Não existe um "documento básico" que outros estendem — cada tipo tem **sua combinação própria** de capacidades. Esse é o cenário clássico onde **interfaces brilham**.

### As 3 Capacidades

1. **Imprimível** — gera uma versão visual do documento (header + conteúdo)
2. **Assinável** — recebe assinaturas digitais e permite verificar quem assinou
3. **Auditável** — registra todos os acessos (quem leu, quando) e disponibiliza o histórico

### Características Comuns dos Documentos

Todo documento (independente das capacidades) tem:
- Identificador (ID)
- Título
- Autor (quem criou)
- Conteúdo (texto)
- Data de criação

> 💡 Como há **estado comum** entre todos os documentos, faz sentido extrair isso em uma **classe abstrata** `Documento`. As capacidades extras vão como **interfaces**. Esse é exatamente o cenário "herança + interfaces" que vimos no Bloco 1.

---

## Planejamento da Hierarquia

```
                ┌──────────────────────────────┐
                │  « abstract » Documento      │  ← Estado comum
                ├──────────────────────────────┤
                │ # id, titulo, autor, ...     │
                └──────────────┬───────────────┘
                               │ extends
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
   ┌────▼─────┐         ┌──────▼──────────┐    ┌──────▼──────────┐
   │ Contrato │         │RelatorioFinanceiro│    │  EmailInterno   │
   └────┬─────┘         └──────┬──────────┘    └────────┬────────┘
        │                      │                        │
        │ implements           │ implements             │ implements
        ▼                      ▼                        ▼
   ┌──────────┐           ┌──────────┐              ┌──────────┐
   │Imprimivel│           │Imprimivel│              │Imprimivel│
   │Assinavel │           │Auditavel │              └──────────┘
   │Auditavel │           └──────────┘
   └──────────┘
```

**Decisões de design:**
- ✅ `Documento` é **classe abstrata** (tem estado comum)
- ✅ `Imprimivel`, `Assinavel` e `Auditavel` são **interfaces** (capacidades transversais)
- ✅ Cada subclasse concreta **escolhe** quais interfaces implementar
- ✅ Não há "documento básico instanciável" — todo documento é de um tipo específico
- ✅ Capacidades podem ser combinadas livremente (sem subclasses extras)

---

## Passo 1 — Criar a classe abstrata `Documento`

Tudo que é **comum** a qualquer documento vai aqui. Nenhuma capacidade extra fica nesta classe — todas vão nas interfaces.

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Documento {

    protected String id;
    protected String titulo;
    protected String autor;
    protected String conteudo;
    protected LocalDateTime dataCriacao;

    public Documento(String id, String titulo, String autor, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.conteudo = conteudo;
        this.dataCriacao = LocalDateTime.now();
    }

    // Método abstrato — cada tipo se identifica
    public abstract String getTipoDocumento();

    // Método concreto auxiliar usado por implementações de Imprimivel
    protected String formatarCabecalho() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "═══════════════════════════════════════════\n"
             + "  " + getTipoDocumento() + " — " + titulo + "\n"
             + "  ID: " + id + " | Autor: " + autor + "\n"
             + "  Criado em: " + dataCriacao.format(fmt) + "\n"
             + "═══════════════════════════════════════════";
    }

    // Getters
    public String getId()       { return id; }
    public String getTitulo()   { return titulo; }
    public String getAutor()    { return autor; }
    public String getConteudo() { return conteudo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
```

**Discussão com os alunos:**
- Por que `Documento` é abstrata e não interface? **Tem estado** (atributos de instância) — interface não pode ter.
- Por que `getTipoDocumento()` é abstrato? Cada subclasse vai se identificar pelo seu nome amigável.
- O método `formatarCabecalho()` será usado pelas implementações da interface `Imprimivel` nas subclasses — é reuso de código sem poluir as interfaces.

---

## Passo 2 — Criar a interface `Imprimivel`

```java
public interface Imprimivel {

    // Imprime o documento (cabeçalho + conteúdo formatado)
    void imprimir();

    // Retorna estimativa de páginas (para fila de impressão, contagem de toner, etc.)
    int estimarPaginas();
}
```

**Discussão:**
- Note que **não há corpo** nos métodos — só assinaturas.
- `public abstract` está implícito (não precisamos escrever).
- Esta interface declara **o que** qualquer documento imprimível deve saber fazer.

---

## Passo 3 — Criar a interface `Assinavel`

```java
import java.util.List;

public interface Assinavel {

    // Adiciona uma assinatura ao documento
    void assinar(String nomeResponsavel, String cargoResponsavel);

    // Retorna se o documento já tem todas as assinaturas necessárias
    boolean estaCompletamenteAssinado();

    // Retorna lista de nomes de quem já assinou
    List<String> getAssinantes();
}
```

**Discussão:**
- A interface define **3 capacidades relacionadas a assinatura**.
- Cada classe que implementar vai precisar gerenciar **seu próprio** estado de assinaturas (porque interface não tem estado).
- A regra "completamente assinado" pode variar por tipo de documento (contrato pede 2 assinaturas, recibo pede 1, etc.).

---

## Passo 4 — Criar a interface `Auditavel`

```java
import java.util.List;

public interface Auditavel {

    // Registra que alguém acessou o documento
    void registrarAcesso(String usuario);

    // Retorna a lista completa de acessos (formato livre, ex: "ana@techbr — 09/05/2026 14:35")
    List<String> getHistoricoAcessos();

    // Retorna a quantidade total de acessos
    int totalAcessos();
}
```

**Discussão:**
- `Auditavel` exige que a classe **mantenha um histórico** — novamente, o estado fica na classe, não na interface.
- Total de acessos poderia ser obtido por `getHistoricoAcessos().size()`, mas expor um método dedicado torna a interface mais expressiva.

---

## Passo 5 — Implementar `Contrato` (3 interfaces!)

`Contrato` é o caso mais rico — implementa **todas as 3 interfaces**, além de herdar de `Documento`.

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Contrato extends Documento
                      implements Imprimivel, Assinavel, Auditavel {

    private static final int ASSINATURAS_NECESSARIAS = 2;

    // Estado próprio para Assinavel
    private List<String> assinantes;
    private List<String> cargosAssinantes;

    // Estado próprio para Auditavel
    private List<String> historicoAcessos;

    public Contrato(String id, String titulo, String autor, String conteudo) {
        super(id, titulo, autor, conteudo);
        this.assinantes = new ArrayList<>();
        this.cargosAssinantes = new ArrayList<>();
        this.historicoAcessos = new ArrayList<>();
    }

    @Override
    public String getTipoDocumento() {
        return "CONTRATO";
    }

    // ─── Imprimivel ─────────────────────────────────────────
    @Override
    public void imprimir() {
        System.out.println(formatarCabecalho());
        System.out.println(conteudo);
        System.out.println("─── Assinaturas ───");
        if (assinantes.isEmpty()) {
            System.out.println("  (nenhuma assinatura registrada)");
        } else {
            for (int i = 0; i < assinantes.size(); i++) {
                System.out.println("  ✍ " + assinantes.get(i)
                    + " (" + cargosAssinantes.get(i) + ")");
            }
        }
        System.out.println("═══════════════════════════════════════════\n");
    }

    @Override
    public int estimarPaginas() {
        // estimativa simplificada: 1 página a cada ~1500 caracteres
        return Math.max(1, conteudo.length() / 1500 + 1);
    }

    // ─── Assinavel ──────────────────────────────────────────
    @Override
    public void assinar(String nomeResponsavel, String cargoResponsavel) {
        assinantes.add(nomeResponsavel);
        cargosAssinantes.add(cargoResponsavel);
        System.out.println("  ✍ " + nomeResponsavel + " assinou " + id);
    }

    @Override
    public boolean estaCompletamenteAssinado() {
        return assinantes.size() >= ASSINATURAS_NECESSARIAS;
    }

    @Override
    public List<String> getAssinantes() {
        return new ArrayList<>(assinantes);
    }

    // ─── Auditavel ──────────────────────────────────────────
    @Override
    public void registrarAcesso(String usuario) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String registro = usuario + " — " + LocalDateTime.now().format(fmt);
        historicoAcessos.add(registro);
    }

    @Override
    public List<String> getHistoricoAcessos() {
        return new ArrayList<>(historicoAcessos);
    }

    @Override
    public int totalAcessos() {
        return historicoAcessos.size();
    }
}
```

**Discussão importante com os alunos:**

1. **Note a declaração da classe:**
   ```java
   public class Contrato extends Documento
                         implements Imprimivel, Assinavel, Auditavel
   ```
   - **Uma** classe (`extends Documento`)
   - **Três** interfaces (separadas por vírgula em `implements`)
   - Java não permite herdar 2 classes, mas permite implementar **quantas interfaces quiser**.

2. **O estado relacionado a cada interface fica na classe**, não na interface:
   - `assinantes` e `cargosAssinantes` → existem por causa de `Assinavel`
   - `historicoAcessos` → existe por causa de `Auditavel`
   - As interfaces apenas **exigem o comportamento**; a classe **provê o estado** necessário.

3. **Os métodos da interface estão agrupados com comentários** (`─── Imprimivel ───`, etc.). Isso é só organização visual, mas ajuda muito a leitura.

4. **`estaCompletamenteAssinado()`** usa a regra **específica do contrato** (2 assinaturas). Outra classe que implementasse `Assinavel` poderia ter regra diferente.

---

## Passo 6 — Implementar `RelatorioFinanceiro` (2 interfaces)

Relatório financeiro é **impresso** e **auditado**, mas **não precisa ser assinado** (é gerado automaticamente pelo sistema).

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioFinanceiro extends Documento
                                 implements Imprimivel, Auditavel {

    private String periodo;    // ex: "2026-Q1"
    private double valorTotal;

    // Estado próprio para Auditavel
    private List<String> historicoAcessos;

    public RelatorioFinanceiro(String id, String titulo, String autor, String conteudo,
                               String periodo, double valorTotal) {
        super(id, titulo, autor, conteudo);
        this.periodo = periodo;
        this.valorTotal = valorTotal;
        this.historicoAcessos = new ArrayList<>();
    }

    @Override
    public String getTipoDocumento() {
        return "RELATÓRIO FINANCEIRO";
    }

    public String getPeriodo()  { return periodo; }
    public double getValorTotal() { return valorTotal; }

    // ─── Imprimivel ─────────────────────────────────────────
    @Override
    public void imprimir() {
        System.out.println(formatarCabecalho());
        System.out.println("Período: " + periodo);
        System.out.printf("Valor total: R$ %.2f%n", valorTotal);
        System.out.println("─── Resumo ───");
        System.out.println(conteudo);
        System.out.println("═══════════════════════════════════════════\n");
    }

    @Override
    public int estimarPaginas() {
        return Math.max(2, conteudo.length() / 1500 + 1);
    }

    // ─── Auditavel ──────────────────────────────────────────
    @Override
    public void registrarAcesso(String usuario) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        historicoAcessos.add(usuario + " — " + LocalDateTime.now().format(fmt));
    }

    @Override
    public List<String> getHistoricoAcessos() {
        return new ArrayList<>(historicoAcessos);
    }

    @Override
    public int totalAcessos() {
        return historicoAcessos.size();
    }
}
```

**Discussão:**
- `RelatorioFinanceiro` **não implementa** `Assinavel` — porque relatórios automáticos não são assinados.
- Tentar chamar `.assinar(...)` num `RelatorioFinanceiro` seria um **erro de compilação** — exatamente o comportamento desejado.
- Note como o **estado** auditável (`historicoAcessos`) é repetido aqui. Isso é uma limitação clássica das interfaces: **cada classe gerencia seu próprio estado**. Em sistemas reais, padrões como **composição** podem reduzir isso (assunto para aulas futuras).

---

## Passo 7 — Implementar `EmailInterno` (1 interface)

E-mail interno é o caso mais simples — **só é imprimível**. Não pede assinatura nem registra auditoria (ele próprio já é um registro).

```java
public class EmailInterno extends Documento implements Imprimivel {

    private String destinatario;
    private String assunto;

    public EmailInterno(String id, String titulo, String autor, String conteudo,
                        String destinatario, String assunto) {
        super(id, titulo, autor, conteudo);
        this.destinatario = destinatario;
        this.assunto = assunto;
    }

    @Override
    public String getTipoDocumento() {
        return "E-MAIL INTERNO";
    }

    public String getDestinatario() { return destinatario; }
    public String getAssunto()       { return assunto; }

    // ─── Imprimivel ─────────────────────────────────────────
    @Override
    public void imprimir() {
        System.out.println(formatarCabecalho());
        System.out.println("Para: " + destinatario);
        System.out.println("Assunto: " + assunto);
        System.out.println("─── Mensagem ───");
        System.out.println(conteudo);
        System.out.println("═══════════════════════════════════════════\n");
    }

    @Override
    public int estimarPaginas() {
        // e-mails internos costumam caber em 1 página
        return 1;
    }
}
```

**Discussão:**
- Apenas **uma** interface (`implements Imprimivel`) — sem vírgula.
- Não tem `assinantes` nem `historicoAcessos` porque **não implementa** `Assinavel` nem `Auditavel`.
- Esse é o **Princípio da Segregação de Interfaces** (ISP do SOLID) em ação: **classes não devem ser forçadas a implementar interfaces que não usam**.

---

## Passo 8 — Classe Principal com Polimorfismo por Capacidade

Não precisamos de uma classe gerenciadora extra. O `main` mantém listas tipadas pelas **interfaces** — e cada lista naturalmente filtra **quem tem aquela capacidade**.

```java
import java.util.ArrayList;
import java.util.List;

public class SistemaDocumentos {
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE DOCUMENTOS - TechBR                  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        // ─────────────────────────────────────────────────────
        //  CRIAR DOCUMENTOS DE TIPOS DIFERENTES
        // ─────────────────────────────────────────────────────
        Contrato c1 = new Contrato(
            "DOC001",
            "Contrato de Prestação de Serviços",
            "Departamento Jurídico",
            "A CONTRATADA prestará serviços de consultoria pelo período de 12 meses...");

        RelatorioFinanceiro r1 = new RelatorioFinanceiro(
            "DOC002",
            "Fechamento do 1º Trimestre",
            "Sistema Automático",
            "Receita líquida de R$ 1,2 milhão no trimestre. Crescimento de 15%...",
            "2026-Q1",
            1_200_000.00);

        EmailInterno e1 = new EmailInterno(
            "DOC003",
            "Reunião na sexta",
            "ana.silva@techbr",
            "Pessoal, lembrete da reunião sexta às 14h na sala 3.",
            "todos@techbr",
            "Reunião sexta 14h");

        Contrato c2 = new Contrato(
            "DOC004",
            "Acordo de Confidencialidade",
            "Departamento Jurídico",
            "As partes comprometem-se a manter sigilo absoluto sobre informações...");

        // ─────────────────────────────────────────────────────
        //  USAR CAPACIDADES — POLIMORFISMO POR INTERFACE
        // ─────────────────────────────────────────────────────

        // ★ Lista tipada por Imprimivel — aceita TODOS os 4 documentos ★
        System.out.println("=== TODOS OS DOCUMENTOS SÃO IMPRIMÍVEIS ===\n");
        List<Imprimivel> paraImprimir = new ArrayList<>();
        paraImprimir.add(c1);
        paraImprimir.add(r1);
        paraImprimir.add(e1);
        paraImprimir.add(c2);

        int totalPaginas = 0;
        for (Imprimivel imp : paraImprimir) {
            imp.imprimir();
            totalPaginas += imp.estimarPaginas();
        }
        System.out.println("→ Fila de impressão: " + paraImprimir.size()
            + " documentos, ~" + totalPaginas + " páginas\n");

        // ★ Lista tipada por Assinavel — só Contratos cabem ★
        System.out.println("=== SOMENTE OS QUE PRECISAM DE ASSINATURA ===\n");
        List<Assinavel> paraAssinar = new ArrayList<>();
        paraAssinar.add(c1);
        paraAssinar.add(c2);
        // r1 e e1 NÃO podem entrar aqui — erro de compilação

        for (Assinavel doc : paraAssinar) {
            doc.assinar("João Pereira", "Diretor Jurídico");
            doc.assinar("Maria Souza", "Diretora Financeira");

            System.out.println("  → Completamente assinado? "
                + doc.estaCompletamenteAssinado());
            System.out.println("  → Assinantes: " + doc.getAssinantes() + "\n");
        }

        // ★ Lista tipada por Auditavel — Contratos e Relatórios ★
        System.out.println("=== REGISTRO DE AUDITORIA (CONTRATOS + RELATÓRIOS) ===\n");
        List<Auditavel> auditaveis = new ArrayList<>();
        auditaveis.add(c1);
        auditaveis.add(r1);
        auditaveis.add(c2);
        // e1 NÃO cabe aqui

        // Simula acessos
        c1.registrarAcesso("ana.silva@techbr");
        c1.registrarAcesso("joao.pereira@techbr");
        c1.registrarAcesso("auditor.externo@techbr");
        r1.registrarAcesso("carla.contadora@techbr");
        r1.registrarAcesso("ana.silva@techbr");
        c2.registrarAcesso("joao.pereira@techbr");

        for (Auditavel a : auditaveis) {
            // Como a referência é Auditavel, precisa de cast para ver o ID;
            // ou então um cast para Documento (que é classe abstrata comum).
            Documento d = (Documento) a;
            System.out.println("  Documento: " + d.getId()
                + " (" + d.getTipoDocumento() + ")");
            System.out.println("  Total de acessos: " + a.totalAcessos());
            for (String registro : a.getHistoricoAcessos()) {
                System.out.println("    - " + registro);
            }
            System.out.println();
        }

        // ─────────────────────────────────────────────────────
        //  FILTRO POR CAPACIDADE (instanceof direto na lista mista)
        // ─────────────────────────────────────────────────────
        System.out.println("=== FILTRO POR CAPACIDADE (LISTA MISTA) ===\n");

        // Lista heterogênea — tipo Documento
        List<Documento> todosDocumentos = new ArrayList<>();
        todosDocumentos.add(c1);
        todosDocumentos.add(r1);
        todosDocumentos.add(e1);
        todosDocumentos.add(c2);

        // Quem é Assinavel?
        System.out.println("Documentos que podem ser assinados:");
        for (Documento d : todosDocumentos) {
            if (d instanceof Assinavel) {
                System.out.println("  - " + d.getId() + " ("
                    + d.getTipoDocumento() + ")");
            }
        }

        // Quem é Auditavel?
        System.out.println("\nDocumentos com auditoria:");
        for (Documento d : todosDocumentos) {
            if (d instanceof Auditavel a) {     // pattern matching (Java 16+)
                System.out.println("  - " + d.getId() + " ("
                    + d.getTipoDocumento() + ") — "
                    + a.totalAcessos() + " acessos");
            }
        }
    }
}
```

**Discussão importante com os alunos:**

1. **Note o triplo polimorfismo do `Contrato`:**
   - Está em `paraImprimir` (como `Imprimivel`)
   - Está em `paraAssinar` (como `Assinavel`)
   - Está em `auditaveis` (como `Auditavel`)
   - **O mesmo objeto** aparece em listas tipadas por **interfaces diferentes**.

2. **Listas tipadas pela interface são o coração do polimorfismo por capacidade:**
   - `List<Imprimivel>` aceita **qualquer** coisa imprimível
   - `List<Assinavel>` aceita **somente** quem precisa de assinatura
   - O **compilador filtra** automaticamente para você

3. **`instanceof` em listas heterogêneas:**
   - Quando você tem uma lista `List<Documento>` (tipo abstrato comum) e quer descobrir "quem é o quê", `instanceof` é a ferramenta natural.
   - O **pattern matching** (`instanceof Auditavel a`) declara e converte em uma única linha.

4. **Tentar adicionar um `EmailInterno` em `paraAssinar` dá erro de compilação:**
   - `EmailInterno` **não implementa** `Assinavel`.
   - O Java te protege em **tempo de compilação** — bug evitado.

---

## Saída Esperada (parcial — varia com data/hora real)

```
╔═══════════════════════════════════════════════════╗
║   SISTEMA DE DOCUMENTOS - TechBR                  ║
╚═══════════════════════════════════════════════════╝

=== TODOS OS DOCUMENTOS SÃO IMPRIMÍVEIS ===

═══════════════════════════════════════════
  CONTRATO — Contrato de Prestação de Serviços
  ID: DOC001 | Autor: Departamento Jurídico
  Criado em: 09/05/2026 14:35
═══════════════════════════════════════════
A CONTRATADA prestará serviços de consultoria pelo período de 12 meses...
─── Assinaturas ───
  (nenhuma assinatura registrada)
═══════════════════════════════════════════

═══════════════════════════════════════════
  RELATÓRIO FINANCEIRO — Fechamento do 1º Trimestre
  ID: DOC002 | Autor: Sistema Automático
  Criado em: 09/05/2026 14:35
═══════════════════════════════════════════
Período: 2026-Q1
Valor total: R$ 1200000,00
─── Resumo ───
Receita líquida de R$ 1,2 milhão no trimestre. Crescimento de 15%...
═══════════════════════════════════════════

═══════════════════════════════════════════
  E-MAIL INTERNO — Reunião na sexta
  ID: DOC003 | Autor: ana.silva@techbr
  Criado em: 09/05/2026 14:35
═══════════════════════════════════════════
Para: todos@techbr
Assunto: Reunião sexta 14h
─── Mensagem ───
Pessoal, lembrete da reunião sexta às 14h na sala 3.
═══════════════════════════════════════════

... (DOC004 também impresso) ...

→ Fila de impressão: 4 documentos, ~6 páginas

=== SOMENTE OS QUE PRECISAM DE ASSINATURA ===

  ✍ João Pereira assinou DOC001
  ✍ Maria Souza assinou DOC001
  → Completamente assinado? true
  → Assinantes: [João Pereira, Maria Souza]

  ✍ João Pereira assinou DOC004
  ✍ Maria Souza assinou DOC004
  → Completamente assinado? true
  → Assinantes: [João Pereira, Maria Souza]

=== REGISTRO DE AUDITORIA (CONTRATOS + RELATÓRIOS) ===

  Documento: DOC001 (CONTRATO)
  Total de acessos: 3
    - ana.silva@techbr — 09/05/2026 14:35:22
    - joao.pereira@techbr — 09/05/2026 14:35:22
    - auditor.externo@techbr — 09/05/2026 14:35:22

  Documento: DOC002 (RELATÓRIO FINANCEIRO)
  Total de acessos: 2
    - carla.contadora@techbr — 09/05/2026 14:35:22
    - ana.silva@techbr — 09/05/2026 14:35:22

  Documento: DOC004 (CONTRATO)
  Total de acessos: 1
    - joao.pereira@techbr — 09/05/2026 14:35:22

=== FILTRO POR CAPACIDADE (LISTA MISTA) ===

Documentos que podem ser assinados:
  - DOC001 (CONTRATO)
  - DOC004 (CONTRATO)

Documentos com auditoria:
  - DOC001 (CONTRATO) — 3 acessos
  - DOC002 (RELATÓRIO FINANCEIRO) — 2 acessos
  - DOC004 (CONTRATO) — 1 acessos
```

---

## Passo 9 — Adicionando NOVA Capacidade (a magia das interfaces)

Imagine que o setor jurídico pede um novo recurso: alguns documentos precisam ser **arquivados** ao final do ciclo. Vamos adicionar uma nova interface `Arquivavel` **sem alterar nenhuma classe existente**:

```java
public interface Arquivavel {
    void arquivar(String motivo);
    boolean estaArquivado();
    String getMotivoArquivamento();
}
```

E agora **apenas** o `Contrato` (que tem ciclo de vida longo) implementa essa nova interface:

```java
public class Contrato extends Documento
                      implements Imprimivel, Assinavel, Auditavel, Arquivavel {

    // ... tudo o que já tinha ...

    private boolean arquivado = false;
    private String motivoArquivamento = "";

    @Override
    public void arquivar(String motivo) {
        this.arquivado = true;
        this.motivoArquivamento = motivo;
        System.out.println("  📦 " + id + " arquivado: " + motivo);
    }

    @Override
    public boolean estaArquivado() {
        return arquivado;
    }

    @Override
    public String getMotivoArquivamento() {
        return motivoArquivamento;
    }
}
```

**Use no `main` sem alterar nada das outras classes:**

```java
c1.arquivar("Contrato cumprido integralmente em 09/05/2026");

System.out.println(c1.estaArquivado());          // true
System.out.println(c1.getMotivoArquivamento());  // "Contrato cumprido..."
```

**Discussão crítica:**
- ✅ Não tocamos em `Documento`
- ✅ Não tocamos em `Imprimivel`, `Assinavel` ou `Auditavel`
- ✅ Não tocamos em `RelatorioFinanceiro` nem `EmailInterno`
- ✅ Apenas **adicionamos** uma nova interface e **fizemos uma classe implementá-la**

> 💎 **Esse é o Princípio Aberto/Fechado em ação — agora via interfaces.** O sistema cresce **por adição**, não por modificação. E adicionar uma capacidade não custa nada às classes que **não precisam** dela.

---

## Discussão Final com os Alunos

### Perguntas para reflexão:

1. **Por que `Documento` é classe abstrata e `Imprimivel`/`Assinavel`/`Auditavel` são interfaces?**
   - `Documento` tem **estado comum** (id, título, autor, conteúdo, data) — só classe pode ter atributos de instância
   - As capacidades **não têm estado próprio** que precise ser compartilhado em comum entre as classes — cada classe gerencia seu próprio estado relacionado
   - Capacidades são **transversais** — qualquer combinação faz sentido, sem hierarquia única

2. **Por que `EmailInterno` não implementa `Auditavel`?**
   - Conceitualmente: e-mails já são, eles próprios, registros — não precisam de "log de quem leu"
   - Tecnicamente: se implementasse, teria que fornecer **estado** + **implementação** de auditoria, sem real utilidade
   - **Princípio da Segregação de Interfaces:** não force uma classe a depender de coisas que ela não usa

3. **Onde está o polimorfismo neste sistema?**
   - Em `List<Imprimivel>` aceitando Contrato, Relatório e E-mail
   - Em `List<Assinavel>` aceitando só Contratos
   - Em `List<Auditavel>` aceitando Contratos e Relatórios
   - Em filtros com `instanceof Assinavel` numa lista mista de `Documento`
   - **O mesmo objeto** (Contrato) aparece em 3 listas diferentes, **por 3 contratos diferentes**

4. **Por que isso seria pior só com herança?**
   - Java tem **herança simples** — não daria pra "estender Imprimivel, Assinavel, Auditavel" ao mesmo tempo
   - Subclasses para cada combinação explodiriam (DocImprAssAud, DocImprAud, DocImpr...)
   - As capacidades não têm uma **ordem natural** em uma árvore — são transversais

5. **Por que repetimos estado de auditoria em Contrato e RelatorioFinanceiro?**
   - É a limitação clássica de interfaces: elas **não têm estado**
   - Em sistemas reais, padrões como **composição** ou classes auxiliares podem reduzir essa repetição
   - É um trade-off **conhecido e aceito** — a flexibilidade compensa

6. **Como adicionar um novo tipo de documento (ex: `NotaFiscal`)?**
   - Criar `class NotaFiscal extends Documento implements Imprimivel, Auditavel { ... }`
   - **Nenhuma outra classe** precisa ser tocada
   - Listas existentes (`List<Imprimivel>`, `List<Auditavel>`) **automaticamente** aceitam o novo tipo

---

## Atividades de Extensão (Opcional)

Se houver tempo, proponha:

1. **Adicionar uma quarta classe:**
   - `NotaFiscal extends Documento implements Imprimivel, Auditavel`
   - Atributos extras: número da nota, CNPJ do emitente, valor
   - `imprimir()` mostra os campos fiscais formatados
   - Mostre que ela **automaticamente** entra em `List<Imprimivel>` e `List<Auditavel>` no main, **sem modificar** o main

2. **Default method em `Imprimivel`:**
   - Adicionar método `default` chamado `imprimirResumo()` que mostra só o cabeçalho
   - Mostre que classes existentes **não precisam ser alteradas** para ganhar o novo método

3. **Interface `Comparavel` simples:**
   - Crie `Comparavel<T>` com método `int comparar(T outro)`
   - Faça `Contrato` implementar comparando por número de assinantes
   - Liste contratos ordenados (use loop com troca, sem `Collections.sort` para manter simples)

4. **Composição em vez de herança:**
   - Crie uma classe auxiliar `RegistroAuditoria` que encapsula a lista de acessos
   - Em vez de cada `Auditavel` reimplementar tudo, ela delega para um `RegistroAuditoria` interno
   - Discuta os trade-offs com a turma

---

## Resumo do Bloco 2

Neste bloco você:

✅ Implementou um sistema com **3 interfaces + 1 classe abstrata + 3 classes concretas**
✅ Combinou `extends` (classe abstrata) com `implements` (múltiplas interfaces) na mesma classe
✅ Usou **polimorfismo por capacidade**: listas tipadas por interface
✅ Aplicou o **Princípio da Segregação de Interfaces** (cada classe só implementa o que usa)
✅ Demonstrou que o **mesmo objeto** vive em listas tipadas por interfaces diferentes
✅ Adicionou nova capacidade (`Arquivavel`) **sem modificar** código existente
✅ Aplicou o **Princípio Aberto/Fechado** via interfaces

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## Próximo Passo

No **Bloco 3**, você vai **desenvolver sozinho** um sistema diferente: **Devices IoT** com 3 interfaces (`Conectavel`, `Controlavel`, `Atualizavel`) e classes que combinam essas capacidades de formas diferentes.

[➡️ Ir para Bloco 3](../Bloco3/README.md)
