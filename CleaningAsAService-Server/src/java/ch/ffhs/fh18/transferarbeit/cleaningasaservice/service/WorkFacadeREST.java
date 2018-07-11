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
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Status;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Work;
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
@Path("work")
public class WorkFacadeREST extends AbstractFacade<Work> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public WorkFacadeREST() {
        super(Work.class);
    }

    @GET
    @Path("assign")
    @Produces({MediaType.APPLICATION_JSON})
    public Work assign(@QueryParam("assigneeId") String assigneeId, @QueryParam("orderId") String orderId, @QueryParam("token") String token) {
        Work w = new Work();
        if (!StorageUtil.isAdmin(getEntityManager(), token)) {
            w.setId(null);
            w.setResponse("Error: permission denied to assign work");
        } else {
            w.setEmployee(StorageUtil.findEmployeeById(getEntityManager(), assigneeId));
            w.setOrder(StorageUtil.findOrderById(getEntityManager(), orderId));
            w.setStatus(Status.OPEN);
            super.create(w);
        }
        return w;
    }

    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Work> myWorks(@QueryParam("token") String token) {
        List<Work> wList = new ArrayList<>();

        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login != null) {

            Employee employee = StorageUtil.getEmployeeByLogin(getEntityManager(), login);

            if (employee == null) {
                return wList;
            }
            wList = StorageUtil.getWorks(getEntityManager(), employee);
            if (wList == null) {
                wList = new ArrayList<>();
            }
        }
        return wList;
    }

    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Work> getAll(@QueryParam("token") String token) {
        List<Work> l = new ArrayList<>();
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
