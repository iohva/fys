
package com.mycompany.fys_tripperssmaven.interfaces;

import com.jfoenix.controls.JFXComboBox;
import java.sql.SQLException;

public interface Queryable {
    
    public void initValidations();
    public void sendToDatabase();
    public void loadComboBoxesFromDb(JFXComboBox pComboBox, String pSql, String pTableHeader) throws SQLException;
//    public void loadFromDatabase();
}
