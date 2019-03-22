
package com.mycompany.fys_tripperssmaven.controllers;

import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class NavigationDrawerController extends BaseClass implements Initializable {

    @FXML ImageView homeLogoButton;
    
    @FXML Button homeButton;
    
    @FXML MenuButton lostLuggageButton;
    @FXML MenuItem overviewLostLuggageButton;
    @FXML MenuItem addLostLuggageButton;
    
    @FXML MenuButton foundLuggageButton;
    @FXML MenuItem overviewFoundLuggageButton;
    @FXML MenuItem addFoundLuggageButton;
    
    @FXML MenuItem overviewExcelButton;
    @FXML MenuItem importExcelButton;
    
    @FXML MenuButton employeeButton;
    @FXML MenuItem overviewEmployeeButton;
    @FXML MenuItem addEmployeeButton;

    @FXML MenuButton statisticsAdmin;
    @FXML MenuItem statisticsAdminService;
    @FXML MenuItem statisticsAdminCom;
    
    @FXML Button statisticsService;
    @FXML Button statisticsCompensation;
    
    @FXML MenuButton helpButton;
    @FXML MenuItem helpManualButton;
        
    @FXML Button settingsButton;
    
    @FXML Button logoutButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(user != null){          
            switch (user.getRole()) {
                case AUTH_ADMIN:         showAdminButtons();
                                         break;
                case AUTH_SERVICE:       showServiceButtons();
                                         break;
                case AUTH_COMPENSATION:  showCompensationButtons();
                                         break; 
                default:                 System.out.println("NO ROLE FOUND FOR NAVIGATIONDRAWER");;
                                         break;
            }
        }
    }
    
    /* Open Home-screen if ImageView (banner) is clicked */
    @FXML
    private void bannerClick(MouseEvent e) throws IOException{
        openPage(Constants.GENERAL_SCREEN, Constants.HOME);
    }
    
    @FXML
    private void initButtons(ActionEvent event) throws IOException {
        
        System.out.println("DIT IS GEKLIKT: " + event.getSource());
        
        //TODO: Search for other option, current is messed up (SWITCH DOESN'T WORK)
        if (event.getSource() == homeButton) {
            openPage(Constants.GENERAL_SCREEN, Constants.HOME);
        }
        else if (event.getSource() == overviewLostLuggageButton) {
            openScrollPage(Constants.OVERVIEW_SCREEN, Constants.LOST_LUGGAGE);
        }
        else if (event.getSource() == addLostLuggageButton) {
            openScrollPage(Constants.ADD_SCREEN, Constants.LOST_LUGGAGE);
        }
        else if (event.getSource() == overviewFoundLuggageButton) {
            openScrollPage(Constants.OVERVIEW_SCREEN, Constants.FOUND_LUGGAGE);
        }
        else if (event.getSource() == addFoundLuggageButton) {
            openScrollPage(Constants.ADD_SCREEN, Constants.FOUND_LUGGAGE);
        }
        else if (event.getSource() == overviewExcelButton) {
            openScrollPage(Constants.OVERVIEW_SCREEN, Constants.EXCEL);
        }
        else if (event.getSource() == importExcelButton) {
            openPage(Constants.ADD_SCREEN, Constants.IMPORT_EXCEL);
        }
        else if (event.getSource() == overviewEmployeeButton) {
            openScrollPage(Constants.OVERVIEW_SCREEN, Constants.EMPLOYEE);
        }
        else if (event.getSource() == addEmployeeButton) {
            openScrollPage(Constants.ADD_SCREEN, Constants.EMPLOYEE);
        }
        else if (event.getSource() == statisticsAdminService) {
            openScrollPage(Constants.STATISTICS_SCREEN, Constants.SERVICE);
        }
        else if (event.getSource() == statisticsAdminCom) {
            openScrollPage(Constants.STATISTICS_SCREEN, Constants.COMPENSATION);
        }
        else if (event.getSource() == statisticsService) {
            openScrollPage(Constants.STATISTICS_SCREEN, Constants.SERVICE);
        }
        else if (event.getSource() == statisticsCompensation) {
            openScrollPage(Constants.STATISTICS_SCREEN, Constants.COMPENSATION);
        }
        else if (event.getSource() == helpButton) {
            openScrollPage(Constants.GENERAL_SCREEN, Constants.HELP);
        }
        else if (event.getSource() == helpManualButton) {
//      TODO: link this button to page: helpManual
        }
        else if (event.getSource() == settingsButton) {
            openScrollPage(Constants.GENERAL_SCREEN, Constants.SETTINGS);
        }
        else if (event.getSource() == logoutButton) {            
            logOut(logoutButton);
        }
    }
    
    private void showAdminButtons(){
        statisticsService.setVisible(false);
        statisticsService.setManaged(false);
        
        statisticsCompensation.setVisible(false);
        statisticsCompensation.setManaged(false);
    }  
    
    private void showServiceButtons(){
        employeeButton.setVisible(false);
        employeeButton.setManaged(false);
        
        statisticsAdmin.setVisible(false);
        statisticsAdmin.setManaged(false);
        
        statisticsCompensation.setVisible(false);
        statisticsCompensation.setManaged(false);      
    }  
    
    private void showCompensationButtons(){
        employeeButton.setVisible(false);
        employeeButton.setManaged(false);
        
        addFoundLuggageButton.setVisible(false);
        addLostLuggageButton.setVisible(false);
        importExcelButton.setVisible(false);
        
        statisticsAdmin.setVisible(false);
        statisticsAdmin.setManaged(false);
        
        statisticsService.setVisible(false);
        statisticsService.setManaged(false);      
    }  
}
