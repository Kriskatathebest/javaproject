import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("=== Конфигурация на магазина ===");

        double foodMarkup = readDouble("Надценка за хранителни продукти (%): ");
        double nonFoodMarkup = readDouble("Надценка за нехранителни продукти (%): ");
        double discountPercent = readDouble("Отстъпка при наближаване на срока (%): ");
        int daysBeforeExpiration = readInt("Дни преди изтичане за активиране на отстъпка: ");

        Store store = new Store();
        store.setPricingRules(foodMarkup, nonFoodMarkup, discountPercent, daysBeforeExpiration);


        while (true) {
            System.out.println("\n===== МЕНЮ =====");
            System.out.println("1. Добави продукт");
            System.out.println("2. Добави касиер");
            System.out.println("3. Извърши продажба");
            System.out.println("4. Покажи продукти");
            System.out.println("5. Покажи касиери");
            System.out.println("6. Покажи касови бележки");
            System.out.println("7. Финансов отчет");
            System.out.println("0. Изход");
            int choice = readInt("Избери опция: ");

            try {
                switch (choice) {
                    case 1 -> addProduct(store);
                    case 2 -> addCashier(store);
                    case 3 -> performSale(store);
                    case 4 -> store.listProducts();
                    case 5 -> store.listCashiers();
                    case 6 -> store.listReceipts();
                    case 7 -> store.printSummary();
                    case 0 -> {
                        System.out.println("Изход...");
                        return;
                    }
                    default -> System.out.println("Невалиден избор!");
                }
            } catch (Exception e) {
                System.out.println("⚠️ ГРЕШКА: " + e.getMessage());
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Моля, въведи цяло число.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Моля, въведи число.");
            }
        }
    }

    private static void addProduct(Store store) {
        System.out.print("ID на продукта: ");
        String id = scanner.nextLine();

        System.out.print("Име: ");
        String name = scanner.nextLine();

        double price = readDouble("Доставна цена: ");
        int quantity = readInt("Количество: ");

        System.out.print("Продуктът е храна? (yes/no): ");
        String isFood = scanner.nextLine().trim().toLowerCase();

        Product product;
        if (isFood.equals("yes")) {
            System.out.print("Дата на изтичане (yyyy-mm-dd): ");
            LocalDate expiration = LocalDate.parse(scanner.nextLine());
            product = new FoodProduct(id, name, price, expiration, quantity);
        } else {
            product = new NonFoodProduct(id, name, price, LocalDate.MAX, quantity);
        }

        store.addProduct(product);
        System.out.println("✅ Продуктът е добавен.");
    }

    private static void addCashier(Store store) {
        System.out.print("ID на касиера: ");
        String id = scanner.nextLine();

        System.out.print("Име: ");
        String name = scanner.nextLine();

        double salary = readDouble("Заплата: ");

        Cashier cashier = new Cashier(id, name, salary);
        store.addCashier(cashier);

        System.out.println("✅ Касиерът е добавен.");
    }

    private static void performSale(Store store) throws Exception {
        System.out.print("ID на касиера: ");
        String cashierId = scanner.nextLine();

        Cashier cashier = store.findCashierById(cashierId);
        if (cashier == null) {
            System.out.println("❌ Няма такъв касиер.");
            return;
        }

        Map<String, Integer> productsToSell = new LinkedHashMap<>();
        double total = 0.0;

        while (true) {
            System.out.print("ID на продукт (или 'end'): ");
            String pid = scanner.nextLine();
            if (pid.equalsIgnoreCase("end")) break;

            int qty = readInt("Количество: ");
            Product p = store.findProductById(pid);

            if (p == null) {
                System.out.println("⚠️ Продуктът не съществува.");
                continue;
            }

            if (p.isExpired()) {
                System.out.println("⚠️ Продуктът е с изтекъл срок.");
                continue;
            }

            if (p.getQuantity() < qty) {
                System.out.println("⚠️ Недостатъчно количество от " + p.getName());
                continue;
            }

            double price = (p instanceof FoodProduct)
                    ? p.calculateSellingPrice(3, 20, 30)
                    : p.calculateSellingPrice(3, 20, 40);

            total += price * qty;
            productsToSell.put(pid, qty);
        }

        if (productsToSell.isEmpty()) {
            System.out.println("❌ Няма валидни продукти за покупка.");
            return;
        }

        double money = readDouble("Колко пари има клиентът: ");
        if (money < total) {
            System.out.printf("❌ Недостатъчно средства. Общата сума е %.2f лв., клиентът има %.2f лв.\n", total, money);
            return;
        }

        store.sellProducts(productsToSell, cashier);
        System.out.printf("✅ Продажбата е успешна. Ресто: %.2f лв.\n", money - total);
    }
}
