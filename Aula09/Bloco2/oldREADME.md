# Bloco 2 — Exercício Guiado Completo: Sistema de Notificações

## Objetivos do Bloco

- Aplicar classes abstratas em um sistema real e completo
- Implementar o **Template Method Pattern** em contexto realista
- Praticar a mistura de **métodos abstratos** + **métodos concretos**
- Usar `ArrayList<Notificacao>` como coleção polimórfica de tipo abstrato
- Demonstrar a **proibição de instanciação** ao vivo
- Mostrar como adicionar **novo canal** sem alterar código existente
- Implementar tratamento de erros e retry passo a passo com o professor

---

## 🎯 Problema: Central de Notificações Multi-canal

A empresa **TechBR** precisa de uma central que envie notificações aos seus clientes por **vários canais diferentes**: SMS, E-mail, Push (notificação no app) e WhatsApp.

### Canais Suportados

- **SMS:** mensagem de texto via celular (limite de 160 caracteres)
- **E-mail:** mensagem com assunto e corpo
- **Push:** notificação dentro do app (com ícone e som opcional)
- **WhatsApp:** mensagem via API oficial (com modelo aprovado)

### Características Comuns

Todas as notificações têm:
- Identificador único (ID)
- Destinatário (telefone, e-mail, ID do dispositivo, número WhatsApp)
- Conteúdo da mensagem
- Data e hora de envio
- Status (PENDENTE, ENVIADO, FALHOU)
- Contador de tentativas

### Fluxo de Envio (igual para todos)

Independente do canal, **toda notificação segue este fluxo**:

```
1. VALIDAR destinatário (formato correto?)
2. FORMATAR mensagem (cada canal tem suas regras)
3. TRANSMITIR pelo canal específico
4. REGISTRAR log da operação
```

> Se algum passo falhar, o sistema retenta até **3 vezes** antes de marcar como FALHOU.

### Regras Específicas

- **SMS:** destinatário deve ser telefone com 11 dígitos. Mensagem **truncada** em 160 caracteres.
- **E-mail:** destinatário deve conter `@`. Mensagem formatada com assunto + corpo.
- **Push:** destinatário deve começar com `device_`. Mensagem incluí título + corpo.
- **WhatsApp:** destinatário deve começar com `+55` (telefone internacional). Mensagem segue modelo aprovado.

---

## Planejamento da Hierarquia

Antes de codificar, vamos **planejar no papel**:

```
                       ┌────────────────────────────────────┐
                       │    « abstract » Notificacao        │  ← SUPERCLASSE
                       ├────────────────────────────────────┤
                       │ # id                               │
                       │ # destinatario                     │
                       │ # mensagem                         │
                       │ # dataEnvio                        │
                       │ # status                           │
                       │ # tentativas                       │
                       ├────────────────────────────────────┤
                       │ + enviar() : final  ◄─── TEMPLATE METHOD
                       │ # validarDestinatario() : abstract │
                       │ # formatarMensagem() : abstract    │
                       │ # transmitir() : abstract          │
                       │ # registrarLog() : concreto        │
                       └─────────────┬──────────────────────┘
                                     │
        ┌───────────────────┬────────┼─────────┬───────────────────────┐
        │                   │        │         │                       │
   ┌────▼────────────┐  ┌───▼───────┐│  ┌──────▼──────────┐  ┌─────────▼──────────┐
   │NotificacaoSMS   │  │NotifEmail ││  │ NotifPush       │  │ NotifWhatsApp      │
   ├─────────────────┤  ├───────────┤│  ├─────────────────┤  ├────────────────────┤
   │-(sem extra)     │  │-assunto   ││  │-titulo          │  │-modelo (template)  │
   │                 │  │           ││  │-comSom          │  │                    │
   ├─────────────────┤  ├───────────┤│  ├─────────────────┤  ├────────────────────┤
   │@Override        │  │@Override  ││  │@Override        │  │@Override           │
   │validar..        │  │validar..  ││  │validar..        │  │validar..           │
   │formatar..       │  │formatar.. ││  │formatar..       │  │formatar..          │
   │transmitir       │  │transmitir ││  │transmitir       │  │transmitir          │
   └─────────────────┘  └───────────┘│  └─────────────────┘  └────────────────────┘
```

**Decisões de design:**
- ✅ `Notificacao` é **abstrata** — não existe "notificação genérica"
- ✅ `enviar()` é o **Template Method** (concreto, **final**)
- ✅ `validarDestinatario()`, `formatarMensagem()`, `transmitir()` são **abstratos**
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

**Discussão com os alunos:**
- Por que enum? Porque os valores são fixos e mutuamente exclusivos.
- Alternativa seria `String`, mas perderia segurança de tipo.

---

## Passo 2 — Criar a Classe Abstrata `Notificacao`

**O que vai aqui?** Tudo que é **comum** + **Template Method** + **promessas abstratas**.

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Notificacao {

    protected static final int MAX_TENTATIVAS = 3;

    // Atributos comuns
    protected String id;
    protected String destinatario;
    protected String mensagem;
    protected LocalDateTime dataEnvio;
    protected StatusEnvio status;
    protected int tentativas;

    // Construtor (chamado pelas subclasses via super)
    public Notificacao(String id, String destinatario, String mensagem) {
        this.id = id;
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.status = StatusEnvio.PENDENTE;
        this.tentativas = 0;
    }

    // ┌─────────────────────────────────────────────────────┐
    // │  TEMPLATE METHOD                                    │
    // │  Define o esqueleto do algoritmo de envio.          │
    // │  É 'final' — subclasses NÃO podem alterar a ordem.  │
    // └─────────────────────────────────────────────────────┘
    public final void enviar() {
        System.out.println("\n>>> Iniciando envio: " + id + " (" + getTipoCanal() + ")");

        while (tentativas < MAX_TENTATIVAS && status != StatusEnvio.ENVIADO) {
            tentativas++;
            System.out.println("    Tentativa " + tentativas + " de " + MAX_TENTATIVAS);

            try {
                // 1. Validação (abstrato)
                if (!validarDestinatario()) {
                    System.out.println("    ✗ Destinatário inválido: " + destinatario);
                    status = StatusEnvio.FALHOU;
                    break;  // não adianta retentar — destinatário sempre será inválido
                }

                // 2. Formatação (abstrato)
                String mensagemFormatada = formatarMensagem();

                // 3. Transmissão (abstrato)
                boolean sucesso = transmitir(mensagemFormatada);

                if (sucesso) {
                    status = StatusEnvio.ENVIADO;
                    dataEnvio = LocalDateTime.now();
                } else if (tentativas >= MAX_TENTATIVAS) {
                    status = StatusEnvio.FALHOU;
                }
            } catch (Exception e) {
                System.out.println("    ✗ Erro: " + e.getMessage());
                if (tentativas >= MAX_TENTATIVAS) {
                    status = StatusEnvio.FALHOU;
                }
            }
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
                + " | Tentativas: " + tentativas
                + " | Data: " + dataStr);
    }

    // Getters
    public String getId() { return id; }
    public String getDestinatario() { return destinatario; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public StatusEnvio getStatus() { return status; }
    public int getTentativas() { return tentativas; }
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
            System.out.println("    ✗ Operadora indisponível. Retentando...");
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
        sb.append("Assunto: ").append(assunto).append("\n");
        sb.append("\n");
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
            System.out.println("    ✗ Servidor SMTP rejeitou. Retentando...");
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
    private boolean comSom;

    public NotificacaoPush(String id, String destinatario,
                           String titulo, String mensagem, boolean comSom) {
        super(id, destinatario, mensagem);
        this.titulo = titulo;
        this.comSom = comSom;
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
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"titulo\": \"").append(titulo).append("\", ");
        sb.append("\"corpo\": \"").append(mensagem).append("\", ");
        sb.append("\"som\": ").append(comSom);
        sb.append(" }");
        return sb.toString();
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
            System.out.println("    ✗ Token de dispositivo expirado. Retentando...");
        }
        return sucesso;
    }
}
```

---

## Passo 6 — Implementar `NotificacaoWhatsApp`

```java
public class NotificacaoWhatsApp extends Notificacao {

    private String modelo;  // ex: "boas_vindas", "promocao_ativa"

    public NotificacaoWhatsApp(String id, String destinatario,
                               String modelo, String mensagem) {
        super(id, destinatario, mensagem);
        this.modelo = modelo;
    }

    @Override
    public String getTipoCanal() {
        return "WhatsApp";
    }

    @Override
    protected boolean validarDestinatario() {
        // Telefone internacional: começa com +55 e tem 14 caracteres no total (+55 + 11 dígitos)
        if (destinatario == null) return false;
        if (!destinatario.startsWith("+55")) return false;
        String apenasDigitos = destinatario.substring(3).replaceAll("\\D", "");
        return apenasDigitos.length() == 11;
    }

    @Override
    protected String formatarMensagem() {
        return "[modelo: " + modelo + "] " + mensagem;
    }

    @Override
    protected boolean transmitir(String mensagemFormatada) {
        System.out.println("    💬 Enviando WhatsApp para " + destinatario);
        System.out.println("    Conteúdo: " + mensagemFormatada);

        // Simulação: 88% de sucesso
        boolean sucesso = Math.random() < 0.88;
        if (sucesso) {
            System.out.println("    ✓ WhatsApp entregue via API oficial.");
        } else {
            System.out.println("    ✗ API WhatsApp temporariamente fora. Retentando...");
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

## Passo 7 — Criar a Classe `CentralNotificacoes`

Agora vamos criar uma classe que **gerencia** o envio em massa.

```java
import java.util.ArrayList;

public class CentralNotificacoes {

    private String nomeEmpresa;
    private ArrayList<Notificacao> historico;

    public CentralNotificacoes(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
        // ★ ArrayList polimórfico de tipo abstrato ★
        // Aceita qualquer subclasse concreta de Notificacao
        this.historico = new ArrayList<>();
    }

    // Adiciona e dispara o envio (Template Method em ação!)
    public void enfileirar(Notificacao n) {
        historico.add(n);
        n.enviar();  // ← Template Method polimórfico
    }

    // Reenvia todas as que falharam
    public void reenviarFalhas() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  REENVIO DE NOTIFICAÇÕES QUE FALHARAM             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        int reenviadas = 0;
        for (Notificacao n : historico) {
            if (n.getStatus() == StatusEnvio.FALHOU) {
                // Reseta para nova tentativa
                System.out.println("\n>>> Reenviando notificação " + n.getId());
                // (na prática, criaríamos uma nova instância — aqui simplificamos)
                reenviadas++;
            }
        }

        if (reenviadas == 0) {
            System.out.println("\n  Nenhuma notificação para reenviar.");
        }
    }

    // Lista resumida
    public void listarTodas() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  HISTÓRICO DE NOTIFICAÇÕES - " + nomeEmpresa.toUpperCase());
        System.out.println("╠═══════════════════════════════════════════════════════════╣");

        if (historico.isEmpty()) {
            System.out.println("║  Nenhuma notificação enviada.");
        } else {
            for (int i = 0; i < historico.size(); i++) {
                Notificacao n = historico.get(i);
                System.out.printf("║  [%d] %-6s | %-15s | %-30s | %s%n",
                    (i + 1),
                    n.getId(),
                    n.getTipoCanal(),
                    truncar(n.getDestinatario(), 28),
                    n.getStatus());
            }
        }

        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    // Estatísticas
    public void exibirEstatisticas() {
        int total = historico.size();
        int enviadas = 0, falhas = 0, pendentes = 0;

        for (Notificacao n : historico) {
            switch (n.getStatus()) {
                case ENVIADO:  enviadas++; break;
                case FALHOU:   falhas++; break;
                case PENDENTE: pendentes++; break;
            }
        }

        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println("Total de notificações: " + total);
        System.out.println("  ✓ Enviadas: " + enviadas);
        System.out.println("  ✗ Falhas:   " + falhas);
        System.out.println("  ⏳ Pendentes: " + pendentes);

        if (total > 0) {
            double taxaSucesso = (enviadas * 100.0) / total;
            System.out.printf("Taxa de sucesso: %.1f%%%n", taxaSucesso);
        }
    }

    // Filtro polimórfico por canal
    public ArrayList<Notificacao> filtrarPorTipo(Class<?> tipo) {
        ArrayList<Notificacao> resultado = new ArrayList<>();
        for (Notificacao n : historico) {
            if (tipo.isInstance(n)) {
                resultado.add(n);
            }
        }
        return resultado;
    }

    // Auxiliar
    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
```

**Discussão importante:**
- `ArrayList<Notificacao>` — note que **`Notificacao` é abstrata**, mas pode ser usada como **tipo de referência**.
- `n.enviar()` chama o Template Method, que internamente chama os métodos abstratos da subclasse correta (polimorfismo!).
- `filtrarPorTipo` é igual ao da Aula 08 — funciona perfeitamente com hierarquias abstratas.
- **Para adicionar novo canal, NÃO precisamos mexer em `CentralNotificacoes`!**

---

## Passo 8 — Classe Principal para Testar

```java
public class SistemaNotificacoes {
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║      CENTRAL DE NOTIFICAÇÕES - TechBR             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        CentralNotificacoes central = new CentralNotificacoes("TechBR");

        // ❌ Tentativa de instanciar a abstrata (deixe COMENTADO):
        // Notificacao n = new Notificacao("X", "y", "z");
        //                                             ^^^^^
        //                  Erro de compilação: Notificacao is abstract

        // SMS — destinatário válido
        central.enfileirar(new NotificacaoSMS(
            "N001",
            "11987654321",
            "Seu pedido #4521 foi enviado e chega amanhã!"
        ));

        // E-mail — destinatário válido
        central.enfileirar(new NotificacaoEmail(
            "N002",
            "ana.silva@email.com",
            "Confirmação de Pagamento",
            "Recebemos seu pagamento de R$ 250,00. Obrigado!"
        ));

        // Push — destinatário válido
        central.enfileirar(new NotificacaoPush(
            "N003",
            "device_abc12345xyz",
            "Promoção Relâmpago",
            "20% OFF em toda a loja por 1 hora!",
            true
        ));

        // WhatsApp — destinatário válido
        central.enfileirar(new NotificacaoWhatsApp(
            "N004",
            "+5511987654321",
            "boas_vindas",
            "Olá! Seja bem-vindo à TechBR."
        ));

        // SMS — destinatário INVÁLIDO (telefone com poucos dígitos)
        central.enfileirar(new NotificacaoSMS(
            "N005",
            "1198765",  // ❌ formato inválido
            "Mensagem de teste"
        ));

        // E-mail — destinatário INVÁLIDO (sem @)
        central.enfileirar(new NotificacaoEmail(
            "N006",
            "email-invalido.com",  // ❌ sem @
            "Teste",
            "Mensagem de teste"
        ));

        // Listar tudo
        central.listarTodas();

        // Estatísticas
        central.exibirEstatisticas();

        // Filtrar por canal
        System.out.println("\n=== FILTRO: APENAS E-MAILS ===");
        ArrayList<Notificacao> emails = central.filtrarPorTipo(NotificacaoEmail.class);
        for (Notificacao n : emails) {
            System.out.println("- " + n.getId() + " | " + n.getDestinatario()
                    + " | " + n.getStatus());
        }
    }
}
```

> **Importante para o professor:** mostre o erro de compilação ao **descomentar** a linha `Notificacao n = new Notificacao(...)`. Isso fixa visualmente o conceito de que **abstratas não são instanciáveis**.

---

## Saída Esperada (parcial — varia por causa do `Math.random`)

```
╔═══════════════════════════════════════════════════╗
║      CENTRAL DE NOTIFICAÇÕES - TechBR             ║
╚═══════════════════════════════════════════════════╝

>>> Iniciando envio: N001 (SMS)
    Tentativa 1 de 3
    📱 Enviando SMS para 11987654321
    Conteúdo: "Seu pedido #4521 foi enviado e chega amanhã!"
    ✓ SMS entregue pela operadora.
    [LOG] N001 | SMS | Status: ENVIADO | Tentativas: 1 | Data: 27/04/2026 14:35:22

>>> Iniciando envio: N002 (E-mail)
    Tentativa 1 de 3
    ✉️  Enviando e-mail para ana.silva@email.com
    Assunto: "Confirmação de Pagamento"
    ✓ E-mail entregue ao servidor SMTP.
    [LOG] N002 | E-mail | Status: ENVIADO | Tentativas: 1 | Data: 27/04/2026 14:35:22

>>> Iniciando envio: N003 (Push)
    Tentativa 1 de 3
    🔔 Enviando push para device_abc12345xyz
    Payload: { "titulo": "Promoção Relâmpago", "corpo": "20% OFF em toda a loja por 1 hora!", "som": true }
    ✗ Token de dispositivo expirado. Retentando...
    Tentativa 2 de 3
    🔔 Enviando push para device_abc12345xyz
    Payload: { "titulo": "Promoção Relâmpago", "corpo": "20% OFF em toda a loja por 1 hora!", "som": true }
    ✓ Push entregue ao FCM.
    [LOG] N003 | Push | Status: ENVIADO | Tentativas: 2 | Data: 27/04/2026 14:35:23

>>> Iniciando envio: N004 (WhatsApp)
    Tentativa 1 de 3
    💬 Enviando WhatsApp para +5511987654321
    Conteúdo: [modelo: boas_vindas] Olá! Seja bem-vindo à TechBR.
    ✓ WhatsApp entregue via API oficial.
    [LOG] N004 | WhatsApp | Status: ENVIADO | Tentativas: 1 | Data: 27/04/2026 14:35:23

>>> Iniciando envio: N005 (SMS)
    Tentativa 1 de 3
    ✗ Destinatário inválido: 1198765
    [LOG] N005 | SMS | Status: FALHOU | Tentativas: 1 | Data: —

>>> Iniciando envio: N006 (E-mail)
    Tentativa 1 de 3
    ✗ Destinatário inválido: email-invalido.com
    [LOG] N006 | E-mail | Status: FALHOU | Tentativas: 1 | Data: —

╔═══════════════════════════════════════════════════════════╗
║  HISTÓRICO DE NOTIFICAÇÕES - TECHBR
╠═══════════════════════════════════════════════════════════╣
║  [1] N001   | SMS             | 11987654321                    | ENVIADO
║  [2] N002   | E-mail          | ana.silva@email.com            | ENVIADO
║  [3] N003   | Push            | device_abc12345xyz             | ENVIADO
║  [4] N004   | WhatsApp        | +5511987654321                 | ENVIADO
║  [5] N005   | SMS             | 1198765                        | FALHOU
║  [6] N006   | E-mail          | email-invalido.com             | FALHOU
╚═══════════════════════════════════════════════════════════╝

=== ESTATÍSTICAS ===
Total de notificações: 6
  ✓ Enviadas: 4
  ✗ Falhas:   2
  ⏳ Pendentes: 0
Taxa de sucesso: 66,7%

=== FILTRO: APENAS E-MAILS ===
- N002 | ana.silva@email.com | ENVIADO
- N006 | email-invalido.com | FALHOU
```

---

## Passo 9 — Adicionando NOVO Canal (a magia das classes abstratas)

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
        // chatId do Telegram é numérico, pode ser positivo ou negativo
        if (destinatario == null) return false;
        try {
            Long.parseLong(destinatario);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
            System.out.println("    ✗ Bot API timeout. Retentando...");
        }
        return sucesso;
    }
}
```

**Use no `main` sem alterar nenhuma outra classe:**

```java
central.enfileirar(new NotificacaoTelegram(
    "N007",
    "987654321",
    "987654321",
    "Bem-vindo ao nosso canal!"
));
```

**Discussão crítica:**
- ✅ Não tocamos em `Notificacao` (abstrata)
- ✅ Não tocamos em `CentralNotificacoes`
- ✅ Não tocamos no `main` (apenas adicionamos uma nova chamada)
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
   - Em `CentralNotificacoes` — `ArrayList<Notificacao>` aceita qualquer subclasse
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

2. **Política de retry customizável:**
   - Cada canal define `getMaxTentativas()` — abstrato ou concreto com default
   - SMS: 3 tentativas | Push: 5 tentativas | E-mail: 2 tentativas

3. **Notificação agendada:**
   - Subclasse `NotificacaoAgendada` que estende qualquer outra
   - Atributo `dataAgendamento`
   - `enviar()` espera até a data antes de processar

4. **Persistência simples:**
   - `gerarLogCSV()` polimórfico em cada subclasse
   - `CentralNotificacoes.exportarLog()` consolida todos os logs

5. **Notificação multi-canal:**
   - Cliente quer receber a mesma mensagem por **vários canais**
   - Classe `NotificacaoComposta` que internamente tem várias `Notificacao`s
   - Padrão **Composite** (preview de outros padrões de projeto)

---

## Resumo do Bloco 2

Neste bloco você:

✅ Implementou um sistema completo com **classe abstrata + Template Method**
✅ Criou hierarquia com **4 (depois 5) canais de notificação**
✅ Aplicou `final` no Template Method para proteger a integridade do fluxo
✅ Misturou **métodos abstratos** (variação) e **métodos concretos** (comum)
✅ Demonstrou ao vivo a **proibição de instanciar** a classe abstrata
✅ Adicionou novo canal (Telegram) **sem modificar** código existente
✅ Aplicou o **Princípio Aberto/Fechado** com classes abstratas

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## Próximo Passo

No **Bloco 3**, você vai **desenvolver sozinho** um sistema diferente: **Sistema de Importação de Dados** com Template Method (CSV, JSON, XML, Excel).

[➡️ Ir para Bloco 3](../Bloco3/README.md)
