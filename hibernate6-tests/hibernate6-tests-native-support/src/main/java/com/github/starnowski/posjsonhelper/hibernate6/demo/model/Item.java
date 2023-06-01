package com.github.starnowski.posjsonhelper.hibernate6.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "item")
public class Item {

    @Id
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "jsonb_content", columnDefinition = "jsonb")
//    @Type(value = MyJsonType.class)
    private JsonbContent jsonbContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonbContent getJsonbContent() {
        return jsonbContent;
    }

    public void setJsonbContent(JsonbContent jsonbContent) {
        this.jsonbContent = jsonbContent;
    }
}
