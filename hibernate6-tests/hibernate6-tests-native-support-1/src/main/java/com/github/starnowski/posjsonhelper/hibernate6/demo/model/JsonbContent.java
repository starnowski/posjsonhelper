package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import org.hibernate.annotations.Struct;

import java.io.Serializable;
import java.util.List;

@Embeddable
public class JsonbContent implements Serializable{

    private Integer integer_value;
    private Double double_value;
    @Enumerated(EnumType.STRING)
    private UserTypeEnum enum_value;
    private String string_value;
    public Integer getInteger_value() {
        return integer_value;
    }

    public JsonbContent setInteger_value(Integer integer_value) {
        this.integer_value = integer_value;
        return this;
    }

    public Double getDouble_value() {
        return double_value;
    }

    public JsonbContent setDouble_value(Double double_value) {
        this.double_value = double_value;
        return this;
    }

    public UserTypeEnum getEnum_value() {
        return enum_value;
    }

    public JsonbContent setEnum_value(UserTypeEnum enum_value) {
        this.enum_value = enum_value;
        return this;
    }

    public String getString_value() {
        return string_value;
    }

    public JsonbContent setString_value(String string_value) {
        this.string_value = string_value;
        return this;
    }
}
