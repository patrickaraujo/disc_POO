import java.util.Scanner;

public class Exercicio7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite um número float:");
        float numeroFloat = scanner.nextFloat();
        
        System.out.println("Digite um caractere:");
        char caractere = scanner.next().charAt(0);
        
        int valorInteiro = (int) caractere;
        
        System.out.println("Caractere digitado: " + caractere);
        System.out.println("Valor inteiro correspondente: " + valorInteiro);
        
        scanner.close();
    }
}
