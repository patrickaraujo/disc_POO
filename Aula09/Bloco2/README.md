# Bloco 2 — Exercício Guiado: Sistema de Notificações

## Objetivos do Bloco

- Aplicar classes abstratas em um sistema completo
- Implementar o **Template Method Pattern** em contexto realista
- Misturar **métodos abstratos** + **métodos concretos**
- Usar `ArrayList<Notificacao>` como coleção polimórfica de tipo abstrato
- Demonstrar a **proibição de instanciação** ao vivo
- Mostrar como adicionar **novo canal** sem alterar código existente

---

## 🎯 Problema: Central de Notificações Multi-canal

A empresa **TechBR** precisa enviar notificações aos seus clientes por **três canais diferentes**: SMS, E-mail e Push (notificação no app).

### Canais Suportados

- **SMS:** mensagem de texto via celular (limite de 160 caracteres)
- **E-mail:** mensagem com assunto e corpo
- **Push:** notificação dentro do app (com título e corpo)

### Características Comuns

Toda notificação tem:
- Identificador (ID)
- Destinatário (telefone, e-mail ou ID do dispositivo)
- Conteúdo da mensagem
- Data e hora de envio
- Status (PENDENTE, ENVIADO, FALHOU)

### Fluxo de Envio (igual para todos)

Independente do canal, **toda notificação segue este fluxo**:

```
1. VALIDAR destinatário (formato correto?)
2. FORMATAR mensagem (cada canal tem suas regras)
3. TRANSMITIR pelo canal específico
4. REGISTRAR log da operação
```

> Se a validação falhar ou a transmissão não for bem-sucedida, o status é marcado como FALHOU e o log é registrado da mesma forma.

### Regras Específicas

- **SMS:** destinatário deve ter 11 dígitos. Mensagem **truncada** em 160 caracteres.
- **E-mail:** destinatário deve conter `@`. Mensagem inclui assunto + corpo.
- **Push:** destinatário deve começar com `device_`. Mensagem inclui título + corpo.

---

## Planejamento da Hierarquia

```
                 ┌────────────────────────────────────┐
                 │    « abstract » Notificacao        │  ← SUPERCLASSE
                 ├────────────────────────────────────┤
                 │ # id                               │
                 │ # destinatario                     │
                 │ # mensagem                         │
                 │ # dataEnvio                        │
                 │ # status                           │
                 ├────────────────────────────────────┤
                 │ + enviar() : final  ◄─── TEMPLATE METHOD
                 │ # validarDestinatario() : abstract │
                 │ # formatarMensagem() : abstract    │
                 │ # transmitir() : abstract          │
                 │ # registrarLog() : concreto        │
                 └─────────────┬──────────────────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
  ┌─────▼─────────┐    ┌───────▼─────────┐    ┌───────▼─────────┐
  │NotificacaoSMS │    │NotificacaoEmail │    │NotificacaoPush  │
  └───────────────┘    └─────────────────┘    └─────────────────┘
```

**Decisões de design:**
- ✅ `Notificacao` é **abstrata** — não existe "notificação genérica"
- ✅ `enviar()` é o **Template Method** (concreto, **`final`**)
- ✅ `validarDestinatario()`, `formatarMensagem()` e `transmitir()` são **abstratos**
- ✅ `registrarLog()` é **concreto** (igual para todos)
- ✅ Atributos comuns são `protected`

---

## Passo 1 — Criar o Enum `StatusEnvio`

**O que vai aqui?** Estados possíveis de uma notificação.

```java
public enum StatusEnvio {
    PENDENTE,
    ENVIADO,
    FALHOU
}
```

**Discussão:**
- Por que enum? Porque os valores são fixos e mutuamente exclusivos.
- Alternativa seria `String`, mas perderia segurança de tipo.

---

## Passo 2 — Criar a Classe Abstrata `Notificacao`

**O que vai aqui?** Tudo que é **comum** + **Template Method** + **promessas abstratas**.

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Notificacao {

    // Atributos comuns
    protected String id;
    protected String destinatario;
    protected String mensagem;
    protected LocalDateTime dataEnvio;
    protected StatusEnvio status;

    public Notificacao(String id, String destinatario, String mensagem) {
        this.id = id;
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.status = StatusEnvio.PENDENTE;
    }

    // ┌─────────────────────────────────────────────────────┐
    // │  TEMPLATE METHOD                                    │
    // │  Define o esqueleto do algoritmo de envio.          │
    // │  É 'final' — subclasses NÃO podem alterar a ordem.  │
    // └─────────────────────────────────────────────────────┘
    public final void enviar() {
        System.out.println("\n>>> Enviando " + id + " (" + getTipoCanal() + ")");

        // 1. Validação (abstrato)
        if (!validarDestinatario()) {
            System.out.println("    ✗ Destinatário inválido: " + destinatario);
            status = StatusEnvio.FALHOU;
            registrarLog();
            return;
        }

        // 2. Formatação (abstrato)
        String mensagemFormatada = formatarMensagem();

        // 3. Transmissão (abstrato)
        boolean sucesso = transmitir(mensagemFormatada);

        if (sucesso) {
            status = StatusEnvio.ENVIADO;
            dataEnvio = LocalDateTime.now();
        } else {
            status = StatusEnvio.FALHOU;
        }

        // 4. Log (concreto — comum a todos)
        registrarLog();
    }

    // ┌─────────────────────────────────────────────────────┐
    // │  MÉTODOS ABSTRATOS — pontos de variação             │
    // │  Cada subclasse implementa do seu jeito.            │
    // └─────────────────────────────────────────────────────┘
    protected abstract boolean validarDestinatario();
    protected abstract String formatarMensagem();
    protected abstract boolean transmitir(String mensagemFormatada);
    public abstract String getTipoCanal();

    // ┌─────────────────────────────────────────────────────┐
    // │  MÉTODO CONCRETO — comum a todos                    │
    // └─────────────────────────────────────────────────────┘
    protected void registrarLog() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataStr = (dataEnvio != null) ? dataEnvio.format(fmt) : "—";

        System.out.println("    [LOG] " + id + " | " + getTipoCanal()
                + " | Status: " + status
                + " | Data: " + dataStr);
    }

    // Getters
    public String getId() { return id; }
    public String getDestinatario() { return destinatario; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public StatusEnvio getStatus() { return status; }
}
```

**Discussão importante com os alunos:**
- **Por que `enviar()` é `final`?** Para que subclasses não possam alterar a ordem dos passos. Isso protege a integridade do fluxo.
- **Por que `validarDestinatario()`, `formatarMensagem()` e `transmitir()` são abstratos?** Porque cada canal valida, formata e transmite de forma diferente.
- **Por que `registrarLog()` é concreto?** Porque o log é igual para todos — não precisa variar por canal.
- **Por que `getTipoCanal()` é abstrato?** Para cada subclasse fornecer seu próprio nome amigável (será usado no log e na exibição).

> 💡 Note como o `enviar()` é **um único algoritmo** que funciona para **qualquer** tipo de notificação. Esse é o coração do Template Method.

---

## Passo 3 — Implementar `NotificacaoSMS`

```java
public class NotificacaoSMS extends Notificacao {

    private static final int LIMITE_CARACTERES = 160;

    public NotificacaoSMS(String id, String destinatario, String mensagem) {
        super(id, destinatario, mensagem);
    }

    @Override
    public String getTipoCanal() {
        return "SMS";
    }

    @Override
    protected boolean validarDestinatario() {
        // Telefone deve ter 11 dígitos (DDD + número, ex: 11987654321)
        if (destinatario == null) return false;
        String apenasDigitos = destinatario.replaceAll("\\D", "");
        return apenasDigitos.length() == 11;
    }

    @Override
    protected String formatarMensagem() {
        // Truncar em 160 caracteres
        if (mensagem.length() <= LIMITE_CARACTERES) {
            return mensagem;
        }
        return mensagem.substring(0, LIMITE_CARACTERES - 3) + "...";
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    📱 Enviando SMS para " + destinatario);
        System.out.println("    Conteúdo: \"" + mensagemFormatada + "\"");

        // Simulação: 90% de sucesso
        boolean sucesso = Math.random() < 0.9;
        if (sucesso) {
            System.out.println("    ✓ SMS entregue pela operadora.");
        } else {
            System.out.println("    ✗ Operadora indisponível.");
        }
        return sucesso;
    }
}
```

**Discussão:**
- Note que **não há corpo** para `enviar()` — ele é herdado da abstrata.
- Cada método sobrescrito tem **`@Override`** (boa prática + verificação).
- A formatação trunca a mensagem se passar do limite (regra específica do SMS).

---

## Passo 4 — Implementar `NotificacaoEmail`

```java
public class NotificacaoEmail extends Notificacao {

    private String assunto;

    public NotificacaoEmail(String id, String destinatario, String assunto, String mensagem) {
        super(id, destinatario, mensagem);
        this.assunto = assunto;
    }

    public String getAssunto() { return assunto; }

    @Override
    public String getTipoCanal() {
        return "E-mail";
    }

    @Override
    protected boolean validarDestinatario() {
        // Validação simplificada: contém @ e tem ao menos um ponto após
        if (destinatario == null) return false;
        int posArroba = destinatario.indexOf('@');
        if (posArroba < 1) return false;
        return destinatario.indexOf('.', posArroba) > posArroba;
    }

    @Override
    protected String formatarMensagem() {
        StringBuilder sb = new StringBuilder();
        sb.append("De: nao-responder@techbr.com\n");
        sb.append("Para: ").append(destinatario).append("\n");
        sb.append("Assunto: ").append(assunto).append("\n\n");
        sb.append(mensagem);
        sb.append("\n\n--\nEnviado pela TechBR");
        return sb.toString();
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    ✉️  Enviando e-mail para " + destinatario);
        System.out.println("    Assunto: \"" + assunto + "\"");

        // Simulação: 95% de sucesso
        boolean sucesso = Math.random() < 0.95;
        if (sucesso) {
            System.out.println("    ✓ E-mail entregue ao servidor SMTP.");
        } else {
            System.out.println("    ✗ Servidor SMTP rejeitou.");
        }
        return sucesso;
    }
}
```

---

## Passo 5 — Implementar `NotificacaoPush`

```java
public class NotificacaoPush extends Notificacao {

    private String titulo;

    public NotificacaoPush(String id, String destinatario, String titulo, String mensagem) {
        super(id, destinatario, mensagem);
        this.titulo = titulo;
    }

    @Override
    public String getTipoCanal() {
        return "Push";
    }

    @Override
    protected boolean validarDestinatario() {
        // Token de dispositivo: começa com "device_" e tem mais de 10 caracteres
        return destinatario != null
            && destinatario.startsWith("device_")
            && destinatario.length() > 10;
    }

    @Override
    protected String formatarMensagem() {
        return "{ \"titulo\": \"" + titulo + "\", \"corpo\": \"" + mensagem + "\" }";
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    🔔 Enviando push para " + destinatario);
        System.out.println("    Payload: " + mensagemFormatada);

        // Simulação: 85% de sucesso
        boolean sucesso = Math.random() < 0.85;
        if (sucesso) {
            System.out.println("    ✓ Push entregue ao FCM.");
        } else {
            System.out.println("    ✗ Token de dispositivo expirado.");
        }
        return sucesso;
    }
}
```

**Discussão importante com os alunos:**
- Cada subclasse tem **suas próprias regras** de validação, formatação e transmissão.
- Mas **todas seguem o mesmo fluxo** (definido em `enviar()`).
- O Template Method permite essa **uniformidade no fluxo + diversidade nos passos**.

---

## Passo 6 — Classe Principal com Coleção Polimórfica

Não precisamos de uma classe gerenciadora extra: o próprio `main` mantém o histórico em um **`ArrayList<Notificacao>`**. A chave do polimorfismo aqui é que **`Notificacao` é abstrata, mas pode ser usada como tipo de referência** — a lista aceita qualquer subclasse concreta.

```java
import java.util.ArrayList;

public class SistemaNotificacoes {
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║      CENTRAL DE NOTIFICAÇÕES - TechBR             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        // ★ ArrayList polimórfico de tipo abstrato ★
        // Aceita qualquer subclasse concreta de Notificacao
        ArrayList<Notificacao> historico = new ArrayList<>();

        // ❌ Tentativa de instanciar a abstrata (deixe COMENTADO):
        // Notificacao n = new Notificacao("X", "y", "z");
        //                                             ^^^^^
        //                  Erro de compilação: Notificacao is abstract

        // SMS — destinatário válido
        Notificacao n1 = new NotificacaoSMS(
            "N001",
            "11987654321",
            "Seu pedido #4521 foi enviado e chega amanhã!");
        historico.add(n1);
        n1.enviar();  // ← Template Method polimórfico

        // E-mail — destinatário válido
        Notificacao n2 = new NotificacaoEmail(
            "N002",
            "ana.silva@email.com",
            "Confirmação de Pagamento",
            "Recebemos seu pagamento de R$ 250,00. Obrigado!");
        historico.add(n2);
        n2.enviar();

        // Push — destinatário válido
        Notificacao n3 = new NotificacaoPush(
            "N003",
            "device_abc12345xyz",
            "Promoção Relâmpago",
            "20% OFF em toda a loja por 1 hora!");
        historico.add(n3);
        n3.enviar();

        // SMS — destinatário INVÁLIDO (telefone com poucos dígitos)
        Notificacao n4 = new NotificacaoSMS(
            "N004",
            "1198765",  // ❌ formato inválido
            "Mensagem de teste");
        historico.add(n4);
        n4.enviar();

        // E-mail — destinatário INVÁLIDO (sem @)
        Notificacao n5 = new NotificacaoEmail(
            "N005",
            "email-invalido.com",  // ❌ sem @
            "Teste",
            "Mensagem de teste");
        historico.add(n5);
        n5.enviar();

        // ─────────────────────────────────────────────────────
        //  LISTAR TODAS
        // ─────────────────────────────────────────────────────
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  HISTÓRICO DE NOTIFICAÇÕES - TECHBR");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        for (int i = 0; i < historico.size(); i++) {
            Notificacao n = historico.get(i);
            System.out.printf("║  [%d] %-6s | %-8s | %-28s | %s%n",
                (i + 1),
                n.getId(),
                n.getTipoCanal(),
                n.getDestinatario(),
                n.getStatus());
        }
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        // ─────────────────────────────────────────────────────
        //  ESTATÍSTICAS
        // ─────────────────────────────────────────────────────
        int enviadas = 0, falhas = 0;
        for (Notificacao n : historico) {
            if (n.getStatus() == StatusEnvio.ENVIADO) enviadas++;
            else if (n.getStatus() == StatusEnvio.FALHOU) falhas++;
        }

        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println("Total de notificações: " + historico.size());
        System.out.println("  ✓ Enviadas: " + enviadas);
        System.out.println("  ✗ Falhas:   " + falhas);
        if (historico.size() > 0) {
            double taxaSucesso = (enviadas * 100.0) / historico.size();
            System.out.printf("Taxa de sucesso: %.1f%%%n", taxaSucesso);
        }

        // ─────────────────────────────────────────────────────
        //  FILTRO POLIMÓRFICO POR CANAL (apenas E-mails)
        // ─────────────────────────────────────────────────────
        System.out.println("\n=== FILTRO: APENAS E-MAILS ===");
        for (Notificacao n : historico) {
            if (n instanceof NotificacaoEmail) {
                System.out.println("- " + n.getId() + " | " + n.getDestinatario()
                    + " | " + n.getStatus());
            }
        }
    }
}
```

**Discussão importante:**
- `ArrayList<Notificacao>` — note que **`Notificacao` é abstrata**, mas pode ser usada como **tipo de referência**.
- `n.enviar()` chama o Template Method, que internamente chama os métodos abstratos da subclasse correta (polimorfismo!).
- O filtro com **`n instanceof NotificacaoEmail`** demonstra polimorfismo na prática: a mesma lista guarda objetos de tipos diferentes, e usamos `instanceof` para distingui-los apenas quando precisamos.
- **Para adicionar um novo canal, basta criar a subclasse e instanciá-la — nenhuma classe existente precisa ser alterada.**

> **Importante para o professor:** mostre o erro de compilação ao **descomentar** a linha `Notificacao n = new Notificacao(...)`. Isso fixa visualmente o conceito de que **abstratas não são instanciáveis**.

---

## Saída Esperada (parcial — varia por causa do `Math.random`)

```
╔═══════════════════════════════════════════════════╗
║      CENTRAL DE NOTIFICAÇÕES - TechBR             ║
╚═══════════════════════════════════════════════════╝

>>> Enviando N001 (SMS)
    📱 Enviando SMS para 11987654321
    Conteúdo: "Seu pedido #4521 foi enviado e chega amanhã!"
    ✓ SMS entregue pela operadora.
    [LOG] N001 | SMS | Status: ENVIADO | Data: 09/05/2026 14:35:22

>>> Enviando N002 (E-mail)
    ✉️  Enviando e-mail para ana.silva@email.com
    Assunto: "Confirmação de Pagamento"
    ✓ E-mail entregue ao servidor SMTP.
    [LOG] N002 | E-mail | Status: ENVIADO | Data: 09/05/2026 14:35:22

>>> Enviando N003 (Push)
    🔔 Enviando push para device_abc12345xyz
    Payload: { "titulo": "Promoção Relâmpago", "corpo": "20% OFF em toda a loja por 1 hora!" }
    ✓ Push entregue ao FCM.
    [LOG] N003 | Push | Status: ENVIADO | Data: 09/05/2026 14:35:22

>>> Enviando N004 (SMS)
    ✗ Destinatário inválido: 1198765
    [LOG] N004 | SMS | Status: FALHOU | Data: —

>>> Enviando N005 (E-mail)
    ✗ Destinatário inválido: email-invalido.com
    [LOG] N005 | E-mail | Status: FALHOU | Data: —

╔═══════════════════════════════════════════════════════════╗
║  HISTÓRICO DE NOTIFICAÇÕES - TECHBR
╠═══════════════════════════════════════════════════════════╣
║  [1] N001   | SMS      | 11987654321                  | ENVIADO
║  [2] N002   | E-mail   | ana.silva@email.com          | ENVIADO
║  [3] N003   | Push     | device_abc12345xyz           | ENVIADO
║  [4] N004   | SMS      | 1198765                      | FALHOU
║  [5] N005   | E-mail   | email-invalido.com           | FALHOU
╚═══════════════════════════════════════════════════════════╝

=== ESTATÍSTICAS ===
Total de notificações: 5
  ✓ Enviadas: 3
  ✗ Falhas:   2
Taxa de sucesso: 60,0%

=== FILTRO: APENAS E-MAILS ===
- N002 | ana.silva@email.com | ENVIADO
- N005 | email-invalido.com | FALHOU
```

---

## Passo 7 — Adicionando NOVO Canal (a magia das classes abstratas)

Imagine que a empresa quer integrar o **Telegram**. Vamos adicionar **sem mexer em nenhuma classe existente**:

```java
public class NotificacaoTelegram extends Notificacao {

    private String chatId;

    public NotificacaoTelegram(String id, String destinatario, String chatId,
                               String mensagem) {
        super(id, destinatario, mensagem);
        this.chatId = chatId;
    }

    @Override
    public String getTipoCanal() {
        return "Telegram";
    }

    @Override
    protected boolean validarDestinatario() {
        // chatId do Telegram é numérico
        return destinatario != null && destinatario.matches("\\d+");
    }

    @Override
    protected String formatarMensagem() {
        return "🤖 [BOT TechBR]\n\n" + mensagem;
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    📨 Enviando Telegram para chat " + chatId);
        System.out.println("    Conteúdo: " + mensagemFormatada);

        boolean sucesso = Math.random() < 0.92;
        if (sucesso) {
            System.out.println("    ✓ Mensagem entregue via Bot API.");
        } else {
            System.out.println("    ✗ Bot API indisponível.");
        }
        return sucesso;
    }
}
```

**Use no `main` sem alterar nenhuma classe existente:**

```java
Notificacao n6 = new NotificacaoTelegram(
    "N006",
    "987654321",
    "987654321",
    "Bem-vindo ao nosso canal!");
historico.add(n6);
n6.enviar();
```

**Discussão crítica:**
- ✅ Não tocamos em `Notificacao` (abstrata)
- ✅ Não tocamos em nenhuma subclasse existente
- ✅ Não tocamos na estrutura do `main` (apenas adicionamos uma nova instância à lista `historico`)
- ✅ Apenas **estendemos** com nova subclasse concreta

> 💎 **A classe abstrata + Template Method criou uma estrutura tão sólida que o sistema cresce por extensão, não por modificação.** Isso é o auge do design orientado a objetos.

---

## Discussão Final com os Alunos

### Perguntas para reflexão:

1. **Por que `Notificacao` precisa ser abstrata?**
   - Não existe "notificação genérica" na realidade
   - Não há como saber `validarDestinatario()`, `formatarMensagem()` ou `transmitir()` sem o canal específico
   - Garante que ninguém crie um objeto sem implementação real

2. **Por que `enviar()` é `final`?**
   - Para que subclasses não possam alterar a ordem dos passos
   - Protege a integridade do fluxo (validar → formatar → transmitir → log)
   - Sem `final`, alguém poderia esquecer um passo importante

3. **Por que `registrarLog()` NÃO é abstrato?**
   - Porque o log é igual para todos os canais
   - Reaproveitamos código (princípio DRY)
   - Centralizamos uma decisão (formato do log) num único lugar

4. **O que aconteceria se eu marcasse a abstrata como concreta (sem `abstract`)?**
   - Compilador exigiria implementação dos métodos abstratos
   - Eu teria que inventar um corpo "vazio" — voltaríamos ao problema original
   - Conceitualmente, perderia a expressão de design

5. **Onde está o polimorfismo neste sistema?**
   - Dentro de `enviar()` — quando chama `validarDestinatario()`, `formatarMensagem()`, `transmitir()` (dispatch dinâmico)
   - No `main` — `ArrayList<Notificacao>` aceita qualquer subclasse, e o filtro com `instanceof` distingue tipos quando necessário
   - Sem polimorfismo, classes abstratas perdem sua razão de ser

6. **Por que isso é melhor que um `if (canal == "SMS") ... else if (canal == "Email") ...`?**
   - Aberto a extensão (novo canal sem mexer no fluxo)
   - Sem encadeamento longo de `if/else`
   - Cada canal **se autodescreve** (suas próprias regras em sua própria classe)
   - Fácil de testar isoladamente

---

## Atividades de Extensão (Opcional)

Se houver tempo, proponha:

1. **Hook methods (ganchos opcionais):**
   - Adicione um método **concreto** vazio `antesDeEnviar()` na abstrata
   - Subclasses **podem** sobrescrever (mas não precisam)
   - Exemplo: `NotificacaoEmail` poderia anexar uma assinatura digital

2. **Notificação multi-canal:**
   - Cliente quer receber a mesma mensagem por **vários canais**
   - Classe `NotificacaoComposta` que internamente tem várias `Notificacao`s
   - Padrão **Composite** (preview de outros padrões de projeto)

3. **Quarta subclasse — WhatsApp:**
   - Destinatário começa com `+55`
   - Mensagem segue um modelo aprovado (ex: "boas_vindas")
   - Implemente seguindo o mesmo padrão dos canais existentes

---

## Resumo do Bloco 2

Neste bloco você:

✅ Implementou um sistema completo com **classe abstrata + Template Method**
✅ Criou hierarquia com **3 (depois 4) canais de notificação**
✅ Aplicou `final` no Template Method para proteger a integridade do fluxo
✅ Misturou **métodos abstratos** (variação) e **métodos concretos** (comum)
✅ Demonstrou ao vivo a **proibição de instanciar** a classe abstrata
✅ Adicionou novo canal (Telegram) **sem modificar** código existente
✅ Aplicou o **Princípio Aberto/Fechado** com classes abstratas

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## Próximo Passo

No **Bloco 3**, você vai **desenvolver sozinho** um sistema diferente: **Sistema de Pagamentos** com Template Method (Cartão de Crédito, PIX, Boleto).

[➡️ Ir para Bloco 3](../Bloco3/README.md)
