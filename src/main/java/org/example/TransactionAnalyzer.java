package org.example;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;


public abstract class TransactionAnalyzer {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            try {
                LocalDate date = LocalDate.parse(transaction.getDate(), dateFormatter);
                String transactionMonthYear = date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
                if (transactionMonthYear.equals(monthYear)) {
                    count++;
                }
            } catch (Exception e) {
                System.err.println("Неправильний формат дати для: " + transaction.getDate());
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    private static boolean isDateInPeriod(Transaction transaction, LocalDate startDate, LocalDate endDate) {
        try {
            LocalDate date = LocalDate.parse(transaction.getDate(), dateFormatter);
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        } catch (Exception e) {
            System.err.println("Неправильний формат дати: " + transaction.getDate());
            return false;
        }
    }

    public static List<Transaction> findTransactionsInPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> isDateInPeriod(t, startDate, endDate))
                .collect(Collectors.toList());
    }

    public static List<Transaction> findLargestExpensesByPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate, int limit) {
        return findTransactionsInPeriod(transactions, startDate, endDate).stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<Transaction> findSmallestExpensesByPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate, int limit) {
        return findTransactionsInPeriod(transactions, startDate, endDate).stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static Map<String, Double> getExpenseSummaryByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public static Map<String, Double> getExpenseSummaryByMonth(List<Transaction> transactions) {
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
