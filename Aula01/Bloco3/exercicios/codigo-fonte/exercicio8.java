import java.util.Scanner;

public class Exercicio8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite o primeiro número inteiro:");
        int inteiro1 = scanner.nextInt();
        
        System.out.println("Digite o segundo número inteiro:");
        int inteiro2 = scanner.nextInt();
        
        System.out.println("Digite o primeiro número float:");
        float float1 = scanner.nextFloat();
        
        System.out.println("Digite o segundo número float:");
        float float2 = scanner.nextFloat();
        
        System.out.println("Valores na ordem inversa:");
        System.out.println(float2);
        System.out.println(float1);
        System.out.println(inteiro2);
        System.out.println(inteiro1);
        
        scanner.close();
    }
}
