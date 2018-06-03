package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class Valid {

    private String validationState;
    private String validationDate;
    private int idUser;
    private String idExpenseB;
    private String idExpenseT;
    private int expenseReportCode;

    /**
     * constructor
     */

    public Valid(String validationState, String validationDate, int idUser, String idExpenseB, String idExpenseT, int expenseReportCode) {
        this.validationState = validationState;
        this.validationDate = validationDate;
        this.idUser = idUser;
        this.idExpenseB = idExpenseB;
        this.idExpenseT = idExpenseT;
        this.expenseReportCode = expenseReportCode;
    }

    /**
     * constructor
     */

    public Valid(){}

    /********************       GETTERS         *******************/

    /**
     * @return String
     */

    public String getValidationState() {
        if (this.validationState.isEmpty()){
            this.validationState = "Non soumise";
        }
        return this.validationState;
    }

    /**
     * @return String
     */

    public String getValidationDate() {
        String date = this.validationDate;
        try {
            date = String.valueOf(setDateFormat(this.validationDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return int
     */

    public int getIdUser() {
        return idUser;
    }

    /**
     * @return String
     */

    public String getIdExpenseB() {
        return idExpenseB;
    }

    /**
     * @return String
     */

    public String getIdExpenseT() {
        return idExpenseT;
    }

    /**
     * @return int
     */

    public int getExpenseReportCode() {
        return expenseReportCode;
    }



    /********************       SETTERS         *******************/


    /**
     * @param validationState le statut de validation
     */

    public void setValidationState(String validationState) {
        this.validationState = validationState;
    }

    /**
     * @param validationDate la date de validation
     */

    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    /**
     * @param idUser l'id de l'utilisateur (comptable)
     */

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * @param idExpenseB l'id de la dépense BusinessExpense
     */

    public void setIdExpenseB(String idExpenseB) {
        this.idExpenseB = idExpenseB;
    }

    /**
     * @param idExpenseT l'id de la dépense Travel
     */

    public void setIdExpenseT(String idExpenseT) {
        this.idExpenseT = idExpenseT;
    }

    /**
     * @param expenseReportCode l'id de la note de frais
     */

    public void setExpenseReportCode(int expenseReportCode) {
        this.expenseReportCode = expenseReportCode;
    }


    /*******************************************************/


    /**
     * Recoit un objet JSON avec les données d'une validation et le transforme en Valid
     * @param jsonObject un objet JSON
     * @return Valid
     */

    public Valid jsonToValid(JSONObject jsonObject) throws JSONException {
        Valid valid;
        String validationDate;
        String validationState;
        String idExpenseB;
        String idExpenseT;
        if (jsonObject.isNull("validationState")){
            validationState = "Non soumise";
        } else {
            validationState = jsonObject.getString("validationState");
        }
        if (jsonObject.isNull("dateValidation")){
            validationDate = "";
        } else {
            validationDate = jsonObject.getString("dateValidation");
        }
        if (jsonObject.isNull("idExpenseB")){
            idExpenseB = "";
        } else {
            idExpenseB = String.valueOf(jsonObject.getInt("idExpenseB"));
        }
        if (jsonObject.isNull("idExpenseT")){
            idExpenseT = "";
        } else {
            idExpenseT = String.valueOf(jsonObject.getInt("idExpenseT"));
        }
        try {
            valid = new Valid(validationState, validationDate,jsonObject.getInt("idUser"), idExpenseB, idExpenseT, jsonObject.getInt("expenseReportCode"));
            return valid;
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
