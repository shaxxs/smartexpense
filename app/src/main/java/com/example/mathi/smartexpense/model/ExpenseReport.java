package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class ExpenseReport {
    private int idUser;
    private int idCustomer;
    private int expenseReportCode;
    private String expenseReportDate;
    private String expenseReportCity;
    private float amount;
    private String expenseReportComment;
    private String submissionDate;

    /**
     * constructor
     */

    public ExpenseReport(String expenseReportDate, String expenseReportCity, String expenseReportComment, int expenseReportCode, String submissionDate, float amount, int idUser, int idCustomer) {
        this.expenseReportDate = expenseReportDate;
        this.expenseReportCity = expenseReportCity;
        this.expenseReportComment = expenseReportComment;
        this.expenseReportCode = expenseReportCode;
        this.submissionDate = submissionDate;
        this.amount = amount;
        this.idUser = idUser;
        this.idCustomer = idCustomer;
    }

    /**
     * constructor
     */

    public ExpenseReport(String expenseReportDate, String expenseReportCity, String expenseReportComment, String submissionDate, int idUser, int idCustomer) {
        this.expenseReportDate = expenseReportDate;
        this.expenseReportCity = expenseReportCity;
        this.expenseReportComment = expenseReportComment;
        this.submissionDate = submissionDate;
        this.idUser = idUser;
        this.idCustomer = idCustomer;
    }

    /**
     * constructor
     */

    public ExpenseReport(){

    }

    /********************       GETTERS         *******************/

    /**
     * @return String
     */

    public String getSubmissionDate() {
        String subDate = this.submissionDate;
        try {
            subDate = setDateFormat(this.submissionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return subDate;
    }

    /**
     * @return int
     */

    public int getIdUser() {
        return idUser;
    }

    /**
     * @return int
     */

    public int getIdCustomer() {
        return idCustomer;
    }

    /**
     * @return int
     */

    public int getExpenseReportCode() {
        return expenseReportCode;
    }

    /**
     * @return String
     */

    public String getExpenseReportCity() {
        return expenseReportCity;
    }

    /**
     * @return String
     */

    public String getExpenseReportComment() {
        return expenseReportComment;
    }

    /**
     * @return String
     */

    public String getExpenseReportDate() {
        String date = this.expenseReportDate;
        try {
            date = String.valueOf(setDateFormat(this.expenseReportDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return float
     */

    public float getAmount() {
        return amount;
    }


    /********************       SETTERS         *******************/

    /**
     * @param submissionDate le date de soumission de la note de frais
     */

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * @param idUser l'id de l'utilisateur de la note de frais
     */

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * @param idCustomer l'id du client associé à la note de frais
     */

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    /**
     * @param expenseReportCode l'id de la note de frais
     */

    public void setExpenseReportCode(int expenseReportCode) {
        this.expenseReportCode = expenseReportCode;
    }

    /**
     * @param expenseReportCity la ville de la note de frais
     */

    public void setExpenseReportCity(String expenseReportCity) {
        this.expenseReportCity = expenseReportCity;
    }

    /**
     * @param expenseReportComment le commentaire de la note de frais
     */

    public void setExpenseReportComment(String expenseReportComment) {
        this.expenseReportComment = expenseReportComment;
    }

    /**
     * @param expenseReportDate la date de la note de frais
     */

    public void setExpenseReportDate(String expenseReportDate) {
        this.expenseReportDate = expenseReportDate;
    }

    /**
     * @param amount le montant de la note de frais
     */

    public void setAmount(float amount) {
        this.amount = amount;
    }

    /*******************************************************/

    /**
     * Transforme une ExpenseReport en objet JSON (retourné en String)
     * @return String
     */

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("expenseReportCode", getExpenseReportCode());
            jsonObject.put("expenseReportDate", getExpenseReportDate());
            jsonObject.put("expenseReportCity", getExpenseReportCity());
            jsonObject.put("expenseReportComment", getExpenseReportComment());
            jsonObject.put("submissionDate", getSubmissionDate());
            jsonObject.put("amount", getAmount());
            jsonObject.put("idUser", getIdUser());
            jsonObject.put("idCustomer", getIdCustomer());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Recoit un objet JSON avec les données d'une note de frais et le transforme en ExpenseReport
     * @param jsonObject un objet JSON
     * @return ExpenseReport
     */

    public ExpenseReport jsonToExpenseReport(JSONObject jsonObject) {
        ExpenseReport er;
        try {
            er = new ExpenseReport(jsonObject.getString("expenseReportDate"), jsonObject.getString("expenseReportCity"),jsonObject.getString("expenseReportComment"), jsonObject.getInt("expenseReportCode"), jsonObject.getString("submissionDate"), jsonObject.getInt("amount"), jsonObject.getInt("idUser"), jsonObject.getInt("idCustomer"));
            return er;
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
