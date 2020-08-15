/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.resources;

import com.diljeet.test.entity.TestUsers;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response createUser(TestUsers user) throws Exception;
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestUsers> getUser();
    
}
