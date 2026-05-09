# Bloco 3 — Exercício Autônomo: Sistema de Pagamentos

## Objetivos do Bloco

- Aplicar classes abstratas e Template Method de forma **independente**
- Tomar decisões de design sem orientação direta
- Implementar **hierarquia abstrata + métodos abstratos + concretos** em conjunto
- Validar a compreensão dos conceitos da Aula 09
- Apresentar e discutir diferentes soluções

---

## 🎯 Problema: Processamento de Pagamentos Multi-método

Você foi contratado para desenvolver o **módulo de pagamentos** de uma loja online. Esse módulo precisa processar pagamentos em **vários métodos diferentes** e seguir o mesmo fluxo padronizado, independente do método.

### Métodos Suportados

1. **Cartão de Crédito**
   - Cobra **taxa de 4%** sobre o valor
   - Valida número do cartão (16 dígitos)

2. **PIX**
   - **Sem taxa** (gratuito)
   - Valida chave PIX no formato CPF (11 dígitos)

3. **Boleto Bancário**
   - Cobra **taxa fixa de R$ 2,50**
   - Valida código de barras (47 dígitos)

### Características Comuns (toda transação)

Toda transação tem:
- Identificador único (ID)
- Nome do cliente
- Valor original (em R$)
- Taxa cobrada (calculada conforme o método)
- Valor total (valor + taxa)
- Data e hora do processamento
- Status (PENDENTE, APROVADO, RECUSADO)
- Mensagem de retorno (motivo de recusa, autorização, etc.)

### Fluxo de Processamento (igual para todos — Template Method!)

Independente do método, **todo pagamento segue este fluxo**:

```
1. VALIDAR dados de pagamento (depende do método)
2. CALCULAR taxa (depende do método)
3. AUTORIZAR o pagamento (depende do método)
4. REGISTRAR a transação (passo comum)
5. NOTIFICAR o cliente (passo comum)
```

> Se a validação dos dados falhar, o pagamento é marcado como RECUSADO e o fluxo termina antes de calcular taxa ou autorizar.
> Se a autorização falhar, o pagamento é marcado como RECUSADO mas mesmo assim o sistema registra e notifica.

### Regras Específicas por Método

- **Cartão:** o número do cartão deve ter **16 dígitos numéricos**. Taxa = 4% do valor. Autorização simulada com 90% de sucesso.
- **PIX:** a chave deve ter **11 dígitos** (formato CPF simplificado). Taxa = R$ 0,00. Autorização simulada com 98% de sucesso.
- **Boleto:** o código deve ter **47 dígitos**. Taxa = R$ 2,50 fixa. Autorização simulada com 95% de sucesso.

> 💡 **Importante:** este exercício é **puramente em memória**. Não há leitura nem escrita de arquivos, nenhum acesso a banco de dados real e nenhuma chamada de API externa. O foco é a **estrutura abstrata + Template Method**, e tudo é simulado com `System.out.println` e `Math.random()`.

---

## Planejamento Necessário (FAÇA ANTES DE CODIFICAR!)

### Passo 1: Desenhe a hierarquia no papel

```
┌──────────────────────────────────────────┐
│   « abstract » Pagamento                 │  ← Que atributos comuns?
│         (SUPERCLASSE)                    │  ← Que métodos abstratos?
│                                          │  ← Que métodos concretos?
└────────────────┬─────────────────────────┘
                 │
   ┌─────────────┼──────────────────┐
   │             │                  │
┌──▼──────────┐ ┌▼──────────────┐ ┌─▼──────────────┐
│PagamentoCart│ │PagamentoPIX   │ │PagamentoBoleto │
│  (Cartão)   │ │               │ │                │
└─────────────┘ └───────────────┘ └────────────────┘
```

**Perguntas para reflexão:**
- O que TODOS os pagamentos têm em comum?
- O que é específico de cada método?
- Quais métodos devem ser **abstratos** (variação)?
- Quais métodos podem ser **concretos** (comum)?
- Onde o **Template Method** vai aparecer?

### Passo 2: Identifique os métodos do Template Method

Marque com **A** (abstrato) ou **C** (concreto) cada passo:

- [ ] `validarDados()`         — A ou C?
- [ ] `calcularTaxa()`         — A ou C?
- [ ] `autorizar()`            — A ou C?
- [ ] `registrarTransacao()`   — A ou C?
- [ ] `notificarCliente()`     — A ou C?
- [ ] `getMetodo()`            — A ou C?

> 💡 **Dica:** o que **varia por método** deve ser **abstrato**. O que é **igual para todos** deve ser **concreto**.

### Passo 3: Decida os modificadores de acesso

- O Template Method (`processar()`) deve ser `public final`?
- Atributos comuns: `protected`?
- Métodos abstratos: `protected` (pois só o Template Method da abstrata os chama)?
- Métodos concretos auxiliares: `protected`?

---

## Requisitos Detalhados

### Enum `StatusPagamento`

```java
public enum StatusPagamento {
    PENDENTE,
    APROVADO,
    RECUSADO
}
```

---

### Classe Abstrata `Pagamento`

**Atributos:**
```java
- String id
- String nomeCliente
- double valor              // valor original
- double taxa               // calculada pelo método específico
- double valorTotal         // valor + taxa
- LocalDateTime dataProcessamento
- StatusPagamento status
- String mensagemRetorno    // motivo de recusa, código de autorização, etc.
```

**Construtor:**
```java
public Pagamento(String id, String nomeCliente, double valor)
```

- Inicializa atributos
- `status = PENDENTE`
- `taxa = 0.0`
- `valorTotal = 0.0`
- `mensagemRetorno = ""`

**TEMPLATE METHOD (final, concreto):**

```java
public final void processar() {
    System.out.println("\n>>> Processando pagamento " + id + " (" + getMetodo() + ")");

    // 1. Validação (abstrato)
    if (!validarDados()) {
        status = StatusPagamento.RECUSADO;
        mensagemRetorno = "Dados inválidos para " + getMetodo();
        System.out.println("    ✗ " + mensagemRetorno);
        registrarTransacao();
        notificarCliente();
        return;
    }

    // 2. Cálculo da taxa (abstrato)
    taxa = calcularTaxa();
    valorTotal = valor + taxa;
    System.out.printf("    Taxa: R$ %.2f | Valor total: R$ %.2f%n", taxa, valorTotal);

    // 3. Autorização (abstrato)
    boolean autorizado = autorizar();
    dataProcessamento = LocalDateTime.now();

    if (autorizado) {
        status = StatusPagamento.APROVADO;
    } else {
        status = StatusPagamento.RECUSADO;
    }

    // 4. Registro (concreto — comum)
    registrarTransacao();

    // 5. Notificação (concreto — comum)
    notificarCliente();
}
```

**Métodos abstratos (cada método de pagamento implementa):**

1. `protected abstract boolean validarDados()`
   - Retorna `true` se os dados específicos do método são válidos.

2. `protected abstract double calcularTaxa()`
   - Retorna a taxa cobrada pelo método.

3. `protected abstract boolean autorizar()`
   - Simula autorização. Use `Math.random()` para definir sucesso/falha.
   - Atualiza `mensagemRetorno` com o resultado (ex: "Autorização: 6X8K9", "Cartão recusado pelo emissor").

4. `public abstract String getMetodo()`
   - Retorna nome amigável: `"Cartão de Crédito"`, `"PIX"`, `"Boleto"`.

**Métodos concretos (comum a todos):**

5. `protected void registrarTransacao()`
   - Imprime o registro da transação:

```
    📝 Transação registrada:
       ID: PAG001 | Cliente: Ana Silva | Total: R$ 104,00 | Status: APROVADO
```

6. `protected void notificarCliente()`
   - Imprime uma notificação ao cliente:

```
    📬 Cliente Ana Silva notificado: pagamento APROVADO
```

7. Getters apropriados (id, nomeCliente, valor, taxa, valorTotal, status, dataProcessamento, mensagemRetorno).

---

### Classe `PagamentoCartao extends Pagamento`

**Atributos específicos:**
```java
- String numeroCartao   // 16 dígitos
- String nomeNoCartao
```

**Construtor:**
```java
public PagamentoCartao(String id, String nomeCliente, double valor,
                       String numeroCartao, String nomeNoCartao)
```

**Implementação dos abstratos:**

1. `validarDados()`: retorna `true` se `numeroCartao` tem exatamente 16 dígitos numéricos.

2. `calcularTaxa()`: retorna `valor * 0.04` (4%).

3. `autorizar()`: simulação com 90% de sucesso. Em caso de sucesso, define `mensagemRetorno` como `"Autorização: " + códigoAleatório`. Em caso de falha, `"Cartão recusado pelo emissor"`.

4. `getMetodo()`: retorna `"Cartão de Crédito"`.

---

### Classe `PagamentoPIX extends Pagamento`

**Atributos específicos:**
```java
- String chavePIX   // 11 dígitos (formato CPF simplificado)
```

**Construtor:**
```java
public PagamentoPIX(String id, String nomeCliente, double valor, String chavePIX)
```

**Implementação dos abstratos:**

1. `validarDados()`: retorna `true` se `chavePIX` tem exatamente 11 dígitos numéricos.

2. `calcularTaxa()`: retorna `0.0` (PIX é gratuito).

3. `autorizar()`: simulação com 98% de sucesso. Em caso de sucesso, `mensagemRetorno = "PIX confirmado em tempo real"`. Em caso de falha, `"Chave PIX não encontrada"`.

4. `getMetodo()`: retorna `"PIX"`.

---

### Classe `PagamentoBoleto extends Pagamento`

**Atributos específicos:**
```java
- String codigoBarras   // 47 dígitos
- LocalDate vencimento
```

**Construtor:**
```java
public PagamentoBoleto(String id, String nomeCliente, double valor,
                       String codigoBarras, LocalDate vencimento)
```

**Implementação dos abstratos:**

1. `validarDados()`: retorna `true` se `codigoBarras` tem exatamente 47 dígitos numéricos.

2. `calcularTaxa()`: retorna `2.50` (taxa fixa).

3. `autorizar()`: simulação com 95% de sucesso. Em caso de sucesso, `mensagemRetorno = "Boleto registrado no banco"`. Em caso de falha, `"Falha no registro do boleto"`.

4. `getMetodo()`: retorna `"Boleto"`.

---

### Classe `GerenciadorPagamentos`

**Atributos:**
```java
- List<Pagamento> historico
- String nomeLoja
```

**Métodos:**

1. `public void processar(Pagamento p)`
   - Adiciona ao histórico
   - Chama `p.processar()`

2. `public void exibirEstatisticas()`
   - Total de pagamentos processados
   - Total aprovados, recusados
   - Soma do valor total movimentado (apenas dos aprovados)
   - Soma das taxas (apenas dos aprovados)
   - Taxa de aprovação (%)

3. `public List<Pagamento> filtrarPorMetodo(Class<?> tipo)`
   - Retorna pagamentos de um determinado método (ex: apenas PIX)

4. `public List<Pagamento> filtrarPorStatus(StatusPagamento status)`
   - Retorna pagamentos com o status indicado

5. `public Pagamento maiorValor()`
   - Retorna o pagamento aprovado de maior valor total

6. `public void listarTodos()`
   - Lista resumida de todos os pagamentos no histórico

---

### Classe `SistemaPagamentos` (Main)

**Requisitos do teste:**

1. Criar um `GerenciadorPagamentos`
2. Processar pelo menos:
   - 2 pagamentos via Cartão (um válido e um com número de cartão inválido)
   - 2 pagamentos via PIX (um válido e um com chave inválida)
   - 1 pagamento via Boleto (válido)

3. Tentar instanciar `Pagamento` direto (deixar comentado para mostrar o erro)
4. Listar todos os pagamentos
5. Exibir estatísticas
6. Filtrar e exibir apenas pagamentos de um método (ex: PIX)
7. Mostrar o pagamento de maior valor

---

## Exemplo de Saída Esperada

```
╔═══════════════════════════════════════════════════╗
║   SISTEMA DE PAGAMENTOS - Loja Online             ║
╚═══════════════════════════════════════════════════╝

>>> Processando pagamento PAG001 (Cartão de Crédito)
    Taxa: R$ 4,00 | Valor total: R$ 104,00
    📝 Transação registrada:
       ID: PAG001 | Cliente: Ana Silva | Total: R$ 104,00 | Status: APROVADO
    📬 Cliente Ana Silva notificado: pagamento APROVADO

>>> Processando pagamento PAG002 (Cartão de Crédito)
    ✗ Dados inválidos para Cartão de Crédito
    📝 Transação registrada:
       ID: PAG002 | Cliente: Bruno Costa | Total: R$ 0,00 | Status: RECUSADO
    📬 Cliente Bruno Costa notificado: pagamento RECUSADO

>>> Processando pagamento PAG003 (PIX)
    Taxa: R$ 0,00 | Valor total: R$ 250,00
    📝 Transação registrada:
       ID: PAG003 | Cliente: Carla Dias | Total: R$ 250,00 | Status: APROVADO
    📬 Cliente Carla Dias notificado: pagamento APROVADO

>>> Processando pagamento PAG004 (PIX)
    ✗ Dados inválidos para PIX
    📝 Transação registrada:
       ID: PAG004 | Cliente: Diego Lima | Total: R$ 0,00 | Status: RECUSADO
    📬 Cliente Diego Lima notificado: pagamento RECUSADO

>>> Processando pagamento PAG005 (Boleto)
    Taxa: R$ 2,50 | Valor total: R$ 502,50
    📝 Transação registrada:
       ID: PAG005 | Cliente: Eduarda Mello | Total: R$ 502,50 | Status: APROVADO
    📬 Cliente Eduarda Mello notificado: pagamento APROVADO

╔═══════════════════════════════════════════════════════════╗
║  HISTÓRICO DE PAGAMENTOS - LOJA ONLINE
╠═══════════════════════════════════════════════════════════╣
║  [1] PAG001 | Cartão de Crédito | Ana Silva       | APROVADO
║  [2] PAG002 | Cartão de Crédito | Bruno Costa     | RECUSADO
║  [3] PAG003 | PIX               | Carla Dias      | APROVADO
║  [4] PAG004 | PIX               | Diego Lima      | RECUSADO
║  [5] PAG005 | Boleto            | Eduarda Mello   | APROVADO
╚═══════════════════════════════════════════════════════════╝

=== ESTATÍSTICAS ===
Total de pagamentos: 5
  ✓ Aprovados: 3
  ✗ Recusados: 2
Valor total movimentado: R$ 856,50
Total de taxas:          R$ 6,50
Taxa de aprovação: 60,0%

=== FILTRO: APENAS PIX ===
- PAG003 | Carla Dias | APROVADO
- PAG004 | Diego Lima | RECUSADO

=== PAGAMENTO DE MAIOR VALOR ===
PAG005 (Boleto) | Eduarda Mello | R$ 502,50
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
- ✅ `Pagamento` é declarada `abstract`
- ✅ Métodos abstratos sem corpo (terminados em `;`)
- ✅ Template Method `processar()` é `public final`
- ✅ Subclasses implementam **todos** os abstratos
- ✅ `registrarTransacao()` e `notificarCliente()` são concretos
- ✅ `List<Pagamento>` usada como coleção polimórfica

### Qualidade do Código (20%)
- ✅ Nomes descritivos
- ✅ Encapsulamento adequado (`private`/`protected`/`public`)
- ✅ Código organizado e legível
- ✅ Uso de `@Override` em sobrescritas
- ✅ Validações claras com retorno booleano

### Apresentação (10%)
- ✅ Saída formatada e profissional
- ✅ Mensagens claras
- ✅ Datas formatadas em padrão brasileiro
- ✅ Valores em reais com duas casas decimais (`%.2f`)

---

## Desafios Opcionais (Bônus)

Se terminar antes do tempo, implemente:

### 1. Hook method opcional

- Adicione método **concreto** (vazio) `antesDeProcessar()` na abstrata
- Subclasses **podem** sobrescrever (mas não precisam)
- Útil para registrar timestamp de início, validar limite de horário, etc.

### 2. Estorno de pagamento

- Adicione método `estornar()` na abstrata
- Apenas pagamentos com status `APROVADO` podem ser estornados
- Status muda para `RECUSADO` e taxa pode ou não ser devolvida (regra por método)

### 3. Novo método: Débito Automático

Adicione `PagamentoDebito` **sem mexer em nenhuma classe existente**:
- Taxa de 1,5% sobre o valor
- Valida conta bancária (agência + conta)
- Mostre que a estrutura abstrata + Template Method permite essa extensão limpa

### 4. Limite máximo por método

- Cartão: até R$ 10.000 por transação
- PIX: até R$ 1.000 por transação
- Boleto: sem limite
- Adicione essa validação dentro de `validarDados()` de cada subclasse

### 5. Sobrescrever `notificarCliente()` em uma subclasse

- Em `PagamentoBoleto`, sobrescreva `notificarCliente()` para imprimir também a data de vencimento
- Use `@Override` e mostre que sobrescrever um método **concreto** é diferente de implementar um abstrato

---

## Dicas de Implementação

### Estrutura básica do Template Method

```java
public final void processar() {
    System.out.println("\n>>> Processando pagamento " + id + " (" + getMetodo() + ")");

    if (!validarDados()) {                       // abstrato
        status = StatusPagamento.RECUSADO;
        mensagemRetorno = "Dados inválidos para " + getMetodo();
        System.out.println("    ✗ " + mensagemRetorno);
        registrarTransacao();                    // concreto
        notificarCliente();                      // concreto
        return;
    }

    taxa = calcularTaxa();                       // abstrato
    valorTotal = valor + taxa;
    System.out.printf("    Taxa: R$ %.2f | Valor total: R$ %.2f%n", taxa, valorTotal);

    boolean autorizado = autorizar();            // abstrato
    dataProcessamento = LocalDateTime.now();

    status = autorizado ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO;

    registrarTransacao();                        // concreto
    notificarCliente();                          // concreto
}
```

### Validação de cartão (16 dígitos)

```java
@Override
protected boolean validarDados() {
    return numeroCartao != null && numeroCartao.matches("\\d{16}");
}
```

### Validação de chave PIX (11 dígitos)

```java
@Override
protected boolean validarDados() {
    return chavePIX != null && chavePIX.matches("\\d{11}");
}
```

### Autorização simulada com código aleatório

```java
@Override
protected boolean autorizar() {
    boolean sucesso = Math.random() < 0.9;
    if (sucesso) {
        String codigo = String.format("%05d", (int)(Math.random() * 100000));
        mensagemRetorno = "Autorização: " + codigo;
    } else {
        mensagemRetorno = "Cartão recusado pelo emissor";
    }
    return sucesso;
}
```

### Registro da transação (concreto)

```java
protected void registrarTransacao() {
    System.out.println("    📝 Transação registrada:");
    System.out.printf("       ID: %s | Cliente: %s | Total: R$ %.2f | Status: %s%n",
        id, nomeCliente, valorTotal, status);
}
```

### Filtro por tipo (polimórfico)

```java
public List<Pagamento> filtrarPorMetodo(Class<?> tipo) {
    List<Pagamento> resultado = new ArrayList<>();
    for (Pagamento p : historico) {
        if (tipo.isInstance(p)) {
            resultado.add(p);
        }
    }
    return resultado;
}

// Uso:
List<Pagamento> pixs = gerenciador.filtrarPorMetodo(PagamentoPIX.class);
```

### Estatísticas — somando valores apenas dos aprovados

```java
public void exibirEstatisticas() {
    int total = historico.size();
    int aprovados = 0, recusados = 0;
    double valorMovimentado = 0.0;
    double totalTaxas = 0.0;

    for (Pagamento p : historico) {
        if (p.getStatus() == StatusPagamento.APROVADO) {
            aprovados++;
            valorMovimentado += p.getValorTotal();
            totalTaxas += p.getTaxa();
        } else if (p.getStatus() == StatusPagamento.RECUSADO) {
            recusados++;
        }
    }

    System.out.println("\n=== ESTATÍSTICAS ===");
    System.out.println("Total de pagamentos: " + total);
    System.out.println("  ✓ Aprovados: " + aprovados);
    System.out.println("  ✗ Recusados: " + recusados);
    System.out.printf("Valor total movimentado: R$ %.2f%n", valorMovimentado);
    System.out.printf("Total de taxas:          R$ %.2f%n", totalTaxas);

    if (total > 0) {
        double taxaAprovacao = (aprovados * 100.0) / total;
        System.out.printf("Taxa de aprovação: %.1f%%%n", taxaAprovacao);
    }
}
```

---

## Checklist de Desenvolvimento

Use este checklist para organizar seu trabalho:

**Fase 1: Planejamento (15 min)**
- [ ] Desenhar hierarquia no papel
- [ ] Marcar cada método como Abstrato (A) ou Concreto (C)
- [ ] Esboçar o Template Method `processar()`
- [ ] Decidir tipos de retorno e modificadores de acesso

**Fase 2: Superclasse Abstrata e Enum (25 min)**
- [ ] Criar enum `StatusPagamento`
- [ ] Criar `Pagamento` como `abstract class`
- [ ] Implementar atributos e construtor
- [ ] Implementar Template Method `processar()` como `public final`
- [ ] Declarar métodos abstratos
- [ ] Implementar métodos concretos (`registrarTransacao`, `notificarCliente`)

**Fase 3: Subclasses Concretas (35 min)**
- [ ] `PagamentoCartao` — implementar todos os abstratos
- [ ] `PagamentoPIX` — implementar todos os abstratos
- [ ] `PagamentoBoleto` — implementar todos os abstratos

**Fase 4: Gerenciamento (20 min)**
- [ ] Criar `GerenciadorPagamentos`
- [ ] Implementar `processar()`, `listarTodos()`, `exibirEstatisticas()`, filtros, `maiorValor()`

**Fase 5: Teste e Apresentação (20 min)**
- [ ] Criar `SistemaPagamentos` com `main`
- [ ] Executar pagamentos variados (válidos e inválidos)
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
   - Por que `processar()` é `final`?
   - Onde está o polimorfismo dentro do Template Method?
   - Como adicionaria um pagamento por Débito Automático?
   - Por que `registrarTransacao()` é concreto e `calcularTaxa()` é abstrato?

3. **Discussão coletiva** (10 min no final)
   - Comparar diferentes abordagens
   - Destacar usos criativos do Template Method
   - Discutir trade-offs (sobrescrever concretos vs criar novo abstrato)

---

## Resumo do Bloco 3

Neste bloco você:

✅ Aplicou classes abstratas e Template Method de forma **independente**
✅ Modelou um **domínio realista** (processamento de pagamentos)
✅ Implementou **dispatch dinâmico** dentro de um Template Method
✅ Demonstrou que classes abstratas + polimorfismo são **complementares**
✅ Trabalhou com diferentes regras de validação, cálculo de taxa e autorização
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
