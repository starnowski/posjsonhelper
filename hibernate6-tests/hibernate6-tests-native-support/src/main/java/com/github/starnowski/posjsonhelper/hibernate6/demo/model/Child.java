package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import java.io.Serializable;
import java.util.List;

public class Child implements Serializable {

    private List<String> pets;

    public List<String> getPets() {
        return pets;
    }

    public Child setPets(List<String> pets) {
        this.pets = pets;
        return this;
    }
}
