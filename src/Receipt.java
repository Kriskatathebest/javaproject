import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Receipt implements Serializable {
    private static int counter = 1;

    private final int number;
    private final Cashier cashier;
    private final LocalDateTime dateTime;
    private final Map<Product, Integer> items;
    private final double total;

    public Receipt(Cashier cashier, Map<Product, Integer> items, double total) {
        this.number = counter++;
        this.cashier = cashier;
        this.dateTime = LocalDateTime.now();
        this.items = new LinkedHashMap<>(items);
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public void print() {
        System.out.println(this);
    }

    public void saveToFile() throws IOException {
        String filename = "receipt_" + number + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(toString());
        }
    }

    public void serialize() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("receipt_" + number + ".ser"))) {
            oos.writeObject(this);
        }
    }

    public static Receipt deserialize(int number) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("receipt_" + number + ".ser"))) {
            return (Receipt) ois.readObject();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Касова бележка №").append(number).append("\n");
        sb.append("Касиер: ").append(cashier).append("\n");
        sb.append("Дата: ").append(dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\n");
        sb.append("Продукти:\n");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            sb.append("- ").append(entry.getKey().getName())
                    .append(" (").append(entry.getValue()).append(" бр.)\n");
        }
        sb.append("Обща сума: ").append(String.format("%.2f", total)).append(" лв.\n");
        return sb.toString();
    }
}
