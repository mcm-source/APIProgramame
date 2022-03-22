package com.example.programame_project_api.entities;

import com.example.programame_project_api.ProgramameProjectApiApplication;

import javax.persistence.*;

import static com.example.programame_project_api.ProgramameProjectApiApplication.*;


@Entity
@Table(name = "SimpleDonations")
public class SimpleDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;

    @Column(nullable = false)
    private double amount;

    @Transient
    private double totalAcount;





    @OneToOne(  mappedBy = "simpleDonation", fetch = FetchType.LAZY)
    private Sponsor sponsor;

    public SimpleDonation() {
    }

    public SimpleDonation(double amount) {
        this.amount = amount;

    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Double calculateAmount (){

        int numberTotalProblems = NUMBEROFSIMPLEPROBLEM+NUMBEROFMEDIUMPROBLEM+NUMBEROFHARDPROBLEM;

        return (numberTotalProblems*amount);




    }

    public double getTotalAcount() {
        return totalAcount;
    }

    public void setTotalAcount(double totalAcount) {
        this.totalAcount = totalAcount;
    }
}
