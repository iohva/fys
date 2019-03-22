
package com.mycompany.fys_tripperssmaven.controllers;

import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;


public class DetailController_employee extends BaseClass implements Initializable {
    
    @FXML private Text txt_name;
    @FXML private Text txt_dateOfBirth;
    @FXML private Text txt_phoneNumber;
    @FXML private Text txt_email;
    @FXML private Text txt_username;
    @FXML private Text txt_role;

    private final String SELECT_ALL_USERS = "SELECT * FROM `user` WHERE id = ";
    
    private String firstName;
    private String infix;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String username;
    private int role;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getValuesFromDb();
            setText();
        } catch (SQLException ex) {
            Logger.getLogger(DetailController_lostLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void openOverviewUsers() throws IOException {
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.EMPLOYEE);
    }
    
    @FXML
    private void openEditUser() throws IOException{
        openScrollPage(Constants.EDIT_SCREEN, Constants.EMPLOYEE);
    }
    
    private void getValuesFromDb() throws SQLException{
        System.out.println(SELECT_ALL_USERS);
        result = statement.executeQuery(SELECT_ALL_USERS + idUser);
        
        while (result.next()){
            firstName = result.getString("firstname");
            infix = result.getString("infix");
            lastName = result.getString("lastname");
            dateOfBirth = result.getDate("dateOfBirth");
            phoneNumber = result.getString("phonenumber");
            email = result.getString("email");
            username = result.getString("username");
            role = result.getInt("role");
        }
        result.close();
    }
    
    private void setText(){  
        txt_name.setText(getFullName());
        txt_dateOfBirth.setText(validateDate(dateOfBirth));
        txt_phoneNumber.setText(phoneNumber);
        txt_email.setText(email);
        txt_username.setText(username);
        txt_role.setText(getFullRole());
    }
    
    private String validateDate(Date pDate){
        return (pDate != null) ? String.valueOf(pDate)
                               : "-";
    }
    
    private String getFullName(){        
        return (infix != null || infix.length() > 1) ? (firstName + " " + infix + " " + lastName) 
                                                     : (firstName + " " + lastName);
    }
    
    public String getFullRole(){
        switch (role) {
            case 1:
                return user.ADMIN;
            case 2:
                return user.SERVICE;
            case 3:
                return user.COMPENSATION;
            default:
                return "empty role";
        }
    }
}
