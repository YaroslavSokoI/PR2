package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "https://informer.com.ua/dut/java/pr2.csv";

        DataReader reader = new URLDataReader();
        TransactionProcessor processor = new TransactionProcessor();

        TransactionCSVReader csvReader = new TransactionCSVReader(reader, processor);
        List<Transaction> transactions = csvReader.readTransactions(filePath);

        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);
        TransactionReportGenerator reportGenerator = new TransactionReportGenerator();

        double totalBalance = analyzer.calculateTotalBalance();
        reportGenerator.printBalanceReport(totalBalance);
        String monthYear = "01-2024";
        int transactionsCount = analyzer.countTransactionsByMonth(monthYear);
        reportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = analyzer.findTopExpenses();
        reportGenerator.printTopExpensesReport(topExpenses);

        System.out.println("\n--- Аналіз за період ---");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 15);

        System.out.println("Найбільші витрати з " + startDate + " по " + endDate + ":");
        List<Transaction> largestByPeriod = analyzer.findLargestExpensesByPeriod(startDate, endDate, 5);
        largestByPeriod.forEach(t -> System.out.println(t.getDate() + ": " + t.getDescription() + " (" + t.getAmount() + ")"));

        Map<String, Double> summaryByCategory = analyzer.getExpenseSummaryByCategory();
        Map<String, Double> summaryByMonth = analyzer.getExpenseSummaryByMonth();

        reportGenerator.printExpenseSummaryReport(summaryByCategory, summaryByMonth, 1000.0);
    }
}
