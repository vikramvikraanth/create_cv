package com.muv.createcv.CommonValidation;

/**
 * Created by vikram on 21-Dec-17.
 */

public class CommonValidation {

    public static String ObjectValidation(Object Values){
        if(Values!=null && !Values.equals("null") && !Values.toString().isEmpty()){
            return Values.toString();
        }else {
            return "";
        }
    }
    public static Double LatLngValidation(Object Values){
        if(Values!=null && !Values.equals("null") && !Values.toString().isEmpty()){
            return Double.parseDouble(Values.toString());
        }else {
            return 0.0;
        }
    }
    public static String StrCaption(String Values){
        if(!Values.isEmpty() && !Values.equals("Empty")){
            return Values.substring(0,1).toUpperCase() + Values.substring(1);

        }else {
            return "";
        }
    }
}
