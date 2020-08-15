/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.ejb;

import com.diljeet.test.entity.TestUsers;
import com.diljeet.test.resources.NewUserCreatedException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class TestUsersBean {

    private static final Logger logger = Logger.getLogger(TestUsersBean.class.getCanonicalName());
   
//    @PersistenceContext(name = "my-persistence-unit")
//    private EntityManager em;
//    
//    public void createUser(TestUsers user) {
//        if(user == null){
//            return;
//        }
////        logger.log(Level.SEVERE, user.getUsername());
////        logger.log(Level.SEVERE, user.getPassword());
////        logger.log(Level.SEVERE, user.getRole());
//        try {
//            if(user.getRole() == null)
//                user.setRole("user");
//            em.persist(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public List<TestUsers> getUser() {
//        logger.log(Level.SEVERE, "Coming from service");
//        List<TestUsers> users = null;
//        try {
//            users = em.createNamedQuery("getAllUsers").getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return users;
//    }
    
    private Client client;
    
    @Inject
    HttpServletRequest req;
    
    @PostConstruct
    public void init(){
        client = ClientBuilder.newClient();
    }
    
    @PreDestroy
    public void destroy(){
        client.close();
    }
    
    public void createUser(TestUsers user) throws NewUserCreatedException{
        if(user == null){
            return;
        }
        try {
            Response response = client.target("http://localhost:8080/test/webapi/TestUsers")
                .request(MediaType.APPLICATION_JSON)                
                .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {                
                throw new NewUserCreatedException("User created successfully. Kindly login again");
            } else {
                logger.log(Level.SEVERE,"Error inserting data {0} ", response.getStatus());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());            
        }
        
    }
    
    public List<TestUsers> getUser(){
        List<TestUsers> users = null;
        users = client.target("http://localhost:8080/test/webapi/TestUsers")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<TestUsers>>(){});
        
        return users;                
    }
}
