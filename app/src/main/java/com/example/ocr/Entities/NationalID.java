package com.example.ocr.Entities;

import androidx.room.PrimaryKey;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.Date;

public class NationalID extends SugarRecord {

    @Ignore
    private String country;
    @Ignore
    private String nameOfCard;
    @Column(name = "Surname")
    private String surname;
    @Column(name = "First_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Ignore
    private char sex;
    @Ignore
    private float height;
    @Ignore
    private String Expirydate;
    @Ignore
    private String dateOfBrth;
    @PrimaryKey
    @Column(name = "ID_Number")
    private String nationalID;

    public NationalID(){
    }

    public NationalID(String surname, String firstName, String middleName, String expirydate,
                      String dateOfBrth, String nationalID) {
        this.surname = surname;
        this.firstName = firstName;
        this.middleName = middleName;
        Expirydate = expirydate;
        this.dateOfBrth = dateOfBrth;
        this.nationalID = nationalID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNameOfCard() {
        return nameOfCard;
    }

    public void setNameOfCard(String nameOfCard) {
        this.nameOfCard = nameOfCard;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getExpirydate() {
        return Expirydate;
    }

    public void setExpirydate(String expirydate) {
        Expirydate = expirydate;
    }

    public String getDateOfBrth() {
        return dateOfBrth;
    }

    public void setDateOfBrth(String dateOfBrth) {
        this.dateOfBrth = dateOfBrth;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }




}
