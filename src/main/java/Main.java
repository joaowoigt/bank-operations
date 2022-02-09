import Entity.BankAccount;
import Entity.Operation;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        String fileName = "C:\\bank-operations\\src\\main\\java\\data\\bank_data.csv";

        List<Operation> operationList = new CsvToBeanBuilder<Operation>(new FileReader(fileName))
                .withType(Operation.class)
                .build()
                .parse();

        Set<String> idSet = new HashSet<>();

        for (Operation operation : operationList) {
            idSet.add(operation.getAccount().getId());
        }

        for (String id : idSet) {
            Set<Operation> filteredSet =
                    operationList.stream()
                            .filter(operation -> Objects.equals(operation.getAccount().getId(), id))
                            .collect(Collectors.toSet());

            boolean firstIteration = true;
            BankAccount account = new BankAccount();

            List<Operation> realizedOperations = new ArrayList<>();

            for (Operation operation : filteredSet) {
                if (firstIteration) {
                    account.setId(operation.getAccount().getId());
                    account.setBankName(operation.getAccount().getBankName());
                    account.setAgency(operation.getAccount().getAgency());
                    account.setAccountNumber(operation.getAccount().getAccountNumber());
                    account.setBalance(operation.getAccount().getBalance());
                    firstIteration = false;
                }
                operation.setAccount(account);
                operation.makeOperation();
                realizedOperations.add(operation);
            }

            List<Operation> sortedList = realizedOperations.stream().sorted().collect(Collectors.toList());

            String newFileName = "Bank statement " + realizedOperations.get(0).getAccount().getId();
            Writer writer = new FileWriter(newFileName);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<List<Operation>>(writer).build();
            beanToCsv.write(sortedList);
            writer.append("\n -------- \n");
            writer.write("Saldo Atual = R$" + account.getBalance().toString());
            writer.append("\n --------");
            writer.close();
        }
    }

}
