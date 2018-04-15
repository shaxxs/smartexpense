package com.example.mathi.smartexpense.model;

import android.widget.ImageView;

/**
 * Created by mathi on 12/04/2018.
 */

public class Expense {

    private int idExpense;
    private String date;
    private String beLabel;
    private String beDetails;
    private double expenseTotal;
    private ImageView status;

    public Expense(int idExpense, String date, String beLabel, String beDetails, double expenseTotal) {
        this.idExpense = idExpense;
        this.date = date;
        this.beLabel = beLabel;
        this.beDetails = beDetails;
        this.expenseTotal = expenseTotal;
    }

    public int getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBeLabel() {
        return beLabel;
    }

    public void setBeLabel(String beLabel) {
        this.beLabel = beLabel;
    }

    public String getBeDetails() {
        return beDetails;
    }

    public void setBeDetails(String beDetails) {
        this.beDetails = beDetails;
    }

    public double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }


}
