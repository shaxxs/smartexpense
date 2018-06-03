package com.example.mathi.smartexpense.model;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class Customer {
    private int idCustomer;
    private String customerLastName;
    private String customerFirstName;
    private String customerAddress;
    private int customerCP;
    private String customerCity;
    private String customerPhone;
    private String customerEmail;

    /**
     * constructor
     */

    public Customer(int idCustomer, String customerLastName, String customerFirstName, String customerAddress, int customerCP, String customerCity, String customerPhone, String customerEmail) {
        this.idCustomer = idCustomer;
        this.customerLastName = customerLastName;
        this.customerFirstName = customerFirstName;
        this.customerAddress = customerAddress;
        this.customerCP = customerCP;
        this.customerCity = customerCity;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
    }

    /**
     * constructor
     */

    public Customer(int idCustomer, String customerLastName, String customerFirstName) {
        this.idCustomer = idCustomer;
        this.customerLastName = customerLastName;
        this.customerFirstName = customerFirstName;
    }

    /********************       GETTERS         *******************/

    /**
     * @return int
     */

    public int getIdCustomer() {
        return idCustomer;
    }

    /**
     * @return String
     */

    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
     * @return String
     */

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
     * @return String
     */

    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @return int
     */

    public int getCustomerCP() {
        return customerCP;
    }

    /**
     * @return String
     */

    public String getCustomerCity() {
        return customerCity;
    }

    /**
     * @return String
     */

    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @return String
     */

    public String getCustomerEmail() {
        return customerEmail;
    }

    /********************       SETTERS         *******************/

    /**
     * @param idCustomer l'id du client
     */

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    /**
     * @param customerLastName le nom du client
     */

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    /**
     * @param customerFirstName le prénom du client
     */

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    /**
     * @param customerAddress l'adresse du client
     */

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @param customerCP le code postal du client
     */

    public void setCustomerCP(int customerCP) {
        this.customerCP = customerCP;
    }

    /**
     * @param customerCity la ville du client
     */

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    /**
     * @param customerPhone le téléphone du client
     */

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * @param customerEmail le mail du client
     */

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


    /*******************************************************/

    /**
     * @return String
     */

    @Override
    public String toString() {
        return customerFirstName + " " + customerLastName;
    }
}
