package com.example.mathi.smartexpense.model;

/**
 * Created by mathi on 12/04/2018.
 */

public class ExpenseReport {
    private String code;
    private String date;
    private String city;
    //private float amount;
    private String comment;
    private String submissionDate;

    public ExpenseReport(String date, String city, String comment, String code, String submissionDate) {
        this.date = date;
        this.city = city;
        //this.amount = amount;
        this.comment = comment;
        this.code = code;
        this.submissionDate = submissionDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

   /* public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }*/

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
