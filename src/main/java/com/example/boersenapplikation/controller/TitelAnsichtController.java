package com.example.boersenapplikation.controller;

import com.example.boersenapplikation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import yahoofinance.Stock;

import java.util.List;

public class TitelAnsichtController {
    @FXML
    private Label titelLbl;
    @FXML
    private Button zurueckBtn;
    @FXML
    private Label symbolLbl;
    @FXML
    private Label kursLbl;
    @FXML
    private Label changelbl;
    @FXML
    private Label vortagLbl;
    @FXML
    private Label eroeffnungLbl;
    @FXML
    private Label tagesHochLbl;
    @FXML
    private Label tagesTiefLbl;
    @FXML
    private Label marktkapitalLbl;
    @FXML
    private Label JahresHochLbl;
    @FXML
    private Label JahresTiefLbl;
    @FXML
    private LineChart<Number, Number> chartDetailed;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Button favoritBtn;

    private Titel stock;
    private Shareholder shareholder;
    public final String YELLOW_TEXT = "-fx-text-fill: yellow; -fx-background-color: #111111;";
    public final String WHITE_TEXT = "-fx-text-fill: white; -fx-background-color: #111111;";

    public void setUpController(StockService stockService, Titel stock, Shareholder shareholder) {
        this.stock = stock;
        this.shareholder = shareholder;
        titelLbl.setText(StockService.getName(stock));
        symbolLbl.setText(stockService.getSymbol(stock));
        kursLbl.setText("Kurs: " + stockService.formatNumbers(stockService.getPrice(stock)) + "$");
        changelbl.setText("Veränderung: " + stockService.formatNumbers(stockService.getChangeInCurrency(stock)) + "$ / " + stockService.formatNumbers(stockService.getChangeInPercent(stock)) + "%");
        vortagLbl.setText("Vortag: " + stockService.formatNumbers(stockService.getPreviousClose(stock)) + "$");
        eroeffnungLbl.setText("Eröffnung: " + stockService.formatNumbers(stockService.getOpen(stock)) + "$");
        tagesHochLbl.setText("Tageshoch: " + stockService.formatNumbers(stockService.getDayHigh(stock)) + "$");
        tagesTiefLbl.setText("Tagestief: " + stockService.formatNumbers(stockService.getDayLow(stock)) + "$");
        JahresHochLbl.setText("Jahreshoch: " + stockService.formatNumbers(stockService.getYearHigh(stock)) + "$");
        JahresTiefLbl.setText("Jahrestief: " + stockService.formatNumbers(stockService.getYearLow(stock)) + "$");
        marktkapitalLbl.setText("Marktkapital: " + stockService.formatNumbers(stockService.getMarketCap(stock)) + "$");
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
        XYChart.Series series = new XYChart.Series();
        List<String> yDates = stockService.getHistory(stock);
        series.setName("Preiskurve vom letzten Jahr");
        UebersichtController.setSeriesAndSetup(stock, series, yDates, yAxis, stockService, xAxis, chartDetailed);
        if(Double.valueOf(StockService.getYearHigh(stock)) - Double.valueOf(StockService.getYearLow(stock)) > 100) {
            yAxis.setTickUnit((Double.valueOf(StockService.getYearHigh(stock)) - Double.valueOf(StockService.getYearLow(stock))) / 10);
        } else {
            yAxis.setTickUnit(5);
        }


        boolean isFavorite = false;
        for(String title : shareholder.getTitles()){
            if(StockService.getSymbol(stock).equals(title)){
                isFavorite = true;
            }
        }
        if(isFavorite){
            favoritBtn.setStyle(YELLOW_TEXT);
        } else {
            favoritBtn.setStyle(WHITE_TEXT);
        }
    }

    public void onZurueckBtnClick(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) zurueckBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Boersenapplikation.class.getResource("uebersicht_view.fxml"));
            Parent root = fxmlLoader.load();
            UebersichtController uebersichtController = fxmlLoader.getController();
            uebersichtController.setUpController(shareholder);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    public void onFavoritHinzufuegen(){
        boolean removed = false;
        int countEmptySpaces = 0;
        Messages messages = new Messages();
        String[] titles = shareholder.getTitles();
        for(String title : titles){
            if(StockService.getSymbol(stock).equals(title)) {
                shareholder.removeFavouriteTitle(stock);
                removed = true;
                favoritBtn.setStyle(WHITE_TEXT);
            } else if (title == null) {
                countEmptySpaces++;
            }
        }
        if(!removed){
            if(countEmptySpaces > 0){
                shareholder.addFavouriteTitle(stock);
                favoritBtn.setStyle(YELLOW_TEXT);
            }  else{
                messages.getErrorMessage("Leider haben Sie die maximale Anzahl an Favoriten erreicht.");
            }
        }

    }
}
