/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.resources;

import com.diljeet.test.ejb.TestUsersBean;
import com.diljeet.test.entity.TestUsers;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
//import org.jboss.security.Base64Encoder;
//import org.jboss.security.auth.spi.Util;
//import org.wildfly.security.WildFlyElytronProvider;
//import org.wildfly.security.asn1.DEREncoder;
//import org.wildfly.security.password.Password;
//import org.wildfly.security.password.PasswordFactory;
//import org.wildfly.security.password.interfaces.ClearPassword;
//import org.wildfly.security.password.interfaces.SaltedSimpleDigestPassword;
//import org.wildfly.security.password.interfaces.SimpleDigestPassword;
//import org.wildfly.security.password.spec.BasicPasswordSpecEncoding;
//import org.wildfly.security.password.spec.ClearPasswordSpec;
//import org.wildfly.security.password.spec.EncryptablePasswordSpec;
//import org.wildfly.security.password.spec.HashPasswordSpec;
//import org.wildfly.security.password.spec.SaltedHashPasswordSpec;
//import org.wildfly.security.password.spec.SaltedPasswordAlgorithmSpec;

/**
 *
 * @author diljeet
 */
@Stateless
//@RolesAllowed("Administrator")
public class TestUsersServiceBean implements TestUsersService {

//    private static final Logger logger = Logger.getLogger(TestUsersServiceBean.class.getCanonicalName());   
//    
//    @Resource
//    TestUsersBean testUserBean;
//
//    @Override
//    public void createUser(TestUsers user) {
//        if(user == null){
//            return;
//        }
//        logger.log(Level.SEVERE, user.getUsername());
//        logger.log(Level.SEVERE, user.getPassword());
//    }
//
//    @Override
//    public List<TestUsers> getUser() {
//        return testUserBean.getUser();
//    }
    private static final Logger logger = Logger.getLogger(TestUsersBean.class.getCanonicalName());

//    static final Provider ELYTRON_PROVIDER = new WildFlyElytronProvider();

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Override
    public Response createUser(TestUsers user) throws Exception {
        logger.log(Level.SEVERE, "inside service createUser method");
        if (user == null) {
            return null;
        } else {

            if (user.getRole() == null) {
                user.setRole("Administrator");
            }

//            if(user.getSalt() == null)
//                user.setSalt(generateSalt());
//            logger.log(Level.SEVERE, "Salt is {0} ", user.getSalt()+user.getPassword());
//            String hashedPassword = Util.createPasswordHash("SHA-512",
//                                                Util.BASE64_ENCODING,
//                                                null,
//                                                null,
//                                                user.getPassword());
//            logger.log(Level.SEVERE, "HashedPassword is {0} ", hashedPassword);
//            user.setPassword(hashedPassword);      
//            PasswordFactory passwordFactory = PasswordFactory.getInstance(SaltedSimpleDigestPassword.ALGORITHM_SALT_PASSWORD_DIGEST_SHA_512, ELYTRON_PROVIDER);
//            byte[] salt = new byte[64];
//            SecureRandom random = new SecureRandom();
//            random.nextBytes(salt);
//
//            SaltedPasswordAlgorithmSpec saltedSpec = new SaltedPasswordAlgorithmSpec(salt);            
//            EncryptablePasswordSpec encryptableSpec = new EncryptablePasswordSpec(user.getPassword().toCharArray(), saltedSpec);
//            ClearPasswordSpec clearSpec = new ClearPasswordSpec(user.getPassword().toCharArray());
//            SaltedSimpleDigestPassword original = (SaltedSimpleDigestPassword) passwordFactory.generatePassword(clearSpec);

//            SaltedSimpleDigestPassword original = (SaltedSimpleDigestPassword) passwordFactory.generatePassword(encryptableSpec);
//            byte[] digest = original.getDigest();
            //byte[] newsalt = original.getSalt();

            //HashPasswordSpec hashSpec = new HashPasswordSpec(digest);
            byte[] salt = generateSalt();
            byte[] password = user.getPassword().getBytes();
//            byte[] saltedPassword = new byte[salt.length + password.length];
//            System.arraycopy(salt, 0, saltedPassword, 0, salt.length);
//            System.arraycopy(password, 0, saltedPassword, salt.length, password.length);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            md.update(password);
            byte[] digest = md.digest();

            Encoder encoder = Base64.getEncoder();

            String encodedSalt = encoder.encodeToString(salt);
            user.setSalt(encodedSalt);

            String encodedPassword = encoder.encodeToString(digest);
            user.setPassword(encodedPassword);

//            SaltedHashPasswordSpec saltedHashSpec = new SaltedHashPasswordSpec(digest, salt);
//
//            SaltedSimpleDigestPassword restored = (SaltedSimpleDigestPassword) passwordFactory.generatePassword(saltedHashSpec);
//
//            System.out.println(String.format("Password Verified '%b'", passwordFactory.verify(restored, user.getPassword().toCharArray())));

        }

        try {
            em.persist(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return Response.status(Response.Status.CREATED).build();
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

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
//        String encodedSalt = Util.encodeBase16(salt);
        return salt;
    }

}
