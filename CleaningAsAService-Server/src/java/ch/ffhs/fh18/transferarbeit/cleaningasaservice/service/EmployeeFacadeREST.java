/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Employee;
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
@Path("employee")
public class EmployeeFacadeREST extends AbstractFacade<Employee> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public EmployeeFacadeREST() {
        super(Employee.class);
    }

    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Employee> getAll(@QueryParam("token") String token) {
         List<Employee> l = new ArrayList<>();
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        return l;
    }
    
    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_JSON})
    public Employee find(@QueryParam("token") String token, @QueryParam("employeeId") String employeeId) {
        Employee c = new Employee();
        c.setId(null);
        c.setResponse("Error: permission denied");
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            Employee c1 = super.find(employeeId);
            if (c1 != null) {
                c1.setResponse("OK");  
                return c1;
            } else {
                c.setResponse("Error: customer not found");
            }
        }
        return c;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
