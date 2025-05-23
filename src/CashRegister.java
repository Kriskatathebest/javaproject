import java.util.ArrayList;
import java.util.List;

public class CashRegister {
    private int number;
    private Cashier cashier;

    public CashRegister(int number) {
        this.number = number;
    }

    public void assignCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Каса №" + number + " - " + (cashier != null ? cashier.getName() : "Няма касиер");
    }
}
