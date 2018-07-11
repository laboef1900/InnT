/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Employee;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Login;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Stamp;
import java.sql.Timestamp;
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
@Path("stamp")
public class StampFacadeREST extends AbstractFacade<Stamp> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public StampFacadeREST() {
        super(Stamp.class);
    }

    @GET
    @Path("in")
    @Produces({MediaType.APPLICATION_JSON})
    public Stamp stampIn(@QueryParam("customerId") String customerId, @QueryParam("token") String token) {
        Stamp st;
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login == null) {
            st = new Stamp();
            st.setId(null);
            st.setStartTime(null);
            st.setResponse("Error: permission denied");
        } else {
            st = new Stamp();
            st.setCustomer(StorageUtil.findCustomerById(getEntityManager(), customerId));
            Employee employee = StorageUtil.getEmployeeByLogin(getEntityManager(), login);

            if (employee == null) {
                return st;
            }

            if (StorageUtil.getOpenStamp(getEntityManager(), employee) != null) {
                st = new Stamp();
                st.setId(null);
                st.setStartTime(null);
                st.setResponse("Error: you can have only one open stamp, please close your open stamp before adding a new one");
                return st;
            }

            st.setEmployee(employee);
            super.create(st);
            Stamp st1 = new Stamp();
            st1.setId(st.getId());
            st1.setResponse("OK");
            return st1;
        }
        return st;
    }

    @GET
    @Path("out")
    @Produces({MediaType.APPLICATION_JSON})
    public Stamp stampOut(@QueryParam("token") String token) {
        Stamp st = new Stamp();
        st.setId(null);
        st.setStartTime(null);

        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login == null) {
            st.setResponse("Error: permission denied");
        } else {
            Employee employee = StorageUtil.getEmployeeByLogin(getEntityManager(), login);

            if (employee == null) {
                return st;
            }
            Stamp openStamp = StorageUtil.getOpenStamp(getEntityManager(), employee);
            if (openStamp == null) {
                st.setResponse("Error: you have no stamp");
                return st;
            }

            openStamp.setEndTime(new Timestamp(System.currentTimeMillis()));
            super.edit(openStamp);
            Stamp st1 = new Stamp();
            st1.setStartTime(null);
            st1.setId(null);
            st1.setResponse("OK");
            return st1;
        }
        return st;
    }

    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Stamp> myStamp(@QueryParam("token") String token) {
        List<Stamp> stList = new ArrayList<>();

        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login == null) {
            return stList;
        } else {
            Employee employee = StorageUtil.getEmployeeByLogin(getEntityManager(), login);

            if (employee == null) {
                return stList;
            }
            stList = StorageUtil.getStamps(getEntityManager(), employee);
            if (stList == null) {
                stList = new ArrayList<>();
            }

        }
        return stList;
    }

    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Stamp> getAll(@QueryParam("token") String token) {
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        List<Stamp> stList = new ArrayList();
        return stList;
    }

    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_JSON})
    public Stamp find(@QueryParam("id") String id, @QueryParam("token") String token) {
        if (StorageUtil.isAdminOrCustomer(getEntityManager(), token)) {
            return super.find(id);
        }
        Stamp st = new Stamp();
        st.setId(null);
        st.setResponse("Error: persmison denied");
        return st;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
