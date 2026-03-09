# Exercícios — Bloco 4 (Aula 04)

## Como usar este material

Estes exercícios consolidam todos os conceitos da Aula 04. Tente resolver **sem olhar o gabarito**. O objetivo é treinar o padrão completo de encapsulamento: `private`, getters, setters com validação e métodos de negócio.

Arquivos de gabarito estão em [`gabarito.md`](./gabarito.md) e os códigos-fonte em [`codigo-fonte/`](./codigo-fonte/).

---

## Exercício 01 — Classe Estacionamento ⭐⭐

### Contexto

Você está desenvolvendo o sistema de um estacionamento. O estacionamento tem um nome, um número fixo de vagas e precisa controlar quantas vagas estão ocupadas. Não é possível entrar mais carros do que o total de vagas, nem sair carros se o estacionamento está vazio.

### O que você deve implementar

**Classe `Estacionamento`** com:

- Atributos (todos `private`): `nome` (String), `totalVagas` (int), `vagasOcupadas` (int)
- Getters para todos os atributos + getter calculado `getVagasDisponiveis()` que retorna `totalVagas - vagasOcupadas`
- `setNome(String nome)` → não aceitar vazio ou null
- `setTotalVagas(int total)` → só aceitar se o valor atual for 0 (primeira definição) e se total > 0
- `entrarVeiculo()` → incrementa `vagasOcupadas` se houver vaga; senão imprime "Estacionamento lotado"
- `sairVeiculo()` → decrementa `vagasOcupadas` se houver veículo; senão imprime "Estacionamento vazio"
- `exibirStatus()` → imprime nome, vagas totais, ocupadas e disponíveis

**Classe `EstacionamentoMain`** com o `main`:

- Crie um estacionamento com 3 vagas
- Entre 3 veículos (lotando)
- Tente entrar um 4º → deve ser recusado
- Saia 1 veículo
- Exiba o status final

### Saída esperada

```
Veículo entrou. Vagas ocupadas: 1
Veículo entrou. Vagas ocupadas: 2
Veículo entrou. Vagas ocupadas: 3
Estacionamento lotado.
Veículo saiu. Vagas ocupadas: 2
=== Estacionamento Central ===
Total de vagas: 3
Vagas ocupadas: 2
Vagas disponíveis: 1
```

---

## Exercício 02 — Classe ContaPoupanca ⭐⭐

### Contexto

Você está desenvolvendo o módulo de poupança de um banco. Cada conta poupança tem titular, saldo e uma taxa de rendimento mensal. O saldo só pode ser alterado por depósitos, saques e aplicação de rendimento — nunca diretamente.

### O que você deve implementar

**Classe `ContaPoupanca`** com:

- Atributos (todos `private`): `titular` (String), `saldo` (double), `taxaRendimentoMensal` (double)
- Getters para todos os atributos (sem setter para `saldo`)
- `setTitular(String titular)` → não aceitar vazio ou null
- `setTaxaRendimentoMensal(double taxa)` → aceitar somente entre 0 e 1
- `depositar(double valor)` → adiciona ao saldo se valor > 0
- `sacar(double valor)` → retira se valor > 0 e saldo suficiente
- `aplicarRendimento()` → calcula `saldo * taxaRendimentoMensal` e soma ao saldo
- `exibirExtrato()` → imprime titular, saldo e taxa

**Classe `ContaPoupancaMain`** com o `main`:

- Crie uma conta com R$ 1000 e taxa 0.005 (0,5%)
- Aplique rendimento 3 vezes (simulando 3 meses)
- Exiba o extrato após cada aplicação

### Saída esperada (valores aproximados)

```
Rendimento aplicado: R$ 5.0 | Novo saldo: R$ 1005.0
=== Extrato ===
Titular: Ana
Saldo: R$ 1005.0
Taxa mensal: 0.5%

Rendimento aplicado: R$ 5.025 | Novo saldo: R$ 1010.025
=== Extrato ===
Titular: Ana
Saldo: R$ 1010.025
Taxa mensal: 0.5%

Rendimento aplicado: R$ 5.050125 | Novo saldo: R$ 1015.075125
=== Extrato ===
Titular: Ana
Saldo: R$ 1015.075125
Taxa mensal: 0.5%
```

---

## Checklist de entrega

Antes de considerar os exercícios concluídos, verifique:

- [ ] Todos os atributos são `private`
- [ ] Getters e setters seguem a convenção `getNome()` / `setNome()`
- [ ] Setters possuem validação adequada
- [ ] Não existem setters para atributos que não devem ser alterados diretamente
- [ ] Métodos de negócio operam sobre o estado do próprio objeto
- [ ] O código compila sem erros (`javac *.java`)
- [ ] A saída no console demonstra as validações funcionando
