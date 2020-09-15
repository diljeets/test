/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.bootstrap;

import com.diljeet.test.services.TestUsersServiceBean;
import com.diljeet.test.services.TestService;
import com.diljeet.test.exceptionmapper.EJBAccessExceptionMapper;
import com.diljeet.test.exceptionmapper.GeneralExceptionMapper;
import com.diljeet.test.ejb.TestBean;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author diljeet
 */
@ApplicationPath("/webapi")
public class TestApplication extends Application{

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        // register root resource        
        classes.add(TestService.class);
        classes.add(TestUsersServiceBean.class);
        classes.add(EJBAccessExceptionMapper.class);
        classes.add(GeneralExceptionMapper.class);
        return classes;
    }
    
}
