package com.example.mathi.smartexpense.model;

import android.widget.ImageView;

/**
 * Created by mathi on 12/04/2018.
 */

public class Expense {

    private int idExpense;
    private String date;
    private String label;
    private String details;
    private double expenseTotal;

    public Expense(int idExpense, String date, String label, String details, double expenseTotal) {
        this.idExpense = idExpense;
        this.date = date;
        this.label = label;
        this.details = details;
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
