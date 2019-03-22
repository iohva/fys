
package com.mycompany.fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.DialogCreator;
import com.mycompany.fys_tripperssmaven.interfaces.Queryable;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import com.mycompany.fys_tripperssmaven.utils.Validation;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;



public class EditController_lostLuggage extends BaseClass implements Initializable, Queryable{

    @FXML private JFXDatePicker datepkr_registerDate;
    @FXML private JFXTimePicker timepkr_registerTime;
    @FXML private JFXComboBox<String> comboBx_registerLocation;
    @FXML private JFXComboBox comboBx_registerStatus;
    @FXML private JFXTextField txtField_firstname;
    @FXML private JFXTextField txtField_infix;
    @FXML private JFXTextField txtField_lastname;
    @FXML private JFXDatePicker datePkr_birth;
    @FXML private JFXTextField txtField_address;
    @FXML private JFXTextField txtField_place;
    @FXML private JFXTextField txtField_zipcode;
    @FXML private JFXTextField txtField_country;
    @FXML private JFXTextField txtField_phoneNumber;
    @FXML private JFXTextField txtField_email;
    @FXML private JFXTextField txtField_labelNumber;
    @FXML private JFXTextField txtField_flightNumber;
    @FXML private JFXComboBox<String> comboBx_destination;
    @FXML private JFXComboBox<String> comboBx_typeLuggage;
    @FXML private JFXComboBox<String> comboBx_brandLuggage;
    @FXML private JFXTextField txtField_otherBrand;
    @FXML private JFXComboBox<String> comboBx_colorLuggage;
    @FXML private JFXTextField txtField_otherColor;
    @FXML private JFXTextArea txtArea_charactersLugagge;
    @FXML private JFXButton btn_submit;
    
    private final String SELECT_LOST_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
                                                 + "INNER JOIN Airport AS registerLocation "
                                                 + "ON RegisterLuggage.idAirport = registerLocation.id  "
                                                 + "INNER JOIN LuggageLabelInformation AS label "
                                                 + "ON RegisterLuggage.idLuggage = label.idLuggage "
                                                 + "INNER JOIN Airport AS destination "
                                                 + "ON label.destination = destination.id "
                                                 + "INNER JOIN Luggage "
                                                 + "ON RegisterLuggage.idLuggage=Luggage.id  "
                                                 + "INNER JOIN Traveller "
                                                 + "ON Luggage.idTraveller = Traveller.id  "
                                                 + "INNER JOIN LuggageType "
                                                 + "ON Luggage.idLuggageType = LuggageType.id  "
                                                 + "INNER JOIN LuggageBrand "
                                                 + "ON Luggage.idLuggageBrand = LuggageBrand.id "
                                                 + "INNER JOIN LuggageColor "
                                                 + "ON Luggage.idLuggageColor = LuggageColor.id "
                                                 + "WHERE RegisterLuggage.status = '" + Constants.STATUS_LOST + "' AND RegisterLuggage.idLuggage = ";
    
    private final String UPDATE_TRAVELLER = "UPDATE `Traveller`"
                                          + "SET firstname = ?, infix = ?, lastname = ?, dateOfBirth = ?, "
                                          +     "address = ?, place = ?, zipcode = ?, country = ?, phone = ?, email = ? "
                                          + "WHERE id = ?";
    
    private final String UPDATE_LUGAGGE = "UPDATE `Luggage`"
                                        + "SET characteristics = ?, idTraveller = ?, idLuggageType = ?, idLuggageBrand = ?, idLuggageColor = ? "
                                        + "WHERE id = ?";
    
    private final String UPDATE_LUGAGGE_LABEL = "UPDATE IGNORE `LuggageLabelInformation`"
                                              + "SET labelNumber = ?, flightNumber = ?, idLuggage = ?, destination = ? "
                                              + "WHERE labelNumber = ?";
    
    private final String UPDATE_LUGAGGE_REGISTER = "UPDATE `RegisterLuggage`"
                                                 + "SET date = ?, time = ?, status = ?, registeredBy = ?, idAirport = ? " 
                                                 + "WHERE idLuggage = ?";
    
    private final String OPTION_LOST = "Lost";
    private final String OPTION_FOUND = "Found";
    private final String OPTION_ROUNDED = "Rounded";
    
    private LocalDate registerDate;
    private LocalTime registerTime;
    private String registerStatus;
    
    private String firstName;
    private String infix;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String place;
    private String zipcode;
    private String country;
    private String phoneNumber;
    private String email;
    
    private String labelNumber;
    private String flightNumber;
    
    private String luggageCharacters;
    
    private int registerPlace;
    private int destinationId;
    private int luggageTypeId;
    private int luggageBrandId;
    private int luggageColorId;
    
    private int ress;
    
    private ArrayList elementsList; 
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println(idLostLuggage);
        
        try {
            initValidations();
            initCheckBoxes();
            getValuesFromDb();
        } catch (SQLException ex) {
            Logger.getLogger(EditController_lostLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void openOverviewLostLuggage() throws IOException{
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.LOST_LUGGAGE);
    }
    
    @FXML
    private void changed(){
        btn_submit.setDisable(false);
    }
    
    @FXML
    @Override
    public void sendToDatabase() {
        if(Validation.checkFields(elementsList)){
            try {
                getValues();
                if(updateTraveller()&& updateLuggage()&& updateLuggageLabelInfo()&& updateLuggageRegister()) 
                    DialogCreator.createOkDialog("", "Gelukt!", "U heeft een verloren koffer aangepast");                                       
                else 
                    DialogCreator.createErrorDialog("", "Mislukt", "Verloren koffer aanpassen is mislukt");
            } 
            catch (SQLException ex) {
                Logger.getLogger(EditController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void initCheckBoxes(){
        try {
            loadComboBoxesFromDb(comboBx_registerLocation, "SELECT * FROM `Airport`", "nameAirport");
            comboBx_registerStatus.getItems().addAll(OPTION_LOST, OPTION_FOUND, OPTION_ROUNDED);
            loadComboBoxesFromDb(comboBx_destination, "SELECT * FROM `Airport`", "nameAirport");
            loadComboBoxesFromDb(comboBx_typeLuggage, "SELECT * FROM `LuggageType`", "name");
            loadComboBoxesFromDb(comboBx_brandLuggage, "SELECT * FROM `LuggageBrand`", "name");
            loadComboBoxesFromDb(comboBx_colorLuggage, "SELECT * FROM `LuggageColor`", "nameColor");
        } catch (SQLException ex) {
            Logger.getLogger(AddController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getValuesFromDb() throws SQLException{
        sql = SELECT_LOST_LUGGAGE + idLostLuggage;
        System.out.println(sql);
        
        result = statement.executeQuery(sql);
        
        while (result.next()){
            datepkr_registerDate.setValue(result.getDate("date").toLocalDate());
            timepkr_registerTime.setValue(result.getTime("time").toLocalTime());
            comboBx_registerLocation.setValue(result.getString("registerLocation.nameAirport"));
            comboBx_registerStatus.setValue(result.getString("registerluggage.status"));

            txtField_firstname.setText(result.getString("firstname"));
            txtField_infix.setText(result.getString("infix"));
            txtField_lastname.setText(result.getString("lastname"));
            datePkr_birth.setValue(result.getDate("dateOfBirth").toLocalDate());
            txtField_address.setText(result.getString("address"));
            txtField_place.setText(result.getString("place"));
            txtField_zipcode.setText(result.getString("zipcode"));
            txtField_country.setText(result.getString("country"));
            txtField_phoneNumber.setText(result.getString("phone"));
            txtField_email.setText(result.getString("email"));

            txtField_labelNumber.setText(result.getString("labelNumber"));
            txtField_flightNumber.setText(result.getString("flightNumber"));
            comboBx_destination.setValue(result.getString("destination.nameAirport"));

            comboBx_typeLuggage.setValue(result.getString("LuggageType.name"));
            comboBx_brandLuggage.setValue(result.getString("LuggageBrand.name"));
            comboBx_colorLuggage.setValue(result.getString("LuggageColor.nameColor"));
            txtArea_charactersLugagge.setText(result.getString("characteristics"));
        }
        result.close();
    }
    
    private void getValues() throws SQLException{
        registerDate = datepkr_registerDate.getValue();
        registerTime = timepkr_registerTime.getValue();
        registerPlace = getIdOfTableValue("Airport", "id", "nameAirport", comboBx_registerLocation.getValue());
        registerStatus = comboBx_registerStatus.getValue().toString();
        
        firstName = txtField_firstname.getText();
        infix = txtField_infix.getText();
        lastName = txtField_lastname.getText();
        dateOfBirth = datePkr_birth.getValue();
        address = txtField_address.getText();
        place = txtField_place.getText();
        zipcode = txtField_zipcode.getText();
        country = txtField_country.getText();
        phoneNumber = txtField_phoneNumber.getText();
        email = txtField_email.getText();
        
        labelNumber = txtField_labelNumber.getText();
        flightNumber = txtField_flightNumber.getText();
        destinationId = getIdOfTableValue("Airport", "id", "nameAirport", comboBx_destination.getValue());
        
        luggageTypeId = getIdOfTableValue("LuggageType", "id", "name", comboBx_typeLuggage.getValue());
        luggageBrandId = getIdOfTableValue("LuggageBrand", "id", "name", comboBx_brandLuggage.getValue());
        luggageColorId = getIdOfTableValue("LuggageColor", "id", "nameColor", comboBx_colorLuggage.getValue());
        luggageCharacters = txtArea_charactersLugagge.getText();
    }
    
    private int getIdOfTableValue(String pTable,String pIdHeader, String pWhere, String pNameValue) throws SQLException{
        int receivedInt = 0;
        
        sql = "SELECT `" + pIdHeader + "` FROM `" + pTable + "` WHERE "+ pWhere + "='" + pNameValue + "'";
        result = statement.executeQuery(sql);
        
        while (result.next()){                
            receivedInt = result.getInt(1);
        }
        result.close();
        resetDatabase();
        
        return receivedInt;
    }

    @Override
    public void loadComboBoxesFromDb(JFXComboBox pComboBox, String pSql, String pTableHeader) throws SQLException {
        sql = pSql;
        result = statement.executeQuery(sql);
        
        while (result.next()){                
            pComboBox.getItems().add(result.getString(pTableHeader));
        }
        result.close();
    }
    
    @Override
    public void initValidations() {
        elementsList = new ArrayList();
        
        elementsList.add(datepkr_registerDate);
        elementsList.add(timepkr_registerTime);
        elementsList.add(comboBx_registerLocation);
        elementsList.add(txtField_firstname);
        elementsList.add(txtField_infix);
        elementsList.add(txtField_lastname);
        elementsList.add(txtField_address);
        elementsList.add(txtField_place);
        elementsList.add(txtField_zipcode);
        elementsList.add(txtField_country);
        elementsList.add(txtField_phoneNumber);
        elementsList.add(txtField_email);
        elementsList.add(txtField_labelNumber);
        elementsList.add(txtField_flightNumber);
        elementsList.add(comboBx_destination);
        elementsList.add(comboBx_typeLuggage);
        elementsList.add(comboBx_brandLuggage);
        elementsList.add(txtField_otherBrand);
        elementsList.add(comboBx_colorLuggage);
        elementsList.add(txtField_otherColor);
        elementsList.add(txtArea_charactersLugagge);
    }
    
    private boolean updateTraveller() throws SQLException{
       
        System.out.println("TRAVELLER " + UPDATE_TRAVELLER);
        System.out.println("TRAVELLERID " + idTraveller);
                
        preparedStatement = dbConnection.prepareStatement(UPDATE_TRAVELLER);

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, infix);
        preparedStatement.setString(3, lastName);
        preparedStatement.setDate(4, Date.valueOf(dateOfBirth));
        preparedStatement.setString(5, address);
        preparedStatement.setString(6, place);
        preparedStatement.setString(7, zipcode);
        preparedStatement.setString(8, country);
        preparedStatement.setString(9, phoneNumber);
        preparedStatement.setString(10, email);
        preparedStatement.setInt(11, idTraveller);

        ress = preparedStatement.executeUpdate();

        preparedStatement.close();
        
        return ress != 0;
    }
    
    private boolean updateLuggage() throws SQLException{
        
        System.out.println("LUGGAGE " + UPDATE_LUGAGGE);
                
        preparedStatement = dbConnection.prepareStatement(UPDATE_LUGAGGE);

        preparedStatement.setString(1, luggageCharacters);
        preparedStatement.setInt(2, idTraveller);
        preparedStatement.setInt(3, luggageTypeId);
        preparedStatement.setInt(4, luggageBrandId);
        preparedStatement.setInt(5, luggageColorId);
        preparedStatement.setInt(6, idLostLuggage);

        ress = preparedStatement.executeUpdate();
        
        preparedStatement.close();     
        
        return ress != 0;
    }
    
    private boolean updateLuggageLabelInfo() throws SQLException{
        
        System.out.println("LUGGAGE LABEL " + UPDATE_LUGAGGE_LABEL);
                
        preparedStatement = dbConnection.prepareStatement(UPDATE_LUGAGGE_LABEL);

        preparedStatement.setString(1, labelNumber);
        preparedStatement.setString(2, flightNumber);
        preparedStatement.setInt(3, idLostLuggage);
        preparedStatement.setInt(4, destinationId);
        preparedStatement.setString(5, idLabelInfo);
        
        ress = preparedStatement.executeUpdate();
    
        preparedStatement.close();   
        
        return ress != 0;
    }
    
    private boolean updateLuggageRegister() throws SQLException{
        
        System.out.println("LUGGAGE REGISTER " + UPDATE_LUGAGGE_REGISTER);
                
        preparedStatement = dbConnection.prepareStatement(UPDATE_LUGAGGE_REGISTER);

        preparedStatement.setDate(1, Date.valueOf(registerDate));
        preparedStatement.setTime(2, Time.valueOf(registerTime));
        preparedStatement.setString(3, registerStatus);
        preparedStatement.setInt(4, user.getId());
        preparedStatement.setInt(5, registerPlace);
        preparedStatement.setInt(6, idLostLuggage);

        ress = preparedStatement.executeUpdate();
        
        preparedStatement.close();      
        
        return ress != 0;
    }
}
