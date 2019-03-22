package com.mycompany.fys_tripperssmaven.controllers;
import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.DialogCreator;
import com.mycompany.fys_tripperssmaven.constants.Languages;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


public class Controller_login extends BaseClass implements Initializable {
            
    @FXML PasswordField fieldPassword;
    @FXML TextField fieldUsername;
    @FXML Button loginButton;
    @FXML Label label;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
    }    
    
    @FXML
    private void loginButtonAction() throws IOException, SQLException {
        if(loginValidation(fieldUsername.getText(), fieldPassword.getText())){
            init();
            openPage(Constants.GENERAL_SCREEN, Constants.HOME);
            initMain();
        }
        else{
            DialogCreator.createErrorDialog("Oepss...", "Helaas", "De ingevoerde gegevens kloppen niet, probeer opnieuw.");
        }        
    }
    
    @FXML
    private void setEN() throws IOException{
        resourcesUser = ResourceBundle.getBundle("bundles/language", Languages.EN);
        BaseClass.loginStackPane.getChildren().clear();
        BaseClass.loginStackPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcesUser));
    }
    
    @FXML
    private void setNL() throws IOException{
        resourcesUser = ResourceBundle.getBundle("bundles/language", Languages.NL);
        BaseClass.loginStackPane.getChildren().clear();
        BaseClass.loginStackPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcesUser));
    }
   
    @FXML
    private void setDE() throws IOException{
        resourcesUser = ResourceBundle.getBundle("bundles/language", Languages.DE);
        BaseClass.loginStackPane.getChildren().clear();
        BaseClass.loginStackPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcesUser));
    }
    
    /* Call @loginButtonAction-method if Enter is pressed */
    @FXML
    private void keyListener(KeyEvent event) throws IOException, SQLException{
        if(event.getCode() == KeyCode.ENTER) {
          loginButtonAction();
        }
    }
    
    private void initMain() throws IOException{
        fieldUsername.clear();
        fieldPassword.clear();
        
        loginFrag.setVisible(false);
        mainFragment.setVisible(true);
        navStackPane.getChildren().clear();
        navStackPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL + Constants.NAVIGATIONDRAWER), resourcesUser));
    }
}
