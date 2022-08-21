package com.github.starnowski.posjsonhelper.poc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

    @Id
    private Long id;

    @Column(name = "jsonb_content", columnDefinition = "jsonb")
    private String jsonbContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonbContent() {
        return jsonbContent;
    }

    public void setJsonbContent(String jsonbContent) {
        this.jsonbContent = jsonbContent;
    }
}
