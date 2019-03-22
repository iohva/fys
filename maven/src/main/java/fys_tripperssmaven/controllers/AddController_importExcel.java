
package fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXSpinner;
import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.DialogCreator;
import fys_tripperssmaven.ExcelReader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ResourceBundle;


public class AddController_importExcel extends BaseClass implements Initializable {
    
    private final FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx");
    private final FileChooser fc = new FileChooser();
    
    private final String CHECK_FILE = "SELECT COUNT(filename) FROM `excelfile` "
                                    + "WHERE filename = ? ";
    
    private final String INSERT_FILE = "INSERT INTO `excelfile` "
                                     + "(`uploadedBy.User`, `filename`, `filesize`) "
                                     + "VALUES (?, ?, ?)";
    
    private @FXML Pane dragDropPane;
    private @FXML JFXSpinner spinner;
    
    private File file;
    private int fileId;
    private String fileSize;
    private String fileName;
    private String filePath;
    
    private ExcelReader excelReader;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fc.getExtensionFilters().add(excelFilter);
        
        // Make dragDropArea accept files
        dragDropPane.setOnDragOver(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }    
    
    @FXML
    private void setHighlight(){
        if(dragDropPane.getStyleClass().contains("highlight"))
            dragDropPane.getStyleClass().remove("highlight");
        else
            dragDropPane.getStyleClass().add("highlight");
    }
    
    @FXML
    private void importExcelFile() throws SQLException, IOException{
        dbConnection.setAutoCommit(false);
        file = fc.showOpenDialog(currStage);
        
        if (file != null) {
            spinner.setVisible(true);
            filePath = file.getAbsolutePath();
            fileName = file.getName();
            fileSize = getFileSize((int) file.length());
            
            System.out.println(filePath);
            System.out.println(fileName);
            System.out.println(fileSize);
        }
        if(!fileExists()){
            insertFile();
            excelReader = new ExcelReader(dbConnection, file, filePath, fileId);
            excelReader.readExcel();

        }
        else DialogCreator.createErrorDialog("", "Excel uploaden mislukt", "Excel-bestand is al in database");
    }
    
    @FXML
    private void dropExcelFile(DragEvent event) throws SQLException, IOException{
        dbConnection.setAutoCommit(false);
        Dragboard db = event.getDragboard();
        
        if (db.hasFiles()) {
            for (File files:db.getFiles()) {
                file = files;
                filePath = files.getAbsolutePath();
                fileName = files.getName();
                fileSize = getFileSize((int) files.length());
                System.out.println(filePath);
                System.out.println(fileName);
                System.out.println(fileSize);
                
//                DialogCreator.createOkDialog("", "TOP!", "Excel-bestand is herkend");
            }
        }        
        if(!fileExists()){
            insertFile();
            excelReader = new ExcelReader(dbConnection, file, filePath, fileId);
            excelReader.readExcel();
        }
        else DialogCreator.createErrorDialog("", "Excel uploaden mislukt", "Excel-bestand is al in database");
    }
    
    // TODO: WERKT NOG NIET GOED, RETURNED 1 NA 1 KEER
    private boolean fileExists() throws SQLException{
        int count = 0;
        preparedStatement = dbConnection.prepareStatement(CHECK_FILE);
        preparedStatement.setString(1, fileName);
        result = preparedStatement.executeQuery();
        
        System.out.println("SQLL EXIST: " + CHECK_FILE + "'" + fileName + "'");
        
        while (result.next()){
            System.out.println("BF COUNTT " + count);
            count = result.getInt(1);
            System.out.println("ATER COUNTT " + count);
        }        
        return (count > 0);
    }
    
    private boolean insertFile() throws SQLException{
        preparedStatement = dbConnection.prepareStatement(INSERT_FILE, Statement.RETURN_GENERATED_KEYS);
        
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, fileName);
        preparedStatement.setString(3, fileSize);
        preparedStatement.addBatch();
        
        ress = preparedStatement.executeBatch();
        
        result = preparedStatement.getGeneratedKeys();
        result.next();
        fileId = result.getInt(1);

        System.out.println("ID IS : " + fileId);
        
        result.close();
        resetDatabase();
        dbConnection.commit();
        preparedStatement.close();

        return ress.length != 0;
    }
    
    private String getFileSize(int pFileSize){      
        double m = pFileSize/1024.0;
        double g = pFileSize/1048576.0;
        double t = pFileSize/1073741824.0;
        
        String hrSize;
        String fileSizeString = String.valueOf(pFileSize);
        
        DecimalFormat dec = new DecimalFormat("0.00");
        
        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(pFileSize).concat(" KB");
        }
        
        return hrSize;
    }
}
