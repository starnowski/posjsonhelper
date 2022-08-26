package com.github.starnowski.posjsonhelper.poc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    @Column(name = "payment_jsonb_data", columnDefinition = "jsonb")
    private String paymentJsonData;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPaymentJsonData() {
        return paymentJsonData;
    }

    public void setPaymentJsonData(String paymentJsonData) {
        this.paymentJsonData = paymentJsonData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
