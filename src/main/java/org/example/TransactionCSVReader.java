package org.example;

import java.util.List;


public abstract class TransactionCSVReader {
    public static List<Transaction> readTransactions(String sourcePath, DataReader dataReader, TransactionProcessor processor) {
        List<String> rawLines = dataReader.readData(sourcePath);
        return processor.processLines(rawLines);
    }
}