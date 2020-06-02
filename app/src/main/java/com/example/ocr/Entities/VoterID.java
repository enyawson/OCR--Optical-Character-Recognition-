package com.example.ocr.Entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

/**
 * The class represents the database schema or details for
 * voterID cards.
 * @athour Yawson Emmanuel
 * @version 1.0
 *
 */
public class VoterID extends SugarRecord {

    @Ignore
    private String cardName;
    @Ignore
    private String country;
    @Ignore
   private String pollStationCode;
    @Column(name ="voter_id")
    @Unique
    private String voterID;
    @Ignore
    private int voterAge;
    @Ignore
    private String voterSex;
    @Ignore
    private String registrationDate;
    @Column(name ="voter_name")
    private String voterName;

    /**
     * This creates the a new voter without taking inputs
     *
     */
    public VoterID() {
    }
    /**
     * This creates the a new voter with the following input or parameters.
     * @param cardName This is the type of card, in this case voter card
     * @param voterID  This is the ID of the voter
     * @param voterName  This is the name of the voter
     *
     */
    public VoterID(String cardName, String voterID, String voterName,String registrationDate) {
        this.cardName = cardName;
        this.voterID = voterID;
        this.voterName = voterName;
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the type of card name
     * @return cardName A string representing the type
     * of card being used
     */
    public String getCardName() {
        return cardName;
    }
    /**
     * Sets the type of card name
     * @param cardName A string representing the card type
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    /**
     * Gets the country of card issue
     * @return country A string representing the country of card issue
     */
    public String getCountry() {
        return country;
    }
    /**
     * Sets the country of card issue
     * @param country A string representing country of card issue   */
    public void setCountry(String country) {
        this.country = country;
    }

    public String getPollStationCode() {
        return pollStationCode;
    }

    public void setPollStationCode(String pollStationCode) {
        this.pollStationCode = pollStationCode;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public int getVoterAge() {
        return voterAge;
    }

    public void setVoterAge(int voterAge) {
        this.voterAge = voterAge;
    }

    public String getVoterSex() {
        return voterSex;
    }

    public void setVoterSex(String voterSex) {
        this.voterSex = voterSex;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }


}
