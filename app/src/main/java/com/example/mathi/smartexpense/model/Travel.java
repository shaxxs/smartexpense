package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class Travel {

    private String travelDuration;
    private String departureCity;
    private String destinationCity;
    private String departureDate;
    private String returnDate;
    private int km;
    private int idExpense;
    private int expenseReportCode;
    private String paymentDate;
    private String refundAmount;
    private float expenseTotal;
    private String idProof;

    /**
     * constructor
     */

    public Travel(String travelDuration, String departureCity, String destinationCity, String departureDate, String returnDate, int km, int idExpense, int expenseReportCode, String paymentDate, String refundAmount, float expenseTotal, String idProof) {
        this.travelDuration = travelDuration;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.km = km;
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

    public Travel(String travelDuration, String departureCity, String destinationCity, String departureDate, String returnDate, int km, int expenseReportCode, float expenseTotal) {
        this.travelDuration = travelDuration;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.km = km;
        this.expenseReportCode = expenseReportCode;
        this.expenseTotal = expenseTotal;
    }

    /**
     * constructor
     */

    public Travel() {}

    /********************       GETTERS         *******************/

    /**
     * @return String
     */

    public String getTravelDuration() {
        return travelDuration;
    }

    /**
     * @return String
     */

    public String getDepartureCity() {
        return departureCity;
    }

    /**
     * @return String
     */

    public String getDestinationCity() {
        return destinationCity;
    }

    /**
     * @return String
     */

    public String getDepartureDate() {
        String date = this.departureDate;
        try {
            date = String.valueOf(setDateFormat(this.departureDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return String
     */

    public String getReturnDate() {
        String date = this.returnDate;
        try {
            date = String.valueOf(setDateFormat(this.returnDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return int
     */

    public int getKm() {
        return km;
    }

    /**
     * @return int
     */

    public int getIdExpense() {
        return idExpense;
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

    /**
     * @return int
     */

    public int getExpenseReportCode() {
        return expenseReportCode;
    }


    /********************       SETTERS         *******************/

    /**
     * @param travelDuration la durée du trajet
     */

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }

    /**
     * @param departureCity la ville de départ
     */

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    /**
     * @param destinationCity la ville de destination
     */

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    /**
     * @param departureDate la date de départ
     */

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @param returnDate la date de retour
     */

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @param km le nombre de kms parcourus
     */

    public void setKm(int km) {
        this.km = km;
    }

    /**
     * @param idExpense l'id de la dépense
     */

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    /**
     * @param expenseReportCode l'id de la note de frais
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
     * @param refundAmount le montant remboursé
     */

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * @param expenseTotal le montant de la dépense
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
     * Recoit un objet JSON avec les données d'une dépense et le transforme en Travel
     * @param jsonObject un objet JSON
     * @return Travel
     */

    public Travel jsonToTravel(JSONObject jsonObject) throws JSONException {
        Travel travel;
        String travelDuration;
        String idProof;
        String paymentDate;
        String refundAmount;
        if (jsonObject.isNull("travelDuration")){
            travelDuration = null;
        } else {
            travelDuration = String.valueOf(jsonObject.getInt("travelDuration"));
        }
        if (jsonObject.isNull("idProofT")){
            idProof = null;
        } else {
            idProof = String.valueOf(jsonObject.getInt("idProofT"));
        }
        if (jsonObject.isNull("paymentDateT")){
            paymentDate = null;
        } else {
            paymentDate = jsonObject.getString("paymentDateT");
        }
        if (jsonObject.isNull("refundAmountT")){
            refundAmount = null;
        } else {
            refundAmount = String.valueOf(jsonObject.getInt("refundAmountT"));
        }
        try {
            travel = new Travel(travelDuration, jsonObject.getString("departureCity"),jsonObject.getString("destinationCity"), jsonObject.getString("departureDate"), jsonObject.getString("returnDate"), jsonObject.getInt("km"), jsonObject.getInt("idExpenseT"), jsonObject.getInt("expenseReportCodeT"), paymentDate, refundAmount, jsonObject.getInt("expenseTotalT"), idProof);
            return travel;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Transforme un Travel en objet JSON (retourné en String)
     * @return String
     */

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("travelDuration", getTravelDuration());
            jsonObject.put("departureCity", getDepartureCity());
            jsonObject.put("destinationCity", getDestinationCity());
            jsonObject.put("departureDate", getDepartureDate());
            jsonObject.put("returnDate", getReturnDate());
            jsonObject.put("km", getKm());
            jsonObject.put("idExpenseT", getIdExpense());
            jsonObject.put("expenseReportCodeT", getExpenseReportCode());
            jsonObject.put("paymentDateT", getPaymentDate());
            jsonObject.put("refundAmountT", getRefundAmount());
            jsonObject.put("expenseTotalT", getExpenseTotal());
            jsonObject.put("idProofT", getIdProof());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
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
