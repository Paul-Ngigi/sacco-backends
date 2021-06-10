package org.sacco.backend.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private double paid;

    private boolean isApproved;

    private boolean isFullyPaid;

    @Column(length = 10000)
    private String disapproveReason;

    @OneToMany()
    private List<Users> guaranters;

    // @OneToMany()
    // private List<Schedule> schedule;

    @OneToOne()
    private Users user;


    public Loan() {
        this.isApproved = false;
        this.isFullyPaid = false;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(final double paid) {
        this.paid = paid;
    }

    public Loan(final Users usr, final double amt,
        final List<Users> guaranters) {
        this.user = usr;
        this.amount = amt;
        this.isApproved = false;
        this.isFullyPaid = false;

    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(final boolean isApprov) {
        this.isApproved = isApprov;
        if (isApprov) {
            this.isFullyPaid = false;
        }
    }

    // public List<Schedule> getSchedule() {
    //     return schedule;
    // }

    // public void addSchedule(final Schedule sched) {
    //     this.schedule.add(sched);
    // }

    public boolean isFullyPaid() {
        return isFullyPaid;
    }

    public List<Users> getGuaranters() {
        return this.guaranters;
    }

    public void addGuaranters(final Users guaranter) {
        this.guaranters.add(guaranter);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amt) {
        this.amount = amt;
    }

    public double getBalance() {
        return this.amount - this.paid;
    }


    public void setFullyPaid() {
        if (this.isApproved && this.paid == this.amount) {
            this.isFullyPaid = true;
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setDisapproveReason(final String reason) {
        this.disapproveReason = reason;
    }

    public String getDisapproveReason() {
        return this.disapproveReason;
    }

    public Users getUser() { return this.user;}
}
