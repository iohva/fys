
package com.mycompany.fys_tripperssmaven.controllers;

import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.DialogCreator;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller voor het bewerken van medewerker gegevens.
 * @author Raoul Ramawadh 500729457
 */
public class EditController_employee extends BaseClass implements Initializable {

    //ComboBox opties
    ObservableList<String> roleList = FXCollections.observableArrayList
    ("Manager","Service", "Schadevergoeding");
    
    //Declareren
    String username;
    private String passwordNew;
    private String repeatPasswordNew;
    private String currPassword;
    String firstNameNew;
    String infixNew;
    String lastNameNew;
    String emailNew;
    String phoneNmbrNew;
    String dobNew;
    String accountRoleNew;
       
    //FXML 
    @FXML TextField firstName;
    @FXML TextField infix;
    @FXML TextField lastName;
    @FXML TextField emailAddress;
    @FXML TextField phoneNmbr;
    @FXML TextField dateOfBirth;
    @FXML TextField usernameRegister;
    @FXML PasswordField currentPassword;
    @FXML PasswordField repeatNewPassword;
    @FXML ComboBox accountLevel;
    @FXML TextField txtSearchUser;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            accountLevel.setItems(roleList);
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(EditController_employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @FXML
    private void openEmployeeOverview() throws IOException{
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.EMPLOYEE);
    }
    
    @FXML
    private void handleSaveChanges(ActionEvent event) throws SQLException{
      getUpdatedInfo();
      
        if(passwordCheck() && checkRole() && updateData()) 
            DialogCreator.createWarningDialog("", resourcesUser.getString("succeeded"),  resourcesUser.getString("account_success_edited"));
        else 
            DialogCreator.createWarningDialog("", resourcesUser.getString("unfortunately"),  resourcesUser.getString("account_failed_edited"));
    }
    
    //Data uit de database halen
    private void getData() throws SQLException{
        String getUserDetails = "SELECT * FROM User WHERE id=?";
        preparedStatement = dbConnection.prepareStatement(getUserDetails);
        preparedStatement.setInt(1, idUser);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
           firstName.setText(rs.getString("firstname"));
           infix.setText(rs.getString("infix"));
           lastName.setText(rs.getString("lastname"));
           emailAddress.setText(rs.getString("email"));
           phoneNmbr.setText(rs.getString("phonenumber"));
           dateOfBirth.setText(rs.getString("dateOfBirth"));
           usernameRegister.setText(rs.getString("username"));
           currentPassword.setText(rs.getString("password"));
           currPassword = rs.getString("password");
           accountLevel.setValue(roleList.get(Integer.valueOf(rs.getString("role")) - 1));
        }
    }
    //Informatie opslaan in variabele
    private void getUpdatedInfo(){
        firstNameNew = firstName.getText();
        infixNew = infix.getText();
        lastNameNew = lastName.getText();
        dobNew = dateOfBirth.getText();
        phoneNmbrNew = phoneNmbr.getText();
        emailNew = emailAddress.getText();
        passwordNew = repeatNewPassword.getText();
        accountRoleNew = (String) accountLevel.getSelectionModel().getSelectedItem();
    }
    
    //Wachtwoord controleren
    private boolean passwordCheck(){
//        if (currentPassword.getText().trim().isEmpty() || 
//            repeatNewPassword.getText().trim().isEmpty()){
//            DialogCreator.createWarningDialog("", "Helaas", "Voer een wachtwoord in!");
//        } 
        
        if (!currentPassword.getText().equals(repeatNewPassword.getText())) {
            DialogCreator.createWarningDialog("", "Oeps..", "De wachtwoorden komen niet overeen");
            return false;
        } 
        return true;
    }
    
    //Controleert de rol van de gebruiker
    private boolean checkRole () {
        switch (accountRoleNew) {
            case "Manager":
                accountRoleNew = "1";
                break;
            case "Schadevergoeding":
                accountRoleNew = "3";
                break;
            case "Service":
                accountRoleNew = "2";
                break;
            default:
                return false;
        }
        return true;
    }
    
    //Data bijwerken in database
    private boolean updateData() throws SQLException{
        String updateUserDetails = "UPDATE User SET firstname=? , infix=?, lastname=?, dateOfBirth=?, phonenumber=?, email=?, password=?, role=? WHERE id=?"; 
        dbConnection.setAutoCommit(false);
        preparedStatement = dbConnection.prepareStatement(updateUserDetails);
        preparedStatement.setString(1, firstNameNew);
        preparedStatement.setString(2, infixNew);
        preparedStatement.setString(3, lastNameNew);
        preparedStatement.setString(4, dobNew);
        preparedStatement.setString(5, phoneNmbrNew);
        preparedStatement.setString(6, emailNew);
        preparedStatement.setString(7, decidePassword());
        preparedStatement.setString(8, accountRoleNew);
        preparedStatement.setInt(9, idUser);
        preparedStatement.addBatch();

        // Telt resultaat en sluit uitvoeren van query af
        int rows = preparedStatement.executeUpdate();
        dbConnection.commit();
        preparedStatement.close();

        // Beantwoord de vraag: is query succesvol uitgevoerd? Geeft true of false als antwoord
        return rows >= 1;
    }
    
    // Bepaalt welke wachtwoord in database gezet wordt (voor als wachtwoord bijv. niet is aangepast)
    private String decidePassword(){
        return (passwordNew.trim().isEmpty()) ? currPassword 
                                              : passwordNew;
    }
}