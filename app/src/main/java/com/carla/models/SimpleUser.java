package com.carla.models;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class SimpleUser
{
    private String firstname;
    private String lastname;
    private String imageLink;

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

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", imageLink='" + imageLink + '\'' +
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
}
