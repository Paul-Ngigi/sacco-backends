package org.sacco.backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;



@Entity
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    private Users user;

    private double amount;

    private double dividends;

    private boolean isLoanPresent;

    public Accounts() {
        isLoanPresent = false;
        amount = 0.0;
        dividends = 0.0;
    }

    public Accounts(final Users client, final double amt) {
        this.user = client;
        isLoanPresent = false;
        amount = amt;
        dividends = 0.0;
    }

    public boolean isLoanPresent() {
        return isLoanPresent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDividends() {
        return dividends;
    }

    public void setDividends(double dividends) {
        this.dividends = dividends;
    }

    public void setLoanPresent(boolean isLoanPresent) {
        this.isLoanPresent = isLoanPresent;
    }

    public Users getClient() {
        return this.user;
    }

    public Long getId() {
        return this.id;
    }
}