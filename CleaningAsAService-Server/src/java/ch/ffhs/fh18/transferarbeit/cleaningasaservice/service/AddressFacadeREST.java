/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Address;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Login;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Person;
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
@Path("address")
public class AddressFacadeREST extends AbstractFacade<Address> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public AddressFacadeREST() {
        super(Address.class);
    }

    @GET
    @Path("register")
    @Produces({MediaType.APPLICATION_JSON})
    public Address assign(@QueryParam("token") String token, @QueryParam("street") String street, @QueryParam("postcode") String postcode, 
            @QueryParam("location") String location) {
        Address a = new Address();
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if ( login == null ) {
            a.setId(null);
            a.setResponse("Error: permission denied to register address");
            return a;
        }
        Person p = StorageUtil.getPersonByLogin(getEntityManager(), login);
        if (p == null) {
            a.setId(null);
            a.setResponse("Error: no valid person found to register address for");
        } else {
            a.setStreet(street);
            a.setPostcode(postcode);
            a.setLocation(location);
            super.create(a);
            p.setAddress(a);
            getEntityManager().merge(p);
        }
        return a;
    }
    
    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public Address myAddress(@QueryParam("token") String token) {
        Address a = new Address();
        a.setId(null);
                
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login == null) {
            a.setResponse("Error: permission denied");
        } else {
            Person p = StorageUtil.getPersonByLogin(getEntityManager(), login);
            if ( p == null ) {
                a.setResponse("Error: No Person found for your account");
            } else {
                return super.find(p.getAddress().getId());
            }

        }
        return a;
    }
    
    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Address> getAll(@QueryParam("token") String token) {
        List<Address> l = new ArrayList<>();
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
