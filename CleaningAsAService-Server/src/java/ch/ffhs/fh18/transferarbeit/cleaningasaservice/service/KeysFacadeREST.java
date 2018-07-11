/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Account;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.AccountRole;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Customer;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.MyKey;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
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
@Path("keys")
public class KeysFacadeREST extends AbstractFacade<MyKey> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public KeysFacadeREST() {
        super(MyKey.class);
    }

    @GET
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public MyKey register(@QueryParam("keyownerId") String keyownerId, @QueryParam("location") String location, @QueryParam("token") String token) {
        MyKey key = new MyKey();
        Account ac = StorageUtil.getAccountByToken(getEntityManager(), token);

        if (ac != null) {
            Customer c;

            if (ac.getAccountRole() == AccountRole.ADMIN) {
                c = StorageUtil.getCustomerByAccountId(getEntityManager(), keyownerId);

            } else {
                c = StorageUtil.getCustomerByAccount(getEntityManager(), ac);

            }
            key.setLocation(location);
            key.setCustomer(c);
            getEntityManager().persist(key);
            key.setResponse("OK");

        } else {
            key.setId(null);
            key.setResponse("Error: permission denied to register key");
        }
        return key;
    }

    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MyKey> getAll(@QueryParam("token") String token) {
        List<MyKey> l = new ArrayList<>();
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        return l;
    }

    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MyKey> myKeys(@QueryParam("token") String token) {
        List<MyKey> wList = new ArrayList<>();

        Customer customer = StorageUtil.getCustomerByToken(getEntityManager(), token);

        if (customer == null) {
            return wList;
        }
        wList = StorageUtil.getKeys(getEntityManager(), customer);
        if (wList == null) {
            wList = new ArrayList<>();
        }

        return wList;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
