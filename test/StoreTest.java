import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {
    private Store store;
    private FoodProduct food;
    private NonFoodProduct nonFood;
    private Cashier cashier;

    @BeforeEach
    public void setup() {
        store = new Store();
        food = new FoodProduct("f1", "Хляб", 1.0, LocalDate.now().plusDays(2), 10);
        nonFood = new NonFoodProduct("n1", "Сапун", 2.0, LocalDate.MAX, 5);
        cashier = new Cashier("c1", "Иван", 1000);

        store.setPricingRules(30.0, 40.0, 20.0, 3);
        store.addProduct(food);
        store.addProduct(nonFood);
        store.addCashier(cashier);
    }

    @Test
    public void testAddProductAndCashier() {
        assertEquals(food, store.findProductById("f1"));
        assertEquals(nonFood, store.findProductById("n1"));
        assertEquals(cashier, store.findCashierById("c1"));
    }

    @Test
    public void testSuccessfulSale() throws Exception {
        Map<String, Integer> basket = new HashMap<>();
        basket.put("f1", 2);  // хляб
        basket.put("n1", 1);  // сапун

        store.sellProducts(basket, cashier);

        // Проверка, че количествата са намалели
        assertEquals(8, food.getQuantity());
        assertEquals(4, nonFood.getQuantity());
    }

    @Test
    public void testSaleThrowsInsufficientQuantityException() {
        Map<String, Integer> basket = new HashMap<>();
        basket.put("n1", 999);  // над наличното

        assertThrows(InsufficientQuantityException.class, () -> store.sellProducts(basket, cashier));
    }

    @Test
    public void testExpiredProductIsNotSold() throws Exception {
        FoodProduct expired = new FoodProduct("f2", "Кисело мляко", 0.9, LocalDate.now().minusDays(1), 3);
        store.addProduct(expired);

        Map<String, Integer> basket = new HashMap<>();
        basket.put("f2", 2);

        store.sellProducts(basket, cashier);  // няма да хвърли грешка, но няма да продаде

        assertEquals(3, expired.getQuantity());  // остава непроменено
    }
}
