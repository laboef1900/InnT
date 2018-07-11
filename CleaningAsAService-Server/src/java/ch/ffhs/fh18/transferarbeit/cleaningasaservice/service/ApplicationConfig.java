/*
 * Fernfachhochschule Schweiz  
 * Transferarbeit Innovationen & Technologien
 * Cleaning as a Service
 * 2018
 */
package ch.ffhs.fh18.transferarbeit.cleaningasaservice.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Djan Sarwari
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.AccountFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.AddressFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.CustomerFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.EmployeeFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.KeysFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.LoginFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.LogoutFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.OrderFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.PersonFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.StampFacadeREST.class);
        resources.add(ch.ffhs.fh18.transferarbeit.cleaningasaservice.service.WorkFacadeREST.class);        
        
    }
    
}
