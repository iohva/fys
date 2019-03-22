package fys_tripperssmaven;

import fys_tripperssmaven.constants.Database;
import fys_tripperssmaven.models.Setting;
import fys_tripperssmaven.models.User;
import fys_tripperssmaven.network.DatabaseConnector;
import fys_tripperssmaven.utils.Constants;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static fys_tripperssmaven.BaseClass.loginFrag;
import static fys_tripperssmaven.BaseClass.mainFragment;

public class Authorization {
    
    protected static final int AUTH_ADMIN = 1;
    protected static final int AUTH_SERVICE = 2;
    protected static final int AUTH_COMPENSATION = 3; 
    
    protected static Connection dbConnection = DatabaseConnector.getConnection();
    protected Optional<ButtonType> optinalButton;
    
    private User userObject;
    private Setting settingObject;

    protected ResultSet result;
    protected int[] ress;
    protected static Statement statement;
    protected static PreparedStatement preparedStatement;
    protected String sql;
    private static boolean loggedIn;
    
    public void setAuthorization(User pUser, boolean pIsLogged){
        userObject = pUser;
        loggedIn = pIsLogged;        
    }
    
    protected User getUserAuthorization(){ return userObject; }
    protected Setting getSettingAuthorization(){ return settingObject; }
    
    public static boolean isLoggedIn(){ return loggedIn; }
    
    protected boolean loginValidation(String pUsername, String pPassword) throws SQLException{
        /* TODO: Use @pUsername and @pPassword and search in database for optinalButton 
                > If found optinalButton return true and makes USER-object
                > If found nothing return false
        */     
        sql = "SELECT * FROM `User` WHERE username='" + pUsername + "' AND password='" + pPassword + "'";
        System.out.println("SQL IS: " + sql);
        statement = dbConnection.createStatement();
        result = statement.executeQuery(sql);

        if(!result.isBeforeFirst()){
            System.out.println(Constants.EMPTY_SQL);
            return false;
        }
                
        while (result.next()){
            System.out.println(Constants.NOT_EMPTY_SQL);
            userObject = new User(result.getInt("id"), result.getInt("role"));
            
            userObject.setFirstname(result.getString(Database.Headers.FIRSTNAME));
            userObject.setInfix(result.getString(Database.Headers.INFIX));
            userObject.setLastName(result.getString(Database.Headers.LASTNAME));
            userObject.setDateOfBirth(result.getString(Database.Headers.DATE_OF_BIRTH));
            userObject.setPhoneNumber(result.getString(Database.Headers.PHONENUMBER));
            userObject.setEmail(result.getString(Database.Headers.EMAIL));
            userObject.setUsername(pUsername);
            userObject.setPassword(pPassword);
            
            System.out.println(userObject.getFirstName() + userObject.getLastName() + userObject.getEmail());
        }
        if(!settingValidation(userObject.getId())){
            System.out.println("GET SETTINGS FAILED");
            return false;
        }
        DialogCreator.createOkDialog("", BaseClass.resourcesUser.getString("loggedIn"), BaseClass.resourcesUser.getString("you_are") + getLanguageUserRole(userObject.getRole()));
        loggedIn = true;
        
        return true;
    }
    
    protected void resetDatabase(){
        result = null;
    }
    
    private String getLanguageUserRole(int pRole){
        switch (pRole) {
            case 1:
                return BaseClass.resourcesUser.getString("manager");
            case 2:
                return BaseClass.resourcesUser.getString("service_employee");
            case 3:
                return BaseClass.resourcesUser.getString("compensation_employee");
            default:
                return "empty role";
        }
    }
    
    private boolean settingValidation(int pUserId) throws SQLException{
        sql = "SELECT * FROM `Setting` WHERE idUser='" + pUserId + "'";
        System.out.println("SQL IS: " + sql);
        statement = dbConnection.createStatement();
        result = statement.executeQuery(sql);

        if(!result.isBeforeFirst()){
            System.out.println(Constants.EMPTY_SQL);
            return false;
        }
        
        while (result.next()){
            System.out.println(Constants.NOT_EMPTY_SQL);
            settingObject = new Setting();
            
            settingObject.setLanguage(result.getString(Database.Headers.LANGUAGE));
            settingObject.setFontSize(result.getInt(Database.Headers.FONTSIZE));
            settingObject.setTimeType(result.getInt(Database.Headers.TIME_TYPE));
            settingObject.setmaxOverviewExcel(result.getInt(Database.Headers.MAX_OVERVIEW_EXCEL));
            
            System.out.println(settingObject.getLanguage()+ settingObject.getFontSize()+ settingObject.getTimeType());
        }
        BaseClass.resourcesUser = ResourceBundle.getBundle("bundles/language",settingObject.getLocale());
        return true;
    }
    
    public void logOut(Button pButton){
        /* TODO: Make logOut-method */
//        loggedIn = false;
//        userObject = null;
//        settingObject = null;
//        dbConnection = null;
        
        optinalButton = DialogCreator.createYesNoDialog(BaseClass.resourcesUser.getString("logout"), BaseClass.resourcesUser.getString("sure"), BaseClass.resourcesUser.getString("want_logout"));            
        if (optinalButton.get() != DialogCreator.NO_BUTTON) {
            loginFrag.setVisible(true);
            mainFragment.setVisible(false);
//            pButton.getScene().getWindow().hide();
        }
    }
}
