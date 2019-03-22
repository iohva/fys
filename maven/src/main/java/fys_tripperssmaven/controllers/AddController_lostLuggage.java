
package fys_tripperssmaven.controllers;

import com.jfoenix.controls.*;
import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.DialogCreator;
import fys_tripperssmaven.interfaces.Queryable;
import fys_tripperssmaven.utils.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddController_lostLuggage extends BaseClass implements Initializable, Queryable {

    private @FXML JFXDatePicker datePkr;
    private @FXML JFXTimePicker timePkr;
    private @FXML JFXComboBox comboBx_airport;
    
    private @FXML JFXTextField txtField_firstname;
    private @FXML JFXTextField txtField_infix;
    private @FXML JFXTextField txtField_lastname;
    private @FXML JFXDatePicker datePkr_birth;
    private @FXML JFXTextField txtField_address;
    private @FXML JFXTextField txtField_place;
    private @FXML JFXTextField txtField_zipcode;
    private @FXML JFXTextField txtField_country;
    private @FXML JFXTextField txtField_phoneNumber;
    private @FXML JFXTextField txtField_email;
    
    private @FXML JFXTextField txtField_labelNumber;
    private @FXML JFXTextField txtField_flightNumber;
    private @FXML JFXComboBox comboBx_destination;
    
    private @FXML JFXComboBox comboBx_typeLuggage;
    private @FXML JFXComboBox comboBx_brandLuggage;
    private @FXML JFXTextField txtField_otherBrand;
    private @FXML JFXComboBox comboBx_colorLuggage;    
    private @FXML JFXTextField txtField_otherColor;
    private @FXML JFXTextArea txtArea_charactersLugagge;
    
    private @FXML JFXButton btn_submit;
    
    private final String STATUS_LOST = "Lost";
    
    private int generatedTravellerId;
    private int generatedLuggageId;
    
    private String date;
    private String time;
    private int registerPlace;
    
    private String firstName;
    private String infix;
    private String lastName;
    private String dateBirth;
    private String address;
    private String place;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private String email;
    
    private String labelNumber;
    private String flightNumber;
    private int destinationId;
    
    private int luggageTypeId;
    private int luggageBrandId;
    private int luggageColorId;
    private String description;
    
    private ArrayList elementsList; 
    private ObservableList databaseList; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidations();
        initCheckBoxes();
    }    

    @Override
    public void initValidations() {
        elementsList = new ArrayList();
        databaseList = FXCollections.observableArrayList();
        datePkr.setValue(LocalDate.now());
        timePkr.setIs24HourView(setting.isTwentyFourHour());
        
        elementsList.add(datePkr);
        elementsList.add(timePkr);
        elementsList.add(comboBx_airport);
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

    @FXML
    @Override
    public void sendToDatabase() {
        if(Validation.checkFields(elementsList)){
            try {
                getValues();
                dbConnection.setAutoCommit(false);
                
                if(insertTraveller() && insertLuggage() && insertLuggageLabelInfo() && insertLuggageRegister()){
                    DialogCreator.createOkDialog("", "Gelukt!", "U heeft een verloren koffer toegevoegd");
                }
                else DialogCreator.createErrorDialog("", "Mislukt", "Verloren koffer toevoegen is mislukt");
                
            } catch (SQLException ex) {
                Logger.getLogger(AddController_lostLuggage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
    
    private void initCheckBoxes(){
        try {
            loadComboBoxesFromDb(comboBx_airport, "SELECT * FROM `Airport`", "nameAirport");
            loadComboBoxesFromDb(comboBx_destination, "SELECT * FROM `Airport`", "nameAirport");
            loadComboBoxesFromDb(comboBx_typeLuggage, "SELECT * FROM `LuggageType`", "name");
            loadComboBoxesFromDb(comboBx_brandLuggage, "SELECT * FROM `LuggageBrand`", "name");
            loadComboBoxesFromDb(comboBx_colorLuggage, "SELECT * FROM `LuggageColor`", "nameColor");
        } catch (SQLException ex) {
            Logger.getLogger(AddController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getValues() throws SQLException{
        date = datePkr.getValue().toString();
        time = timePkr.getValue().toString();
        registerPlace = getIdOfTableValue("Airport", "id", "nameAirport", comboBx_airport.getValue().toString());
        
        firstName = txtField_firstname.getText();
        infix = txtField_infix.getText();
        lastName = txtField_lastname.getText();
        dateBirth = datePkr_birth.getValue().toString();
        address = txtField_address.getText();
        place = txtField_place.getText();
        zipCode = txtField_zipcode.getText();
        country = txtField_country.getText();
        phoneNumber = txtField_phoneNumber.getText();
        email = txtField_email.getText();
        
        labelNumber = txtField_labelNumber.getText();
        flightNumber = txtField_flightNumber.getText();
        destinationId = getIdOfTableValue("Airport", "id", "nameAirport", comboBx_destination.getValue().toString());
        
        luggageTypeId = getIdOfTableValue("LuggageType", "id", "name", comboBx_typeLuggage.getValue().toString());
        luggageBrandId = getIdOfTableValue("LuggageBrand", "id", "name", comboBx_brandLuggage.getValue().toString());
        luggageColorId = getIdOfTableValue("LuggageColor", "id", "nameColor", comboBx_colorLuggage.getValue().toString());
        description = txtArea_charactersLugagge.getText();
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
    
    private boolean insertTraveller() throws SQLException{
        String insertTraveller = "INSERT INTO `Traveller`"
                               + "(firstname, infix, lastname, dateOfBirth, address, place, zipcode, country, phone, email)"
                               + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        
        System.out.println("TRAVELLER " + insertTraveller);
                
        preparedStatement = dbConnection.prepareStatement(insertTraveller, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, infix);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, dateBirth);
        preparedStatement.setString(5, address);
        preparedStatement.setString(6, place);
        preparedStatement.setString(7, zipCode);
        preparedStatement.setString(8, country);
        preparedStatement.setString(9, phoneNumber);
        preparedStatement.setString(10, email);
        preparedStatement.addBatch();
        ress = preparedStatement.executeBatch();

        result = preparedStatement.getGeneratedKeys();
        result.next();
        generatedTravellerId = result.getInt(1);

        System.out.println("ID IS : " + generatedTravellerId);
        
        result.close();
        resetDatabase();
        dbConnection.commit();
        preparedStatement.close();
        
        return ress.length != 0;
    }
    
    private boolean insertLuggage() throws SQLException{
        String insertLugagge = "INSERT INTO `Luggage`"
                               + "(characteristics, idTraveller, idLuggageType, idLuggageBrand, idLuggageColor)"
                               + "VALUES(?,?,?,?,?)";
        
        System.out.println("LUGGAGE " + insertLugagge);
                
        preparedStatement = dbConnection.prepareStatement(insertLugagge, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, description);
        preparedStatement.setInt(2, generatedTravellerId);
        preparedStatement.setInt(3, luggageTypeId);
        preparedStatement.setInt(4, luggageBrandId);
        preparedStatement.setInt(5, luggageColorId);
        preparedStatement.addBatch();
        ress = preparedStatement.executeBatch();

        result = preparedStatement.getGeneratedKeys();
        result.next();
        generatedLuggageId = result.getInt(1);

        System.out.println("ID IS : " + generatedLuggageId);
        
        result.close();
        resetDatabase();
        dbConnection.commit();
        preparedStatement.close();    
        
        return ress.length != 0;
    }
    
    private boolean insertLuggageLabelInfo() throws SQLException{
        String insertLugaggeLabel = "INSERT INTO `LuggageLabelInformation`"
                                  + "(labelNumber, flightNumber, idLuggage, destination)"
                                  + "VALUES(?,?,?,?)";
        
        System.out.println("LUGGAGE LABEL " + insertLugaggeLabel);
                
        preparedStatement = dbConnection.prepareStatement(insertLugaggeLabel);

        preparedStatement.setString(1, labelNumber);
        preparedStatement.setString(2, flightNumber);
        preparedStatement.setInt(3, generatedLuggageId);
        preparedStatement.setInt(4, destinationId);
        preparedStatement.addBatch();
        ress = preparedStatement.executeBatch();
        
        dbConnection.commit();
        preparedStatement.close();   
        
        return ress.length != 0;
    }
    
    private boolean insertLuggageRegister() throws SQLException{
        String insertLugaggeRegister = "INSERT INTO `RegisterLuggage`"
                                     + "(idLuggage, date, time, status, registeredBy, idAirport)"
                                     + "VALUES(?,?,?,?,?,?)";
        
        System.out.println("LUGGAGE REGISTER " + insertLugaggeRegister);
                
        preparedStatement = dbConnection.prepareStatement(insertLugaggeRegister);

        preparedStatement.setInt(1, generatedLuggageId);
        preparedStatement.setString(2, date);
        preparedStatement.setString(3, time);
        preparedStatement.setString(4, STATUS_LOST);
        preparedStatement.setInt(5, user.getId());
        preparedStatement.setInt(6, registerPlace);
        preparedStatement.addBatch();
        ress = preparedStatement.executeBatch();
        
        dbConnection.commit();
        preparedStatement.close();
        
        return ress.length != 0;
    }
}
