/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.resources;

import com.diljeet.test.entity.Address;
import com.diljeet.test.entity.Family;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Stateless
@Path("/Family")
public class TestService {
    
    private static final Logger logger = Logger.getLogger(TestService.class.getCanonicalName());
    
    @PersistenceContext(name="my-persistence-unit")
    private EntityManager em;
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})    
    public List<Family> getAllFamily(){
        List<Family> family = null;
        try{       
            family = em.createNamedQuery("getAllFamilyMembers").getResultList();
        } catch (Exception e){
            logger.log(Level.INFO, e.toString());
        }
       
        
        return family;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Family getFamilyById(@PathParam("id") Long id){
        Family family = null;
        try{
            family = em.find(Family.class, id);
        } catch(Exception e){
            logger.log(Level.INFO, e.toString());
        }   
        return family;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response createFamily(Family family){ 
       
        logger.log(Level.INFO, "Object is " + family.getName());
        logger.log(Level.INFO, "Object is " + family.getFathersName());
        
        long familyId = 0;
        try{
            em.persist(family);
            familyId = family.getId();
            
        } catch(Exception e){
            System.out.println(e.toString());
        }
        
        return Response.created(URI.create("/" + familyId)).build();
        
        
    }
    
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public void deleteFamilyById(@PathParam("id") Long id){
        Family family = null;
        try{
            family = em.find(Family.class, id);
            em.remove(family);
        } catch(Exception e){
            logger.log(Level.INFO, e.toString());
        }           
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response updateFamilyById(@PathParam("id") Long id , Family family){
        logger.log(Level.INFO, "Enterd into update method" + id);
        Family oldFamily = null;
        try{
            oldFamily = em.find(Family.class, id);
            logger.log(Level.INFO, "Got record");
            if(oldFamily == null){
                logger.log(Level.INFO, "Family not found");
            } else{
                
                //em.merge(family);
                oldFamily.setName(family.getName());
                oldFamily.setFathersName(family.getFathersName());
                oldFamily.setMothersName(family.getMothersName());
                oldFamily.setAddress(family.getAddress());    
                
//                Query query = em.createNamedQuery("updateFamilyMembers");
//                query.setParameter("id", id);
//                query.setParameter("name", family.getName());
//                query.setParameter("fathersName", family.getFathersName());
//                query.setParameter("mothersName", family.getMothersName());
//                query.setParameter("address", family.getAddress());
//                query.executeUpdate();
                
                logger.log(Level.INFO, "Updated record");
                
                return Response.ok().status(303).build(); //return a seeOther code
            }
            
        } catch(Exception e){
            logger.log(Level.INFO, "Encountered error");
            logger.log(Level.INFO, e.toString());
        }    
        
        return Response.ok().status(303).build(); //return a seeOther code
        
    }
    
}
