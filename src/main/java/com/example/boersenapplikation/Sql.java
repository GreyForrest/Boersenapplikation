package com.example.boersenapplikation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Sql {
    private Connection c = null;
    private Statement stmt = null;

    public void createTableIfNotExists() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS shareholders " +
                    "(username TEXT PRIMARY KEY     NOT NULL," +
                    " password           TEXT    NOT NULL, " +
                    "favouriteTitles TEXT DEFAULT (NULL));";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public boolean checkIfUsernameIsAvailable(String username){
        boolean isAvailable = true;
        if(username.contains(" ") || username.length() < 3){
             return false;
        }
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT username FROM shareholders;";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if(rs.getString("username").equals(username)){
                    isAvailable = false;
                }
            }
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            isAvailable = false;
        }
        return isAvailable;
    }

    public void insertNewShareholder(String username, String password) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO shareholders (username, password) " +
                    "VALUES ('" + username + "','" + password + "');";
            stmt.executeUpdate(sql);
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public void updateShareholder(String favouriteTitles, String username) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");

            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE shareholders SET favouriteTitles = '" + favouriteTitles + "' WHERE username = '" + username + "';";
            stmt.executeUpdate(sql);
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public String getFavouriteTitles(String username) {
        String favouriteTitles = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT * FROM shareholders WHERE username='" + username + "';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                favouriteTitles += rs.getString("favouriteTitles") + ";";
            }
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        return favouriteTitles;
    }

    public boolean checkPassword(String username, String password) {
        String databasePassword = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.sqlite");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT * FROM shareholders WHERE username = '" + username + "';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                databasePassword = rs.getString("password");
            }
            c.close();
            if (password.equals(databasePassword)){
                return true;
            }else{
                Messages.getErrorMessage("User oder Passwort existiert nicht.");
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
}
