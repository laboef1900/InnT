/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Djan Sarwari
 */
@Entity
@Table(schema = "CLEANINGASSERVICE", name = "STAMP")
@XmlRootElement
public class Stamp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id = UUID.randomUUID().toString();
    
    private Timestamp startTime;    
    
    private Timestamp endTime;
    
    private String response;
    
    @OneToOne
    private Employee employee;
    
    @OneToOne
    private Customer customer;

    public Stamp() {
        this.startTime = new Timestamp(System.currentTimeMillis());
    }

    public Stamp(Timestamp startTime, Timestamp endTime, Employee employee, Customer customer) {
        this.startTime = startTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
        this.customer = customer;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

   // public Employee getEmployee() {
  //      return employee;
 //   }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

   // public Customer getCustomer() {
    //    return customer;
   // }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        if (!(object instanceof Stamp)) {
            return false;
        }
        Stamp other = (Stamp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Stamp[ id=" + id + " ]";
    }   

}
