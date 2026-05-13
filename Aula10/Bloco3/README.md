# Bloco 3 — Exercício Autônomo: Sistema de Devices IoT

## Objetivos do Bloco

- Aplicar **múltiplas interfaces** de forma **independente**
- Tomar decisões de design sem orientação direta (qual classe implementa qual interface)
- Implementar **polimorfismo por capacidade** com listas filtradas
- Combinar herança (classe abstrata) + múltiplas interfaces no mesmo sistema
- Validar a compreensão dos conceitos da Aula 10
- Apresentar e discutir diferentes soluções

---

## 🎯 Problema: Hub de Devices IoT (Internet of Things)

Você foi contratado para desenvolver o **hub central** de uma casa inteligente. O hub gerencia **vários tipos de devices** (lâmpada, smart TV, sensor de temperatura, robô aspirador), e **cada device tem um conjunto diferente de capacidades**.

| Device              | Conecta-se à rede? | Pode ser ligado/desligado? | Recebe atualizações? |
|---------------------|:--:|:--:|:--:|
| **Lâmpada Smart**   | ✅ | ✅ | ❌ |
| **Smart TV**        | ✅ | ✅ | ✅ |
| **Sensor de Temperatura** | ✅ | ❌ (sempre ativo) | ✅ |
| **Robô Aspirador**  | ✅ | ✅ | ✅ |

Note que **as combinações de capacidades diferem** entre os devices. Essa é a marca registrada do uso de **múltiplas interfaces**.

> 💡 **Importante:** este exercício é **puramente em memória**. Não há comunicação de rede real, nenhum arquivo é lido ou escrito, nenhuma chamada de API externa. O foco é a **modelagem com interfaces**, e tudo é simulado com `System.out.println` e variáveis booleanas.

### As 3 Capacidades

1. **Conectavel** — sabe se conectar e se desconectar da rede Wi-Fi do hub
2. **Controlavel** — pode ser ligado e desligado pelo usuário
3. **Atualizavel** — recebe atualizações de firmware

### Características Comuns dos Devices

Todo device (independente das capacidades) tem:
- Identificador (ID), ex: "DEV-001"
- Nome amigável, ex: "Lâmpada da sala"
- Marca, ex: "Philips"
- Modelo, ex: "Hue 9W"
- Cômodo em que está, ex: "Sala"

> 💡 Como há **estado comum** entre todos os devices, você usará uma **classe abstrata** `Device` para isso, e as **interfaces** para as capacidades transversais.

---

## Planejamento Necessário (FAÇA ANTES DE CODIFICAR!)

### Passo 1: Desenhe a hierarquia no papel

```
                ┌─────────────────────────────────┐
                │   « abstract » Device           │  ← Que atributos comuns?
                │                                 │  ← Que métodos abstratos?
                └─────────────────┬───────────────┘
                                  │ extends
       ┌─────────────┬────────────┼────────────┬────────────┐
       │             │            │            │            │
   ┌───▼───────┐ ┌───▼───────┐ ┌──▼──────────┐ ┌▼──────────────┐
   │ Lampada   │ │ SmartTV   │ │ SensorTemp  │ │ RoboAspirador │
   └───────────┘ └───────────┘ └─────────────┘ └───────────────┘

   Capacidades transversais (interfaces):
   ┌────────────┐  ┌────────────┐  ┌────────────┐
   │ Conectavel │  │ Controlavel│  │ Atualizavel│
   └────────────┘  └────────────┘  └────────────┘
```

**Perguntas para reflexão:**
- O que TODOS os devices têm em comum?
- Qual subconjunto de interfaces cada device implementa?
- Por que `Device` deve ser classe **abstrata** e não interface?
- O que aconteceria se `Conectavel` tivesse um atributo `boolean conectado` dentro dela?

### Passo 2: Para cada device, marque um ✅ nas interfaces que ele implementa

| Device              | `Conectavel` | `Controlavel` | `Atualizavel` |
|---------------------|:--:|:--:|:--:|
| `Lampada`           | ✅ | ✅ | ❌ |
| `SmartTV`           | ✅ | ✅ | ✅ |
| `SensorTemperatura` | ✅ | ❌ | ✅ |
| `RoboAspirador`     | ✅ | ✅ | ✅ |

### Passo 3: Decida os modificadores de acesso

- Atributos comuns em `Device`: `protected`
- Métodos das interfaces: `public` (obrigatório ao implementar)
- Atributos próprios das subclasses: `private`

---

## Requisitos Detalhados

### Classe Abstrata `Device`

**Atributos:**
```java
- String id
- String nome
- String marca
- String modelo
- String comodo
```

**Construtor:**
```java
public Device(String id, String nome, String marca, String modelo, String comodo)
```

**Método abstrato:**
```java
public abstract String getTipo();   // ex: "Lâmpada Smart", "Smart TV", ...
```

**Métodos concretos:**

- Getters apropriados
- `public void exibirInfo()` — imprime ID, tipo, nome, marca/modelo, cômodo

---

### Interface `Conectavel`

```java
public interface Conectavel {
    void conectar();
    void desconectar();
    boolean estaConectado();
    String getEnderecoIP();
}
```

**Regras:**
- `conectar()` deve definir conectado = true e gerar/atribuir um IP fictício (ex: `"192.168.0.X"` com X aleatório de 2 a 254)
- `desconectar()` deve definir conectado = false e limpar o IP
- Imprima mensagens no console: `"✓ Lâmpada da sala conectou-se em 192.168.0.42"`, `"✗ Lâmpada da sala desconectou-se"`
- Se `estaConectado()` retorna false, `getEnderecoIP()` retorna `""` ou `"—"`

---

### Interface `Controlavel`

```java
public interface Controlavel {
    void ligar();
    void desligar();
    boolean estaLigado();
}
```

**Regras:**
- `ligar()` define ligado = true e imprime `"💡 Lâmpada da sala foi ligada"`
- `desligar()` define ligado = false e imprime `"○ Lâmpada da sala foi desligada"`
- Um device **só pode ser ligado se estiver conectado** (ver dica de implementação)

> 💡 **Regra de negócio importante:** se uma classe implementa **ambos** `Conectavel` e `Controlavel`, então `ligar()` deve verificar `estaConectado()` antes. Se não estiver conectado, imprime mensagem de erro e não liga.

---

### Interface `Atualizavel`

```java
public interface Atualizavel {
    String getVersaoFirmware();
    boolean verificarAtualizacao();
    void atualizar(String novaVersao);
}
```

**Regras:**
- Cada device guarda sua versão atual (ex: `"1.0.0"`)
- `verificarAtualizacao()` simula consulta — retorna `true` com 60% de probabilidade (`Math.random() < 0.6`)
- `atualizar(novaVersao)` define a nova versão e imprime `"⬆ SmartTV: firmware atualizado de 1.0.0 para 1.0.1"`
- Para atualizar é necessário estar **conectado** — se não estiver, não atualiza e imprime mensagem de erro

---

### Classes Concretas (4 devices)

Implementação esperada de cada uma:

#### `Lampada extends Device implements Conectavel, Controlavel`

- Atributos próprios: `private boolean conectado`, `private String enderecoIP`, `private boolean ligada`, `private int intensidade` (0 a 100)
- Método extra: `public void setIntensidade(int valor)` — valida intervalo 0–100 e imprime
- **Não** implementa `Atualizavel` (lâmpadas simples não recebem firmware)

#### `SmartTV extends Device implements Conectavel, Controlavel, Atualizavel`

- Atributos próprios: `conectado`, `enderecoIP`, `ligada`, `volume` (0–100), `canal`, `versaoFirmware`
- Método extra: `public void mudarCanal(int canal)`
- Implementa **todas as 3 interfaces**

#### `SensorTemperatura extends Device implements Conectavel, Atualizavel`

- Atributos próprios: `conectado`, `enderecoIP`, `versaoFirmware`, `double temperaturaAtual`
- Método extra: `public double medir()` — retorna uma temperatura aleatória entre 18 e 32°C
- **Não** implementa `Controlavel` (sensor está sempre operando enquanto conectado)

#### `RoboAspirador extends Device implements Conectavel, Controlavel, Atualizavel`

- Atributos próprios: `conectado`, `enderecoIP`, `ligado`, `versaoFirmware`, `int bateria` (0–100)
- Método extra: `public void aspirar(int minutos)` — diminui bateria proporcionalmente; só funciona se ligado
- Implementa **todas as 3 interfaces**

---

### Classe `HubCasaInteligente` (gerenciamento polimórfico)

**Atributos:**
```java
- String nomeCasa
- List<Device> devices
```

**Métodos:**

1. `public void adicionar(Device d)` — adiciona ao hub
2. `public void listarTodos()` — lista todos os devices com tipo, ID, nome, cômodo
3. `public void conectarTodos()` — conecta **todos** os devices que implementam `Conectavel`
4. `public void atualizarFirmwareDosQuePuderem()` — para cada device `Atualizavel`, verifica e atualiza
5. `public void desligarTudoNoComodo(String comodo)` — desliga (se `Controlavel`) os devices daquele cômodo
6. `public List<Device> filtrarPorCapacidade(Class<?> tipo)` — retorna devices que implementam a interface passada
7. `public void exibirEstatisticas()` — total de devices, total conectados, total controláveis, total atualizáveis, total por cômodo

> 💡 **Dica:** o filtro `filtrarPorCapacidade(Class<?> tipo)` aceita `Conectavel.class`, `Controlavel.class` ou `Atualizavel.class`. Use `tipo.isInstance(device)` para verificar.

---

### Classe `SistemaIoT` (Main)

**Requisitos do teste:**

1. Criar um `HubCasaInteligente`
2. Cadastrar pelo menos:
   - 2 Lâmpadas (cômodos diferentes)
   - 1 Smart TV
   - 1 Sensor de Temperatura
   - 1 Robô Aspirador

3. **Tentar instanciar a abstrata `Device` direto** (deixar comentado, para mostrar o erro de compilação)
4. Listar todos os devices
5. Conectar todos
6. Ligar a Smart TV e o Robô Aspirador, mas tentar ligar a Lâmpada **antes** de conectá-la — para mostrar a regra de proteção
7. Mudar canal e volume da TV (chamadas específicas — exigem cast)
8. Fazer o Robô Aspirador aspirar 30 minutos
9. Medir temperatura do sensor (chamada específica — exige cast)
10. Atualizar firmware dos atualizáveis
11. Filtrar e exibir apenas os `Atualizavel`
12. Filtrar e exibir apenas os `Controlavel`
13. Desligar tudo no cômodo "Sala"
14. Exibir estatísticas

---

## Exemplo de Saída Esperada (parcial — valores variam)

```
╔═══════════════════════════════════════════════════╗
║   HUB DA CASA INTELIGENTE - Residência Silva      ║
╚═══════════════════════════════════════════════════╝

=== DEVICES CADASTRADOS ===

[1] DEV-001 | Lâmpada Smart  | Lâmpada da sala     | Sala
[2] DEV-002 | Lâmpada Smart  | Lâmpada da cozinha  | Cozinha
[3] DEV-003 | Smart TV       | TV da sala          | Sala
[4] DEV-004 | Sensor de Temperatura | Sensor sala  | Sala
[5] DEV-005 | Robô Aspirador | Aspirador           | Cozinha

=== CONECTANDO TODOS OS DEVICES À REDE ===

✓ Lâmpada da sala conectou-se em 192.168.0.42
✓ Lâmpada da cozinha conectou-se em 192.168.0.117
✓ TV da sala conectou-se em 192.168.0.85
✓ Sensor sala conectou-se em 192.168.0.203
✓ Aspirador conectou-se em 192.168.0.31

=== LIGANDO DEVICES ===

💡 Lâmpada da sala foi ligada
💡 Lâmpada da cozinha foi ligada
📺 TV da sala foi ligada
🤖 Aspirador foi ligado

=== TENTATIVA DE LIGAR DEVICE NÃO CONECTADO ===

(criando uma nova Lâmpada "Lâmpada do quarto", SEM conectar)
✗ Lâmpada do quarto: não é possível ligar — device não está conectado

=== CONTROLES ESPECÍFICOS ===

📺 TV da sala — canal mudado para 9
🔊 TV da sala — volume agora em 25
🌡 Sensor sala mediu 24.7°C
🤖 Aspirador aspirando por 30 minutos — bateria caiu de 100 para 70

=== VERIFICAÇÃO E ATUALIZAÇÃO DE FIRMWARE ===

TV da sala — atualização disponível!
⬆ TV da sala: firmware atualizado de 1.0.0 para 1.0.1
Sensor sala — atualização indisponível no momento
Aspirador — atualização disponível!
⬆ Aspirador: firmware atualizado de 2.3.0 para 2.3.1

=== DEVICES ATUALIZÁVEIS ===

- DEV-003 (Smart TV) — versão 1.0.1
- DEV-004 (Sensor de Temperatura) — versão 1.0.0
- DEV-005 (Robô Aspirador) — versão 2.3.1

=== DEVICES CONTROLÁVEIS ===

- DEV-001 (Lâmpada Smart) — Ligado: true
- DEV-002 (Lâmpada Smart) — Ligado: true
- DEV-003 (Smart TV) — Ligado: true
- DEV-005 (Robô Aspirador) — Ligado: true

=== DESLIGANDO TUDO NA SALA ===

○ Lâmpada da sala foi desligada
○ TV da sala foi desligada
(Sensor sala não é Controlavel — ignorado)

=== ESTATÍSTICAS ===

Total de devices:      5
  Conectáveis:         5
  Controláveis:        4
  Atualizáveis:        3

Por cômodo:
  Sala:     3
  Cozinha:  2
```

---

## Critérios de Avaliação

Seu código será avaliado por:

### Funcionalidade (30%)
- ✅ Todas as classes e interfaces criadas
- ✅ 3 interfaces declaradas corretamente
- ✅ 4 classes concretas com combinações corretas
- ✅ Hub gerencia devices polimorficamente
- ✅ Sistema completo executa sem erros

### Uso de Interfaces (40%)
- ✅ Cada interface declarada com palavra-chave `interface`
- ✅ Cada classe usa `implements` corretamente (com vírgula entre múltiplas interfaces)
- ✅ Métodos das interfaces são `public` ao implementar
- ✅ Uso de `@Override` em todos os métodos implementados
- ✅ `Device` é classe abstrata (não interface) — justificativa correta
- ✅ Listas tipadas pela interface (`List<Atualizavel>`, etc.) demonstram polimorfismo por capacidade
- ✅ Filtro `filtrarPorCapacidade` funciona corretamente com `isInstance`

### Qualidade do Código (20%)
- ✅ Nomes descritivos
- ✅ Encapsulamento adequado (`private`/`protected`/`public`)
- ✅ Código organizado e legível
- ✅ Validações claras com retorno booleano
- ✅ Comentários nos pontos não-óbvios (regras de negócio)

### Apresentação (10%)
- ✅ Saída formatada e profissional
- ✅ Mensagens claras
- ✅ Uso adequado de emojis para diferenciar tipos de operação

---

## Desafios Opcionais (Bônus)

Se terminar antes do tempo, implemente:

### 1. Nova interface `Monitoravel`

- Adicione interface `Monitoravel { String getStatusAtual(); }`
- Faça apenas `Sensor` e `RoboAspirador` implementarem
- Sensor retorna `"Medindo: 24.5°C"`
- Aspirador retorna `"Bateria: 70%, status: aspirando"`
- Mostre que **adicionar uma nova capacidade não exige modificar** as outras classes

### 2. Default method em `Atualizavel`

- Adicione um método `default` em `Atualizavel`:
  ```java
  default boolean precisaAtualizar(String versaoMinima) {
      return getVersaoFirmware().compareTo(versaoMinima) < 0;
  }
  ```
- Mostre que classes existentes **não precisam ser alteradas** para ter esse novo método
- Use no `main` para listar quem precisa atualizar para `"2.0.0"`

### 3. Interface funcional `FiltroDevice`

- Crie `@FunctionalInterface interface FiltroDevice { boolean aceita(Device d); }`
- No hub, adicione `public List<Device> filtrar(FiltroDevice f)`
- Use **lambda** no main: `hub.filtrar(d -> d.getComodo().equals("Sala"))`
- (Recurso de interface funcional + lambda — só para alunos curiosos)

### 4. Novo device sem mexer em nada

- Adicione `Termostato extends Device implements Conectavel, Controlavel, Atualizavel`
- Atributo extra: `double temperaturaDesejada`
- Método extra: `public void setTemperaturaDesejada(double t)`
- Confirme: nenhuma outra classe precisou mudar para o termostato ser:
  - listado pelo hub
  - conectado em massa
  - filtrado por `Controlavel`
  - atualizado em massa

### 5. Estado em hub: histórico de eventos

- Adicione `List<String> historicoEventos` em `HubCasaInteligente`
- Cada operação (conectar, ligar, desligar, atualizar) **adiciona um evento** com timestamp
- Método `exibirHistorico()`

---

## Dicas de Implementação

### Endereço IP aleatório

```java
@Override
public void conectar() {
    int octeto = 2 + (int)(Math.random() * 253);
    this.enderecoIP = "192.168.0." + octeto;
    this.conectado = true;
    System.out.println("✓ " + nome + " conectou-se em " + enderecoIP);
}
```

### Proteção: não pode ligar sem estar conectado

```java
@Override
public void ligar() {
    if (!conectado) {
        System.out.println("✗ " + nome + ": não é possível ligar — device não está conectado");
        return;
    }
    this.ligada = true;
    System.out.println("💡 " + nome + " foi ligada");
}
```

### Conectar todos os Conectavel do hub

```java
public void conectarTodos() {
    System.out.println("\n=== CONECTANDO TODOS OS DEVICES À REDE ===\n");
    for (Device d : devices) {
        if (d instanceof Conectavel c) {     // pattern matching
            c.conectar();
        }
    }
}
```

### Atualizar firmware dos atualizáveis

```java
public void atualizarFirmwareDosQuePuderem() {
    System.out.println("\n=== VERIFICAÇÃO E ATUALIZAÇÃO DE FIRMWARE ===\n");
    for (Device d : devices) {
        if (d instanceof Atualizavel a) {
            if (a.verificarAtualizacao()) {
                System.out.println(d.getNome() + " — atualização disponível!");
                String versaoAtual = a.getVersaoFirmware();
                // gera próxima versão simples: incrementa último dígito
                String[] partes = versaoAtual.split("\\.");
                int patch = Integer.parseInt(partes[2]) + 1;
                String nova = partes[0] + "." + partes[1] + "." + patch;
                a.atualizar(nova);
            } else {
                System.out.println(d.getNome() + " — atualização indisponível no momento");
            }
        }
    }
}
```

### Desligar tudo num cômodo

```java
public void desligarTudoNoComodo(String comodo) {
    System.out.println("\n=== DESLIGANDO TUDO EM " + comodo.toUpperCase() + " ===\n");
    for (Device d : devices) {
        if (d.getComodo().equalsIgnoreCase(comodo)) {
            if (d instanceof Controlavel c) {
                c.desligar();
            } else {
                System.out.println("(" + d.getNome() + " não é Controlavel — ignorado)");
            }
        }
    }
}
```

### Filtro polimórfico por capacidade

```java
public List<Device> filtrarPorCapacidade(Class<?> tipo) {
    List<Device> resultado = new ArrayList<>();
    for (Device d : devices) {
        if (tipo.isInstance(d)) {
            resultado.add(d);
        }
    }
    return resultado;
}

// Uso no main:
List<Device> atualizaveis = hub.filtrarPorCapacidade(Atualizavel.class);
List<Device> controlaveis = hub.filtrarPorCapacidade(Controlavel.class);
```

### Estatísticas — contando por capacidade

```java
public void exibirEstatisticas() {
    int conectaveis = 0, controlaveis = 0, atualizaveis = 0;
    for (Device d : devices) {
        if (d instanceof Conectavel)  conectaveis++;
        if (d instanceof Controlavel) controlaveis++;
        if (d instanceof Atualizavel) atualizaveis++;
    }

    System.out.println("\n=== ESTATÍSTICAS ===\n");
    System.out.println("Total de devices:    " + devices.size());
    System.out.println("  Conectáveis:       " + conectaveis);
    System.out.println("  Controláveis:      " + controlaveis);
    System.out.println("  Atualizáveis:      " + atualizaveis);

    // contagem por cômodo
    Map<String, Integer> porComodo = new HashMap<>();
    for (Device d : devices) {
        porComodo.merge(d.getComodo(), 1, Integer::sum);
    }
    System.out.println("\nPor cômodo:");
    for (Map.Entry<String, Integer> e : porComodo.entrySet()) {
        System.out.printf("  %-10s %d%n", e.getKey() + ":", e.getValue());
    }
}
```

### Acessando método específico — exige cast

```java
// SmartTV tem método mudarCanal que NÃO está em nenhuma interface
SmartTV tv = (SmartTV) hub.getDevices().get(2);   // ou guarde a referência direta
tv.mudarCanal(9);

// ou, usando instanceof:
for (Device d : hub.getDevices()) {
    if (d instanceof SmartTV t) {
        t.mudarCanal(9);
    }
}
```

---

## Checklist de Desenvolvimento

Use este checklist para organizar seu trabalho:

**Fase 1: Planejamento (15 min)**
- [ ] Desenhar hierarquia no papel
- [ ] Decidir quais interfaces cada device implementa
- [ ] Listar atributos de cada classe concreta
- [ ] Decidir métodos extras específicos de cada device

**Fase 2: Interfaces e Abstrata (20 min)**
- [ ] Criar interface `Conectavel`
- [ ] Criar interface `Controlavel`
- [ ] Criar interface `Atualizavel`
- [ ] Criar `Device` como `abstract class`
- [ ] Implementar atributos, construtor, getters e `exibirInfo()`

**Fase 3: Classes Concretas (35 min)**
- [ ] `Lampada implements Conectavel, Controlavel`
- [ ] `SmartTV implements Conectavel, Controlavel, Atualizavel`
- [ ] `SensorTemperatura implements Conectavel, Atualizavel`
- [ ] `RoboAspirador implements Conectavel, Controlavel, Atualizavel`
- [ ] Aplicar a regra "só liga se conectado" onde aplicável
- [ ] Aplicar a regra "só atualiza se conectado"

**Fase 4: Hub (20 min)**
- [ ] Criar `HubCasaInteligente`
- [ ] Implementar `adicionar`, `listarTodos`, `conectarTodos`
- [ ] Implementar `atualizarFirmwareDosQuePuderem`, `desligarTudoNoComodo`
- [ ] Implementar `filtrarPorCapacidade` e `exibirEstatisticas`

**Fase 5: Teste e Apresentação (20 min)**
- [ ] Criar `SistemaIoT` com `main`
- [ ] Cadastrar 5+ devices variados
- [ ] **Tentar instanciar `Device` direto** (deixar comentado para mostrar o erro)
- [ ] Executar todo o fluxo: conectar, ligar, controlar, atualizar, filtrar
- [ ] Provocar o erro de ligar sem conectar
- [ ] Ajustar formatação de saída

---

## Apresentação das Soluções

Ao final do bloco, cada aluno (ou dupla) deve:

1. **Demonstrar o sistema funcionando** (5 min)
   - Executar e mostrar saída
   - Apontar onde múltiplas interfaces estão sendo usadas
   - Mostrar o erro de compilação ao tentar instanciar `Device`

2. **Responder perguntas** (3 min)
   - Por que `Device` é classe abstrata e não interface?
   - Por que `Lampada` **não** implementa `Atualizavel`?
   - Por que `SensorTemperatura` **não** implementa `Controlavel`?
   - Como adicionaria um novo device chamado `Termostato`?
   - Como adicionaria uma nova capacidade (ex: `Monitoravel`)?
   - O que aconteceria se você tentasse adicionar uma `Lampada` em `List<Atualizavel>`?

3. **Discussão coletiva** (10 min no final)
   - Comparar diferentes abordagens
   - Discutir trade-offs (a duplicação de estado entre classes que implementam a mesma interface)
   - Identificar oportunidades de melhoria

---

## Resumo do Bloco 3

Neste bloco você:

✅ Aplicou múltiplas interfaces de forma **independente**
✅ Modelou um **domínio realista** (devices IoT)
✅ Implementou **4 classes concretas com combinações diferentes** de capacidades
✅ Usou **polimorfismo por capacidade** com listas filtradas
✅ Aplicou o **Princípio da Segregação de Interfaces** (só implementar o que faz sentido)
✅ Aplicou o **Princípio Aberto/Fechado** via interfaces
✅ Demonstrou que **regras de negócio entre interfaces** (ex: ligar exige conectar) ficam **nas classes**
✅ Apresentou e **defendeu** suas escolhas de design

**Tempo esperado:** 90-120 minutos (uma aula completa)

---

## 🎓 Conclusão da Aula 10

Parabéns! Você completou a Aula 10 e agora domina:

✅ Conceito e mecânica de **Interfaces**
✅ Sintaxe de `interface` e `implements`
✅ **Múltiplas interfaces** em uma única classe
✅ **Combinação herança + interfaces** (`extends X implements A, B, C`)
✅ **Polimorfismo por capacidade** (listas tipadas pela interface)
✅ Diferenças e similaridades entre **interface, classe abstrata e herança**
✅ Noção de **default methods** e **interfaces funcionais**

### Resposta à problematização inicial

> **"Como permitir que um `FuncionarioCLT`, um `FuncionarioHorista` e um `FuncionarioComissionado` possam, cada um, opcionalmente ser também `Lider`, `Mentor` e/ou `Avaliador` — sem explodir em 24 subclasses?"**

**Resposta:** Modelar **Líder**, **Mentor** e **Avaliador** como **interfaces** separadas e fazer cada classe concreta de funcionário implementar **apenas as interfaces que correspondem às responsabilidades reais daquele tipo**. A hierarquia principal de `Funcionario` continua intacta (`extends`), e as capacidades extras são adicionadas **por composição de contratos** (`implements A, B, C`). Não há explosão combinatória — para cada nova capacidade, apenas uma nova interface.

### A trilha completa de OO em Java

```
Encapsulamento  → CONTROLA o acesso aos dados            (Aula 04)
Herança         → COMPARTILHA estrutura entre classes    (Aula 07)
Polimorfismo    → UNIFICA tratamento de tipos diferentes (Aula 08)
Abstração       → EXPRESSA conceitos genéricos no design (Aula 09)
Interfaces      → DESACOPLA capacidades de hierarquias   (Aula 10) ★
```

A trilha de OO está **completa**. Você agora tem o vocabulário e os mecanismos para modelar **qualquer** sistema orientado a objetos.

### Onde isso aparece no mundo real

Você acabou de aprender o mecanismo que está na base de **praticamente todos os frameworks Java**:

- **`Comparable`** — interface para objetos ordenáveis
- **`Runnable`** — interface para tarefas que rodam em threads
- **`Iterable`** — interface para coisas que podem ser percorridas com `for-each`
- **`Iterator`** — interface para o próprio percurso
- **`AutoCloseable`** — interface para recursos que precisam ser fechados
- **`Predicate`, `Function`, `Consumer`, `Supplier`** — interfaces funcionais da API de Streams

Toda vez que você ler em uma documentação algo como *"esta classe implementa `Iterable<E>`"*, você agora sabe exatamente o que isso significa e o que essa classe **promete** entregar.

🚀 **Parabéns por completar a base de OO em Java!**
