package com.eden.edenbe;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String profile_picture_url;
    private Integer parent;
    private Long left_child;
    private Long right_child;
    private String package_type;
    private Integer points;
    private BigDecimal money_amount;
    private int role_id;
    private Integer direct_referral;

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
    public Long getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail() { return this.email; }
    public int getParent() { return this.parent; }
    public Long getLeft_child() {return this.left_child; }
    public Long getRight_child() {return this.right_child;}
    public Integer getPoints() { return this.points; }
    public String getPackageType() { return this.package_type; }
    public BigDecimal getMoney_amount() { return this.money_amount; }
    public int getRole_id() {
        return this.role_id;
    }
    public Integer getDirect_referral() { return this.direct_referral; }

    /*
    * Setters:
    * */
    public void setFirst_name(String name) {
        this.first_name = name;
    }
    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }
    public void setProfile_picture_url(String newUrl) {
        this.profile_picture_url = newUrl;
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
    public void setRole_id(int newRoleId) {
        this.role_id = newRoleId;
    }
    public void setDirect_referral(Integer direct_referral) { this.direct_referral = direct_referral; }
}
