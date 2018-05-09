package com.example.mathi.smartexpense.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mathi on 12/04/2018.
 */

public class ExpenseReport {
    private int code;
    private String date;
    private String city;
    private float amount;
    private String comment;
    private String submissionDate;

    public ExpenseReport(String date, String city, String comment, int code, String submissionDate, float amount) {
        this.date = date;
        this.city = city;
        this.comment = comment;
        this.code = code;
        this.submissionDate = submissionDate;
        this.amount = amount;
    }

    // fonction qui transforme la date format US au format FR
    public String setDateFormat(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);

        format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(newDate);
        return date;
    }

    public String getSubmissionDate() {
        String subDate = "null";
        try {
            subDate = String.valueOf(setDateFormat(submissionDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return subDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDate() {
        String date1 = "null";
        try {
            date1 = String.valueOf(setDateFormat(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
