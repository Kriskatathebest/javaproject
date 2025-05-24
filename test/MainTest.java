import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    public void testFoodProductSellingPrice_withinValidRange() {
        FoodProduct product = new FoodProduct("1", "Хляб", 1.0, LocalDate.now().plusDays(2), 10);
        double price = product.calculateSellingPrice(3, 20, 30);
        assertEquals(1.04, price, 0.01); // 1.0 + 30% = 1.30, -20% = 1.04
    }

    @Test
    public void testExpiredProductPriceIsZero() {
        FoodProduct expired = new FoodProduct("2", "Кисело мляко", 1.0, LocalDate.now().minusDays(1), 5);
        assertEquals(0.0, expired.calculateSellingPrice(3, 20, 30));
    }

    @Test
    public void testIsExpiredReturnsTrue() {
        Product p = new FoodProduct("3", "Месо", 2.0, LocalDate.now().minusDays(5), 3);
        assertTrue(p.isExpired());
    }

    @Test
    public void testIsExpiredReturnsFalse() {
        Product p = new FoodProduct("4", "Мляко", 2.0, LocalDate.now().plusDays(5), 3);
        assertFalse(p.isExpired());
    }
}
