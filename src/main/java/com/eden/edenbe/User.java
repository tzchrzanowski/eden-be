package com.eden.edenbe;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String profile_picture_url;
    private String creation_date;
    private String last_login_date;
    private Integer parent;
    private Long left_child;
    private Long right_child;
    private int active;
    private int role_id;
    private Integer points;
    private String package_type;
    private BigDecimal money_amount;

    public User() {
    }
    /*
    * Constructor of new user
    * */
    public User(
        Long id,
        String username,
        String email,
        String password,
        String first_name,
        String last_name,
        String profile_picture_url,
        int active,
        int role_id,
        Long left_child,
        Long right_child,
        int parent,
        int points,
        String package_type,
        BigDecimal new_money_amount
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_picture_url = profile_picture_url;
        this.active = active;
        this.role_id = role_id;
        this.left_child = left_child;
        this.right_child = right_child;
        this.parent = parent;
        this.points = points;
        this.package_type = package_type;
        this.money_amount = new_money_amount.setScale(2, RoundingMode.HALF_UP);
    }

    /*
    * Getters
    * */
    public String getFirst_name() {
        return this.first_name;
    }
    public String getLast_name() {
        return this.last_name;
    }
    public String getProfile_picture_url() {
        return this.profile_picture_url;
    }
    public String getCreation_date() {
        return this.creation_date;
    }
    public String getLast_login_date() {
        return this.last_login_date;
    }
    public int getRole_id() {
        return this.role_id;
    }
    public int getIsAccountActive() {
        return this.active;
    }
    public Long getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public int getParent() {
        if (this.parent == null) {
            return -1;
        } else {
            return this.parent;
        }
    }
    public Long getLeft_child() {
        if (this.left_child == null) {
            Long minusOne = -1l;
            return minusOne;
        } else {
            return this.left_child;
        }
    }
    public Long getRight_child() {
        if (this.right_child == null) {
            Long minusOne = -1l;
            return minusOne;
        } else {
            return this.right_child;
        }
    }
    public Integer getPoints() {
        return this.points;
    }

    public String getPackageType() {
        return this.package_type;
    }
    public BigDecimal getMoney_amount() { return this.money_amount; }

    /*
    * Setters:
    * */
    public void setPassword(String pwd) {
        this.password = pwd;
    }
    public void setFirst_name(String name) {
        this.first_name = name;
    }
    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }
    public void setProfile_picture_url(String newUrl) {
        this.profile_picture_url = newUrl;
    }
    public void setCreation_date(String newDate) {
        this.creation_date = newDate;
    }
    public void setLast_login_date(String newDate) {
        this.last_login_date = newDate;
    }
    public void setActive(int newActiveState) {
        this.active = newActiveState;
    }
    public void setRole_id(int newRoleId) {
        this.role_id = newRoleId;
    }
    public void setId(Long newId) {
        this.id = newId;
    }
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }
    public void setParent(int newParent) { this.parent = newParent;}
    public void setLeft_child(Long newLeftChildId) {this.left_child = newLeftChildId; }
    public void setRight_child(Long newRightChildId) {this.right_child = newRightChildId; }
    public void setPoints(Integer newPoints) {
        this.points = newPoints;
    }
    public void setPackageType(String newPackageType) {
        this.package_type = newPackageType;
    }
    public void setMoney_amount(BigDecimal new_money_amount) { this.money_amount = new_money_amount.setScale(2, RoundingMode.HALF_UP); }
}
