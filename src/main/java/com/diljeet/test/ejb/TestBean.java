/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.ejb;

import com.diljeet.test.entity.Family;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author diljeet
 */
@Named
@Stateless
public class TestBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger logger = Logger.getLogger(TestBean.class.getCanonicalName());

    private Client client;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    private void clean() {
        client.close();
    }

//    public String createFamily(Family family) {
//        logger.info("Entered into testBean.createFamily method1");
//        if (family == null) {
//            logger.info("Family is null");
//            return "index";
//        }
//
//        String navigation = null;
//        logger.log(Level.INFO, "Object is " + family.getName());
//        Response response = client.target("http://localhost:9090/test/webapi/Family")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);
//
//        if (response.getStatus() == Status.CREATED.getStatusCode()) {
//            logger.info("data inserted into table");
//            navigation = "index";
//        } else {
//            navigation = "index";
//        }
//        return navigation;
//
//    }
    public String createFamily(Family family) {
        logger.info("Entered into testBean.createFamily method1");
        
        if (family == null) {
            logger.info("Family is null");
            return "index";
        }

        Map<String,String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("id");
        
        String navigation = null;
        //Long id = family.getId();
        logger.log(Level.INFO, "Object is " + id + family.getName() + family.getFathersName() + family.getMothersName());
        

        if (id == null) {
            Response response = client.target("http://localhost:9090/test/webapi/Family")
                    .request(MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON)
                    .post(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);

            if (response.getStatus() == Status.CREATED.getStatusCode()) {
                logger.info("data inserted into table");
                navigation = "index";
            } else {
                navigation = "index";
            }
        } else {
            
            Response response = client.target("http://localhost:9090/test/webapi/Family")
                    .path(id)
                    .request(MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON)
                    .put(Entity.entity(family, MediaType.APPLICATION_JSON), Response.class);
            
            if (response.getStatus() == Status.CREATED.getStatusCode()) {
                logger.info("data updated into table");
                navigation = "index";
            } else {
                navigation = "index";
            }

        }

        return navigation;

    }

    public List<Family> getALlFamily() {

        List<Family> family
                = client.target("http://localhost:9090/test/webapi/Family")
                        .path("all")
                        .request(MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<Family>>() {
                        });

        return family;
    }

    public Family getFamilyById(String id) {

        Family family
                = client.target("http://localhost:9090/test/webapi/Family")
                        .path(id)
                        .request(MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON)
                        .get(Family.class);

        return family;
    }

    public void deleteFamilyById(String id) {
        client.target("http://localhost:9090/test/webapi/Family")
                .path(id)
                .request(MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON)
                .delete(Family.class);
    }
}
