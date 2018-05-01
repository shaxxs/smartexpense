package com.example.mathi.smartexpense.model;

import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mathi on 12/04/2018.
 */

public class Expense {

    private int idExpense;
    private String date;
    private String label;
    private String details;
    private double expenseTotal;
    private String submissionDate;

    public Expense(int idExpense, String date, String label, String details, double expenseTotal, String submissionDate) {
        this.idExpense = idExpense;
        this.date = date;
        this.label = label;
        this.details = details;
        this.expenseTotal = expenseTotal;
        this.submissionDate = submissionDate;
    }

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

    public int getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String beLabel) {
        this.label = beLabel;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String beDetails) {
        this.details = details;
    }

    public double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }


}
