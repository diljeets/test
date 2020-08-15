/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.resources;

import com.diljeet.test.entity.Address;
import com.diljeet.test.entity.Family;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java.net.URI;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.AbstractDocument;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import org.jboss.ejb3.annotation.RunAsPrincipal;
//import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author diljeet
 */
@Stateless
//@Path("/Family")
//@SecurityDomain("testApplicationDomain")
@RolesAllowed("Administrator")
public class TestService implements TestServiceInterface {

    private static final Logger logger = Logger.getLogger(TestService.class.getCanonicalName());

    //private static final long SerialVersionUID = 42L;
//    @Resource
//    SessionContext ctx;
    @Inject
    HttpServletRequest req;

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

//    @Override
//    @RolesAllowed({"Administrator","Monitor"}) 
//    public List<Family> getAllFamily(){   
//        logger.info("testservice");
//        List<Family> family = null;
//        try{       
//            family = em.createNamedQuery("getAllFamilyMembers").getResultList();
//        } catch (Exception e){
//            logger.log(Level.INFO, e.toString());
//        }          
//        return family;
//    }
    @Override
    @RolesAllowed({"Administrator","Monitor"})
    public Response getAllFamily(){
//        Principal principal = ctx.getCallerPrincipal();
//        String name = principal.getName();
//        logger.log(Level.SEVERE, "Caller name is: {0}", name); 
        //throw new WebApplicationException(); 
//        Enumeration<String> headerNames =  req.getHeaderNames();
//         while(headerNames.hasMoreElements())
//             logger.log(Level.SEVERE, headerNames.nextElement()); 
//         
//        logger.log(Level.SEVERE, req.getHeader("Authorization"));  
        List<Family> family = null;
        GenericEntity<List<Family>> genericFamilyList = null;
        try {            
            family = em.createNamedQuery("getAllFamilyMembers").getResultList();
            genericFamilyList = new GenericEntity<List<Family>>(family) {
            };            
          } catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());              
        }
        //return Response.ok(genericFamilyList, req.getHeader("Accept")).build();
        //return Response.ok().entity(genericFamilyList).header(HttpHeaders.CONTENT_TYPE, req.getHeader("Accept")).build();
        return Response.ok().entity(genericFamilyList).build();
    }

    @Override
    public Family getFamilyById(Long id) {
        Family family = null;
        try {
            family = em.find(Family.class, id);
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
        }
        return family;
    }

    @Override
    public Response createFamily(Family family) {

//        logger.log(Level.INFO, "Object is " + family.getName());
//        logger.log(Level.INFO, "Object is " + family.getFathersName());
        long familyId = 0;
        try {
            em.persist(family);
            //familyId = family.getId();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return Response.created(URI.create("/" + familyId)).build();

    }

    @Override
    public void deleteFamilyById(Long id) {
        Family family = null;
        try {
            family = em.find(Family.class, id);
            em.remove(family);
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    @Override
    public void updateFamilyById(Long id, Family family) {
        logger.log(Level.INFO, "Enterd into update method" + id);
        Family oldFamily = null;
        try {
            oldFamily = em.find(Family.class, id);
            logger.log(Level.INFO, "Got record");
            if (oldFamily == null) {
                logger.log(Level.INFO, "Family not found");
            } else {
                //logger.log(Level.SEVERE, "Pincode is {0}", family.getAddress().getPincode());

                //em.merge(family);
                oldFamily.setName(family.getName());
                oldFamily.setFathersName(family.getFathersName());
                oldFamily.setMothersName(family.getMothersName());
                oldFamily.setAddress(family.getAddress());
                em.merge(oldFamily);

//                Query query = em.createNamedQuery("updateFamilyMembers");
//                query.setParameter("id", id);
//                query.setParameter("name", family.getName());
//                query.setParameter("fathersName", family.getFathersName());
//                query.setParameter("mothersName", family.getMothersName());
//                query.setParameter("address", family.getAddress());
//                query.executeUpdate();
                logger.log(Level.INFO, "Updated record");

                //return Response.ok().status(303).build(); //return a seeOther code
            }

        } catch (Exception e) {
            logger.log(Level.INFO, "Encountered error");
            logger.log(Level.INFO, e.toString());
        }

        //return Response.ok().status(303).build(); //return a seeOther code
    }

}
