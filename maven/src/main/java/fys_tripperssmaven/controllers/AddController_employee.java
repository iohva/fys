/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fys_tripperssmaven.controllers;

import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.DialogCreator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 * Controller class voor het toevoegen van accounts voor de medewerkers.
 * @author Raoul Ramawadh 500729457
 */
public class AddController_employee extends BaseClass implements Initializable {

    //ComboBox opties
     ObservableList<String> roleList = FXCollections.observableArrayList
     ("Manager", "Service", "Schadevergoeding");
    
    //Declareren
    String username;
    private String password;
    private String repeatPasswordReg;
    String firstNameReg;
    String infixReg;
    String lastNameReg;
    String emailReg;
    String phoneNmbrReg;
    String dobReg;
    String accountRoleReg;
    int newUserId;
    //FXML 
    @FXML TextField firstName;
    @FXML TextField infix;
    @FXML TextField lastName;
    @FXML TextField emailAddress;
    @FXML TextField phoneNmbr;
    @FXML TextField dateOfBirth;
    @FXML TextField usernameRegister;
    @FXML PasswordField newPassword;
    @FXML PasswordField repeatPassword;
    @FXML ComboBox accountRole;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountRole.setValue ("Service");
        accountRole.setItems(roleList);
    }
    
    @FXML
    private void handleCreateAccount(ActionEvent event) throws SQLException{
        getRegInfo();
        passwordCheck();
        roleCheck();
        sendDataToDB();
        insertAccSettings();
    }
    
    //Accountgegevens van de gebruiker opslaan
    private void getRegInfo(){
        firstNameReg=firstName.getText(); 
        infixReg=infix.getText();
        lastNameReg=lastName.getText();
        emailReg=emailAddress.getText();
        phoneNmbrReg=phoneNmbr.getText();
        dobReg=dateOfBirth.getText();
        username=usernameRegister.getText();
        password=newPassword.getText();
        repeatPasswordReg=repeatPassword.getText(); 
        accountRoleReg=(String) accountRole.getSelectionModel().getSelectedItem();
    }

    
    //Controleren of de wachtwoorden overeenkomen
    private void passwordCheck() throws SQLException {
        if (newPassword.getText().trim().isEmpty() || 
            repeatPassword.getText().trim().isEmpty()){
            DialogCreator.createWarningDialog("", "Helaas", "Voer een wachtwoord in!");
        } 
        else if (password.equals(repeatPasswordReg)){
            DialogCreator.createOkDialog("", "Succes!", "Account is succesvol aangemaakt!");
        } 
        else if (password != (repeatPasswordReg)) {
            DialogCreator.createOkDialog("", "Oeps..", "De wachtwoorden komen niet overeen");
        }
    }
    //Controleert de rol van de gebruiker en zet dit om tot een getal voor in de database
    private void roleCheck(){
         switch (accountRoleReg) {
             case "Manager":
                 accountRoleReg = "1";
                 break;
             case "Schadevergoeding":
                 accountRoleReg = "3";
                 break;
             case "Service":
                 accountRoleReg = "2";
                 break;
             default:
                 break;
         }
    }
    
   //Accountgegevens sturen naar de database
    private void sendDataToDB () throws SQLException{
        
        String insertUserDetails = "INSERT INTO User"
                    + "(firstname, infix, lastname, dateOfBirth, phonenumber, email, username, password, role) " 
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            dbConnection.setAutoCommit(false);
    	    preparedStatement = dbConnection.prepareStatement(insertUserDetails, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, firstNameReg);
            preparedStatement.setString(2, infixReg);
            preparedStatement.setString(3, lastNameReg);
            preparedStatement.setString(4, dobReg);
            preparedStatement.setString(5, phoneNmbrReg);
            preparedStatement.setString(6, emailReg);
            preparedStatement.setString(7, username);
            preparedStatement.setString(8, password);
            preparedStatement.setString(9, accountRoleReg);
            preparedStatement.addBatch();
            preparedStatement.executeUpdate();
            
            ResultSet foundId = preparedStatement.getGeneratedKeys();
            if(foundId.next()){
                newUserId = foundId.getInt(1);
            }
            
            dbConnection.commit();
            preparedStatement.close();

    }
    
    private void insertAccSettings () throws SQLException {      
        String insertSetting = "INSERT INTO Setting"
                             + "(idUser, language, fontSize, timeType, maxOverviewExcel)"
                             + "VALUES (?, 'NL', 30, 24, 100)";
        
        dbConnection.setAutoCommit(false);
        preparedStatement = dbConnection.prepareStatement(insertSetting);
        preparedStatement.setInt(1, newUserId);
                    preparedStatement.addBatch();
        preparedStatement.executeUpdate();
                    dbConnection.commit();
        preparedStatement.close();
    }    
}