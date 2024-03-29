package com.github.starnowski.posjsonhelper.text.hibernate6.model;


import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "tweet_with_locale")
public class TweetWithLocale {
    @Id
    private Long id;
    @Column
    private String title;
    @Column(name = "short_content")
    private String shortContent;
    @Column
    private String locale;
//    @Transient
    @Formula("title || ' ' || short_content")
    private String titleAndShortContent;

    public Long getId() {
        return id;
    }

    public TweetWithLocale withId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TweetWithLocale withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getShortContent() {
        return shortContent;
    }

    public TweetWithLocale withShortContent(String shortContent) {
        this.shortContent = shortContent;
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public TweetWithLocale withLocale(String locale) {
        this.locale = locale;
        return this;
    }
}
