package com.example.hp.smartmess2.restModel;

import java.util.Date;

/**
 * Created by HP on 7/14/2016.
 */
public class Member {
    private String user;
    private String password;
    private String email;

    private String gender;
    private Date dob;
    private String location;
    private byte[] image;
    private String firstName;
    private String lastName;
    private String messName;
    private String type;
    private String occupation;

    public Member(String user, String password, String email, String gender, Date dob, String location, byte[] image, String firstName, String lastName, String messName, String type, String occupation) {
        this.user = user;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.location = location;
        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messName = messName;
        this.type = type;
        this.occupation = occupation;
    }

    public Member(String user, String password, String email, String gender, Date dob, String firstName, String lastName, String messName, String type) {
        this.user = user;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messName = messName;
        this.type = type;
    }

    public Member() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
