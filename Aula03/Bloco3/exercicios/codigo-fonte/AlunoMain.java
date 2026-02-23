public class AlunoMain {

    public static void main(String[] args) {

        Aluno a1 = new Aluno();
        a1.nome = "Maria";
        a1.matricula = "2024001";
        a1.nota1 = 8.0;
        a1.nota2 = 7.0;

        Aluno a2 = new Aluno();
        a2.nome = "João";
        a2.matricula = "2024002";
        a2.nota1 = 3.0;
        a2.nota2 = 5.0;

        Aluno a3 = new Aluno();
        a3.nome = "Paula";
        a3.matricula = "2024003";
        a3.nota1 = 6.0;
        a3.nota2 = 6.0;

        a1.exibirSituacao();
        a2.exibirSituacao();
        a3.exibirSituacao();
    }
}
