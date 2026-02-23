# Exercícios — Bloco 3 (Aula 03)

## Como usar este material

Leia o enunciado de cada exercício, tente resolver **sem olhar o gabarito**, e só depois compare com a solução. O objetivo não é acertar de primeira — é treinar o raciocínio de modelagem OO.

Arquivos de gabarito estão em [`gabarito.md`](./gabarito.md) e os códigos-fonte em [`codigo-fonte/`](./codigo-fonte/).

---

## Exercício 01 — Classe Aluno ⭐

### Contexto

Você foi contratado para desenvolver o módulo de notas de um sistema escolar. Cada aluno tem nome, matrícula e duas notas. O sistema precisa calcular a média e informar se o aluno está aprovado ou reprovado.

### O que você deve implementar

**Classe `Aluno`** com:

- Atributos: `nome` (String), `matricula` (String), `nota1` (double), `nota2` (double)
- Método `calcularMedia()` → retorna a média das duas notas (tipo `double`)
- Método `exibirSituacao()` → imprime "Aprovado" se a média for `>= 6.0`, ou "Reprovado" caso contrário

**Classe `AlunoMain`** com o `main`:

- Crie 3 objetos `Aluno` com notas diferentes (um aprovado, um reprovado, um na fronteira com média exatamente 6.0)
- Chame `exibirSituacao()` para cada um

### Saída esperada (valores de exemplo)

```
Aluno: Maria | Média: 7.5 | Aprovado
Aluno: João  | Média: 4.0 | Reprovado
Aluno: Paula | Média: 6.0 | Aprovado
```

### Dicas

- `calcularMedia()` retorna um valor — use `return (nota1 + nota2) / 2;`
- Em `exibirSituacao()`, você pode chamar `calcularMedia()` internamente

---

## Exercício 02 — Classe Produto ⭐

### Contexto

Uma pequena loja precisa controlar seus produtos. Cada produto tem nome, preço e quantidade em estoque. O sistema deve permitir aplicar descontos e repor o estoque.

### O que você deve implementar

**Classe `Produto`** com:

- Atributos: `nome` (String), `preco` (double), `quantidadeEstoque` (int)
- Método `exibirInformacoes()` → imprime nome, preço e estoque
- Método `aplicarDesconto(double percentual)` → reduz o preço pelo percentual informado. Se o percentual for `<= 0` ou `>= 100`, exibir mensagem de erro
- Método `reporEstoque(int quantidade)` → adiciona ao estoque. Se a quantidade for `<= 0`, exibir mensagem de erro

**Classe `ProdutoMain`** com o `main`:

- Crie 2 produtos
- Exiba as informações iniciais
- Aplique desconto em um deles e reponha o estoque do outro
- Exiba as informações finais de ambos

### Dica

Operação de desconto: `preco = preco - (preco * percentual / 100);`

---

## Exercício 03 — Agência Bancária ⭐⭐

### Contexto

Use a classe `ContaBancaria` implementada no Bloco 3 e simule uma pequena agência com 3 clientes. Não é necessário criar uma nova classe — apenas escreva um `main` mais completo.

### O que você deve implementar

**Classe `AgenciaMain`** com o `main`:

- Crie 3 contas: uma com saldo inicial alto, uma com saldo médio e uma zerada
- Realize as seguintes operações em sequência:
  1. Conta 1 realiza um depósito
  2. Conta 1 transfere valor para a Conta 2 (saque em uma, depósito em outra)
  3. Conta 3 tenta sacar (deve falhar por saldo zero)
  4. Conta 3 recebe um depósito
- Ao final, exiba o saldo de todas as três contas

> **Atenção:** Java não tem um método "transferir" ainda — simule isso com um `sacar` seguido de um `depositar`.

### Desafio extra (opcional)

Modifique a classe `ContaBancaria` para adicionar um método `transferir(ContaBancaria destino, double valor)` que faz o saque na conta de origem e o depósito na conta de destino. Qual seria a assinatura desse método?

---

## Exercício 04 — Modelagem Livre ⭐⭐

### Contexto

Escolha um contexto que faça sentido para você:

> **Opções:** Academia | Estacionamento | Restaurante | Clínica veterinária | Biblioteca | Outro à sua escolha

### O que você deve fazer

**Etapa 1 — Modelagem (sem código):**

Identifique uma classe do contexto escolhido e preencha:

| Campo | Sua resposta |
|---|---|
| Nome da classe | |
| Atributo 1 (nome e tipo) | |
| Atributo 2 (nome e tipo) | |
| Atributo 3 (nome e tipo) | |
| Método 1 (nome e o que faz) | |
| Método 2 (nome e o que faz) | |

**Etapa 2 — Implementação:**

Implemente a classe modelada e uma classe `Main` que crie pelo menos 2 objetos e exercite todos os métodos.

### Critério de avaliação

- Os métodos operam sobre os atributos da própria classe (não recebem tudo por parâmetro)
- Há pelo menos uma validação em algum método (ex.: valor não pode ser negativo)
- Os dois objetos demonstram independência de estado

---

## Checklist de entrega

Antes de considerar os exercícios concluídos, verifique:

- [ ] Cada classe está em seu próprio arquivo `.java`
- [ ] Os nomes dos arquivos são idênticos aos nomes das classes
- [ ] Os objetos foram criados com `new`
- [ ] Os métodos operam sobre os atributos do objeto (não recalculam tudo por parâmetro)
- [ ] O código compila sem erros (`javac *.java`)
- [ ] A saída no console faz sentido para o problema modelado
