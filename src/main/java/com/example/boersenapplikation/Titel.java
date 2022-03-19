package com.example.boersenapplikation;

import yahoofinance.Stock;

import java.time.LocalDateTime;

public class Titel {
    private final Stock stock;

    public Titel(final Stock stock){
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }
}
