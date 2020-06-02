package com.example.ocr.Entities;

import androidx.room.PrimaryKey;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;

public class Ssnit extends SugarRecord {

    @Ignore
    private String nameOfCard;
    @Ignore
    private String country;
    @Column(name = "Name")
    private String name;

    public Ssnit(){

    }

    public Ssnit(String name, String ssnitNumber, String serialNumber,
                 String dateOfBirth) {
        this.name = name;
        this.ssnitNumber = ssnitNumber;
        this.serialNumber = serialNumber;
        this.dateOfBirth = dateOfBirth;
    }

    @PrimaryKey
    @Column(name = "SSNIT_Number")
    private String ssnitNumber;
    @Column(name ="Serial_Number")
    @PrimaryKey
    private String serialNumber;
    @Ignore
    private String gender;
    @Ignore
    private String dateOfBirth;

    public String getNameOfCard() {
        return nameOfCard;
    }

    public void setNameOfCard(String nameOfCard) {
        this.nameOfCard = nameOfCard;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsnitNumber() {
        return ssnitNumber;
    }

    public void setSsnitNumber(String ssnitNumber) {
        this.ssnitNumber = ssnitNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }



}
