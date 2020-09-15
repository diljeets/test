/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.exceptionmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author diljeet
 */
public class GeneralExceptionMapper implements ExceptionMapper<Exception>{

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response.serverError().build();
    }
    
}
