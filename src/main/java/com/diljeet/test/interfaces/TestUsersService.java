/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.interfaces;

import com.diljeet.test.customexceptions.ErrorCreatingUserException;
import com.diljeet.test.customexceptions.PasswordsDontMatchException;
import com.diljeet.test.entity.TestUsers;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Path("/TestUsers")
public interface TestUsersService {

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(TestUsers user) throws NoSuchAlgorithmException;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestUsers> getUser();
    
    @POST
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getUserByUsername(@PathParam("username") String username , TestUsers user);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/retrieve-password/{username}")
    public Response forgotPassword(@PathParam("username") String username);
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/change-password/{username}/{password}")
    public void changePassword(@PathParam("username") String encodedUsername, 
            @PathParam("password") String encodedPassword,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/activate-account/{username}")
    public void activateAccount(@PathParam("username") String username,
            @Context HttpServletRequest req,
            @Context HttpServletResponse res);

}
