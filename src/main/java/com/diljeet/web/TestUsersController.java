/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.web;

import com.diljeet.test.ejb.TestUsersBean;
import com.diljeet.test.entity.TestUsers;
import com.diljeet.test.resources.TestUsersServiceBean;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author diljeet
 */
@Named(value = "testUsersController")
@SessionScoped
public class TestUsersController implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private TestUsers user;
    
    @EJB
    private TestUsersBean testUsersBean;
    /**
     * Creates a new instance of TestUsersController
     */
    public TestUsersController() {
        user = new TestUsers();
    }   

    public TestUsers getUser() {
        return user;
    }

    public void setUser(TestUsers user) {
        this.user = user;
    }   
    
//    public void createUser(TestUsers user){
//        testUsersBean.createUser(user);
//    }
    
}
