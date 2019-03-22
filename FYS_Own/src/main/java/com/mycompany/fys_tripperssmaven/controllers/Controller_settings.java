
package com.mycompany.fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.mycompany.fys_tripperssmaven.BaseClass;
import com.mycompany.fys_tripperssmaven.DialogCreator;
import com.mycompany.fys_tripperssmaven.constants.Languages;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class Controller_settings extends BaseClass implements Initializable {

    @FXML ImageView flag_nl;
    @FXML ImageView flag_en;
    @FXML ImageView flag_de;
    @FXML ImageView flag_fr;
    @FXML ImageView flag_tr;
    
    @FXML VBox flagBox_nl;
    @FXML VBox flagBox_en;
    @FXML VBox flagBox_de;
    @FXML VBox flagBox_fr;
    @FXML VBox flagBox_tr;
    
    @FXML Label label_minTextSize;
    @FXML Label label_maxTextSize;
    @FXML Label label_curTextSize;
    
    
    @FXML JFXSlider slider_textSize;
    
    @FXML JFXRadioButton radiobtn_twelveHour;
    @FXML JFXRadioButton radiobtn_twentyFourHour;
    
    @FXML Spinner<Double> spinner_excelAmount;
    
    @FXML JFXButton btn_save;
    
    private final int MIN_FONTSIZE = 15;
    private final int MAX_FONTSIZE = 150;
    private final int TWELVE = 12;
    private final int TWENTY_FOUR = 24;
    
    private boolean hasChanged;
    
    private String val_language;
    private String flagId;
    private int val_fontSize;
    private int val_timeType;
    private double val_maxAmountExcel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValues();
    }
    
    @FXML
    private void flagClick(MouseEvent e){               
        btn_save.setDisable(false);
        
        flagBox_nl.getStyleClass().remove("selected");
        flagBox_en.getStyleClass().remove("selected");
        flagBox_de.getStyleClass().remove("selected");
        flagBox_fr.getStyleClass().remove("selected");
        flagBox_tr.getStyleClass().remove("selected");
        
        flagId = e.getPickResult().getIntersectedNode().getId();
                
        if(flagId.equals(flag_nl.getId())){
            val_language = Languages.NL_STRING;
            flagBox_nl.getStyleClass().add("selected");
        }
        else if(flagId.equals(flag_en.getId())){
            val_language = Languages.EN_STRING;
            flagBox_en.getStyleClass().add("selected");
        }
        else if(flagId.equals(flag_de.getId())){
            val_language = Languages.DE_STRING;
            flagBox_de.getStyleClass().add("selected");
        }
        else if(flagId.equals(flag_fr.getId())){
            val_language = Languages.FR_STRING;
            flagBox_fr.getStyleClass().add("selected");
        }
        else if(flagId.equals(flag_tr.getId())){
            val_language = Languages.TR_STRING;
            flagBox_tr.getStyleClass().add("selected");
        }     
    }
    
    @FXML
    private void sliderClick(){
      getValues();
      label_curTextSize.setText(String.valueOf(val_fontSize));
    }
    
    @FXML
    private void radioButtonClick(){
        getValues();
    }
    
    @FXML
    private void saveSettings() throws SQLException, IOException{
        getValues();
        if(insertChanges()) {
            //TODO: REFRESH PAGE
            openScrollPage(Constants.GENERAL_SCREEN, Constants.SETTINGS);
        }  
    }
    
    private boolean insertChanges() throws SQLException, IOException{
        try {
            setting.setFontSize(val_fontSize);
            setting.setLanguage(val_language);
            setting.setTimeType(val_timeType);
            setting.setmaxOverviewExcel((int) val_maxAmountExcel);

            sql = "UPDATE Setting SET language = ?, fontSize = ?, timeType= ?, maxOverviewExcel = ? WHERE idUser = ?";
            preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setString(1, val_language);
            preparedStatement.setInt(2, val_fontSize);
            preparedStatement.setInt(3, val_timeType);
            preparedStatement.setInt(4, (int) val_maxAmountExcel);
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } 
        catch (SQLException se) {
            DialogCreator.createErrorDialog("Oeps", "Foutmelding", "Instellingen aanpassen is niet gelukt");
            throw se;
        }
        
        resourcesUser = ResourceBundle.getBundle("bundles/language",setting.getLocale());
        System.out.println("LOCAL NA SETTINGS " + setting.getLocale());
        navStackPane.getChildren().clear();
        navStackPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL + Constants.NAVIGATIONDRAWER), resourcesUser));
        DialogCreator.createOkDialog("Yoehoe!", "Instellingen aangepast", "U heeft uw instellingen succesvol aangepast");
        return true;
    }
    
    private void initValues(){        
        spinner_excelAmount.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 10000, setting.getmaxOverviewExcel()));
        
        slider_textSize.setMin(MIN_FONTSIZE);
        slider_textSize.setMax(MAX_FONTSIZE);
        
        label_minTextSize.setText(String.valueOf(MIN_FONTSIZE));
        label_maxTextSize.setText(String.valueOf(MAX_FONTSIZE));
        label_curTextSize.setText(String.valueOf(setting.getFontSize()));
        
        slider_textSize.setValue(setting.getFontSize());        
        
        if(setting.isTwentyFourHour()) radiobtn_twentyFourHour.setSelected(true);
        else radiobtn_twelveHour.setSelected(true);
        
        val_language = setting.getLanguage();
        
        switch (setting.getLanguage()) {
            case Languages.NL_STRING: flagBox_nl.getStyleClass().add("selected"); 
                     break;
            case Languages.EN_STRING: flagBox_en.getStyleClass().add("selected");
                     break;
            case Languages.DE_STRING: flagBox_de.getStyleClass().add("selected");
                     break;
            case Languages.FR_STRING: flagBox_fr.getStyleClass().add("selected");
                     break;
            case Languages.TR_STRING: flagBox_tr.getStyleClass().add("selected");
                     break;
            default: 
                break;
        }
    }
    
    private int getTimeType(){
        if(radiobtn_twelveHour.isSelected()){
            return TWELVE;
        }
        return TWENTY_FOUR;
    }
    
    private void getValues(){
        hasChanged = true;
        btn_save.setDisable(false);
        val_fontSize = (int) slider_textSize.getValue();
        val_timeType = getTimeType();
        val_maxAmountExcel = spinner_excelAmount.getValue();
    }
}
