/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.entity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
//import org.jboss.security.auth.spi.Util;




/**
 *
 * @author diljeet
 */
@Table(name = "testUsers")
@Entity
@NamedQuery(name = "getAllUsers",
        query = "Select u from TestUsers u")
public class TestUsers implements Serializable {
    
    private static final Logger logger = Logger.getLogger(TestUsers.class.getCanonicalName());

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @NotEmpty
    @Email(message = "Not a valid Email Id.")
    private String username;
    
//    @Pattern(regexp = "^[A-Za-z0-9]+$",message="invalid password")
//    @NotBlank
//    @NotEmpty
    
    @NotNull
    @Size(min = 8 , max = 15 , message = "Password can be 8-15 characters in length")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%&]+$" , message="invalid password")
    private String password;    
    
    @Transient
    private String retypePassword;
    
    private String role;
    
    private String salt;   
    
    private String isActive;
    
    private String isPasswordChangeRequest;

//    @PostConstruct
//    public void init(){
//        logger.log(Level.SEVERE, "TestUsers Entity init method");
//    }
    public TestUsers() {        
//        logger.log(Level.SEVERE, "TestUsers Entity Constructor");
    }       

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
//        SecureRandom random = new SecureRandom();
//        MessageDigest md = null;
//        try {
//            //byte[] salt = new byte[16];
//            //random.nextBytes(salt);
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(TestUsers.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //md.update(salt);
//        this.password = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
//        this.password = Util.createPasswordHash("MD5",
//                                                Util.BASE64_ENCODING,
//                                                null,
//                                                null,
//                                                password);
//try {
//            // Create MessageDigest instance for MD5
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            //Add password bytes to digest
//            md.update(password.getBytes());
//            //Get the hash's bytes 
//            byte[] bytes = md.digest();
//            //This bytes[] has bytes in decimal format;
//            //Convert it to hexadecimal format
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            //Get complete hashed password in hex format
//            this.password  = sb.toString();
//        } 
//        catch (NoSuchAlgorithmException e) 
//        {
//            e.printStackTrace();
//        }
//        System.out.println(this.password);
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }    

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsPasswordChangeRequest() {
        return isPasswordChangeRequest;
    }

    public void setIsPasswordChangeRequest(String isPasswordChangeRequest) {
        this.isPasswordChangeRequest = isPasswordChangeRequest;
    }    
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestUsers)) {
            return false;
        }
        TestUsers other = (TestUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.test.entity.TestUsers[ id=" + id + " ]";
    }
    
}
