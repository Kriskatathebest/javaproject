import java.io.Serializable;
import java.time.LocalDate;

public abstract class Product implements Serializable {
    private String id;
    private String name;
    private double deliveryPrice;
    private LocalDate expirationDate;
    private int quantity;

    public Product(String id, String name, double deliveryPrice, LocalDate expirationDate, int quantity) {
        this.id = id;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public abstract double calculateSellingPrice(int daysBeforeExpiration, double discountPercent, double markupPercent);

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }
}
