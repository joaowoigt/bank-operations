package Entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class Operation implements Comparable<Operation> {

    public static final String DEPOSIT_TYPE = "DEPOSITO";

    @CsvBindByName(column = "OPERADOR")
    private String client;

    @CsvBindByName(column = "VALOR")
    private Double value;

    @CsvBindByName(column = "TIPO")
    private String type;

    @CsvBindByName(column = "DATAHORAOPERACAO")
    private String timestamp;

    @CsvRecurse
    private BankAccount account;

    public void makeOperation() {
        if (Objects.equals(type, DEPOSIT_TYPE)) {
            deposit(value);
        } else {
            withdraw(value);
        }
    }

    private void withdraw(double value) {
        Double newBalance = value - account.getBalance();
        account.setBalance(newBalance);
    }

    private void deposit(double value) {
        Double newBalance = value + account.getBalance();
        account.setBalance(newBalance);
    }

    @Override
    public int compareTo(Operation o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}

