import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Store store = Store.createStore();

        Product p1 = new Product("Milk", BigDecimal.valueOf(20));
        Product p2 = new Product("Water", BigDecimal.valueOf(10));
        Product p3 = new Product("Bread", BigDecimal.valueOf(5));
        store.stockProduct(p1, 20);
        store.stockProduct(p2, 5);

        System.out.println(store.sellProduct("Milk", 8, BigDecimal.valueOf(200)));
        System.out.println(store.sellProduct("Water", 8, BigDecimal.valueOf(200)));
    }
}
