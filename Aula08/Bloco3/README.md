# Bloco 3 — Exercício Autônomo: Sistema de Pagamentos Polimórfico

## Objetivos do Bloco

- Aplicar polimorfismo de forma **independente** em um novo domínio
- Tomar decisões de design sem orientação direta
- Implementar **hierarquia + sobrescrita + dispatch dinâmico** em conjunto
- Validar compreensão dos conceitos da Aula 08
- Apresentar e discutir diferentes soluções

---

## 🎯 Problema: E-commerce com Múltiplas Formas de Pagamento

Você foi contratado para desenvolver o **módulo de pagamentos** de um e-commerce brasileiro. A loja precisa aceitar **quatro formas de pagamento** diferentes, e cada uma tem regras específicas.

### Formas de Pagamento Disponíveis

1. **PIX**
   - Chave PIX (CPF, e-mail ou telefone)
   - Sem taxas
   - Aprovação imediata
   - Desconto de **5%** sobre o valor (incentivo)

2. **Boleto Bancário**
   - Linha digitável (44 dígitos)
   - Data de vencimento (ex: 3 dias úteis após emissão)
   - Sem taxas para o cliente
   - **Não tem aprovação imediata** (paga até o vencimento)

3. **Cartão de Crédito**
   - Número do cartão (mascarado: `**** **** **** 1234`)
   - Bandeira (Visa, Master, Elo, Amex)
   - Quantidade de parcelas (1 a 12)
   - Taxa de **2,5%** sobre o valor (acrescentada ao total)
   - Acima de **6 parcelas:** taxa adicional de **1%** por parcela extra
   - Aprovação imediata (mas pode ser **negada**)

4. **Cartão de Débito**
   - Número do cartão (mascarado)
   - Bandeira
   - Taxa fixa de **R$ 1,50** por transação
   - Aprovação imediata
   - Sem parcelamento

### Características Comuns

Todos os pagamentos têm:
- Identificador único (UUID ou código)
- Valor original da compra
- Data e hora da transação
- Status (PENDENTE, APROVADO, NEGADO)
- Cliente (nome e CPF)

### Regras de Negócio

1. **Cálculo do valor final:**
   - **PIX:** `valor × 0,95` (5% desconto)
   - **Boleto:** `valor` (sem alteração)
   - **Cartão Crédito:** depende do número de parcelas (ver tabela abaixo)
   - **Cartão Débito:** `valor + 1,50`

2. **Tabela de taxas do cartão de crédito:**

   | Parcelas | Taxa total |
   |----------|------------|
   | 1–6      | 2,5%       |
   | 7        | 3,5%       |
   | 8        | 4,5%       |
   | 9        | 5,5%       |
   | 10       | 6,5%       |
   | 11       | 7,5%       |
   | 12       | 8,5%       |

   Fórmula: `taxaBase = 2.5%; se parcelas > 6, taxa = 2.5% + (parcelas - 6) × 1%`

3. **Processamento (`processar()`):**
   - Cada tipo simula seu processamento
   - **PIX:** sempre aprovado (status = APROVADO)
   - **Boleto:** sempre fica PENDENTE (aguarda compensação)
   - **Cartão Crédito:** aprovado em **80%** dos casos (use `Math.random()`)
   - **Cartão Débito:** aprovado em **95%** dos casos

4. **Comprovante (`gerarComprovante()`):**
   - Todos exibem dados básicos
   - Cada tipo adiciona dados específicos
   - Cartões devem mascarar o número (mostrar só os últimos 4 dígitos)

---

## Planejamento Necessário (FAÇA ANTES DE CODIFICAR!)

### Passo 1: Desenhe a hierarquia no papel

```
┌──────────────────────────────────┐
│         Pagamento                │  ← Que atributos comuns?
│       (SUPERCLASSE)              │  ← Que métodos polimórficos?
└──────────────┬───────────────────┘
               │
   ┌───────────┼─────────────┬──────────────────┐
   │           │             │                  │
┌──▼───┐  ┌────▼─────┐  ┌────▼─────────┐  ┌────▼──────────┐
│ Pix  │  │  Boleto  │  │CartaoCredito │  │ CartaoDebito  │
│      │  │          │  │              │  │               │
└──────┘  └──────────┘  └──────────────┘  └───────────────┘
```

**Perguntas para reflexão:**
- O que TODOS os pagamentos têm em comum?
- O que é específico de cada tipo?
- Quais métodos devem ser **polimórficos** (sobrescritos)?
- Quais métodos podem ser **herdados** sem alteração?
- Onde o polimorfismo vai brilhar?

### Passo 2: Identifique os métodos polimórficos

Marque com ✅ os métodos que **CADA SUBCLASSE deve sobrescrever**:

- [ ] `calcularValorFinal()` — fórmula varia por tipo
- [ ] `processar()` — cada tipo simula diferente
- [ ] `gerarComprovante()` — dados específicos diferentes
- [ ] `getDescricaoTipo()` — retorna nome amigável

### Passo 3: Decida os modificadores de acesso

- Atributos comuns: `protected`?
- Atributos específicos: `private`
- Métodos: `public` (para serem polimórficos)

### Passo 4: Planeje a coleção polimórfica

A classe `ProcessadorPagamentos` vai usar:

```java
private ArrayList<Pagamento> historico;
```

**Esta lista aceitará TODOS os tipos!**

---

## Requisitos Detalhados

### Enum `StatusPagamento`

Crie um enum para os possíveis status:

```java
public enum StatusPagamento {
    PENDENTE,
    APROVADO,
    NEGADO
}
```

---

### Classe `Pagamento` (Superclasse)

**Atributos:**
```java
- String id                    // identificador único
- String nomeCliente
- String cpfCliente
- double valorOriginal
- StatusPagamento status
- LocalDateTime dataHora       // import java.time.LocalDateTime
```

**Construtor:**
```java
public Pagamento(String id, String nomeCliente, String cpfCliente,
                 double valorOriginal)
```

- Inicializa atributos
- Define `status = StatusPagamento.PENDENTE`
- Define `dataHora = LocalDateTime.now()`

**Métodos polimórficos (devem ser sobrescritos):**

1. `public double calcularValorFinal()`
   - Retorna o valor final após taxas/descontos
   - Implementação base: retorna `valorOriginal`

2. `public void processar()`
   - Simula o processamento (define status)
   - Implementação base: marca como APROVADO

3. `public String getDescricaoTipo()`
   - Retorna nome amigável do tipo
   - Implementação base: `"Pagamento Genérico"`

**Métodos herdados (NÃO sobrescrever):**

4. `public void gerarComprovante()`
   - Mostra: ID, cliente, CPF, data/hora, valor original, valor final, status
   - **DEVE chamar `calcularValorFinal()` polimórficamente**
   - **DEVE chamar `getDescricaoTipo()` polimórficamente**
   - Subclasses adicionam dados específicos via sobrescrita parcial

5. Getters para todos os atributos

**Dica de formatação para data/hora:**

```java
import java.time.format.DateTimeFormatter;

DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
String dataHoraStr = dataHora.format(fmt);
```

---

### Classe `Pix extends Pagamento`

**Atributos específicos:**
```java
- String chavePix
```

**Construtor:**
```java
public Pix(String id, String nomeCliente, String cpfCliente,
           double valorOriginal, String chavePix)
```

**Métodos a sobrescrever:**

1. `@Override calcularValorFinal()`
   - Retorna `valorOriginal × 0.95` (5% desconto)

2. `@Override processar()`
   - Sempre aprova: `status = APROVADO`
   - Imprime: `"PIX processado instantaneamente"`

3. `@Override getDescricaoTipo()`
   - Retorna `"PIX"`

4. `@Override gerarComprovante()`
   - Chama `super.gerarComprovante()`
   - Adiciona linha com `Chave PIX: ...`

---

### Classe `Boleto extends Pagamento`

**Atributos específicos:**
```java
- String linhaDigitavel  // 44 dígitos
- LocalDate dataVencimento
```

**Construtor:**
```java
public Boleto(String id, String nomeCliente, String cpfCliente,
              double valorOriginal, String linhaDigitavel, int diasParaVencer)
```

- `dataVencimento = LocalDate.now().plusDays(diasParaVencer)`

**Métodos a sobrescrever:**

1. `@Override calcularValorFinal()`
   - Retorna `valorOriginal` (sem alteração)

2. `@Override processar()`
   - Mantém status `PENDENTE`
   - Imprime: `"Boleto gerado. Pagar até [data]"`

3. `@Override getDescricaoTipo()`
   - Retorna `"Boleto Bancário"`

4. `@Override gerarComprovante()`
   - Chama `super.gerarComprovante()`
   - Adiciona linhas com `Linha Digitável` e `Vencimento`

---

### Classe `CartaoCredito extends Pagamento`

**Atributos específicos:**
```java
- String numeroCartao        // 16 dígitos
- String bandeira             // "Visa", "Master", "Elo", "Amex"
- int parcelas                // 1 a 12
```

**Construtor:**
```java
public CartaoCredito(String id, String nomeCliente, String cpfCliente,
                     double valorOriginal, String numeroCartao,
                     String bandeira, int parcelas)
```

- Validação: parcelas entre 1 e 12. Se inválido, atribui 1.

**Métodos a sobrescrever:**

1. `@Override calcularValorFinal()`
   - Calcula a taxa conforme tabela de parcelas
   - Retorna `valorOriginal × (1 + taxa)`

2. `@Override processar()`
   - 80% de chance de aprovar: `Math.random() < 0.8`
   - Define status APROVADO ou NEGADO
   - Imprime mensagem condizente

3. `@Override getDescricaoTipo()`
   - Retorna `"Cartão de Crédito (" + bandeira + ")"`

4. `@Override gerarComprovante()`
   - Chama `super.gerarComprovante()`
   - Adiciona: bandeira, cartão **mascarado**, parcelas, valor da parcela

**Método auxiliar:**

```java
private String mascarar(String numero) {
    if (numero.length() < 4) return numero;
    return "**** **** **** " + numero.substring(numero.length() - 4);
}
```

---

### Classe `CartaoDebito extends Pagamento`

**Atributos específicos:**
```java
- String numeroCartao
- String bandeira
```

**Atributo estático:**
```java
private static final double TAXA_FIXA = 1.50;
```

**Construtor:**
```java
public CartaoDebito(String id, String nomeCliente, String cpfCliente,
                    double valorOriginal, String numeroCartao, String bandeira)
```

**Métodos a sobrescrever:**

1. `@Override calcularValorFinal()`
   - Retorna `valorOriginal + TAXA_FIXA`

2. `@Override processar()`
   - 95% de chance de aprovar: `Math.random() < 0.95`
   - Define status APROVADO ou NEGADO

3. `@Override getDescricaoTipo()`
   - Retorna `"Cartão de Débito (" + bandeira + ")"`

4. `@Override gerarComprovante()`
   - Chama `super.gerarComprovante()`
   - Adiciona: bandeira, cartão mascarado, taxa fixa

---

### Classe `ProcessadorPagamentos` (Gerenciamento polimórfico)

**Atributos:**
```java
- ArrayList<Pagamento> historico
- String nomeLoja
```

**Construtor:**
```java
public ProcessadorPagamentos(String nomeLoja)
```

**Métodos:**

1. `public void adicionarPagamento(Pagamento pagamento)`
   - Adiciona no histórico
   - **Chama `pagamento.processar()` automaticamente**

2. `public void listarTodos()`
   - Lista todos os pagamentos com tipo, cliente, valor final, status

3. `public double calcularReceitaTotal()`
   - Soma `calcularValorFinal()` de TODOS os pagamentos com status APROVADO

4. `public ArrayList<Pagamento> filtrarPorStatus(StatusPagamento status)`
   - Retorna pagamentos com o status indicado

5. `public ArrayList<Pagamento> filtrarPorTipo(Class<?> tipo)`
   - Retorna pagamentos do tipo especificado
   - Use `tipo.isInstance(p)`

6. `public void exibirEstatisticas()`
   - Total de pagamentos
   - Aprovados, Pendentes, Negados (contagem)
   - Receita total (apenas aprovados)
   - Forma de pagamento mais usada

7. `public void exibirComprovantesAprovados()`
   - Para cada pagamento APROVADO, chama `gerarComprovante()`
   - **Polimórfico: cada um exibe diferente**

---

### Classe `SistemaEcommerce` (Main)

**Requisitos do teste:**

1. Criar uma instância de `ProcessadorPagamentos`
2. Cadastrar pelo menos:
   - 2 pagamentos via PIX
   - 2 pagamentos via Boleto
   - 3 pagamentos via Cartão de Crédito (com parcelas variadas)
   - 2 pagamentos via Cartão de Débito

3. Listar todos os pagamentos
4. Exibir comprovantes dos APROVADOS
5. Mostrar estatísticas
6. Filtrar por tipo (PIX, por exemplo) e exibir
7. Filtrar por status (NEGADO) e exibir
8. Calcular e exibir receita total

---

## Exemplo de Saída Esperada

```
╔═══════════════════════════════════════════════════╗
║   SISTEMA DE PAGAMENTOS - LOJA TECHSHOP           ║
╚═══════════════════════════════════════════════════╝

=== PROCESSANDO PAGAMENTOS ===

✓ Pagamento PIX adicionado.
  PIX processado instantaneamente

✓ Pagamento Boleto Bancário adicionado.
  Boleto gerado. Pagar até 30/04/2026

✓ Pagamento Cartão de Crédito (Visa) adicionado.
  Pagamento aprovado pela operadora.

✓ Pagamento Cartão de Crédito (Master) adicionado.
  Pagamento NEGADO pela operadora.
...

╔═══════════════════════════════════════════════════════════╗
║  HISTÓRICO DE PAGAMENTOS - TECHSHOP
╠═══════════════════════════════════════════════════════════╣
║  [1] PIX                            | Ana    | R$  475,00 | APROVADO
║  [2] Boleto Bancário                | João   | R$  500,00 | PENDENTE
║  [3] Cartão de Crédito (Visa)       | Carla  | R$  102,50 | APROVADO
║  [4] Cartão de Crédito (Master)     | Diego  | R$  306,00 | NEGADO
║  [5] Cartão de Débito (Elo)         | Eduardo| R$   76,50 | APROVADO
...
╚═══════════════════════════════════════════════════════════╝

=== COMPROVANTES DOS PAGAMENTOS APROVADOS ===

╔════════════════════════════════════════╗
║         COMPROVANTE                    ║
╠════════════════════════════════════════╣
║  ID: PG001
║  Tipo: PIX
║  Cliente: Ana Silva (111.111.111-11)
║  Data/Hora: 27/04/2026 14:35:22
║  Valor original: R$ 500,00
║  Valor final: R$ 475,00
║  Status: APROVADO
║  Chave PIX: ana@email.com
╚════════════════════════════════════════╝

╔════════════════════════════════════════╗
║         COMPROVANTE                    ║
╠════════════════════════════════════════╣
║  ID: PG003
║  Tipo: Cartão de Crédito (Visa)
║  Cliente: Carla Lima (333.333.333-33)
║  Data/Hora: 27/04/2026 14:35:25
║  Valor original: R$ 100,00
║  Valor final: R$ 102,50
║  Status: APROVADO
║  Bandeira: Visa
║  Cartão: **** **** **** 1234
║  Parcelas: 3x de R$ 34,17
╚════════════════════════════════════════╝

=== ESTATÍSTICAS ===

Total de pagamentos: 9
  - Aprovados: 6
  - Pendentes: 2
  - Negados: 1

Receita total (aprovados): R$ 2.453,75
Forma mais usada: Cartão de Crédito (3 ocorrências)

=== FILTRO POR TIPO: PIX ===

- PG001 - Ana Silva - R$ 475,00 - APROVADO
- PG002 - Bruno Souza - R$ 285,00 - APROVADO

=== FILTRO POR STATUS: NEGADO ===

- PG004 - Diego Rocha - R$ 306,00 - Cartão de Crédito (Master)
```

---

## Critérios de Avaliação

Seu código será avaliado por:

### Funcionalidade (35%)
- ✅ Todas as classes criadas
- ✅ Hierarquia implementada corretamente
- ✅ Métodos funcionam como especificado
- ✅ Sistema completo executa sem erros

### Uso de Polimorfismo (35%)
- ✅ Métodos polimórficos sobrescritos com `@Override`
- ✅ `ArrayList<Pagamento>` usada como coleção polimórfica
- ✅ `gerarComprovante()` da superclasse chama métodos polimórficos
- ✅ `processar()` chamado automaticamente pelo gerenciador
- ✅ Filtros e estatísticas usam dispatch dinâmico

### Qualidade do Código (20%)
- ✅ Nomes descritivos de variáveis e métodos
- ✅ Encapsulamento adequado (private/protected/public)
- ✅ Código organizado e legível
- ✅ Validações e tratamento de casos especiais

### Apresentação (10%)
- ✅ Saída formatada e profissional
- ✅ Mensagens claras para o usuário
- ✅ Mascaramento correto de cartões
- ✅ Datas formatadas em padrão brasileiro

---

## Desafios Opcionais (Bônus)

Se terminar antes do tempo, implemente:

### 1. Estorno polimórfico

- Método `estornar()` na superclasse
- PIX: estorno imediato (status volta para... como representar isso?)
- Boleto: só pode estornar se ainda estiver PENDENTE
- Cartão Crédito: estorno em até X dias após aprovação
- Cartão Débito: estorno imediato

### 2. Nova forma de pagamento: Criptomoeda

Adicione **sem mexer nas outras classes**:
- Atributos: `tipoCripto` (BTC, ETH, etc.), `enderecoCarteira`
- `calcularValorFinal()`: aplica taxa de conversão
- `processar()`: simula confirmação na blockchain

> **Teste de polimorfismo:** se você precisar mexer em mais de uma classe além da nova `Criptomoeda`, sua arquitetura tem problemas!

### 3. Padrão Strategy de descontos

- Interface ou classe `EstrategiaDesconto`
- Estratégias: `PrimeiraCompra` (5% extra), `BlackFriday` (15%), `Aniversariante` (10%)
- Cada `Pagamento` pode ter uma `EstrategiaDesconto` agregada
- Adapte `calcularValorFinal()` para considerar a estratégia

### 4. Relatório por período

- `relatorio(LocalDate inicio, LocalDate fim)`
- Filtra pagamentos do período
- Agrupa por tipo
- Mostra totais por categoria

### 5. Persistência simples

- Salvar histórico em arquivo CSV ao final
- Cabeçalho: `id,tipo,cliente,valor_original,valor_final,status,data`
- Cada pagamento gera sua linha (polimorficamente!) via `gerarLinhaCSV()`

---

## Dicas de Implementação

### Geração de ID único

```java
import java.util.UUID;

String id = "PG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
```

### Cálculo da taxa do Cartão de Crédito

```java
@Override
public double calcularValorFinal() {
    double taxa;
    if (parcelas <= 6) {
        taxa = 0.025;  // 2.5%
    } else {
        taxa = 0.025 + (parcelas - 6) * 0.01;  // +1% por parcela extra
    }
    return valorOriginal * (1 + taxa);
}
```

### Mascaramento de cartão

```java
private String mascarar(String numero) {
    if (numero == null || numero.length() < 4) return "****";
    String ultimos = numero.substring(numero.length() - 4);
    return "**** **** **** " + ultimos;
}
```

### Filtro por tipo usando reflexão

```java
public ArrayList<Pagamento> filtrarPorTipo(Class<?> tipo) {
    ArrayList<Pagamento> resultado = new ArrayList<>();
    for (Pagamento p : historico) {
        if (tipo.isInstance(p)) {
            resultado.add(p);
        }
    }
    return resultado;
}

// Uso:
ArrayList<Pagamento> pixs = processador.filtrarPorTipo(Pix.class);
```

### Aprovação probabilística

```java
@Override
public void processar() {
    if (Math.random() < 0.8) {  // 80% de chance
        this.status = StatusPagamento.APROVADO;
        System.out.println("  Pagamento aprovado pela operadora.");
    } else {
        this.status = StatusPagamento.NEGADO;
        System.out.println("  Pagamento NEGADO pela operadora.");
    }
}
```

### Setter protegido para status

Como subclasses precisam alterar o status, você pode:

**Opção 1:** deixar `status` como `protected`
```java
protected StatusPagamento status;
```

**Opção 2:** criar setter `protected`
```java
protected void setStatus(StatusPagamento novoStatus) {
    this.status = novoStatus;
}
```

A **Opção 2** preserva melhor o encapsulamento.

---

## Checklist de Desenvolvimento

Use este checklist para organizar seu trabalho:

**Fase 1: Planejamento (15 min)**
- [ ] Desenhar hierarquia no papel
- [ ] Listar atributos de cada classe
- [ ] Marcar métodos polimórficos
- [ ] Planejar assinatura dos construtores
- [ ] Decidir tipos de retorno e modificadores

**Fase 2: Superclasse e Enum (20 min)**
- [ ] Criar enum `StatusPagamento`
- [ ] Criar classe `Pagamento`
- [ ] Implementar construtor e getters
- [ ] Implementar `gerarComprovante()` (genérico, usando polimorfismo)
- [ ] Testar isoladamente com classe de teste

**Fase 3: Subclasses (30 min)**
- [ ] Criar `Pix` com sobrescritas
- [ ] Criar `Boleto` com sobrescritas
- [ ] Criar `CartaoCredito` com sobrescritas + cálculo de taxa
- [ ] Criar `CartaoDebito` com sobrescritas
- [ ] Testar cada uma isoladamente

**Fase 4: Gerenciamento (25 min)**
- [ ] Criar classe `ProcessadorPagamentos`
- [ ] Implementar coleção polimórfica
- [ ] Implementar listagem e filtros
- [ ] Implementar estatísticas
- [ ] Testar com mistura de tipos

**Fase 5: Teste e Apresentação (30 min)**
- [ ] Criar `SistemaEcommerce` com `main`
- [ ] Cadastrar pagamentos variados
- [ ] Executar todas as operações
- [ ] Ajustar formatação de saída
- [ ] Validar mascaramento de cartões
- [ ] Preparar apresentação

---

## Apresentação das Soluções

Ao final do bloco, cada aluno (ou dupla) deve:

1. **Demonstrar o sistema funcionando** (5 min)
   - Executar e mostrar saída
   - Apontar onde o polimorfismo está agindo

2. **Responder perguntas** (3 min)
   - Onde está o polimorfismo no seu código?
   - O que aconteceria se tirasse `@Override` de um método?
   - Como adicionaria uma forma de pagamento nova?
   - Por que `gerarComprovante()` na superclasse já mostra os dados específicos das subclasses?

3. **Discussão coletiva** (10 min no final)
   - Comparar diferentes abordagens
   - Destacar usos criativos do polimorfismo
   - Discutir trade-offs (`protected` vs `private + setter`)

---

## Resumo do Bloco 3

Neste bloco você:

✅ Aplicou polimorfismo de forma **independente**
✅ Modelou um **domínio realista** (pagamentos brasileiros)
✅ Implementou **dispatch dinâmico** em vários métodos
✅ Praticou **listas polimórficas** com `ArrayList<Pagamento>`
✅ Aplicou o **Princípio Aberto/Fechado**
✅ Apresentou e **defendeu** suas escolhas

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## 🎓 Conclusão da Aula 08

Parabéns! Você completou a Aula 08 e agora domina:

✅ Conceito e mecânica de **Polimorfismo**
✅ **Sobrescrita** (`@Override`) e diferença para sobrecarga
✅ **Tipo estático** (referência) × **Tipo dinâmico** (objeto)
✅ **Late binding** / **Dispatch dinâmico** em runtime
✅ **Coleções polimórficas** (`ArrayList<Tipo>`)
✅ **Upcasting** implícito e **Downcasting** com `instanceof`
✅ **Princípio Aberto/Fechado** aplicado na prática

### Resposta à problematização inicial

> **"Como processar uma folha de pagamento com 500 funcionários de tipos diferentes em um único loop?"**

**Resposta:** Coloque todos em uma `ArrayList<Funcionario>` e itere uma única vez chamando `f.calcularSalario()`. Como cada subclasse sobrescreveu esse método, o **dispatch dinâmico** da JVM escolhe — em tempo de execução, baseado no **tipo dinâmico** de cada objeto — a versão correta. Para adicionar novos tipos no futuro, basta criar a subclasse: o código da folha não muda.

### A trilogia da OO está quase completa

```
Encapsulamento    → CONTROLA o acesso aos dados (Aula 04)
Herança           → COMPARTILHA estrutura entre classes (Aula 07)
Polimorfismo      → UNIFICA tratamento de tipos diferentes (Aula 08) ★
```

**Na próxima aula (Aula 09):** Você aprenderá **Classes Abstratas** — como **forçar** que subclasses implementem certos métodos e criar hierarquias mais robustas! 🚀
