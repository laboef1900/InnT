/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Djan Sarwari
 */
@Entity
@Table(schema = "CLEANINGASSERVICE", name = "ACCOUNT")
@XmlRootElement
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id = UUID.randomUUID().toString();
    
    @Column(unique=true)
    private String email;
    
    private String password;
    private AccountType type;
    private String response = "OK";
    
    private AccountRole accountrole = AccountRole.EMPLOYEE;

    public Account() {
    }
    
    public Account(String email, String password, AccountType type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }  
    
    public AccountRole getAccountRole() {
        return this.accountrole;
    }
    
    public void setAccountRole(AccountRole accountRole) {
        this.accountrole = accountRole;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getPassword() {
        return password;
    }*/

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ( !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Account[ id=" + id + " ]";
    }
    
}
