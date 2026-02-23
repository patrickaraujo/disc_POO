## 📝 **Trabalho Prático - Sistema de Gerenciamento de Biblioteca**

### **Objetivo**
Desenvolver um sistema completo de gerenciamento de biblioteca que aplique os conceitos de Programação Orientada a Objetos estudados durante o curso.

---

### **Requisitos Funcionais**

#### **1. Modelagem de Classes Básicas (Aulas 03-06)**
- `Livro`: com atributos privados (título, autor, ISBN, anoPublicacao, quantidadeDisponivel)
- `Usuario`: com atributos privados (nome, matricula, email, lista de livros emprestados)
- `Emprestimo`: com atributos (livro, usuario, dataEmprestimo, dataDevolucaoPrevista, dataDevolucaoReal)

#### **2. Encapsulamento e Construtores (Aulas 04-05)**
- Implementar getters e setters adequados para cada classe
- Criar múltiplos construtores sobrecarregados para as classes principais
- Utilizar `this` para diferenciar parâmetros de atributos

#### **3. Relacionamentos entre Classes (Aula 06)**
- Implementar associação entre `Usuario` e `Livro` através da classe `Emprestimo`
- Usar composição para gerenciar a lista de empréstimos ativos de um usuário
- Criar relacionamento de agregação entre `Biblioteca` e seus `Livros`

#### **4. Herança e Polimorfismo (Aulas 07-08)**
- Criar uma hierarquia de `Usuario`:
  - `Usuario` (classe base)
  - `Aluno` (subclasse com atributo curso)
  - `Professor` (subclasse com atributo departamento)
- Implementar polimorfismo no método `calcularPrazoMaximo()` que retorna prazos diferentes para alunos (14 dias) e professores (30 dias)

#### **5. Classes Abstratas e Interfaces (Aulas 09-10)**
- Criar uma interface `IPesquisavel` com métodos `buscarPorTitulo()` e `buscarPorAutor()`
- Implementar uma classe abstrata `RecursoBiblioteca` que será estendida por `Livro` e futuramente por `Revista`
- Fazer com que `Biblioteca` implemente a interface `IPesquisavel`

#### **6. Tratamento de Exceções (Aula 11)**
- Criar exceções personalizadas:
  - `LivroIndisponivelException`
  - `UsuarioInadimplenteException`
  - `LimiteEmprestimosExcedidoException`
- Implementar tratamento de exceções no processo de empréstimo e devolução

#### **7. Coleções (Aula 12)**
- Utilizar `ArrayList` para armazenar livros e usuários
- Usar `HashMap` para mapear ISBN para objetos `Livro`
- Implementar busca eficiente usando coleções apropriadas

#### **8. Padrões de Projeto (Aula 14)**
- Implementar o padrão **Singleton** para a classe `Biblioteca` (garantir apenas uma instância)
- Aplicar o padrão **Factory** para criar diferentes tipos de usuários
- Utilizar o padrão **Strategy** para diferentes políticas de multa (aluno vs professor)

---

### **Funcionalidades Esperadas**

1. **Cadastro de Livros**
   - Adicionar novos livros à biblioteca
   - Atualizar informações de livros existentes
   - Remover livros (apenas se não estiverem emprestados)

2. **Cadastro de Usuários**
   - Registrar novos usuários (alunos e professores)
   - Atualizar dados dos usuários
   - Listar usuários cadastrados

3. **Gestão de Empréstimos**
   - Realizar empréstimos verificando disponibilidade
   - Registrar devoluções com cálculo de multas se necessário
   - Listar empréstimos ativos e históricos

4. **Busca e Consulta**
   - Buscar livros por título, autor ou ISBN
   - Consultar situação de empréstimos de um usuário
   - Verificar disponibilidade de livros

---

### **Critérios de Avaliação**

- **Encapsulamento** (20%): Uso correto de modificadores de acesso e métodos getters/setters
- **Herança e Polimorfismo** (20%): Implementação adequada da hierarquia e uso de polimorfismo
- **Interfaces e Classes Abstratas** (15%): Aplicação correta desses conceitos
- **Tratamento de Exceções** (15%): Uso de exceções personalizadas e tratamento adequado
- **Coleções** (10%): Utilização eficiente das estruturas de dados
- **Padrões de Projeto** (10%): Implementação correta dos padrões solicitados
- **Funcionalidade** (10%): Sistema operacional conforme especificado

---

### **Entrega**
- Código fonte completo com comentários explicativos
- Classe `Main` com menu interativo demonstrando todas as funcionalidades
