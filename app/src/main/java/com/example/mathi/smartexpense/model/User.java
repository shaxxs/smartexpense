package com.example.mathi.smartexpense.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class User {

    private int idUser;
    private String userLastName;
    private String userFirstName;
    private String userEmail;
    private String userAddress;
    private int userCP;
    private String userCity;
    private String userPhone;
    private int idStatus;

    /**
     * constructor
     */

    public User(int idUser, String userLastName, String userFirstName, String userEmail, String userAddress, int userCP, String userCity, String userPhone, int idStatus) {
        this.idUser = idUser;
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userCP = userCP;
        this.userCity = userCity;
        this.userPhone = userPhone;
        this.idStatus = idStatus;
    }

    /**
     * constructor
     */

    public User(){
    }

    /********************       GETTERS         *******************/

    /**
     * @return int
     */

    public int getIdUser() {
        return idUser;
    }

    /**
     * @return String
     */

    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @return String
     */

    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @return String
     */

    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @return String
     */

    public String getUserAddress() {
        return userAddress;
    }

    /**
     * @return int
     */

    public int getUserCP() {
        return userCP;
    }

    /**
     * @return String
     */

    public String getUserCity() {
        return userCity;
    }

    /**
     * @return String
     */

    public String getUserPhone() {
        return userPhone;
    }

    /**
     * @return int
     */

    public int getIdStatus() {
        return idStatus;
    }


    /********************       SETTERS         *******************/

    /**
     * @param idUser l'id de l'utilisateur
     */

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * @param userLastName le nom de l'utilisateur
     */

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     * @param userFirstName le prénom de l'utilisateur
     */

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @param userEmail le mail de l'utilisateur
     */

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @param userAddress l'adresse de l'utilisateur
     */

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * @param userCP le code postal de l'utilisateur
     */

    public void setUserCP(int userCP) {
        this.userCP = userCP;
    }

    /**
     * @param userCity la ville de l'utilisateur
     */

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    /**
     * @param userPhone le téléphone de l'utilisateur
     */

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * @param idStatus l'id du statut de l'utilisateur
     */

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }


    /*******************************************************/

    /**
     * Recoit un objet JSON avec les données d'un utilisateur et le transforme en User
     * @param jsonObject un objet JSON
     * @return User
     */

    public User jsonToUser(JSONObject jsonObject) {
        User u;
        try {
            u = new User(jsonObject.getInt("idUser"), jsonObject.getString("userLastName"), jsonObject.getString("userFirstName"), jsonObject.getString("userEmail"), jsonObject.getString("userAddress"), jsonObject.getInt("userCP"), jsonObject.getString("userCity"), jsonObject.getString("userPhone"), jsonObject.getInt("idStatus"));
            return u;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Transforme un User en objet JSON (retourné en String)
     * @return String
     */

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", getIdUser());
            jsonObject.put("userLastName", getUserLastName());
            jsonObject.put("userFirstName", getUserFirstName());
            jsonObject.put("userEmail", getUserEmail());
            jsonObject.put("userAddress", getUserAddress());
            jsonObject.put("userCP", getUserCP());
            jsonObject.put("userCity", getUserCity());
            jsonObject.put("userPhone", getUserPhone());
            jsonObject.put("idStatus", getIdStatus());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
