package fys_tripperssmaven.utils;

import com.jfoenix.controls.*;
import fys_tripperssmaven.DialogCreator;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;


public class Validation {
    
    private final static String ONLY_LETTERS = "[A-Za-z ]+";
    private final static String ONLY_NUMBERS = "[0-9 ]+";
    private final static String ONLY_LETTERS_NUMBERS = "[A-Za-z0-9 ]+";
    private final static String ONLY_EMAIL = "[-a-z0-9~!$%^&*_=+}{\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\'?]+)"
                                           + "*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\."
                                           + "(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])"
                                           + "|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?";
    
    private final String ATLEAST_LETTER_NUMBER = "(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9 ]+)";
    private final String columnRegex = "(?=.*[a-zA-Z])([a-zA-Z_]+)";
    
    private final static String ONLY_LETTERS_STYLE = "only_letter";
    private final static String ONLY_NUMBER_STYLE = "only_number";
    private final static String ONLY_LETTER_NUMBER_STYLE = "only_letter_number";
    private final static String ONLY_EMAIL_STYLE = "only_email";
    private final static String INVALID_STYLE = "invalid";
    
    private final static String ERROR_ONLY_LETTERS = "Veld mag alleen letters bevatten\n";
    private final static String ERROR_ONLY_NUMBERS = "Veld mag alleen nummers bevatten\n";
    private final static String ERROR_ONLY_LETTERS_NUMBERS = "Veld mag alleen letters en nummers bevatten\n";
    private final static String ERROR_ONLY_EMAIL = "Veld bevat ongeldige emailadres\n";
    private final static String ERROR_EMPTY = "Veld is leeg\n";
    
    private static final String TEXTFIELD = "JFXTextField";
    private static final String TEXTAREA = "JFXTextArea";
    private static final String JFXTIMEPICKER = "JFXTimePicker";
    private static final String DATEPICKER = "JFXDatePicker";
    private static final String RADIOBUTTON = "RadioButton";
    private static final String COMBOBOX = "JFXComboBox";
    
    private static ArrayList objects;
    
    private static String objectText;
    private static int screenObjectsLength;
    private static String errorMessage;
    
    
    public static boolean checkFields(ArrayList pObjects){        
        objects = pObjects;
        screenObjectsLength = pObjects.size();
        errorMessage = "";
        boolean result = false;
        
        for (int i = 0; i < screenObjectsLength; i++) {
//            System.out.println(objects.get(i).getClass().getSimpleName());
            switch (objects.get(i).getClass().getSimpleName()) {
                case TEXTFIELD:
                    checkTextField((JFXTextField) objects.get(i));
                    break;
                case TEXTAREA:
                    checkTextArea((JFXTextArea) objects.get(i));
                    break;
                case DATEPICKER:
                    checkDatePicker((JFXDatePicker) objects.get(i));
                    break;
                case JFXTIMEPICKER:
                    checkTimePicker((JFXTimePicker) objects.get(i));
                    break;
                case RADIOBUTTON:
                    checkRadioButton((RadioButton) objects.get(i));
                    break;
                case COMBOBOX:
                    checkComboBox((JFXComboBox) objects.get(i));
                    break;
                default:
                    break;
            }
        }
        if(!errorMessage.equals("")){
            showDialog();
            return false;
        }
        return true;
    }
    
    private static boolean checkTextField(JFXTextField pObject){      
        objectText = pObject.getText();

        if(pObject.getText().isEmpty()){
            pObject.getStyleClass().add("invalid");
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_LETTERS_STYLE) && !(objectText.matches(ONLY_LETTERS))){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_LETTERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_NUMBER_STYLE) && (!objectText.matches(ONLY_NUMBERS))){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_NUMBERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_LETTER_NUMBER_STYLE) && (!objectText.matches(ONLY_LETTERS_NUMBERS))){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_LETTERS_NUMBERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_EMAIL_STYLE) && (!objectText.matches(ONLY_EMAIL))){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_EMAIL;
            return false;
        }
        return true;
    }
    
    private static boolean checkTextArea(JFXTextArea pObject){
        objectText = pObject.getText();
        
        if(objectText == null || objectText.isEmpty()){
            errorMessage += ERROR_EMPTY;
            pObject.getStyleClass().add(INVALID_STYLE);
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_LETTERS_STYLE) && !objectText.matches(ONLY_LETTERS)){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_LETTERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_NUMBER_STYLE) && !objectText.matches(ONLY_NUMBERS)){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_NUMBERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_LETTER_NUMBER_STYLE) && !objectText.matches(ONLY_LETTERS_NUMBERS)){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_LETTERS_NUMBERS;
            return false;
        }
        else if(pObject.getStyleClass().contains(ONLY_EMAIL_STYLE) && !objectText.matches(ONLY_EMAIL)){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_ONLY_EMAIL;
            return false;
        }
        return true;       
    }
    
    private static boolean checkDatePicker(JFXDatePicker pObject){  
        // TODO:        
//        System.out.println("DATE: " + pObject.getValue());
        
        if(pObject.getValue() == null || pObject.getValue().toString().length() == 0){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_EMPTY;
            return false;
        }
        return true;
    }
    
    private static boolean checkTimePicker(JFXTimePicker pObject){  
//        System.out.println("TIME: " + pObject.getValue());     
        
        if(pObject.getValue() == null || pObject.getValue().toString().length() == 0){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_EMPTY;
            return false;
        }
        return true;
    }
    
    private static boolean checkComboBox(JFXComboBox pObject){
//        System.out.println("Combobox: " + pObject.getValue());
        
        if(pObject.getValue() == null || pObject.getSelectionModel().isEmpty()){
            pObject.getStyleClass().add(INVALID_STYLE);
            errorMessage += ERROR_EMPTY;
            return false;
        }
        return true;
    }
    
    private static void checkRadioButton(RadioButton pObject){    
        // TODO:
        System.out.println("Check RadioButton");       
    }
    
    private static void showDialog(){
        DialogCreator.createErrorDialog("", "De volgende error(s) is/zijn opgetreden: ", errorMessage);
    }
}
