package demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


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
