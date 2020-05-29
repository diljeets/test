/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.test.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author diljeet
 */
@Entity
@Table(name="family")
@NamedQuery(name = "getAllFamilyMembers",
        query = "SELECT f FROM Family f " + 
                    "ORDER BY f.id"
        )
//@NamedQueries({
//    @NamedQuery(name = "getAllFamilyMembers",
//        query = "SELECT f FROM Family f " + 
//                    "ORDER BY f.id"
//        ),
//    @NamedQuery(name = "updateFamilyMembers",
//        query = "UPDATE Family f SET f.name = :name, f.fathersName = :fathersName, f.mothersName = :mothersName, f.address = :address WHERE f.id = :id"
//        )
//})
@XmlRootElement(name="family")
@XmlAccessorType(XmlAccessType.FIELD)
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
//    @Id
//    @GeneratedValue(
//        strategy = GenerationType.SEQUENCE,
//        generator = "sequence-generator"
//    )
//    @SequenceGenerator(
//        name = "sequence-generator",
//        sequenceName = "the_sequence_name"
//    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @XmlAttribute(required = true)
    private Long id;    
    
    @NotEmpty
    @NotBlank
    @XmlAttribute(required = true)
    private String name;    
    
    @NotEmpty
    @NotBlank
    @Column(name = "fathers_name")
    @XmlAttribute(required = true)
    private String fathersName;
    
    @NotEmpty
    @NotBlank
    @Column(name = "mothers_name")
    @XmlAttribute(required = true)
    private String mothersName;
    
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    //@XmlAttribute(required = true)
    private Address address;

    public Family() {        
        address = new Address();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }    

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
        if (!(object instanceof Family)) {
            return false;
        }
        Family other = (Family) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diljeet.test.entity.Family[ id=" + id + " ]";
    }
    
}
