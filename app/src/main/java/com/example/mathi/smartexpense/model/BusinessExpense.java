package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class BusinessExpense {

    private String businessExpenseLabel;
    private String businessExpenseDetails;
    private String businessExpenseDate;
    private int idExpense;
    private int expenseReportCode;
    private String paymentDate;
    private String refundAmount;
    private float expenseTotal;
    private String idProof;

    /**
     * constructor
     */

    public BusinessExpense(String businessExpenseLabel, String businessExpenseDetails, String businessExpenseDate, int idExpense, int expenseReportCode, String paymentDate, String refundAmount, float expenseTotal, String idProof) {
        this.businessExpenseLabel = businessExpenseLabel;
        this.businessExpenseDetails = businessExpenseDetails;
        this.businessExpenseDate = businessExpenseDate;
        this.idExpense = idExpense;
        this.expenseReportCode = expenseReportCode;
        this.paymentDate = paymentDate;
        this.refundAmount = refundAmount;
        this.expenseTotal = expenseTotal;
        this.idProof = idProof;
    }

    /**
     * constructor
     */

    public BusinessExpense(String businessExpenseLabel, String businessExpenseDetails, String businessExpenseDate, int expenseReportCode, float expenseTotal) {
        this.businessExpenseLabel = businessExpenseLabel;
        this.businessExpenseDetails = businessExpenseDetails;
        this.businessExpenseDate = businessExpenseDate;
        this.expenseReportCode = expenseReportCode;
        this.expenseTotal = expenseTotal;
    }

    /**
     * constructor
     */

    public BusinessExpense(){}

    /********************       GETTERS         *******************/

    /**
     * @return String
     */

    public String getBusinessExpenseLabel() {
        return businessExpenseLabel;
    }

    /**
     * @return String
     */

    public String getBusinessExpenseDetails() {
        return businessExpenseDetails;
    }

    /**
     * @return String
     */

    public String getBusinessExpenseDate() {
        String date = this.businessExpenseDate;
        try {
            date = String.valueOf(setDateFormat(this.businessExpenseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return int
     */

    public int getIdExpense() {
        return idExpense;
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

    public String getPaymentDate() {
        if (this.paymentDate != null) {
            String date = this.paymentDate;
            try {
                date = String.valueOf(setDateFormat(this.paymentDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return this.paymentDate;
        }
    }

    /**
     * @return String
     */

    public String getRefundAmount() {
        return refundAmount;
    }

    /**
     * @return float
     */

    public float getExpenseTotal() {
        return expenseTotal;
    }

    /**
     * @return String
     */

    public String getIdProof() {
        return idProof;
    }

    /********************       SETTERS         *******************/

    /**
     * @param businessExpenseLabel le label de la dépense
     */

    public void setBusinessExpenseLabel(String businessExpenseLabel) {
        this.businessExpenseLabel = businessExpenseLabel;
    }

    /**
     * @param businessExpenseDetails le détail de la dépense
     */

    public void setBusinessExpenseDetails(String businessExpenseDetails) {
        this.businessExpenseDetails = businessExpenseDetails;
    }

    /**
     * @param businessExpenseDate le date de la dépense
     */

    public void setBusinessExpenseDate(String businessExpenseDate) {
        this.businessExpenseDate = businessExpenseDate;
    }

    /**
     * @param idExpense l'id de la dépense
     */

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    /**
     * @param expenseReportCode le code de la note de frais de la dépense
     */

    public void setExpenseReportCode(int expenseReportCode) {
        this.expenseReportCode = expenseReportCode;
    }

    /**
     * @param paymentDate la date de paiement de la dépense
     */

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @param refundAmount la montant remboursé de la dépense
     */

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * @param expenseTotal la montant de la dépense
     */

    public void setExpenseTotal(float expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    /**
     * @param idProof l'id du justificatif de la dépense
     */

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    /*******************************************************/

    /**
     * Recoit un objet JSON avec les données d'une dépense et le transforme en BusinessExpense
     * @param jsonObject un objet JSON
     * @return BusinessExpense
     */

    public BusinessExpense jsonToBusinessExpense(JSONObject jsonObject) throws JSONException {
        BusinessExpense businessExp;
        String businessExpenseDetails;
        String refundAmount;
        String paymentDate;
        String idProof;
        if (jsonObject.isNull("businessExpenseDetails")){
            businessExpenseDetails = null;
        } else {
            businessExpenseDetails = jsonObject.getString("businessExpenseDetails");
        }
        if (jsonObject.isNull("paymentDateB")){
            paymentDate = null;
        } else {
            paymentDate = jsonObject.getString("paymentDateB");
        }
        if (jsonObject.isNull("refundAmountB")){
            refundAmount = null;
        } else {
            refundAmount = String.valueOf(jsonObject.getInt("refundAmountB"));
        }
        if (jsonObject.isNull("idProofB")){
            idProof = null;
        } else {
            idProof = String.valueOf(jsonObject.getInt("idProofB"));
        }
        try {
            businessExp = new BusinessExpense(jsonObject.getString("businessExpenseLabel"), businessExpenseDetails,jsonObject.getString("businessExpenseDate"), jsonObject.getInt("idExpenseB"), jsonObject.getInt("expenseReportCodeB"), paymentDate, refundAmount, jsonObject.getInt("expenseTotalB"),idProof);
            return businessExp;
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
