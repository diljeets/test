/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.services;

import com.diljeet.test.customexceptions.PasswordsDontMatchException;
import com.diljeet.test.interfaces.TestUsersService;
import com.diljeet.test.ejb.TestUsersBean;
import com.diljeet.test.entity.TestUsers;
import com.diljeet.test.utils.TestUtils;
import static com.diljeet.test.utils.TestUtils.manipulateEncodedPassword;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 *
 * @author diljeet
 */
@Stateless
//@RolesAllowed("Administrator")
public class TestUsersServiceBean implements TestUsersService {

    private static final Logger logger = Logger.getLogger(TestUsersBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Resource(lookup = "java:jboss/mail/gmailSession")
    public Session mailSession;

    @Override
    public Response createUser(TestUsers user) throws NoSuchAlgorithmException {

        if (user == null) {
            return null;
        } else {
            try {
                Query query = em.createQuery("Select u FROM TestUsers u where u.username = :username");
                query.setParameter("username", user.getUsername());
                List<TestUsers> userExists = query.getResultList();

                if (!userExists.isEmpty()) {
                    return Response.status(Response.Status.FOUND).build();
                } else {
                    if (user.getPassword().equals(user.getRetypePassword())) {

                        byte[] salt = TestUtils.generateSalt();
                        byte[] password = user.getPassword().getBytes();

                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        md.update(salt);
                        md.update(password);
                        byte[] digest = md.digest();

                        Encoder encoder = Base64.getEncoder();

                        String encodedSalt = encoder.encodeToString(salt);
                        user.setSalt(encodedSalt);

                        String encodedPassword = encoder.encodeToString(digest);
                        user.setPassword(encodedPassword);

                        if (user.getRole() == null) {
                            user.setRole("Administrator");
                        }

                        if (user.getIsActive() == null) {
                            user.setIsActive("no");
                        }

                        em.persist(user);
                        TestUtils.sendActivationLinkOnMail(mailSession, user.getUsername());

                    } else {
                        return Response.status(Response.Status.PRECONDITION_FAILED).build();
                    }

                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }

            return Response.status(Response.Status.CREATED).build();

        }

    }

    @Override
    public List<TestUsers> getUser() {
        //logger.log(Level.SEVERE, "Coming from service");
        List<TestUsers> users = null;
        try {
            users = em.createNamedQuery("getAllUsers").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void activateAccount(String encodedUsername,
            HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] decodedUsername = decoder.decode(encodedUsername);
            String username = new String(decodedUsername);

            Query query = em.createQuery("Select u FROM TestUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            TestUsers existingUser = (TestUsers) accountExists;
            
            if (existingUser instanceof TestUsers) {
                if ((existingUser.getIsActive()).equals("no")) {
                    existingUser.setIsActive("yes");
                    res.sendRedirect(req.getContextPath() + "/login.xhtml?account=true");
                } else {
                    res.sendRedirect(req.getContextPath() + "/login.xhtml?isactive=true");
                }
            } else {
                logger.log(Level.SEVERE, "Account does not Exist");
                res.sendRedirect(req.getContextPath() + "/login.xhtml?account=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public Response forgotPassword(String username) {
        try {
            Query query = em.createQuery("Select u FROM TestUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            TestUsers existingUser = (TestUsers) accountExists;
            if (existingUser instanceof TestUsers) {
                if ((existingUser.getIsActive()).equals("yes")) {
                    String encodedSalt = existingUser.getSalt();
                    String password = TestUtils.generateForgotPassword();

                    Decoder decoder = Base64.getDecoder();

                    byte[] saltInBytes = decoder.decode(encodedSalt);
                    byte[] passwordInBytes = password.getBytes();
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    md.update(saltInBytes);
                    md.update(passwordInBytes);
                    byte[] digest = md.digest();
                    Encoder encoder = Base64.getEncoder();
                    String encodedPassword = encoder.encodeToString(digest);
                    existingUser.setPassword(encodedPassword);

                    TestUtils.sendForgotPasswordOnMail(mailSession, username, password);

                    return Response.status(Response.Status.OK).build();

                } else {
                    return Response.status(Response.Status.NOT_MODIFIED).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }

    }

    @Override
    public Response getUserByUsername(String username, TestUsers user) {
        try {
            Query query = em.createQuery("Select u FROM TestUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            TestUsers existingUser = (TestUsers) accountExists;

            if ((existingUser instanceof TestUsers) 
                    && (existingUser.getIsActive()).equals("yes")) {
                existingUser.setIsPasswordChangeRequest("yes");
                if ((user.getPassword()).equals(user.getRetypePassword())) {
                    String encodedSalt = existingUser.getSalt();

                    Decoder decoder = Base64.getDecoder();

                    byte[] saltInBytes = decoder.decode(encodedSalt);
                    byte[] passwordInBytes = user.getPassword().getBytes();
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    md.update(saltInBytes);
                    md.update(passwordInBytes);
                    byte[] digest = md.digest();

                    if ((existingUser.getIsPasswordChangeRequest()).equals("yes")) {
                        TestUtils.sendChangePasswordConsentLinkOnMail(mailSession, username, digest);
                    }

                    return Response.status(Response.Status.ACCEPTED).build();
                } else {
                    return Response.status(Response.Status.PRECONDITION_FAILED).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }
    }

    @Override
    public void changePassword(String encodedUsername,
            String encodedPassword,
            HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] decodedUsername = decoder.decode(encodedUsername);
            String username = new String(decodedUsername);

            Query query = em.createQuery("Select u FROM TestUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            TestUsers existingUser = (TestUsers) accountExists;
            if ((existingUser instanceof TestUsers)
                    && (existingUser.getIsActive()).equals("yes")
                    && (existingUser.getIsPasswordChangeRequest()).equals("yes")) {
                if (encodedPassword.contains("_")) {
                    encodedPassword = manipulateEncodedPassword(encodedPassword, "_", "/");
                }
                existingUser.setPassword(encodedPassword);
                existingUser.setIsPasswordChangeRequest("no");
                res.sendRedirect(req.getContextPath() + "/login.xhtml?passwordChanged=true");
            } else if ((existingUser.getIsPasswordChangeRequest()).equals("no")) {                
                res.sendRedirect(req.getContextPath() + "/login.xhtml?passwordAlreadyChanged=true");
            } else {                
                res.sendRedirect(req.getContextPath() + "/login.xhtml?account=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
