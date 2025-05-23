import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NonFoodProduct extends Product {

    public NonFoodProduct(String id, String name, double deliveryPrice, LocalDate expirationDate, int quantity) {
        super(id, name, deliveryPrice, expirationDate, quantity);
    }

    @Override
    public double calculateSellingPrice(int daysBeforeExpiration, double discountPercent, double markupPercent) {
        if (isExpired()) {
            return 0.0;
        }

        double basePrice = getDeliveryPrice() * (1 + markupPercent / 100.0);

        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), getExpirationDate());

        if (daysUntilExpiration <= daysBeforeExpiration) {
            basePrice = basePrice * (1 - discountPercent / 100.0);
        }

        return basePrice;
    }
}
