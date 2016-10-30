package com.tummsmedia.entities;

import javax.persistence.*;

/**
 * Created by john.tumminelli on 10/29/16.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String phone;

    public User() {
    }

    public User(String username, String password, String phone) {
        this.name = username;
        this.password = password;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
