
package com.mycompany.fys_tripperssmaven.controllers;

import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class Controller_home extends BaseClass implements Initializable{
    
    @FXML private Pane otherPane;
    @FXML private Pane compensationPane;
    
    @FXML private Label titleHead;
    @FXML private Label labelBtnOne;
    @FXML private Label labelBtnTwo;
    @FXML private Label labelBtnThree;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleHead.setStyle("-fx-font: " + String.valueOf(setting.getFontSize()) + " arial;");
        
        switch (user.getFullRole()) {
            case "admin": initAdminService(resources);
                 break;
            case "service": initAdminService(resources);
                 break;
            case "compensation": initCompensation(resources);
                 break;
        }
    }
    
    @FXML
    private void openFoundLuggagePage() throws IOException{
        openScrollPage(Constants.ADD_SCREEN, Constants.FOUND_LUGGAGE);
    }
    
    @FXML
    private void openLostLuggagePage() throws IOException{
        openScrollPage(Constants.ADD_SCREEN, Constants.LOST_LUGGAGE);
    }
    
    @FXML
    private void openStatisticsPage() throws IOException{
        openPage(Constants.STATISTICS_SCREEN, Constants.SERVICE);
    }
    @FXML
    private void openOverviewFoundLuggage() throws IOException{
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.FOUND_LUGGAGE);
    }
    
    @FXML
    private void openOverviewLostLuggagePage() throws IOException{
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.LOST_LUGGAGE);
    }
    
    @FXML
    private void openStatisticsCompensation() throws IOException{
        openPage(Constants.STATISTICS_SCREEN, Constants.COMPENSATION);
    }
    
    private void initAdminService(ResourceBundle pRs){
        labelBtnOne.setText(pRs.getString("label_add_lostLuggage"));
        labelBtnTwo.setText(pRs.getString("label_add_foundLuggage"));
        labelBtnThree.setText(pRs.getString("label_see_statistics"));
    }
    
    private void initCompensation(ResourceBundle pRs){
        compensationPane.setVisible(true);
        otherPane.setVisible(false);
        
        labelBtnOne.setText(pRs.getString("overview_foundLuggage"));
        labelBtnTwo.setText(pRs.getString("overview_lostLuggage"));
        labelBtnThree.setText(pRs.getString("statistics"));
    }
}

