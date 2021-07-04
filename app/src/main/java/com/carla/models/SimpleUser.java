package com.carla.models;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class SimpleUser
{
    private String firstname;
    private String lastname;
    private String imageLink;
    private String userID;

    public SimpleUser() {
    }

    public SimpleUser(String firstname, String lastname, String imageLink) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.imageLink = imageLink;
    }

    public SimpleUser(User user)
    {
        firstname = user.getFirstname();
        lastname = user.getLastname();
        imageLink = user.getImageLink();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public String toString() {
        return "SimpleUser{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
