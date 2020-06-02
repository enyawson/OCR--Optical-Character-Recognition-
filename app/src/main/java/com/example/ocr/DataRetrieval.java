package com.example.ocr;

import android.util.Log;

/**
 * This class retrieves data from a large string(string obtained
 * from string builder),and split the string to smaller strings.
 */
public class DataRetrieval {
    // input data
    private String stringToSplit;
    // The type of card
    private String typeOfCard;
    //log
    private String TAG = this.getClass().getSimpleName();




    public DataRetrieval() {
    }

    //returns the stringToSplit
    public String getStringToSplit() {
        return stringToSplit;
    }
    //set the value of stringToSplit
    public void setStringToSplit(String stringToSplit) {
        this.stringToSplit = stringToSplit;
    }
    public String setTypeOfCard(String type) {
        return (typeOfCard= type);
    }
    //returns the type of card used
    public String getTypeOfCard() {
        return typeOfCard;
    }

    /**
     * This method splits comma delimited string into individual
     * or smaller strings and uses each string to identify the
     * type of card
     */
    public String identifyCardType() {
        String text = getStringToSplit();
        Log.i(TAG, "TEXT" + text);

        String[] splitString = text.split(",");
        for (String string : splitString){
            //convert string to upper case
            String splittedString = string.toLowerCase();
            //check for the type of card being used.
            if(splittedString.contains("voter") ){
                typeOfCard = "Voter ID Card";
            }else if(splittedString.contains("ssnit") ){
                typeOfCard = " SSNIT CARD";
            }else if(splittedString.contains("identity") ){
                typeOfCard = " National Identity Card";
            }else if(splittedString.contains("ecowas") ){
                typeOfCard = " ECOWAS CARD";
            }else if(splittedString.contains("nhis") ){
                typeOfCard = "NHIS Card";
            }else if(splittedString.contains("driver") ){
                typeOfCard = "Driver Licence";
            }else if(splittedString.contains("passport") ){
                typeOfCard = "PASSPORT";
            }
            break;
        }
        Log.i(TAG, "TYPE" + typeOfCard);
        return setTypeOfCard(typeOfCard);
    }
}
