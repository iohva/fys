
package com.mycompany.fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
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


public class DetailController_foundLuggage extends BaseClass implements Initializable {
    
    @FXML private Text title;
    @FXML private Text txt_registerDate;
    @FXML private Text txt_registerTime;
    @FXML private Text txt_registerLocation;
    @FXML private Text txt_fullName;
    @FXML private Text txt_dateOfBirth;
    @FXML private Text txt_address;
    @FXML private Text txt_place;
    @FXML private Text txt_zipcode;
    @FXML private Text txt_country;
    @FXML private Text txt_phoneNumber;
    @FXML private Text txt_email;
    @FXML private Text txt_labelNumber;
    @FXML private Text txt_flightNumber;
    @FXML private Text txt_destination;
    @FXML private Text txt_luggageType;
    @FXML private Text txt_luggageBrand;
    @FXML private Text txt_luggageColor;
    @FXML private Text txt_luggageCharacters;
    @FXML private JFXButton btn_edit;
    
    private final String SELECT_ALL_FOUND_LUGGAGE = "SELECT * FROM `RegisterLuggage` "
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
                                                  + "WHERE RegisterLuggage.status = '" + Constants.STATUS_FOUND + "' AND RegisterLuggage.idLuggage = ";
    
    private String titleReplaced;
    private Date registerDate;
    private String registerTime;
    private String registerLocation;
    
    private String firstName;
    private String infix;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String place;
    private String zipcode;
    private String country;
    private String phoneNumber;
    private String email;
    
    private String labelNumber;
    private String flightNumber;
    private String destination;
    
    private String luggageType;
    private String luggageBrand;
    private String luggageColor;
    private String luggageCharacters;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(user.getFullRole().equals(user.COMPENSATION)) btn_edit.setVisible(false);
        titleReplaced = title.getText().replace("00", String.valueOf(idFoundLuggage));
        try {
            title.setText(titleReplaced);
            getValuesFromDb();
            setText();
        } catch (SQLException ex) {
            Logger.getLogger(DetailController_lostLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void openEditLostLuggage() throws IOException{
        openScrollPage(Constants.EDIT_SCREEN, Constants.FOUND_LUGGAGE);
    }
    
    @FXML
    private void openOverviewLostLuggage() throws IOException{
        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.FOUND_LUGGAGE);
    }
    
    private void getValuesFromDb() throws SQLException{
        sql = SELECT_ALL_FOUND_LUGGAGE + idFoundLuggage;
        System.out.println(sql);
        
        result = statement.executeQuery(sql);
        
        while (result.next()){      
            registerDate = result.getDate("date");
            registerTime = result.getString("time");
            registerLocation = result.getString("registerLocation.nameAirport");
            
            firstName = result.getString("firstname");
            infix = result.getString("infix");
            lastName = result.getString("lastname");
            dateOfBirth = result.getDate("dateOfBirth");
            address = result.getString("address");
            place = result.getString("place");
            zipcode = result.getString("zipcode");
            country = result.getString("country");
            phoneNumber = result.getString("phone");
            email = result.getString("email");
            
            labelNumber = result.getString("labelNumber");
            flightNumber = result.getString("flightNumber");
            destination = result.getString("destination.nameAirport");
            
            luggageType = result.getString("LuggageType.name");
            luggageBrand = result.getString("LuggageBrand.name");
            luggageColor = result.getString("LuggageColor.nameColor");
            luggageCharacters = result.getString("characteristics");
        }
        result.close();
    }
    
    private void setText(){                        
        txt_registerDate.setText(validateDate(registerDate));
        txt_registerTime.setText(registerTime);
        txt_registerLocation.setText(registerLocation);
        txt_fullName.setText(getFullName());
        txt_dateOfBirth.setText(validateDate(dateOfBirth));
        txt_address.setText(address);
        txt_place.setText(place);
        txt_zipcode.setText(zipcode);
        txt_country.setText(country);
        txt_phoneNumber.setText(phoneNumber);
        txt_email.setText(email);
        txt_labelNumber.setText(labelNumber);
        txt_flightNumber.setText(flightNumber);
        txt_destination.setText(String.valueOf(destination));
        txt_luggageType.setText(luggageType);
        txt_luggageBrand.setText(luggageBrand);
        txt_luggageColor.setText(luggageColor);
        txt_luggageCharacters.setText(luggageCharacters);
    }
    
    private String validateDate(Date pDate){
        return (pDate != null) ? String.valueOf(registerDate)
                               : "-";
    }
    
    private String getFullName(){
        System.out.println(firstName + infix + lastName);
        return (infix != null || infix.length() > 1) ? (firstName + " " + infix + " " + lastName) 
                                                     : (firstName + " " + lastName);
    }
}
