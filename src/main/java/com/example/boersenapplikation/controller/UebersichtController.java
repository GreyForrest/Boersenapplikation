package com.example.boersenapplikation.controller;

import com.example.boersenapplikation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import yahoofinance.Stock;

import java.io.IOException;
import java.util.List;


public class UebersichtController {
    @FXML
    private TextField searchbarTextField;
    @FXML
    private Button searchBtn;
    @FXML
    private ListView titleListView;
    @FXML
    private Label usernameLbl;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private Shareholder shareholder;

    public void setUpController(Shareholder shareholder) {
        this.shareholder = shareholder;
        refreshFavouriteTitles();
        usernameLbl.setText(shareholder.getUsername());
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
    }

    public void onSearchBtnClick(ActionEvent actionEvent) {
        try {
            Titel stock = StockService.findStock(searchbarTextField.getText().toUpperCase());
            if(stock == null){
                throw new Exception();
            }
            Stage stage = (Stage) searchBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Boersenapplikation.class.getResource("titelAnsicht_view.fxml"));
            Parent root = fxmlLoader.load();
            TitelAnsichtController titelAnsichtController = fxmlLoader.getController();
            titelAnsichtController.setUpController(stock, shareholder);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Titel wurde nicht gefunden");
            alert.setContentText("Dieser Titel konnte leider nicht gefunden werden.\nStellen Sie sicher, dass Sie nach dem Ticker," +
                    " also einem 4-stelligen Namen suchen (zum Beispiel TSLA für die Tesla-Aktie).");
            alert.show();

        }
    }

    public void onLogoutBtnClick(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Boersenapplikation.class.getResource("anmelde_view.fxml"));
            UebersichtController controller = fxmlLoader.getController();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Anmelden");
            stage.setScene(scene);
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public void refreshFavouriteTitles() {
        titleListView.setItems(FXCollections.observableArrayList(shareholder.getTitles()));
    }

    public void onListViewClick(MouseEvent mouseEvent) throws IOException {
        if (titleListView.getSelectionModel().getSelectedItem() != null) {
            String stockString = (String)titleListView.getSelectionModel().getSelectedItem();
            Titel stock = StockService.findStock(stockString);

            chart.getData().removeAll();
            XYChart.Series series = new XYChart.Series();
            List<String> yDates = StockService.getHistory(stock);
            series.setName(stockString);
            if(Double.valueOf(StockService.getYearHigh(stock)) - Double.valueOf(StockService.getYearLow(stock)) > 10) {
                yAxis.setTickUnit((Double.valueOf(StockService.getYearHigh(stock)) - Double.valueOf(StockService.getYearLow(stock))) / 10);
            } else {
                yAxis.setTickUnit(2);
            }
            UebersichtController.setSeriesAndSetup(stock, series, yDates, yAxis, xAxis, chart);
        }
    }

    public static void setSeriesAndSetup(Titel stock, XYChart.Series series, List<String> yDates, NumberAxis yAxis, NumberAxis xAxis, LineChart<Number, Number> chart) {
        for (int i = 1; i <= yDates.size(); i++) {
            double yPoint = Double.parseDouble(yDates.get(i - 1));
            int intYPoint = (int) yPoint;
            series.getData().add(new XYChart.Data(i, intYPoint));
        }
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound((int) Double.parseDouble(StockService.getYearLow(stock)) - 10);
        yAxis.setUpperBound((int) Double.parseDouble(StockService.getYearHigh(stock)) + 10);
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(yDates.size());

        chart.setCreateSymbols(false);
        chart.getData().add(series);
    }
}
