/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
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
@Path("logout")
public class LogoutFacadeREST extends AbstractFacade<Login> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public LogoutFacadeREST() {
        super(Login.class);
    }

    @GET    
    @Produces({MediaType.APPLICATION_JSON})
    public Login logout(@QueryParam("token") String token ) {
        Login login = StorageUtil.getLoginByToken(getEntityManager(),token);
        if ( login != null ) {
            super.remove(login);
            login = new Login();
            login.setToken(null);
            login.setId(null);
            login.setAccountId(null);
            login.setResponse("OK");
        } else {
            login = new Login();
            login.setId(null);
            login.setToken(null);
            login.setResponse("Error: logout failed, wrong token");
        }
        return login;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }   
}
