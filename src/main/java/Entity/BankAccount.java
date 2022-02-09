package Entity;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @CsvBindByName(column = "ID_DA_CONTA")
    private String id;

    @CsvBindByName(column = "NOME_DO_BANCO")
    private String bankName;

    @CsvBindByName(column = "NUMERO_DA_AGENCIA")
    private String agency;

    @CsvBindByName(column = "NUMERO_DA_CONTA")
    private String accountNumber;

    private Double balance = 0.0;
}
