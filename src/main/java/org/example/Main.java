package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "https://informer.com.ua/dut/java/pr2.csv";

        DataReader reader = new URLDataReader();
        TransactionProcessor processor = new TransactionProcessor();

        List<Transaction> transactions = TransactionCSVReader.readTransactions(filePath, reader, processor);

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        System.out.println("\n--- Аналіз за період ---");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);

        System.out.println("Найбільші витрати з " + startDate + " по " + endDate + ":");
        List<Transaction> largestByPeriod = TransactionAnalyzer.findLargestExpensesByPeriod(transactions, startDate, endDate, 5);
        largestByPeriod.forEach(t -> System.out.println(t.getDate() + ": " + t.getDescription() + " (" + t.getAmount() + ")"));

        Map<String, Double> summaryByCategory = TransactionAnalyzer.getExpenseSummaryByCategory(transactions);
        Map<String, Double> summaryByMonth = TransactionAnalyzer.getExpenseSummaryByMonth(transactions);

        TransactionReportGenerator.printExpenseSummaryReport(summaryByCategory, summaryByMonth, 1000.0);
    }
}
