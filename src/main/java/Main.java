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

        List<Operation> allOperationsList = new CsvToBeanBuilder<Operation>(new FileReader(fileName))
                .withType(Operation.class)
                .build()
                .parse();

        Set<String> idSet = new HashSet<>();

        for (Operation operation : allOperationsList) {
            idSet.add(operation.getAccount().getId());
        }

        for (String id : idSet) {
            Set<Operation> filteredOperationSet =
                    allOperationsList.stream()
                            .filter(operation -> Objects.equals(operation.getAccount().getId(), id))
                            .collect(Collectors.toSet());

            boolean firstIteration = true;
            BankAccount currentAccount = new BankAccount();

            List<Operation> realizedOperations = new ArrayList<>();

            for (Operation operation : filteredOperationSet) {
                if (firstIteration) {
                    setCurrentAccountOnFirstOperation(currentAccount, operation);
                    firstIteration = false;
                }
                operation.setAccount(currentAccount);
                operation.makeOperation();
                realizedOperations.add(operation);
            }

            List<Operation> finalList = realizedOperations.stream().sorted().collect(Collectors.toList());

            createBankStatement(finalList, currentAccount);
        }
    }

    public static void setCurrentAccountOnFirstOperation(BankAccount currentAccount, Operation firstOperation) {
        currentAccount.setId(firstOperation.getAccount().getId());
        currentAccount.setBankName(firstOperation.getAccount().getBankName());
        currentAccount.setAgency(firstOperation.getAccount().getAgency());
        currentAccount.setAccountNumber(firstOperation.getAccount().getAccountNumber());
        currentAccount.setBalance(firstOperation.getAccount().getBalance());
    }

    public static void createBankStatement(List<Operation> currentAccountOperations, BankAccount currentAccount) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String newFileName = "Bank statement " + currentAccountOperations.get(0).getAccount().getId();
        Writer writer = new FileWriter(newFileName);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<List<Operation>>(writer).build();
        beanToCsv.write(currentAccountOperations);
        writer.append("\n -------- \n");
        writer.write("Saldo Atual = R$" + currentAccount.getBalance().toString());
        writer.append("\n --------");
        writer.close();
    }
}
