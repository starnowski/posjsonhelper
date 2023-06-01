package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;

//@Embeddable
//@Struct(name = "child")
public class Child implements Serializable{

//    @Transient
    private List<String> pets;
//    private String[] pets;
//    @JdbcTypeCode(SqlTypes.BLOB)
//    private String pets;

//    public String getPets() {
//        return pets;
//    }
//
//    public Child setPets(String pets) {
//        this.pets = pets;
//        return this;
//    }

//    public String[] getPets() {
//        return pets;
//    }
//
//    public Child setPets(String[] pets) {
//        this.pets = pets;
//        return this;
//    }

    public List<String> getPets() {
        return pets;
    }

    public Child setPets(List<String> pets) {
        this.pets = pets;
        return this;
    }
}
