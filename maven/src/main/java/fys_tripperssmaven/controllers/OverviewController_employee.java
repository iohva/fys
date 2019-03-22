
package fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.utils.Constants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OverviewController_employee extends BaseClass implements Initializable {

    @FXML TableView<User> table_user;
    @FXML TableColumn<User, Number> column_id;
    @FXML TableColumn<User, String> column_firstname;
    @FXML TableColumn<User, String> column_infix;
    @FXML TableColumn<User, String> column_lastname;
    @FXML TableColumn<User, String> column_dateOfBirth;
    @FXML TableColumn<User, String> column_phoneNumber;
    @FXML TableColumn<User, String> column_email;
    @FXML TableColumn<User, String> column_detailUser;
    @FXML TableColumn<User, String> column_editUser;
    
    private final String SELECT_ALL_USER = "SELECT * FROM `User`";
    
    private String firstName;
    private String infix;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private String username;

    private ObservableList<User> userData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @FXML
    private void loadData() throws SQLException{   
        userData = FXCollections.observableArrayList();
        result = statement.executeQuery(SELECT_ALL_USER);
        
        while (result.next()){            
            idUser = result.getInt("id");
            firstName = result.getString("firstname");
            infix = result.getString("infix");
            lastName = result.getString("lastname");
            dateOfBirth = result.getDate("dateOfBirth");
            System.out.println("DATEE" + dateOfBirth);
            phoneNumber = result.getString("phonenumber");
            email = result.getString("email");
            username = result.getString("username");
                        
            userData.add(new User(idUser, firstName, infix, lastName, dateOfBirth, phoneNumber, email, username));
        }
        result.close();    
        
        column_id.setCellValueFactory(new PropertyValueFactory<User, Number>("idUserr"));
        column_firstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        column_infix.setCellValueFactory(new PropertyValueFactory<User, String>("infix"));
        column_lastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        column_dateOfBirth.setCellValueFactory(new PropertyValueFactory<User, String>("dateOfBirth"));
        column_phoneNumber.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        column_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        column_detailUser.setCellValueFactory(new PropertyValueFactory<User, String>("btnDetailUser"));
        column_editUser.setCellValueFactory(new PropertyValueFactory<User, String>("btnEditUser"));
                
        table_user.setItems(userData);
    }
    
    @FXML
    private void openAddUserPage() throws IOException{
        openScrollPage(Constants.ADD_SCREEN, Constants.EMPLOYEE, true);
    }
    
    /*
     * data class
     */
    public final class User {
        private final IntegerProperty idUserr;
        private final StringProperty firstName;
        private final StringProperty infix;
        private final StringProperty lastName;
        private final StringProperty dateOfBirth;
        private final StringProperty phoneNumber;
        private final StringProperty email;
        private final StringProperty username;
        private final JFXButton btnDetailUser;
        private final JFXButton btnEditUser;
        
        public User(int pIdUser, String pFirstname, String pInfix, String pLastname, 
                    Date pDateOfBirth, String pPhoneNumber, String pEmail, String pUsername) {
            
            this.idUserr = new SimpleIntegerProperty(pIdUser);
            this.firstName = new SimpleStringProperty(pFirstname);
            this.infix = new SimpleStringProperty(pInfix);
            this.lastName = new SimpleStringProperty(pLastname);
            this.dateOfBirth = new SimpleStringProperty(String.valueOf(pDateOfBirth));
            this.phoneNumber = new SimpleStringProperty(pPhoneNumber);
            this.email = new SimpleStringProperty(pEmail);
            this.username = new SimpleStringProperty(pUsername);
            this.btnDetailUser = new JFXButton(resourcesUser.getString("details"));
            this.btnEditUser = new JFXButton(resourcesUser.getString("edit"));
        }

        public int getIdUserr() {
            return idUserr.get();
        }
        
        public String getFirstName() {
            return firstName.get();
        }

        public String getInfix() {
            return infix.get();
        }
        
        public String getLastName() {
            return lastName.get();
        }
        
        public String getDateOfBirth() {
            return dateOfBirth.get();
        }
       
        public String getPhoneNumber() {
            return phoneNumber.get();
        }
        
        public String getEmail() {
            return email.get() + " / " + username.get();
        }
        
        public Button getBtnDetailUser() {
            btnDetailUser.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        idUser = idUserr.get();
                        openScrollPage(Constants.DETAIL_SCREEN, Constants.EMPLOYEE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnDetailUser;
        }
       
        public Button getBtnEditUser() {
            btnEditUser.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        idUser = idUserr.get();
                        openScrollPage(Constants.EDIT_SCREEN, Constants.EMPLOYEE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnEditUser;
        }
    }
}
