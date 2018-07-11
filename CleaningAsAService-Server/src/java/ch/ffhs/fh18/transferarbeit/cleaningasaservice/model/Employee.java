/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.model;

import java.io.Serializable;
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
@Table(schema = "CLEANINGASSERVICE", name = "EMPLOYEE")
@XmlRootElement
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id = UUID.randomUUID().toString();

    private int salary;
    private int availability;
    private String response;
    
    @OneToOne
    private Person person;

    public Employee() {
    } 

    public Employee(int salary, int availability, String response, Person person) {
        this.salary = salary;
        this.availability = availability;
        this.response = response;
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
 

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }  
    
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Employee[ id=" + id + " ]";
    }
    
}
