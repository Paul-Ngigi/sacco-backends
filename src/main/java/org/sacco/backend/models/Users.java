package org.sacco.backend.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String lastName;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 5000)
    private String token;

    private String uid;

    private String role;

    @Column(length = 5000000)
    private String authToken;

    private boolean isActive;

    public Users() {
        this.isActive = false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(final String tok) {
        this.token=tok;
    }

    public void setAuthToken(final String authTok) {
        this.authToken = new HashPassword()
            .getHashToken(authTok);
    }

    public String getAuthToken() {
        return this.authToken;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Users(final String Fname, final String Mname,
        final String Lname, final String mail,final String password) {
            this();
            this.email = mail;
            this.firstName = Fname;
            this.username =  Mname;
            this.lastName = Lname;
            this.setAuthToken(password);

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
}