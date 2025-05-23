import java.util.*;
import java.io.*;
import java.time.LocalDate;


public class Store {
    private List<Product> products = new ArrayList<>();
    private List<Cashier> cashiers = new ArrayList<>();
    private List<CashRegister> cashRegisters = new ArrayList<>();
    private List<Receipt> receipts = new ArrayList<>();


    private double foodMarkupPercent = 30.0;
    private double nonFoodMarkupPercent = 40.0;
    private double discountPercent = 20.0;
    private int daysBeforeExpiration = 3;

    private double totalRevenue = 0.0;
    private double totalSalaries = 0.0;
    private double totalDeliveryCosts = 0.0;
    public void setPricingRules(double foodMarkup, double nonFoodMarkup, double discount, int daysBefore) {
        this.foodMarkupPercent = foodMarkup;
        this.nonFoodMarkupPercent = nonFoodMarkup;
        this.discountPercent = discount;
        this.daysBeforeExpiration = daysBefore;
    }

    public void addProduct(Product p) {
        products.add(p);
        totalDeliveryCosts += p.getDeliveryPrice() * p.getQuantity();
    }

    public void addCashier(Cashier c) {
        cashiers.add(c);
        totalSalaries += c.getMonthlySalary();
    }

    public void addCashRegister(CashRegister cr) {
        cashRegisters.add(cr);
    }

    public Product findProductById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Cashier findCashierById(String id) {
        for (Cashier c : cashiers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void sellProducts(Map<String, Integer> productIdToQuantity, Cashier cashier) throws Exception {
        Map<Product, Integer> toSell = new LinkedHashMap<>();
        double total = 0.0;


        for (Map.Entry<String, Integer> entry : productIdToQuantity.entrySet()) {
            Product p = findProductById(entry.getKey());
            if (p == null) continue;

            int requested = entry.getValue();
            if (p.getQuantity() < requested) {
                throw new InsufficientQuantityException(p.getId(), requested, p.getQuantity());
            }
            if (p.isExpired()) continue;

            double price = (p instanceof FoodProduct)
                    ? p.calculateSellingPrice(daysBeforeExpiration, discountPercent, foodMarkupPercent)
                    : p.calculateSellingPrice(daysBeforeExpiration, discountPercent, nonFoodMarkupPercent);

            total += price * requested;
            toSell.put(p, requested);
        }


        for (Map.Entry<Product, Integer> entry : toSell.entrySet()) {
            entry.getKey().reduceQuantity(entry.getValue());
        }

        Receipt r = new Receipt(cashier, toSell, total);
        receipts.add(r);
        totalRevenue += total;
        r.print();
        r.saveToFile();
        r.serialize();
    }

    public void printSummary() {
        System.out.println("----- Финансов отчет -----");
        System.out.printf("Оборот: %.2f лв.\n", totalRevenue);
        System.out.printf("Разходи за заплати: %.2f лв.\n", totalSalaries);
        System.out.printf("Разходи за доставки: %.2f лв.\n", totalDeliveryCosts);
        System.out.printf("Печалба: %.2f лв.\n", (totalRevenue - totalSalaries - totalDeliveryCosts));
    }

    public void listProducts() {
        System.out.println(String.format("%-10s %-15s %-10s %-15s %-10s %-15s %-15s",
                "ID", "Name", "Type", "Delivery Price", "Qty", "Expiration", "Selling Price"));

        for (Product p : products) {
            String type = (p instanceof FoodProduct) ? "Food" : "NonFood";
            double sellingPrice = (p instanceof FoodProduct)
                    ? p.calculateSellingPrice(daysBeforeExpiration, discountPercent, foodMarkupPercent)
                    : p.calculateSellingPrice(daysBeforeExpiration, discountPercent, nonFoodMarkupPercent);

            System.out.println(String.format("%-10s %-15s %-10s %-15.2f %-10d %-15s %-15.2f",
                    p.getId(),
                    p.getName(),
                    type,
                    p.getDeliveryPrice(),
                    p.getQuantity(),
                    (p.getExpirationDate().equals(LocalDate.MAX) ? "N/A" : p.getExpirationDate().toString()),

                    sellingPrice));
        }
    }


    public void listCashiers() {
        for (Cashier c : cashiers) {
            System.out.println(c);
        }
    }

    public void listReceipts() {
        for (Receipt r : receipts) {
            System.out.println("Касова бележка №" + r.getNumber());
        }
    }
}
