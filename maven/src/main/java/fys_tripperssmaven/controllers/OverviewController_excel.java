package fys_tripperssmaven.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import fys_tripperssmaven.BaseClass;
import fys_tripperssmaven.utils.Constants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OverviewController_excel extends BaseClass implements Initializable {

    @FXML private JFXRadioButton radiobtn_showAll;
    @FXML private JFXRadioButton radiobtn_showMine;
    @FXML private TableView<ExcelFile> table_excelFiles;
    @FXML private TableColumn<ExcelFile, Number> column_id;
    @FXML private TableColumn<ExcelFile, String> column_date;
    @FXML private TableColumn<ExcelFile, String> column_name;
    @FXML private TableColumn<ExcelFile, String> column_size;
    @FXML private TableColumn<ExcelFile, String> column_luggages;
    @FXML private JFXButton btn_importExcel;
    
    private final String SELECT_ALL_EXCEL_FILES = "SELECT * FROM `excelfile`";
    private final String SELECT_MINE_EXCEL_FILES = "SELECT * FROM `excelfile` WHERE `uploadedBy.User` = ";

    private int idFile;
    private Date insertDate;
    private String nameFile;
    private String sizeFile;
    private Button btnShowLuggages;
    
    private ObservableList<ExcelFile> excelFileData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(user.getFullRole().equals(user.COMPENSATION)){
            radiobtn_showMine.setVisible(false);
            btn_importExcel.setVisible(false);
        }
        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    @FXML
    private void loadData() throws SQLException {
        sql = (radiobtn_showAll.isSelected()) ? SELECT_ALL_EXCEL_FILES 
                                              : SELECT_MINE_EXCEL_FILES + user.getId();
        
        excelFileData = FXCollections.observableArrayList();
        result = statement.executeQuery(sql);
        
        while (result.next()){
            idFile = result.getInt("uploadedBy.User");
            insertDate = result.getDate("uploadDate");
            nameFile = result.getString("filename");
            sizeFile = result.getString("filesize");
  
            excelFileData.add(new ExcelFile(idFile, insertDate, nameFile, sizeFile));
        }
        result.close(); 
        
        column_id.setCellValueFactory(new PropertyValueFactory<ExcelFile, Number>("idFile"));
        column_date.setCellValueFactory(new PropertyValueFactory<ExcelFile, String>("uploadDate"));
        column_name.setCellValueFactory(new PropertyValueFactory<ExcelFile, String>("fileName"));
        column_size.setCellValueFactory(new PropertyValueFactory<ExcelFile, String>("fileSize"));
        column_luggages.setCellValueFactory(new PropertyValueFactory<ExcelFile, String>("btnShowLuggages"));
       
        table_excelFiles.setItems(excelFileData);
    }
    
    @FXML
    private void importExcelFile() throws IOException{
        openPage(Constants.ADD_SCREEN, Constants.IMPORT_EXCEL);
    }
    
    public final class ExcelFile {
        private final IntegerProperty idFile;
        private final StringProperty uploadDate;
        private final StringProperty fileName;
        private final StringProperty fileSize;

        private final JFXButton btnShowLuggages;
        
        public ExcelFile(int pIdFile, Date pUploadDate, String pFilename, String pFileSize) {
            
            this.idFile = new SimpleIntegerProperty(pIdFile);
            this.fileName = new SimpleStringProperty(pFilename);
            this.uploadDate = new SimpleStringProperty(String.valueOf(pUploadDate));
            this.fileSize = new SimpleStringProperty(pFileSize);
            this.btnShowLuggages = new JFXButton("Laat koffers zien");
        }

        public int getIdFile() {
            return idFile.get();
        }
        
        public String getUploadDate() {
            return uploadDate.get();
        }

        public String getFileName() {
            return fileName.get();
        }
        
        public String getFileSize() {
            return fileSize.get();
        }
        
        public Button getBtnShowLuggages() {
            btnShowLuggages.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        idFileExcel = idFile.get();
                        openScrollPage(Constants.OVERVIEW_SCREEN, Constants.EXCEL_LUGGAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(OverviewController_foundLuggage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return btnShowLuggages;
        }
    }
}
