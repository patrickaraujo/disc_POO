# Bloco 3 — Exercício Autônomo: Sistema de Importação de Dados

## Objetivos do Bloco

- Aplicar classes abstratas e Template Method de forma **independente**
- Tomar decisões de design sem orientação direta
- Implementar **hierarquia abstrata + métodos abstratos + concretos** em conjunto
- Validar compreensão dos conceitos da Aula 09
- Apresentar e discutir diferentes soluções

---

## 🎯 Problema: ETL Multi-formato

Você foi contratado para desenvolver o **módulo de importação de dados** de um sistema de BI. Esse módulo precisa receber arquivos de **vários formatos diferentes** e converter os dados para um formato canônico antes de salvar no banco.

### Formatos Suportados

1. **CSV** (Comma-Separated Values)
   - Texto simples com colunas separadas por `;`
   - Primeira linha é o cabeçalho
   - Encoding: UTF-8

2. **JSON** (JavaScript Object Notation)
   - Estrutura hierárquica com `{}` e `[]`
   - Pode ter dados aninhados (ex: endereço dentro de cliente)

3. **XML** (eXtensible Markup Language)
   - Estrutura com tags `<elemento>`
   - Mais verboso que JSON
   - Pode ter atributos nas tags

4. **Excel** (formato XLSX)
   - Arquivo binário
   - Pode ter várias **planilhas** (sheets)
   - Cada célula tem tipo (número, texto, data)

### Características Comuns (toda importação)

Toda importação tem:
- Identificador único (ID)
- Nome/caminho do arquivo
- Data e hora de início
- Quantidade de registros lidos
- Quantidade de registros válidos
- Quantidade de registros rejeitados
- Status (PENDENTE, EM_ANDAMENTO, CONCLUIDO, FALHOU)
- Lista de erros encontrados

### Fluxo de Importação (igual para todos — Template Method!)

Independente do formato, **toda importação segue este fluxo**:

```
1. ABRIR arquivo
2. LER conteúdo
3. PARSEAR (converter texto/binário em estrutura)
4. VALIDAR registros (descartar inválidos)
5. TRANSFORMAR para o formato canônico (Map<String, Object>)
6. SALVAR no banco (passo comum)
7. FECHAR arquivo (passo comum)
8. GERAR relatório (passo comum)
```

> Se o passo 1, 2 ou 3 falhar, a importação é abortada (não tem como continuar). Os passos 4–8 sempre são executados, mesmo que registros sejam rejeitados na validação.

### Regras Específicas por Formato

- **CSV:** primeira linha é cabeçalho. Separador é `;`. Linhas começando com `#` são comentários e devem ser ignoradas.
- **JSON:** o conteúdo é um array de objetos. Cada objeto vira um registro.
- **XML:** existe um elemento raiz `<dados>` contendo elementos `<registro>` repetidos.
- **Excel:** apenas a **primeira planilha** é processada. Primeira linha é cabeçalho.

### Validações Comuns (todos)

- Registros precisam ter pelo menos os campos: `id`, `nome`, `valor`
- `id` precisa ser numérico
- `valor` precisa ser numérico positivo
- Nenhum campo obrigatório pode estar vazio

> Para esta atividade, você pode **simular** a leitura dos arquivos (não precisa realmente abrir CSV/JSON/XML/Excel reais). O foco é a **estrutura abstrata + Template Method**, não as bibliotecas.

---

## Planejamento Necessário (FAÇA ANTES DE CODIFICAR!)

### Passo 1: Desenhe a hierarquia no papel

```
┌─────────────────────────────────────────┐
│   « abstract » ImportadorDados          │  ← Que atributos comuns?
│         (SUPERCLASSE)                   │  ← Que métodos abstratos?
│                                         │  ← Que métodos concretos?
└────────────────┬────────────────────────┘
                 │
   ┌─────────────┼──────────────┬──────────────────┐
   │             │              │                  │
┌──▼─────────┐ ┌─▼────────────┐ ┌▼─────────────┐ ┌─▼─────────────┐
│ImportadorCSV│ │ImportadorJSON│ │ImportadorXML │ │ImportadorExcel│
│             │ │              │ │              │ │               │
└─────────────┘ └──────────────┘ └──────────────┘ └───────────────┘
```

**Perguntas para reflexão:**
- O que TODAS as importações têm em comum?
- O que é específico de cada formato?
- Quais métodos devem ser **abstratos** (variação)?
- Quais métodos podem ser **concretos** (comum)?
- Onde o **Template Method** vai aparecer?

### Passo 2: Identifique os métodos do Template Method

Marque com **A** (abstrato) ou **C** (concreto) cada passo:

- [ ] `abrirArquivo()`         — A ou C?
- [ ] `lerConteudo()`          — A ou C?
- [ ] `parsear()`              — A ou C?
- [ ] `validar(registros)`     — A ou C?
- [ ] `transformar(registros)` — A ou C?
- [ ] `salvarNoBanco(canonico)` — A ou C?
- [ ] `fecharArquivo()`        — A ou C?
- [ ] `gerarRelatorio()`       — A ou C?

> 💡 **Dica:** o que **varia por formato** deve ser **abstrato**. O que é **igual para todos** deve ser **concreto**.

### Passo 3: Decida os modificadores de acesso

- O Template Method (`importar()`) deve ser `public final`?
- Atributos comuns: `protected`?
- Métodos abstratos: `protected` (pois só o Template Method da abstrata os chama)?
- Métodos concretos auxiliares: `protected`?

---

## Requisitos Detalhados

### Enum `StatusImportacao`

```java
public enum StatusImportacao {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDO,
    FALHOU
}
```

---

### Classe Abstrata `ImportadorDados`

**Atributos:**
```java
- String id
- String caminhoArquivo
- LocalDateTime inicio
- LocalDateTime fim
- int totalLido
- int totalValidos
- int totalRejeitados
- StatusImportacao status
- List<String> erros               // mensagens de erro
- List<Map<String, Object>> dadosCanonicos  // dados transformados
```

**Construtor:**
```java
public ImportadorDados(String id, String caminhoArquivo)
```

- Inicializa atributos
- `status = PENDENTE`
- `totalLido = 0`, `totalValidos = 0`, `totalRejeitados = 0`
- `erros = new ArrayList<>()`
- `dadosCanonicos = new ArrayList<>()`

**TEMPLATE METHOD (final, concreto):**

```java
public final void importar() {
    inicio = LocalDateTime.now();
    status = StatusImportacao.EM_ANDAMENTO;

    try {
        abrirArquivo();
        String conteudo = lerConteudo();
        List<Map<String, String>> registrosBrutos = parsear(conteudo);
        totalLido = registrosBrutos.size();

        List<Map<String, String>> validos = validar(registrosBrutos);
        totalValidos = validos.size();
        totalRejeitados = totalLido - totalValidos;

        dadosCanonicos = transformar(validos);
        salvarNoBanco();
        fecharArquivo();

        status = StatusImportacao.CONCLUIDO;
    } catch (Exception e) {
        erros.add("Erro fatal: " + e.getMessage());
        status = StatusImportacao.FALHOU;
    }

    fim = LocalDateTime.now();
    gerarRelatorio();
}
```

**Métodos abstratos (cada formato implementa):**

1. `protected abstract void abrirArquivo() throws Exception`
   - Simula abrir o arquivo
   - Pode lançar exceção se arquivo não existir

2. `protected abstract String lerConteudo() throws Exception`
   - Retorna o conteúdo bruto do arquivo
   - Para esta atividade, retorne uma string simulada

3. `protected abstract List<Map<String, String>> parsear(String conteudo) throws Exception`
   - Converte o conteúdo bruto em uma lista de registros
   - Cada registro é um `Map<String, String>` (nome do campo → valor)

4. `protected abstract List<Map<String, Object>> transformar(List<Map<String, String>> validos)`
   - Converte os registros validados para o formato canônico
   - O canônico tem `Object` em vez de `String` (id como Integer, valor como Double)

5. `public abstract String getFormato()`
   - Retorna nome amigável: `"CSV"`, `"JSON"`, `"XML"`, `"Excel"`

**Métodos concretos (comum a todos):**

6. `protected List<Map<String, String>> validar(List<Map<String, String>> registros)`
   - Aplica as regras de validação comuns
   - Para cada registro inválido, adiciona uma mensagem em `erros`
   - Retorna apenas os registros válidos

7. `protected void salvarNoBanco()`
   - Simula a persistência (apenas imprime)
   - Mensagem: `"💾 Salvando X registros no banco..."`

8. `protected void fecharArquivo()`
   - Simula o fechamento (apenas imprime)

9. `protected void gerarRelatorio()`
   - Imprime um relatório com:
     - ID, formato, arquivo
     - Início, fim, duração
     - Total lido, válidos, rejeitados
     - Status
     - Lista de erros (se houver)

10. Getters apropriados

---

### Classe `ImportadorCSV extends ImportadorDados`

**Atributos específicos:**
```java
- String separador  // ";"
```

**Construtor:**
```java
public ImportadorCSV(String id, String caminhoArquivo)
```

- Define `separador = ";"`

**Implementação dos abstratos:**

1. `abrirArquivo()`: imprime `"📂 Abrindo CSV: <arquivo>"`. Se nome contém `"erro"`, lança `Exception("Arquivo não encontrado")`.

2. `lerConteudo()`: retorna uma string CSV simulada como:

```
# Comentário ignorado
id;nome;valor
1;Produto A;100
2;Produto B;200
3;;-50
4;Produto D;abc
5;Produto E;300
```

3. `parsear(conteudo)`:
   - Divide por `\n`
   - Ignora linhas que começam com `#`
   - Primeira linha não-comentário é o cabeçalho
   - Demais linhas são registros (cada coluna pelo separador)
   - Retorna `List<Map<String, String>>` com chave = nome da coluna

4. `transformar(validos)`:
   - Para cada `Map<String, String>`, gera um `Map<String, Object>` com:
     - `id` como `Integer`
     - `nome` como `String`
     - `valor` como `Double`

5. `getFormato()`: retorna `"CSV"`

---

### Classe `ImportadorJSON extends ImportadorDados`

**Atributos específicos:**
```java
- String encoding  // "UTF-8"
```

**Implementação dos abstratos:**

1. `abrirArquivo()`: imprime `"📂 Abrindo JSON: <arquivo>"`.

2. `lerConteudo()`: retorna string JSON **simulada** como:

```json
[
  {"id": "1", "nome": "Produto X", "valor": "150"},
  {"id": "2", "nome": "Produto Y", "valor": "250"},
  {"id": "abc", "nome": "Produto Z", "valor": "350"}
]
```

3. `parsear(conteudo)`:
   - Para esta atividade, você pode fazer um **parser muito simples** (ou usar `String.split` e regex)
   - Para cada objeto JSON, crie um `Map<String, String>`

> 💡 **Simplificação aceitável:** em vez de parsear JSON real, retorne uma lista pré-construída no método. O foco da aula é o Template Method, não JSON parsing.

4. `transformar(validos)`: igual ao CSV.

5. `getFormato()`: retorna `"JSON"`.

---

### Classe `ImportadorXML extends ImportadorDados`

**Atributos específicos:**
```java
- String elementoRaiz  // "dados"
- String elementoRegistro  // "registro"
```

**Implementação dos abstratos:**

1. `abrirArquivo()`: imprime `"📂 Abrindo XML: <arquivo>"`.

2. `lerConteudo()`: retorna XML simulado:

```xml
<dados>
  <registro>
    <id>1</id>
    <nome>Item A</nome>
    <valor>500</valor>
  </registro>
  <registro>
    <id>2</id>
    <nome>Item B</nome>
    <valor>0</valor>
  </registro>
</dados>
```

3. `parsear(conteudo)`: para cada `<registro>`, extrai os filhos e cria um Map.

4. `transformar`: igual aos outros.

5. `getFormato()`: retorna `"XML"`.

---

### Classe `ImportadorExcel extends ImportadorDados`

**Atributos específicos:**
```java
- String planilha  // "Sheet1"
- int linhaInicial  // 2 (linha 1 é cabeçalho)
```

**Implementação dos abstratos:**

1. `abrirArquivo()`: imprime `"📂 Abrindo Excel: <arquivo> (planilha: <planilha>)"`.

2. `lerConteudo()`: retorna string que representa as células da planilha (use formato CSV interno como simulação, separado por `|`).

3. `parsear(conteudo)`: similar ao CSV mas com `|`.

4. **Método sobrescrito (concreto):** `validar()` — adicionalmente, descarta linhas onde **todas** as células estão vazias (típico de Excel com linhas em branco no fim). Para isso, **chame `super.validar()`** primeiro e depois aplique a regra extra.

5. `transformar`: igual aos outros.

6. `getFormato()`: retorna `"Excel"`.

> ⚠️ **Atenção:** sobrescrever um método **concreto** da abstrata é diferente de implementar um abstrato. Use `@Override` em ambos.

---

### Classe `GerenciadorImportacoes`

**Atributos:**
```java
- List<ImportadorDados> historico
- String nomeProjeto
```

**Métodos:**

1. `public void executar(ImportadorDados importador)`
   - Adiciona ao histórico
   - Chama `importador.importar()`

2. `public void exibirEstatisticas()`
   - Total de importações
   - Total concluídas, falhas, em andamento
   - Total geral de registros lidos, válidos, rejeitados
   - Taxa de sucesso (%)

3. `public List<ImportadorDados> filtrarPorFormato(Class<?> tipo)`
   - Retorna importações do tipo especificado

4. `public List<ImportadorDados> filtrarPorStatus(StatusImportacao status)`
   - Retorna importações com o status indicado

5. `public ImportadorDados maisLento()`
   - Retorna a importação que demorou mais (maior diferença `fim - inicio`)

---

### Classe `SistemaImportacao` (Main)

**Requisitos do teste:**

1. Criar um `GerenciadorImportacoes`
2. Executar pelo menos:
   - 2 importações CSV (uma com sucesso, uma que falha por arquivo "erro" no nome)
   - 1 importação JSON
   - 1 importação XML
   - 1 importação Excel

3. Tentar instanciar `ImportadorDados` direto (deixar comentado para mostrar o erro)
4. Exibir estatísticas
5. Filtrar e exibir apenas importações de um formato (ex: CSV)
6. Mostrar a importação mais lenta

---

## Exemplo de Saída Esperada

```
╔═══════════════════════════════════════════════════╗
║   SISTEMA DE IMPORTAÇÃO DE DADOS - DataLab        ║
╚═══════════════════════════════════════════════════╝

>>> Executando importação IMP001 (CSV)
📂 Abrindo CSV: vendas_jan.csv
📄 Lendo conteúdo...
🔍 Parseando 5 registros...
✓ 3 registros válidos / 2 rejeitados
🔄 Transformando para formato canônico...
💾 Salvando 3 registros no banco...
✓ Arquivo fechado.

╔═══════════════════════════════════════════════════════════╗
║  RELATÓRIO DE IMPORTAÇÃO
╠═══════════════════════════════════════════════════════════╣
║  ID:           IMP001
║  Formato:      CSV
║  Arquivo:      vendas_jan.csv
║  Início:       27/04/2026 14:35:22
║  Fim:          27/04/2026 14:35:23
║  Duração:      0.752 segundos
║  Lidos:        5
║  Válidos:      3
║  Rejeitados:   2
║  Status:       CONCLUIDO
║  Erros:
║    - Linha 3: campo 'nome' vazio
║    - Linha 4: campo 'valor' não numérico (abc)
╚═══════════════════════════════════════════════════════════╝

>>> Executando importação IMP002 (CSV)
📂 Abrindo CSV: vendas_erro.csv
✗ Erro fatal: Arquivo não encontrado

╔═══════════════════════════════════════════════════════════╗
║  RELATÓRIO DE IMPORTAÇÃO
╠═══════════════════════════════════════════════════════════╣
║  ID:           IMP002
║  Formato:      CSV
║  Arquivo:      vendas_erro.csv
║  Status:       FALHOU
║  Erros:
║    - Erro fatal: Arquivo não encontrado
╚═══════════════════════════════════════════════════════════╝

>>> Executando importação IMP003 (JSON)
📂 Abrindo JSON: produtos.json
📄 Lendo conteúdo...
🔍 Parseando 3 registros...
✓ 2 registros válidos / 1 rejeitados
🔄 Transformando para formato canônico...
💾 Salvando 2 registros no banco...
✓ Arquivo fechado.
...

=== ESTATÍSTICAS ===
Total de importações: 5
  ✓ Concluídas: 4
  ✗ Falhas:    1
Total geral de registros: 18
  Válidos:    13
  Rejeitados: 5
Taxa de sucesso (importações): 80,0%

=== FILTRO: APENAS CSV ===
- IMP001 | vendas_jan.csv | CONCLUIDO
- IMP002 | vendas_erro.csv | FALHOU

=== IMPORTAÇÃO MAIS LENTA ===
IMP004 (XML) | 1.234 segundos
```

---

## Critérios de Avaliação

Seu código será avaliado por:

### Funcionalidade (30%)
- ✅ Todas as classes criadas
- ✅ Hierarquia abstrata implementada corretamente
- ✅ Métodos funcionam como especificado
- ✅ Sistema completo executa sem erros

### Uso de Classe Abstrata + Template Method (40%)
- ✅ `ImportadorDados` é declarada `abstract`
- ✅ Métodos abstratos sem corpo (terminados em `;`)
- ✅ Template Method `importar()` é `public final`
- ✅ Subclasses implementam **todos** os abstratos
- ✅ `salvarNoBanco()`, `fecharArquivo()`, `gerarRelatorio()` são concretos
- ✅ `validar()` é concreto e (no Excel) é sobrescrito chamando `super.validar()`
- ✅ `ArrayList<ImportadorDados>` usada como coleção polimórfica

### Qualidade do Código (20%)
- ✅ Nomes descritivos
- ✅ Encapsulamento adequado (private/protected/public)
- ✅ Código organizado e legível
- ✅ Tratamento de erros (try/catch no Template Method)
- ✅ Uso de `@Override` em sobrescritas

### Apresentação (10%)
- ✅ Saída formatada e profissional
- ✅ Mensagens claras
- ✅ Datas formatadas em padrão brasileiro
- ✅ Relatório bem estruturado

---

## Desafios Opcionais (Bônus)

Se terminar antes do tempo, implemente:

### 1. Hook method opcional

- Adicione método **concreto** (vazio) `antesDeImportar()` na abstrata
- Subclasses **podem** sobrescrever (mas não precisam)
- Útil para limpar cache, validar permissões, etc.

### 2. Estratégia de salvamento configurável

- Em vez de `salvarNoBanco()` fixo, permita escolher entre:
  - `salvarNoBanco()`
  - `salvarEmArquivo()`
  - `salvarEmAPI()`
- Use o atributo `Modo` (enum) e um único método `salvar()` que escolhe internamente

### 3. Novo formato: YAML

Adicione `ImportadorYAML` **sem mexer em nenhuma classe existente**:
- Mostre que a estrutura abstrata + Template Method permite essa extensão limpa

### 4. Importação em paralelo

- `GerenciadorImportacoes.executarTodos(List<ImportadorDados>)`
- Use `Thread` ou `ExecutorService`
- Cada importação roda em sua thread

### 5. Persistência do histórico

- Após cada importação, salvar `relatório.txt` com o conteúdo do relatório
- `gerarRelatorio()` na abstrata, com método auxiliar `salvarRelatorioEmArquivo()`

---

## Dicas de Implementação

### Estrutura básica do Template Method

```java
public final void importar() {
    inicio = LocalDateTime.now();
    status = StatusImportacao.EM_ANDAMENTO;

    try {
        abrirArquivo();                          // abstrato
        String conteudo = lerConteudo();         // abstrato
        var registrosBrutos = parsear(conteudo); // abstrato
        totalLido = registrosBrutos.size();

        var validos = validar(registrosBrutos);  // concreto (genérico)
        totalValidos = validos.size();
        totalRejeitados = totalLido - totalValidos;

        dadosCanonicos = transformar(validos);   // abstrato
        salvarNoBanco();                          // concreto
        fecharArquivo();                          // concreto

        status = StatusImportacao.CONCLUIDO;
    } catch (Exception e) {
        erros.add("Erro fatal: " + e.getMessage());
        status = StatusImportacao.FALHOU;
    }

    fim = LocalDateTime.now();
    gerarRelatorio();                            // concreto
}
```

### Validação genérica (concreta)

```java
protected List<Map<String, String>> validar(List<Map<String, String>> registros) {
    List<Map<String, String>> validos = new ArrayList<>();

    for (int i = 0; i < registros.size(); i++) {
        Map<String, String> r = registros.get(i);
        boolean valido = true;

        // Campos obrigatórios
        if (!r.containsKey("id") || !r.containsKey("nome") || !r.containsKey("valor")) {
            erros.add("Linha " + (i + 1) + ": campos obrigatórios ausentes");
            valido = false;
        } else {
            // id numérico
            try {
                Integer.parseInt(r.get("id"));
            } catch (NumberFormatException e) {
                erros.add("Linha " + (i + 1) + ": id não numérico (" + r.get("id") + ")");
                valido = false;
            }

            // nome não vazio
            if (r.get("nome") == null || r.get("nome").isBlank()) {
                erros.add("Linha " + (i + 1) + ": campo 'nome' vazio");
                valido = false;
            }

            // valor numérico positivo
            try {
                double v = Double.parseDouble(r.get("valor"));
                if (v <= 0) {
                    erros.add("Linha " + (i + 1) + ": valor deve ser positivo");
                    valido = false;
                }
            } catch (NumberFormatException e) {
                erros.add("Linha " + (i + 1) + ": valor não numérico (" + r.get("valor") + ")");
                valido = false;
            }
        }

        if (valido) {
            validos.add(r);
        }
    }

    return validos;
}
```

### Validação sobrescrita no Excel

```java
@Override
protected List<Map<String, String>> validar(List<Map<String, String>> registros) {
    // 1. Aplica validação genérica primeiro
    List<Map<String, String>> validos = super.validar(registros);

    // 2. Aplica regra específica do Excel (descartar linhas vazias)
    List<Map<String, String>> filtrados = new ArrayList<>();
    for (Map<String, String> r : validos) {
        boolean todoVazio = r.values().stream()
            .allMatch(v -> v == null || v.isBlank());
        if (!todoVazio) {
            filtrados.add(r);
        }
    }

    return filtrados;
}
```

### Cálculo de duração

```java
import java.time.Duration;

Duration d = Duration.between(inicio, fim);
double segundos = d.toMillis() / 1000.0;
System.out.printf("Duração: %.3f segundos%n", segundos);
```

### Filtro por tipo

```java
public List<ImportadorDados> filtrarPorFormato(Class<?> tipo) {
    List<ImportadorDados> resultado = new ArrayList<>();
    for (ImportadorDados imp : historico) {
        if (tipo.isInstance(imp)) {
            resultado.add(imp);
        }
    }
    return resultado;
}

// Uso:
var csvs = gerenciador.filtrarPorFormato(ImportadorCSV.class);
```

### Parser CSV simples

```java
@Override
protected List<Map<String, String>> parsear(String conteudo) {
    List<Map<String, String>> registros = new ArrayList<>();
    String[] linhas = conteudo.split("\n");

    String[] cabecalho = null;

    for (String linha : linhas) {
        linha = linha.trim();
        if (linha.isEmpty() || linha.startsWith("#")) continue;

        String[] campos = linha.split(separador);

        if (cabecalho == null) {
            cabecalho = campos;
        } else {
            Map<String, String> registro = new HashMap<>();
            for (int i = 0; i < cabecalho.length && i < campos.length; i++) {
                registro.put(cabecalho[i], campos[i]);
            }
            registros.add(registro);
        }
    }

    return registros;
}
```

---

## Checklist de Desenvolvimento

Use este checklist para organizar seu trabalho:

**Fase 1: Planejamento (15 min)**
- [ ] Desenhar hierarquia no papel
- [ ] Marcar cada método como Abstrato (A) ou Concreto (C)
- [ ] Esboçar o Template Method `importar()`
- [ ] Decidir tipos de retorno e modificadores

**Fase 2: Superclasse Abstrata e Enum (25 min)**
- [ ] Criar enum `StatusImportacao`
- [ ] Criar `ImportadorDados` como `abstract class`
- [ ] Implementar atributos e construtor
- [ ] Implementar Template Method `importar()` como `public final`
- [ ] Declarar métodos abstratos
- [ ] Implementar métodos concretos (validar, salvar, fechar, relatório)

**Fase 3: Subclasses Concretas (40 min)**
- [ ] `ImportadorCSV` — implementar todos os abstratos
- [ ] `ImportadorJSON` — implementar todos os abstratos
- [ ] `ImportadorXML` — implementar todos os abstratos
- [ ] `ImportadorExcel` — implementar abstratos + sobrescrever `validar()`

**Fase 4: Gerenciamento (20 min)**
- [ ] Criar `GerenciadorImportacoes`
- [ ] Implementar `executar()`, estatísticas, filtros, mais lenta

**Fase 5: Teste e Apresentação (20 min)**
- [ ] Criar `SistemaImportacao` com `main`
- [ ] Executar importações variadas
- [ ] **Tentar instanciar abstrata** (deixar comentado para mostrar o erro)
- [ ] Ajustar formatação de saída
- [ ] Preparar apresentação

---

## Apresentação das Soluções

Ao final do bloco, cada aluno (ou dupla) deve:

1. **Demonstrar o sistema funcionando** (5 min)
   - Executar e mostrar saída
   - Apontar onde está o Template Method
   - Mostrar o erro de compilação ao tentar instanciar a abstrata

2. **Responder perguntas** (3 min)
   - Por que a classe principal é abstrata?
   - Por que `importar()` é `final`?
   - Onde está o polimorfismo dentro do Template Method?
   - Como adicionaria um formato YAML?
   - Por que `validar()` é concreto, e por que o Excel sobrescreveu?

3. **Discussão coletiva** (10 min no final)
   - Comparar diferentes abordagens
   - Destacar usos criativos do Template Method
   - Discutir trade-offs (sobrescrever concretos vs criar novo abstrato)

---

## Resumo do Bloco 3

Neste bloco você:

✅ Aplicou classes abstratas e Template Method de forma **independente**
✅ Modelou um **domínio realista** (ETL de dados)
✅ Implementou **dispatch dinâmico** dentro de um Template Method
✅ Demonstrou que classes abstratas + polimorfismo são **complementares**
✅ Mostrou que **sobrescrever métodos concretos** é uma técnica válida (Excel)
✅ Aplicou o **Princípio Aberto/Fechado** com classes abstratas
✅ Apresentou e **defendeu** suas escolhas de design

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## 🎓 Conclusão da Aula 09

Parabéns! Você completou a Aula 09 e agora domina:

✅ Conceito e mecânica de **Classes Abstratas**
✅ Sintaxe de `abstract class` e `abstract method`
✅ **Mistura** de métodos abstratos + concretos
✅ **Proibição de instanciação** de classes abstratas
✅ **Obrigação** de subclasses concretas implementarem abstratos
✅ Padrão **Template Method** em sistemas reais
✅ **Sobrescrita** de métodos concretos quando necessário

### Resposta à problematização inicial

> **"Como impedir que alguém crie um `new Funcionario(...)` genérico, e como garantir que toda nova subclasse implemente `calcularSalario()`?"**

**Resposta:** Marcar `Funcionario` como `abstract class` e `calcularSalario()` como método abstrato. O compilador passa a **proibir** a instanciação direta da classe genérica e a **exigir** a implementação do método em qualquer subclasse concreta. Bugs como "salário zero por método esquecido" se tornam **impossíveis** — a verificação acontece em **tempo de compilação**.

### A arquitetura OO completa (até aqui)

```
Encapsulamento  → CONTROLA o acesso aos dados            (Aula 04)
Herança         → COMPARTILHA estrutura entre classes    (Aula 07)
Polimorfismo    → UNIFICA tratamento de tipos diferentes (Aula 08)
Abstração       → EXPRESSA conceitos genéricos no design (Aula 09) ★
```

**Refatoração sugerida (treino opcional):** volte ao Sistema de Folha de Pagamento da Aula 08 e transforme `Funcionario` em classe abstrata com `calcularSalario()` abstrato. Você verá como o código fica **mais expressivo** e **mais seguro**.

**Na próxima aula (Aula 10):** Você aprenderá **Interfaces** — como definir contratos puros, ter "herança múltipla" de comportamento e desacoplar ainda mais as classes do seu sistema! 🚀
