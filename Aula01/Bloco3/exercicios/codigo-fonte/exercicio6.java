import java.util.Scanner;

public class Exercicio6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite um número inteiro:");
        int numero = scanner.nextInt();
        
        System.out.println("Você digitou: " + numero);
        
        scanner.close();
    }
}
