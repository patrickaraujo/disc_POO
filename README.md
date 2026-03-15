# Programação Orientada a Objetos

Repositório destinado ao armazenamento de materiais, códigos-fonte e exercícios da disciplina de **Programação Orientada a Objetos**. O curso é focado na linguagem Java, partindo dos fundamentos sintáticos até a aplicação de padrões de projeto e modelagem UML.

---

## 📂 Estrutura do Repositório

A organização das pastas segue o cronograma das aulas, facilitando a navegação pelos tópicos estudados:

```text
.
├── Aula01/
├── Aula02/
├── Aula03/
├── Aula04/
├── Aula05/
├── Aula06/
├── Aula07/
├── Aula08/
├── Aula09/
├── Aula10/
├── Aula11/
├── Aula12/
├── Aula13/
├── Aula14/
└── README.md

```

---

## 📚 Ementa Detalhada

**Aula 01 - Fundamentos de Java: Introdução, Ambiente e Primeiro Contato**

* Conceito de Java como linguagem e plataforma (JVM, JRE e JDK).
* Estrutura básica de um programa Java e o método `main`.
* Primeiros comandos: `System.out.println` e organização de pacotes.
* Configuração do ambiente de desenvolvimento.
* Atividade: Criação e execução do "Olá Mundo".

**Aula 02 - Fundamentos de Java: Sintaxe Básica e Controle de Fluxo**

* Tipos primitivos e declaração de variáveis.
* Operadores aritméticos, lógicos e relacionais.
* Estruturas condicionais: `if/else` e `switch`.
* Laços de repetição: `while`, `do-while`, `for` e `for-each`.
* Noções de escopo de variáveis.

**Aula 03 - Fundamentos de OO: O que é um Objeto e uma Classe?**

* Diferenças entre o paradigma procedural e o orientado a objetos.
* Conceito de Classe como molde e Objeto como instância.
* Definição de atributos (estado) e métodos (comportamento).
* Instanciação de objetos com a palavra-chave `new`.
* Exemplos práticos: Classes Pessoa e ContaBancária.

**Aula 04 - Fundamentos de OO: Encapsulamento**

* Modificadores de acesso: `private`, `public` e `protected`.
* Implementação de métodos Getters e Setters.
* Proteção do estado do objeto e integridade dos dados.
* Boas práticas de visibilidade.
* Por que evitar o acesso direto aos atributos.

**Aula 05 - Fundamentos de OO: Construtores e Sobrecarga**

* Papel do construtor padrão e construtores personalizados.
* Inicialização de atributos no momento da criação.
* Sobrecarga de métodos: múltiplos métodos com o mesmo nome.
* Uso da palavra-chave `this`.
* Regras para definição de parâmetros em construtores.

**Aula 06 - Fundamentos de OO: Relacionamentos entre Classes**

* Conceito e implementação de Associação.
* Diferença entre Agregação e Composição.
* Como representar "tem um" no código Java.
* Exemplos do mundo real aplicados à modelagem.
* Impacto dos relacionamentos na estrutura do sistema.

**Aula 07 - Fundamentos de OO: Herança**

* Uso da palavra-chave `extends` para reuso de código.
* Criação de hierarquias de classes (Superclasse e Subclasse).
* Acesso a membros da classe pai com `super`.
* Vantagens da herança e os perigos do seu mau uso.
* Especialização de comportamentos em classes filhas.

**Aula 08 - Fundamentos de OO: Polimorfismo**

* Conceito de sobrescrita de métodos (`@Override`).
* Diferença entre tipos estáticos (referência) e tipos dinâmicos.
* Comportamentos diferentes para a mesma chamada de método.
* Polimorfismo em tempo de execução.
* Exemplos práticos com listas de tipos genéricos.

**Aula 09 - OO Avançado em Java: Classes Abstratas**

* Definição de classes que não podem ser instanciadas.
* Criação de métodos abstratos (assinaturas sem corpo).
* Quando optar por uma classe abstrata em vez de uma concreta.
* Obrigatoriedade de implementação nas subclasses.
* Uso de classes abstratas para padronização de modelos.

**Aula 10 - OO Avançado em Java: Interfaces**

* Interfaces como contratos de comportamento.
* Uso da palavra-chave `implements`.
* Diferenças fundamentais entre Interface e Herança.
* Múltiplas interfaces em uma única classe.
* Breve noção de interfaces funcionais.

**Aula 11 - OO Avançado em Java: Exceções**

* Hierarquia de erros e exceções em Java.
* Tratamento com blocos `try`, `catch` e `finally`.
* Diferença entre exceções verificadas (`checked`) e não verificadas.
* Lançamento de exceções com `throw` e `throws`.
* Criação de exceções personalizadas para o negócio.

**Aula 12 - OO Avançado em Java: Coleções**

* Visão geral da Java Collections Framework.
* Trabalhando com listas: `ArrayList` e `LinkedList`.
* Conjuntos e Mapas: `Set` e `Map` (`HashMap`).
* Uso de Generics para segurança de tipos.
* Iteração e manipulação de grandes volumes de dados.

**Aula 13 - OO Avançado em Java: Classes Internas e Visibilidade**

* Definição e uso de Nested Classes (classes internas).
* Regras de escopo e acesso a membros da classe externa.
* Organização de código e encapsulamento avançado.
* Casos de uso comuns para classes internas.
* Visibilidade de pacote vs. visibilidade de classe.

**Aula 14 - OO Avançado em Java: Introdução a Padrões de Projeto**

* O que são Design Patterns e por que utilizá-los.
* Padrão Singleton: Garantindo uma única instância.
* Padrão Factory: Descentralizando a criação de objetos.
* Padrão Strategy: Flexibilidade na troca de algoritmos.
* Identificação de problemas recorrentes e soluções padrão.

<!--**Aula 15 - UML, Projeto e Consolidação: UML e Modelagem**

* Principais símbolos do Diagrama de Classes.
* Representação de atributos, métodos e visibilidade em UML.
* Modelagem de relacionamentos (setas e multiplicidade).
* Tradução direta: do diagrama para o código Java.
* Exercício de leitura e interpretação de diagramas.

**Aula 16 - UML, Projeto e Consolidação: Início do Projeto**

* Análise do problema e definição de requisitos.
* Modelagem orientada a objetos do sistema.
* Divisão de responsabilidades entre as classes.
* Criação do esqueleto do projeto (pacotes e classes base).
* Planejamento das etapas de desenvolvimento.

**Aula 17 - UML, Projeto e Consolidação: Desenvolvimento e Refatoração**

* Implementação das funcionalidades principais.
* Aplicação prática de herança, polimorfismo e coleções.
* Revisão de conceitos através do código desenvolvido.
* Técnicas de refatoração para melhoria da qualidade.
* Ajustes finais e tratamento de erros.

**Aula 18 - UML, Projeto e Consolidação: Apresentação e Fechamento**

* Apresentação das soluções desenvolvidas pelos alunos.
* Discussão sobre as diferentes abordagens para o mesmo problema.
* Revisão geral dos pilares da Orientação a Objetos.
* Dicas para próximos passos e encerramento do curso.-->

---

## 📖 Referências Bibliográficas

**Referência Básica:**  
DEITEL, Harvey. *Java: como programar*. Contribuição de Paul Deitel. Prentice Hall, 2009.

**Referência Complementar:**  
*Java para iniciantes: crie, compile e execute programas Java rapidamente*. Bookman, 2021.
