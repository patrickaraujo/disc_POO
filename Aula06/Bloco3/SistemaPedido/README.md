# Sistema de Pedidos

Projeto Java do exemplo teórico de **Composição** da **Aula 06 — Bloco 3**.

Este sistema demonstra o relacionamento mais forte entre classes: a **composição** (◆). Um `ItemPedido` só faz sentido dentro de um `Pedido` — se o pedido deixa de existir, seus itens também deixam.

---

## Relacionamento modelado

| Relacionamento | Símbolo UML | No código |
|---|:-:|---|
| **Composição** | ◆ | `Pedido` ◆ `ItemPedido` (array `itens[]`, criados dentro de `adicionarItem`) |

```
┌──────────────────┐
│     Pedido       │
├──────────────────┤
│ - numero         │
│ - cliente        │
│ - data           │
│ - itens[]        │◆────────┐  Losango CHEIO = Composição
└──────────────────┘          │
       1                      1..*
                              │
                      ┌───────▼────────┐
                      │  ItemPedido    │
                      ├────────────────┤
                      │ - produto      │
                      │ - quantidade   │
                      │ - precoUnitario│
                      └────────────────┘
```

---

## Estrutura do projeto

```
SistemaPedido/
└── src/
    ├── ItemPedido.java    (parte da composição, construtor package-private)
    ├── Pedido.java        (o todo, cria os itens internamente)
    └── PedidoMain.java    (classe principal de teste)
```

---

## Como compilar e executar

Na pasta `src/`:

```bash
javac *.java
java PedidoMain
```

Requer **Java 8 ou superior**.

---

## O que o programa demonstra

1. **Criação de um pedido** com capacidade de 10 itens
2. **Adição de itens** — cada `ItemPedido` é criado **dentro** do método `adicionarItem()`
3. **Exibição formatada** do pedido com subtotais e total
4. **Remoção de item** — na composição, remover = destruir a parte
5. **Tentativa de remoção** de item inexistente (validação)
6. **Limite de capacidade** — teste estourando o array
7. **Múltiplos pedidos** — cada pedido tem seus **próprios** `ItemPedido`, mesmo que o nome do produto se repita
8. **Descarte com `null`** + sugestão ao GC — demonstra que itens ficam inalcançáveis quando o pedido é descartado

---

## Por que isso é composição e não agregação?

### Quatro critérios atendidos

1. **O item é criado dentro do pedido** (não recebido pronto)
   ```java
   // Dentro de Pedido.adicionarItem:
   itens[qtdItens] = new ItemPedido(produto, quantidade, precoUnitario);
   ```

2. **O construtor de `ItemPedido` é package-private** — impede que código externo crie itens soltos:
   ```java
   // Não tem `public` antes!
   ItemPedido(String produto, int quantidade, double precoUnitario) { ... }
   ```

3. **Um `ItemPedido` não faz sentido fora de um `Pedido`** — o conceito "2 unidades de Mouse Gamer" é uma **linha de pedido**, não uma entidade independente.

4. **O ciclo de vida é dependente** — se o pedido some, os itens somem junto (ficam inalcançáveis e são coletados pelo garbage collector).

### Pergunta-chave

> **"Se eu deletar o pedido, os itens também devem sumir?"**
> → SIM → é **composição** (◆)

---

## Pontos didáticos para discutir em sala

1. **Por que o construtor de `ItemPedido` não é `public`?**
   → Para que, fora do pacote, **ninguém consiga fazer `new ItemPedido(...)`**. Só a classe `Pedido` (no mesmo pacote) pode criar itens. Isso implementa a composição no nível da linguagem.

2. **O que aconteceria se `adicionarItem` recebesse um `ItemPedido` já pronto?**
   → Deixaria de ser composição e viraria **agregação** — pois a "parte" teria sido criada fora do "todo".

3. **Os dois pedidos com "Mouse Gamer" compartilham o mesmo objeto?**
   → Não. Cada pedido tem seu **próprio** `ItemPedido`. Composição implica "cada todo tem suas próprias partes".

4. **Por que não existe um `getItens()` que retorne o array?**
   → Para evitar que código externo modifique ou anule os itens por fora, violando a composição. Expomos só métodos derivados como `calcularTotal()` e `exibirPedido()`.

5. **Comparação com a agregação `Time ◇ Jogador` do Bloco 2:**
   - Um jogador pode existir sem time e mudar de time → agregação
   - Um item de pedido **não existe** fora do pedido que o criou → composição
