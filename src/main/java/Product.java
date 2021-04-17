import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Comparable<Product> {

    private final String productName;
    private final BigDecimal productPrice;

    public Product(String productName, BigDecimal productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return Objects.equals(productName, product.productName) &&
                Objects.equals(productPrice, product.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, productPrice);
    }

    @Override
    public int compareTo(Product product) {
        return Integer
                .compare(product.getProductPrice().compareTo(this.getProductPrice()), 0);
    }
}
