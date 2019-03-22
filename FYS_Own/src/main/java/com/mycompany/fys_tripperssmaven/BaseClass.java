package com.mycompany.fys_tripperssmaven;

import com.mycompany.fys_tripperssmaven.models.Setting;
import com.mycompany.fys_tripperssmaven.models.User;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BaseClass extends Authorization{
   
    protected static AnchorPane loginFrag;
    protected static AnchorPane mainFragment;
    
    protected static StackPane loginStackPane;
    protected static StackPane pageStackPane;
    protected static StackPane navStackPane;
    protected static StackPane navigationStackPane;
    protected static ScrollPane scrollPane;
    
    private static Stage prevStage;
    protected static Stage currStage;
    private Pane myPane;
    private Scene scene;    
    protected static User user;
    protected static Setting setting;
    
    protected static int idLostLuggage;
    protected static int idFoundLuggage;
    protected static int idTraveller;
    protected static int idUser;
    protected static int idFileExcel;
    protected static String idLabelInfo;
    
    protected static boolean hasBackStack;
    public static ResourceBundle resourcesUser;
    
    protected void init(){
       user = isLoggedIn()    ? getUserAuthorization() 
                              : null;      
       setting = isLoggedIn() ? getSettingAuthorization()
                              : null;
    }
    
    protected static void setStackPane(StackPane pStackPane){
        loginStackPane = pStackPane;
    }
    
    protected static void setFragments(AnchorPane pLoginFragment, AnchorPane pMainFragment){
        loginFrag = pLoginFragment;
        mainFragment = pMainFragment;
    }
    
    protected StackPane getStackPane(){
        return loginStackPane;
    }
    
    public void openScene(String pXmlName, String pStageTitle) throws IOException{
       currStage = new Stage();
       currStage.setTitle(pStageTitle);
       
       myPane = FXMLLoader.load(getClass().getResource("fxml/" + pXmlName));
       scene = new Scene(myPane);
       currStage.setScene(scene);
       currStage.getIcons().add(new Image(getClass().getResourceAsStream("images/favicon.png")));

       System.out.println("VORIGE STAGE IS: " + prevStage);
       prevStage.close();
       currStage.show();
    }
    
    protected void openPage(String pPath, String pNameScreen) throws IOException{
        if(!pageStackPane.isVisible() || scrollPane.isVisible()){
            pageStackPane.setVisible(true);
            scrollPane.setVisible(false);
        }
        else if(!isLoggedIn()){
            loginStackPane.getChildren().clear();
            loginStackPane.getChildren().add((Pane)FXMLLoader.load(BaseClass.class.getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcesUser));
            System.out.println(String.valueOf(BaseClass.class.getResource(pPath + pNameScreen)));
        }
        hasBackStack = false;
        resourcesUser = ResourceBundle.getBundle("bundles/language",setting.getLocale());
        pageStackPane.getChildren().clear();
        pageStackPane.getChildren().add((Pane)FXMLLoader.load(BaseClass.class.getResource(pPath + pNameScreen), resourcesUser));
    }
    
    /* OPEN PAGE WITH OR WITHOUT BACKSTACK */
    protected void openPage(String pPath, String pNameScreen, boolean pHasBackStack) throws IOException{
        if(!pageStackPane.isVisible() || scrollPane.isVisible()){
            pageStackPane.setVisible(true);
            scrollPane.setVisible(false);
        }
        else if(!isLoggedIn()){
            loginStackPane.getChildren().clear();
            loginStackPane.getChildren().add((Pane)FXMLLoader.load(BaseClass.class.getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcesUser));
            System.out.println(String.valueOf(BaseClass.class.getResource(pPath + pNameScreen)));
        }
        hasBackStack = pHasBackStack;
        resourcesUser = ResourceBundle.getBundle("bundles/language",setting.getLocale());
        pageStackPane.getChildren().clear();
        pageStackPane.getChildren().add((Pane)FXMLLoader.load(BaseClass.class.getResource(pPath + pNameScreen), resourcesUser));
    }
    
    protected void openScrollPage(String pPath, String pNameScreen) throws IOException{
        if(!scrollPane.isVisible() || pageStackPane.isVisible()){
            scrollPane.setVisible(true);
            pageStackPane.setVisible(false);
        }
        hasBackStack = false;
        resourcesUser = ResourceBundle.getBundle("bundles/language",setting.getLocale());
        myPane = FXMLLoader.load(getClass().getResource(pPath + pNameScreen), resourcesUser);
        scrollPane.setContent(myPane);
    }

    /* OPEN SCROLLPAGE WITH OR WITHOUT BACKSTACK */
    protected void openScrollPage(String pPath, String pNameScreen, boolean pHasBackStack) throws IOException{
        if(!scrollPane.isVisible() || pageStackPane.isVisible()){
            scrollPane.setVisible(true);
            pageStackPane.setVisible(false);
        }
        hasBackStack = pHasBackStack;
        resourcesUser = ResourceBundle.getBundle("bundles/language",setting.getLocale());
        myPane = FXMLLoader.load(getClass().getResource(pPath + pNameScreen), resourcesUser);
        scrollPane.setContent(myPane);
    }
}
