package caas.slutz.ffhs.ch.caas;

import java.io.Serializable;

public class Person implements Serializable{
    String token;
    String custID;
    String lastStamp;
    String actype;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getActype() {
        return actype;
    }

    public void setActype(String actype) {
        this.actype = actype;
    }
}
