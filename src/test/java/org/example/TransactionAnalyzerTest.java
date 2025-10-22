package org.example;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class MockDataReader implements DataReader {
    private List<String> lines;

    public MockDataReader(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public List<String> readData(String sourcePath) {
        return lines;
    }
}


class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");

        assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    @Test
    public void testReadTransactionsFromCSV() {
        List<String> csvLines = Arrays.asList(
                "01-01-2023,-150.0,Їжа",
                "02-01-2023,2000.0,Зарплата",
                "03-01-2023,-75.50,Кава",
                "неправильний,рядок"
        );

        DataReader mockReader = new MockDataReader(csvLines);
        TransactionProcessor processor = new TransactionProcessor();

        List<Transaction> transactions = TransactionCSVReader.readTransactions("dummy/path", mockReader, processor);

        assertNotNull(transactions, "Список транзакцій не повинен бути null");
        assertEquals(3, transactions.size(), "Кількість зчитаних транзакцій неправильна");
        assertEquals(-150.0, transactions.get(0).getAmount(), "Сума першої транзакції неправильна");
        assertEquals("Зарплата", transactions.get(1).getDescription(), "Опис другої транзакції неправильний");
    }

    @Test
    public void testFindTopExpenses() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2023", 1000.0, "Дохід"),
                new Transaction("02-01-2023", -100.0, "Їжа"),
                new Transaction("03-01-2023", -500.0, "Одяг"),
                new Transaction("04-01-2023", -50.0, "Кава"),
                new Transaction("05-01-2023", -10.0, "Дрібниці"),
                new Transaction("06-01-2023", -2000.0, "Оренда"),
                new Transaction("07-01-2023", -300.0, "Комуналка"),
                new Transaction("08-01-2023", -250.0, "Транспорт"),
                new Transaction("09-01-2023", -800.0, "Ремонт"),
                new Transaction("10-01-2023", -1200.0, "Подарунок"),
                new Transaction("11-01-2023", -150.0, "Інтернет"),
                new Transaction("12-01-2023", -5.0, "Жуйка")
        );

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);

        assertNotNull(topExpenses, "Список не повинен бути null");
        assertEquals(10, topExpenses.size(), "Має бути рівно 10 найбільших витрат");
        assertEquals(-2000.0, topExpenses.get(0).getAmount(), "Найбільша витрата - оренда (-2000)");
        assertEquals(-1200.0, topExpenses.get(1).getAmount(), "Друга найбільша витрата - подарунок (-1200)");
        assertEquals(-10.0, topExpenses.get(9).getAmount(), "Десята найбільша витрата - дрібниці (-10.0)");
    }
}