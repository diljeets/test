/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.web;

import com.diljeet.test.ejb.TestBean;
import com.diljeet.test.entity.Family;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author diljeet
 */
@Named(value = "testManager")
@RequestScoped
public class TestManager implements Serializable{

    private static final Logger logger = Logger.getLogger(TestManager.class.getCanonicalName());
    
    private static final long SerialVersionUID = 42L;    
    
    private Family family;    
    private List<Family> families;
    
    @EJB
    private TestBean testBean;
    
    @PostConstruct
    private void init(){
        family = new Family();
        //setFamilies(testBean.getALlFamily());
    }
    
    /**
     * Creates a new instance of TestManager
     */
    public TestManager() {
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public List<Family> getFamilies() {
        return testBean.getALlFamily();        
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }  
    
    public void fetchFamilyById(String id){
        //logger.log(Level.INFO, "ID is " + id);
        this.family = testBean.getFamilyById(id);
//        logger.log(Level.INFO, "ID is " + this.family.getId());
//        logger.log(Level.INFO, "Name is " + this.family.getName());
//        logger.log(Level.INFO, "Relation is " + this.family.getRelationship());
    }
    
    public void deleteFamilyById(String id){
        //logger.log(Level.INFO, "ID is " + id);
        testBean.deleteFamilyById(id);
    }
    
}
