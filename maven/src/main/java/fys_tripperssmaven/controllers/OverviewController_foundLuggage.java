
package fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
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


public class OverviewController_foundLuggage extends BaseClass implements Initializable {

    @FXML JFXRadioButton radiobtn_showAll;
    @FXML JFXRadioButton radiobtn_showMine;
    
    @FXML TableView<FoundLuggage> table_foundLuggage;
    @FXML TableColumn<FoundLuggage, Number> column_id;
    @FXML TableColumn<FoundLuggage, String> column_date;
    @FXML TableColumn<FoundLuggage, String> column_airport;
    @FXML TableColumn<FoundLuggage, String> column_nameTraveller;
    @FXML TableColumn<FoundLuggage, String> column_labelNumber;
    @FXML TableColumn<FoundLuggage, String> column_typeLuggage;
    @FXML TableColumn<FoundLuggage, String> column_detailLuggage;
    @FXML TableColumn<FoundLuggage, String> column_editLuggage;
    @FXML JFXButton btn_addFoundLuggage;
    
    private final String SELECT_ALL_FOUNDED_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
                                                    + "INNER JOIN Airport "
                                                    + "ON RegisterLuggage.idAirport = Airport.id "
                                                    + "INNER JOIN LuggageLabelInformation "
                                                    + "ON RegisterLuggage.idLuggage = LuggageLabelInformation.idLuggage "
                                                    + "INNER JOIN Luggage "
                                                    + "ON RegisterLuggage.idLuggage=Luggage.id "
                                                    + "INNER JOIN Traveller "
                                                    + "ON Luggage.idTraveller = Traveller.id "
                                                    + "INNER JOIN LuggageType "
                                                    + "ON Luggage.idLuggageType = LuggageType.id "
                                                    + "WHERE RegisterLuggage.status = '" + Constants.STATUS_FOUND + "'";
    
    private final String SELECT_MINE_FOUNDED_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
                                                     + "INNER JOIN Airport "
                                                     + "ON RegisterLuggage.idAirport = Airport.id "
                                                     + "INNER JOIN LuggageLabelInformation "
                                                     + "ON RegisterLuggage.idLuggage = LuggageLabelInformation.idLuggage "
                                                     + "INNER JOIN Luggage "
                                                     + "ON RegisterLuggage.idLuggage=Luggage.id "
                                                     + "INNER JOIN Traveller "
                                                     + "ON Luggage.idTraveller = Traveller.id "
                                                     + "INNER JOIN LuggageType "
                                                     + "ON Luggage.idLuggageType = LuggageType.id "
                                                     + "WHERE RegisterLuggage.status = '" + Constants.STATUS_FOUND + "' AND RegisterLuggage.registeredBy = " + user.getId();
    private int idLuggage;
    private Date date;
    private String time;
    private String nameAirport;
    private String firstName;
    private String infix;
    private String lastName;
    private String labelNumber;
    private String luggageType;
    private Button btnDetailLuggage;
    
    private ObservableList<FoundLuggage> foundLuggageData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if(user.getFullRole().equals(user.COMPENSATION)){
                radiobtn_showMine.setVisible(false);
                column_editLuggage.setVisible(false);
                btn_addFoundLuggage.setVisible(false);
            }
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void loadData() throws SQLException{   
        sql = (radiobtn_showAll.isSelected()) ? SELECT_ALL_FOUNDED_LUGGAGE 
                                              : SELECT_MINE_FOUNDED_LUGGAGE;
        
        foundLuggageData = FXCollections.observableArrayList();
        result = statement.executeQuery(sql);
        
        while (result.next()){
            
            idTraveller = result.getInt("Traveller.id");
            idLuggage = result.getInt("idLuggage");
            idLabelInfo = result.getString("labelNumber");
            date = result.getDate("date");
            time = result.getString("time");
            nameAirport = result.getString("nameAirport");
            firstName = result.getString("firstname");
            infix = result.getString("infix");
            lastName = result.getString("lastname");
            labelNumber = result.getString("labelNumber");
            luggageType = result.getString("LuggageType.name");
                        
            foundLuggageData.add(new FoundLuggage(idLuggage, date, time, nameAirport, firstName, infix, lastName, labelNumber, luggageType));
        }
        result.close();    
        
        column_id.setCellValueFactory(new PropertyValueFactory<FoundLuggage, Number>("idLuggage"));
        column_date.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("date"));
        column_airport.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("nameAirport"));
        column_nameTraveller.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("firstName"));
        column_labelNumber.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("labelNumber"));
        column_typeLuggage.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("luggageType"));
        column_detailLuggage.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("btnDetailLuggage"));
        
        if(!user.getFullRole().equals(user.COMPENSATION)) column_editLuggage.setCellValueFactory(new PropertyValueFactory<FoundLuggage, String>("btnEditLuggage"));
                
        table_foundLuggage.setItems(foundLuggageData);
    }
    
    @FXML
    private void openAddFoundLuggagePage() throws IOException{
        openScrollPage(Constants.ADD_SCREEN, Constants.FOUND_LUGGAGE, true);
    }
    
    /*
     * data class
     */
    public final class FoundLuggage {
        private final IntegerProperty idLuggage;
        private final StringProperty date;
        private final StringProperty time;
        private final StringProperty nameAirport;
        private final StringProperty firstName;
        private final StringProperty infix;
        private final StringProperty lastName;
        private final StringProperty labelNumber;
        private final StringProperty luggageType;
        private final JFXButton btnDetailLuggage;
        private final JFXButton btnEditLuggage;
        
        public FoundLuggage(int pIdLuggage, Date pDate, String pTime, 
                     String pNameAirport, String pFirstNameTraveller, 
                     String pInfixTraveller, String pLastNameTraveller, 
                     String pLabelNumber, String pLuggageType) {
            
            this.idLuggage = new SimpleIntegerProperty(pIdLuggage);
            this.date = new SimpleStringProperty(String.valueOf(pDate));
            this.time = new SimpleStringProperty(pTime);
            this.nameAirport = new SimpleStringProperty(pNameAirport);
            this.firstName = new SimpleStringProperty(pFirstNameTraveller);
            this.infix = new SimpleStringProperty(pInfixTraveller);
            this.lastName = new SimpleStringProperty(pLastNameTraveller);
            this.labelNumber = new SimpleStringProperty(pLabelNumber);
            this.luggageType = new SimpleStringProperty(pLuggageType);
            this.btnDetailLuggage = new JFXButton(resourcesUser.getString("details"));
            this.btnEditLuggage = new JFXButton(resourcesUser.getString("edit"));
        }

        public int getIdLuggage() {
            return idLuggage.get();
        }
        
        public String getDate() {
            return date.get();
        }

        public String getTime() {
            return time.get();
        }
        
        public String getNameAirport() {
            return nameAirport.get();
        }
        
        public String getFirstName() { 
            return (infix.get() != null) ? (firstName.get() + " " + infix.get() + " " + lastName.get()) 
                                         : (firstName.get() + " " + lastName.get());
        }
        
        public String getInfix() {
            return infix.get();
        }
        
        public String getLastName() {
            return lastName.get();
        }
        
        public String getLabelNumber() {
            return labelNumber.get();
        }
        
        public String getLuggageType() {
            return luggageType.get();
        }
        
        public Button getBtnDetailLuggage() {
            btnDetailLuggage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        idFoundLuggage = idLuggage.get();
                        openScrollPage(Constants.DETAIL_SCREEN, Constants.FOUND_LUGGAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnDetailLuggage;
        }
       
        public Button getBtnEditLuggage() {
            btnEditLuggage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        idFoundLuggage = idLuggage.get();
                        openScrollPage(Constants.EDIT_SCREEN, Constants.FOUND_LUGGAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnEditLuggage;
        }
    }
}
