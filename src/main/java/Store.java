import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Store {

    public static final String NO_MORE_STOCK_IN_THE_STORE = "In the store there is no more of product: ";
    public static final String NOT_ENOUGH_MONEY_MESSAGE = "You don't have enough money to buy product: ";
    public static final String THERE_IS_NO_SUCH_PRODUCT_AS = "There is no such product as: ";

    private final Map<String, TreeMap<Product, Integer>> products;

    private Store() {
        products = new HashMap<>();
    }

    public static Store createStore() {
        return new Store();
    }

    public void stockProduct(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid value! Quantity of product: \""
                    + product.getProductName() + "\" must be greater than 0!");
        }

        products.putIfAbsent(product.getProductName(), new TreeMap<>());
        Map<Product, Integer> currentProduct = products.get(product.getProductName());

        if (!currentProduct.containsKey(product)) {
            products.get(product.getProductName()).put(product, 1);
            products.get(product.getProductName())
                    .put(product, products.get(product.getProductName()).get(product) + quantity - 1);
        } else {
            products.get(product.getProductName())
                    .put(product, products.get(product.getProductName()).get(product) + quantity);
        }

    }

    public boolean isProductStocked(String productName) {
        return products.containsKey(productName);
    }

    public Receipt sellProduct(String productName, int sellQuantity, BigDecimal availableMoney) {

        Map<Product, Integer> currentProduct = products.get(productName);
        validateProduct(currentProduct == null,
                THERE_IS_NO_SUCH_PRODUCT_AS + "\"" + productName + "\"" + " in the shop");

        if (!isProductStocked(productName)) {
            throw new IllegalArgumentException(NO_MORE_STOCK_IN_THE_STORE + productName);
        }

        int productCount = getProductCount(currentProduct);

        if (sellQuantity > productCount) {
            throw new IllegalArgumentException("You want to buy " + sellQuantity + " of product: "
                    + "\"" +  productName + "\"" + ", but there is not enough amount of this product");
        }

        Product product = getCheapestProduct(currentProduct);

        int remainingQuantity;

        BigDecimal totalPrice;

        if (sellQuantity > currentProduct.get(product)) {
            totalPrice = product.getProductPrice().multiply(BigDecimal.valueOf(currentProduct.get(product)));

            if (isMoneyEnough(totalPrice, availableMoney)) {

                remainingQuantity = sellQuantity - currentProduct.get(product);
                currentProduct.remove(product);

                while (remainingQuantity > 0) {

                    product = currentProduct.keySet()
                            .stream().skip(1)
                            .min(Comparator.comparing(Product::getProductPrice))
                            .get();

                    if (isMoneyEnough(totalPrice, availableMoney.subtract(totalPrice))) {

                        int currentProductQuantity = currentProduct.get(product);
                        totalPrice = totalPrice.add(BigDecimal.valueOf(remainingQuantity).multiply(product.getProductPrice()));


                        currentProduct.put(product, Math.abs(remainingQuantity - currentProduct.get(product)));

                        remainingQuantity -= currentProductQuantity;

                        removeProductInstance(currentProduct, product);
                        removeProductFromShop(productName, currentProduct);
                    } else {
                        throw new IllegalArgumentException(NOT_ENOUGH_MONEY_MESSAGE + productName);
                    }
                }
            }

        } else {
            totalPrice = product.getProductPrice().multiply(BigDecimal.valueOf(sellQuantity));

            if (!isMoneyEnough(totalPrice, availableMoney)) {
                throw new IllegalArgumentException(NOT_ENOUGH_MONEY_MESSAGE + productName);
            }

            currentProduct.put(product, currentProduct.get(product) - sellQuantity);
            removeProductInstance(currentProduct, product);
        }

        removeProductFromShop(productName, currentProduct);

        return new Receipt(productName, sellQuantity, totalPrice, availableMoney.subtract(totalPrice));
    }

    private void removeProductInstance(Map<Product, Integer> currentProduct, Product product) {
        if (currentProduct.get(product) == 0) {
            currentProduct.remove(product);
        }
    }

    private void removeProductFromShop(String productName, Map<Product, Integer> currentProduct) {
        if (currentProduct.size() == 0) {
            products.remove(productName);
        }
    }

    private Product getCheapestProduct(Map<Product, Integer> currentProduct) {
        return currentProduct.keySet()
                .stream()
                .min(Comparator.comparing(Product::getProductPrice))
                .get();
    }

    /**
     * @param currentProduct - map of products with same type
     * @return the quantity of the products from same type
     */
    private int getProductCount(Map<Product, Integer> currentProduct) {
        int productCount = 0;

        for (int value : currentProduct.values()) {
            productCount += value;
        }

        return productCount;
    }


    private void validateProduct(boolean condition, String s) {
        if (condition) {
            throw new IllegalArgumentException(s);
        }
    }

    /*
    * This method checks if we have enough money
     */
    private boolean isMoneyEnough(BigDecimal neededMoney, BigDecimal availableMoney) {
        return neededMoney.compareTo(availableMoney) <= 0;
    }
}
