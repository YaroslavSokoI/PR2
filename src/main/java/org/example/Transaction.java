package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Transaction {
    private String date;
    private double amount;
    private String description;
}
