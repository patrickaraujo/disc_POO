# Exercícios — Bloco 4 (Aula 05)

## Como usar este material

Estes exercícios consolidam todos os conceitos da Aula 05. Tente resolver **sem olhar o gabarito**. O objetivo é treinar o padrão completo: construtores com validação, `this` e `this()`, sobrecarga de métodos e construtores.

Arquivos de gabarito estão em [`gabarito.md`](./gabarito.md) e os códigos-fonte em [`codigo-fonte/`](./codigo-fonte/).

---

## Exercício 01 — Classe Pedido ⭐⭐

### Contexto

Você está desenvolvendo o sistema de um restaurante. Cada pedido tem um número, o nome do cliente, a quantidade de itens e o valor total. O sistema deve permitir adicionar itens de formas diferentes: um por vez ou em quantidade.

### O que você deve implementar

**Classe `Pedido`** com:

- Atributos (todos `private`): `numero` (int), `nomeCliente` (String), `totalItens` (int), `valorTotal` (double)
- Construtor: `Pedido(int numero, String nomeCliente)` com validação (número <= 0 → 1; nome vazio → "Cliente")
- `adicionarItem(double preco)` → adiciona 1 item com o preço informado (preco > 0)
- `adicionarItem(double preco, int quantidade)` → adiciona múltiplos itens (preco > 0, quantidade > 0)
- `exibirResumo()` → imprime número, cliente, total de itens e valor total

**Classe `PedidoMain`** com o `main`:

- Crie um pedido
- Adicione 1 item de R$ 25.00
- Adicione 3 itens de R$ 12.50
- Tente adicionar item com preço negativo → deve ser recusado
- Exiba o resumo

### Saída esperada

```
Item adicionado: R$ 25.0 | Itens: 1 | Total: R$ 25.0
3 itens adicionados: R$ 12.5 cada | Itens: 4 | Total: R$ 62.5
Erro: preço deve ser positivo.
=== Pedido #1 ===
Cliente: Ana
Itens: 4
Valor total: R$ 62.5
```

---

## Exercício 02 — Classe ContaPoupanca ⭐⭐

### Contexto

Você está desenvolvendo o módulo de poupança de um banco, agora com construtores que facilitam a criação de contas com diferentes níveis de informação. O titular é sempre obrigatório, mas o saldo inicial e a taxa de rendimento podem ter valores padrão.

### O que você deve implementar

**Classe `ContaPoupanca`** com:

- Atributos (todos `private`): `titular` (String), `saldo` (double), `taxaRendimentoMensal` (double)
- Construtor completo: `ContaPoupanca(String titular, double saldoInicial, double taxaRendimentoMensal)`
  - Validações: titular vazio → "Sem nome"; saldo negativo → 0; taxa fora de 0–1 → 0.005
- Construtor parcial: `ContaPoupanca(String titular, double saldoInicial)` → taxa padrão 0.005
- Construtor mínimo: `ContaPoupanca(String titular)` → saldo 0.0, taxa padrão 0.005
- Getters para todos os atributos (sem setter para `saldo`)
- `setTitular(String titular)` → não aceitar vazio
- `setTaxaRendimentoMensal(double taxa)` → aceitar somente entre 0 e 1
- `depositar(double valor)` → adiciona ao saldo se valor > 0
- `sacar(double valor)` → retira se valor > 0 e saldo suficiente
- `aplicarRendimento()` → calcula `saldo * taxaRendimentoMensal` e soma ao saldo
- `exibirExtrato()` → imprime titular, saldo e taxa

**Classe `ContaPoupancaMain`** com o `main`:

- Crie conta1 com todos os dados: "Ana", 1000.0, 0.01
- Crie conta2 com nome e saldo: "Carlos", 500.0 (taxa padrão)
- Crie conta3 só com nome: "Maria" (saldo 0, taxa padrão)
- Deposite R$ 200 na conta3
- Aplique rendimento em todas
- Exiba o extrato de todas

### Saída esperada (valores aproximados)

```
Rendimento aplicado: R$ 10.0 | Novo saldo: R$ 1010.0
Rendimento aplicado: R$ 2.5 | Novo saldo: R$ 502.5
Depósito de R$ 200.0 realizado. Saldo: R$ 200.0
Rendimento aplicado: R$ 1.0 | Novo saldo: R$ 201.0
=== Extrato ===
Titular: Ana
Saldo: R$ 1010.0
Taxa mensal: 1.0%

=== Extrato ===
Titular: Carlos
Saldo: R$ 502.5
Taxa mensal: 0.5%

=== Extrato ===
Titular: Maria
Saldo: R$ 201.0
Taxa mensal: 0.5%
```

---

## Checklist de entrega

Antes de considerar os exercícios concluídos, verifique:

- [ ] Todos os atributos são `private`
- [ ] Construtores validam os dados e garantem estado inicial válido
- [ ] Construtores parciais usam `this()` para delegar ao completo
- [ ] Métodos sobrecarregados diferem nos parâmetros (não apenas no nome)
- [ ] Getters e setters seguem a convenção `getNome()` / `setNome()`
- [ ] `this.atributo` é usado sempre que parâmetro e atributo têm o mesmo nome
- [ ] O código compila sem erros (`javac *.java`)
- [ ] A saída no console demonstra construtores e sobrecarga funcionando
