package org.example;

import java.util.ArrayList;
import java.util.List;

public class TransactionProcessor {
    public List<Transaction> processLines(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length >= 3) {
                try {
                    String date = values[0];
                    double amount = Double.parseDouble(values[1]);
                    String description = values[2];
                    transactions.add(new Transaction(date, amount, description));
                } catch (NumberFormatException e) {
                    System.out.println("Помилка у числовому полі: " + line);
                }
            }
        }
        return transactions;
    }
}
