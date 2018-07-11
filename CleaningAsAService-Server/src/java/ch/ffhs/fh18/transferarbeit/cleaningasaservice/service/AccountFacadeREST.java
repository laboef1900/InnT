/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.StorageUtil;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Account;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.AccountRole;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.AccountType;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Customer;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Employee;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Login;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.model.Person;
import ch.ffhs.fh18.transferarbeit.cleaningasaservice.util.EncryptedPassword;
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
@Path("account")
public class AccountFacadeREST extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "CleaningAsAServicePU")
    private EntityManager em;

    public AccountFacadeREST() {
        super(Account.class);
    }

    @GET
    @Path("register")
    @Produces({MediaType.APPLICATION_JSON})
    public Account register(@QueryParam("acemail") String acemail,@QueryParam("acpassword") String acpassword, @QueryParam("actype") String actype ) {
        Account ac;
        if ( acemail == null || acemail.isEmpty() || acpassword == null || acpassword.length() < 4 || actype == null || actype.isEmpty() ) {
            ac = new Account();
            ac.setId(null);
            ac.setResponse("Error: Invalid input, please provide all requires data");
            return ac;
        }        
        if ( StorageUtil.isAvailable(getEntityManager(),acemail) ) {
            ac = new Account();
            ac.setEmail(acemail);
            ac.setPassword(EncryptedPassword.forRawPassword(acpassword).getEncrypted());
            ac.setType( AccountType.CUSTOMER.toString().equalsIgnoreCase(actype) ? AccountType.CUSTOMER : AccountType.EMPLOYEE);
            ac.setAccountRole(AccountType.CUSTOMER.toString().equalsIgnoreCase(actype) ? AccountRole.CUSTOMER : AccountRole.EMPLOYEE);
            super.create(ac);
            
            Person p = new Person();
            p.setAccount(ac);
            getEntityManager().persist(p);
            if (AccountType.CUSTOMER.toString().equalsIgnoreCase(actype)) {
                Customer c = new Customer();
                c.setPerson(p);
                getEntityManager().persist(c);
            } else{
                Employee e = new Employee();
                e.setPerson(p);
                getEntityManager().persist(e);
            }
        } else {
            ac = new Account();
            ac.setId(null);
            ac.setResponse("Error: the email is already registered");
        }
        return ac;
    }
    
    @GET
    @Path("addAdmin")
    @Produces({MediaType.APPLICATION_JSON})
    public Account addAdmin(@QueryParam("acpassword") String acpassword, @QueryParam("secret") String secret) {
        String acemail = "admin@cleaningasservice.ch";
        String lsecret = "57ACC7E7-387C-41C8-99A2-46E5DF37AC29";        
       
        Account ac;        
        if ( lsecret.equals(secret) && StorageUtil.isAvailable(getEntityManager(),acemail) ) {
            ac = new Account();
            ac.setEmail(acemail);
            ac.setPassword(EncryptedPassword.forRawPassword(acpassword).getEncrypted());
            ac.setType(AccountType.ADMIN);
            ac.setAccountRole(AccountRole.ADMIN);
            super.create(ac);
            
            Person p = new Person();
            p.setAccount(ac);
            getEntityManager().persist(p);         
            Customer c = new Customer();
            c.setPerson(p);
            getEntityManager().persist(c);
            ac.setResponse("OK");
        } else {
            ac = new Account();
            ac.setId(null);
            ac.setResponse("Error: permision denied to execute the operation");
        }
        return ac;
    }
    
    @GET
    @Path("my")
    @Produces({ MediaType.APPLICATION_JSON})
    public Account myAccount(@QueryParam("token") String token) {
        Login login = StorageUtil.getLoginByToken(getEntityManager(), token);
        if (login != null) {
            return super.find(login.getAccountId());
        }
        Account ac = new Account();
        ac.setId(null);
        ac.setResponse("Error: permission denied");
        return ac;
    }
    
    @GET
    @Path("getAll")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Account> getAll(@QueryParam("token") String token) {
        if (StorageUtil.isAdmin(getEntityManager(), token)) {
            return super.findAll();
        }
        List<Account> acList = new ArrayList();
        return acList;
    }
    
    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_JSON})
    public Account find(@QueryParam("id") String id, @QueryParam("token") String token) {
        if (StorageUtil.isAdminOrCustomer(getEntityManager(), token)) {
            return super.find(id);
        }
        Account ac = new Account();
        ac.setId(null);
        ac.setResponse("Error: persmison denied");
        return ac;
    }
    
    @GET
    @Path("remove")
    @Produces({MediaType.APPLICATION_JSON})
    public Account remove(@QueryParam("id") String id, @QueryParam("token") String token) {
        Account ac = new Account();
         ac.setId(null);
         ac.setResponse("Error: permision denied");
         if (StorageUtil.isAdmin(getEntityManager(), token)) {
             Account c = StorageUtil.getAccountById(em, id);
             if (c != null){
                ac.setId(c.getId());
                ac.setResponse("OK");
                super.remove(c);
             }
         }      
         return ac;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    } 
}
