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

    public void setUpController(Titel stock, Shareholder shareholder) {
        this.stock = stock;
        this.shareholder = shareholder;
        titelLbl.setText(StockService.getName(stock));
        symbolLbl.setText(StockService.getSymbol(stock));
        kursLbl.setText("Kurs: " + StockService.formatNumbers(StockService.getPrice(stock)) + "$");
        changelbl.setText("Veränderung: " + StockService.formatNumbers(StockService.getChangeInCurrency(stock)) + "$ / " + StockService.formatNumbers(StockService.getChangeInPercent(stock)) + "%");
        vortagLbl.setText("Vortag: " + StockService.formatNumbers(StockService.getPreviousClose(stock)) + "$");
        eroeffnungLbl.setText("Eröffnung: " + StockService.formatNumbers(StockService.getOpen(stock)) + "$");
        tagesHochLbl.setText("Tageshoch: " + StockService.formatNumbers(StockService.getDayHigh(stock)) + "$");
        tagesTiefLbl.setText("Tagestief: " + StockService.formatNumbers(StockService.getDayLow(stock)) + "$");
        JahresHochLbl.setText("Jahreshoch: " + StockService.formatNumbers(StockService.getYearHigh(stock)) + "$");
        JahresTiefLbl.setText("Jahrestief: " + StockService.formatNumbers(StockService.getYearLow(stock)) + "$");
        marktkapitalLbl.setText("Marktkapital: " + StockService.formatNumbers(StockService.getMarketCap(stock)) + "$");
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
        XYChart.Series series = new XYChart.Series();
        List<String> yDates = StockService.getHistory(stock);
        series.setName("Preiskurve vom letzten Jahr");
        UebersichtController.setSeriesAndSetup(stock, series, yDates, yAxis, xAxis, chartDetailed);
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
            System.err.println(e.getLocalizedMessage());
        }
    }

    public void onFavoritHinzufuegen(){
        boolean removed = false;
        List<String> titles = shareholder.getTitles();
        for(String title : titles) {
            if (StockService.getSymbol(stock).equals(title)) {
                shareholder.removeFavouriteTitle(stock);
                removed = true;
                favoritBtn.setStyle(WHITE_TEXT);
                break;
            }
        }
        if(!removed){
                shareholder.addFavouriteTitle(stock);
                favoritBtn.setStyle(YELLOW_TEXT);
            }
        }

}
