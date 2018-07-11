/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Customer;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Djan Sarwari
 */
@Stateless
@Path("customer")
public class CustomerFacadeREST extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public CustomerFacadeREST() {
        super(Customer.class);
    }
    
    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_JSON})
    public Customer find(@QueryParam("token") String token, @QueryParam("customerId") String customerId) {
        Customer c = new Customer();
        c.setId(null);
        c.setResponse("Error: permission denied");
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            Customer c1 = super.find(customerId);
            if (c1 != null) {
                c1.setResponse("OK");  
                return c1;
            } else {
                c.setResponse("Error: customer not found");
            }
        }
        return c;
    }

    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Customer> getAll(@QueryParam("token") String token) {
        List<Customer> l = new ArrayList<>();
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        return l;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
