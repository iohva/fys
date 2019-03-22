
package fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.DialogCreator;
import fys_tripperssmaven.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsController_compensation extends BaseClass implements Initializable {

    @FXML LineChart lineChart;
    @FXML PieChart pieChart;
    
    @FXML JFXComboBox comboBx_timeDisplay;
    @FXML JFXDatePicker datePkr_start;
    @FXML JFXDatePicker datePkr_end;
    
    @FXML JFXRadioButton radioBtn_lineChart;
    @FXML JFXRadioButton radioBtn_pieChart;
    
    @FXML JFXButton btn_filterDisplay;
    
    private final String SELECT_DAY = "Per dag";
    private final String SELECT_MONTH = "Per maand";
    private final String SELECT_YEAR = "Per jaar";
    
    private final String TITLE_DAY = "Statistieken per dag";
    private final String TITLE_MONTH = "Statistieken per maand";
    private final String TITLE_YEAR = "Statistieken per jaar";
    
    private final String xMonths[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    private final String xWeekdays[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sa",
                                        "Su"};
    
    private XYChart.Series series;
    private ObservableList<PieChart.Data> pieChartData;
    
    private int currentYear;

    private Map<Integer, Integer> dayValues;
    private Map<Integer, Integer> monthValues;
    private Map<Integer, Integer> yearValues;
    private Map<Integer, Integer> sortedMap;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    private Date resultDate;
    private Calendar cal;
    
    private String chosenTime;
    
    private int resultTime;
    private int startYear;
    private int endYear;
    private int yearsInterval;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            initValues();
            initChart();
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsController_compensation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void filterDisplay() throws SQLException{
        // TODO: Validation for startdate and enddate      
        chosenTime = comboBx_timeDisplay.getValue().toString();
        startDate = datePkr_start.getValue();
        endDate = datePkr_end.getValue();
        
        startYear = datePkr_start.getValue().getYear();
        endYear = datePkr_end.getValue().getYear();
        yearsInterval = endYear - startYear;
        
        resetCharts();
        
        switch (chosenTime) {
            case SELECT_DAY: displayDay();
                 break;
            case SELECT_MONTH: displayMonth();
                 break;
            case SELECT_YEAR:  displayYear();
                 break;
            default: break;
        }
    }
    
    @FXML
    private void showChart(){
        System.out.println("TRIGGERED");
        if(!lineChart.isVisible()){
            lineChart.setVisible(true);
            pieChart.setVisible(false);
        }
        else{
            lineChart.setVisible(false);
            pieChart.setVisible(true);
        }
    }
    
    private void initValues(){
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        cal = Calendar.getInstance();
        datePkr_start.setValue(LocalDate.now());
        datePkr_end.setValue(LocalDate.now());
        
        comboBx_timeDisplay.getItems().addAll(SELECT_DAY, SELECT_MONTH, SELECT_YEAR);
        comboBx_timeDisplay.setValue(SELECT_MONTH);
        
        series = new XYChart.Series();
        pieChartData = FXCollections.observableArrayList();
        
        dayValues = new HashMap<>();
        monthValues = new HashMap<>();
        yearValues = new HashMap<>();
    }
    
    /*
     *  SHOW VALUES OF CURRENT YEAR PER MONTH (DEFAULT)
     */
    private void initChart() throws SQLException{
        lineChart.setTitle(TITLE_YEAR);
        pieChart.setTitle(TITLE_YEAR);
        
        sortedMap = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, true);
        
        for(int key : sortedMap.keySet()) {
            series.getData().add(new XYChart.Data(xMonths[key], sortedMap.get(key)));
            pieChartData.add(new PieChart.Data(xMonths[key],sortedMap.get(key)));
        }

        lineChart.getData().add(series);
        pieChart.setData(pieChartData);
    }
    
    /*
     *  CLEAR CHARTS AND CREATED MAPS (THAT CONTAIN OLD DATA)
     */
    private void resetCharts(){
        series = null;
        pieChartData = null;
        
        dayValues.clear();
        monthValues.clear();
        yearValues.clear();
        lineChart.getData().clear();
        pieChart.getData().clear();
        
        series = new XYChart.Series();
        pieChartData = FXCollections.observableArrayList();
    }
    
    /*
     *  CHECK IF STARTDATE TO ENDDATE IS MIN. 1 YEAR INTERVAL
     */
    private boolean validStartEndDate(){
        if((yearsInterval) <= 0){
            DialogCreator.createErrorDialog("", "Er ging iets mis", "De start- en einddatum moeten minimaal 1 jaar verschillen");
            return false;
        }
        return true;
    }
    
    /*
     *  GET DATA FROM DATABASE AND CREATE MAP (WITH FOUND DATA) FOR CHARTS
     */
    private Map<Integer, Integer> getFilteredChartData(int pLoopLength, Map<Integer,Integer> pMap, int whichTime, int pAdd, boolean isStartUpChart) throws SQLException{
        
        for(int i = 0; i < pLoopLength; i++) {
            pMap.put((pAdd+i), 0);
        }
        
        if(isStartUpChart){
            sql = "SELECT * FROM `RegisterLuggage` WHERE status='" + Constants.STATUS_HANDLED + "' AND YEAR(insertDate) = " + currentYear;
        } else{
            sql = "SELECT * FROM `RegisterLuggage` WHERE status='" + Constants.STATUS_HANDLED + "' AND date >= '" + startDate + "' AND date <= '" + endDate + "'";
        }
        
        result = statement.executeQuery(sql);

        while (result.next()){                
            resultDate = result.getDate("date");
            cal = Calendar.getInstance();
            cal.setTime(resultDate);
            resultTime = cal.get(whichTime);

            pMap.put(resultTime, (pMap.get(resultTime) + 1));
        }
        result.close();      
        return new TreeMap<>(pMap);
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER DAY
     */
    private void displayDay() throws SQLException{
        lineChart.setTitle(TITLE_DAY + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_DAY + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        
        sortedMap = getFilteredChartData(xWeekdays.length, dayValues, Calendar.DAY_OF_WEEK, 0, false);
                   
        for (int key : sortedMap.keySet()) {
            series.getData().add(new XYChart.Data(xWeekdays[key], sortedMap.get(key)));
            pieChartData.add(new PieChart.Data(xWeekdays[key],sortedMap.get(key)));
        }

        lineChart.getData().add(series);
        pieChart.setData(pieChartData);      
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER MONTH
     */
    private void displayMonth() throws SQLException{        
        lineChart.setTitle(TITLE_MONTH + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_MONTH + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
                 
        sortedMap = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, false);

        for (int key : sortedMap.keySet() ) {
            series.getData().add(new XYChart.Data(xMonths[key], sortedMap.get(key)));
            pieChartData.add(new PieChart.Data(xMonths[key],sortedMap.get(key)));
        }

        lineChart.getData().add(series);
        pieChart.setData(pieChartData);   
        
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER YEAR (MUST HAVE MIN. 1 YEAR INTERVAL)
     */
    private void displayYear() throws SQLException{
        lineChart.setTitle(TITLE_YEAR + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_YEAR + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        
        if(validStartEndDate()){            
            series.setName("Some");
            sortedMap = getFilteredChartData(yearsInterval, yearValues, Calendar.YEAR, startYear, false);
   
            for (int key : sortedMap.keySet() ) {                
                series.getData().add(new XYChart.Data(String.valueOf(key), sortedMap.get(key)));
                pieChartData.add(new PieChart.Data(String.valueOf(key), sortedMap.get(key)));
            }
            
            lineChart.getData().add(series);
            pieChart.setData(pieChartData);
        }
    }
}