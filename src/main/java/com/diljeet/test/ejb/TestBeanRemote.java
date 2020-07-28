/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.ejb;

import com.diljeet.test.entity.Family;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author diljeet
 */
@Remote
public interface TestBeanRemote {
    List<Family> getAllFamily();
    Family getFamilyById(String id);
    void deleteFamilyById(String id);
}
