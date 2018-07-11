/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

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
@Path("person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @GET
    @Path("register")
    @Produces({MediaType.APPLICATION_JSON})
    public Person assign(@QueryParam("token") String token, @QueryParam("firstname") String firstname, @QueryParam("lastname") String lastname ) {
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        Person p = new Person();
        if ( login == null ) {
            p.setId(null);
            p.setResponse("Error: permission denied to register person");
        }
        Person accountPerson = StorageUtil.getPersonByLogin(getEntityManager(), login);
        if (accountPerson == null) {            
            p.setId(null);
            p.setResponse("Error: no valid person found to register address for");
        } else {           
            accountPerson.setFirstname(firstname);
            accountPerson.setLastname(lastname);
            super.edit(accountPerson);
            p.setResponse("OK");
            p.setId(accountPerson.getId());
            p.setFirstname(firstname);
            p.setLastname(lastname);            
        }
        return p;
    }
    
    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public Person myPerson(@QueryParam("token") String token) {
        Person p = new Person();
        p.setId(null);
                
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login == null) {
            p.setResponse("Error: permission denied");           
        } else {
            Person acP = StorageUtil.getPersonByLogin(getEntityManager(), login);
            if ( acP == null ) {
                p.setResponse("Error: No Person found for your account");
            } else {
                acP.setResponse("OK");
                return acP;
            }

        }
        return p;
    }
    
    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> getAll(@QueryParam("token") String token) {
        List<Person> l = new ArrayList<>();
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
