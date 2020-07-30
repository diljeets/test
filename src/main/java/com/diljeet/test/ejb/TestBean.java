/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.ejb;

import com.diljeet.test.entity.Family;
import com.diljeet.test.resources.TestService;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
//import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author diljeet
 */
@Named
//@SessionScoped
@Stateless
public class TestBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger logger = Logger.getLogger(TestBean.class.getCanonicalName());

    //private static final long SerialVersionUID = 42L;
//    @Inject
//    SessionContext ctx;
    @Inject
    HttpServletRequest req;

//    @Inject
//    HttpSession sess;
//    
//    @Inject
//    ServletContext ctx;
    private Client client;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    private void clean() {
        client.close();
    }

//    public String createFamily(Family family) {
//        logger.info("Entered into testBean.createFamily method1");
//        if (family == null) {
//            logger.info("Family is null");
//            return "index";
//        }
//
//        String navigation = null;
//        logger.log(Level.INFO, "Object is " + family.getName());
//        Response response = client.target("http://localhost:9090/test/webapi/Family")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);
//
//        if (response.getStatus() == Status.CREATED.getStatusCode()) {
//            logger.info("data inserted into table");
//            navigation = "index";
//        } else {
//            navigation = "index";
//        }
//        return navigation;
//
//    }
    public void createFamily(Family family) {
        // logger.info("Entered into testBean.createFamily method1");

        if (family == null) {
            logger.info("Family is null");
            //return "/index.xhtml?faces-redirect=true";
        }

        Map<String, String> params
                = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("id");

        String navigation = null;
        //Long id = family.getId();
        //logger.log(Level.INFO, "Object is " + id + family.getName() + family.getFathersName() + family.getMothersName());

        if (id == null) {
            //logger.info("Entered into null condition");
            Response response = client.target("http://localhost:8080/test/webapi/Family")
                    .request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)                    
                    .header("Cookie", req.getHeader("Cookie"))
                    .post(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);

            if (response.getStatus() == Status.CREATED.getStatusCode()) {
                logger.info("data inserted into table");
                //navigation = "index";
            } else {
                //navigation = "index";
            }
        } else {
            try {
                Response response = client.target("http://localhost:8080/test/webapi/Family")
                        .path(id)
                        .request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
                        .header("Cookie", req.getHeader("Cookie"))
                        .put(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);

                if (response.getStatus() == Status.CREATED.getStatusCode()) {
                    logger.info("data updated into table");
                    //navigation = "index";
                } else {
                    //navigation = "index";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //return "/index.xhtml?faces-redirect=true";

    }

//    public List<Family> getAllFamily() {
//        logger.info("testbean");
//        List<Family> family = null;
//        try {
//            family = client.target("http://localhost:8080/test/webapi/Family")
//                .path("all")
//                .request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
//                .header("Authorization", req.getHeader("Authorization"))
//                .get(new GenericType<List<Family>>() {
//                });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.toString());
//        }
//        
//        return family;
//
//    }
    
    public List<Family> getAllFamily(){
//         Principal principal = ctx.getCallerPrincipal();
//        String name = principal.getName();
//        logger.log(Level.SEVERE, "Caller name is: {0}", name);

//         Enumeration<String> headerNames =  req.getHeaderNames();
//         while(headerNames.hasMoreElements())
//             logger.log(Level.SEVERE, headerNames.nextElement()); 
//         
//        logger.log(Level.SEVERE, "TestBean {0} ", req.getHeader("Cookie"));    
        
        List<Family> family = null;
        Response response = null;
        try {            
            response = client.target("http://localhost:8080/test/webapi/Family")
                    .path("all")
                    .request(MediaType.APPLICATION_JSON)  
                    .header("Cookie", req.getHeader("Cookie"))
                    .accept(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Status.OK.getStatusCode()) { 
//                MultivaluedMap<String,Object> headers = response.getHeaders();
//                Set<String> keysets = headers.keySet();
//                String stringKeysets = keysets.toString();
//                String headerValue = response.getHeaderString("Content-Type");
//                logger.log(Level.SEVERE, "Response is ok {0}", stringKeysets);
//                logger.log(Level.SEVERE, "Response is ok {0} ", headerValue);
                family = response.readEntity(new GenericType<List<Family>>() {
                });                
            } else {
                logger.log(Level.SEVERE, "Problem fetching results");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Response is {0}", e.getMessage());
        }

        return family;
    }

    public Family getFamilyById(String id) {

        Family family
                = client.target("http://localhost:8080/test/webapi/Family")
                        .path(id)
                        .request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
                        .header("Cookie", req.getHeader("Cookie"))
                        .get(Family.class);

        return family;
    }

    public void deleteFamilyById(String id) {
        client.target("http://localhost:8080/test/webapi/Family")
                .path(id)
                .request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
                .header("Cookie", req.getHeader("Cookie"))
                .delete(Family.class);
    }
}
