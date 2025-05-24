public class InsufficientQuantityException extends Exception {
    private final String productId;
    private final int requested;
    private final int available;

    public InsufficientQuantityException(String productId, int requested, int available) {
        super("Недостатъчно количество от продукт с ID: " + productId +
                ". Заявени: " + requested + ", Налични: " + available);
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }

    public String getProductId() {
        return productId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
