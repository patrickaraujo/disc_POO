# 📚 Tutorial Completo --- Criando e Executando um Projeto Java no Eclipse

## 🧩 1️⃣ Criar o Projeto Java

1.  Vá em **File → New → Java Project**
2.  Nome do projeto: `TesteFor`
3.  Clique em **Next**
4.  Se aparecer a opção **Create module-info.java**, desmarque.
5.  Clique em **Finish**

------------------------------------------------------------------------

## 📦 2️⃣ Criar o Package (Pacote)

Use o nome:

    br.com.teste

### Como criar:

1.  Clique com botão direito em **src**
2.  **New → Package**
3.  Digite `br.com.teste`
4.  Clique em **Finish**

------------------------------------------------------------------------

## 🧱 3️⃣ Criar a Classe Principal

1.  Clique com botão direito no pacote `br.com.teste`

2.  **New → Class**

3.  Nome da classe: `Principal`

4.  Marque a opção:

        public static void main(String[] args)

5.  Clique em **Finish**

------------------------------------------------------------------------

## 💻 4️⃣ Código Completo

``` java
package br.com.teste;

public class Principal {

    public static void main(String[] args) {

        for (int i = 1; i <= 5; i++) {
            System.out.println("Valor de i: " + i);
        }

    }
}
```

------------------------------------------------------------------------

## ▶ 5️⃣ Executar o Programa

1.  Clique com botão direito na classe `Principal`
2.  **Run As → Java Application**

------------------------------------------------------------------------

## ✔ Saída Esperada

    Valor de i: 1
    Valor de i: 2
    Valor de i: 3
    Valor de i: 4
    Valor de i: 5

------------------------------------------------------------------------

## 📁 Estrutura Final do Projeto

    TesteFor
     └── src
          └── br.com.teste
                └── Principal.java

------------------------------------------------------------------------

## 🎯 Conclusão

Para treinar estruturas como `for`, você só precisa:

-   Criar o projeto\
-   Criar o package\
-   Criar a classe com `main`\
-   Escrever o código\
-   Executar

Simples, organizado e funcional 🚀
