package com.example.boersenapplikation;


public class Shareholder {
    private String username;
    private String[] favouriteTitles = new String[5];
    private String password;
    Sql sql = new Sql();

    public void setFavouriteTitles(String[] favouriteTitles) {
        this.favouriteTitles = favouriteTitles;
    }

    public Shareholder(String username, String password) {
        this.username = username;
        this.password = password;
        String favouriteTitlesAsOneString = sql.getFavouriteTitles(username);
        if (favouriteTitlesAsOneString.length() > 0) {
            String favouriteTitlesWithoutLastSemicolon = favouriteTitlesAsOneString.substring(0, favouriteTitlesAsOneString.length() - 1);
            String[] tempFavouriteTitles = favouriteTitlesWithoutLastSemicolon.split(";");
            for (int i = 0; i < 5; i++) {
                try {
                    favouriteTitles[i] = tempFavouriteTitles[i];
                } catch (IndexOutOfBoundsException e) {
                    favouriteTitles[i] = null;
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String[] getTitles() {
        return favouriteTitles;
    }

    public String[] getTitlesForDisplay() {
        int arrSize = 0;
        for (int i = 0; i < 5; i++) {
            if (favouriteTitles[i] != null) {
                arrSize++;
            }
        }
        String[] favouriteTitlesForReturn = new String[arrSize];
        for (int i = 0; i < arrSize; i++) {
            if (favouriteTitles[i] != null) {
                favouriteTitlesForReturn[i] = favouriteTitles[i];
            }
        }
        return favouriteTitlesForReturn;
    }

    public void updateFavouriteTitles() {
        String favouriteTitlesAsString = "";
        for (int i = 0; i < favouriteTitles.length; i++) {
            if (favouriteTitles[i] != null) {
                favouriteTitlesAsString += favouriteTitles[i];
                favouriteTitlesAsString += ";";
            }
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
        StockService stockService = new StockService();
        for (int i = 0; i < 5; i++) {
            if (favouriteTitles[i] == null) {
                favouriteTitles[i] = stockService.getSymbol(stock);
                updateFavouriteTitles();
                break;
            }
        }
    }

    public void removeFavouriteTitle(Titel stock) {
        for (int i = 0; i <= 4; i++) {
            if (StockService.getSymbol(stock).equals(favouriteTitles[i])) {
                favouriteTitles[i] = null;
                for (int j = i; j < 4; j++) {
                    if (favouriteTitles[i + 1] != null) {
                        favouriteTitles[i] = favouriteTitles[i + 1];
                        favouriteTitles[i + 1] = null;
                    }
                }
            }
        }
        updateFavouriteTitles();
    }
}
