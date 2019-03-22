
package com.mycompany.fys_tripperssmaven.controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.DialogCreator;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 * Controller voor het bekijken van statistieken.
 * @author Raoul Ramawadh 500729457
 */
public class StatisticsController_service extends BaseClass implements Initializable {

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
    
    private final String xMonths[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    private final String xWeekdays[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
                                        "Sun"};
    
    private String TITLE_DAY;
    private String TITLE_MONTH;
    private String TITLE_YEAR;
    
    private XYChart.Series seriesFoundLuggage;
    private XYChart.Series seriesLostLuggage;
    private ObservableList<PieChart.Data> pieChartFoundData;
    private ObservableList<PieChart.Data> pieChartLostData;
    
    private int currentYear;

    private Map<Integer, Integer> dayValues;
    private Map<Integer, Integer> monthValues;
    private Map<Integer, Integer> yearValues;
    private Map<Integer, Integer> sortedMapFound;
    private Map<Integer, Integer> sortedMapLost;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    private Date resultDate;
    private Calendar cal;
    
    private String chosenTime;
    
    private int resultTime;
    private int startYear;
    private int endYear;
    private int yearsInterval;
    
    private ResourceBundle resource;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resource = rb;
        TITLE_DAY = rb.getString("statistics_day");
        TITLE_MONTH = rb.getString("statistics_month");
        TITLE_YEAR = rb.getString("statistics_year");
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
        
        seriesFoundLuggage = new XYChart.Series();
        seriesFoundLuggage.setName(resource.getString("foundLuggage"));
        seriesLostLuggage = new XYChart.Series();
        seriesLostLuggage.setName(resource.getString("lostLuggage"));
        
        pieChartFoundData = FXCollections.observableArrayList();
        pieChartLostData = FXCollections.observableArrayList();
        
        dayValues = new HashMap<>();
        monthValues = new HashMap<>();
        yearValues = new HashMap<>();
    }
    
    /*
     *  GET DATA FROM DATABASE AND CREATE MAP (WITH FOUND DATA) FOR CHARTS
     */
    private Map<Integer, Integer> getFilteredChartData(int pLoopLength, Map<Integer,Integer> pMap, 
                                                       int whichTime, int pAmountAddInLoop, String whichLuggage,
                                                       boolean isStartUpChart) throws SQLException{
        
        for(int i = 0; i < pLoopLength; i++) {
            pMap.put((pAmountAddInLoop+i), 0);
        }
        
        if(isStartUpChart){
            sql = "SELECT * FROM `RegisterLuggage` WHERE status='" + whichLuggage + "' AND YEAR(insertDate) = " + currentYear;
        } else{
            sql = "SELECT * FROM `RegisterLuggage` WHERE status='" + whichLuggage + "' AND date >= '" + startDate + "' AND date <= '" + endDate + "'";
        }
        
        result = statement.executeQuery(sql);

        while (result.next()){                
            resultDate = result.getDate("date");
            cal = Calendar.getInstance();
            cal.setTime(resultDate);
            resultTime = cal.get(whichTime);    // whichTime = Calendar.DAY_OF_WEEK or Calendar.MONTH or Calendar.YEAR

            pMap.put(resultTime, (pMap.get(resultTime) + 1));
        }
        result.close();      
        return new TreeMap<>(pMap);
    }
    
    /*
     *  SHOW VALUES OF CURRENT YEAR PER MONTH (DEFAULT)
     */
    private void initChart() throws SQLException{
        lineChart.setTitle(TITLE_YEAR);
        pieChart.setTitle(TITLE_YEAR);
        
        sortedMapFound = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, Constants.STATUS_FOUND, true);
        sortedMapLost = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, Constants.STATUS_LOST, true);
        
        for(int key : sortedMapFound.keySet()) {
            seriesFoundLuggage.getData().add(new XYChart.Data(xMonths[key], sortedMapFound.get(key)));
            pieChartFoundData.add(new PieChart.Data(xMonths[key],sortedMapFound.get(key)));
        }
        for(int key : sortedMapLost.keySet()) {
            seriesLostLuggage.getData().add(new XYChart.Data(xMonths[key], sortedMapLost.get(key)));
            pieChartLostData.add(new PieChart.Data(xMonths[key],sortedMapLost.get(key)));
        }

        lineChart.getData().addAll(seriesFoundLuggage, seriesLostLuggage);
        pieChart.setData(pieChartFoundData);
        pieChart.setData(pieChartLostData);
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER DAY
     */
    private void displayDay() throws SQLException{
        lineChart.setTitle(TITLE_DAY + " (" + String.valueOf(startDate) + " " + resource.getString("till") + " " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_DAY + " (" + String.valueOf(startDate) + " " + resource.getString("till") + " " + String.valueOf(endDate) + ")");
        
        sortedMapFound = getFilteredChartData(xWeekdays.length, dayValues, Calendar.DAY_OF_WEEK, 0, Constants.STATUS_FOUND, false);
        sortedMapLost = getFilteredChartData(xWeekdays.length, dayValues, Calendar.DAY_OF_WEEK, 0, Constants.STATUS_LOST, false);
                   
        for (int key : sortedMapFound.keySet()) {
            seriesFoundLuggage.getData().add(new XYChart.Data(xWeekdays[key], sortedMapFound.get(key)));
            pieChartFoundData.add(new PieChart.Data(xWeekdays[key],sortedMapFound.get(key)));
        }
        for (int key : sortedMapLost.keySet()) {
            seriesLostLuggage.getData().add(new XYChart.Data(xWeekdays[key], sortedMapFound.get(key)));
            pieChartLostData.add(new PieChart.Data(xWeekdays[key],sortedMapLost.get(key)));
        }

        lineChart.getData().addAll(seriesFoundLuggage, seriesLostLuggage);
        pieChart.setData(pieChartFoundData);
        pieChart.setData(pieChartLostData);  
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER MONTH
     */
    private void displayMonth() throws SQLException{        
        lineChart.setTitle(TITLE_MONTH + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_MONTH + " (" + String.valueOf(startDate) + " till " + String.valueOf(endDate) + ")");
                 
        sortedMapFound = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, Constants.STATUS_FOUND, false);
        sortedMapLost = getFilteredChartData(xMonths.length, monthValues, Calendar.MONTH, 0, Constants.STATUS_LOST, false);

        for (int key : sortedMapFound.keySet() ) {
            seriesFoundLuggage.getData().add(new XYChart.Data(xMonths[key], sortedMapFound.get(key)));
            pieChartFoundData.add(new PieChart.Data(xMonths[key],sortedMapFound.get(key)));
        }
        for (int key : sortedMapLost.keySet() ) {
            seriesLostLuggage.getData().add(new XYChart.Data(xMonths[key], sortedMapLost.get(key)));
            pieChartLostData.add(new PieChart.Data(xMonths[key],sortedMapLost.get(key)));
        }

        lineChart.getData().addAll(seriesFoundLuggage, seriesLostLuggage);
        pieChart.setData(pieChartFoundData);
        pieChart.setData(pieChartLostData);     
        
    }
    
    /*
     *  SHOW VALUES FROM STARTDATE TO ENDDATE PER YEAR (MUST HAVE MIN. 1 YEAR INTERVAL)
     */
    private void displayYear() throws SQLException{
        lineChart.setTitle(TITLE_YEAR + " (" + String.valueOf(startDate) + " " + resource.getString("till") + " " + String.valueOf(endDate) + ")");
        pieChart.setTitle(TITLE_YEAR + " (" + String.valueOf(startDate) + " " + resource.getString("till") + " " + String.valueOf(endDate) + ")");
        
        if(validStartEndDate()){            
            sortedMapFound = getFilteredChartData(yearsInterval, yearValues, Calendar.YEAR, startYear, Constants.STATUS_FOUND, false);
            sortedMapLost = getFilteredChartData(yearsInterval, yearValues, Calendar.YEAR, startYear, Constants.STATUS_LOST, false);
   
            for (int key : sortedMapFound.keySet() ) {                
                seriesFoundLuggage.getData().add(new XYChart.Data(String.valueOf(key), sortedMapFound.get(key)));
                pieChartFoundData.add(new PieChart.Data(String.valueOf(key), sortedMapFound.get(key)));
            }
            for (int key : sortedMapLost.keySet() ) {                
                seriesLostLuggage.getData().add(new XYChart.Data(String.valueOf(key), sortedMapLost.get(key)));
                pieChartLostData.add(new PieChart.Data(String.valueOf(key), sortedMapLost.get(key)));
            }
            
            lineChart.getData().addAll(seriesFoundLuggage, seriesLostLuggage);
            pieChart.setData(pieChartFoundData);
            pieChart.setData(pieChartLostData); 
        }
    }
    
    /*
    *  CHECK IF STARTDATE TO ENDDATE IS MIN. 1 YEAR INTERVAL
    */
    private boolean validStartEndDate(){
        if((yearsInterval) <= 0){
            DialogCreator.createErrorDialog("", "Er ging iets mis", "Controleer de start en eind datum");
            return false;
        }
        return true;
    }
    
    /*
    *  CLEAR CHARTS AND CREATED MAPS (THAT CONTAIN OLD DATA)
    */
    private void resetCharts(){
        seriesFoundLuggage = null;
        seriesLostLuggage = null;
        pieChartFoundData = null;
        pieChartLostData = null;
        
        dayValues.clear();
        monthValues.clear();
        yearValues.clear();
        lineChart.getData().clear();
        pieChart.getData().clear();
        
        seriesFoundLuggage = new XYChart.Series();
        seriesFoundLuggage.setName(resource.getString("foundLuggage"));
        seriesLostLuggage = new XYChart.Series();
        seriesLostLuggage.setName(resource.getString("lostLuggage"));
        
        pieChartFoundData = FXCollections.observableArrayList();
        pieChartLostData = FXCollections.observableArrayList();
    }
}