public class PedidoMain {

    public static void main(String[] args) {

        Pedido p1 = new Pedido(1, "Ana");

        p1.adicionarItem(25.0);
        p1.adicionarItem(12.50, 3);
        p1.adicionarItem(-5.0);  // deve ser recusado

        System.out.println();
        p1.exibirResumo();
    }
}
