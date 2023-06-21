package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import org.hibernate.annotations.Struct;

import java.io.Serializable;
import java.util.List;

@Embeddable
@Struct(name = "my_json_content")
public class JsonbContent implements Serializable{

//    @Transient
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(name = "top_element_with_set_of_values")
//    private List<String> top_element_with_set_of_values;
//    private String[] top_element_with_set_of_values;
//    private String top_element_with_set_of_values;
    private Integer integer_value;
    private Double double_value;
    @Enumerated(EnumType.STRING)
    private UserTypeEnum enum_value;
    private String string_value;
//    private Child child;

//    public String getTop_element_with_set_of_values() {
//        return top_element_with_set_of_values;
//    }
//
//    public JsonbContent setTop_element_with_set_of_values(String top_element_with_set_of_values) {
//        this.top_element_with_set_of_values = top_element_with_set_of_values;
//        return this;
//    }

//    public JsonbContent setTop_element_with_set_of_values(String[] top_element_with_set_of_values) {
//        this.top_element_with_set_of_values = top_element_with_set_of_values;
//        return this;
//    }

//    public List<String> getTop_element_with_set_of_values() {
//        return top_element_with_set_of_values;
//    }
//
//    public JsonbContent setTop_element_with_set_of_values(List<String> top_element_with_set_of_values) {
//        this.top_element_with_set_of_values = top_element_with_set_of_values;
//        return this;
//    }

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

//    public Child getChild() {
//        return child;
//    }
//
//    public JsonbContent setChild(Child child) {
//        this.child = child;
//        return this;
//    }
}
