import java.math.BigDecimal;

public class Receipt {

    private final String productName;
    private final int quantity;
    private final BigDecimal priceForProduct;
    private final BigDecimal change;

    public Receipt(String productName, int quantity, BigDecimal priceForProduct, BigDecimal change) {
        this.productName = productName;
        this.quantity = quantity;
        this.priceForProduct = priceForProduct;
        this.change = change;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPriceForProduct() {
        return priceForProduct;
    }

    public BigDecimal getChange() {
        return change;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", priceForProduct=" + priceForProduct +
                ", change=" + change +
                '}';
    }
}
