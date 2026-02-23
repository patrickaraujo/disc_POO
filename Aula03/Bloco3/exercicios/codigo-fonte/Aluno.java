public class Aluno {

    String nome;
    String matricula;
    double nota1;
    double nota2;

    double calcularMedia() {
        return (nota1 + nota2) / 2;
    }

    void exibirSituacao() {
        double media = calcularMedia();
        String situacao = (media >= 6.0) ? "Aprovado" : "Reprovado";
        System.out.println("Aluno: " + nome + " | Média: " + media + " | " + situacao);
    }
}
