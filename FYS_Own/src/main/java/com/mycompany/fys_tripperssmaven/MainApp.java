package com.mycompany.fys_tripperssmaven;

import com.mycompany.fys_tripperssmaven.network.DatabaseConnector;
import com.mycompany.fys_tripperssmaven.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;    
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainApp extends Application implements Initializable{

    private DatabaseConnector databaseConnector;
    
    @FXML AnchorPane mainFragment;
    @FXML AnchorPane loginFragment;
    
    @FXML StackPane loginPane;
    @FXML StackPane navigationPane;
    @FXML StackPane pagePane;
    @FXML ScrollPane scrollPane;
    
    ResourceBundle resourcess;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Constants.GENERAL + Constants.MAIN), resourcess);

        Scene scene = new Scene(root);
        primaryStage.setTitle(Constants.CORENDON);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(Constants.IMAGES + Constants.FAVICON)));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourcess = ResourceBundle.getBundle("bundles/language",new Locale("en", "EN"));
        try {
            initLogin();
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initLogin() throws IOException{
        databaseConnector = new DatabaseConnector();
        BaseClass.loginStackPane = loginPane;
        BaseClass.pageStackPane = pagePane;
        BaseClass.scrollPane = scrollPane;
        BaseClass.navStackPane = navigationPane;
        BaseClass.setFragments(loginFragment, mainFragment);
        
        System.out.println("RESS" + resourcess);
        
        loginPane.getChildren().clear();
        loginPane.getChildren().add((Pane)FXMLLoader.load(getClass().getResource(Constants.GENERAL_SCREEN + Constants.LOGIN), resourcess));
    }
}
