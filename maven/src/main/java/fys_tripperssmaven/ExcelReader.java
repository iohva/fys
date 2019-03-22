
package fys_tripperssmaven;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;


public class ExcelReader {
    
    private final Connection databaseConnection;
    private final File excelFile;
    private final int excelFileId;
    private final String excelFilePath;
    
    // 15 parameters
    private final String INSERT_LUGGAGE = "INSERT INTO `excelluggage`"
                                        + " (`Registration`, `DateFound`, `TimeFound`, `LuggageType`, `LuggageBrand`, "
                                        + "`FlightNumber`, `LuggageLabel`, `LocationFound`, `LuggageMainColor`, `LuggageSecColor`, "
                                        + "`LuggageSize`, `LuggageWeight`, `Traveller`, `LuggageCharacters`, `ExcelFile_id`) "
                                        + "VALUES "
                                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private FileInputStream fileIn;
    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private Row row;
    
    private int i;
    
    public ExcelReader(Connection pDbConnection, File pFile, String pFilePath, int pFileId){
        databaseConnection = pDbConnection;
        excelFile = pFile;
        excelFilePath = pFilePath;
        excelFileId = pFileId;
    }
    
    public void readExcel() throws FileNotFoundException, IOException{
        fileIn = new FileInputStream(excelFile);
        wb = new XSSFWorkbook(fileIn);
        sheet = wb.getSheetAt(0);
        int amount = wb.getNumberOfSheets();
        
        for(i=4; i < sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            System.out.println("amount sheets: " + amount);
            System.out.println("amount rows: " + sheet.getLastRowNum());
            System.out.println(row.getCell(0).getStringCellValue());
            System.out.println(row.getCell(1).getDateCellValue());
            System.out.println(row.getCell(2).getDateCellValue());
            System.out.println(row.getCell(3).getStringCellValue());
            System.out.println(row.getCell(4).getStringCellValue());
            System.out.println(row.getCell(5).getStringCellValue());
            System.out.println(row.getCell(6).getNumericCellValue());
            System.out.println(row.getCell(7).getStringCellValue());
            System.out.println(row.getCell(8).getStringCellValue());
            System.out.println(row.getCell(9).getStringCellValue());
            System.out.println(row.getCell(10).getStringCellValue());
            System.out.println(row.getCell(11).getStringCellValue());
            System.out.println(row.getCell(12).getStringCellValue());
            System.out.println(row.getCell(13).getStringCellValue());
        }
    }
}
