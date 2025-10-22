package org.example;

import java.util.List;
import java.util.Map;
import java.util.Collections;

public abstract class TransactionReportGenerator {
    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExpenseSummaryReport(Map<String, Double> byCategory, Map<String, Double> byMonth, double symbolValue) {
        System.out.println("\n--- Текстовий звіт по витратах ---");
        System.out.println("Кожен символ (*) = " + symbolValue + " грн.");

        System.out.println("\nВитрати за категоріями:");
        for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            String stars = generateStars(amount, symbolValue);
            System.out.printf("%-20s | %10.2f | %s\n", category, amount, stars);
        }

        System.out.println("\nВитрати за місяцями:");
        for (Map.Entry<String, Double> entry : byMonth.entrySet()) {
            String month = entry.getKey();
            double amount = entry.getValue();
            String stars = generateStars(amount, symbolValue);
            System.out.printf("%-10s | %10.2f | %s\n", month, amount, stars);
        }
    }

    private static String generateStars(double amount, double symbolValue) {
        long starCount = Math.round(Math.abs(amount) / symbolValue);
        if (starCount == 0 && Math.abs(amount) > 0) {
            starCount = 1;
        }
        return String.join("", Collections.nCopies((int) starCount, "*"));
    }
}
