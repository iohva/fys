package fys_tripperssmaven.models;


import fys_tripperssmaven.constants.Languages;

import java.util.Locale;

public class Setting {
    
    private String language;
    private Locale languageLocale;
    private int fontSize;
    private int timeType;
    private int maxOverviewExcel;
    
//    public Setting(String pLanguage, int pFontSize, int pTimeType,
//                   int pMaxOverviewExcel){
//        
//        language = pLanguage;
//        fontSize = pFontSize;
//        timeType = pTimeType;
//        maxOverviewExcel = pMaxOverviewExcel;
//    }
    
    public String getLanguage(){
        return language;
    }
    
    public Locale getLocale(){
        System.out.println("LOCAL LANG: " + language);
        switch(language){
            case Languages.NL_STRING:  languageLocale = Languages.NL;
                     break;
            case Languages.EN_STRING:  languageLocale = Languages.EN;
                     break;
            case Languages.DE_STRING:  languageLocale = Languages.DE;
                     break;
            case Languages.FR_STRING:  languageLocale = Languages.FR;
                     break;
            case Languages.TR_STRING:  languageLocale = Languages.TR;
                     break;
            default: 
                languageLocale = Languages.EN;
        }
        return languageLocale;
    }
    
    public void setLanguage(String pLanguage){
        language = pLanguage;
    }
    
    public int getFontSize(){
        return fontSize;
    }
    
    public void setFontSize(int pFontSize){
        fontSize = pFontSize;
    }
    
    public boolean isTwentyFourHour(){
        return timeType == 24;
    }
    
    public int getTimeType(){
        return timeType;
    }
    
    public void setTimeType(int pTimeType){
        timeType = pTimeType;
    }
    
    public int getmaxOverviewExcel(){
        return maxOverviewExcel;
    }
    
    public void setmaxOverviewExcel(int pmaxOverviewExcel){
        maxOverviewExcel = pmaxOverviewExcel;
    }
}