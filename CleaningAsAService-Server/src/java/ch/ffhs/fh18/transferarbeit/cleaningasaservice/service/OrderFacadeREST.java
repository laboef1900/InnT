/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Customer;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.MyOrder;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.PaymentStatus;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Status;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Djan Sarwari
 */
@Stateless
@Path("order")
public class OrderFacadeREST extends AbstractFacade<MyOrder> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public OrderFacadeREST() {
        super(MyOrder.class);
    }
    
    @GET
    @Path("register")
    @Produces({MediaType.APPLICATION_JSON})
    public MyOrder register(@QueryParam("token") String token, @QueryParam("hours") int hours, @QueryParam("description") String description) {
        MyOrder o = new MyOrder();
        o.setId(null);
        o.setResponse("Error: permision denied");
        Customer c = StorageUtil.getCustomerByToken(getEntityManager(), token);
        if (c != null) {
            MyOrder newOrder = new MyOrder();
            newOrder.setHours(hours);
            newOrder.setDescription(description);
            newOrder.setCustomer(c);
            super.create(newOrder);
            o.setId(newOrder.getId());
            o.setResponse("OK");
        }
        return o;
    }
    
    @GET
    @Path("registerFor")
    @Produces({MediaType.APPLICATION_JSON})
    public MyOrder registerFor(@QueryParam("token") String token,@QueryParam("customerId") String customerId, @QueryParam("hours") int hours, @QueryParam("description") String description) {
        MyOrder o = new MyOrder();
        o.setId(null);
        o.setResponse("Error: permision denied");
        
        if (!StorageUtil.isAdmin(getEntityManager(), token)) {
            return o;
        }
        
        Customer c = StorageUtil.findCustomerById(getEntityManager(), customerId);
        if (c != null) {
            MyOrder newOrder = new MyOrder();
            newOrder.setHours(hours);
            newOrder.setDescription(description);
            newOrder.setCustomer(c);
            super.create(newOrder);
            o.setId(newOrder.getId());
            o.setResponse("OK");
        } else {
            o.setResponse("Error: customer not found");
        }
        return o;
    }
    
    @GET
    @Path("status")
    @Produces({MediaType.APPLICATION_JSON})
    public MyOrder updateStatus(@QueryParam("token") String token,@QueryParam("orderId") String orderId, @QueryParam("status") String status) {
        MyOrder o = new MyOrder();
        boolean success = false;
        if (StorageUtil.isAdmin(getEntityManager(), token) && orderId != null && !"".equalsIgnoreCase(orderId) 
                && status != null && !"".equalsIgnoreCase(status)) {
            MyOrder targetO = super.find(orderId);
            if (targetO == null) {
                o.setResponse("Error: the order could not be found");
                return o;
            }
            switch ( status.toUpperCase() ){
                case "OPEN":
                    targetO.setStatus(Status.OPEN);  
                    success = true;
                    break;
                case "DONE":
                     targetO.setStatus(Status.DONE);
                     success = true;
                    break;
                case "INPROGRESS":
                     targetO.setStatus(Status.INPROGRESS);
                     success = true;
                    break;
                case "CANCELED":
                     targetO.setStatus(Status.CANCELED);
                     success = true;
                    break;
            }            
            if ( success ) {
                o.setResponse("OK");
                o.setId(targetO.getId());
                o.setStatus(targetO.getStatus());
                o.setDateOrdered(targetO.getDateOrdered());
                o.setHours(targetO.getHours());
                o.setDescription(targetO.getDescription());
                o.setPaymentStatus(targetO.getPaymentStatus());
                super.edit(targetO);
            }
        }
        
        return o;
    }

    @GET
    @Path("payment")
    @Produces({MediaType.APPLICATION_JSON})
    public MyOrder updatePaymet(@QueryParam("token") String token,@QueryParam("orderId") String orderId, @QueryParam("paymentStatus") String paymentStatus) {
        MyOrder o = new MyOrder();
        boolean success = false;
        if (StorageUtil.isAdmin(getEntityManager(), token) && orderId != null && paymentStatus != null 
                && !"".equalsIgnoreCase(orderId) && !"".equalsIgnoreCase(paymentStatus)) {
            MyOrder targetO = super.find(orderId);
            if (targetO == null) {
                o.setResponse("Error: the order could not be found");
                return o;
            }
            switch ( paymentStatus.toUpperCase() ){
                case "OPEN":
                    targetO.setPaymentStatus(PaymentStatus.OPEN);
                    success = true;
                    break;
                case "PAID":
                     targetO.setPaymentStatus(PaymentStatus.PAID);
                     success = true;
                    break;
                case "LATE":
                     targetO.setPaymentStatus(PaymentStatus.LATE);
                     success = true;
                    break;                
            }            
            if ( success ) {
                o.setResponse("OK");
                o.setId(targetO.getId());
                o.setStatus(targetO.getStatus());
                o.setDateOrdered(targetO.getDateOrdered());
                o.setHours(targetO.getHours());
                o.setDescription(targetO.getDescription());
                o.setPaymentStatus(targetO.getPaymentStatus());
                super.edit(targetO);
            }
        }        
        return o;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public MyOrder find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Path("my")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MyOrder> myOrders(@QueryParam("token") String token) {
        Customer customer = StorageUtil.getCustomerByToken(getEntityManager(), token);
        if (customer != null) {
            return StorageUtil.getMyOrders(getEntityManager(), customer);
        }
        List<MyOrder> l = new ArrayList<>();
        return l;
    }
    
    @GET
    @Path("getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<MyOrder> getAll(@QueryParam("token") String token) {
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        List<MyOrder> l = new ArrayList<>();
        return l;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
