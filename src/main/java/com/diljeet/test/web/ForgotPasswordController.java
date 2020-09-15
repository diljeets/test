/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.web;

import com.diljeet.test.ejb.TestUsersBean;
import com.diljeet.test.entity.TestUsers;
import com.diljeet.test.services.TestUsersServiceBean;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.inject.Inject;

/**
 *
 * @author diljeet
 */
@Named(value = "forgotPasswordController")
@SessionScoped
public class ForgotPasswordController implements Serializable {

    private static final Logger logger = Logger.getLogger(ForgotPasswordController.class.getCanonicalName());

    private static final long serialVersionUID = 1L;

    private TestUsers user;

//    private UIComponent createBtn;
    @EJB
    private TestUsersBean testUsersBean;

    @PostConstruct
    public void init() {
        user = new TestUsers();
    }

    /**
     * Creates a new instance of TestUsersController
     */
    public ForgotPasswordController() {
    }

    public TestUsers getUser() {
        return user;
    }

    public void setUser(TestUsers user) {
        this.user = user;
    }

//    public UIComponent getCreateBtn() {
//        return createBtn;
//    }
//
//    public void setCreateBtn(UIComponent createBtn) {
//        this.createBtn = createBtn;
//    }
    public void clearFields() {
        user.setUsername(null);
//        user.setPassword(null);
//        user.setRetypePassword(null);
//        user.setRole(null);
//        user.setSalt(null);
    }

    public void forgotPassword(String username) {
        testUsersBean.forgotPassword(username);
        clearFields();
    }

}
