/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Account;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Login;
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
@Path("login")
public class LoginFacadeREST extends AbstractFacade<Login> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public LoginFacadeREST() {
        super(Login.class);
    }    

    @GET    
    @Produces({MediaType.APPLICATION_JSON})
    public Login login(@QueryParam("acemail") String acemail,@QueryParam("acpassword") String acpassword ) {
        Login login = null;
        Account ac = StorageUtil.getAccountByCredentials(getEntityManager(),acemail, acpassword);
        if ( ac != null ) {
            login = new Login("OK", ac.getId());
            super.create(login);
        } else {
            login = new Login();
            login.setId(null);
            login.setToken(null);
            login.setResponse("Error: login failed, username or password wrong");
        }
        return login;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }    
}
