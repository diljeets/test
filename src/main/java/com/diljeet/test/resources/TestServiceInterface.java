/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.resources;

import com.diljeet.test.entity.Family;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Path("/Family")
public interface TestServiceInterface {
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})    
    public Response getAllFamily();
//    public List<Family> getAllFamily();
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})    
    public Family getFamilyById(@PathParam("id") Long id);
    
    @POST
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response createFamily(Family family);
    
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public void deleteFamilyById(@PathParam("id") Long id);
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public void updateFamilyById(@PathParam("id") Long id , Family family);
    
}
