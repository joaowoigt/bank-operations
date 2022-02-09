package Entity;

import com.opencsv.bean.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class Operation implements Comparable<Operation> {

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
        if (Objects.equals(type, "DEPOSITO")) {
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
    public int compareTo(Operation o)  { return timestamp.compareTo(o.getTimestamp()); }
}


