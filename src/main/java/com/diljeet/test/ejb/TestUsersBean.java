/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.ejb;

import com.diljeet.test.customexceptions.ChangePasswordRequestAcceptedException;
import com.diljeet.test.customexceptions.ErrorCreatingUserException;
import com.diljeet.test.entity.TestUsers;
import com.diljeet.test.customexceptions.NewUserCreatedException;
import com.diljeet.test.customexceptions.PasswordsDontMatchException;
import com.diljeet.test.customexceptions.UserAccountDoesNotExistException;
import com.diljeet.test.utils.TestUtils;
import com.diljeet.test.web.TestUsersController;
import java.security.KeyStore;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private FacesMessage msg;

    @Inject
    TestUsersController testUsersController;

//    @Inject
//    HttpServletRequest req;
//
//    @
//    HttpServletResponse res;    
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();           
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }       

    public void createUser(TestUsers user) {
        if (user == null) {
            return;
        }
//        boolean isValid = false;
        try {
//            try {
//                InternetAddress email = new InternetAddress(user.getUsername());
//                email.validate();
//                isValid = true;
//            } catch (AddressException e) {
//                throw new AddressException("Not valid");
//            }
//            if (isValid) {
                Response response = client.target("http://localhost:8080/test/webapi/TestUsers")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
                if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                    throw new NewUserCreatedException("User created successfully. Activation link has been sent to registerd email id. Kindly click on the link to activate your account.");

                } else if (response.getStatus() == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
                    throw new PasswordsDontMatchException("Error creating User. Password and Retype Password don't match.");
                } else if (response.getStatus() == Response.Status.FOUND.getStatusCode()) {
                    throw new SQLIntegrityConstraintViolationException("Error creating User. User already exists.");
                } else {
                    throw new ErrorCreatingUserException("Error creating User");
                }
//            } //else {
//                throw new AddressException("Not a valid Email Address");
//            }
        } catch (NewUserCreatedException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getCreateBtn().getClientId(), msg);
        } catch (ErrorCreatingUserException | PasswordsDontMatchException | SQLIntegrityConstraintViolationException e) {
//            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getCreateBtn().getClientId(), msg);
        } catch (Exception e) {
//            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getCreateBtn().getClientId(), msg);
        }

    }

    public List<TestUsers> getUser() {
        List<TestUsers> users = null;
        users = client.target("http://localhost:8080/test/webapi/TestUsers")
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<TestUsers>>() {
                });

        return users;
    }

    public void forgotPassword(String username) {     
        try {
            Response response = client.target("http://localhost:8080/test/webapi/TestUsers/retrieve-password/")
                    .path(username)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/test/login.xhtml?sentPassword=true");
//                res.sendRedirect(req.getContextPath() + "/login.xhtml?sentPassword=true");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()
                    || response.getStatus() == Response.Status.NOT_MODIFIED.getStatusCode()) {                
                throw new UserAccountDoesNotExistException("Account/User either is inactive or does not exist.");
            }
        } catch (UserAccountDoesNotExistException e) {            
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getResetPasswordBtn().getClientId(), msg);            
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getResetPasswordBtn().getClientId(), msg);
        }

    }
    
    public void changePassword(TestUsers user) {   
        try {
            Response response = client.target("http://localhost:8080/test/webapi/TestUsers/")
                    .path(user.getUsername())                 
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON),Response.class);
            if (response.getStatus() == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
                throw new PasswordsDontMatchException("Password and Retype Password don't match.");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new UserAccountDoesNotExistException("Account/User either is inactive or does not exist.");
            } else if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                throw new ChangePasswordRequestAcceptedException("For Security reasons we have sent you a mail asking for your consent to go ahead with Password change request. Kindly click on the link to proceed.");
            }
        } catch (ChangePasswordRequestAcceptedException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getChangePasswordBtn().getClientId(), msg);
        } catch (UserAccountDoesNotExistException | PasswordsDontMatchException e) {
//            testUsersController.setIsProgressBar(false);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getChangePasswordBtn().getClientId(), msg);
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(testUsersController.getChangePasswordBtn().getClientId(), msg);
        }

    }

}
