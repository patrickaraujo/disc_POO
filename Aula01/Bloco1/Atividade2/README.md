# 🧠 Atividade 2 — Por que esse código não escala?

> **Duração:** 15–20 minutos  
> **Formato:** Discussão guiada em grupo  
> **Objetivo:** Compreender por que código procedural tem limitações e por que a Orientação a Objetos é necessária

---

## 📋 Contexto da Atividade

Imagine que o código a seguir faz parte de um **sistema bancário simples**.  
Ele permite realizar operações básicas: **sacar**, **depositar** e **consultar saldo**.

⚠️ **Importante:** O código funciona corretamente! A questão não é se ele funciona, mas sim como ele está organizado.

---

## 💻 Código de Exemplo (Procedural)

```java
double saldo = 1000.0;

void sacar(double valor) {
    if (valor <= saldo) {
        saldo = saldo - valor;
        System.out.println("Saque realizado");
    } else {
        System.out.println("Saldo insuficiente");
    }
}

void depositar(double valor) {
    saldo = saldo + valor;
    System.out.println("Depósito realizado");
}

void imprimirSaldo() {
    System.out.println("Saldo atual: " + saldo);
}
```

📌 **Observe:** Esse código funciona. O sistema faz exatamente o que se espera dele.

---

## 🔍 Análise Crítica — Perguntas para Reflexão

### 1️⃣ Onde estão os dados? (Estado)

**Pergunta:**  
Onde estão armazenados os dados deste sistema?

**Resposta:**
- O `saldo` está em uma **variável solta**
- Qualquer função pode acessá-la diretamente
- Não há proteção ou controle de acesso

**📌 Conclusão:**  
Os dados não pertencem a nada específico. Eles estão "soltos" no código.

---

### 2️⃣ Onde estão as regras do sistema?

**Primeiro, entenda o que são "regras":**

> **Regras** são as condições e decisões que controlam o comportamento do sistema.  
> Elas dizem **quando** algo pode ou não acontecer.

**Exemplos de regras neste sistema:**
- ✅ Só pode sacar se houver saldo suficiente
- ✅ O saldo deve ser atualizado corretamente após cada operação
- ✅ Toda operação deve alterar o mesmo saldo

**Pergunta:**  
Onde essas regras estão implementadas?

**Resposta:**
- Espalhadas dentro das funções
- Misturadas com impressão de mensagens
- Dependentes de uma variável global

**📌 Conclusão:**  
As regras existem, mas estão **espalhadas** e **sem dono**. Não há um lugar claro onde elas "vivem".

---

### 3️⃣ Se outro programador mexer aqui, o que pode dar errado?

**Pergunta:**  
Imagine que outro programador precise alterar ou estender esse código.  
O que pode dar errado?

**Problemas possíveis:**
- ❌ Ele pode alterar o `saldo` diretamente, sem validação
- ❌ Pode esquecer de validar regras importantes
- ❌ Pode duplicar lógica em outros lugares
- ❌ Pode criar funções que quebram o estado do sistema
- ❌ Pode não saber onde mexer quando surgir um bug

**📌 Conclusão:**  
Nada impede o uso incorreto do `saldo`. Não há proteção.

---

### 4️⃣ Se o sistema crescer, isso melhora ou piora?

**Cenário proposto:**

Agora imagine que o sistema precise:
- Ter **várias contas** diferentes
- Ter **diferentes tipos de conta** (corrente, poupança, empresarial)
- Registrar **histórico de operações**

**Pergunta:**  
Esse código escala bem?

**Resposta:**
- ❌ **Piora significativamente**
- Muito `if` para controlar cada tipo de conta
- Mais variáveis globais (uma para cada conta?)
- Código cada vez mais difícil de entender e manter
- Alto risco de bugs ao fazer alterações

**📌 Conclusão:**  
Quanto maior o sistema, maior a bagunça. A organização não escala.

---

## 📊 Síntese do Problema

| Aspecto | Status |
|---------|--------|
| Código funciona | ✅ |
| Lógica está correta | ✅ |
| Organização é boa | ❌ |
| Regras estão bem localizadas | ❌ |
| Manutenção é segura | ❌ |
| Código é escalável | ❌ |

### 💡 Frase-chave:

> **"O problema não é a lógica. É a estrutura."**

---

## 🧩 Conexão com Orientação a Objetos

Sem mostrar código ainda, pense nessas questões:

🤔 **E se o saldo pertencesse à conta?**  
🤔 **E se só a conta pudesse mudar seu próprio saldo?**  
🤔 **E se as regras ficassem dentro do objeto conta?**

### ✨ A solução

**Orientação a Objetos surge para organizar dados e regras no mesmo lugar.**

Em vez de ter:
- Dados soltos + Funções soltas

Teríamos:
- **Objetos** que contêm seus próprios dados e comportamentos

---

## 📝 Questões para Discussão

1. **Quais outros problemas você identifica neste código?**

2. **Como você organizaria múltiplas contas bancárias usando esse estilo procedural?**

3. **O que aconteceria se você precisasse adicionar novos tipos de operações (transferência, investimento, etc.)?**

4. **Por que variáveis globais são problemáticas em sistemas grandes?**

5. **Você consegue imaginar uma forma de organizar melhor esse código?**

---

## ✅ O que você deve ter aprendido

Ao final desta atividade, você deve ser capaz de:

- ✅ Identificar que **código funcional ≠ código bem organizado**
- ✅ Reconhecer o conceito de **regras de negócio**
- ✅ Perceber os **riscos do código procedural** em sistemas grandes
- ✅ Entender **por que a Orientação a Objetos existe**
- ✅ Justificar a necessidade de uma melhor organização de código

---

## 💭 Reflexão Final

> **"Orientação a Objetos não surgiu por moda.  
> Surgiu por necessidade."**

O código procedural funciona bem para programas pequenos, mas quando o sistema cresce, a falta de organização se torna um problema sério.

**OO é a solução para organizar sistemas complexos de forma sustentável.**
