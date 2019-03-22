
package com.mycompany.fys_tripperssmaven.constants;

public class Database {
    
    public class Selectors{
        //SYMBOLS
        public static final String IS = "=";
        
        //SELECTORS
        public static final String SELECT_ALL_FROM = "SELECT * FROM";
        public static final String INSERT_INTO = "INSERT INTO";
        public static final String DELETE_FROM = "DELETE FROM";
        
        public static final String FROM = "FROM";
        public static final String WHERE = "WHERE";
        public static final String UPDATE = "UPDATE";
        public static final String SET = "SET";
    }
    
    public class Tables{
        public static final String USER = "'User'";
        public static final String TRAVELLER = "'Traveller'";
        public static final String LUGGAGE = "'Luggage'";
        public static final String REGISTER_LUGGAGE = "'RegisterLuggage'";
        public static final String LUGGAGE_LABEL_INFORMATION = "'LuggageLabelInformation'";
        public static final String AIRPORT = "'Airport'";
        public static final String EXCEL_FILE = "'ExcelFile'";
        public static final String SETTING = "'Setting'";
    }
    
    public class Headers{        
        // MySQL: GENERAL TABLE HEADERS
        public static final String ID = "id";
        public static final String ID_USER = "idUser";
        public static final String ID_LUGGAGE = "idLuggage";
        public static final String ID_AIRPORT = "idAirport";
        public static final String DATE = "date";
        public static final String TIME = "time";

        // MySQL: HEADERS OF TABLE 'User' AND 'Traveller'
        public static final String FIRSTNAME = "firstname";
        public static final String INFIX = "infix";
        public static final String LASTNAME = "lastname";
        public static final String DATE_OF_BIRTH = "dateOfBirth";
        public static final String PHONENUMBER = "phonenumber";
        public static final String EMAIL = "email";

        // MySQL: HEADERS OF TABLE 'User'
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String ROLE = "role";

        // MySQL: HEADERS OF TABLE 'Traveller'
        public static final String ADDRESS = "address";
        public static final String PLACE = "place";
        public static final String ZIPCODE = "zipcode";
        public static final String COUNTRY = "country";

        // MySQL: HEADERS OF TABLE 'Luggage'
        public static final String ID_TRAVELLER = "idTraveller";
        public static final String TYPE = "idTraveller";
        public static final String BRAND = "idTraveller";
        public static final String COLOR = "idTraveller";
        public static final String CHARACTERISTICS = "characteristics";
        public static final String STATUS = "status";

        // MySQL: HEADERS OF TABLE 'LuggageLabelInformation'
        public static final String DESTINATION = "destination";
        public static final String LABEL_NUMBER = "labelNumber";
        public static final String FLIGHT_NUMBER = "flightNumber";

        // MySQL: HEADERS OF TABLE 'Airport'
        public static final String NAME_AIRPORT = "nameAirport";
        public static final String LOCATION = "location";
        public static final String ABBREVIATION = "abbreviation";

        // MySQL: HEADERS OF TABLE 'ExcelFile'
        public static final String UPLOADED_BY = "uploadedBy.User";
        public static final String UPLOAD_DATE = "uploadDate";
        public static final String FILENAME = "filename";
        public static final String FILESIZE = "filesize";

        // MySQL: HEADERS OF TABLE 'Setting'
        public static final String LANGUAGE = "language";
        public static final String FONTSIZE = "fontSize";
        public static final String TIME_TYPE = "timeType";
        public static final String MAX_OVERVIEW_EXCEL = "maxOverviewExcel";
    }
}
