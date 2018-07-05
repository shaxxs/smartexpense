package com.example.mathi.smartexpense.model;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class Proof {
    private int idProof;
    private String proofTitle;
    private String proofUrl;
    private int idExpenseT;
    private int idExpenseB;
    private int expenseReportCode;

    /**
     * constructor
     */

    public Proof(int idProof, String proofTitle, String proofUrl, int idExpenseT, int idExpenseB, int expenseReportCode) {
        this.idProof = idProof;
        this.proofTitle = proofTitle;
        this.proofUrl = proofUrl;
        this.idExpenseT = idExpenseT;
        this.idExpenseB = idExpenseB;
        this.expenseReportCode = expenseReportCode;
    }

    /**
     * constructor
     */

    public Proof(String proofTitle, String proofUrl, int expenseReportCode) {
        this.proofTitle = proofTitle;
        this.proofUrl = proofUrl;
        this.expenseReportCode = expenseReportCode;
    }

    /**
     * constructor
     */

    public Proof(String proofUrl){
        this.proofUrl = proofUrl;
    }


    /********************       GETTERS         *******************/

    /**
     * @return int
     */

    public int getIdProof() {
        return idProof;
    }

    /**
     * @return String
     */

    public String getProofTitle() {
        return proofTitle;
    }

    /**
     * @return String
     */

    public String getProofUrl() {
        return proofUrl;
    }

    /**
     * @return int
     */

    public int getIdExpenseT() {
        return idExpenseT;
    }

    /**
     * @return int
     */

    public int getIdExpenseB() {
        return idExpenseB;
    }

    /**
     * @return int
     */

    public int getExpenseReportCode() {
        return expenseReportCode;
    }


    /********************       SETTERS         *******************/

    /**
     * @param idProof l'id du justificatif
     */

    public void setIdProof(int idProof) {
        this.idProof = idProof;
    }

    /**
     * @param proofTitle le titre du justificatif
     */

    public void setProofTitle(String proofTitle) {
        this.proofTitle = proofTitle;
    }

    /**
     * @param proofUrl l'url du justificatif
     */

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    /**
     * @param idExpenseT l'id de la dépense Travel du justificatif
     */

    public void setIdExpenseT(int idExpenseT) {
        this.idExpenseT = idExpenseT;
    }

    /**
     * @param idExpenseB l'id de la dépense BusinessExpense du justificatif
     */

    public void setIdExpenseB(int idExpenseB) {
        this.idExpenseB = idExpenseB;
    }

    /**
     * @param expenseReportCode l'id de la note de frais du justificatif
     */

    public void setExpenseReportCode(int expenseReportCode) {
        this.expenseReportCode = expenseReportCode;
    }
}
