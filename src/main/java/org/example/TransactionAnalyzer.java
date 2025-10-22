package org.example;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class TransactionAnalyzer {
    private List<Transaction> transactions;
    private DateTimeFormatter dateFormatter;

    public TransactionAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public double calculateTotalBalance() {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public int countTransactionsByMonth(String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), dateFormatter);
            String transactionMonthYear = date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
            if (transactionMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public List<Transaction> findTopExpenses() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    private boolean isDateInPeriod(Transaction transaction, LocalDate startDate, LocalDate endDate) {
        try {
            LocalDate date = LocalDate.parse(transaction.getDate(), dateFormatter);
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        } catch (Exception e) {
            System.err.println("Неправильний формат дати: " + transaction.getDate());
            return false;
        }
    }

    public List<Transaction> findTransactionsInPeriod(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> isDateInPeriod(t, startDate, endDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> findLargestExpensesByPeriod(LocalDate startDate, LocalDate endDate, int limit) {
        return findTransactionsInPeriod(startDate, endDate).stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Transaction> findSmallestExpensesByPeriod(LocalDate startDate, LocalDate endDate, int limit) {
        return findTransactionsInPeriod(startDate, endDate).stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getExpenseSummaryByCategory() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getExpenseSummaryByMonth() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> {
                            try {
                                LocalDate date = LocalDate.parse(t.getDate(), dateFormatter);
                                return date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
                            } catch (Exception e) {
                                return "Невідома дата";
                            }
                        },
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }
}
