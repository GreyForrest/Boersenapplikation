package com.example.boersenapplikation;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StockService {
    public static Titel findStock(String ticker) throws IOException {
        return new Titel(YahooFinance.get(ticker));
    }

    public static String getPrice(Titel stock) {
        try {
            return stock.getStock().getQuote(false).getPrice().toString();
        } catch (Exception e) {
            System.err.println("Error getPrice");
        }
        return null;
    }
    public static String getChangeInPercent(Titel stock){
        try{
        return stock.getStock().getQuote(false).getChangeInPercent().toString();
        } catch (Exception e) {
            System.err.println("Error getChangeInPercent");
        }
        return null;
    }

    public static String getChangeInCurrency(Titel stock){
        try{
            return stock.getStock().getQuote(false).getChange().toString();
        } catch (Exception e) {
            System.err.println("Error getChangeInCurrency");
        }
        return null;
    }

    public static String getPreviousClose(Titel stock){
        try{
            return stock.getStock().getQuote(false).getPreviousClose().toString();
        } catch (Exception e) {
            System.err.println("Error getPreviousClose");
        }
        return null;
    }

    public static String getDayHigh(Titel stock){
        try{
            return stock.getStock().getQuote(false).getDayHigh().toString();
        } catch (Exception e) {
            System.err.println("Error getDayHigh");
        }
        return null;
    }

    public static String getDayLow(Titel stock){
        try{
            return stock.getStock().getQuote(false).getDayLow().toString();
        } catch (Exception e) {
            System.err.println("Error getDayLow");
        }
        return null;
    }

    public static String getOpen(Titel stock){
        try{
            return stock.getStock().getQuote(false).getOpen().toString();
        } catch (Exception e) {
            System.err.println("Error getOpen");
        }
        return null;
    }

    public static String getSymbol(Titel stock){
        try{
            return stock.getStock().getQuote(false).getSymbol();
        } catch (Exception e) {
            System.err.println("Error getSymbol");
        }
        return null;
    }

    public static String getMarketCap(Titel stock){
        try{
            return stock.getStock().getStats(false).getMarketCap().toString();
        } catch (Exception e) {
            System.err.println("Error getMarketCap");
        }
        return null;
    }

    public static String getYearHigh(Titel stock){
        try{
            return stock.getStock().getQuote(false).getYearHigh().toString();
        } catch (Exception e) {
            System.err.println("Error getMarketCap");
        }
        return null;
    }

    public static String getYearLow(Titel stock){
        try{
            return stock.getStock().getQuote(false).getYearLow().toString();
        } catch (Exception e) {
            System.err.println("Error getMarketCap");
        }
        return null;
    }

    public static String getName(Titel stock){
        try{
            return stock.getStock().getName();
        } catch (Exception e) {
            System.err.println("Error getName");
        }
        return null;
    }

    public String formatNumbers(String price){
        char[] priceArray = null;
        String[] priceSplitted = new String[2];
        String attachment = "";
        boolean hasDecimalPoint = false;
        for(char number : price.toCharArray()){
            if(number == '.'){
                hasDecimalPoint = true;
            }
        }
        if(hasDecimalPoint){
            priceSplitted = price.split("\\.");
            priceArray = priceSplitted[0].toCharArray();
            attachment = "." + priceSplitted[1];
        } else {
            priceArray = price.toCharArray();
        }
        String priceString = "";
        int counter = 0;
        if(priceArray.length > 0) {
            for (int i = priceArray.length - 1; i >= 0; i--) {
                counter++;

                if (counter == 3 && i != 0) {
                    priceString = "'" + priceArray[i] + priceString;
                    counter = 0;
                } else {
                    priceString = priceArray[i] + priceString;
                }
            }
        }
        return priceString + attachment;
    }

    public static List<String> getHistory(Titel stock) {
        try {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -1);
            List<HistoricalQuote> history = stock.getStock().getHistory(from, to, Interval.DAILY);
            List<String> dates = new ArrayList<>();
            List<String> values = new ArrayList<>();
            for (HistoricalQuote oneDate : history) {
                String[] splittedValues = oneDate.toString().split(":");
                dates.add(splittedValues[0]);
                values.add(splittedValues[1]);
            }
            List<String> closedValues = new ArrayList<>();
            for(String value : values){
                String[] splitted = value.split("\\(");
                splitted[1] = splitted[1].replace(")", "");
                closedValues.add(splitted[1]);
            }
            return closedValues;
        } catch (Exception e) {
            System.err.println("Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
