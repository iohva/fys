package fys_tripperssmaven;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.util.Optional;

public class DialogCreator {

    public static final ButtonType YES_BUTTON = new ButtonType(BaseClass.resourcesUser.getString("yes"), ButtonBar.ButtonData.OK_DONE);
    public static final ButtonType NO_BUTTON = new ButtonType(BaseClass.resourcesUser.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    
    private static final Alert ALERT_INFORMATION = new Alert(Alert.AlertType.INFORMATION);
    private static final Alert ALERT_ERROR = new Alert(Alert.AlertType.ERROR);
    private static final Alert ALERT_BLANK = new Alert(Alert.AlertType.NONE);
    private static final Alert ALERT_WARNING = new Alert(Alert.AlertType.WARNING);
    private static final Alert ALERT_CONFIRM = new Alert(Alert.AlertType.CONFIRMATION);

    
    
    public static Alert createOkDialog(String pTitle, String header, String pContextText){
        ALERT_INFORMATION.setTitle(pTitle);
        ALERT_INFORMATION.setHeaderText(header);
        ALERT_INFORMATION.setContentText(pContextText);
        ALERT_INFORMATION.showAndWait();
        
        return ALERT_INFORMATION;
    }
    
    public static Alert createWarningDialog(String pTitle, String header, String pContextText){
        ALERT_WARNING.setTitle(pTitle);
        ALERT_WARNING.setHeaderText(header);
        ALERT_WARNING.setContentText(pContextText);
        ALERT_WARNING.showAndWait();
      
        return ALERT_WARNING;
    }
    
    public static Alert createErrorDialog(String pTitle, String header, String pContextText){
        ALERT_ERROR.setTitle(pTitle);
        ALERT_ERROR.setHeaderText(header);
        ALERT_ERROR.setContentText(pContextText);
        TOOLKIT.beep();                             // Play error sound
        ALERT_ERROR.showAndWait();
      
        return ALERT_ERROR;
    }
    
    public static Optional<ButtonType> createYesNoDialog(String pTitle, String header, String pContextText){
        ALERT_CONFIRM.setTitle(pTitle);
        ALERT_CONFIRM.setHeaderText(header);
        ALERT_CONFIRM.setContentText(pContextText);
        
        // add custom button types if specified
        ALERT_CONFIRM.getButtonTypes().setAll(YES_BUTTON, NO_BUTTON);      
        
        return ALERT_CONFIRM.showAndWait();
    }
    
    public static Optional<ButtonType> createCustomDialog(String pTitle, String header, String pContextText, final ButtonType... buttonTypes){
        ALERT_BLANK.setTitle(pTitle);
        ALERT_BLANK.setHeaderText(header);
        ALERT_BLANK.setContentText(pContextText);
        
        // add buttons
        if (buttonTypes.length > 0) {
            ALERT_BLANK.getButtonTypes().setAll(buttonTypes);
        }        
        
        return ALERT_BLANK.showAndWait();
    }   
}
