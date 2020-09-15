/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author diljeet
 */
public class TestUtils {

    private static final Logger logger = Logger.getLogger(TestUtils.class.getCanonicalName());

//    @Resource(lookup = "java:jboss/mail/gmailSession")
//    public Session mailSession;
    public TestUtils() {
    }

    public static void sendActivationLinkOnMail(Session mailSession, String username) {
        try {

            Encoder encoder = Base64.getEncoder();
            String encodedUsername = encoder.encodeToString(username.getBytes());

            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Account Activation");
            m.setSentDate(new java.util.Date());
//            m.setContent("Thank you for registering with us.<br/>"
//                    + "Kindly click on the below link to activate your account.<br/>"
//                    + "<a href='http://localhost:8080/test/webapi/TestUsers?username="+username+"'>http://localhost:8080/test/webapi/TestUsers?username="+username+"</a>", "text/html");
            m.setContent("Thank you for registering with us.<br/>"
                    + "Kindly click on the below link to activate your account.<br/>"
                    + "<a href='http://localhost:8080/test/webapi/TestUsers/activate-account/" + encodedUsername + "'>http://localhost:8080/test/webapi/TestUsers/activate-account/" + encodedUsername + "</a>", "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void sendForgotPasswordOnMail(Session mailSession, String username, String password) {
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Reset Password");
            m.setSentDate(new java.util.Date());
            m.setContent("Dear User,<br/>"
                    + "Kindly use the below 10 digit password in bold letters to login into your account.<br/>"
                    + "<strong>" + password + "</strong><br/>"
                    + "Post login it is advised you change your password for security reasons.",
                    "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void sendChangePasswordConsentLinkOnMail(Session mailSession, String username, byte[] password) {
        try {

            Encoder encoder = Base64.getEncoder();
            String encodedUsername = encoder.encodeToString(username.getBytes());
            String encodedPassword = encoder.encodeToString(password);

            if (encodedPassword.contains("/")) {
                encodedPassword = manipulateEncodedPassword(encodedPassword, "/", "_");
            }

            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("rayatdiljeet1983@gmail.com");
            Address[] to = new InternetAddress[]{new InternetAddress(username)};

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("DO NOT REPLY: Consent to Change Password");
            m.setSentDate(new java.util.Date());
            m.setContent("Dear Customer;<br/>"
                    + "We have received a Password change Request for your Account.<br/>"
                    + "Kindly click on the below link to proceed with the request.<br/>"
                    + "<a href='http://localhost:8080/test/webapi/TestUsers/change-password/" + encodedUsername + "/" + encodedPassword + "'>http://localhost:8080/test/webapi/TestUsers/change-password/" + encodedUsername + "/" + encodedPassword + "</a>", "text/html");
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String generateForgotPassword() {
        ArrayList<String> passwordList = new ArrayList<>();
        ArrayList<String> replaceByte = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            replaceByte.add(Integer.toString(random.nextInt(10)));
        }
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] >= 35 && bytes[i] <= 38)
                    || (bytes[i] >= 63 && bytes[i] <= 90)
                    || (bytes[i] >= 97 && bytes[i] <= 122)) {
                passwordList.add(new String(bytes, i, 1));
//                        logger.log(Level.SEVERE, Byte.toString(bytes[i]));
            } else {
                passwordList.add(replaceByte.get(i));
            }
        }

        StringBuilder password = new StringBuilder();
        Iterator<String> itr = passwordList.iterator();
        while (itr.hasNext()) {
            password.append(itr.next());
        }
//                String utfPassword = new String(bytes, StandardCharsets.UTF_8);
//                logger.log(Level.SEVERE, "UTF Password is {0}", utfPassword);
//                logger.log(Level.SEVERE, "Password is {0}", passwordList.toString());
//                logger.log(Level.SEVERE, "Password is {0}", password);
        return password.toString();
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
//        String encodedSalt = Util.encodeBase16(salt);
        return salt;
    }

    public static String manipulateEncodedPassword(String encodedPassword, CharSequence toReplace, CharSequence replaceWith) {
        for (int i = 0; i < encodedPassword.length(); i++) {
            encodedPassword = encodedPassword.replace(toReplace, replaceWith);
        }
        return encodedPassword;
    }

}
