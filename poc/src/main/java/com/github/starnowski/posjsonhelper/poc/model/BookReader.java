package com.github.starnowski.posjsonhelper.poc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_reader")
public class BookReader {
    @Id
    private Long id;

    @Column(name = "jsonb_data", columnDefinition = "jsonb")
    private String jsonData;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
