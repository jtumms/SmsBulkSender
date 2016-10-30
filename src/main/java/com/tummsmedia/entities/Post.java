package com.tummsmedia.entities;

import javax.persistence.*;

/**
 * Created by john.tumminelli on 10/29/16.
 */
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    public String subject;

    @Column(nullable = false)
    public String content;

    @Column(nullable = false)
    public boolean isSent;

    @ManyToOne
    User user;

    public Post() {
    }

    public Post(String subject, String content, User user, boolean isSent) {
        this.subject = subject;
        this.content = content;
        this.user = user;
        this.isSent = isSent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
