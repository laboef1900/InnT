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
@Table(schema = "CLEANINGASSERVICE", name = "ORDER")
@XmlRootElement
public class MyOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    
    private Timestamp dateOrdered;
    private int hours;
    private String description;
    private String response;
    
    @OneToOne
    private Customer customer;
    
    private Status status;
    private PaymentStatus paymentStatus;

    public MyOrder() {
        dateOrdered = new Timestamp(System.currentTimeMillis());
        paymentStatus = PaymentStatus.OPEN;
        status = Status.OPEN;
        id = UUID.randomUUID().toString();
    }

    public MyOrder(Timestamp date, int hours, String description, Customer customer, Status status, PaymentStatus paymentStatus) {
        this.dateOrdered = date;
        this.hours = hours;
        this.description = description;
        this.customer = customer;
        this.status = status;
        this.paymentStatus = paymentStatus;
        id = UUID.randomUUID().toString();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Timestamp getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Timestamp dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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
        if (!(object instanceof MyOrder)) {
            return false;
        }
        MyOrder other = (MyOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Order[ id=" + id + " ]";
    }
    
}
