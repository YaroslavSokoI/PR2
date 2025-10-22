package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TransactionCSVReader {
    private final DataReader dataReader;
    private final TransactionProcessor processor;

    public TransactionCSVReader(DataReader dataReader, TransactionProcessor processor) {
        this.dataReader = dataReader;
        this.processor = processor;
    }

    public List<Transaction> readTransactions(String sourcePath) {
        List<String> rawLines = dataReader.readData(sourcePath);
        return processor.processLines(rawLines);
    }
}