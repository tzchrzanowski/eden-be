package com.eden.edenbe;

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
}
