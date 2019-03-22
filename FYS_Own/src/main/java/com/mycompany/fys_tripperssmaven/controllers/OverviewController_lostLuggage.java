package com.mycompany.fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class OverviewController_lostLuggage extends BaseClass implements Initializable {

    @FXML JFXRadioButton radiobtn_showAll;
    @FXML JFXRadioButton radiobtn_showMine;
    
    @FXML TableView<LostLuggage> table_lostLuggage;
    @FXML TableColumn<LostLuggage, Number> column_id;
    @FXML TableColumn<LostLuggage, String> column_date;
    @FXML TableColumn<LostLuggage, String> column_airport;
    @FXML TableColumn<LostLuggage, String> column_nameTraveller;
    @FXML TableColumn<LostLuggage, String> column_labelNumber;
    @FXML TableColumn<LostLuggage, String> column_typeLuggage;
    @FXML TableColumn<LostLuggage, String> column_detailLuggage;
    @FXML TableColumn<LostLuggage, String> column_editLuggage;
    @FXML JFXButton btn_addLostLuggage;
    
    private final String SELECT_ALL_LOST_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
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
                                                    + "WHERE RegisterLuggage.status = '" + Constants.STATUS_LOST + "'";
    
    private final String SELECT_MINE_LOST_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
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
                                                     + "WHERE RegisterLuggage.status = '" + Constants.STATUS_LOST + "' AND RegisterLuggage.registeredBy = " + user.getId();
    private int idLuggage;
    private Date date;
    private String time;
    private String nameAirport;
    private String firstName;
    private String infix;
    private String lastName;
    private String labelNumber;
    private String luggageType;
    
    private ObservableList<LostLuggage> foundLuggageData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if(user.getFullRole().equals(user.COMPENSATION)){
                radiobtn_showMine.setVisible(false);
                column_editLuggage.setVisible(false);
                btn_addLostLuggage.setVisible(false);
            }
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void loadData() throws SQLException{   
        sql = (radiobtn_showAll.isSelected()) ? SELECT_ALL_LOST_LUGGAGE 
                                              : SELECT_MINE_LOST_LUGGAGE;
        
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
                        
            foundLuggageData.add(new LostLuggage(idLuggage, date, time, nameAirport, firstName, infix, lastName, labelNumber, luggageType));
        }
        result.close();    
        
        column_id.setCellValueFactory(new PropertyValueFactory<LostLuggage, Number>("idLuggage"));
        column_date.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("date"));
        column_airport.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("nameAirport"));
        column_nameTraveller.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("firstName"));
        column_labelNumber.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("labelNumber"));
        column_typeLuggage.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("luggageType"));
        column_detailLuggage.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("btnDetailLuggage"));
        
        if(!user.getFullRole().equals(user.COMPENSATION)) column_editLuggage.setCellValueFactory(new PropertyValueFactory<LostLuggage, String>("btnEditLuggage"));
                
        table_lostLuggage.setItems(foundLuggageData);
    }
    
    @FXML
    private void openAddLostLuggagePage() throws IOException{
        openScrollPage(Constants.ADD_SCREEN, Constants.LOST_LUGGAGE, true);
    }
    
    /*
     * data class
     */
    public final class LostLuggage {
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
        
        private String fullName;

        public LostLuggage(int pIdLuggage, Date pDate, String pTime, 
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
                        idLostLuggage = idLuggage.get();
                        openScrollPage(Constants.DETAIL_SCREEN, Constants.LOST_LUGGAGE);
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
                        idLostLuggage = idLuggage.get();                        
                        openScrollPage(Constants.EDIT_SCREEN, Constants.LOST_LUGGAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnEditLuggage;
        }
    }
}
