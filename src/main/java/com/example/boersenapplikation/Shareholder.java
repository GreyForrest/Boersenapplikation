package com.example.boersenapplikation;


import java.util.ArrayList;
import java.util.List;

public class Shareholder {
    private final String username;
    private List<String> favouriteTitles = new ArrayList<>();
    private final String password;
    Sql sql = new Sql();

    public Shareholder(String username, String password) {
        this.username = username;
        this.password = password;
        String favouriteTitlesAsOneString = sql.getFavouriteTitles(username);
        if (favouriteTitlesAsOneString.length() > 0) {
            String favouriteTitlesWithoutLastSemicolon = favouriteTitlesAsOneString.substring(0, favouriteTitlesAsOneString.length() - 1);
            favouriteTitles = List.of(favouriteTitlesWithoutLastSemicolon.split(";"));
        }
    }

    public String getUsername() {
        return username;
    }

    public List<String> getTitles() {
        return favouriteTitles;
    }

    public void updateFavouriteTitles() {
        String favouriteTitlesAsString = "";
        for (String title : favouriteTitles) {
             favouriteTitlesAsString += title + ";";
        }
        if (favouriteTitlesAsString.length() >= 1) {
            favouriteTitlesAsString = favouriteTitlesAsString.substring(0, favouriteTitlesAsString.length() - 1);
        }
        sql.updateShareholder(favouriteTitlesAsString, username);
    }

    public boolean loginShareholder() {
        return sql.checkPassword(username, password);
    }

    public void registerShareholder(String repeatPassword) {
        if (password.equals(repeatPassword)) {
            sql.insertNewShareholder(username, password);
        }
    }

    public void addFavouriteTitle(Titel stock) {
        favouriteTitles.add(StockService.getSymbol(stock));
        updateFavouriteTitles();

    }

    public void removeFavouriteTitle(Titel stock) {
        for (int i = 0; i < favouriteTitles.size(); i++) {
            if (StockService.getSymbol(stock).equals(favouriteTitles.get(i))) {
                favouriteTitles.remove(i);
                break;
            }
        }
        updateFavouriteTitles();
    }
}
