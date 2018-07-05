package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class Expense {

    private int idExpense;
    private String date;
    private String label;
    private String details;
    private double expenseTotal;
    private String submissionDate;

    /**
     * constructor
     */

    public Expense(int idExpense, String date, String label, String details, double expenseTotal, String submissionDate) {
        this.idExpense = idExpense;
        this.date = date;
        this.label = label;
        this.details = details;
        this.expenseTotal = expenseTotal;
        this.submissionDate = submissionDate;
    }

    /**
     * constructor
     */

    public Expense(){}

    /********************       GETTERS         *******************/

    /**
     * @return int
     */

    public int getIdExpense() {
        return idExpense;
    }

    /**
     * @return String
     */

    public String getDate() {
        String date1 = this.date;
        try {
            date1 = String.valueOf(setDateFormat(this.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * @return String
     */

    public String getLabel() {
        return label;
    }

    /**
     * @return String
     */

    public String getDetails() {
        return details;
    }

    /**
     * @return String
     */

    public String getSubmissionDate() {
        String subDate = this.submissionDate;
        try {
            subDate = String.valueOf(setDateFormat(this.submissionDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return subDate;
    }

    /**
     * @return double
     */

    public double getExpenseTotal() {
        return expenseTotal;
    }

    /********************       SETTERS         *******************/

    /**
     * @param submissionDate le date de soumission de la note de frais de la dépense
     */

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * @param idExpense l'id de la dépense
     */

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    /**
     * @param date la date de la dépense
     */

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param label le label de la dépense
     */

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param details le détail de la dépense
     */

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @param expenseTotal le montant de la dépense
     */

    public void setExpenseTotal(double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    /*******************************************************/

    /**
     * Transforme une Expense en objet JSON (retourné en String)
     * @return String
     */

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idExpense", getIdExpense());
            jsonObject.put("expenseDate", getDate());
            jsonObject.put("expenseLabel", getLabel());
            jsonObject.put("expenseDetails", getDetails());
            jsonObject.put("expenseTotal", getExpenseTotal());
            jsonObject.put("submissionDate", getSubmissionDate());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Recoit un objet JSON avec les données d'une dépense et le transforme en Expense
     * @param jsonObject un objet JSON
     * @return Expense
     */

    public Expense jsonToExpense(JSONObject jsonObject) {
        Expense exp;
        try {
            exp = new Expense(jsonObject.getInt("idExpense"), jsonObject.getString("expenseDate"),jsonObject.getString("expenseLabel"), jsonObject.getString("expenseDetails"), jsonObject.getInt("expenseTotal"), jsonObject.getString("submissionDate"));
            return exp;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recoit une date au format US et la transforme en format FR
     * @param date date
     * @return String date
     */

    private String setDateFormat(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);

        format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(newDate);
        return date;
    }
}
